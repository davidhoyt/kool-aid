#!/bin/bash -e

export JAVA_HOME=/Library/Java/JavaVirtualMachines/jdk1.8.0_45.jdk/Contents/Home

curr=$(pwd -P)
cd $(cd -P -- "$( dirname -- "${BASH_SOURCE[0]}")" && pwd -P)
cd ..

sbt "$@"

cd "$curr"
