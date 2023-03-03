#!/bin/sh

VERSION=1.0

mvn clean install

if [ -e /tmp/WUSTL ]; then rm -r /tmp/WUSTL; fi

mkdir /tmp/WUSTL

cp -rp scripts /tmp/WUSTL
cp -rp rules /tmp/WUSTL
mkdir /tmp/WUSTL/lib

cp  target/WUSTL-RESTful-CLI-1.0-SNAPSHOT-jar-with-dependencies.jar	\
	/tmp/WUSTL/lib/360X.jar

pushd /tmp/WUSTL
zip -rq ../360X-$VERSION.zip .
cd ..; ls
popd
