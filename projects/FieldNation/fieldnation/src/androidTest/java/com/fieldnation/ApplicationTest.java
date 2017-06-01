package com.fieldnation;

import android.test.ApplicationTestCase;

import com.fieldnation.fnlog.Log;
import com.fieldnation.v2.data.client.WorkordersWebApi;
import com.fieldnation.v2.data.listener.TransactionParams;

import java.util.concurrent.CountDownLatch;

/**
 * <a href="http://d.android.com/tools/testing/testing_android.html">Testing Fundamentals</a>
 */
public class ApplicationTest extends ApplicationTestCase<App> {
    private static final String TAG = "ApplicationTest";

    public ApplicationTest() {
        super(App.class);
    }

    @Override
    protected void tearDown() throws Exception {
        super.tearDown();

        if (_client != null)
            _client.disconnect(App.get());
    }

    private WorkordersWebApi _client;
    private CountDownLatch signal;

    public void test_api() throws Exception {
        signal = new CountDownLatch(1);
        _client = new WorkordersWebApi(_workordersWebApi_listener);
        _client.connect(App.get());

        WorkordersWebApi.getWorkOrderLists(App.get(), true, false);

        signal.await();
        Log.v(TAG, "break!");
    }


    private WorkordersWebApi.Listener _workordersWebApi_listener = new WorkordersWebApi.Listener() {
        @Override
        public void onConnected() {
            _client.subWorkordersWebApi();
        }

        @Override
        public boolean processTransaction(TransactionParams transactionParams, String methodName) {
            return methodName.equals("getWorkOrderLists");
        }

        @Override
        public void onComplete(TransactionParams transactionParams, String methodName, Object successObject, boolean success, Object failObject) {
            if (methodName.equals("getWorkOrderLists")) {
                signal.countDown();
                Log.v(TAG, "onGetWorkOrderLists");
            }
        }
    };
}
