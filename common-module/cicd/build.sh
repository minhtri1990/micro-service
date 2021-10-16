#!/bin/sh
set -e
username=$1
pass=$2
echo "mvn clean deploy -Denv.NEXUS_USERNAME=$username -Denv.NEXUS_PASSS=$pass"
mvn clean deploy --settings .settings.xml -Denv.NEXUS_USERNAME=$username -Denv.NEXUS_PASSS=$pass
