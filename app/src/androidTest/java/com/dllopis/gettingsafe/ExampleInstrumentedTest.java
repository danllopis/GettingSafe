package com.dllopis.gettingsafe;

import android.content.Context;
import android.content.Intent;

import androidx.test.core.app.ApplicationProvider;
import androidx.test.espresso.ViewAction;
import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.uiautomator.By;
import androidx.test.uiautomator.UiDevice;
import androidx.test.uiautomator.Until;


import org.junit.Test;
import org.junit.runner.RunWith;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.clearText;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.*;

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class ExampleInstrumentedTest {

    private UiDevice device;
    private static final int LAUNCH_TIMEOUT = 5000;
    private static final String BASIC_SAMPLE_PACKAGE
            = "com.dllopis.gettingsafe";

    @Test
    public void getToInitTrip() {
        device = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation());

        device.pressHome();

        final String launcherPackage = device.getLauncherPackageName();
        assertThat(launcherPackage, notNullValue());
        device.wait(Until.hasObject(By.pkg(launcherPackage).depth(0)),
                LAUNCH_TIMEOUT);

        Context context = ApplicationProvider.getApplicationContext();
        final Intent intent = context.getPackageManager()
                .getLaunchIntentForPackage(BASIC_SAMPLE_PACKAGE);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        context.startActivity(intent);

        device.wait(Until.hasObject(By.pkg(BASIC_SAMPLE_PACKAGE).depth(0)),
                LAUNCH_TIMEOUT);


        device.waitForWindowUpdate(null, 2500);

        onView(withId(R.id.initTripButton)).perform(click());



        device.waitForWindowUpdate(null, 3000);
    }

    @Test
    public void getToTripHistory() {
        device = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation());

        device.pressHome();

        final String launcherPackage = device.getLauncherPackageName();
        assertThat(launcherPackage, notNullValue());
        device.wait(Until.hasObject(By.pkg(launcherPackage).depth(0)),
                LAUNCH_TIMEOUT);

        Context context = ApplicationProvider.getApplicationContext();
        final Intent intent = context.getPackageManager()
                .getLaunchIntentForPackage(BASIC_SAMPLE_PACKAGE);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        context.startActivity(intent);

        device.wait(Until.hasObject(By.pkg(BASIC_SAMPLE_PACKAGE).depth(0)),
                LAUNCH_TIMEOUT);


        device.waitForWindowUpdate(null, 2500);

        onView(withId(R.id.lastTripsButton)).perform(click());



        device.waitForWindowUpdate(null, 3000);
    }

    @Test
    public void getToUserData() {
        device = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation());

        device.pressHome();

        final String launcherPackage = device.getLauncherPackageName();
        assertThat(launcherPackage, notNullValue());
        device.wait(Until.hasObject(By.pkg(launcherPackage).depth(0)),
                LAUNCH_TIMEOUT);

        Context context = ApplicationProvider.getApplicationContext();
        final Intent intent = context.getPackageManager()
                .getLaunchIntentForPackage(BASIC_SAMPLE_PACKAGE);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        context.startActivity(intent);

        device.wait(Until.hasObject(By.pkg(BASIC_SAMPLE_PACKAGE).depth(0)),
                LAUNCH_TIMEOUT);


        device.waitForWindowUpdate(null, 2500);

        onView(withId(R.id.updateUserInfo)).perform(click());



        device.waitForWindowUpdate(null, 3000);
    }

    @Test
    public void initTrip() {
        device = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation());

        device.pressHome();

        final String launcherPackage = device.getLauncherPackageName();
        assertThat(launcherPackage, notNullValue());
        device.wait(Until.hasObject(By.pkg(launcherPackage).depth(0)),
                LAUNCH_TIMEOUT);

        Context context = ApplicationProvider.getApplicationContext();
        final Intent intent = context.getPackageManager()
                .getLaunchIntentForPackage(BASIC_SAMPLE_PACKAGE);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        context.startActivity(intent);

        device.wait(Until.hasObject(By.pkg(BASIC_SAMPLE_PACKAGE).depth(0)),
                LAUNCH_TIMEOUT);


        device.waitForWindowUpdate(null, 2500);

        onView(withId(R.id.initTripButton)).perform(click());

        onView(withId(R.id.destinyTextEdit)).perform(click()).perform(typeText("Vialia"), closeSoftKeyboard());


        onView(withId(R.id.tripTime)).check(matches(withText("26 min")));



        device.waitForWindowUpdate(null, 3000);
    }

    @Test
    public void updateUserName() {
        device = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation());

        device.pressHome();

        final String launcherPackage = device.getLauncherPackageName();
        assertThat(launcherPackage, notNullValue());
        device.wait(Until.hasObject(By.pkg(launcherPackage).depth(0)),
                LAUNCH_TIMEOUT);

        Context context = ApplicationProvider.getApplicationContext();
        final Intent intent = context.getPackageManager()
                .getLaunchIntentForPackage(BASIC_SAMPLE_PACKAGE);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        context.startActivity(intent);

        device.wait(Until.hasObject(By.pkg(BASIC_SAMPLE_PACKAGE).depth(0)),
                LAUNCH_TIMEOUT);


        device.waitForWindowUpdate(null, 2500);

        onView(withId(R.id.updateUserInfo)).perform(click());

        onView(withId(R.id.userName)).perform(click()).perform(clearText()).perform(typeText("Pepe"), closeSoftKeyboard());

        onView(withId(R.id.saveButton)).perform(click());


        onView(withId(R.id.welcome)).check(matches(withText(containsString("Pepe"))));;



        device.waitForWindowUpdate(null, 3000);
    }
}