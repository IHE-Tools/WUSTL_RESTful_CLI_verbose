package org.nrg.xnat.dicom.web

import org.dcm4che3.data.DatasetWithFMI
import org.dcm4che3.data.Tag
import org.nrg.xnat.dicom.wado.AcceptableWadoResponse
import org.nrg.xnat.dicom.qido.AcceptableQidoResponse

import static org.nrg.xnat.dicom.web.DicomWebTestSeries.*

class DicomWebTestInstance extends DicomWebTestData implements AcceptableQidoResponse, AcceptableWadoResponse {

    DicomWebTestSeries series
    String sopInstanceUID

    public static final DicomWebTestInstance CT1_CT_1_INSTANCE = new DicomWebTestInstance().series(CT1_CT_1).sopInstanceUID('1.3.6.1.4.1.14519.5.2.1.1188.4001.133056717902214064531021149221')
    public static final DicomWebTestInstance CT1_CT_2_INSTANCE = new DicomWebTestInstance().series(CT1_CT_2).sopInstanceUID('1.3.6.1.4.1.14519.5.2.1.1188.4001.252943792189697614095593527935')
    public static final DicomWebTestInstance CT1_CT_3_INSTANCE = new DicomWebTestInstance().series(CT1_CT_3).sopInstanceUID('1.3.6.1.4.1.14519.5.2.1.1188.4001.200203730521643225197932649107')
    public static final DicomWebTestInstance CT1_CT_4_INSTANCE = new DicomWebTestInstance().series(CT1_CT_4).sopInstanceUID('1.3.6.1.4.1.14519.5.2.1.1188.4001.202661496974906983476730835036')
    public static final DicomWebTestInstance CT2_CT_1_INSTANCE = new DicomWebTestInstance().series(CT2_CT_1).sopInstanceUID('1.3.6.1.4.1.14519.5.2.1.2783.4001.115486598237582040790065507324')
    public static final DicomWebTestInstance CT2_CT_2_INSTANCE = new DicomWebTestInstance().series(CT2_CT_2).sopInstanceUID('1.3.6.1.4.1.14519.5.2.1.2783.4001.311145264150710515514893611900')
    public static final DicomWebTestInstance CT2_CT_200_INSTANCE = new DicomWebTestInstance().series(CT2_CT_200).sopInstanceUID('1.3.6.1.4.1.14519.5.2.1.2783.4001.290670505526579814297611693297')
    public static final DicomWebTestInstance CT3_CT_1_INSTANCE = new DicomWebTestInstance().series(CT3_CT_1).sopInstanceUID('1.3.6.1.4.1.14519.5.2.1.2783.4001.194444683015997490445151545276')
    public static final DicomWebTestInstance CT3_CT_2_INSTANCE = new DicomWebTestInstance().series(CT3_CT_2).sopInstanceUID('1.3.6.1.4.1.14519.5.2.1.2783.4001.274755459909996764558423619596')
    public static final DicomWebTestInstance CT3_CT_3_INSTANCE = new DicomWebTestInstance().series(CT3_CT_3).sopInstanceUID('1.3.6.1.4.1.14519.5.2.1.2783.4001.217209378145191127960831011147')
    public static final DicomWebTestInstance CT4_CT_1_INSTANCE = new DicomWebTestInstance().series(CT4_CT_1).sopInstanceUID('1.3.6.1.4.1.14519.5.2.1.5826.4001.203307839668135059994392403415')
    public static final DicomWebTestInstance CT4_CT_2_INSTANCE = new DicomWebTestInstance().series(CT4_CT_2).sopInstanceUID('1.3.6.1.4.1.14519.5.2.1.5826.4001.169714574306975159724456893037')
    public static final DicomWebTestInstance CT4_CT_501_INSTANCE = new DicomWebTestInstance().series(CT4_CT_501).sopInstanceUID('1.3.6.1.4.1.14519.5.2.1.5826.4001.309819691556756470637705391823')
    public static final DicomWebTestInstance CT5_CT_1_INSTANCE = new DicomWebTestInstance().series(CT5_CT_1).sopInstanceUID('1.3.6.1.4.1.14519.5.2.1.2783.4001.159398830631739059867404980225')
    public static final DicomWebTestInstance CT5_CT_2_INSTANCE = new DicomWebTestInstance().series(CT5_CT_2).sopInstanceUID('1.3.6.1.4.1.14519.5.2.1.2783.4001.107354455479858605696513646391')
    public static final DicomWebTestInstance CT5_CT_3_INSTANCE = new DicomWebTestInstance().series(CT5_CT_3).sopInstanceUID('1.3.6.1.4.1.14519.5.2.1.2783.4001.266988892096091754382483881179')
    public static final DicomWebTestInstance CT6_CT_1_INSTANCE = new DicomWebTestInstance().series(CT6_CT_1).sopInstanceUID('1.3.6.1.4.1.14519.5.2.1.2783.4001.707215407438142622617406210411')
    public static final DicomWebTestInstance CT6_CT_2_INSTANCE = new DicomWebTestInstance().series(CT6_CT_2).sopInstanceUID('1.3.6.1.4.1.14519.5.2.1.2783.4001.109098001464785679236357882419')
    public static final DicomWebTestInstance CT6_CT_3_INSTANCE = new DicomWebTestInstance().series(CT6_CT_3).sopInstanceUID('1.3.6.1.4.1.14519.5.2.1.2783.4001.974095224420636892083992613814')
    public static final DicomWebTestInstance CT7_CT_8_INSTANCE = new DicomWebTestInstance().series(CT7_CT_8).sopInstanceUID('1.3.6.1.4.1.14519.5.2.1.4591.4001.142590404904166961859798564732')
    public static final DicomWebTestInstance CT7_CT_9_INSTANCE = new DicomWebTestInstance().series(CT7_CT_9).sopInstanceUID('1.3.6.1.4.1.14519.5.2.1.4591.4001.303206736477666924730406684466')
    public static final DicomWebTestInstance CT7_CT_10_INSTANCE = new DicomWebTestInstance().series(CT7_CT_10).sopInstanceUID('1.3.6.1.4.1.14519.5.2.1.4591.4001.263749971757948594155970981101')
    public static final DicomWebTestInstance CT7_CT_11_INSTANCE = new DicomWebTestInstance().series(CT7_CT_11).sopInstanceUID('1.3.6.1.4.1.14519.5.2.1.4591.4001.195987805692448185324595379475')
    public static final DicomWebTestInstance CT8_CT_1_INSTANCE = new DicomWebTestInstance().series(CT8_CT_1).sopInstanceUID('1.3.6.1.4.1.14519.5.2.1.2783.4001.307113215010313872147203159465')
    public static final DicomWebTestInstance CT8_CT_2_INSTANCE = new DicomWebTestInstance().series(CT8_CT_2).sopInstanceUID('1.3.6.1.4.1.14519.5.2.1.2783.4001.326812803859966913779050386459')
    public static final DicomWebTestInstance MR1_MR_3_INSTANCE = new DicomWebTestInstance().series(MR1_MR_3).sopInstanceUID('1.3.6.1.4.1.14519.5.2.1.3344.2526.267056948362748079462575713449')
    public static final DicomWebTestInstance MR1_MR_4_INSTANCE = new DicomWebTestInstance().series(MR1_MR_4).sopInstanceUID('1.3.6.1.4.1.14519.5.2.1.3344.2526.133946805817883033146925425321')
    public static final DicomWebTestInstance MR2_MR_3_INSTANCE = new DicomWebTestInstance().series(MR2_MR_3).sopInstanceUID('1.3.6.1.4.1.14519.5.2.1.3344.2526.181913055877813340878478415597')
    public static final DicomWebTestInstance MR2_MR_4_INSTANCE = new DicomWebTestInstance().series(MR2_MR_4).sopInstanceUID('1.3.6.1.4.1.14519.5.2.1.3344.2526.209670966228229855233369629787')
    public static final DicomWebTestInstance MR3_MR_4_INSTANCE = new DicomWebTestInstance().series(MR3_MR_4).sopInstanceUID('1.3.6.1.4.1.14519.5.2.1.3344.2526.220359742310293141651506759376')
    public static final DicomWebTestInstance MR3_MR_6_INSTANCE = new DicomWebTestInstance().series(MR3_MR_6).sopInstanceUID('1.3.6.1.4.1.14519.5.2.1.3344.2526.145444259305170355221072689727')
    public static final DicomWebTestInstance MR4_MR_3_INSTANCE = new DicomWebTestInstance().series(MR4_MR_3).sopInstanceUID('1.3.6.1.4.1.14519.5.2.1.3344.2526.577533687965702910981499976256')
    public static final DicomWebTestInstance MR4_MR_5_INSTANCE = new DicomWebTestInstance().series(MR4_MR_5).sopInstanceUID('1.3.6.1.4.1.14519.5.2.1.3344.2526.157247062995779645495191518338')
    // Data in TestsWithComplexData
    public static final DicomWebTestInstance PETCT1_CT_2_INSTANCE = new DicomWebTestInstance().series(PETCT1_CT_2).sopInstanceUID('1.3.6.1.4.1.14519.5.2.1.5099.8010.251994005496290535434821893415')
    public static final DicomWebTestInstance PETCT1_PT_605_INSTANCE = new DicomWebTestInstance().series(PETCT1_PT_605).sopInstanceUID('1.3.6.1.4.1.14519.5.2.1.5099.8010.489707101979592861883494553274')
    public static final DicomWebTestInstance PETCT1_PT_606_INSTANCE = new DicomWebTestInstance().series(PETCT1_PT_606).sopInstanceUID('1.3.6.1.4.1.14519.5.2.1.5099.8010.173150824320261523544739689823')
    public static final DicomWebTestInstance PETCT2_CT_2_INSTANCE = new DicomWebTestInstance().series(PETCT2_CT_2).sopInstanceUID('1.3.6.1.4.1.14519.5.2.1.5099.8010.319149938203136481798760798805')
    public static final DicomWebTestInstance PETCT2_PT_605_INSTANCE = new DicomWebTestInstance().series(PETCT2_PT_605).sopInstanceUID('1.3.6.1.4.1.14519.5.2.1.5099.8010.162289287550393612789119534860')
    public static final DicomWebTestInstance PETCT2_PT_606_INSTANCE = new DicomWebTestInstance().series(PETCT2_PT_606).sopInstanceUID('1.3.6.1.4.1.14519.5.2.1.5099.8010.139900957889730530137810915173')
    public static final DicomWebTestInstance CTRT1_CT_1_INSTANCE = new DicomWebTestInstance().series(CTRT1_CT_1).sopInstanceUID('1.3.6.1.4.1.22213.2.26555.2.7')
    public static final DicomWebTestInstance CTRT1_RTSTRUCT_2_INSTANCE = new DicomWebTestInstance().series(CTRT1_RTSTRUCT_2).sopInstanceUID('1.3.6.1.4.1.22213.2.26555.3.1')
    public static final DicomWebTestInstance CTRT1_RTPLAN_3_INSTANCE = new DicomWebTestInstance().series(CTRT1_RTPLAN_3).sopInstanceUID('1.3.6.1.4.1.22213.2.26555.4.1.1')
    public static final DicomWebTestInstance CTRT1_RTDOSE_4_INSTANCE = new DicomWebTestInstance().series(CTRT1_RTDOSE_4).sopInstanceUID('1.3.6.1.4.1.22213.2.26555.5.1.1')
    public static final DicomWebTestInstance CTRT2_CT_1_INSTANCE = new DicomWebTestInstance().series(CTRT2_CT_1).sopInstanceUID('1.3.6.1.4.1.22213.2.26556.2.3')
    public static final DicomWebTestInstance CTRT2_RTSTRUCT_2_INSTANCE = new DicomWebTestInstance().series(CTRT2_RTSTRUCT_2).sopInstanceUID('1.3.6.1.4.1.22213.2.26556.3.1')
    public static final DicomWebTestInstance PETMR1_PT_1_INSTANCE = new DicomWebTestInstance().series(PETMR1_PT_1).sopInstanceUID('1.3.6.1.4.1.14519.5.2.1.5168.1900.138504846043826247551955293620')
    public static final DicomWebTestInstance PETMR1_MR_5436027_INSTANCE = new DicomWebTestInstance().series(PETMR1_MR_5436027).sopInstanceUID('1.3.6.1.4.1.14519.5.2.1.5168.1900.183995267853943066037853276206')
    public static final DicomWebTestInstance PETMR1_MR_5442056_INSTANCE = new DicomWebTestInstance().series(PETMR1_MR_5442056).sopInstanceUID('1.3.6.1.4.1.14519.5.2.1.5168.1900.138969304298928639301008953180')
    public static final DicomWebTestInstance PETMR2_PT_4_INSTANCE = new DicomWebTestInstance().series(PETMR2_PT_4).sopInstanceUID('1.3.6.1.4.1.14519.5.2.1.5168.1900.324748495093257426496628317910')
    public static final DicomWebTestInstance PETMR2_MR_5610035_INSTANCE = new DicomWebTestInstance().series(PETMR2_MR_5610035).sopInstanceUID('1.3.6.1.4.1.14519.5.2.1.5168.1900.314478885305558198544672527391')
    public static final DicomWebTestInstance PETMR2_MR_5616046_INSTANCE = new DicomWebTestInstance().series(PETMR2_MR_5616046).sopInstanceUID('1.3.6.1.4.1.14519.5.2.1.5168.1900.186270582228055374718440702572')
    public static final DicomWebTestInstance PETMR3_PT_1_INSTANCE = new DicomWebTestInstance().series(PETMR3_PT_1).sopInstanceUID('1.3.6.1.4.1.14519.5.2.1.5168.1900.317636944302727681989245227144')
    public static final DicomWebTestInstance PETMR3_MR_6978035_INSTANCE = new DicomWebTestInstance().series(PETMR3_MR_6978035).sopInstanceUID('1.3.6.1.4.1.14519.5.2.1.5168.1900.286000716409385393008654241022')
    public static final DicomWebTestInstance PETMR3_MR_6984036_INSTANCE = new DicomWebTestInstance().series(PETMR3_MR_6984036).sopInstanceUID('1.3.6.1.4.1.14519.5.2.1.5168.1900.246739887652586939056041270036')
    public static final DicomWebTestInstance MRPR_MR_0_INSTANCE = new DicomWebTestInstance().series(MRPR_MR_0).sopInstanceUID('1.3.46.670589.11.5730.5.24.5.1.6152.2010043012352943002')
    public static final DicomWebTestInstance MRPR_PR_0_A_INSTANCE = new DicomWebTestInstance().series(MRPR_PR_0_A).sopInstanceUID('1.3.46.670589.11.5730.5.0.1744.2010043013164568023')
    public static final DicomWebTestInstance MRPR_PR_0_B_INSTANCE = new DicomWebTestInstance().series(MRPR_PR_0_B).sopInstanceUID('1.3.46.670589.11.5730.5.0.5036.2010050310223068000')
    public static final DicomWebTestInstance MRPR_MR_601_INSTANCE_ENHANCED_MR = new DicomWebTestInstance().series(MRPR_MR_601).sopInstanceUID('1.3.46.670589.11.5730.5.20.1.1.3144.2010043013044142001')
    public static final DicomWebTestInstance MRPR_MR_601_INSTANCE_RAW_DATA = new DicomWebTestInstance().series(MRPR_MR_601).sopInstanceUID('1.3.46.670589.11.5730.5.24.5.1.3144.2010043013044142001')
    public static final DicomWebTestInstance MRPR_MR_701_INSTANCE = new DicomWebTestInstance().series(MRPR_MR_701).sopInstanceUID('1.3.46.670589.11.5730.5.24.5.1.3144.2010043013074510003')
    public static final DicomWebTestInstance MULTIFRAME_CT_CT_1_INSTANCE = new DicomWebTestInstance().series(MULTIFRAME_CT_CT_1).sopInstanceUID('1.3.6.1.4.1.5962.1.1.10.1.1.1166562673.14401')
    public static final DicomWebTestInstance MULTIFRAME_CT_CT_2_INSTANCE = new DicomWebTestInstance().series(MULTIFRAME_CT_CT_2).sopInstanceUID('1.3.6.1.4.1.5962.1.1.10.2.1.1166562673.14401')
    public static final DicomWebTestInstance MULTIFRAME_CT_CT_3_INSTANCE = new DicomWebTestInstance().series(MULTIFRAME_CT_CT_3).sopInstanceUID('1.3.6.1.4.1.5962.1.1.10.3.2.1166562673.14401')
    public static final DicomWebTestInstance MULTIFRAME_CT_CT_4_INSTANCE = new DicomWebTestInstance().series(MULTIFRAME_CT_CT_4).sopInstanceUID('1.3.6.1.4.1.5962.1.1.10.3.3.1166562673.14401')
    public static final DicomWebTestInstance MISSINGNO_MR_MR_1_INSTANCE = new DicomWebTestInstance().series(MISSINGNO_MR_MR_1).sopInstanceUID('1.3.6.1.4.1.9328.50.50.161426359342396286185680154998611820596')
    public static final DicomWebTestInstance MISSINGNO_MR_MR_2_INSTANCE = new DicomWebTestInstance().series(MISSINGNO_MR_MR_2).sopInstanceUID('1.3.6.1.4.1.9328.50.50.339521278622378476496900445525309121688')
    public static final DicomWebTestInstance MISSINGNO_MR_MR_3_INSTANCE = new DicomWebTestInstance().series(MISSINGNO_MR_MR_3).sopInstanceUID('1.3.6.1.4.1.9328.50.50.109494927821731876474941207138347844844')
    public static final DicomWebTestInstance MISSINGNO_MR_MR_EMPTY_INSTANCE = new DicomWebTestInstance().series(MISSINGNO_MR_MR_EMPTY).sopInstanceUID('1.3.6.1.4.1.9328.50.50.182738097536783268727347648053358626718')
    public static final DicomWebTestInstance MG_MG_1_INSTANCE = new DicomWebTestInstance().series(MG_MG_1).sopInstanceUID('1.3.6.1.4.1.14519.5.2.1.4792.2001.287985434210601932151578831635')
    public static final DicomWebTestInstance MG_MG_2_INSTANCE = new DicomWebTestInstance().series(MG_MG_2).sopInstanceUID('1.3.6.1.4.1.14519.5.2.1.4792.2001.326853941362912707889211241598')
    public static final DicomWebTestInstance MG_MG_3_INSTANCE = new DicomWebTestInstance().series(MG_MG_3).sopInstanceUID('1.3.6.1.4.1.14519.5.2.1.4792.2001.311338505539845498757513260134')
    // Data in TestsForSecurity
    public static final DicomWebTestInstance PETMR4_PT_1_INSTANCE = new DicomWebTestInstance().series(PETMR4_PT_1).sopInstanceUID('1.3.6.1.4.1.14519.5.2.1.5168.1900.136109928602105100714068646347')
    public static final DicomWebTestInstance PETMR4_MR_4488021_INSTANCE = new DicomWebTestInstance().series(PETMR4_MR_4488021).sopInstanceUID('1.3.6.1.4.1.14519.5.2.1.5168.1900.120127367752469895524130707190')
    public static final DicomWebTestInstance PETMR4_MR_4494017_INSTANCE = new DicomWebTestInstance().series(PETMR4_MR_4494017).sopInstanceUID('1.3.6.1.4.1.14519.5.2.1.5168.1900.316966337171675985204715863168')
    // Data in TestsWithDifferentTransferSyntaxes
    public static final DicomWebTestInstance BIG_ENDIAN_US_0_INSTANCE = new DicomWebTestInstance().series(BIG_ENDIAN_US_0).sopInstanceUID('1.2.276.0.7230010.3.1.4.0.35635.1517265970.592489')
    public static final DicomWebTestInstance BIG_ENDIAN_US_1_INSTANCE = new DicomWebTestInstance().series(BIG_ENDIAN_US_1).sopInstanceUID('1.2.276.0.7230010.3.1.4.0.35668.1517266217.85068')
    public static final DicomWebTestInstance JPEGLOSSLESS_MR_3_INSTANCE = new DicomWebTestInstance().series(JPEGLOSSLESS_MR_3).sopInstanceUID('1.2.840.113654.2.45.6231.260305947168620663018901903642874398423')
    public static final DicomWebTestInstance MIXED_FRAME_MR_0_INSTANCE = new DicomWebTestInstance().series(MIXED_FRAME_MR_0).sopInstanceUID('1.3.6.1.4.1.5962.1.1.5011.0.1.1166546115.14677')
    public static final DicomWebTestInstance MIXED_FRAME_MR_1_INSTANCE = new DicomWebTestInstance().series(MIXED_FRAME_MR_1).sopInstanceUID('1.3.6.1.4.1.5962.1.1.5011.1.1.1166546115.14677')
    public static final DicomWebTestInstance MIXED_FRAME_MR_2_INSTANCE = new DicomWebTestInstance().series(MIXED_FRAME_MR_2).sopInstanceUID('1.3.6.1.4.1.5962.1.1.5011.2.1.1166546115.14677')
    public static final DicomWebTestInstance MIXED_FRAME_MR_3_INSTANCE = new DicomWebTestInstance().series(MIXED_FRAME_MR_3).sopInstanceUID('1.3.6.1.4.1.5962.1.1.5011.3.1.1166546115.14677')
    public static final DicomWebTestInstance MIXED_FRAME_MR_100_INSTANCE = new DicomWebTestInstance().series(MIXED_FRAME_MR_100).sopInstanceUID('1.3.6.1.4.1.9328.50.51.193665397951601366505963308726521622780')

    DicomWebTestInstance series(DicomWebTestSeries series) {
        setSeries(series)
        series.knownInstances << this
        this
    }

    DicomWebTestInstance sopInstanceUID(String uid) {
        setSopInstanceUID(uid)
        this
    }

    @Override
    DicomWebTestStudy parentStudy() {
        series.study
    }

    @Override
    protected Map<String, DatasetWithFMI> filter(Map<String, DatasetWithFMI> instances) {
        instances.findAll { uid, datasetWithFMI ->
            datasetWithFMI.dataset.getString(Tag.SOPInstanceUID) == sopInstanceUID
        }
    }

}
