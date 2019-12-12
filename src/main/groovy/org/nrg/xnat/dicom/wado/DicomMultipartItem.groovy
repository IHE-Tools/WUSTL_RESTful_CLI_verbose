package org.nrg.xnat.dicom.wado

import org.dcm4che3.data.DatasetWithFMI

class DicomMultipartItem implements WadoResponseItem {

    String reportedTSUID
    DatasetWithFMI dicom

    DicomMultipartItem reportedTSUID(String uid) {
        setReportedTSUID(uid)
        this
    }

    DicomMultipartItem dicom(DatasetWithFMI dicom) {
        setDicom(dicom)
        this
    }

}
