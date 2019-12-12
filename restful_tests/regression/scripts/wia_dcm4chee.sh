#!/bin/sh

BASE_URL=http://localhost:9090/dcm4chee-arc/aets/DCM4CHEE/rs
BASE_LOGS=/opt/december/restful_logs
SESSION=dcm4chee

BASE_TESTS=/opt/december/restful_tests
JAR=$BASE_TESTS/lib/WUSTL-RESTful-CLI-1.0-SNAPSHOT.jar

java -jar $JAR		\
	$BASE_TESTS/wia/testcases-server	\
	$BASE_TESTS/wia/testcases-server/query.txt	\
	$BASE_LOGS/wia/server/query/$SESSION	\
	$BASE_URL

