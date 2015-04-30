package com.fieldnation.service.data.workorder;

import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.os.Parcelable;

import com.fieldnation.AsyncTaskEx;
import com.fieldnation.FileHelper;
import com.fieldnation.Log;
import com.fieldnation.UniqueTag;
import com.fieldnation.data.workorder.Deliverable;
import com.fieldnation.data.workorder.Expense;
import com.fieldnation.data.workorder.Pay;
import com.fieldnation.data.workorder.Schedule;
import com.fieldnation.data.workorder.Signature;
import com.fieldnation.data.workorder.Workorder;
import com.fieldnation.json.JsonArray;
import com.fieldnation.json.JsonObject;
import com.fieldnation.service.topics.TopicClient;
import com.fieldnation.ui.workorder.WorkorderDataSelector;

import java.io.File;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by Michael Carver on 3/13/2015.
 */
public class WorkorderDataClient extends TopicClient implements WorkorderDataConstants {
    public static final String STAG = "WorkorderDataClient";
    public final String TAG = UniqueTag.makeTag(STAG);

    public WorkorderDataClient(Listener listener) {
        super(listener);
    }

    public void disconnect(Context context) {
        super.disconnect(context, TAG);
    }

    /*-*****************************-*/
    /*-             list            -*/
    /*-*****************************-*/
    public static void list(Context context, WorkorderDataSelector selector, int page) {
        list(context, selector, page, false);
    }

    public static void list(Context context, WorkorderDataSelector selector, int page, boolean isSync) {
        Intent intent = new Intent(context, WorkorderDataService.class);
        intent.putExtra(PARAM_ACTION, PARAM_ACTION_LIST);
        intent.putExtra(PARAM_LIST_SELECTOR, selector.getCall());
        intent.putExtra(PARAM_PAGE, page);
        intent.putExtra(PARAM_IS_SYNC, isSync);
        context.startService(intent);
    }

    public boolean subList(boolean isSync) {
        return subList(null, isSync);
    }

    public boolean subList(WorkorderDataSelector selector) {
        return subList(selector, false);
    }

    public boolean subList(WorkorderDataSelector selector, boolean isSync) {
        String topicId = PARAM_ACTION_LIST;

        if (isSync) {
            topicId += "-SYNC";
        }

        if (selector != null) {
            topicId += "/" + selector.getCall();
        }

        return register(topicId, TAG);
    }

    /*-********************************-*/
    /*-             details            -*/
    /*-********************************-*/
    public static void get(Context context, long id) {
        get(context, id, false);
    }

    public static void get(Context context, long id, boolean isSync) {
        Intent intent = new Intent(context, WorkorderDataService.class);
        intent.putExtra(PARAM_ACTION, PARAM_ACTION_DETAILS);
        intent.putExtra(PARAM_ID, id);
        intent.putExtra(PARAM_IS_SYNC, isSync);
        context.startService(intent);
    }

    public boolean subGet(boolean isSync) {
        return subGet(0, isSync);
    }

    public boolean subGet(long workorderId) {
        return subGet(workorderId, false);
    }

    public boolean subGet(long workorderId, boolean isSync) {
        String topicId = PARAM_ACTION_DETAILS;

        if (isSync) {
            topicId += "-SYNC";
        }

        if (workorderId > 0) {
            topicId += "/" + workorderId;
        }

        return register(topicId, TAG);
    }

    /*-**********************************-*/
    /*-             signature            -*/
    /*-**********************************-*/


    /*-******************************************-*/
    /*-             workorder actions            -*/
    /*-******************************************-*/
    // complete workorder
    public static void requestComplete(Context context, long workorderId) {
        WorkorderTransactionBuilder.postComplete(context, workorderId);
    }

    public static void requestSetClosingNotes(Context context, long workorderId, String closingNotes) {
        WorkorderTransactionBuilder.postClosingNotes(context, workorderId, closingNotes);
    }

    // acknowledge hold
    public static void requestAcknowledgeHold(Context context, long workorderId) {
        WorkorderTransactionBuilder.postAcknowledgeHold(context, workorderId);
    }

