#!/usr/bin/env bash

curr=$(pwd -P)
cd $(cd -P -- "$( dirname -- "${BASH_SOURCE[0]}")" && pwd -P)
cd ../..

. "bin/navigate/git.sh"

first_step

cd "$curr"
