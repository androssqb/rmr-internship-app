#!/usr/bin/env bash

BUILD_CACHE_THRESHOLD=1500

set -eu

function get_gradle_slug() {
  local dist
  dist=$(source gradle/wrapper/gradle-wrapper.properties && echo "${distributionUrl?:}")
  dist=${dist##*gradle-}
  echo "${dist%.zip}"
}

# Delete outdated Gradle wrappers
shopt -s extglob
gradle_slug=$(get_gradle_slug)
rm -rf .gradle/wrapper/dists/gradle-!("$gradle_slug")

# Delete dependencies lock file and plugin resolution
rm -f .gradle/caches/modules-2/modules-2.lock
rm -rf .gradle/caches/*/plugin-resolution/

# Drop build cache if it contains too many files
build_cache_count=$(find .gradle/caches/build-cache-1/ -type f | wc -l)
if [[ $build_cache_count -gt $BUILD_CACHE_THRESHOLD ]]; then
  echo "Build cache contains $build_cache_count files. Cleaning..."
  rm -rf .gradle/caches/build-cache-1/
fi
