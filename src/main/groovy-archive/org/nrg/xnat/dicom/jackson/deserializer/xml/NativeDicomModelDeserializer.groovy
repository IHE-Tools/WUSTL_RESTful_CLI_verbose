package org.nrg.xnat.dicom.jackson.deserializer.xml

import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.core.JsonProcessingException
import com.fasterxml.jackson.core.JsonToken
import com.fasterxml.jackson.databind.DeserializationContext
import com.fasterxml.jackson.databind.JsonDeserializer
import com.fasterxml.jackson.dataformat.xml.deser.FromXmlParser
import org.nrg.xnat.dicom.jackson.deserializer.DefaultInstances
import org.nrg.xnat.dicom.model.DicomAttribute
import org.nrg.xnat.dicom.model.DicomObject
import org.nrg.xnat.dicom.model.SequenceItem

import javax.xml.namespace.QName
import javax.xml.stream.XMLStreamReader

class NativeDicomModelDeserializer extends CustomXmlDeserializer<DicomObject> {

    private final JsonDeserializer<DicomAttribute> dicomAttributeDeserializer = new DicomAttributeDeserializer(DicomAttribute)
    boolean withinSequence

    NativeDicomModelDeserializer(Class<?> vc, boolean withinSequence) {
        super(vc)
        setWithinSequence(withinSequence)
    }

    @Override
    DicomObject deserialize(JsonParser p, DeserializationContext ctxt) throws IOException, JsonProcessingException {
        final DicomObject dicomObject = (withinSequence) ? new SequenceItem() : new DicomObject()

        if (p.currentToken != JsonToken.START_OBJECT) {
            throw new IOException('XML did not start NativeDicomModel object correctly.')
        }

        validateCurrentTag(p, (withinSequence) ? 'Item' : 'NativeDicomModel')

        if (withinSequence) {
            if (p.nextFieldName() != 'number') throw new IOException('[Sequence] Item element should have number attribute.')
            p.nextToken()
            (dicomObject as SequenceItem).setNumber(DefaultInstances.intDeserializer.deserialize(p, ctxt))
        } else {
            XMLStreamReader staxReader = (p as FromXmlParser).staxReader
            final QName attributeName = staxReader.getAttributeName(0)
            if (("${attributeName.prefix}:${attributeName.localPart}") != 'xml:space') throw new IOException('<NativeDicomModel> tag must have xml:space attribute')
            p.nextToken()
            if (p.nextTextValue() != 'preserve') throw new IOException('xml:space attribute must have value "preserve".')
        }

        while (p.nextFieldName() != null) {
            p.nextToken()
            dicomObject.attributes << dicomAttributeDeserializer.deserialize(p, ctxt)
        }

        dicomObject
    }

}
