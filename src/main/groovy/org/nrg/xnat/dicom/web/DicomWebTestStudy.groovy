package org.nrg.xnat.dicom.web

import groovy.transform.AutoClone
import org.dcm4che3.data.DatasetWithFMI
import org.nrg.testing.enums.TestData
import org.nrg.xnat.dicom.wado.AcceptableWadoResponse
import org.nrg.xnat.dicom.qido.AcceptableQidoResponse
import org.nrg.xnat.util.TimeUtils

import java.time.LocalDate

import static org.nrg.testing.enums.TestData.*
import static DicomWebTestSeries.*

@AutoClone(excludes = 'series')
class DicomWebTestStudy extends DicomWebTestData implements AcceptableQidoResponse, AcceptableWadoResponse {

    List<TestData> data
    String studyDate
    String studyTime
    String accessionNumber
    Set<String> modalitiesInStudy = []
    String patientName
    String patientID
    String studyInstanceUID
    String studyID
    String patientBirthdate
    String patientSex
    long numStudyRelatedSeries
    long numStudyRelatedInstances
    Set<DicomWebTestSeries> series = []

    // Data in TestsWithStandardDataSetup
    public static final DicomWebTestStudy CT1 = new DicomWebTestStudy().data(DICOM_WEB_CT1).studyDate('20170101').studyTime('143546').accessionNumber('5498216673989931').patientName('TCGA-CS-6186').patientID('TCGA-CS-6186').patientSex('M').series([CT1_CT_1, CT1_CT_2, CT1_CT_3, CT1_CT_4])
    public static final DicomWebTestStudy CT2 = new DicomWebTestStudy().data(DICOM_WEB_CT2).studyDate('20170101').studyTime('212603').accessionNumber('3205915794460453').patientName('TCGA-14-1794').patientID('TCGA-14-1794').patientSex('M').series([CT2_CT_1, CT2_CT_2, CT2_CT_200])
    public static final DicomWebTestStudy CT3 = new DicomWebTestStudy().data(DICOM_WEB_CT3).studyDate('20170303').studyTime('183531').accessionNumber('3001140007623749').patientName('TCGA-14-1794').patientID('TCGA-14-1794').patientSex('M').series([CT3_CT_1, CT3_CT_2, CT3_CT_3])
    public static final DicomWebTestStudy CT4 = new DicomWebTestStudy().data(DICOM_WEB_CT4).studyDate('20170101').studyTime('212156').accessionNumber('2771128247345677').patientName('TCGA-19-1787').patientID('TCGA-19-1787').patientSex('M').series([CT4_CT_1, CT4_CT_2, CT4_CT_501])
    public static final DicomWebTestStudy CT5 = new DicomWebTestStudy().data(DICOM_WEB_CT5).studyDate('20170404').studyTime('212342').accessionNumber('7293749322131698').patientName('TCGA-14-1459').patientID('TCGA-14-1459').patientSex('F').series([CT5_CT_1, CT5_CT_2, CT5_CT_3])
    public static final DicomWebTestStudy CT6 = new DicomWebTestStudy().data(DICOM_WEB_CT6).studyDate('20170505').studyTime('211816').accessionNumber('9396451424767484').patientName('TCGA-14-1459').patientID('TCGA-14-1459').patientSex('F').series([CT6_CT_1, CT6_CT_2, CT6_CT_3])
    public static final DicomWebTestStudy CT7 = new DicomWebTestStudy().data(DICOM_WEB_CT7).studyDate('20170101').studyTime('091823').accessionNumber('3011926318851597').patientName('TCGA-06-5410').patientID('TCGA-06-5410').studyID('123456789').patientSex('F').series([CT7_CT_8, CT7_CT_9, CT7_CT_10, CT7_CT_11])
    public static final DicomWebTestStudy CT8 = new DicomWebTestStudy().data(DICOM_WEB_CT8).studyDate('20171010').studyTime('144348').accessionNumber('1547753158701809').patientName('TCGA-14-0783').patientID('TCGA-14-0783').patientSex('F').series([CT8_CT_1, CT8_CT_2])
    public static final DicomWebTestStudy MR1 = new DicomWebTestStudy().data(DICOM_WEB_MR1).studyDate('20170101').studyTime('091936').accessionNumber('2614586945879657').patientName('TCGA-CS-6186').patientID('TCGA-CS-6186').patientSex('M').series([MR1_MR_3, MR1_MR_4])
    public static final DicomWebTestStudy MR2 = new DicomWebTestStudy().data(DICOM_WEB_MR2).studyDate('20170303').studyTime('073440').accessionNumber('1835939773555709').patientName('TCGA-19-1787').patientID('TCGA-19-1787').patientSex('M').series([MR2_MR_3, MR2_MR_4])
    public static final DicomWebTestStudy MR3 = new DicomWebTestStudy().data(DICOM_WEB_MR3).studyDate('20170404').studyTime('094437').accessionNumber('4930324029839549').patientName('TCGA-06-5410').patientID('TCGA-06-5410').patientSex('F').series([MR3_MR_4, MR3_MR_6])
    public static final DicomWebTestStudy MR4 = new DicomWebTestStudy().data(DICOM_WEB_MR4).studyDate('20170909').studyTime('090706').accessionNumber('2697095860614577').patientName('TCGA-14-0783').patientID('TCGA-14-0783').patientSex('F').series([MR4_MR_3, MR4_MR_5])
    // Data in TestsWithComplexData
    public static final DicomWebTestStudy PETCT1 = new DicomWebTestStudy().data(DICOM_WEB_PETCT1).studyDate('19990823').studyTime('192847').accessionNumber('2819497684894126').patientName('0522c0001').patientID('0522c0001').patientBirthdate('19500101').studyID('0522c0001_0000').series([PETCT1_CT_2, PETCT1_PT_605, PETCT1_PT_606])
    public static final DicomWebTestStudy PETCT2 = new DicomWebTestStudy().data([DICOM_WEB_PETCT2_PET, DICOM_WEB_PETCT2_CT]).studyDate('19991207').studyTime('131622').accessionNumber('2819497684894126').patientName('0522c0001').patientID('0522c0001').patientBirthdate('19500101').studyID('0522c0001_0001').series([PETCT2_CT_2, PETCT2_PT_605, PETCT2_PT_606])
    public static final DicomWebTestStudy CTRT1 = new DicomWebTestStudy().data(DICOM_WEB_CTRT1).studyDate('19990831').patientName('0522c0001').patientID('0522c0001').patientBirthdate('19500101').studyID('0522c0001_0003').series([CTRT1_CT_1, CTRT1_RTSTRUCT_2, CTRT1_RTPLAN_3, CTRT1_RTDOSE_4])
    public static final DicomWebTestStudy CTRT2 = new DicomWebTestStudy().data(DICOM_WEB_CTRT2).patientName('0522c0002').patientID('0522c0002').studyID('522').series([CTRT2_CT_1, CTRT2_RTSTRUCT_2])
    public static final DicomWebTestStudy PETMR1 = new DicomWebTestStudy().data(DICOM_WEB_PETMR1).studyDate('20000907').studyTime('101142').accessionNumber('1764353264683505').patientName('STS_001').patientID('STS_001').studyID('M8O3XXWA').series([PETMR1_PT_1, PETMR1_MR_5436027, PETMR1_MR_5442056])
    public static final DicomWebTestStudy PETMR2 = new DicomWebTestStudy().data([DICOM_WEB_PETMR2_PT, DICOM_WEB_PETMR2_PT]).studyDate('20040401').studyTime('115338').accessionNumber('1111786747118502').patientName('STS_001').patientID('STS_001').patientBirthdate('19430819').studyID('B45PB88CX').series([PETMR2_PT_4, PETMR2_MR_5610035, PETMR2_MR_5616046])
    public static final DicomWebTestStudy PETMR3 = new DicomWebTestStudy().data(DICOM_WEB_PETMR3).studyDate('20000511').studyTime('111829').accessionNumber('1965292013600439').patientName('STS_040').patientID('STS_040').studyID('RQLNI8ZW3').series([PETMR3_PT_1, PETMR3_MR_6978035, PETMR3_MR_6984036])
    public static final DicomWebTestStudy MRPR = new DicomWebTestStudy().data(DICOM_WEB_MRPR).studyDate('20100430').studyTime('123437').accessionNumber('20100430').patientName('WATERMELON^FRUIT').patientID('W4T3RM310N').studyID('325773276').patientBirthdate('19800101').series([MRPR_MR_0, MRPR_PR_0_A, MRPR_PR_0_B, MRPR_MR_601, MRPR_MR_701])
    public static final DicomWebTestStudy MULTIFRAME_CT = new DicomWebTestStudy().data(DICOM_WEB_MULTIFRAME_CT).studyDate('20061219').studyTime('111154').accessionNumber('0010').patientName('Perfusion^MCA Stroke').patientID('0010').studyID('0010').patientBirthdate('19500704').patientSex('M').series([MULTIFRAME_CT_CT_1, MULTIFRAME_CT_CT_2, MULTIFRAME_CT_CT_3, MULTIFRAME_CT_CT_4])
    public static final DicomWebTestStudy MISSINGNO_MR = new DicomWebTestStudy().data(DICOM_WEB_MISSINGNO_MR).studyDate('20050712').accessionNumber('1260908518947618').patientName('103536').patientID('900-00-5346').patientBirthdate('19570818').series([MISSINGNO_MR_MR_1, MISSINGNO_MR_MR_2, MISSINGNO_MR_MR_3, MISSINGNO_MR_MR_EMPTY])
    public static final DicomWebTestStudy MG = new DicomWebTestStudy().data(DICOM_WEB_MG).studyDate('20061219').studyTime('074801').accessionNumber('8237622809490464').patientID('BreastDx-01-0025').patientSex('F').series([MG_MG_1, MG_MG_2, MG_MG_3])
    // Data in TestsForSecurity
    public static final DicomWebTestStudy PETMR4 = new DicomWebTestStudy().data([DICOM_WEB_PETMR4_PT, DICOM_WEB_PETMR4_MR]).studyDate('20010105').studyTime('102416').accessionNumber('3399931340487711').patientName('STS_045').patientID('STS_045').patientSex('M').series([PETMR4_PT_1, PETMR4_MR_4488021, PETMR4_MR_4494017])
    // Data in TestsWithDifferentTransferSyntaxes
    public static final DicomWebTestStudy BIG_ENDIAN_STUDY = new DicomWebTestStudy().data(BIG_ENDIAN).studyDate('19970424').studyTime('140438').patientName('Anonymized').series([BIG_ENDIAN_US_0, BIG_ENDIAN_US_1])
    public static final DicomWebTestStudy JPEGLOSSLESS = new DicomWebTestStudy().data(JPEGLOSSLESS_2000).studyDate('20000101').studyTime('102951').patientName('ANONYMIZED_SUBJ').patientID('ANONYMIZED_SESSION').patientSex('M').series([JPEGLOSSLESS_MR_3])
    public static final DicomWebTestStudy MIXED_FRAME = new DicomWebTestStudy().data(MIXED_FRAME_STUDY).studyDate('19990912').studyTime('141414').accessionNumber('2819497684894126').patientName('HF0608').patientID('HF0608').studyID('05011').patientBirthdate('19500704').patientSex('M')

