import org.jenkinsci.plugins.workflow.steps.FlowInterruptedException
import java.text.DecimalFormat
import hudson.tasks.test.AbstractTestResultAction
import groovy.json.*


//functions scan source code
def getSonarQubeAnalysisResult(sonarQubeURL, projectKey) {
    def metricKeys = "bugs,vulnerabilities,code_smells"
    def measureResp = httpRequest([
        acceptType : 'APPLICATION_JSON',
        httpMode   : 'GET',
        contentType: 'APPLICATION_JSON',
        url        : "${sonarQubeURL}/api/measures/component?metricKeys=${metricKeys}&component=${projectKey}"
    ])
    def measureInfo = jenkinsfile_utils.jsonParse(measureResp.content)
    def metricResultList = measureInfo['component']['measures']
    echo "${metricResultList}"
    int bugsEntry = getMetricEntryByKey(metricResultList, "bugs")['value'] as Integer
    int vulnerabilitiesEntry = getMetricEntryByKey(metricResultList, "vulnerabilities")['value'] as Integer
    int codeSmellEntry = getMetricEntryByKey(metricResultList, "code_smells")['value'] as Integer
    return ["bugs": bugsEntry, "vulnerabilities": vulnerabilitiesEntry, "code_smells" : codeSmellEntry ]
}

def getMetricEntryByKey(metricResultList, metricKey) {
    for (metricEntry in metricResultList) {
        if (metricEntry["metric"] == metricKey) {
            echo "${metricEntry}"
            return metricEntry
        }
    }
    return null
}

@NonCPS
def genSonarQubeProjectKey() {
    def sonarqubeProjectKey = ""
    if ("${env.gitlabActionType}".toString() == "PUSH" || "${env.gitlabActionType}".toString() == "TAG_PUSH") {
        sonarqubeProjectKey = "${env.gitlabSourceRepoName}:${env.gitlabSourceBranch}"
    } else if ("${env.gitlabActionType}".toString() == "MERGE" || "${env.gitlabActionType}".toString() == "NOTE") {
        sonarqubeProjectKey = "MR-${env.gitlabSourceRepoName}:${env.gitlabSourceBranch}-to-" +
            "${env.gitlabTargetBranch}"
    }
    return sonarqubeProjectKey.replace('/', '-')
}

@NonCPS
def getTestResultFromJenkins() {
    def testResult = [:]
    AbstractTestResultAction testResultAction = currentBuild.rawBuild.getAction(AbstractTestResultAction.class)
    testResult["total"] = testResultAction.totalCount
    testResult["failed"] = testResultAction.failCount
    testResult["skipped"] = testResultAction.skipCount
    testResult["passed"] = testResultAction.totalCount - testResultAction.failCount - testResultAction.skipCount
    return testResult
}

@NonCPS
def getProjectCodeCoverageInfo(coverageInfoXmlStr) {
    def coverageInfoXml = jenkinsfile_utils.parseXml(coverageInfoXmlStr)
    def coverageInfoStr = ""
    coverageInfoXml.counter.each {
        def coverageType = it.@type as String
        int missed = (it.@missed as String) as Integer
        int covered = (it.@covered as String) as Integer
        int total = missed + covered

        def coveragePercent = 0.00
        if (total > 0) {
            coveragePercent = Double.parseDouble(
                new DecimalFormat("###.##").format(covered * 100.0 / total))
        }
        coverageInfoStr += "- <b>${coverageType}</b>: <i>${covered}</i>/<i>${total}</i> (<b>${coveragePercent}%</b>)<br/>"
    }
    return coverageInfoStr
}

def unitTestAndCodeCoverage(buildType){
	echo  'Skip unittest'
}

