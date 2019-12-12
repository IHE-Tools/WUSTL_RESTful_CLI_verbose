package org.nrg.xnat.dicom.enums

import com.jayway.restassured.response.Response
import org.dcm4che3.data.Keyword
import org.nrg.xnat.dicom.RequestUtils
import org.nrg.xnat.dicom.jackson.mapper.DicomWebXmlMapper
import org.nrg.xnat.dicom.model.DicomObject
import org.nrg.xnat.util.DicomUtils

import javax.mail.BodyPart
import javax.mail.internet.MimeMultipart
import javax.mail.util.ByteArrayDataSource

import static org.testng.AssertJUnit.*

enum QidoRequestContentType {

    JSON ('application/dicom+json') {
        @Override
        List<DicomObject> validateAndReadResponse(Response response) {
            assertEquals("${contentType};charset=UTF-8", response.contentType())
            response.as(DicomObject[]) as List
        }

        @Override
        String acceptHeaderType() {
            contentType
        }
    },
    XML ('application/dicom+xml') {
        @Override
        List<DicomObject> validateAndReadResponse(Response response) {
            assertTrue(response.contentType().startsWith(acceptHeaderType()))
            assertTrue(response.contentType().contains('boundary')) // DICOM PS 3.18 6.1.1.1

            final MimeMultipart multipart = new MimeMultipart(new ByteArrayDataSource(response.asInputStream(), 'application/xml'))
            (0..<multipart.getCount()).collect { int index ->
                final BodyPart part = multipart.getBodyPart(index)
                assertEquals(contentType, part.getContentType())
                final DicomObject dicomObject = DicomWebXmlMapper.INSTANCE.readValue(part.getInputStream(), DicomObject)
                dicomObject.attributes.each { attribute ->
                    assertEquals(Keyword.valueOf(DicomUtils.stringHeaderToHexInt(attribute.tag)), attribute.keyword) // check here because this isn't included in JSON so we only validate it in XML
                }
                dicomObject
            }
        }

        @Override
        String acceptHeaderType() {
            RequestUtils.multipartRelated(contentType)
        }
    }

    String contentType

    QidoRequestContentType(String contentType) {
        setContentType(contentType)
    }

    abstract List<DicomObject> validateAndReadResponse(Response response)

    abstract String acceptHeaderType()

}
