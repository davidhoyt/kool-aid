#!/usr/bin/env bash

# grep_commit_message="step:initial"

curr=$(pwd -P)
cd $(cd -P -- "$( dirname -- "${BASH_SOURCE[0]}")" && pwd -P)
cd ../..

# initial_commit=$(git log --pretty=format:"%H" --all --grep="$grep_commit_message" | head -n 1)

mkdir -p "/tmp/steps/"

sbt "koan init" > /tmp/steps/last.out
git clean -fd >> /tmp/steps/last.out

cd "$curr"
