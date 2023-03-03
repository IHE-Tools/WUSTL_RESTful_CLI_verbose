package org.nrg.xnat.dicom.wado

import com.google.common.base.Charsets
import com.jayway.restassured.response.Response
import com.jayway.restassured.specification.RequestSpecification
import org.apache.commons.lang3.CharSet
import org.dcm4che3.data.DatasetWithFMI
import org.dcm4che3.data.Tag
import org.nrg.testing.xnat.rest.XnatRestDriver
import org.nrg.xnat.dicom.enums.WadoValidationType
import org.nrg.xnat.dicom.web.DicomWebTestInstance
import org.nrg.xnat.dicom.web.DicomWebTestSeries
import org.nrg.xnat.dicom.web.DicomWebTestSpec
import org.nrg.xnat.dicom.enums.WadoRequestContentType
import org.nrg.xnat.dicom.enums.WadoRequestType
import org.nrg.xnat.dicom.web.DicomWebTestStudy

import static org.testng.AssertJUnit.*

class WadoTestSpec extends DicomWebTestSpec<AcceptableWadoResponse, WadoTestSpec> {

    WadoRequestContentType contentType
    WadoRequestType requestType
    WadoValidationType validationType = WadoValidationType.FULL_VALIDATION
    String charset
    String sopInstanceUID
    List<String> transferSyntaxes = []

    WadoTestSpec(WadoRequestType requestType) {
        setRequestType(requestType)
        setExpectedStatusCode(200)
    }

    @Override
    WadoTestSpec expectedResult(Collection<? extends AcceptableWadoResponse> expected) {
        if (expected.size() > 1) fail('WADO-RS test spec should only have 1 item in expectedResult.')
        super.expectedResult(expected)
    }

    WadoTestSpec contentType(WadoRequestContentType contentType) {
        setContentType(contentType)
        this
    }

    WadoTestSpec validationType(WadoValidationType validationType) {
        setValidationType(validationType)
        this
    }

    WadoTestSpec charset(String set) {
        setCharset(set)
        this
    }

    @Override
    WadoTestSpec seriesInstanceUID(DicomWebTestSeries series) {
        setStudyInstanceUID(series.study.studyInstanceUID)
        super.seriesInstanceUID(series)
    }

    WadoTestSpec sopInstanceUID(String uid) {
        setSopInstanceUID(uid)
        this
    }

    WadoTestSpec sopInstanceUID(DicomWebTestInstance instance) {
        setSopInstanceUID(instance.sopInstanceUID)
        seriesInstanceUID(instance.series)
    }

    WadoTestSpec transferSyntax(String tsUid) {
        transferSyntaxes([tsUid])
    }

    WadoTestSpec transferSyntaxes(List<String> transferSyntaxes) {
        setTransferSyntaxes(transferSyntaxes)
        this
    }

    RequestSpecification addAcceptHeader(RequestSpecification requestSpecification) {
        if (contentType != null) {
            if (transferSyntaxes.isEmpty()) {
                requestSpecification.accept(acceptContentWithCharset()) // TODO: can transfer-syntax and charset both be specified at once?
            } else {
                requestSpecification.accept(transferSyntaxes.collect { acceptContentWithTransferSyntax(it) }.join(', '))
            }
        }
        requestSpecification
    }

    private String acceptContentWithCharset() {
        charset == null ? contentType.acceptHeaderType() : "${contentType.acceptHeaderType()}; charset=${charset}"
    }

    private String acceptContentWithTransferSyntax(String transferSyntax) {
        "${contentType.acceptHeaderType()}; transfer-syntax=${transferSyntax}"
    }

    @Override
    void execute(XnatRestDriver driver) {
        final Response response = addAcceptHeader(buildBaseQuery()).get(driver.formatXapiUrl('dicomweb', requestType.urlPathFor(studyInstanceUID, seriesInstanceUID, sopInstanceUID)))
        assertEquals(expectedStatusCode, response.statusCode)
        switch (expectedStatusCode) {
            case 200:
            case 206:
                validateResult(contentType.validateAndReadResponse(response, transferSyntaxes))
                break
            default:
                assertEquals(expectedBody, response.body().asString())
        }
    }

    @SuppressWarnings("GroovyAssignabilityCheck")
    void validateResult(List<? extends WadoResponseItem> responseItems) {
        switch (requestType) {
            case WadoRequestType.STUDY:
                final DicomWebTestStudy study = expectedResult[0] as DicomWebTestStudy
                assertEquals(study.numStudyRelatedInstances, responseItems.size())
                validate(study.instanceMap, responseItems)
                break
            case WadoRequestType.SERIES:
                final DicomWebTestSeries series = expectedResult[0] as DicomWebTestSeries
                assertEquals(series.numSeriesRelatedInstances, responseItems.size())
                validate(series.instanceMap, responseItems)
                break
            case WadoRequestType.INSTANCE:
                final DicomWebTestInstance instance = expectedResult[0] as DicomWebTestInstance
                assertEquals(1, responseItems.size())
                validate(instance.instanceMap, responseItems)
                break
        }
    }

    @SuppressWarnings("GroovyAssignabilityCheck")
    private void validate(Map<String, DatasetWithFMI> expectedInstances, List<? extends WadoResponseItem> responseItems) {
        responseItems.each { DicomMultipartItem responseObject ->
            validationType.validate(expectedInstances.get(responseObject.dicom.dataset.getString(Tag.SOPInstanceUID)), responseObject.dicom)
        }
    }

}
