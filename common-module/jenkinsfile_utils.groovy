import groovy.json.JsonOutput
import groovy.util.XmlSlurper


@SuppressWarnings("GrMethodMayBeStatic")
@NonCPS
def parseXml(xmlString) {
    def xmlParser = new XmlSlurper()
    xmlParser.setFeature("http://apache.org/xml/features/disallow-doctype-decl", false)
    xmlParser.setFeature("http://apache.org/xml/features/nonvalidating/load-external-dtd", false)
    return xmlParser.parseText(xmlString)
}

@SuppressWarnings("GrMethodMayBeStatic")
@NonCPS
def jsonParse(def jsonString) {
    new groovy.json.JsonSlurperClassic().parseText(jsonString)
}

def toJSONString(data) {
    return JsonOutput.toJson(data)
}
//functions check event source code
def checkoutSourceCode(checkoutType){
    if (checkoutType == "PUSH"){
        checkout changelog: true, poll: true, scm: [
            $class                           :  'GitSCM',
            branches                         : [[name: "${env.gitlabAfter}"]],
            doGenerateSubmoduleConfigurations: false,
            extensions                       : [[$class: 'UserIdentity',
                                                 email : 'vuonghv5@viettel.com.vn', name: 'vuonghv5'],
                                                [$class: 'CleanBeforeCheckout']],
            submoduleCfg                     : [],
            userRemoteConfigs                : [[credentialsId: "094aae41-3d61-483e-a122-265c3f4ce738",
                                                 url          : "${env.gitlabSourceRepoHomepage}" +".git"]]
        ]
    } else if (checkoutType == "MERGE") {
        checkout changelog: true, poll: true, scm: [
            $class                           : 'GitSCM',
            branches                         : [[name: "origin/${env.gitlabSourceBranch}"]],
            doGenerateSubmoduleConfigurations: false,
            extensions                       : [[$class : 'PreBuildMerge',
                                                 options: [
                                                     fastForwardMode: 'FF',
                                                     mergeRemote    : 'origin',
                                                     mergeStrategy  : 'RESOLVE',
                                                     mergeTarget    : "${env.gitlabTargetBranch}"
                                                 ]],
                                                [$class: 'UserIdentity',
                                                    email : 'vuonghv5@viettel.com.vn', name: 'vuonghv5'],
                                                [$class: 'CleanBeforeCheckout']],
            submoduleCfg                     : [],
            userRemoteConfigs                : [[credentialsId: "094aae41-3d61-483e-a122-265c3f4ce738",
                                                 url          : "${env.gitlabSourceRepoHomepage}" + ".git"]]
        ]
    }
}
// functions lấy thông tin member project
def getProjectMember(notifyMemberLevel){
    def project_members = []
    def project_member_notify = []
     withCredentials([usernamePassword(credentialsId: 'a5eedd9f-332d-4575-9756-c358bbd808eb', usernameVariable: 'user',
              passwordVariable: 'password')]){
        def currentPage = 1
        haveNextPage = true
        while (haveNextPage) {
            def response = httpRequest([
                acceptType   : 'APPLICATION_JSON',
                httpMode     : 'GET',
                contentType  : 'APPLICATION_JSON',
                customHeaders: [[name: 'Private-Token', value: password ]],
                url          : "${env.GITLAB_PROJECT_API_URL}/members/all?per_page=100&page=${currentPage}"
            ])

            def project_members_resp = jenkinsfile_utils.jsonParse(response.content)
            project_members.addAll(project_members_resp)
            if (project_members_resp.size() == 0) {
                haveNextPage = false
            } else {
                currentPage += 1
            }
        }
    }
    for (member in project_members) {
        if (member['access_level'].toInteger() >= notifyMemberLevel) {
            if (!project_member_notify.contains(member['username'])) {
                project_member_notify.add(member['username'])
            }
        }
    }
    return project_member_notify
}
def getServiceList(pomXMLStr, notServiceModuleList) {
    def serviceList = []
    def pomXml = parseXml(pomXMLStr)
    pomXml.modules[0].module.each {
        if (checkModuleIsService(it.text(), notServiceModuleList))
            serviceList.add(it.text())
    }
    return serviceList
}

def checkModuleIsService(String moduleName,notServiceModuleList) {
    isService = true
    for (notServiceModule in notServiceModuleList) {
        echo "${notServiceModule}"
        if (moduleName == notServiceModule) {
            isService = false
        }
    }
    echo "is service: ${isService}"
    return isService
}
def boolean hasChangesIn(branch, buildType, String module) {
    if(buildType == "PUSH"){
        if (branch == null) {
            return true;
        }
        def MASTER = sh(
            returnStdout: true,
            script: "git rev-parse origin/${branch}"
        ).trim()
        // Gets commit hash of HEAD commit. Jenkins will try to merge master into
        // HEAD before running checks. If this is a fast-forward merge, HEAD does
        // not change. If it is not a fast-forward merge, a new commit becomes HEAD
        // so we check for the non-master parent commit hash to get the original
        // HEAD. Jenkins does not save this hash in an environment variable.
        def HEAD = sh(
            returnStdout: true,
            script: "git show -s --no-abbrev-commit --pretty=format:%P%n%H%n HEAD | tr ' ' '\n' | grep -v ${MASTER} | head -n 1"
        ).trim()

        return sh(
            returnStatus: true,
            script: "git diff --name-only ${MASTER} ${HEAD} | grep ^${module}/"
        ) == 0
    } else if (buildType == "MERGE"){
        echo "Start test MERGE"
        echo " env.sourceBranchCommitID : $env.sourceBranchCommitID"
        echo "env.targetBranchCommitID: $env.targetBranchCommitID"
        def check = sh(
            returnStatus: true,
            script: "git diff --name-only ${env.sourceBranchCommitID} ${env.targetBranchCommitID} | grep ^${module}/"
        ) == 0
        println("Check change: $check")
        return check
    }

}
@NonCPS
def getChangSet(){
    def listServiceChange = []
    def changeLogSets = currentBuild.changeSets
        for (int i = 0; i < changeLogSets.size(); i++) {
                def entries = changeLogSets[i].items
                for (int j = 0; j < entries.length; j++) {
                    def entry = entries[j]
                    // echo "${entry.commitId} by ${entry.author} on ${new Date(entry.timestamp)}: ${entry.msg}"
                    def files = new ArrayList(entry.affectedFiles)
                    for (int k = 0; k < files.size(); k++) {
                        def file = files[k]
                        // echo "filePath:  ${file.editType.name} ${file.path}"
                        listServiceChange.add(file.path)
                    }
                }
        }

    return listServiceChange
}
def getListRebuild(String module){
    def listServiceChange = []
    listServiceChange = getChangSet()
    def isRebuild = false
    println("listChange: $listServiceChange")
    for(def i = 0; i < listServiceChange.size(); i++){
        println("listServiceRebuild $i:$listServiceChange[i]")
        if(listServiceChange[i].contains(module)){
            isRebuild = true
        }
    }
    return isRebuild
}
return [
    parseXml                           : this.&parseXml,
    jsonParse                          : this.&jsonParse,
    toJSONString                       : this.&toJSONString,
    checkoutSourceCode                 : this.&checkoutSourceCode,
    getProjectMember                   : this.&getProjectMember,
    hasChangesIn                       : this.&hasChangesIn,
    getChangSet                        : this.&getChangSet,
    getListRebuild                     : this.&getListRebuild
]
