package com.dicoding.submissionintermediatedicoding

import androidx.recyclerview.widget.RecyclerView
import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.IdlingRegistry
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.MediumTest
import com.dicoding.submissionintermediatedicoding.data.remote.api.RetrofitConfig
import com.dicoding.submissionintermediatedicoding.utils.EspressoIdlingResource
import com.dicoding.submissionintermediatedicoding.utils.JsonConverter
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
@MediumTest
class MainActivityTest {

    @get:Rule
    val rule = ActivityScenarioRule(MainActivity::class.java)
    private val mockWebServer = MockWebServer()

    @Before
    fun setUp(){
        ActivityScenario.launch(MainActivity::class.java)
        mockWebServer.start(8080)
        RetrofitConfig.BASE_URL = "http://127.0.0.1:8080/"
        IdlingRegistry.getInstance().register(EspressoIdlingResource.countingIdlingResource)
    }

    @After
    fun tearDown() {
        mockWebServer.shutdown()
        IdlingRegistry.getInstance().unregister(EspressoIdlingResource.countingIdlingResource)
    }

    @Test
    fun getListStory_responseSuccess(){
        val mockResponse = MockResponse()
            .setResponseCode(200)
            .setBody(JsonConverter.readStringFromFile("response.json"))
        mockWebServer.enqueue(mockResponse)

        onView(withId(R.id.rv_story))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))

        onView(withId(R.id.rv_story))
            .perform(RecyclerViewActions.scrollToPosition<RecyclerView.ViewHolder>(7))
    }

    @Test
    fun getListStory_toMapView(){
        val mockResponse = MockResponse()
            .setResponseCode(200)
            .setBody(JsonConverter.readStringFromFile("response.json"))
        mockWebServer.enqueue(mockResponse)

        onView(withId(R.id.fab_toMap)).perform(ViewActions.click())
    }
}
