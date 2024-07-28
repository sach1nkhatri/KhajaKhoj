
package com.example.khajakhoj

import androidx.test.espresso.Espresso.closeSoftKeyboard
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import com.example.khajakhoj.activity.Dashboard
import com.example.khajakhoj.activity.LoginPage
import com.example.khajakhoj.activity.SignUpActivity
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith


@LargeTest
@RunWith(AndroidJUnit4::class)
class LoginPageInstrumentedTest {

    @get:Rule
    val activityRule = ActivityScenarioRule(LoginPage::class.java)

    @Test
    fun testLoginSuccess() {
        Intents.init()
        onView(withId(R.id.username_input)).perform(typeText("test@example.com"))
        onView(withId(R.id.password_input)).perform(replaceText("password"))

        closeSoftKeyboard()

        onView(withId(R.id.login_Btn)).perform(click())
        Thread.sleep(1000)
        Intents.intended(hasComponent(Dashboard::class.java.name))

        Intents.release()
    }


    @Test
    fun testSignUpNavigation() {
        Intents.init()
        onView(withId(R.id.signup)).perform(click())
        Intents.intended(hasComponent(SignUpActivity::class.java.name))

        Intents.release()
    }

    @Test
    fun testForgotPasswordDialogPopup() {
        onView(withId(R.id.forgotpasswordBtn)).perform(click())
        onView(withText("Forgot Password?")).check(matches(isDisplayed()))
    }

}