    // counter offer
    public static void requestCounterOffer(Context context, long workorderId, boolean expires,
                                           String reason, int expiresAfterInSecond, Pay pay,
                                           Schedule schedule, Expense[] expenses) {
        WorkorderTransactionBuilder.postCounterOffer(context, workorderId, expires, reason,
                expiresAfterInSecond, pay, schedule, expenses);
    }

    // request
    public static void request(Context context, long workorderId, long expireInSeconds) {
        WorkorderTransactionBuilder.postRequest(context, workorderId, expireInSeconds);
    }

    public static void requestConfirmAssignment(Context context, long workorderId, String startTimeIso8601, String endTimeIso8601) {
        WorkorderTransactionBuilder.postConfirmAssignment(context, workorderId, startTimeIso8601, endTimeIso8601);
    }

    /*-******************************************-*/
    /*-             workorder checkin            -*/
    /*-******************************************-*/
    public boolean registerCheckin() {
        if (!isConnected())
            return false;

        return register(PARAM_ACTION_CHECKIN, TAG);
    }

    public static void requestCheckin(Context context, long workorderId) {
        WorkorderTransactionBuilder.postCheckin(context, workorderId);
    }

    public static void requestCheckin(Context context, long workorderId, Location location) {
        WorkorderTransactionBuilder.postCheckin(context, workorderId, location);
    }

    /*-*******************************************-*/
    /*-             workorder checkout            -*/
    /*-*******************************************-*/
    public boolean registerCheckout() {
        if (!isConnected())
            return false;

        return register(PARAM_ACTION_CHECKOUT, TAG);
    }

    public static void requestCheckout(Context context, long workorderId) {
        WorkorderTransactionBuilder.postCheckout(context, workorderId);
    }

    public static void requestCheckout(Context context, long workorderId, Location location) {
        WorkorderTransactionBuilder.postCheckout(context, workorderId, location);
    }

    public static void requestCheckout(Context context, long workorderId, int deviceCount) {
        WorkorderTransactionBuilder.postCheckout(context, workorderId, deviceCount);
    }

    public static void requestCheckout(Context context, long workorderId, int deviceCount, Location location) {
        WorkorderTransactionBuilder.postCheckout(context, workorderId, deviceCount, location);
    }


    /*-*****************************************-*/
    /*-             workorder bundle            -*/
    /*-*****************************************-*/
    public static void requestBundle(Context context, long bundleId) {
        requestBundle(context, bundleId, false);
    }

    public static void requestBundle(Context context, long bundleId, boolean isSync) {
        Intent intent = new Intent(context, WorkorderDataService.class);
        intent.putExtra(PARAM_ACTION, PARAM_ACTION_GET_BUNDLE);
        intent.putExtra(PARAM_ID, bundleId);
        intent.putExtra(PARAM_IS_SYNC, isSync);
        context.startService(intent);
    }

    public boolean registerBundle() {
        return registerBundle(false);
    }

    public boolean registerBundle(boolean isSync) {
        if (!isConnected())
            return false;

        Log.v(TAG, "registerBundle");

        return register(PARAM_ACTION_GET_BUNDLE + (isSync ? "-SYNC" : ""), TAG);
    }

    /*-*************************************-*/
    /*-             deliverables            -*/
    /*-*************************************-*/
    public static void requestUploadDeliverable(Context context, long workorderId, long uploadSlotId, String filename, String filePath) {
        Log.v(STAG, "requestUploadDeliverable");
        Intent intent = new Intent(context, WorkorderDataService.class);
        intent.putExtra(PARAM_ACTION, PARAM_ACTION_UPLOAD_DELIVERABLE);
        intent.putExtra(PARAM_ID, workorderId);
        intent.putExtra(PARAM_UPLOAD_SLOT_ID, uploadSlotId);
        intent.putExtra(PARAM_LOCAL_PATH, filePath);
        intent.putExtra(PARAM_FILE_NAME, filename);
        context.startService(intent);
    }

