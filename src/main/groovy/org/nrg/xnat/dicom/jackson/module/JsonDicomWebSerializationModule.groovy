package org.nrg.xnat.dicom.jackson.module

import com.fasterxml.jackson.databind.module.SimpleModule
import org.nrg.xnat.dicom.jackson.serializer.BulkDataSerializer
import org.nrg.xnat.dicom.jackson.serializer.JsonDicomObjectSerializer
import org.nrg.xnat.dicom.model.BulkData
import org.nrg.xnat.dicom.model.DicomObject

class JsonDicomWebSerializationModule {

    static SimpleModule build() {
        final SimpleModule module = new SimpleModule("JSON_XNAT_DicomWeb_Serializers")

        module.addSerializer(DicomObject, new JsonDicomObjectSerializer(DicomObject))
        module.addSerializer(BulkData, new BulkDataSerializer(BulkData))

        module
    }
}
