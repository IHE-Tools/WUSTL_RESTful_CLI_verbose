package org.nrg.xnat.dicom.jackson.deserializer.json

import com.fasterxml.jackson.core.JsonProcessingException
import com.fasterxml.jackson.core.ObjectCodec
import com.fasterxml.jackson.databind.JsonNode
import org.dcm4che3.data.VR
import org.nrg.xnat.dicom.model.BulkData
import org.nrg.xnat.dicom.model.DicomAttribute
import org.nrg.xnat.jackson.deserializers.CustomDeserializer

class DicomAttributeDeserializer extends CustomDeserializer<DicomAttribute> {

    @Override
    DicomAttribute deserialize(ObjectCodec objectCodec, JsonNode node) throws IOException, JsonProcessingException {
        final VR vr = readEnumByName(node.get('vr').asText(), VR)
        final DicomAttribute<?> content = DicomAttribute.ofVR(vr)

        content.setVr(vr)
        if (fieldNonnull(node, 'BulkDataURI')) {
            content.setBulkData(new BulkData().uri(node.get('BulkDataURI').asText()))
        }
        setStringIfNonnull(node, 'InlineBinary', content.&setInlineBinary)
        if (fieldNonnull(node, 'Value')) {
            setObjectList(node, 'Value', objectCodec, content.type, content.&setValue)
        }
        setStringIfNonnull(node, 'tag', content.&setTag) // injected via JsonDicomObjectDeserializer

        content
    }

}
