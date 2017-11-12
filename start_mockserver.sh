#!/usr/bin/env bash

docker pull jamesdbloom/mockserver
docker run -d -p1080:1080 jamesdbloom/mockserver
sleep 5
curl -vX PUT \
  http://localhost:1080/expectation \
  -H 'cache-control: no-cache' \
  -H 'Content-Type: application/json' \
  -d @mockResponse.json