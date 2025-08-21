#!/usr/bin/env bash
set -euo pipefail
cd "$(dirname "$0")/.."
cd app
mvn -q clean package
echo "Jar: target/catalog-0.0.1-SNAPSHOT.jar"
