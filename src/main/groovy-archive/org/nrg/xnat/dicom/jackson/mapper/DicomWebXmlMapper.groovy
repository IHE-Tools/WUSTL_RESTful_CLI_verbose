package org.nrg.xnat.dicom.jackson.mapper

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.dataformat.xml.XmlMapper
import org.nrg.xnat.dicom.jackson.module.XmlXnatDicomWebRestDeserializers

class DicomWebXmlMapper {

    public static final ObjectMapper INSTANCE = cache()

    private static ObjectMapper cache() {
        final ObjectMapper mapper = new XmlMapper()

        mapper.registerModule(XmlXnatDicomWebRestDeserializers.build())

        mapper
    }

}