    /*
    List<TestData> data
    String studyDate
    String studyTime
    String accessionNumber
    Set<String> modalitiesInStudy = []
    String patientName
    String patientID
    String studyInstanceUID
    String studyID
    String patientBirthdate
    String patientSex
    long numStudyRelatedSeries
    long numStudyRelatedInstances
    Set<DicomWebTestSeries> series = []
     */

    Set<String> getModalitiesInStudy() {
        !modalitiesInStudy.isEmpty() ? modalitiesInStudy : series.modality as Set
    }

    long getNumStudyRelatedSeries() {
        numStudyRelatedSeries != 0 ? numStudyRelatedSeries : series.size()
    }

    long getNumStudyRelatedInstances() {
        numStudyRelatedInstances != 0 ? numStudyRelatedInstances : series.numSeriesRelatedInstances.sum()
    }

    void setSeries(Set<DicomWebTestSeries> seriesSet) {
        seriesSet.each { series ->
            series.setStudy(this)
        }
        series = seriesSet
    }

    File toFile() {
        data[0].toFile()
    }

    DicomWebTestStudy data(TestData dataSingleton) {
        data([dataSingleton])
    }

    DicomWebTestStudy data(List<TestData> data) {
        setData(data)
        if (data.studyInstanceUid != null) setStudyInstanceUID(data[0].studyInstanceUid)
        this
    }

