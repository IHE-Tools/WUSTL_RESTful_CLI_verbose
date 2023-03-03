package org.nrg.xnat.dicom.jackson.deserializer.json

import com.fasterxml.jackson.core.JsonProcessingException
import com.fasterxml.jackson.core.ObjectCodec
import com.fasterxml.jackson.databind.JsonNode
import org.nrg.xnat.dicom.model.DicomAttribute
import org.nrg.xnat.dicom.model.DicomObject
import org.nrg.xnat.jackson.deserializers.CustomDeserializer

class JsonDicomObjectDeserializer extends CustomDeserializer<DicomObject> {

    @Override
    DicomObject deserialize(ObjectCodec objectCodec, JsonNode node) throws IOException, JsonProcessingException {
        final DicomObject dicomObject = new DicomObject()

        dicomObject.attributes = readMapObjectList(node, objectCodec, DicomAttribute, 'tag')

        dicomObject
    }

}
