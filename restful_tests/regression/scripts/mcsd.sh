#!/bin/sh

touch /tmp/mcsd; rm -r /tmp/mcsd

./restful_tests/scripts/mcsd.sh /tmp http://localhost:8080/hapi-fhir-jpaserver/fhir
