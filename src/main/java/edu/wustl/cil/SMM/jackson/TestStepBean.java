package edu.wustl.cil.SMM.jackson;

public class TestStepBean {
    private String stepIdentifier;
    private String description;
    private String optionality;
    private String action;
    private String attachment;
    private String inputFolder;
    private String outputFolder;
    private String inputReference;
    private String inputToTest;
    private String toTestType;
    private String mimeTypeReference;
    private String mimeTypeToTest;
    private String registryPackageElements;
    private String fieldNamesValues;
    private String fieldSpecification;
    private String conformanceRequirements;
    private String consistencyRequirements;
    private String referenceFolder;
    private String submittedFolder;

    public enum Action {
        UNDEFINED,
        EXTRACT_360X_SUBMISSION, EXTRACT_XDM, EXTRACT_XDR, INDEX_EXPLODED_FILES,
        COMPARE, COMPARE_METADATA, COMPARE_METADATA_FIXED,
        METADATA_CONSISTENCY, SUBMISSION_SET_CONSISTENCY,
        SUBMISSION_SET_CONFORMANCE,
        DOCUMENT_ENTRY_LINKAGE,
        DOCUMENT_ENTRY_CONFORMANCE_HL7V2,
        DOCUMENT_CONFORMANCE_HL7V2,
        DOCUMENT_ENTRY_CONFORMANCE_CDA,
        DOCUMENT_CONFORMANCE_CDA,
        DOC_ENTRY_DOCUMENT_CONSISTENCY_HL7V2,
        CONFORMANCE,
        CONSISTENCY
    };

    public TestStepBean(String stepIdentifier, String optionality, String action, String attachment, String inputFolder) {
        this.stepIdentifier = stepIdentifier;
        this.optionality = optionality;
        this.action = action;
        this.attachment = attachment;
        this.inputFolder= inputFolder;
    }

    public TestStepBean() {
     }

     public void initialize(String stepIdentifier, String optionality, String action, String attachment, String inputFolder) {
         this.stepIdentifier = stepIdentifier;
         this.optionality = optionality;
         this.action = action;
         this.attachment = attachment;
         this.inputFolder= inputFolder;
     }

    public static Action parseAction(String x) throws Exception {
        Action action = Action.UNDEFINED;

        switch (x) {
            case "Extract360XSubmission":
            case "EXTRACT_360X_SUBMISSION":
                action = Action.EXTRACT_360X_SUBMISSION;
                break;
            case "ExtractXDM":
            case "EXTRACT_XDM":
                action = Action.EXTRACT_XDM;
                break;
            case "ExtractXDR":
            case "EXTRACT_XDR":
                action = Action.EXTRACT_XDR;
                break;
            case "IndexExplodedFiles":
            case "INDEX_EXPLODED_FILES":
                action = Action.INDEX_EXPLODED_FILES;
                break;
            case "Compare":
            case "COMPARE":
                action = Action.COMPARE;
                break;
            case "CompareMetadata":
            case "COMPARE_METADATA":
                action = Action.COMPARE_METADATA;
                break;
            case "CompareMetadataFixed":
            case "COMPARE_METADATA_FIXED":
                action = Action.COMPARE_METADATA_FIXED;
                break;
            case "MetadataConsistency":
            case "METADATA_CONSISTENCY":
                action = Action.METADATA_CONSISTENCY;
                break;
            case "SubmissionSetConsistency":
            case "SUBMISSION_SET_CONSISTENCY":
                action = Action.SUBMISSION_SET_CONSISTENCY;
                break;
            case "SubmissionSet-Conformance":
            case "SUBMISSION_SET_CONFORMANCE":
                action = Action.SUBMISSION_SET_CONFORMANCE;
                break;
            case "DocumentEntry-Linkage":
            case "DOCUMENT_ENTRY_LINKAGE":
                action = Action.DOCUMENT_ENTRY_LINKAGE;
                break;

            case "DocumentEntry-Conformance-HL7V2":
            case "DOCUMENT_ENTRY_CONFORMANCE_HL7V2":
                action = Action.DOCUMENT_ENTRY_CONFORMANCE_HL7V2;
                break;
            case "Document-Conformance-HL7V2":
            case "DOCUMENT_CONFORMANCE_HL7V2":
                action = Action.DOCUMENT_CONFORMANCE_HL7V2;
                break;
            case "DocumentEntry_Document-Consistency-HL7V2":
            case "DOCUMENT_ENTRY_DOCUMENT_CONSISTENCY_HL7V2":
                action = Action.DOC_ENTRY_DOCUMENT_CONSISTENCY_HL7V2;
                break;

            case "DocumentEntry-Conformance-CDA":
            case "DOCUMENT_ENTRY_CONFORMANCE_CDA":
                action = Action.DOCUMENT_ENTRY_CONFORMANCE_CDA;
                break;
            case "Document-Conformance-CDA":
            case "DOCUMENT_CONFORMANCE_CDA":
                //action = Action.DOCUMENT_ENTRY_CONFORMANCE_CDA;
                throw new Exception(x);
//                break;
            case "Conformance":
            case "CONFORMANCE":
                action = Action.CONFORMANCE;
                break;
            case "Consistency":
            case "CONSISTENCY":
                action = Action.CONSISTENCY;
                break;
        }
        return action;
    }

