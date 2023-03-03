package org.nrg.xnat.dicom.jackson.deserializer.xml

import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.core.JsonProcessingException
import com.fasterxml.jackson.core.JsonToken
import com.fasterxml.jackson.databind.DeserializationContext
import com.fasterxml.jackson.databind.deser.std.StringDeserializer
import org.apache.commons.lang3.StringUtils
import org.dcm4che3.data.PersonName
import org.nrg.xnat.dicom.jackson.deserializer.DefaultInstances
import org.nrg.xnat.dicom.model.DicomPersonNames

class DicomPersonNamesDeserializer extends CustomXmlDeserializer<DicomPersonNames> {

    DicomPersonNamesDeserializer(Class<?> vc) {
        super(vc)
    }

    @Override
    DicomPersonNames deserialize(JsonParser p, DeserializationContext ctxt) throws IOException, JsonProcessingException {
        final DicomPersonNames names = new DicomPersonNames()
        final PersonName dcm4cheNamesObject = new PersonName()

        if (p.currentToken != JsonToken.START_OBJECT) {
            throw new IOException('XML did not start PersonName object correctly.')
        }

        validateCurrentTag(p, 'PersonName')

        if (p.nextFieldName() != 'number') throw new IOException('PersonName element should have number attribute.')
        p.nextToken()
        names.setNumber(DefaultInstances.intDeserializer.deserialize(p, ctxt))

        String nameType
        while ((nameType = p.nextFieldName()) != null) {
            p.nextToken()
            PersonName.Group nameGroup
            switch (nameType) {
                case 'Alphabetic':
                    nameGroup = PersonName.Group.Alphabetic
                    break
                case 'Ideographic':
                    nameGroup = PersonName.Group.Ideographic
                    break
                case 'Phonetic':
                    nameGroup = PersonName.Group.Phonetic
                    break
                default:
                    throw new IOException("Unknown name type in PersonNames tag: ${nameType}.")
            }
            setNames(p, ctxt, dcm4cheNamesObject, nameGroup)
        }

        final String parsedAlphabetic = dcm4cheNamesObject.toString(PersonName.Group.Alphabetic, true)
        final String parsedIdeographic = dcm4cheNamesObject.toString(PersonName.Group.Ideographic, true)
        final String parsedPhonetic = dcm4cheNamesObject.toString(PersonName.Group.Phonetic, true)
        if (StringUtils.isNotEmpty(parsedAlphabetic)) names.alphabetic(parsedAlphabetic)
        if (StringUtils.isNotEmpty(parsedIdeographic)) names.ideographic(parsedIdeographic)
        if (StringUtils.isNotEmpty(parsedPhonetic)) names.phonetic(parsedPhonetic)
        names
    }

    @SuppressWarnings("GrMethodMayBeStatic")
    private void setNames(JsonParser p, DeserializationContext ctxt, PersonName personName, PersonName.Group nameType) {
        String tag
        while ((tag = p.nextFieldName()) != null) {
            p.nextToken()
            personName.set(nameType, EnumSet.allOf(PersonName.Component).find { it.name() == tag }, StringDeserializer.instance.deserialize(p, ctxt))
        }
    }

}
