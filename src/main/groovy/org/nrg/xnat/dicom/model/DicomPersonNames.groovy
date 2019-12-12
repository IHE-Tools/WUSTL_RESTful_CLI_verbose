package org.nrg.xnat.dicom.model

import com.fasterxml.jackson.annotation.JsonIgnore
import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonProperty

@JsonInclude(JsonInclude.Include.NON_NULL)
class DicomPersonNames {

    @JsonProperty('Alphabetic') String alphabetic
    @JsonProperty('Ideographic') String ideographic
    @JsonProperty('Phonetic') String phonetic
    @JsonIgnore int number

    DicomPersonNames alphabetic(String name) {
        setAlphabetic(name)
        this
    }

    DicomPersonNames ideographic(String name) {
        setIdeographic(name)
        this
    }

    DicomPersonNames phonetic(String name) {
        setPhonetic(name)
        this
    }

    boolean equals(o) {
        if (this.is(o)) return true
        if (!(o instanceof DicomPersonNames)) return false

        DicomPersonNames that = (DicomPersonNames) o

        if (alphabetic != that.alphabetic) return false
        if (ideographic != that.ideographic) return false
        if (phonetic != that.phonetic) return false

        return true
    }

    int hashCode() {
        int result
        result = (alphabetic != null ? alphabetic.hashCode() : 0)
        result = 31 * result + (ideographic != null ? ideographic.hashCode() : 0)
        result = 31 * result + (phonetic != null ? phonetic.hashCode() : 0)
        return result
    }

}