//
def sonarQubeScan(buildType) {

	echo  'Checkout source code'
	jenkinsfile_utils.checkoutSourceCode(buildType)
	def version = readMavenPom([file: "pom.xml"]).getVersion()
	echo  'SonarQube analysis'
	    env.SONAR_QUBE_PROJECT_KEY = genSonarQubeProjectKey()
        withSonarQubeEnv('SONARQ_V6'){
            sh(returnStatus: true, script: 
                "/home/app/server/sonar-scanner/bin/sonar-scanner " +
                "-Dsonar.projectName=${env.SONAR_QUBE_PROJECT_KEY} " +
                "-Dsonar.projectKey=${env.SONAR_QUBE_PROJECT_KEY} " +
				"-Dsonar.projectVersion=${version} " +
                "-Dsonar.java.binaries=. " +
                "-Dsonar.sources=. " +
                "-Dsonar.inclusions=**/*.java " +
                "-Dsonar.exclusions=**/*.zip,**/*.jar,**/*.html,**/R.java,**/build/**,**/target/**,**/.settings/**,**/.mvn/**,**/src/main/resources/static/bower_components/**,**/src/main/resources/static/jquery-validation/**,**/src/main/resources/static/scripts/material/**,**/src/main/resources/static/scripts/multiselect/**,**/src/main/resources/static/scripts/nanobar/**,**/src/main/resources/static/scripts/plugins/**,**/src/main/resources/static/scripts/utils/**"
            )
                sh 'ls -al'
                sh 'cat .scannerwork/report-task.txt'
                def props = readProperties file: '.scannerwork/report-task.txt'
                env.SONAR_CE_TASK_ID = props['ceTaskId']
                env.SONAR_PROJECT_KEY = props['projectKey']
                env.SONAR_SERVER_URL = props['serverUrl']
                env.SONAR_DASHBOARD_URL = props['dashboardUrl']

                echo "SONAR_SERVER_URL: ${env.SONAR_SERVER_URL}"
                echo "SONAR_PROJECT_KEY: ${env.SONAR_PROJECT_KEY}"
                echo "SONAR_DASHBOARD_URL: ${env.SONAR_DASHBOARD_URL}"
    }
	echo  'Quality Gate'
	def qg = null
	try {
		def sonarQubeRetry = 0
		def sonarScanCompleted = false
		while (!sonarScanCompleted) {
			try {
				sleep 10
				timeout(time: 1, unit: 'MINUTES') {
					script {
						qg = waitForQualityGate()
						sonarScanCompleted = true
						if (qg.status != 'OK') {
							if (env.bypass == 'true') {
								echo "Sonar contain error"
							}else {
								error "Pipeline failed due to quality gate failure: ${qg.status}"
							}
						}
					}
				}
			} catch (FlowInterruptedException interruptEx) {
				// check if exception is system timeout
				if (interruptEx.getCauses()[0] instanceof org.jenkinsci.plugins.workflow.steps.TimeoutStepExecution.ExceededTimeout) {
					if (sonarQubeRetry <= 10) {
						sonarQubeRetry += 1
					} else {
						if (env.bypass == 'true') {
							echo "Sonar contain error"
						} else {
							error "Cannot get result from Sonarqube server. Build Failed."
						} 
					}
				} else {
					throw interruptEx
				}
			}
			catch (err) {
				throw err
			}
		}
	}
	catch (err) {
		throw err
	} finally {
		def codeAnalysisResult = getSonarQubeAnalysisResult(env.SONAR_SERVER_URL, env.SONAR_PROJECT_KEY)
		def sonarQubeAnalysisStr = "- Vulnerabilities: <b>${codeAnalysisResult["vulnerabilities"]}</b> <br/>" +
			"- Bugs: <b>${codeAnalysisResult["bugs"]}</b> <br/>" +
			"- Code Smell: <b>${codeAnalysisResult["code_smells"]}</b> <br/>"
		def sonarQubeAnalysisComment = "<b>SonarQube Code Analysis Result: ${qg.status}</b> <br/><br/>${sonarQubeAnalysisStr} " +
			"<i><a href='${SONAR_DASHBOARD_URL}'>" +
			"Details SonarQube Code Analysis Report...</a></i><br/><br/>"
		env.SONAR_QUBE_SCAN_RESULT_STR = sonarQubeAnalysisComment
		if ("${env.gitlabActionType}".toString() == "MERGE" || "${env.gitlabActionType}".toString() == "NOTE") {
			echo "check vulnerabilities, code smell and bugs"
			int maximumAllowedVulnerabilities = env.MAXIMUM_ALLOWED_VUNERABILITIES as Integer
			int maximumAllowedBugs = env.MAXIMUM_ALLOWED_BUGS as Integer
			int maximumAllowedCodeSmell = env.MAXIMUM_ALLOWED_CODE_SMELL as Integer
			echo "maximum allow vulnerabilities:  ${maximumAllowedVulnerabilities} "
			echo "maximum allow bugs:  ${maximumAllowedBugs}"
			echo "maximum allow code smell:  ${maximumAllowedCodeSmell}"
			if (codeAnalysisResult["vulnerabilities"] > maximumAllowedVulnerabilities ||
				codeAnalysisResult["bugs"] > maximumAllowedBugs || codeAnalysisResult["code_smells"] > maximumAllowedCodeSmell) {
				if (env.bypass == 'true') {
					echo "Vulnerability, code smell or bug number overs allowed limits!"
				} else {
					error "Vulnerability, code smell or bug number overs allowed limits!"
				} 
				
			}
		}
	}
}
/*
    - Build all module.
    - change module to build in def buildService
*/
def buildProject(buildType, version) {
	echo	"Build version: ${version}"
	echo  'Checkout source code'
	jenkinsfile_utils.checkoutSourceCode(buildType)
	
	echo  'Run build script'
//	def pomVersion = readMavenPom([file: "pom.xml"]).getVersion()
//	def version = "${pomVersion}_u${BUILD_NUMBER}"
	
	sh """
			sh cicd/build.sh ${version}
        """
		
	echo  'Login to Docker Repository'
	withCredentials([usernamePassword(credentialsId: 'dinhnc7', usernameVariable: 'username',
                passwordVariable: 'password')]){
            sh """
                docker --config ~/.docker/.dinhnc7 login -u ${username} -p ${password} 10.60.156.72
            """
    }
	 
	echo  'Run push script'
	sh """
			sh cicd/push.sh ${version}
        """
}

