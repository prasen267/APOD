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
    private val expectedApodByDate = ApodResponse(
        "2016-11-05",
        "Shot in Ultra HD, this stunning video can take you on a tour of the International Space Station. A fisheye lens with sharp focus and extreme depth of field provides an immersive visual experience of life in the orbital outpost. In the 18 minute fly-through, your point of view will float serenely while you watch our fair planet go by 400 kilometers below the seven-windowed Cupola, and explore the interior of the station's habitable nodes and modules from an astronaut's perspective. The modular International Space Station is Earth's largest artificial satellite, about the size of a football field in overall length and width. Its total pressurized volume is approximately equal to that of a Boeing 747 aircraft.",
        "",
        "video",
        "v1",
        "ISS Fisheye Fly-Through",
        "https://www.youtube.com/embed/DhmdyQdu96M?rel=0"
    )

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        subject = ApodViewModel(
            repository = repository,
            rxSchedulers = RxSchedulerImmediate()
        )
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

    @Test
    fun `given a succesfull ApodByDate fetch,when observing on apod,it shoudl provide a apod`() {

        every { repository.getApodByDate("1995-11-11") } returns Single.just(expectedApodByDate)
        subject.getApodByDate("1995-11-11")
        val actual = subject.apod.value!!
        assertEquals(actual, expectedApodByDate)
    }

}