package com.freelance.samuelagbede.ubake;

import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * Created by Agbede Samuel D on 6/12/2017.
 */

@RunWith(AndroidJUnit4.class)
public class UITest {
    @Rule
    ActivityTestRule<MainActivity> mainActivityActivityTestRule = new ActivityTestRule<MainActivity>(MainActivity.class);

    @Test
    public void onRetryButtonClick(){

    }
}
