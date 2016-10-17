package com.fieldnation.fnhttp;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.assertEquals;

/**
 * Instrumentation test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class ExampleInstrumentedTest {
    @Test
    public void useAppContext() throws Exception {
        // Context of the app under test.
        Context appContext = InstrumentationRegistry.getTargetContext();

        assertEquals("com.fieldnation.fnhttp.test", appContext.getPackageName());


        FnHttp.run(appContext, request, new ResultListener() {
            @Override
            public void onStart() {

            }

            @Override
            public void onProgress(long totalTime, long bytes) {

            }

            void onComplete(HttpResult httpResult){

            }

            @Override
            public void onSuccess(HttpResult httpResult) {

            }

            @Override
            public void onFail(Exception exception) {

            }
        });
    }
}
