package com.pb.apod.ui

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.pb.apod.common.RxSchedulerImmediate
import com.pb.apod.data.model.ApodResponse
import com.pb.apod.data.repository.ApodRepository
import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.impl.annotations.RelaxedMockK
import io.reactivex.rxjava3.core.Single
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test


class ApodViewModelTest {
    @get:Rule
    val viewModelRule = InstantTaskExecutorRule()

    @RelaxedMockK
    private lateinit var repository: ApodRepository
    private lateinit var subject: ApodViewModel
    private val expectedModel = ApodResponse("", "", "", "", "", "", "")

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        subject = ApodViewModel(repository=repository,
            rxSchedulers = RxSchedulerImmediate())
    }

    @Test
    fun `given a new Apod,when observing on state,it should provide an initial state`() {
        val actual = subject.apod.value!!
        assertEquals(actual, ApodResponse("", "", "", "", "", "", ""))
    }

    @Test
    fun `given a succesfull apod fetch,when observing on apod,it shoudl provide a apod`() {

        every { repository.getAPOD() } returns Single.just(expectedModel)
        subject.getApod()
        val actual = subject.apod.value!!
        assertEquals(actual, expectedModel)
    }
}