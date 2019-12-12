package org.nrg.xnat.dicom.jackson.serializer

import com.fasterxml.jackson.core.JsonGenerator
import com.fasterxml.jackson.databind.SerializerProvider
import com.fasterxml.jackson.databind.ser.std.StdSerializer
import org.nrg.xnat.dicom.model.BulkData

class BulkDataSerializer extends StdSerializer<BulkData> {

    BulkDataSerializer(Class<BulkData> t) {
        super(t)
    }

    @Override
    void serialize(BulkData value, JsonGenerator gen, SerializerProvider provider) {
        gen.writeString(value.uri)
    }

}
