package org.nrg.xnat.dicom.web

import groovy.transform.AutoClone
import org.dcm4che3.data.DatasetWithFMI
import org.dcm4che3.data.Tag
import org.nrg.xnat.dicom.wado.AcceptableWadoResponse
import org.nrg.xnat.dicom.qido.AcceptableQidoResponse
import org.nrg.xnat.util.TimeUtils

import java.time.LocalDate
import java.time.LocalTime

@AutoClone(excludes = ['study', 'knownInstances'])
class DicomWebTestSeries extends DicomWebTestData implements AcceptableQidoResponse, AcceptableWadoResponse {

    String modality
    String seriesDescription
    String seriesInstanceUID
    Long seriesNumber
    long numSeriesRelatedInstances
    String performedProcedureStepStartDate // XNAT implementation: proxy for series date
    String performedProcedureStepStartTime // XNAT implementation: proxy for series time
    DicomWebTestStudy study
    Set<DicomWebTestInstance> knownInstances = []

    // Data in TestsWithStandardDataSetup
    public static final DicomWebTestSeries CT1_CT_1 = new DicomWebTestSeries().modality('CT').seriesDescription('Surview').seriesInstanceUID('1.3.6.1.4.1.14519.5.2.1.1188.4001.150609247512029149652021114524').seriesNumber(1).numSeriesRelatedInstances(1)
    public static final DicomWebTestSeries CT1_CT_2 = new DicomWebTestSeries().modality('CT').seriesDescription('CAP5').seriesInstanceUID('1.3.6.1.4.1.14519.5.2.1.1188.4001.260580685341047613056529970289').seriesNumber(2).numSeriesRelatedInstances(145)
    public static final DicomWebTestSeries CT1_CT_3 = new DicomWebTestSeries().modality('CT').seriesDescription('THINS').seriesInstanceUID('1.3.6.1.4.1.14519.5.2.1.1188.4001.208044394955947270439186507904').seriesNumber(3).numSeriesRelatedInstances(662)
    public static final DicomWebTestSeries CT1_CT_4 = new DicomWebTestSeries().modality('CT').seriesDescription('HR').seriesInstanceUID('1.3.6.1.4.1.14519.5.2.1.1188.4001.130362218978431842276851655967').seriesNumber(4).numSeriesRelatedInstances(10)
    public static final DicomWebTestSeries CT2_CT_1 = new DicomWebTestSeries().modality('CT').seriesDescription('Scout').seriesInstanceUID('1.3.6.1.4.1.14519.5.2.1.2783.4001.642406782370196606897283078836').seriesNumber(1).numSeriesRelatedInstances(2).performedProcedureStepStartDate('20170101').performedProcedureStepStartTime('212641')
    public static final DicomWebTestSeries CT2_CT_2 = new DicomWebTestSeries().modality('CT').seriesDescription('PE').seriesInstanceUID('1.3.6.1.4.1.14519.5.2.1.2783.4001.649854360657315151647709076589').seriesNumber(2).numSeriesRelatedInstances(478).performedProcedureStepStartDate('20170101').performedProcedureStepStartTime('212751')
    public static final DicomWebTestSeries CT2_CT_200 = new DicomWebTestSeries().modality('CT').seriesDescription('Smart Prep Series').seriesInstanceUID('1.3.6.1.4.1.14519.5.2.1.2783.4001.232304661314561318733851113180').seriesNumber(200).numSeriesRelatedInstances(4).performedProcedureStepStartDate('20170101').performedProcedureStepStartTime('212745')
    public static final DicomWebTestSeries CT3_CT_1 = new DicomWebTestSeries().modality('CT').seriesInstanceUID('1.3.6.1.4.1.14519.5.2.1.2783.4001.263725748921156154971161116781').seriesNumber(1).numSeriesRelatedInstances(1).performedProcedureStepStartDate('20170303').performedProcedureStepStartTime('183531')
    public static final DicomWebTestSeries CT3_CT_2 = new DicomWebTestSeries().modality('CT').seriesInstanceUID('1.3.6.1.4.1.14519.5.2.1.2783.4001.238142279344386343458393193163').seriesNumber(2).numSeriesRelatedInstances(48).performedProcedureStepStartDate('20170303').performedProcedureStepStartTime('183608')
    public static final DicomWebTestSeries CT3_CT_3 = new DicomWebTestSeries().modality('CT').seriesDescription('Recon 2:').seriesInstanceUID('1.3.6.1.4.1.14519.5.2.1.2783.4001.217063637471639775936142773217').seriesNumber(3).numSeriesRelatedInstances(48).performedProcedureStepStartDate('20170303').performedProcedureStepStartTime('183608')
    public static final DicomWebTestSeries CT4_CT_1 = new DicomWebTestSeries().modality('CT').seriesDescription('Topogram  0.6  T20s').seriesInstanceUID('1.3.6.1.4.1.14519.5.2.1.5826.4001.239117835934919276129835117782').seriesNumber(1).numSeriesRelatedInstances(2).performedProcedureStepStartDate('20170101').performedProcedureStepStartTime('212722')
    public static final DicomWebTestSeries CT4_CT_2 = new DicomWebTestSeries().modality('CT').seriesDescription('Head Vol.  1.5  H40s ST').seriesInstanceUID('1.3.6.1.4.1.14519.5.2.1.5826.4001.312669389023517091391958251391').seriesNumber(2).numSeriesRelatedInstances(124).performedProcedureStepStartDate('20170101').performedProcedureStepStartTime('213011')
    public static final DicomWebTestSeries CT4_CT_501 = new DicomWebTestSeries().modality('CT').seriesDescription('Patient Protocol').seriesInstanceUID('1.3.6.1.4.1.14519.5.2.1.5826.4001.317818950367322288608870356840').seriesNumber(501).numSeriesRelatedInstances(1).performedProcedureStepStartDate('20170101').performedProcedureStepStartTime('213223')
    public static final DicomWebTestSeries CT5_CT_1 = new DicomWebTestSeries().modality('CT').seriesDescription('Scout').seriesInstanceUID('1.3.6.1.4.1.14519.5.2.1.2783.4001.782279203515545577639956321842').seriesNumber(1).numSeriesRelatedInstances(1).performedProcedureStepStartDate('20170404').performedProcedureStepStartTime('212342')
    public static final DicomWebTestSeries CT5_CT_2 = new DicomWebTestSeries().modality('CT').seriesDescription('Routine Head').seriesInstanceUID('1.3.6.1.4.1.14519.5.2.1.2783.4001.219063271726950776299067442244').seriesNumber(2).numSeriesRelatedInstances(44).performedProcedureStepStartDate('20170404').performedProcedureStepStartTime('212446')
    public static final DicomWebTestSeries CT5_CT_3 = new DicomWebTestSeries().modality('CT').seriesDescription('Recon 2: Routine Head').seriesInstanceUID('1.3.6.1.4.1.14519.5.2.1.2783.4001.312636935420780245850586361851').seriesNumber(3).numSeriesRelatedInstances(44).performedProcedureStepStartDate('20170404').performedProcedureStepStartTime('212446')
    public static final DicomWebTestSeries CT6_CT_1 = new DicomWebTestSeries().modality('CT').seriesDescription('Scout').seriesInstanceUID('1.3.6.1.4.1.14519.5.2.1.2783.4001.339005869680617350310729290767').seriesNumber(1).numSeriesRelatedInstances(1).performedProcedureStepStartDate('20170505').performedProcedureStepStartTime('211816')
    public static final DicomWebTestSeries CT6_CT_2 = new DicomWebTestSeries().modality('CT').seriesDescription('Routine Head').seriesInstanceUID('1.3.6.1.4.1.14519.5.2.1.2783.4001.254219102340082017233438170168').seriesNumber(2).numSeriesRelatedInstances(44).performedProcedureStepStartDate('20170505').performedProcedureStepStartTime('211857')
    public static final DicomWebTestSeries CT6_CT_3 = new DicomWebTestSeries().modality('CT').seriesDescription('Recon 2: Routine Head').seriesInstanceUID('1.3.6.1.4.1.14519.5.2.1.2783.4001.121189083018022724141697855715').seriesNumber(3).numSeriesRelatedInstances(44).performedProcedureStepStartDate('20170505').performedProcedureStepStartTime('211857')
    public static final DicomWebTestSeries CT7_CT_8 = new DicomWebTestSeries().modality('CT').seriesDescription('FIDUCIALS  1.0  SPO  cor').seriesInstanceUID('1.3.6.1.4.1.14519.5.2.1.4591.4001.192263247216768060808003960215').seriesNumber(8).numSeriesRelatedInstances(216).performedProcedureStepStartDate('20170101').performedProcedureStepStartTime('161133')
    public static final DicomWebTestSeries CT7_CT_9 = new DicomWebTestSeries().modality('CT').seriesDescription('FIDUCIALS  1.0  SPO  cor').seriesInstanceUID('1.3.6.1.4.1.14519.5.2.1.4591.4001.304957751206244114385301305190').seriesNumber(9).numSeriesRelatedInstances(220).performedProcedureStepStartDate('20170101').performedProcedureStepStartTime('162844')
    public static final DicomWebTestSeries CT7_CT_10 = new DicomWebTestSeries().modality('CT').seriesDescription('FIDUCIALS  1.0  SPO  cor').seriesInstanceUID('1.3.6.1.4.1.14519.5.2.1.4591.4001.169429299966800532747772954510').seriesNumber(10).numSeriesRelatedInstances(216).performedProcedureStepStartDate('20170101').performedProcedureStepStartTime('162903')
    public static final DicomWebTestSeries CT7_CT_11 = new DicomWebTestSeries().modality('CT').seriesDescription('FIDUCIALS  1.0  SPO  sag').seriesInstanceUID('1.3.6.1.4.1.14519.5.2.1.4591.4001.204616770497872144385747998263').seriesNumber(11).numSeriesRelatedInstances(174).performedProcedureStepStartDate('20170101').performedProcedureStepStartTime('162932')
    public static final DicomWebTestSeries CT8_CT_1 = new DicomWebTestSeries().modality('CT').seriesInstanceUID('1.3.6.1.4.1.14519.5.2.1.2783.4001.235482826409522795318678484324').seriesNumber(1).numSeriesRelatedInstances(1).performedProcedureStepStartDate('20171010').performedProcedureStepStartTime('144347')
    public static final DicomWebTestSeries CT8_CT_2 = new DicomWebTestSeries().modality('CT').seriesInstanceUID('1.3.6.1.4.1.14519.5.2.1.2783.4001.107724776816060402138347741784').seriesNumber(2).numSeriesRelatedInstances(34).performedProcedureStepStartDate('20171010').performedProcedureStepStartTime('144452')
    public static final DicomWebTestSeries MR1_MR_3 = new DicomWebTestSeries().modality('MR').seriesDescription('Post Gd axial FSE').seriesInstanceUID('1.3.6.1.4.1.14519.5.2.1.3344.2526.218354651192468028651117710081').seriesNumber(3).numSeriesRelatedInstances(48).performedProcedureStepStartDate('20170101').performedProcedureStepStartTime('092540')
    public static final DicomWebTestSeries MR1_MR_4 = new DicomWebTestSeries().modality('MR').seriesDescription('Post Gd AX. SPGR').seriesInstanceUID('1.3.6.1.4.1.14519.5.2.1.3344.2526.337514999512537544992141921011').seriesNumber(4).numSeriesRelatedInstances(60).performedProcedureStepStartDate('20170101').performedProcedureStepStartTime('093256')
    public static final DicomWebTestSeries MR2_MR_3 = new DicomWebTestSeries().modality('MR').seriesDescription('TSE_T2_RST_STRAIGHT AXIAL').seriesInstanceUID('1.3.6.1.4.1.14519.5.2.1.3344.2526.188467905121607361369986644668').seriesNumber(3).numSeriesRelatedInstances(54).performedProcedureStepStartDate('20170303').performedProcedureStepStartTime('074507')
    public static final DicomWebTestSeries MR2_MR_4 = new DicomWebTestSeries().modality('MR').seriesDescription('T1_FLASH_3D_STRAIGHT AXIAL').seriesInstanceUID('1.3.6.1.4.1.14519.5.2.1.3344.2526.305518688617718608117317026420').seriesNumber(4).numSeriesRelatedInstances(60).performedProcedureStepStartDate('20170303').performedProcedureStepStartTime('075455')
    public static final DicomWebTestSeries MR3_MR_4 = new DicomWebTestSeries().modality('MR').seriesDescription('axial FSE').seriesInstanceUID('1.3.6.1.4.1.14519.5.2.1.3344.2526.235068236686638852654176814306').seriesNumber(4).numSeriesRelatedInstances(52).performedProcedureStepStartDate('20170404').performedProcedureStepStartTime('095137')
    public static final DicomWebTestSeries MR3_MR_6 = new DicomWebTestSeries().modality('MR').seriesDescription('AX. SPGR').seriesInstanceUID('1.3.6.1.4.1.14519.5.2.1.3344.2526.113832057470238345174227553232').seriesNumber(6).numSeriesRelatedInstances(60).performedProcedureStepStartDate('20170404').performedProcedureStepStartTime('100823')
    public static final DicomWebTestSeries MR4_MR_3 = new DicomWebTestSeries().modality('MR').seriesDescription('axial FSE').seriesInstanceUID('1.3.6.1.4.1.14519.5.2.1.3344.2526.180005421835373276005338675965').seriesNumber(3).numSeriesRelatedInstances(52).performedProcedureStepStartDate('20170909').performedProcedureStepStartTime('091002')
    public static final DicomWebTestSeries MR4_MR_5 = new DicomWebTestSeries().modality('MR').seriesDescription('AX. SPGR').seriesInstanceUID('1.3.6.1.4.1.14519.5.2.1.3344.2526.114528902460884162463057548546').seriesNumber(5).numSeriesRelatedInstances(60).performedProcedureStepStartDate('20170909').performedProcedureStepStartTime('092950')
    // Data in TestsWithComplexData
    public static final DicomWebTestSeries PETCT1_CT_2 = new DicomWebTestSeries().modality('CT').seriesDescription('CT 5.0 H30s').seriesInstanceUID('1.3.6.1.4.1.14519.5.2.1.5099.8010.427264300850965737262860055580').seriesNumber(2).numSeriesRelatedInstances(113).performedProcedureStepStartDate('19990823').performedProcedureStepStartTime('192939')
    public static final DicomWebTestSeries PETCT1_PT_605 = new DicomWebTestSeries().modality('PT').seriesDescription('PET WB-uncorrected').seriesInstanceUID('1.3.6.1.4.1.14519.5.2.1.5099.8010.260151978148957514594497217760').seriesNumber(605).numSeriesRelatedInstances(112).performedProcedureStepStartDate('19990823').performedProcedureStepStartTime('193121')
    public static final DicomWebTestSeries PETCT1_PT_606 = new DicomWebTestSeries().modality('PT').seriesDescription('PET WB').seriesInstanceUID('1.3.6.1.4.1.14519.5.2.1.5099.8010.308184765901558710285007064772').seriesNumber(606).numSeriesRelatedInstances(112).performedProcedureStepStartDate('19990823').performedProcedureStepStartTime('193121')
    public static final DicomWebTestSeries PETCT2_CT_2 = new DicomWebTestSeries().modality('CT').seriesDescription('CT 5.0 H30s').seriesInstanceUID('1.3.6.1.4.1.14519.5.2.1.5099.8010.293653169363509354643731389289').seriesNumber(2).numSeriesRelatedInstances(113).performedProcedureStepStartDate('19991207').performedProcedureStepStartTime('131852')
    public static final DicomWebTestSeries PETCT2_PT_605 = new DicomWebTestSeries().modality('PT').seriesDescription('PET WB-uncorrected').seriesInstanceUID('1.3.6.1.4.1.14519.5.2.1.5099.8010.313834683360850380813202838079').seriesNumber(605).numSeriesRelatedInstances(112).performedProcedureStepStartDate('19991207').performedProcedureStepStartTime('132029')
    public static final DicomWebTestSeries PETCT2_PT_606 = new DicomWebTestSeries().modality('PT').seriesDescription('PET WB').seriesInstanceUID('1.3.6.1.4.1.14519.5.2.1.5099.8010.224799699720072341908493257751').seriesNumber(606).numSeriesRelatedInstances(112).performedProcedureStepStartDate('19991207').performedProcedureStepStartTime('132029')
    public static final DicomWebTestSeries CTRT1_CT_1 = new DicomWebTestSeries().modality('CT').seriesDescription('CTs from rtog conversion').seriesInstanceUID('1.3.6.1.4.1.22213.2.26555.2').seriesNumber(1).numSeriesRelatedInstances(107).performedProcedureStepStartDate('19990831')
    public static final DicomWebTestSeries CTRT1_RTSTRUCT_2 = new DicomWebTestSeries().modality('RTSTRUCT').seriesDescription('RTStruct from rtog conversion').seriesInstanceUID('1.3.6.1.4.1.22213.2.26555.3').seriesNumber(2).numSeriesRelatedInstances(1).performedProcedureStepStartDate('19990831')
    public static final DicomWebTestSeries CTRT1_RTPLAN_3 = new DicomWebTestSeries().modality('RTPLAN').seriesDescription('RT Plan (excerpt) - fx1hetero').seriesInstanceUID('1.3.6.1.4.1.22213.2.26555.4.1').seriesNumber(3).numSeriesRelatedInstances(1).performedProcedureStepStartDate('19990831')
    public static final DicomWebTestSeries CTRT1_RTDOSE_4 = new DicomWebTestSeries().modality('RTDOSE').seriesDescription('RT Dose - fx1hetero').seriesInstanceUID('1.3.6.1.4.1.22213.2.26555.5.1').seriesNumber(4).numSeriesRelatedInstances(1).performedProcedureStepStartDate('19990831')
    public static final DicomWebTestSeries CTRT2_CT_1 = new DicomWebTestSeries().modality('CT').seriesDescription('CTs from rtog conversion').seriesInstanceUID('1.3.6.1.4.1.22213.2.26556.2').seriesNumber(1).numSeriesRelatedInstances(130).performedProcedureStepStartDate('19990906')
    public static final DicomWebTestSeries CTRT2_RTSTRUCT_2 = new DicomWebTestSeries().modality('RTSTRUCT').seriesDescription('RTStruct from rtog conversion').seriesInstanceUID('1.3.6.1.4.1.22213.2.26556.3').seriesNumber(2).numSeriesRelatedInstances(1).performedProcedureStepStartDate('19990906')
    public static final DicomWebTestSeries PETMR1_PT_1 = new DicomWebTestSeries().modality('PT').seriesDescription('PET AC').seriesInstanceUID('1.3.6.1.4.1.14519.5.2.1.5168.1900.279539551699081894888330051583').seriesNumber(1).numSeriesRelatedInstances(267).performedProcedureStepStartDate('20000907').performedProcedureStepStartTime('112011')
    public static final DicomWebTestSeries PETMR1_MR_5436027 = new DicomWebTestSeries().modality('MR').seriesDescription('Aligned_T1toPET_BOX').seriesInstanceUID('1.3.6.1.4.1.14519.5.2.1.5168.1900.232182644086451239142307988600').seriesNumber(5436027).numSeriesRelatedInstances(61).performedProcedureStepStartDate('20000907').performedProcedureStepStartTime('111033')
    public static final DicomWebTestSeries PETMR1_MR_5442056 = new DicomWebTestSeries().modality('MR').seriesDescription('Aligned_T2FStoPET_BOX').seriesInstanceUID('1.3.6.1.4.1.14519.5.2.1.5168.1900.337751267676171606047504706608').seriesNumber(5442056).numSeriesRelatedInstances(60).performedProcedureStepStartDate('20000907').performedProcedureStepStartTime('110422')
    public static final DicomWebTestSeries PETMR2_PT_4 = new DicomWebTestSeries().modality('PT').seriesDescription('LEGS_2D_AC').seriesInstanceUID('1.3.6.1.4.1.14519.5.2.1.5168.1900.234308622016032251001504298675').seriesNumber(4).numSeriesRelatedInstances(267).performedProcedureStepStartDate('20040401').performedProcedureStepStartTime('133347')
    public static final DicomWebTestSeries PETMR2_MR_5610035 = new DicomWebTestSeries().modality('MR').seriesDescription('Aligned_T1toPET_BOX').seriesInstanceUID('1.3.6.1.4.1.14519.5.2.1.5168.1900.862727881609861157081214356697').seriesNumber(5610035).numSeriesRelatedInstances(99).performedProcedureStepStartDate('20040312').performedProcedureStepStartTime('115338')
    public static final DicomWebTestSeries PETMR2_MR_5616046 = new DicomWebTestSeries().modality('MR').seriesDescription('Aligned_T2FStoPET_BOX').seriesInstanceUID('1.3.6.1.4.1.14519.5.2.1.5168.1900.257294103283551870070863688852').seriesNumber(5616046).numSeriesRelatedInstances(93).performedProcedureStepStartDate('20040312').performedProcedureStepStartTime('115338')
    public static final DicomWebTestSeries PETMR3_PT_1 = new DicomWebTestSeries().modality('PT').seriesDescription('PET AC').seriesInstanceUID('1.3.6.1.4.1.14519.5.2.1.5168.1900.195100674832026104807657164849').seriesNumber(1).numSeriesRelatedInstances(267).performedProcedureStepStartDate('20000511').performedProcedureStepStartTime('125036')
    public static final DicomWebTestSeries PETMR3_MR_6978035 = new DicomWebTestSeries().modality('MR').seriesDescription('Aligned_T1toPET_BOX').seriesInstanceUID('1.3.6.1.4.1.14519.5.2.1.5168.1900.337286961749349747177856705932').seriesNumber(6978035).numSeriesRelatedInstances(56).performedProcedureStepStartDate('20000505').performedProcedureStepStartTime('112115')
    public static final DicomWebTestSeries PETMR3_MR_6984036 = new DicomWebTestSeries().modality('MR').seriesDescription('Aligned_STIRtoPET_BOX').seriesInstanceUID('1.3.6.1.4.1.14519.5.2.1.5168.1900.372010671000103577039674066896').seriesNumber(6984036).numSeriesRelatedInstances(57).performedProcedureStepStartDate('20000505').performedProcedureStepStartTime('112612')
    public static final DicomWebTestSeries MRPR_MR_0 = new DicomWebTestSeries().modality('MR').seriesInstanceUID('1.3.46.670589.11.5730.5.0.6152.2010043012352943002').seriesNumber(0).numSeriesRelatedInstances(1).performedProcedureStepStartDate('20100430').performedProcedureStepStartTime('123529')
    public static final DicomWebTestSeries MRPR_PR_0_A = new DicomWebTestSeries().modality('PR').seriesDescription('PDW_TSE').seriesInstanceUID('1.3.46.670589.11.5730.5.0.1744.2010043013074604020').seriesNumber(0).numSeriesRelatedInstances(1).performedProcedureStepStartDate('20100430').performedProcedureStepStartTime('130746')
    public static final DicomWebTestSeries MRPR_PR_0_B = new DicomWebTestSeries().modality('PR').seriesInstanceUID('1.3.46.670589.11.5730.5.0.7972.2010050310223084000').seriesNumber(0).numSeriesRelatedInstances(1).performedProcedureStepStartDate('20100503').performedProcedureStepStartTime('102230')
    public static final DicomWebTestSeries MRPR_MR_601 = new DicomWebTestSeries().modality('MR').seriesDescription('PDW_TSE').seriesInstanceUID('1.3.46.670589.11.5730.5.0.3144.2010043013044142001').seriesNumber(601).numSeriesRelatedInstances(2).performedProcedureStepStartDate('20100430').performedProcedureStepStartTime('130441')
    public static final DicomWebTestSeries MRPR_MR_701 = new DicomWebTestSeries().modality('MR').seriesDescription('PDW_TSE').seriesInstanceUID('1.3.46.670589.11.5730.5.0.3144.2010043013074510003').seriesNumber(701).numSeriesRelatedInstances(1).performedProcedureStepStartDate('20100430').performedProcedureStepStartTime('130745')
    public static final DicomWebTestSeries MULTIFRAME_CT_CT_1 = new DicomWebTestSeries().modality('CT').seriesInstanceUID('1.3.6.1.4.1.5962.1.3.10.1.1166562673.14401').seriesNumber(1).numSeriesRelatedInstances(1).performedProcedureStepStartDate('20061219').performedProcedureStepStartTime('112850')
    public static final DicomWebTestSeries MULTIFRAME_CT_CT_2 = new DicomWebTestSeries().modality('CT').seriesInstanceUID('1.3.6.1.4.1.5962.1.3.10.2.1166562673.14401').seriesNumber(2).numSeriesRelatedInstances(1).performedProcedureStepStartDate('20061219').performedProcedureStepStartTime('110929')
    public static final DicomWebTestSeries MULTIFRAME_CT_CT_3 = new DicomWebTestSeries().modality('CT').seriesInstanceUID('1.3.6.1.4.1.5962.1.3.10.3.1166562673.14401').seriesNumber(3).numSeriesRelatedInstances(3).performedProcedureStepStartDate('20061219').performedProcedureStepStartTime('110929')
    public static final DicomWebTestSeries MULTIFRAME_CT_CT_4 = new DicomWebTestSeries().modality('CT').seriesInstanceUID('1.3.6.1.4.1.5962.1.3.10.4.1166562673.14401').seriesNumber(4).numSeriesRelatedInstances(1).performedProcedureStepStartDate('20061219').performedProcedureStepStartTime('110929')
    public static final DicomWebTestSeries MISSINGNO_MR_MR_1 = new DicomWebTestSeries().modality('MR').seriesInstanceUID('1.3.6.1.4.1.9328.50.50.188668431850770029429968052263321474030').seriesNumber(1).numSeriesRelatedInstances(3)
    public static final DicomWebTestSeries MISSINGNO_MR_MR_2 = new DicomWebTestSeries().modality('MR').seriesInstanceUID('1.3.6.1.4.1.9328.50.50.170866093297598428410920991019518117099').seriesNumber(2).numSeriesRelatedInstances(16)
    public static final DicomWebTestSeries MISSINGNO_MR_MR_3 = new DicomWebTestSeries().modality('MR').seriesInstanceUID('1.3.6.1.4.1.9328.50.50.284580784251070985032161794421580425228').seriesNumber(3).numSeriesRelatedInstances(20)
    public static final DicomWebTestSeries MISSINGNO_MR_MR_EMPTY = new DicomWebTestSeries().modality('MR').seriesInstanceUID('1.3.6.1.4.1.9328.50.50.306371722625968631430245896034757131913').numSeriesRelatedInstances(40)
    public static final DicomWebTestSeries MG_MG_1 = new DicomWebTestSeries().modality('MG').seriesDescription('L ML').seriesInstanceUID('1.3.6.1.4.1.14519.5.2.1.4792.2001.264858695626730718489225809673').seriesNumber(1).numSeriesRelatedInstances(1).performedProcedureStepStartDate('20061220').performedProcedureStepStartTime('112011')
    public static final DicomWebTestSeries MG_MG_2 = new DicomWebTestSeries().modality('MG').seriesDescription('L CC').seriesInstanceUID('1.3.6.1.4.1.14519.5.2.1.4792.2001.254826338354062673395380973800').seriesNumber(2).numSeriesRelatedInstances(1).performedProcedureStepStartDate('20061219').performedProcedureStepStartTime('080912')
    public static final DicomWebTestSeries MG_MG_3 = new DicomWebTestSeries().modality('MG').seriesDescription('L CC').seriesInstanceUID('1.3.6.1.4.1.14519.5.2.1.4792.2001.213518258200882713390015640113').seriesNumber(3).numSeriesRelatedInstances(1).performedProcedureStepStartDate('20061221').performedProcedureStepStartTime('080918')
    // Data in TestsForSecurity
    public static final DicomWebTestSeries PETMR4_PT_1 = new DicomWebTestSeries().modality('PT').seriesDescription('PET AC').seriesInstanceUID('1.3.6.1.4.1.14519.5.2.1.5168.1900.226344837495129111073465998042').seriesNumber(1).numSeriesRelatedInstances(267).performedProcedureStepStartDate('20010105').performedProcedureStepStartTime('102831')
    public static final DicomWebTestSeries PETMR4_MR_4488021 = new DicomWebTestSeries().modality('MR').seriesDescription('Aligned_T1toPET_BOX').seriesInstanceUID('1.3.6.1.4.1.14519.5.2.1.5168.1900.549724518950173278556230187948').seriesNumber(4488021).numSeriesRelatedInstances(70).performedProcedureStepStartDate('20001207').performedProcedureStepStartTime('105649')
    public static final DicomWebTestSeries PETMR4_MR_4494017 = new DicomWebTestSeries().modality('MR').seriesDescription('Aligned_T2FStoPET_BOXX').seriesInstanceUID('1.3.6.1.4.1.14519.5.2.1.5168.1900.282701428393764933631530100392').seriesNumber(4494017).numSeriesRelatedInstances(70).performedProcedureStepStartDate('20001207').performedProcedureStepStartTime('105128')
    // Data in TestsWithDifferentTransferSyntaxes
    public static final DicomWebTestSeries BIG_ENDIAN_US_0 = new DicomWebTestSeries().modality('US').seriesInstanceUID('1.2.276.0.7230010.3.1.3.0.35635.1517265970.592488').seriesNumber(0).numSeriesRelatedInstances(1)
    public static final DicomWebTestSeries BIG_ENDIAN_US_1 = new DicomWebTestSeries().modality('US').seriesInstanceUID('1.2.276.0.7230010.3.1.3.0.35668.1517266217.85069').seriesNumber(1).numSeriesRelatedInstances(1)
    public static final DicomWebTestSeries JPEGLOSSLESS_MR_3 = new DicomWebTestSeries().modality('MR').seriesDescription('localizer').seriesInstanceUID('1.2.840.113654.2.45.6231.249605034573251700870918088077740300479').seriesNumber(3).numSeriesRelatedInstances(3).performedProcedureStepStartDate('20000101').performedProcedureStepStartTime('103733')
    public static final DicomWebTestSeries MIXED_FRAME_MR_0 = new DicomWebTestSeries().modality('MR').seriesDescription('TOF coronal source only and no contrast module').seriesInstanceUID('1.3.6.1.4.1.5962.1.3.5011.0.1166546115.14677').seriesNumber(0).numSeriesRelatedInstances(1).performedProcedureStepStartDate('20061219').performedProcedureStepStartTime('000012')
    public static final DicomWebTestSeries MIXED_FRAME_MR_1 = new DicomWebTestSeries().modality('MR').seriesDescription('TOF coronal source and MIPs around AP').seriesInstanceUID('1.3.6.1.4.1.5962.1.3.5011.1.1166546115.14677').seriesNumber(1).numSeriesRelatedInstances(1).performedProcedureStepStartDate('20061219').performedProcedureStepStartTime('000012')
    public static final DicomWebTestSeries MIXED_FRAME_MR_2 = new DicomWebTestSeries().modality('MR').seriesDescription('TOF MIPs around HF').seriesInstanceUID('1.3.6.1.4.1.5962.1.3.5011.2.1166546115.14677').seriesNumber(2).numSeriesRelatedInstances(1).performedProcedureStepStartDate('20061219').performedProcedureStepStartTime('000012')
    public static final DicomWebTestSeries MIXED_FRAME_MR_3 = new DicomWebTestSeries().modality('MR').seriesDescription('Surface rendering').seriesInstanceUID('1.3.6.1.4.1.5962.1.3.5011.3.1166546115.14677').seriesNumber(3).numSeriesRelatedInstances(1).performedProcedureStepStartDate('20061219').performedProcedureStepStartTime('000012')
    public static final DicomWebTestSeries MIXED_FRAME_MR_100 = new DicomWebTestSeries().modality('MR').seriesDescription('AXIAL MEMP 1').seriesInstanceUID('1.3.6.1.4.1.9328.50.51.114695938670722702002257372583026067785').seriesNumber(100).numSeriesRelatedInstances(104)

