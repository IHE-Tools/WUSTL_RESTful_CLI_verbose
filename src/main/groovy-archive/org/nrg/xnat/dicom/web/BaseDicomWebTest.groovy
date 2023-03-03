package org.nrg.xnat.dicom.web

import org.nrg.testing.xnat.BaseXnatRestTest
import org.nrg.testing.xnat.rest.XnatRestDriver
import org.nrg.xnat.dicom.jackson.module.JsonDicomWebDeserializationModule
import org.nrg.xnat.pogo.Project
import org.nrg.xnat.pogo.experiments.ImagingSession
import org.nrg.xnat.pogo.extensions.subject_assessor.SessionImportExtension
import org.testng.annotations.AfterClass
import org.testng.annotations.BeforeClass
import org.testng.annotations.BeforeSuite
import java.util.*

import static org.testng.AssertJUnit.assertFalse

abstract class BaseDicomWebTest extends BaseXnatRestTest {

    List<Project> projects

    @SuppressWarnings("GroovyResultOfObjectAllocationIgnored")
    @BeforeSuite
    void implicitClassLoad() {
        new DicomWebTestInstance()
        new DicomWebTestSeries()
        new DicomWebTestStudy()
        assertFalse(DicomWebTestStudy.CT1.series.isEmpty())
        assertFalse(DicomWebTestStudy.CT1.series[0].knownInstances.isEmpty())
    }

    @BeforeSuite
    void addDicomWebDeserializers() {
        XnatRestDriver.XNAT_REST_MAPPER.registerModule(JsonDicomWebDeserializationModule.build())
        restDriver.disableSiteAnonScript(mainAdminUser)
    }

    @BeforeClass
    void setupProjects() {
        projects = defineClassProjects()
        projects.each { project ->
            restDriver.createProject(mainAdminUser, project)
        }
    }

    @AfterClass(alwaysRun = true)
    void performCleanup() {
        projects.each { project ->
            restDriver.deleteProjectSilently(mainAdminUser, project)
        }
    }

    abstract List<Project> defineClassProjects()

    protected void execute(DicomWebTestSpec testSpec) {
        testSpec.execute(restDriver)
    }

    protected void addImportExtension(ImagingSession session, DicomWebTestStudy study) {
        new SessionImportExtension(restDriver.interfaceFor(mainAdminUser), session, study.toFile())
    }

}
