#!/bin/sh

VERSION=1.0

mvn clean install

if [ -e /tmp/WUSTL ]; then rm -r /tmp/WUSTL; fi

mkdir /tmp/WUSTL

cp -rp restful_tests /tmp/WUSTL
cp -rp content_tests /tmp/WUSTL
mkdir /tmp/WUSTL/lib

cp  target/WUSTL-RESTful-CLI-1.0-SNAPSHOT-jar-with-dependencies.jar	\
	/tmp/WUSTL/lib/WUSTL-RESTful-CLI-1.0-SNAPSHOT.jar

pushd /tmp/WUSTL
zip -rq ../WUSTL_RESTful_CLI-$VERSION.zip .
cd ..; ls
popd
