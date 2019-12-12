#!/bin/sh

function check_file() {
 if [ ! -e $1 ] ; then
  echo This required file not found: $1
  exit 1
 fi
}

function check_folder() {
 if [ ! -d $1 ] ; then
  echo This required folder does not found: $1
  exit 1
 fi
}

function check_log_folder() {
 if [ ! -d $1 ] ; then
  echo Did not find the base log folder $1
  echo We will try to create that folder
  mkdir -p $1
  if [ $? != 0 ] ; then
   echo "mkdir -p $1" failed
   exit 1
  fi
 fi
}

function check_args() {
 if [ $# != 3 ] ; then
  echo "Arguments: <log folder> <session> <url>"
  echo "           <log folder> Base folder for output logs"
  echo "                        This script will create logs/wia/server/query/session"
  echo "           <session>    Label for this testing session. Allows you to have separate outputs"
  echo "                        Do not include spaces in this label"
  echo "           <url>        URL for your WIA server"
  echo "           "
  echo "           "
  exit 1
 fi

 check_folder wia
 check_file   lib/WUSTL-RESTful-CLI-1.0-SNAPSHOT.jar
 check_log_folder $1
}

check_args $*

BASE_TESTS=.
BASE_LOGS=$1
SESSION=$2
BASE_URL=$3


JAR=$BASE_TESTS/lib/WUSTL-RESTful-CLI-1.0-SNAPSHOT.jar

echo	\
java -jar $JAR		\
	$BASE_TESTS/wia/testcases-server	\
	$BASE_TESTS/wia/testcases-server/query.txt	\
	$BASE_LOGS/wia/server/query/$SESSION	\
	$BASE_URL

