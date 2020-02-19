#!/bin/sh

function check_environment() {
 if [ ! -f ./scripts/check_environment.sh ] ; then
  echo "The file ./scripts/check_environment.sh does not exist. Are you running this script from the right folder?"
  exit 1
 fi

 ./scripts/check_environment.sh
 if [ $? != 0 ] ; then
  echo "The script ./scripts/check_environment.sh failed; please review."
  exit 1
 fi
}

function check_arguments() {
 if [ $# != 2 ] ; then
  echo "Arguments: <log_folder> <input_file>"
  echo "           log_folder is a base folder; will be created if necessary"
  echo "           input_file contains JSON representation of opioid case"
  exit 1
 fi
}

function get_arguments() {
 LOG_FOLDER="$1/vrdr/content/opioid"
 INPUT_FILE=$2

 mkdir -p $LOG_FOLDER
 if [ $? != 0 ] ; then
  echo "Failed to make the log folder $LOG_FOLDER; script exiting"
  exit 1
 fi

 if [ ! -f $INPUT_FILE ] ; then
  echo "Input file $INPUT_FILE not found; script exiting"
  exit 1
 fi
}

check_environment
check_arguments $*
get_arguments $*

JAR=`./scripts/jar.sh`
echo $JAR

java -jar $JAR	\
  CONTENT	\
  ./content_tests/vrdr/testcases-content	\
  ./content_tests/vrdr/testcases-content/opioid_1.txt	\
  $LOG_FOLDER	\
  $INPUT_FILE

if [ $? != 0 ] ; then
 echo "The script ./content_tests/scripts/vrdr_opioid.sh failed to execute java command."
 echo "Script exiting with error status."
 exit 1
fi
