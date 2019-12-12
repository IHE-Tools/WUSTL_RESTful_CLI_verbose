package org.nrg.xnat.dicom.web

import com.jayway.restassured.RestAssured
import com.jayway.restassured.specification.RequestSpecification
import org.nrg.testing.xnat.rest.XnatRestDriver
import org.nrg.xnat.pogo.users.User
import org.nrg.xnat.rest.Credentials

abstract class DicomWebTestSpec<T, U extends DicomWebTestSpec<T, U>> {

    String studyInstanceUID
    String seriesInstanceUID
    User user
    int expectedStatusCode
    String expectedBody = ''
    Map<String, Object> queryParams = [:]
    Collection<? extends T> expectedResult = []

    abstract void execute(XnatRestDriver driver)

    U studyInstanceUID(String uid) {
        setStudyInstanceUID(uid)
        this as U
    }

    U studyInstanceUID(DicomWebTestStudy study) {
        studyInstanceUID(study.studyInstanceUID)
    }

    U seriesInstanceUID(String uid) {
        setSeriesInstanceUID(uid)
        this as U
    }

    U seriesInstanceUID(DicomWebTestSeries series) {
        seriesInstanceUID(series.seriesInstanceUID)
    }

    U user(User user) {
        setUser(user)
        this as U
    }

    U expectedStatusCode(int code) {
        setExpectedStatusCode(code)
        this as U
    }

    U expectedBody(String body) {
        setExpectedBody(body)
        this as U
    }

    U queryParams(Map<String, Object> queryParams) {
        setQueryParams(queryParams)
        this as U
    }

    U expectedResult(Collection<? extends T> expectedResult) {
        setExpectedResult(expectedResult)
        setExpectedStatusCode(expectedResult.isEmpty() ? 204 : 200)
        this as U
    }

    U expectedResult(T result) {
        expectedResult([result])
    }

    U expectedResultIfGuestAllowed(boolean isGuestAllowed, Collection<? extends T> result) {
        isGuestAllowed ? expectedResult(result) : expectedStatusCode(401)
    }

    protected RequestSpecification buildBaseQuery() {
        (user == null) ? RestAssured.given() : Credentials.build(user)
    }

}
