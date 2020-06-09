package com.pb.apod.data.repository

import com.pb.apod.data.api.ApodService
import com.pb.apod.data.model.ApodResponse
import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.impl.annotations.RelaxedMockK
import io.mockk.unmockkAll
import io.mockk.verify
import io.reactivex.rxjava3.core.Single
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

class ApodRepositoryTest{


    @RelaxedMockK
    private lateinit var apodService: ApodService
    private lateinit var subject: ApodRepository
    private val expectedApodResponse = ApodResponse(
        "1995-11-11",
        "Recently two new types of lightning have been verified: red sprites and blue jets.  These atmospheric discharges occur very high in the Earth's atmosphere - much higher than the familiar form of lightning. Red sprites appear red in color and go from the tops of clouds to as high as the ionosphere - an ionized layer 90 kilometers above the Earth's surface. They last only a small fraction of a second.  The existence of red sprites has been suggested previously, but only in 1994 were aircraft flown above massive thunderstorms with the high speed video equipment necessary to verify these spectacular events. Scientists are unsure of the cause and nature of red sprites.",
        "https://apod.nasa.gov/apod/image/redsprite.gif",
        "image",
        "v1",
        "Tommorow's picture",
        "https://apod.nasa.gov/apod/image/redsprite.gif"

    )

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        subject = ApodRepository(apodService)
    }

    @After
    fun tearDown() {
        unmockkAll()
    }

    fun givenAnUnsuccesfulNetworkingCall() {
        every { apodService.getApod() } returns Single.error(Exception())
    }

    fun givenAnSuccesfulNetworkingCall() {
        every { apodService.getApod() } returns Single
            .just(
                expectedApodResponse
            )
    }
    fun givenAnSuccesfulNetworkingCallForByDate() {
        every { apodService.getApodByDate("1995-11-11") } returns Single
            .just(
                expectedApodResponse
            )
    }
    @Test
    fun `given an unsuccessful network call,when getAPOD is called,an error should be emitted`() {
        givenAnUnsuccesfulNetworkingCall()

        subject.getAPOD().test()
            .assertNoValues()
            .assertNotComplete()
            .assertError(Throwable::class.java)
    }

    @Test
    fun `give a successful network call,a APOD should be emitted`() {

        givenAnSuccesfulNetworkingCall()
        subject.getAPOD().test()
            .assertNoErrors()
            .assertComplete()
            .assertValueCount(1)
    }

    @Test
    fun `given a successful network call,when getAPOD is called, the correct APod is provided`() {
        givenAnSuccesfulNetworkingCall()
        assertEquals(subject.getAPOD().blockingGet(),expectedApodResponse)


    }
    @Test
    fun`given a successful network call,when getApodByDate is called,the correct Apod should be provided`(){
        givenAnSuccesfulNetworkingCallForByDate()

        assertEquals(subject.getApodByDate("1995-11-11").blockingGet(),expectedApodResponse)
    }


}
