package com.example.myapplication

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.closeSoftKeyboard
import androidx.test.espresso.action.ViewActions.typeText
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

// don't need this anymore but will keep just to be safe
// https://stackoverflow.com/a/49834662
//class ToastMatcher(private val maxFailures: Int ) : TypeSafeMatcher<Root>() {
//    private var failures = 1
//
//    override fun describeTo(description: Description) {
//        description.appendText("is toast")
//    }
//
//    override fun matchesSafely(root: Root): Boolean {
//
//        val type = root.windowLayoutParams.get().type
//        @Suppress("DEPRECATION")
//        if (type == WindowManager.LayoutParams.TYPE_TOAST || type == WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY) {
//            val windowToken = root.decorView.windowToken
//            val appToken = root.decorView.applicationWindowToken
//            if (windowToken == appToken){
//                return true
//            }
//            //return windowToken === appToken
//        }
//        return (++failures >= maxFailures)
//        //return false
//    }
//}
@RunWith(AndroidJUnit4::class)
class LoginActivityTest {

    @get:Rule
    val activityRule = ActivityScenarioRule(LoginActivity::class.java)

    //https://developer.android.com/training/testing/espresso/setup#kotlin
    @Test fun testSuccessfulLogin() {

        onView(withId(R.id.username))
            .perform(typeText("anyuser"), closeSoftKeyboard())


        onView(withId(R.id.password))
            .perform(typeText("secret123"), closeSoftKeyboard())


        //onView(withId(R.id.login_button)).perform(click())

        Intents.init()
        onView(withId(R.id.login_button)).perform(click())
        Intents.intended(hasComponent(ChatActivity::class.java.name))
        Intents.release()

    }

    @Test fun testInvalidPassword() {
        onView(withId(R.id.username))
            .perform(typeText("anyuser"), closeSoftKeyboard())
        onView(withId(R.id.password))
            .perform(typeText("wrongpassword"), closeSoftKeyboard())
        onView(withId(R.id.login_button)).perform(click())
        //Thread.sleep(1000)
        onView(withId(com.google.android.material.R.id.snackbar_text))
            .check(matches(withText("Invalid password")))

    }

    @Test fun backwardsPasswordInput() {
        onView(withId(R.id.username))
            .perform(typeText("secret123"), closeSoftKeyboard())
        onView(withId(R.id.password))
            .perform(typeText("anyuser"), closeSoftKeyboard())
        onView(withId(R.id.login_button)).perform(click())
        onView(withId(com.google.android.material.R.id.snackbar_text))
            .check(matches(withText("blyat check the field!")))
    }



}