    public static void requestUploadDeliverable(final Context context, final long workorderId, final long uploadSlotId, Intent data) {
        FileHelper.getFileFromActivityResult(context, data, new FileHelper.Listener() {
            @Override
            public void fileReady(String filename, File file) {
                requestUploadDeliverable(context, workorderId, uploadSlotId, filename, file.getPath());
            }

            @Override
            public void fail(String reason) {
                Log.v("WorkorderDataClient.requestUploadDeliverable", reason);
            }
        });
    }

    public static void requestDeleteDeliverable(Context context, long workorderId, long workorderUploadId, String filename) {
        WorkorderTransactionBuilder.deleteDeliverable(context, workorderId, workorderUploadId, filename);
    }

    public static void requestGetDeliverable(Context context, long workorderId, long deliverableId, boolean isSync) {
        Intent intent = new Intent(context, WorkorderDataService.class);
        intent.putExtra(PARAM_ACTION, PARAM_ACTION_DELIVERABLE);
        intent.putExtra(PARAM_ID, workorderId);
        intent.putExtra(PARAM_DELIVERABLE_ID, deliverableId);
        intent.putExtra(PARAM_IS_SYNC, isSync);
        context.startService(intent);
    }

    public static void requestDownloadDeliverable(Context context, long workorderId, long deliverableId, String url, boolean isSync) {
        Intent intent = new Intent(context, WorkorderDataService.class);
        intent.putExtra(PARAM_ACTION, PARAM_ACTION_DOWNLOAD_DELIVERABLE);
        intent.putExtra(PARAM_ID, workorderId);
        intent.putExtra(PARAM_DELIVERABLE_ID, deliverableId);
        intent.putExtra(PARAM_URL, url);
        intent.putExtra(PARAM_IS_SYNC, isSync);
        context.startService(intent);
    }

    public boolean registerDeliverableList(boolean isSync) {
        if (!isConnected())
            return false;

        Log.v(TAG, "registerDeliverableList");

        return register(PARAM_ACTION_DELIVERABLE_LIST
                + (isSync ? "-SYNC" : ""), TAG);
    }


    public static void requestDeliverableList(Context context, long workorderId, boolean isSync) {
        Intent intent = new Intent(context, WorkorderDataService.class);
        intent.putExtra(PARAM_ACTION, PARAM_ACTION_DELIVERABLE_LIST);
        intent.putExtra(PARAM_ID, workorderId);
        intent.putExtra(PARAM_IS_SYNC, isSync);
        context.startService(intent);
    }

    /*-**********************************-*/
    /*-             Listener             -*/
    /*-**********************************-*/
    public static abstract class Listener extends TopicClient.Listener {
        @Override
        public void onEvent(String topicId, Parcelable payload) {
            if (topicId.startsWith(PARAM_ACTION_LIST)) {
                preOnWorkorderList((Bundle) payload);
            } else if (topicId.startsWith(PARAM_ACTION_DETAILS)) {
                preOnDetails((Bundle) payload);
            } else if (topicId.startsWith(PARAM_ACTION_GET_SIGNATURE)) {
                preOnGetSignature((Bundle) payload);
            } else if (topicId.startsWith(PARAM_ACTION_GET_BUNDLE)) {
                preOnGetBundle((Bundle) payload);
            } else if (topicId.startsWith(PARAM_ACTION_CHECKIN)) {
                preCheckIn((Bundle) payload);
            } else if (topicId.startsWith(PARAM_ACTION_CHECKOUT)) {
                preCheckOut((Bundle) payload);
            } else if (topicId.startsWith(PARAM_ACTION_DELIVERABLE_LIST)) {
                preDeliverableList((Bundle) payload);
            }
        }

        private void preDeliverableList(Bundle payload) {
            new AsyncTaskEx<Object, Object, List<Deliverable>>() {
                private long workorderId;

                @Override
                protected List<Deliverable> doInBackground(Object... params) {
                    Bundle payload = (Bundle) params[0];

                    workorderId = payload.getLong(PARAM_ID);
                    JsonArray ja = payload.getParcelable(PARAM_DATA_PARCELABLE);

                    List<Deliverable> list = new LinkedList<>();
                    for (int i = 0; i < ja.size(); i++) {
                        list.add(Deliverable.fromJson(ja.getJsonObject(i)));
                    }

                    return list;
                }

                @Override
                protected void onPostExecute(List<Deliverable> o) {
                    onDeliverableList(o, workorderId);
                }
            }.executeEx(payload);

        }

