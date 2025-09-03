#!/bin/bash
cd /home/kavia/workspace/code-generation/fittrack-pro-17265-17274/fitness_tracker_frontend
./gradlew lint
LINT_EXIT_CODE=$?
if [ $LINT_EXIT_CODE -ne 0 ]; then
   exit 1
fi

