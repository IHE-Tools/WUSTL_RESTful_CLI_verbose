package org.nrg.xnat.dicom.jackson.module

import com.fasterxml.jackson.databind.module.SimpleModule
import org.nrg.xnat.dicom.jackson.deserializer.json.DicomAttributeDeserializer
import org.nrg.xnat.dicom.jackson.deserializer.json.JsonDicomObjectDeserializer
import org.nrg.xnat.dicom.model.DicomAttribute
import org.nrg.xnat.dicom.model.DicomObject

class JsonDicomWebDeserializationModule {

    static SimpleModule build() {
        final SimpleModule module = new SimpleModule("JSON_XNAT_DicomWeb_Deserializers")

        module.addDeserializer(DicomObject, new JsonDicomObjectDeserializer())
        module.addDeserializer(DicomAttribute, new DicomAttributeDeserializer())

        module
    }
}
