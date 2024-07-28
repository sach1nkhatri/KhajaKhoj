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
class SignupPageInstrumentedTest {

    @get:Rule
    val activityRule = ActivityScenarioRule(SignUpActivity::class.java)

    @Test
    fun testSuccessfulSignup() {
        Intents.init()
        onView(withId(R.id.fullNameEditText)).perform(typeText("Test User"))
        onView(withId(R.id.emailEditText)).perform(typeText("test10112@example.com"))
        onView(withId(R.id.phoneEditText)).perform(typeText("9841128000"))
        onView(withId(R.id.passwordEditText)).perform(replaceText("password"))
        onView(withId(R.id.confirmPasswordEditText)).perform(replaceText("password"))
        onView(withId(R.id.termsCheckBox)).perform(click())
        closeSoftKeyboard()

        onView(withId(R.id.signupCustBtn)).perform(click())
        Thread.sleep(5000)
        Intents.intended(hasComponent(LoginPage::class.java.name))

        Intents.release()
    }

    @Test
    fun testLoginNavigation() {
        Intents.init()
        onView(withId(R.id.backLoginView)).perform(click())
        Intents.intended(hasComponent(LoginPage::class.java.name))
        Intents.release()
    }

    @Test
    fun testTermsAndConditionsDialogPopup() {
        onView(withId(R.id.termsTextView)).perform(click())
        onView(withText("Terms and Conditions")).check(matches(isDisplayed()))
    }

}