#!/bin/sh

JAR=target/WUSTL-RESTful-CLI-1.0-SNAPSHOT-jar-with-dependencies.jar

java -jar $JAR	\
  CONTENT	\
  /opt/december/software-projects/WUSTL_RESTful_CLI-Intellij/content_tests/vrdr/testcases-content	\
  /opt/december/software-projects/WUSTL_RESTful_CLI-Intellij/content_tests/vrdr/testcases-content/opioid_1.txt	\
  /opt/december/restful_logs/vrdr/content/opioid_1/fri	\
  /opt/december/software-projects/WUSTL_RESTful_CLI-Intellij/content_tests/vrdr/testcases-content/VRDR-C-1200/opioid.json

#  /tmp/record-1576860621139.json
