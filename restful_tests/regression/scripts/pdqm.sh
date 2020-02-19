#!/bin/sh

touch /tmp/pdqm; rm -r /tmp/pdqm

./restful_tests/scripts/pdqm_farnsworth.sh /tmp http://localhost:8080/hapi-fhir-jpaserver/fhir