        public void onDeliverableList(List<Deliverable> list, long workorderId) {
        }

        private void preCheckOut(Bundle payload) {
            onCheckOut(payload.getLong(PARAM_ID));
        }

        public void onCheckOut(long WorkorderId) {
        }

        private void preCheckIn(Bundle payload) {
            onCheckIn(payload.getLong(PARAM_ID));
        }

        public void onCheckIn(long WorkorderId) {
        }

        // list
        protected void preOnWorkorderList(Bundle payload) {
            new AsyncTaskEx<Bundle, Object, List<Workorder>>() {
                private WorkorderDataSelector selector;
                private int page;

                @Override
                protected List<Workorder> doInBackground(Bundle... params) {
                    Bundle bundle = params[0];
                    try {
                        selector = WorkorderDataSelector.fromCall(bundle.getString(PARAM_LIST_SELECTOR));
                        Log.v(STAG, "Selector " + bundle.getString(PARAM_LIST_SELECTOR));
                        page = bundle.getInt(PARAM_PAGE);
                        List<Workorder> list = new LinkedList<>();
                        JsonArray ja = bundle.getParcelable(PARAM_DATA_PARCELABLE);
                        for (int i = 0; i < ja.size(); i++) {
                            list.add(Workorder.fromJson(ja.getJsonObject(i)));
                        }
                        return list;
                    } catch (Exception ex) {
//                        Log.v(STAG, selector.name());
                        ex.printStackTrace();
                    }
                    return null;
                }

                @Override
                protected void onPostExecute(List<Workorder> workorders) {
                    onWorkorderList(workorders, selector, page);
                }
            }.executeEx(payload);
        }

        public void onWorkorderList(List<Workorder> list, WorkorderDataSelector selector, int page) {
        }

        // details
        protected void preOnDetails(Bundle payload) {
            new AsyncTaskEx<Bundle, Object, Workorder>() {
                @Override
                protected Workorder doInBackground(Bundle... params) {
                    Bundle bundle = params[0];
                    try {
                        return Workorder.fromJson((JsonObject) bundle.getParcelable(PARAM_DATA_PARCELABLE));
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                    return null;
                }

                @Override
                protected void onPostExecute(Workorder workorder) {
                    if (workorder.getTransfer() != null)
                        Log.v(STAG, workorder.getTransfer().toJson().display());
                    else
                        Log.v(STAG, "no _proc");

                    onDetails(workorder);
                }
            }.executeEx(payload);
        }

        public void onDetails(Workorder workorder) {
        }

        // get signature
        private void preOnGetSignature(Bundle payload) {
            new AsyncTaskEx<Bundle, Object, Signature>() {

                @Override
                protected Signature doInBackground(Bundle... params) {
                    Bundle bundle = params[0];
                    try {
                        return Signature.fromJson((JsonObject) bundle.getParcelable(PARAM_DATA_PARCELABLE));
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                    return null;
                }

                @Override
                protected void onPostExecute(Signature signature) {
                    onGetSignature(signature);
                }
            }.executeEx(payload);
        }

        public void onGetSignature(Signature signature) {
        }

        private void preOnGetBundle(Bundle payload) {
            new AsyncTaskEx<Bundle, Object, com.fieldnation.data.workorder.Bundle>() {
                @Override
                protected com.fieldnation.data.workorder.Bundle doInBackground(Bundle... params) {
                    Bundle bundle = params[0];
                    try {
                        return com.fieldnation.data.workorder.Bundle.fromJson((JsonObject) bundle.getParcelable(PARAM_DATA_PARCELABLE));
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                    return null;
                }

                @Override
                protected void onPostExecute(com.fieldnation.data.workorder.Bundle bundle) {
                    onGetBundle(bundle);
                }
            }.executeEx(payload);
        }

        public void onGetBundle(com.fieldnation.data.workorder.Bundle bundle) {
        }
    }
}
