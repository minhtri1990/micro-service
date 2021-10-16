#!/bin/sh
set -e

VERSION=$1
DEPLOY_TYPE=$2
# 0: test, 1: staging

echo "Run its-device-service"
case $DEPLOY_TYPE in
  "0")
    DEPLOY_FILE='cicd/its-test-deployment.yaml'
    CONFIG_FILE='cicd/its-k8s-test-config'
    NAME_SPACE='its'
    ;;
  "1")
    DEPLOY_FILE='cicd/its-staging-deployment.yaml'
    CONFIG_FILE='cicd/its-k8s-staging-config'
    NAME_SPACE='its'
    ;;
esac

echo $DEPLOY_FILE
echo $CONFIG_FILE
echo $NAME_SPACE

sed -i -e "s,__SERVICE_VERSION__,$VERSION,g" $DEPLOY_FILE
sudo kubectl -n $NAME_SPACE apply -f $DEPLOY_FILE --kubeconfig=$CONFIG_FILE

echo  'Waiting for deploy'
sleep 30

echo  'View result deploy'
sudo kubectl -n $NAME_SPACE get pods,svc --kubeconfig=$CONFIG_FILE

echo "Finish run its-device-service"
