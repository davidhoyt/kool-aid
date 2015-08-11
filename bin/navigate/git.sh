#!/usr/bin/env bash

#grep_commit_message="step:initial"

curr=$(pwd -P)
cd $(cd -P -- "$( dirname -- "${BASH_SOURCE[0]}")" && pwd -P)
cd ../..

#initial_commit=$(git log --pretty=format:"%H" --all --grep="$grep_commit_message" | head -n 1)

mkdir -p "/tmp/steps/"

if [ -f ".tag.txt" ]; then
  step=$(cat ".tag.txt")
else
  step="1"
fi

prev_step=$step

function next_step() {
  step=$((step + 1))
  sanitize_step
  persist_step
  cleanup
}

function prev_step() {
  step=$((step - 1))
  sanitize_step
  persist_step
  cleanup
}

function first_step() {
  step=1
  sanitize_step
  persist_step
  cleanup
}

function last_step() {
  i=1
  step=1
  while [  $i -lt 30 ]; do
    if git rev-parse "step-$i" >/dev/null 2>&1 ; then
      step=$i
    fi
    i=$((i + 1))
  done
  sanitize_step
  persist_step
  cleanup
}

function sanitize_step() {
  if [ "$step" -lt 1 ]; then
    step=1
  fi

  # find last tag
  i=1
  last_step=1
  while [  $i -lt 30 ]; do
    if git rev-parse "step-$i" >/dev/null 2>&1 ; then
      last_step=$i
    fi
    i=$((i + 1))
  done

  if [ "$step" -gt "$last_step" ]; then
    step=$last_step
  fi

  if ! git rev-parse "step-$step" >/dev/null 2>&1 ; then
    >&2 echo "Required git tag \"step-$step\" but it was missing."
    exit 1
  fi
}

function persist_step() {
  echo $step > ".tag.txt"
}

function cleanup() {
  if [ "$prev_step" -ne "$step" ]; then
    echo "Cleaning up the source for step $step."
    git reset --hard > /tmp/steps/last.out 2>&1
    git checkout "tags/step-$step" >> /tmp/steps/last.out 2>&1
    git clean -fd >> /tmp/steps/last.out 2>&1
  else
    echo "Already at step $step. Nothing to do."
  fi
}

cd "$curr"
