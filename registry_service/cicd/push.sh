#!/bin/sh
set -e

VERSION=$1

echo "Push image to registry server"
docker --config ~/.docker/.dinhnc7 push 10.60.156.72/its/its-registry-service:$VERSION
docker rmi 10.60.156.72/its/its-registry-service:$VERSION
echo "Finish push image to registry server"
