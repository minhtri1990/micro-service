#!/bin/sh
set -e

VERSION=$1

echo "Push image to violation server"
docker --config ~/.docker/.dinhnc7 push 10.60.156.72/its/its-violation-service:$VERSION
docker rmi 10.60.156.72/its/its-violation-service:$VERSION
echo "Finish push image to violation server"
