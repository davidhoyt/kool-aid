#!/usr/bin/env bash

curr=$(pwd -P)
cd $(cd -P -- "$( dirname -- "${BASH_SOURCE[0]}")" && pwd -P)
cd ../..

mkdir -p /tmp/steps

sbt "koan next" > /tmp/steps/last.out
git clean -fd >> /tmp/steps/last.out

cd "$curr"
