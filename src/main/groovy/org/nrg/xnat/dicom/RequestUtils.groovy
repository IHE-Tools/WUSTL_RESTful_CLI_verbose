package org.nrg.xnat.dicom

class RequestUtils {

    static String multipartRelated(String baseContentType) {
        "multipart/related; type=\"${baseContentType}\""
    }

}
