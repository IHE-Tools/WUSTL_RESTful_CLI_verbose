package org.nrg.xnat.dicom.jackson.module

import com.fasterxml.jackson.databind.module.SimpleModule
import org.nrg.xnat.dicom.jackson.deserializer.xml.DicomPersonNamesDeserializer
import org.nrg.xnat.dicom.jackson.deserializer.xml.NativeDicomModelDeserializer
import org.nrg.xnat.dicom.model.DicomObject
import org.nrg.xnat.dicom.model.DicomPersonNames
import org.nrg.xnat.dicom.model.SequenceItem

class XmlXnatDicomWebRestDeserializers {

    static SimpleModule build() {
        final SimpleModule module = new SimpleModule("XML_XNAT_DICOM_Web_Deserializers")

        module.addDeserializer(DicomObject, new NativeDicomModelDeserializer(DicomObject, false))
        module.addDeserializer(SequenceItem, new NativeDicomModelDeserializer(DicomObject, true))
        module.addDeserializer(DicomPersonNames, new DicomPersonNamesDeserializer(DicomPersonNames))

        module
    }
}
