package org.nrg.xnat.dicom.enums

enum WadoRequestType {

    STUDY {
        @Override
        String urlPathFor(String studyInstanceUID, String seriesInstanceUID, String sopInstanceUID) {
            "/studies/${studyInstanceUID}"
        }
    },
    SERIES {
        @Override
        String urlPathFor(String studyInstanceUID, String seriesInstanceUID, String sopInstanceUID) {
            "/studies/${studyInstanceUID}/series/${seriesInstanceUID}"
        }
    },
    INSTANCE {
        @Override
        String urlPathFor(String studyInstanceUID, String seriesInstanceUID, String sopInstanceUID) {
            "/studies/${studyInstanceUID}/series/${seriesInstanceUID}/instances/${sopInstanceUID}"
        }
    }

    abstract String urlPathFor(String studyInstanceUID, String seriesInstanceUID, String sopInstanceUID)

}
