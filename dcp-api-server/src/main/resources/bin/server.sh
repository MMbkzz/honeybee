#! /bin/sh
#=======================================================================
# Define arguments for process
#=======================================================================
SERVER_DIR=$(
  cd "$(dirname "$0")"
  pwd
)
#-----------------------------------------------------------------------
# Process Tag to identify
# Use this tag should identify the process.
# The command is :  ps -ef | grep -w $PROC_TAG
PROC_TAG="dcp-api-server"
#-----------------------------------------------------------------------

#-----------------------------------------------------------------------
# Flags for java Virtal Machine
JMX_PORT="0"
JAVA_OPTS="$JAVA_OPTS -Dcom.sun.management.jmxremote.port=$JMX_PORT"
JAVA_OPTS="$JAVA_OPTS -Dcom.sun.management.jmxremote.ssl=false "
JAVA_OPTS="$JAVA_OPTS -Dcom.sun.management.jmxremote "
JAVA_OPTS="$JAVA_OPTS -Dcom.sun.management.jmxremote.authenticate=false "
JAVA_OPTS="$JAVA_OPTS -Xms512m -Xmx2048m -XX:MetaspaceSize=512m -XX:MaxMetaspaceSize=768m"
PROC_OPTS="-Djmx.port=$JMX_PORT"
#-----------------------------------------------------------------------

#-----------------------------------------------------------------------
# Process log file
LOG_FILE="$SERVER_DIR/log/dcp-api-server.out"
#-----------------------------------------------------------------------

#-----------------------------------------------------------------------
# List of blank-separated paths defining the contents of the classes
# and jars
# Examples:
#	LOADER_PATH="foo foo/*.jar lib/ipnet.jar"
#     "foo": Add this folder as a class repository
#     "foo/*.jar": Add all the JARs of the specified folder as class
#                  repositories
#     "lib/ipnet.jar": Add lib/ipnet.jar as a class repository
LOADER_PATH="$SERVER_DIR/lib/*.jar $SERVER_DIR/conf/"
#-----------------------------------------------------------------------

#-----------------------------------------------------------------------
# Specify the JAVA_HOME
# if it has JAVA_HOME environment argument,specify to it,
# else change it to correct java path
#-----------------------------------------------------------------------
JAVA_HOME="/Library/Java/JavaVirtualMachines/jdk1.8.0_241.jdk/Contents/Home"

#-----------------------------------------------------------------------
# Process Entrance class
MAIN_CLASS="com.stackstech.dcp.apiserver.ApiServerApplication"
#-----------------------------------------------------------------------

#-----------------------------------------------------------------------
# Process Arguments
PROC_ARGS_START="-start"
PROC_ARGS_STOP="-stop"
#-----------------------------------------------------------------------

#=======================================================================
# Define information tag
#=======================================================================
RUNNING_TAG="[U]"
NOT_RUNNING_TAG="[X]"
ERROR_TAG="[E]"
INFO_TAG="[I]"

#=======================================================================
# Define functions for process
#=======================================================================

set_classpath() {
  set ${LOADER_PATH}
  while [ $# -gt 0 ]; do
    classpath=${classpath}:$1
    shift
  done
  CLASSPATH=${classpath}:${CLASSPATH}
}

is_proc_run() {
  localServerId=$(ps -ef | grep -w "${PROC_TAG}" | grep -v grep | awk '{print $2}')
  if [ -z "${localServerId}" ]; then
    return 1
  else
    return 0
  fi
}

status_proc() {
  is_proc_run
  if [ $? -eq 0 ]; then
    echo "${RUNNING_TAG} ${PROC_TAG} is running !"
    ps -ef | grep -w "${PROC_TAG}" | grep -v grep
  else
    echo "${NOT_RUNNING_TAG} ${PROC_TAG} is not running !"
  fi
}

start_proc() {
  is_proc_run
  if [ $? -eq 0 ]; then
    echo "${INFO_TAG} ${PROC_TAG} is already running !"
  else
    echo "${INFO_TAG} Starting ${PROC_TAG} ..."
    set_classpath
    nohup ${JAVA_HOME}/bin/java -Diname=${PROC_TAG} ${JAVA_OPTS} ${PROC_OPTS} -cp ${CLASSPATH} ${MAIN_CLASS} ${PROC_ARGS_START} 1>&- 2>${LOG_FILE} &
    sleep 3
    is_proc_run
    if [ $? -eq 0 ]; then
      echo "${INFO_TAG} ${PROC_TAG} started !"
    else
      echo "${ERROR_TAG} ${PROC_TAG} starts failed !"
    fi
  fi
}

stop_proc() {
  is_proc_run
  if [ $? -eq 0 ]; then
    echo "${INFO_TAG} Stopping ${PROC_TAG} ..."
    set_classpath
    nohup ${JAVA_HOME}/bin/java -Diname=${PROC_TAG} ${PROC_OPTS} -cp ${CLASSPATH} ${MAIN_CLASS} ${PROC_ARGS_STOP} 1>&- 2>>${LOG_FILE} &
  else
    echo "${INFO_TAG} ${PROC_TAG} has already stopped !"
  fi
}

kill_proc() {
  is_proc_run
  if [ $? -eq 0 ]; then
    echo "${INFO_TAG} Killing ${PROC_TAG} ..."
    /bin/kill ${localServerId}
    sleep 3
    is_proc_run
    if [ $? -eq 0 ]; then
      echo "${ERROR_TAG} ${PROC_TAG} kill failed !"
    else
      echo "${INFO_TAG} ${PROC_TAG} killed !"
    fi
  else
    echo "${INFO_TAG} ${PROC_TAG} has already killed !"
  fi
}

usage() {
  echo ${PROC_TAG} usage:
  echo "$(basename $0) <start|stop|kill|status|restart>"
}

#=======================================================================
# Main Program begin
#=======================================================================

case $1 in
start)
  start_proc
  ;;
status)
  status_proc
  ;;
stop)
  stop_proc
  ;;
kill)
  kill_proc
  ;;
restart)
  stop_proc
  start_proc
  ;;
*)
  usage
  ;;
esac