    // Getters / setters

    public String getStepIdentifier() {
        return stepIdentifier;
    }

    public void setStepIdentifier(String stepIdentifier) {
        this.stepIdentifier = stepIdentifier;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getOptionality() {
        return optionality;
    }

    public void setOptionality(String optionality) {
        this.optionality = optionality;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public String getAttachment() {
        return attachment;
    }

    public void setAttachment(String attachment) {
        this.attachment = attachment;
    }

    public String getInputFolder() {
        return inputFolder;
    }

    public void setInputFolder(String inputFolder) {
        this.inputFolder = inputFolder;
    }

    public String getOutputFolder() {
        return outputFolder;
    }

    public void setOutputFolder(String outputFolder) {
        this.outputFolder = outputFolder;
    }

    public String getInputReference() {
        return inputReference;
    }

    public void setInputReference(String inputReference) {
        this.inputReference = inputReference;
    }

    public String getInputToTest() {
        return inputToTest;
    }

    public void setInputToTest(String inputToTest) {
        this.inputToTest = inputToTest;
    }

    public String getToTestType() {
        return toTestType;
    }

    public void setToTestType(String toTestType) {
        this.toTestType = toTestType;
    }

    public String getMimeTypeReference() {
        return mimeTypeReference;
    }

    public void setMimeTypeReference(String mimeTypeReference) {
        this.mimeTypeReference = mimeTypeReference;
    }

    public String getMimeTypeToTest() {
        return mimeTypeToTest;
    }

    public void setMimeTypeToTest(String mimeTypeToTest) {
        this.mimeTypeToTest = mimeTypeToTest;
    }

    public String getRegistryPackageElements() {
        return registryPackageElements;
    }

    public void setRegistryPackageElements(String registryPackageElements) {
        this.registryPackageElements = registryPackageElements;
    }

    public String getFieldNamesValues() {
        return fieldNamesValues;
    }

    public void setFieldNamesValues(String fieldNamesValues) {
        this.fieldNamesValues = fieldNamesValues;
    }

    public String getFieldSpecification() {
        return fieldSpecification;
    }

    public void setFieldSpecification(String fieldSpecification) {
        this.fieldSpecification = fieldSpecification;
    }

    public String getConformanceRequirements() {
        return conformanceRequirements;
    }

    public void setConformanceRequirements(String conformanceRequirements) {
        this.conformanceRequirements = conformanceRequirements;
    }

    public String getConsistencyRequirements() {
        return consistencyRequirements;
    }

    public void setConsistencyRequirements(String consistencyRequirements) {
        this.consistencyRequirements = consistencyRequirements;
    }

    public String getReferenceFolder() {
        return referenceFolder;
    }

    public void setReferenceFolder(String referenceFolder) {
        this.referenceFolder = referenceFolder;
    }

    public String getSubmittedFolder() {
        return submittedFolder;
    }

    public void setSubmittedFolder(String submittedFolder) {
        this.submittedFolder = submittedFolder;
    }
}
