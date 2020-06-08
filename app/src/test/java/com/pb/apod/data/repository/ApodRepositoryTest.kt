package com.pb.apod.data.repository

import org.junit.Test

class ApodRepositoryTest {
    private val subject = ApodRepository()

    @Test
    fun `given an api error,when getAPOD is called,an error should be emitted`() {
        subject.getAPOD().test()
            .assertNoValues()
            .assertNotComplete()
            .assertError(Throwable::class.java)
    }
}