    DicomWebTestStudy studyDate(String date) {
        setStudyDate(date)
        this
    }

    DicomWebTestStudy studyTime(String time) {
        setStudyTime(time)
        this
    }

    DicomWebTestStudy accessionNumber(String accessionNumber) {
        setAccessionNumber(accessionNumber)
        this
    }

    DicomWebTestStudy modalitiesInStudy(Set<String> modalitiesInStudy) {
        setModalitiesInStudy(modalitiesInStudy)
        this
    }

    DicomWebTestStudy patientName(String patientName) {
        setPatientName(patientName)
        this
    }

    DicomWebTestStudy patientID(String patientID) {
        setPatientID(patientID)
        this
    }

    DicomWebTestStudy studyInstanceUID(String uid) {
        setStudyInstanceUID(uid)
        this
    }

    DicomWebTestStudy studyID(String studyID) {
        setStudyID(studyID)
        this
    }

    DicomWebTestStudy patientBirthdate(String birthdate) {
        setPatientBirthdate(birthdate)
        this
    }

    DicomWebTestStudy patientSex(String patientSex) {
        setPatientSex(patientSex)
        this
    }

    DicomWebTestStudy numStudyRelatedSeries(long num) {
        setNumStudyRelatedSeries(num)
        this
    }

    DicomWebTestStudy numStudyRelatedInstances(long num) {
        setNumStudyRelatedInstances(num)
        this
    }

    DicomWebTestStudy series(Collection<DicomWebTestSeries> series) {
        setSeries(series.toSet())
        this
    }

    LocalDate studyDateObject() {
        LocalDate.parse(studyDate, TimeUtils.DICOM_DA_FORMAT)
    }

    LocalDate birthDateObject() {
        LocalDate.parse(patientBirthdate, TimeUtils.DICOM_DA_FORMAT)
    }

    @Override
    DicomWebTestStudy parentStudy() {
        this
    }

    @Override
    protected Map<String, DatasetWithFMI> filter(Map<String, DatasetWithFMI> instances) {
        instances // all files *should* belong to study
    }

}
