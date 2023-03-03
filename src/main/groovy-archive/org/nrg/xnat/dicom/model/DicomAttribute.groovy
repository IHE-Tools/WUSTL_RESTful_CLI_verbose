package org.nrg.xnat.dicom.model

import com.fasterxml.jackson.annotation.JsonIgnore
import com.fasterxml.jackson.annotation.JsonInclude
//import com.fasterxml.jackson.annotation.Json
import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.databind.annotation.JsonSerialize
import org.dcm4che3.data.VR
import org.nrg.xnat.dicom.DicomValidationException

import static org.dcm4che3.data.VR.*

@com.fasterxml.jackson.annotation.JsonInclude(com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL)
//@JsonSerialize(include = Inclusion.NON_NULL)
class DicomAttribute<T> {

    public static final List<VR> STRING_VRS      = [AE, AS, AT, CS, DA, DT, LO, LT, OB, OD, OF, OW, SH, ST, TM, UI, UN, UR, UT]
    public static final List<VR> DOUBLE_VRS      = [DS, FL, FD]
    public static final List<VR> LONG_VRS        = [IS, SL, SS, UL, US]
    public static final List<VR> PERSON_NAME_VRS = [PN]
    public static final List<VR> SEQUENCE_VRS    = [SQ]
    public static final Map<Class, List<VR>> VR_CLASS_MAP = [(String) : STRING_VRS, (Double) : DOUBLE_VRS, (Long) : LONG_VRS, (DicomPersonNames) : PERSON_NAME_VRS, (DicomObject) : SEQUENCE_VRS]

    @JsonIgnore Class<T> type
    @JsonIgnore String keyword
    @JsonIgnore String tag
    VR vr
    String privateCreator
    @JsonProperty('BulkDataURI') BulkData bulkData
    @JsonProperty('InlineBinary') String inlineBinary
    @JsonProperty('Value') List<T> value

    DicomAttribute(Class<T> type) {
        this.type = type
    }

    static DicomAttribute<?> ofVR(VR vr) {
        new DicomAttribute<?>(VR_CLASS_MAP.find{ vr in it.value }.key).vr(vr)
    }

    static DicomAttribute<?> ofSingleton(Object value) {
        new DicomAttribute<?>((value instanceof Integer) ? Long : value.class).value(Collections.singletonList(value))
    }

    static DicomAttribute<String> ofStrings(String... strings) {
        new DicomAttribute<>(String).value(strings as List)
    }

    static DicomAttribute<String> ofDoubles(Double... doubles) {
        new DicomAttribute<>(Double).value(doubles as List)
    }

    static DicomAttribute<String> ofLongs(Long... longs) {
        new DicomAttribute<>(Long).value(longs as List)
    }

    static DicomAttribute<String> ofPersonNames(DicomPersonNames... names) {
        new DicomAttribute<>(DicomPersonNames).value(names as List).vr(PN)
    }

    static DicomAttribute<String> ofSequenceItems(DicomObject... objects) {
        new DicomAttribute<>(DicomObject).value(objects as List).vr(SQ)
    }

    @JsonIgnore
    String getSingletonStringValue() {
        (value == null) ? null : value.get(0) as String
    }

    @JsonIgnore
    Double getSingletonDoubleValue() {
        (value == null) ? null : value.get(0) as Double
    }

    @JsonIgnore
    Long getSingletonLongValue() {
        (value == null) ? null : value.get(0) as Long
    }

    @JsonIgnore
    DicomPersonNames getSingletonDicomPersonNames() {
        (value == null) ? null : value.get(0) as DicomPersonNames
    }

    DicomAttribute<T> keyword(String keyword) {
        setKeyword(keyword)
        this
    }

    DicomAttribute<T> tag(String tag) {
        setTag(tag)
        this
    }

    DicomAttribute<T> vr(VR vr) {
        setVr(vr)
        this
    }

    DicomAttribute<T> privateCreator(String privateCreator) {
        setPrivateCreator(privateCreator)
        this
    }

    DicomAttribute<T> bulkData(BulkData bulkData) {
        setBulkData(bulkData)
        this
    }

    DicomAttribute<T> inlineBinary(String binary) {
        setInlineBinary(binary)
        this
    }

    DicomAttribute<T> value(List<T> value) {
        setValue(value)
        this
    }

    void validate() throws DicomValidationException {
        validatePossibleDataChildren()
        validateTypeVRMatch()
    }

    void validatePossibleDataChildren() throws DicomValidationException {
        int numValuesPresent = 0
        if (bulkData != null) numValuesPresent++
        if (inlineBinary != null) numValuesPresent++
        if (value != null && !value.isEmpty()) numValuesPresent++
        if (numValuesPresent > 1) throw new DicomValidationException('At most one of BulkData, InlineBinary, and Value can be set.')
    }

    void validateTypeVRMatch() throws DicomValidationException {
        final List<VR> acceptableVRsForType = VR_CLASS_MAP[type] ?: []
        if (!(vr in acceptableVRsForType)) throw new DicomValidationException("DicomAttribute cannot store a list of values using the type ${type.simpleName} with corresponding VR ${vr}.")
    }

    boolean equals(o) {
        if (this.is(o)) return true
        if (!(o instanceof DicomAttribute)) return false

        DicomAttribute that = (DicomAttribute) o

        if (bulkData != that.bulkData) return false
        if (inlineBinary != that.inlineBinary) return false
        if (keyword != that.keyword) return false
        if (privateCreator != that.privateCreator) return false
        if (tag != that.tag) return false
        if (type != that.type) return false
        if (value != that.value) return false
        if (vr != that.vr) return false

        return true
    }

    int hashCode() {
        int result
        result = (type != null ? type.hashCode() : 0)
        result = 31 * result + (keyword != null ? keyword.hashCode() : 0)
        result = 31 * result + (tag != null ? tag.hashCode() : 0)
        result = 31 * result + (vr != null ? vr.hashCode() : 0)
        result = 31 * result + (privateCreator != null ? privateCreator.hashCode() : 0)
        result = 31 * result + (bulkData != null ? bulkData.hashCode() : 0)
        result = 31 * result + (inlineBinary != null ? inlineBinary.hashCode() : 0)
        result = 31 * result + (value != null ? value.hashCode() : 0)
        return result
    }

}
