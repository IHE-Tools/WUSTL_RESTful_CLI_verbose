package edu.wustl.cil.TestingWIA;

import java.util.List;
import java.util.Map;
import java.util.HashMap;

//import org.nrg.xnat.dicom.model.DicomObject;

public class DicomFactory {
/*
    static public Map<String, DicomObject> constructDicomStudySet(List<DicomObject> arrayList) {
        HashMap<String, DicomObject> map = new HashMap<>();

        for (DicomObject e: arrayList) {
//            DicomObject study = new DicomObject(e);
            String id = e.studyInstanceUid();
            map.put(id, e);
        }

        return map;
    }
    static public Map<String, DicomObject> constructDicomStudySet(DicomObject[] arrayList) {
        if (arrayList == null) return null;

        HashMap<String, DicomObject> map = new HashMap<>();

        for (DicomObject e: arrayList) {
//            DicomObject study = new DicomObject(e);
            String id = e.studyInstanceUid();
            map.put(id, e);
        }

        return map;
    }

    static public Map<String, DicomObject> constructDicomSetFromArray(DicomObject[] arrayList, String level) throws Exception {
        if (arrayList == null) return null;

        HashMap<String, DicomObject> map = new HashMap<>();

        for (DicomObject e: arrayList) {
            String id;
            switch(level) {
                case "Study":
                    id = e.studyInstanceUid();
                    break;
                case "Series":
                    id = e.seriesInstanceUid();
                    break;
                case "Instance":
                    id = e.sopInstanceUID();
                    break;
                default:
                    throw new Exception("Did not recognize value for level: " + level);
            }

            map.put(id, e);
        }
        return map;
    }

 */
}
