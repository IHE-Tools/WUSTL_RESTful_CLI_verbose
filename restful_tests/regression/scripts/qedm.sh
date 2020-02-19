#!/bin/sh

touch /tmp/qedm; rm -r /tmp/qedm

#./restful_tests/scripts/qedm_procedure.sh /tmp http://localhost:8080/hapi-fhir-jpaserver/fhir

#./restful_tests/scripts/qedm_condition.sh /tmp http://localhost:8080/hapi-fhir-jpaserver/fhir

#./restful_tests/scripts/qedm_encounter.sh /tmp http://localhost:8080/hapi-fhir-jpaserver/fhir

./restful_tests/scripts/qedm_medication_statement.sh /tmp http://localhost:8080/hapi-fhir-jpaserver/fhir
