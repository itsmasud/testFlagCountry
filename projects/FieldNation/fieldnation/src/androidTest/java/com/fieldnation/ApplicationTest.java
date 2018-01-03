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

        _workOrdersApi.unsub();
    }

    private CountDownLatch signal;

    public void test_api() throws Exception {
        signal = new CountDownLatch(1);
        _workOrdersApi.sub();

        WorkordersWebApi.getWorkOrderLists(App.get(), true, false);

        signal.await();
        Log.v(TAG, "break!");
    }

    private WorkordersWebApi _workOrdersApi = new WorkordersWebApi() {
        @Override
        public boolean processTransaction(TransactionParams transactionParams, String methodName) {
            return methodName.equals("getWorkOrderLists");
        }

        @Override
        public boolean onComplete(TransactionParams transactionParams, String methodName, Object successObject, boolean success, Object failObject, boolean isCached) {
            if (methodName.equals("getWorkOrderLists")) {
                signal.countDown();
                Log.v(TAG, "onGetWorkOrderLists");
            }
            return super.onComplete(transactionParams, methodName, successObject, success, failObject, isCached);
        }
    };
}
