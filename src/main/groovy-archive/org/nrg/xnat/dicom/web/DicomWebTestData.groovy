package org.nrg.xnat.dicom.web

import org.dcm4che3.data.DatasetWithFMI
import org.dcm4che3.data.Tag
import org.nrg.xnat.util.DicomUtils
import org.nrg.xnat.util.FileIOUtils

abstract class DicomWebTestData {

    Map<String, DatasetWithFMI> instanceMap

    Map<String, DatasetWithFMI> getInstanceMap() {
        if (instanceMap == null) {
            instanceMap = findAllInstances()
        }
        instanceMap
    }

    abstract DicomWebTestStudy parentStudy()

    protected abstract Map<String, DatasetWithFMI> filter(Map<String, DatasetWithFMI> instances)

    private Map<String, DatasetWithFMI> findAllInstances() {
        final Map<String, DatasetWithFMI> instances = parentStudy().data.collect { data ->
            FileIOUtils.listFilesRecursively(data.toDirectory())
        }.flatten().collectEntries { dicomFile ->
            final DatasetWithFMI datasetWithFMI = DicomUtils.readDicom(dicomFile as File)
            [(datasetWithFMI.dataset.getString(Tag.SOPInstanceUID)): datasetWithFMI]
        }
        filter(instances)
        instances
    }

}
