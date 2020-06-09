package com.pb.apod.ui


import android.widget.DatePicker
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.navigation.Navigation
import androidx.navigation.testing.TestNavHostController
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.IdlingRegistry
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.PickerActions
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.pb.apod.R
import com.pb.apod.common.EspressoIdlingResource
import com.pb.apod.common.RxSchedulerImmediate
import com.pb.apod.common.RxSchedulers
import com.pb.apod.data.api.ApodService
import com.pb.apod.data.model.ApodResponse
import com.pb.apod.data.repository.ApodRepository
import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.impl.annotations.RelaxedMockK
import io.reactivex.rxjava3.core.Single
import org.hamcrest.Matchers
import org.hamcrest.core.IsNot.not
import org.junit.*
import org.junit.runner.RunWith
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.generic.bind
import org.kodein.di.generic.instance
import org.kodein.di.generic.provider
import org.kodein.di.generic.singleton


@RunWith(AndroidJUnit4::class)
class MainFragmentTest : KodeinAware {
    private val instrumentationContext = InstrumentationRegistry.getInstrumentation().context
    override val kodein = Kodein.lazy {
        //import(androidXModule(this@ApodApplication))
        bind() from singleton { ApodService(instance()) }
        bind() from singleton { RxSchedulers() }
        bind() from singleton { ApodRepository(instance()) }
        bind() from provider { ApodViewModelFactory(instance(), instance()) }
    }

    @get:Rule
    val viewModelRule = InstantTaskExecutorRule()

    @RelaxedMockK
    private lateinit var repository: ApodRepository
    private lateinit var subject: ApodViewModel
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
        IdlingRegistry.getInstance().register(EspressoIdlingResource.countingIdlingResource)
        MockKAnnotations.init(this)
        subject = ApodViewModel(
            repository = repository,
            rxSchedulers = RxSchedulerImmediate()
        )
    }

    @After
    fun tearDown() {
        IdlingRegistry.getInstance().unregister(EspressoIdlingResource.countingIdlingResource)
    }

    @Test
    fun `testNavigationToPhotoFragment`() {
        every { repository.getAPOD() } returns Single.just(expectedApodByDate)
        // Create a TestNavHostController
        val navController = TestNavHostController(
            ApplicationProvider.getApplicationContext()
        )
        navController.setGraph(R.navigation.main_navigation)

        val mainScenario = launchFragmentInContainer<MainFragment>()

        // Set the NavController property on the fragment
        mainScenario.onFragment { fragment ->
            Navigation.setViewNavController(fragment.requireView(), navController)
        }
        // Verify that performing a click changes the NavController’s state
        onView(withId(R.id.btn_play)).perform(ViewActions.click())
        //assertThat(navController.currentDestination?.id).isEqualTo(R.id.in_game)
        Assert.assertEquals(navController.currentDestination?.id, R.id.videoFragment)
    }

    @Test
    fun `test_to_check_if_homescreen_is_displayedcorrectly_on_launch`() {
        launchFragmentInContainer<MainFragment>()
        onView(withId(R.id.tv_title)).check(matches(isDisplayed()))
        onView(withId(R.id.tv_explanation)).check(matches(not(isDisplayed())))
        onView(withId(R.id.btn_play)).check(matches(isDisplayed()))
        onView(withId(R.id.iv_calender)).check(matches(isDisplayed()))
        onView(withId(R.id.textView2)).check(matches(withText(R.string.swipe_to_view_description)))
    }



}