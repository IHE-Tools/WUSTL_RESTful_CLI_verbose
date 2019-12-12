package org.nrg.xnat.dicom.jackson.deserializer

import com.fasterxml.jackson.databind.JsonDeserializer
import com.fasterxml.jackson.databind.deser.std.NumberDeserializers

class DefaultInstances {
    public static final JsonDeserializer<Integer> intDeserializer = new NumberDeserializers.IntegerDeserializer(Integer, -1)
}
