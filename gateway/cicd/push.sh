#!/bin/sh
set -e

VERSION=$1

echo "Push image to gateway server"
docker --config ~/.docker/.dinhnc7 push 10.60.156.72/its/its-gateway:$VERSION
docker rmi 10.60.156.72/its/its-gateway:$VERSION
echo "Finish push image to gateway server"
