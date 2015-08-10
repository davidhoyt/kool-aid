#!/usr/bin/env bash

curr=$(pwd -P)
cd $(cd -P -- "$( dirname -- "${BASH_SOURCE[0]}")" && pwd -P)
cd ../..

mkdir -p "/tmp/steps/"

git reset --hard > /tmp/steps/last.out
sbt "koan prev" >> /tmp/steps/last.out
git clean -fd >> /tmp/steps/last.out

cd "$curr"
