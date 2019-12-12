package org.nrg.xnat.dicom.jackson.deserializer.xml

import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.core.JsonProcessingException
import com.fasterxml.jackson.core.JsonToken
import com.fasterxml.jackson.databind.DeserializationContext
import com.fasterxml.jackson.databind.deser.std.StringDeserializer
import org.dcm4che3.data.VR
import org.nrg.xnat.dicom.jackson.deserializer.DefaultInstances
import org.nrg.xnat.dicom.model.BulkData
import org.nrg.xnat.dicom.model.DicomAttribute
import org.nrg.xnat.dicom.model.DicomObject
import org.nrg.xnat.dicom.model.DicomPersonNames
import org.nrg.xnat.dicom.model.SequenceItem

class DicomAttributeDeserializer extends CustomXmlDeserializer<DicomAttribute> {

    DicomAttributeDeserializer(Class<?> vc) {
        super(vc)
    }

    @SuppressWarnings("GroovyAssignabilityCheck")
    @Override
    DicomAttribute deserialize(JsonParser p, DeserializationContext ctxt) throws IOException, JsonProcessingException {
        if (p.currentToken != JsonToken.START_OBJECT) {
            throw new IOException('XML did not start DicomAttribute object correctly.')
        }

        validateCurrentTag(p, 'DicomAttribute')

        String keyword = null
        String tag = null
        VR vr = null
        String privateCreator = null
        final List values = []
        BulkData bulkData = null
        String inlineBinary = null

        String fieldName
        while ((fieldName = p.nextFieldName()) != null) {
            p.nextToken()
            switch (fieldName) {
                case 'keyword':
                    keyword = StringDeserializer.instance.deserialize(p, ctxt)
                    break
                case 'tag':
                    tag = StringDeserializer.instance.deserialize(p, ctxt)
                    break
                case 'vr':
                    vr = EnumSet.allOf(VR).find { it.name() == StringDeserializer.instance.deserialize(p, ctxt) }
                    break
                case 'privateCreator':
                    privateCreator = StringDeserializer.instance.deserialize(p, ctxt)
                    break
                case 'Value':
                    if (p.nextFieldName() != 'number') throw new IOException('Value element should have number attribute.')
                    p.nextToken()
                    if (DefaultInstances.intDeserializer.deserialize(p, ctxt) != values.size() + 1) throw new IOException("Value element's number attribute should be 1 + the number of processed elements.")
                    final String plainValueName = p.nextFieldName()
                    if (plainValueName != null) {
                        if (plainValueName != '') {
                            throw new IOException("Value should have plain character data instead of another attribute: ${plainValueName}.")
                        } else {
                            p.nextToken()
                            values << StringDeserializer.instance.deserialize(p, ctxt)
                            if (p.nextToken() != JsonToken.END_OBJECT) {
                                throw new IOException('Value should have ended here.')
                            }
                        }
                    } else {
                        values << null
                    }
                    break
                case 'Item':
                    values << p.codec.readValue(p, SequenceItem)
                    if ((values.last() as SequenceItem).number != values.size()) throw new IOException("Value element's number attribute should be the number of processed elements.")
                    break
                case 'PersonName':
                    values << p.codec.readValue(p, DicomPersonNames)
                    if ((values.last() as DicomPersonNames).number != values.size()) throw new IOException("PersonName element's number attribute should be the number of processed elements.")
                    break
                case 'BulkData':
                    bulkData = ctxt.readValue(p, BulkData)
                    break
                case 'InlineBinary':
                    if (p.currentToken != JsonToken.VALUE_STRING) throw new IOException('Inline binary should only have text stored in the element (and no attributes).')
                    inlineBinary = StringDeserializer.instance.deserialize(p, ctxt)
                    break
                default:
                    throw new IOException("Unknown attribute in a <DicomAttribute> tag: ${fieldName}.")
            }
        }

        final DicomAttribute<?> attribute = DicomAttribute.ofVR(vr).keyword(keyword).tag(tag).privateCreator(privateCreator).bulkData(bulkData).inlineBinary(inlineBinary)


        if (!(values.isEmpty())) {
            switch (DicomAttribute.VR_CLASS_MAP.find { vr in it.value }.key) {
                case String:
                case DicomPersonNames:
                case DicomObject:
                    attribute.value(values)
                    break
                case Double:
                    attribute.setValue(values.collect { Double.parseDouble(it) })
                    break
                case Long:
                    attribute.setValue(values.collect{ Long.parseLong(it) })
                    break
            }
        }

        attribute
    }

}
