#!/usr/bin/env bash
set -euo pipefail
IMAGE="gattisuresh/wayfair-catalog:latest"
docker build -t "$IMAGE" -f docker/Dockerfile .
docker push "$IMAGE"
