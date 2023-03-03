package org.nrg.xnat.dicom.enums

import com.jayway.restassured.response.Response
import org.dcm4che3.data.Tag
import org.dcm4che3.data.UID
import org.nrg.xnat.dicom.wado.DicomMultipartItem
import org.nrg.xnat.dicom.RequestUtils
import org.nrg.xnat.dicom.wado.WadoResponseItem
import org.nrg.xnat.util.DicomUtils

import javax.mail.BodyPart
import javax.mail.internet.MimeMultipart
import javax.mail.util.ByteArrayDataSource

import static org.testng.AssertJUnit.assertEquals
import static org.testng.AssertJUnit.assertTrue

abstract class WadoRequestContentType {

    String contentType

    WadoRequestContentType(String contentType) {
        setContentType(contentType)
    }

    abstract List<WadoResponseItem> validateAndReadResponse(Response response, List<String> requestedTransferSyntaxes)

    String acceptHeaderType() {
        RequestUtils.multipartRelated(contentType)
    }

    public static final WadoRequestContentType DICOM = new WadoRequestContentType('application/dicom') {
        @Override
        List<WadoResponseItem> validateAndReadResponse(Response response, List<String> requestedTransferSyntaxes) {
            assertTrue(response.contentType().startsWith(RequestUtils.multipartRelated(getContentType())))
            // TODO: assertTrue(response.contentType().contains('boundary')) // DICOM PS 3.18 6.1.1.1

            final MimeMultipart multipart = new MimeMultipart(new ByteArrayDataSource(response.asInputStream(), getContentType()))
            (0..<multipart.getCount()).collect { int index ->
                final BodyPart part = multipart.getBodyPart(index)
                final List<String> reportedContentType = part.contentType.split('; ') // header should be formatted as: Content-Type: application/dicom; transfer-syntax=1.2.3.4
                assertEquals(contentType, reportedContentType[0])
                final DicomMultipartItem dicomItem = new DicomMultipartItem().dicom(DicomUtils.readDicom(part.inputStream))
                /*final List<String> fullReportedTransferSyntax = reportedContentType[1].split('=')
                assertEquals('transfer-syntax', fullReportedTransferSyntax[0])
                final String reportedTransferSyntax = fullReportedTransferSyntax[1]
                assertEquals(reportedTransferSyntax, dicomItem.dicom.fileMetaInformation.getString(Tag.TransferSyntaxUID))
                if (requestedTransferSyntaxes == ['*']) {
                    assertEquals(reportedTransferSyntax, UID.ExplicitVRLittleEndian)
                } else {
                    assertTrue(requestedTransferSyntaxes.contains(reportedTransferSyntax))
                }
                dicomItem.reportedTSUID(reportedTransferSyntax)*/
                dicomItem // TODO: uncomment out above when XNAT is ready
            }
        }
    }

    public static final WadoRequestContentType BULK_DATA = new BinaryWadoRequestContentType('application/octet-stream')
    public static final WadoRequestContentType JPEG      = new BinaryWadoRequestContentType('image/jpeg')
    public static final WadoRequestContentType DICOM_RLE = new BinaryWadoRequestContentType('image/x-dicom-rle')
    public static final WadoRequestContentType JLS       = new BinaryWadoRequestContentType('image/x-jls')
    public static final WadoRequestContentType JP2       = new BinaryWadoRequestContentType('image/jp2')
    public static final WadoRequestContentType JPX       = new BinaryWadoRequestContentType('image/jpx')
    public static final WadoRequestContentType MPEG2     = new BinaryWadoRequestContentType('video/mpeg2')
    public static final WadoRequestContentType MP4       = new BinaryWadoRequestContentType('video/mp4')

    private static class BinaryWadoRequestContentType extends WadoRequestContentType {
        BinaryWadoRequestContentType(String contentType) {
            super(contentType)
        }

        @Override
        List<WadoResponseItem> validateAndReadResponse(Response response, List<String> requestedTransferSyntaxes) {
            throw new UnsupportedOperationException('BulkData and PixelData responses are not supported by XNAT implementation of DICOM Web Services API.')
        }
    }

}
