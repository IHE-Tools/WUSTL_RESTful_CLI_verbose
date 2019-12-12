package edu.wustl.cil.SMM.RESTfulTesting;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import org.hl7.fhir.dstu3.model.*;
import ca.uhn.fhir.context.FhirContext;

public class RESTfulMain {
    public static void main(String[] args) {
        System.out.println("Hello world");

        FhirContext ctx = FhirContext.forDstu3();
        Patient patient = new Patient();
//        patient.setBirthDate("19581201");
//        AllergyIntolerance allergyIntolerance = new AllergyIntolerance();
    }
}