    DicomWebTestSeries modality(String modality) {
        setModality(modality)
        this
    }

    DicomWebTestSeries seriesDescription(String seriesDescription) {
        setSeriesDescription(seriesDescription)
        this
    }

    DicomWebTestSeries seriesInstanceUID(String seriesInstanceUID) {
        setSeriesInstanceUID(seriesInstanceUID)
        this
    }

    DicomWebTestSeries seriesNumber(long seriesNumber) {
        setSeriesNumber(seriesNumber)
        this
    }

    DicomWebTestSeries numSeriesRelatedInstances(long numInstances) {
        setNumSeriesRelatedInstances(numInstances)
        this
    }

    DicomWebTestSeries performedProcedureStepStartDate(String date) {
        setPerformedProcedureStepStartDate(date)
        this
    }

    DicomWebTestSeries performedProcedureStepStartTime(String time) {
        setPerformedProcedureStepStartTime(time)
        this
    }

    LocalDate performedProcedureStepStartDateObject() {
        LocalDate.parse(performedProcedureStepStartDate, TimeUtils.DICOM_DA_FORMAT)
    }

    LocalTime performedProcedureStepStartTimeObject() {
        LocalTime.parse(performedProcedureStepStartTime, TimeUtils.DICOM_TM_FORMAT)
    }

    @Override
    DicomWebTestStudy parentStudy() {
        study
    }

    @Override
    protected Map<String, DatasetWithFMI> filter(Map<String, DatasetWithFMI> instances) {
        instances.findAll { uid, datasetWithFMI ->
            datasetWithFMI.dataset.getString(Tag.SeriesInstanceUID) == seriesInstanceUID
        }
    }

}