def release2k8s(buildType, version, releaseType){
	
	echo  'Checkout sourcecode'
	jenkinsfile_utils.checkoutSourceCode(buildType)
    
	echo  'Wating...'
	sleep(5) 
	
	echo  'Run deploy script'
//	def pomVersion = readMavenPom([file: "pom.xml"]).getVersion()
//	def version = "${pomVersion}_u${BUILD_NUMBER}"
	
    sh """
		sh cicd/deploy.sh ${version} ${releaseType}
    """
}

/*
    - Config các stage run when push commit
    - SonarQube
    - Build
    - Deploy
*/
def buildPushCommit() {
    echo "gitlabBranch: $env.gitlabBranch"

	def checkSourceCodeTask = [:]

	def pomVersion = readMavenPom([file: "pom.xml"]).getVersion()
	def version = "b${env.gitlabBranch}_${pomVersion}_u${BUILD_NUMBER}"
	
    checkSourceCodeTask['2. SonarQubeScan'] = {
		stage("2. SonarQubeScan") {
			node("node_cicd") {
				sonarQubeScan("PUSH")
			}
		}
    }
	
    checkSourceCodeTask['3. Unittest And Code Coverage'] = {
		stage("3. Unittest And Code Coverage") {
			node("node_cicd") {
				unitTestAndCodeCoverage("PUSH")
			}
		}
    }
	
	parallel checkSourceCodeTask

	if (env.gitlabBranch == env.STAGING_BRANCH) {
		stage("4. Package and build docker image") {
			node("node_cicd") {
				buildProject("PUSH", version)
			}
		}

		stage("5. Deploy to Server Staging") {
			node("node_cicd") {
				release2k8s("PUSH", version, "1")
			}
		}

		def testTasks = [:]

		testTasks['6. Automations testing in Staging'] = {
			stage("6. Automations testing in Staging") {
				node("node_cicd") {
					echo "Skip autonation test"
				}
			}
		}

		testTasks['7. Performance test in Staging'] = {
			stage("7. Performance test in Staging") {
				node("node_cicd") {
					echo "Skip Performance test"
				}
			}
		}

		parallel testTasks

	}else {

		stage("4. Package and build docker image") {
			node("node_cicd") {
				buildProject("PUSH", version)
			}
		}

		stage("5. Deploy to Server Test") {
			node("node_cicd") {
				release2k8s("PUSH", version, "0")
			}
		}

		stage("6. Automations testing in Test") {
			node("node_cicd") {
				echo "Skip autonation test"
			}
		}
	}
	currentBuild.result = "SUCCESS"
}

def buildMergeRequest() {

	echo "gitlabBranch: $env.gitlabBranch"
	echo "gitlabTargetBranch: $env.gitlabTargetBranch"

	def pomVersion = readMavenPom([file: "pom.xml"]).getVersion()
	def version = "b${env.gitlabTargetBranch}_${pomVersion}_u${BUILD_NUMBER}"
	
	def checkSourceCodeTask = [:]
	
    checkSourceCodeTask['2. SonarQubeScan'] = {
		stage("2. SonarQubeScan") {
			node("node_cicd") {
				sonarQubeScan("MERGE")
			}
		}
    }
	
    checkSourceCodeTask['3. Unittest And Code Coverage'] = {
		stage("3. Unittest And Code Coverage") {
			node("node_cicd") {
				unitTestAndCodeCoverage("MERGE")
			}
		}
    }
	
	parallel checkSourceCodeTask
	
	if (env.gitlabTargetBranch == env.STAGING_BRANCH) {
		stage("4. Package and build docker image") {
			node("node_cicd") {
				buildProject("MERGE", version)
			}
		}
		
		stage("5. Deploy to Server Staging") {
			node("node_cicd") {
				release2k8s("MERGE", version, "1")
			}
		}
		
		def testTasks = [:]
	
		testTasks['6. Automations testing in Staging'] = {
			stage("6. Automations testing in Staging") {
				node("node_cicd") {
					echo "Skip autonation test"
				}
			}
		}
		
		testTasks['7. Performance test in Staging'] = {
			stage("7. Performance test in Staging") {
				node("node_cicd") {
					echo "Skip Performance test"
				}
			}
		}
		
		parallel testTasks
		
	}else{
		stage("4. Package and build docker image") {
			node("node_cicd") {
				buildProject("MERGE", version)
			}
		}
		
		stage("5. Deploy to Server Test") {
			node("node_cicd") {
				release2k8s("MERGE", version, "0")
			}
		}
		
		stage("6. Automations testing in Test") {
			echo "Skip autonation test"
		}
	}
	
    currentBuild.result = "SUCCESS"
}


return [
    buildPushCommit         : this.&buildPushCommit,
    buildMergeRequest       : this.&buildMergeRequest,
    buildAcceptAndCloseMR   : this.&buildAcceptAndCloseMR
    //sonarQubeScan			: this.&sonarQubeScan,
    //buildSerrvice           : this.&buildSerrvice,
	//release2k8s             : this.&release2k8s
    //deploy_module_web    : this.&deploy_module_web,
    //packageServicesAndUploadToRepo: this.&packageServicesAndUploadToRepo

]
