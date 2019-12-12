package org.nrg.xnat.dicom.enums

import org.nrg.xnat.dicom.model.DicomObject
import org.nrg.xnat.dicom.qido.AcceptableQidoResponse
import org.nrg.xnat.dicom.web.DicomWebTestSeries
import org.nrg.xnat.dicom.web.DicomWebTestStudy

import static org.testng.AssertJUnit.*

enum QidoRequestType {

    STUDIES {
        @Override
        String urlPathFor(String studyInstanceUID, String seriesInstanceUID) {
            'studies'
        }

        @Override
        void validate(Collection<? extends AcceptableQidoResponse> expectedObjects, List<DicomObject> responseObjects, boolean responseIncludesStudyData, boolean responseIncludesSeriesData) {
            assertEquals(expectedObjects.size(), responseObjects.size()) // should be same number of studies in response as we expect
            assertEquals(expectedObjects.size(), responseObjects.collect { it.studyInstanceUid() }.unique().size()) // study UIDs should all be unique
            responseObjects.each { responseObject -> // with previous two lines, we can just match up each object in response to an arbitrary one in expected
                final DicomWebTestStudy matchedStudy = expectedObjects.find { (it as DicomWebTestStudy).studyInstanceUID == responseObject.studyInstanceUid() } as DicomWebTestStudy
                assertNotNull(matchedStudy)
                validateResponseObjectAsStudy(matchedStudy, responseObject)
            }
        }
    },
    SERIES {
        @Override
        String urlPathFor(String studyInstanceUID, String seriesInstanceUID) {
            studyInstanceUID == null ? 'series' : "studies/${studyInstanceUID}/series"
        }

        @Override
        void validate(Collection<? extends AcceptableQidoResponse> expectedObjects, List<DicomObject> responseObjects, boolean responseIncludesStudyData, boolean responseIncludesSeriesData) {
            assertEquals(expectedObjects.size(), responseObjects.size()) // should be same number of series in response as we expect
            assertEquals(expectedObjects.size(), responseObjects.collect { it.seriesInstanceUid() }.unique().size()) // series UIDs should all be unique
            responseObjects.each { responseObject -> // with previous two lines, we can just match up each object in response to an arbitrary one in expected
                final DicomWebTestSeries matchedSeries = expectedObjects.find { (it as DicomWebTestSeries).seriesInstanceUID == responseObject.seriesInstanceUid() } as DicomWebTestSeries
                assertNotNull(matchedSeries)
                validateResponseObjectAsSeries(matchedSeries, responseObject)
                if (responseIncludesStudyData) validateResponseObjectAsStudy(matchedSeries.study, responseObject)
            }
        }
    },
    INSTANCES {
        @Override
        String urlPathFor(String studyInstanceUID, String seriesInstanceUID) {
            if (studyInstanceUID == null) {
                'instances'
            } else if (seriesInstanceUID == null) {
                "studies/${studyInstanceUID}/instances"
            } else {
                "studies/${studyInstanceUID}/series/${seriesInstanceUID}/instances"
            }
        }

        @Override
        void validate(Collection<? extends AcceptableQidoResponse> expectedObjects, List<DicomObject> responseObjects, boolean responseIncludesStudyData, boolean responseIncludesSeriesData) {
            throw new UnsupportedOperationException("XNAT's implementation of DICOM Web does not support instance level queries.")
        }
    }

    abstract String urlPathFor(String studyInstanceUID, String seriesInstanceUID)

    abstract void validate(Collection<? extends AcceptableQidoResponse> expectedObjects, List<DicomObject> responseObjects, boolean responseIncludesStudyData, boolean responseIncludesSeriesData)

    void validateResponseObjectAsStudy(DicomWebTestStudy matchedStudy, DicomObject responseObject) {
        // TODO: uncomment next line when XNAT-5413 and XNAT-5292 are fixed
        // assertEquals(0, responseObject.missingStudyAttributes().size())
        responseObject.attributes.each { attribute ->
            attribute.validate()
        }
        assertEquals(matchedStudy.studyDate, responseObject.studyDate())
        assertEquals(matchedStudy.studyTime, responseObject.studyTime())
        assertEquals(matchedStudy.accessionNumber, responseObject.accessionNumber())
        assertEquals(matchedStudy.modalitiesInStudy, responseObject.modalitiesInStudy().toSet())
        if (matchedStudy.patientName == null) {
            assertNull(responseObject.patientName())
        } else {
            assertEquals(matchedStudy.patientName, responseObject.patientName().alphabetic)
        }
        assertEquals(matchedStudy.patientID, responseObject.patientID())
        assertEquals(matchedStudy.studyInstanceUID, responseObject.studyInstanceUid())
        assertEquals(matchedStudy.studyID, responseObject.studyID())
        // TODO: uncomment next line when XNAT-5288 is fixed.
        // assertEquals(matchedStudy.patientBirthdate, responseObject.patientBirthDate())
        assertEquals(matchedStudy.patientSex, responseObject.patientSex())
        assertEquals(matchedStudy.numStudyRelatedSeries, responseObject.numStudyRelatedSeries())
        assertEquals(matchedStudy.numStudyRelatedInstances, responseObject.numStudyRelatedInstances())
        assertEquals('ONLINE', responseObject.instanceAvailability())
    }

    void validateResponseObjectAsSeries(DicomWebTestSeries matchedSeries, DicomObject responseObject) {
        // TODO: uncomment next line when XNAT-5292 is fixed
        // assertEquals(0, responseObject.missingSeriesAttributes().size())
        responseObject.attributes.each { attribute ->
            attribute.validate()
        }
        assertEquals(matchedSeries.modality, responseObject.modality())
        assertEquals(matchedSeries.seriesDescription, responseObject.seriesDescription())
        assertEquals(matchedSeries.seriesInstanceUID, responseObject.seriesInstanceUid())
        assertEquals(matchedSeries.seriesNumber, responseObject.seriesNumber())
        assertEquals(matchedSeries.numSeriesRelatedInstances, responseObject.numSeriesRelatedInstances())
        assertEquals(matchedSeries.performedProcedureStepStartDate, responseObject.performedProcedureStepStartDate())
        assertEquals(matchedSeries.performedProcedureStepStartTime, responseObject.performedProcedureStepStartTime())
    }

}
