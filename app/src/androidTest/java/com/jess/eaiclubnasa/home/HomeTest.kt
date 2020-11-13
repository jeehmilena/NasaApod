package com.jess.eaiclubnasa.home

import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.rule.ActivityTestRule
import com.jess.eaiclubnasa.ApodService
import com.jess.eaiclubnasa.R
import com.jess.eaiclubnasa.robots.HomeRobot
import com.jess.eaiclubnasa.view.HomeActivity
import kotlinx.android.synthetic.main.fragment_apod.view.*
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class HomeTest {
    private lateinit var robot: HomeRobot
    private var mockWebServer: MockWebServer? = null

    init {
        mockWebServer = MockWebServer()
        ApodService.setBaseUrl(mockWebServer?.url("/").toString())
    }

    @get:Rule
    var activityRule = ActivityTestRule(HomeActivity::class.java, false, false)

    @Before
    fun setup() {
        robot = HomeRobot(mockWebServer, activityRule)
    }

    @After
    fun tearDown() {
        mockWebServer?.shutdown();
        robot.finishActivity()
    }

    @Test
    fun testIsListFragmentVisibleOnApplaunch() {
        robot.setRequest()
            .startActivity()
            .checkIsDisplayedRecyclerView()
    }

    @Test
    fun testSelectedItemisDetailFragmentVIsible() {
        robot.setRequest()
            .startActivity()
            .testSelectedItemisDetailFragmentVisible()
    }
}