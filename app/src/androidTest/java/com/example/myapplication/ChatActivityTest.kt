package com.example.myapplication

import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.closeSoftKeyboard
import androidx.test.espresso.action.ViewActions.typeText
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.matcher.ViewMatchers.hasDescendant
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.hamcrest.Matchers.startsWith
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class ChatActivityTest {

    @get:Rule
    val activityRule = ActivityScenarioRule(ChatActivity::class.java)


    @Test
    fun testInitialMessageDisplayed() {

        onView(withId(R.id.recyclerView))
            .check(matches(hasDescendant(withText("Privyet! What secrets are you selling today?"))))
    }


//    @Test
//    fun testSendMessageNonEmulator() {
//        onView(withId(R.id.etMessage))
//            .perform(typeText("Hello"), closeSoftKeyboard())
//        onView(withId(R.id.btnSend))
//            .perform(click())
//        onView(withId(R.id.recyclerView))
//            .check(matches(hasDescendant(withText("Hello"))))
//        onView(withId(R.id.recyclerView))
//            .check(matches(hasDescendant(withText("Here is your flag: winner"))))
//    }

    @Test
    fun testSendMessageEmulator() {

        onView(withId(R.id.etMessage))
            .perform(typeText("Hello"), closeSoftKeyboard())
        onView(withId(R.id.btnSend))
            .perform(click())

        onView(withId(R.id.recyclerView))
            .perform(RecyclerViewActions.scrollTo<RecyclerView.ViewHolder>(hasDescendant(withText("Hello"))))

        onView(withId(R.id.recyclerView))
            .check(matches(hasDescendant(withText("Hello"))))

        onView(withId(R.id.recyclerView))
            .perform(RecyclerViewActions.scrollTo<RecyclerView.ViewHolder>(hasDescendant(withText("Americanski I know u are using an emulator!"))))

        onView(withId(R.id.recyclerView))
            .check(matches(hasDescendant(withText(startsWith("Americanski I know u are using an emulator!")))))
    }
}
