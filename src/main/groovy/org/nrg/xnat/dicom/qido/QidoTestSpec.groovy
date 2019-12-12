package org.nrg.xnat.dicom.qido

import com.jayway.restassured.response.Response
import com.jayway.restassured.specification.RequestSpecification
import org.dcm4che3.data.Keyword
import org.nrg.testing.xnat.rest.XnatRestDriver
import org.nrg.xnat.dicom.web.DicomWebTestSpec
import org.nrg.xnat.dicom.enums.QidoRequestContentType
import org.nrg.xnat.dicom.enums.QidoRequestType
import org.nrg.xnat.dicom.model.DicomObject
import org.nrg.xnat.util.DicomUtils

import static org.testng.AssertJUnit.*

class QidoTestSpec extends DicomWebTestSpec<AcceptableQidoResponse, QidoTestSpec> {

    QidoRequestContentType contentType
    QidoRequestType requestType
    Map<Integer, Object> querySearchParams = [:] // will perform test twice with both formats of queries

    QidoTestSpec(QidoRequestType requestType) {
        setRequestType(requestType)
        setExpectedStatusCode(204)
    }

    QidoTestSpec() {}

    QidoTestSpec contentType(QidoRequestContentType contentType) {
        setContentType(contentType)
        this
    }

    QidoTestSpec simpleQuery(int dicomHeader, String value) {
        querySearchParams = [(dicomHeader) : value]
        this
    }

    QidoTestSpec simpleQuery(int dicomHeader, List<String> value) {
        querySearchParams = [(dicomHeader) : value]
        this
    }

    QidoTestSpec querySearchParams(Map<Integer, Object> params) {
        setQuerySearchParams(params)
        this
    }

    QidoRequestContentType contentTypeWithFallback() {
        contentType ?: QidoRequestContentType.JSON
    }

    @Override
    void execute(XnatRestDriver restDriver) {
        if (querySearchParams.isEmpty()) {
            performTest(restDriver, queryParams)
        } else {
            performTest(restDriver, querySearchParams.collectEntries { hexTag, value -> [(Keyword.valueOf(hexTag)) : value] } as Map<String, Object>)
            // query once with keyword (e.g. PatientName), repeat query with header string (e.g. 00100010)
            performTest(restDriver, querySearchParams.collectEntries { hexTag, value -> [(DicomUtils.intToSimpleHeaderString(hexTag)) : value] } as Map<String, Object>)
        }
    }

    private void performTest(XnatRestDriver restDriver, Map<String, Object> effectiveParams) {
        RequestSpecification requestSpecification = (contentType == null ? buildBaseQuery() : buildBaseQuery().accept(contentType.acceptHeaderType()))
        effectiveParams.each { paramKey, paramValue ->
            requestSpecification.queryParam(paramKey, paramValue)
        }
        final Response response = requestSpecification.get(restDriver.formatXapiUrl('dicomweb', requestType.urlPathFor(studyInstanceUID, seriesInstanceUID)))
        assertEquals(expectedStatusCode, response.statusCode)
        switch (expectedStatusCode) {
            case 200:
                final List<DicomObject> responseObjects = contentTypeWithFallback().validateAndReadResponse(response)
                requestType.validate(expectedResult, responseObjects, studyInstanceUID == null, seriesInstanceUID == null) // PS3.18 6.7.1.2.2.2 : last line of table. PS3.18 6.7.1.2.2.3 : last 2 lines of table
                break
            case 204:
                assertEquals('', response.body().asString()) // PS3.18 6.7.1.2
                break
            default:
                assertEquals(expectedBody, response.body().asString())
        }
    }

}
