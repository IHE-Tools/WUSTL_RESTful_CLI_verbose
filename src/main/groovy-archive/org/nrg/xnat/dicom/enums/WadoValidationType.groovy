package org.nrg.xnat.dicom.enums

import org.dcm4che3.data.DatasetWithFMI
import org.nrg.testing.xnat.processing.files.comparators.imaging.DiffedImage
import org.nrg.xnat.util.DicomUtils

import static org.testng.AssertJUnit.assertEquals
import static org.dcm4che3.data.Tag.*

enum WadoValidationType {

    FULL_VALIDATION { // Checks that all headers are equal. Very thorough but not appropriate if DICOM has been modified by XNAT or transcoded in response
        @Override
        void validate(DatasetWithFMI sourceDicom, DatasetWithFMI responseDicom) {
            assertEquals(sourceDicom, responseDicom)
        }
    },

    IDENTICAL_IMAGES_AFTER_TRANSCODING {
        @Override
        void validate(DatasetWithFMI sourceDicom, DatasetWithFMI responseDicom) {
            assertEquals(0, new DiffedImage(DicomUtils.writeDicomToFile(sourceDicom), DicomUtils.writeDicomToFile(responseDicom)).getNumNonzeroPixels())
            BLACKLISTED_ELEMENTS_FROM_COMPARISON.each { blacklistedElement ->
                final boolean isMetaTag = blacklistedElement < DicomUtils.SMALLEST_DATASET_HEADER
                [sourceDicom, responseDicom].each { dicom ->
                    (isMetaTag ? dicom.fileMetaInformation : dicom.dataset).remove(blacklistedElement)
                }
            }
            FULL_VALIDATION.validate(sourceDicom, responseDicom)
        }
    }

    private static final List<Integer> BLACKLISTED_ELEMENTS_FROM_COMPARISON = [TransferSyntaxUID, PixelData]

    abstract void validate(DatasetWithFMI sourceDicom, DatasetWithFMI responseDicom)

}