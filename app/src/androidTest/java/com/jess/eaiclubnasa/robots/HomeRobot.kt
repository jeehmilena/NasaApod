package com.jess.eaiclubnasa.robots

import android.content.Context
import android.content.Intent
import android.view.View
import android.view.View.VISIBLE
import androidx.test.espresso.*
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.contrib.RecyclerViewActions.actionOnItemAtPosition
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.espresso.util.TreeIterables
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.rule.ActivityTestRule
import com.jess.eaiclubnasa.ApodAPI
import com.jess.eaiclubnasa.R
import com.jess.eaiclubnasa.view.HomeActivity
import com.jess.eaiclubnasa.view.adapter.ApodAdapter
import kotlinx.android.synthetic.main.fragment_apod.*
import okhttp3.mockwebserver.Dispatcher
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import okhttp3.mockwebserver.RecordedRequest
import org.hamcrest.Matcher
import org.hamcrest.Matchers.allOf
import java.io.IOException
import java.io.InputStream
import java.lang.Thread.sleep

class HomeRobot(
    private val mockWebServer: MockWebServer?,
    private val activityTestRule: ActivityTestRule<HomeActivity>
) {

    fun setRequest(): HomeRobot {
        mockWebServer?.dispatcher = object : Dispatcher() {
            override fun dispatch(request: RecordedRequest): MockResponse {
                if (request.path?.contains(ApodAPI.SOURCE) == true) {
                    return MockResponse().setBody(
                        readFileFromAssets(
                            "response.json",
                            InstrumentationRegistry.getInstrumentation().context
                        )
                    )
                }

                return MockResponse().setBody(
                    readFileFromAssets(
                        "error_not_found.json",
                        InstrumentationRegistry.getInstrumentation().context
                    )
                )
            }
        }

        return this
    }

    private fun readFileFromAssets(fileName: String, c: Context): String {
        return try {
            val `is`: InputStream = c.assets.open(fileName)
            val size: Int = `is`.available()
            val buffer = ByteArray(size)
            `is`.read(buffer)
            `is`.close()
            String(buffer)
        } catch (e: IOException) {
            throw RuntimeException(e)
        }
    }

    fun startActivity(): HomeRobot {
        activityTestRule.launchActivity(Intent(Intent.ACTION_MAIN))
        return this
    }

    fun finishActivity() {
        activityTestRule.finishActivity()
    }

    fun checkIsDisplayedRecyclerView() {
        onView(withId(R.id.rv_apod))
            .check(matches(isDisplayed()))
    }

    fun testSelectedItemisDetailFragmentVisible() {

        onView(withId(R.id.rv_apod))
            .check(matches(isDisplayed()))

        sleep(1000)

        onView(withId(R.id.rv_apod)).perform(
            RecyclerViewActions.scrollToPosition<ApodAdapter.ViewHolder>(
                1
            ), click()
        )

        sleep(1000)

        onView(withId(R.id.tv_apod_detail)).check(matches(isDisplayed()))

        onView(withText("Galileo Explores Europa")).check(matches(isDisplayed()))
    }

}