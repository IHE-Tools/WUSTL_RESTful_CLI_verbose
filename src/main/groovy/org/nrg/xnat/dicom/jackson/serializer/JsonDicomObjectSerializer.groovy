package org.nrg.xnat.dicom.jackson.serializer

import com.fasterxml.jackson.core.JsonGenerator
import com.fasterxml.jackson.databind.SerializerProvider
import com.fasterxml.jackson.databind.ser.std.StdSerializer
import org.nrg.xnat.dicom.model.DicomObject

class JsonDicomObjectSerializer extends StdSerializer<DicomObject> {

    JsonDicomObjectSerializer(Class<DicomObject> t) {
        super(t)
    }

    @Override
    void serialize(DicomObject value, JsonGenerator gen, SerializerProvider provider) {
        gen.writeStartObject()
        value.attributes.each { attribute ->
            gen.writeObjectField(attribute.tag, attribute)
        }
        gen.writeEndObject()
    }

}
