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
  echo "Arguments: <log_folder> base_url"
  echo "           log_folder is a base folder; will be created if necessary"
  echo "           base_url   is the endpoint for the FHIR server under test"
  exit 1
 fi
}

function get_arguments() {
 LOG_FOLDER="$1/pdqm/server/farnsworth"
 BASE_URL=$2

 mkdir -p $LOG_FOLDER
 if [ $? != 0 ] ; then
  echo "Failed to make the log folder $LOG_FOLDER; script exiting"
  exit 1
 fi
}

check_environment
check_arguments $*
get_arguments $*

# Pick up the proper jar file
. ./scripts/jar.sh
echo $JAR

echo java -jar $JAR	\
  REST	\
  ./restful_tests/pdqm/server	\
  ./restful_tests/pdqm/server/farnsworth.txt	\
  $LOG_FOLDER	\
  $BASE_URL

java -jar $JAR	\
  REST	\
  ./restful_tests/pdqm/server	\
  ./restful_tests/pdqm/server/farnsworth.txt	\
  $LOG_FOLDER	\
  $BASE_URL

if [ $? != 0 ] ; then
 echo "The script ./restful_tests/scripts/pdqm_farnsworth.sh failed to execute java command."
 echo "Script exiting with error status."
 exit 1
fi
