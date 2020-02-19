#!/bin/sh

FOLDER=/tmp/qedm/server/condition

if [ -e $FOLDER ] ; then rm -r $FOLDER; fi

mkdir -p $FOLDER

./restful_tests/scripts/qedm_condition.sh /tmp http://localhost:8080/hapi-fhir-jpaserver/fhir
