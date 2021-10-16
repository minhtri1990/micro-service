#!/bin/bash
set -e

VERSION=$1

echo "Start build maven"
mvn -U clean install -Dmaven.test.skip=true
echo "Finish build maven"

echo "Start build docker"
docker build -t 10.60.156.72/its/its-highway-service:$VERSION .
echo "Finish build docker"
