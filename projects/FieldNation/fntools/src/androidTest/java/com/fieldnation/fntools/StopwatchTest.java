package com.fieldnation.fntools;

import android.support.test.filters.SmallTest;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Test;
import org.junit.runner.RunWith;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

/**
 * Created by mc on 9/8/17.
 */

@RunWith(AndroidJUnit4.class)
@SmallTest
public class StopwatchTest {

    private void pauseThread() {
        try {
            Thread.sleep(1000);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @Test
    public void normal() {
        Stopwatch stopwatch = new Stopwatch(true);
        pauseThread();
        assertThat(stopwatch.finish() / 1000, is(equalTo(1L)));
    }

    @Test
    public void pauseAndUnpause() {
        Stopwatch stopwatch = new Stopwatch(true);
        pauseThread();
        stopwatch.pause();
        pauseThread(); // should not be timed
        stopwatch.unpause();
        pauseThread();
        assertThat(stopwatch.finish() / 1000, is(equalTo(2L)));
    }

    @Test
    public void pauseAndStart() {
        Stopwatch stopwatch = new Stopwatch(true);
        pauseThread();
        stopwatch.pause();
        pauseThread(); // should be not counted
        stopwatch.start();
        pauseThread();
        assertThat(stopwatch.finish() / 1000, is(equalTo(2L)));
    }

    @Test
    public void pauseAndFinish() {
        Stopwatch stopwatch = new Stopwatch(true);
        pauseThread();
        stopwatch.pause();
        pauseThread(); // should be not counted
        assertThat(stopwatch.finish() / 1000, is(equalTo(1L)));
    }

    @Test
    public void unpauseWithoutPause() {
        Stopwatch stopwatch = new Stopwatch(true);
        pauseThread();
        stopwatch.unpause();
        pauseThread();
        assertThat(stopwatch.finish() / 1000, is(equalTo(2L)));
    }

    @Test
    public void finishAndRestart() {
        Stopwatch stopwatch = new Stopwatch(true);
        pauseThread();
        assertThat(stopwatch.finishAndRestart() / 1000, is(equalTo(1L)));
        pauseThread();
        assertThat(stopwatch.finish() / 1000, is(equalTo(1L)));
    }
}
