package org.nrg.xnat.dicom.jackson.deserializer.xml

import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.databind.deser.std.StdDeserializer
import com.fasterxml.jackson.dataformat.xml.deser.FromXmlParser

abstract class CustomXmlDeserializer<T> extends StdDeserializer<T> {

    CustomXmlDeserializer(Class<?> vc) {
        super(vc)
    }

    @SuppressWarnings("GrMethodMayBeStatic")
    protected void validateCurrentTag(JsonParser p, String expectedTagName) throws IOException {
        final String tagName = (p as FromXmlParser).staxReader.localName

        if (tagName != expectedTagName) {
            throw new IOException("Tag name for Dicom Object (${tagName}) does not match expected tag name (${expectedTagName}).")
        }
    }

}
