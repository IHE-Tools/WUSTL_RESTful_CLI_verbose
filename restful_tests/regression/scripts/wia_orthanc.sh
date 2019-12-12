#!/bin/sh

BASE_URL=http://mac-mini:8042/dicom-web
BASE_LOGS=/opt/december/restful_logs
SESSION=wxy

BASE_TESTS=/opt/december/restful_tests
JAR=$BASE_TESTS/lib/WUSTL-RESTful-CLI-1.0-SNAPSHOT.jar

echo	\
java -jar $JAR		\
	$BASE_TESTS/wia/testcases-server	\
	$BASE_TESTS/wia/testcases-server/query.txt	\
	$BASE_LOGS/wia/server/query/$SESSION	\
	$BASE_URL

