#!/usr/bin/env bash
# Logging functions
function log_info() {
  echo "[INFO] | $(date -u +"%D %T") UTC | $1"
}

function log_error() {
  echo "[ERROR] | $(date -u +"%D %T") UTC | $1"
}

BASEDIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && cd .. && pwd)"

if [ ! $# -eq 2 ]; then
  log_error "env file and dq file must be provided!"
  exit 1
fi

env_file=$1
if [ ! -f "${env_file}" ]; then
  log_error "Not found env file: ${env_file}"
  exit
fi
shift

dq_file=$1
if [ ! -f "${dq_file}" ]; then
  log_error "Not found dq file: ${dq_file}"
  exit
fi
shift

cd ${BASEDIR}

export JAVA_OPTS="-Xmx2048m -Xms256m -server -XX:+UseG1GC"

LIB_JARS=$(echo ${BASEDIR}/lib/*.jar | tr ' ' ',')
APP_CLASS="com.stackstech.honeybee.Application"
APP_JAR=$(ls ${BASEDIR}/lib/measure-*.jar)

exec $(which spark-submit) --class ${APP_CLASS} --jars ${LIB_JARS} ${APP_JAR} ${env_file} ${dq_file}
