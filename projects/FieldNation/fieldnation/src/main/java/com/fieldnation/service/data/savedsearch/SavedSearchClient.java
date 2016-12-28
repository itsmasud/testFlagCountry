package com.fieldnation.service.data.savedsearch;

import android.os.Bundle;
import android.os.Parcelable;

import com.fieldnation.App;
import com.fieldnation.data.v2.SavedSearchParams;
import com.fieldnation.fnjson.JsonArray;
import com.fieldnation.fnjson.JsonObject;
import com.fieldnation.fnlog.Log;
import com.fieldnation.fnpigeon.Sticky;
import com.fieldnation.fnpigeon.TopicClient;
import com.fieldnation.fnstore.StoredObject;
import com.fieldnation.fntools.AsyncTaskEx;
import com.fieldnation.fntools.UniqueTag;
import com.fieldnation.service.data.v2.workorder.WorkOrderListType;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by mc on 12/21/16.
 */

public class SavedSearchClient extends TopicClient implements SavedSearchConstants {
    private static final String STAG = "SavedSearchClient";
    private final String TAG = UniqueTag.makeTag(STAG);

    private static final SavedSearchParams[] defaults;

    static {
        if (App.isNcns()) {
            defaults = new SavedSearchParams[]{
                    new SavedSearchParams(0)
                            .type(WorkOrderListType.TODAYS_WORK.getType())
                            .status(WorkOrderListType.TODAYS_WORK.getStatuses())
                            .title("Today's Work"),
                    new SavedSearchParams(1)
                            .type(WorkOrderListType.TOMORROWS_WORK.getType())
                            .status(WorkOrderListType.TOMORROWS_WORK.getStatuses())
                            .title("Tomorrow's Work"),
                    new SavedSearchParams(2)
                            .type(WorkOrderListType.ASSIGNED.getType())
                            .status(WorkOrderListType.ASSIGNED.getStatuses())
                            .title("Assigned"),
                    new SavedSearchParams(3)
                            .type(WorkOrderListType.AVAILABLE.getType())
                            .status(WorkOrderListType.AVAILABLE.getStatuses())
                            .canEdit(true)
                            .title("Available"),
                    new SavedSearchParams(4)
                            .type(WorkOrderListType.CANCELED.getType())
                            .status(WorkOrderListType.CANCELED.getStatuses())
                            .order("desc")
                            .title("Canceled"),
                    new SavedSearchParams(5)
                            .type(WorkOrderListType.COMPLETED.getType())
                            .status(WorkOrderListType.COMPLETED.getStatuses())
                            .order("desc")
                            .title("Completed"),
                    new SavedSearchParams(6)
                            .type(WorkOrderListType.REQUESTED.getType())
                            .status(WorkOrderListType.REQUESTED.getStatuses())
                            .canEdit(true)
                            .title("Requested"),
                    new SavedSearchParams(7)
                            .canEdit(true)
                            .type(WorkOrderListType.ROUTED.getType())
                            .status(WorkOrderListType.ROUTED.getStatuses())
                            .title("Routed")
            };
        } else {
            defaults = new SavedSearchParams[]{
                    new SavedSearchParams(0)
                            .type(WorkOrderListType.ASSIGNED.getType())
                            .status(WorkOrderListType.ASSIGNED.getStatuses())
                            .title("Assigned"),
                    new SavedSearchParams(1)
                            .type(WorkOrderListType.AVAILABLE.getType())
                            .status(WorkOrderListType.AVAILABLE.getStatuses())
                            .canEdit(true)
                            .title("Available"),
                    new SavedSearchParams(2)
                            .type(WorkOrderListType.CANCELED.getType())
                            .status(WorkOrderListType.CANCELED.getStatuses())
                            .order("desc")
                            .title("Canceled"),
                    new SavedSearchParams(3)
                            .type(WorkOrderListType.COMPLETED.getType())
                            .status(WorkOrderListType.COMPLETED.getStatuses())
                            .order("desc")
                            .title("Completed"),
                    new SavedSearchParams(4)
                            .type(WorkOrderListType.REQUESTED.getType())
                            .status(WorkOrderListType.REQUESTED.getStatuses())
                            .canEdit(true)
                            .title("Requested"),
                    new SavedSearchParams(5)
                            .type(WorkOrderListType.ROUTED.getType())
                            .status(WorkOrderListType.ROUTED.getStatuses())
                            .canEdit(true)
                            .title("Routed")
            };
        }
    }

    private static SavedSearchParams[] searchParams;

    /*-************************************-*/
    /*-             Life Cycle             -*/
    /*-************************************-*/
    public SavedSearchClient(Listener listener) {
        super(listener);
    }

    @Override
    public String getUserTag() {
        return TAG;
    }

    public static void list() {
        new AsyncTaskEx<Object, Object, Object>() {
            @Override
            protected Object doInBackground(Object... params) {
                StoredObject so = StoredObject.get(App.get(), App.getProfileId(), "SavedSearch", 0);

                if (so == null) {
                    // create defaults if none in database
                    searchParams = new SavedSearchParams[defaults.length];
                    JsonArray ja = new JsonArray();
                    for (int i = 0; i < defaults.length; i++) {
                        JsonObject obj = defaults[i].toJson();
                        searchParams[i] = SavedSearchParams.fromJson(obj);
                        ja.add(obj);
                    }
                    StoredObject.put(App.get(), App.getProfileId(), "SavedSearch", 0, ja.toByteArray());

                } else {
                    // loading from StoredObject
                    try {
                        JsonArray ja = new JsonArray(so.getData());
                        searchParams = new SavedSearchParams[ja.size()];

                        for (int i = 0; i < ja.size(); i++) {
                            searchParams[i] = SavedSearchParams.fromJson(ja.getJsonObject(i));
                        }
                    } catch (Exception ex) {
                        Log.v(STAG, ex);
                    }
                }

                // sending the data
                Bundle bundle = new Bundle();
                bundle.putParcelableArray("LIST", searchParams);
                dispatchEvent(App.get(), TOPIC_ID_LIST, bundle, Sticky.NONE);
                return null;
            }
        }.executeEx();
    }

    public boolean subList() {
        return register(TOPIC_ID_LIST);
    }

    public static void save(SavedSearchParams savedSearchParams) {
        new AsyncTaskEx<SavedSearchParams, Object, Object>() {
            @Override
            protected Object doInBackground(SavedSearchParams... params) {
                searchParams[params[0].id] = params[0];
                JsonArray ja = new JsonArray();
                for (int i = 0; i < searchParams.length; i++) {
                    ja.add(searchParams[i].toJson());
                }
                StoredObject.put(App.get(), App.getProfileId(), "SavedSearch", 0, ja.toByteArray());

                SavedSearchClient.list();

                Bundle bundle = new Bundle();
                bundle.putParcelable("SavedSearchParams", params[0]);
                dispatchEvent(App.get(), TOPIC_ID_SAVED, bundle, Sticky.NONE);
                return null;
            }
        }.executeEx(savedSearchParams);
    }

    public boolean subSaved() {
        return register(TOPIC_ID_SAVED);
    }

    /*-**********************************-*/
    /*-             Listener             -*/
    /*-**********************************-*/
    public static abstract class Listener extends TopicClient.Listener {
        @Override
        public void onEvent(String topicId, Parcelable payload) {
            if (topicId.startsWith(TOPIC_ID_LIST)) {
                new AsyncTaskEx<Bundle, Object, List<SavedSearchParams>>() {
                    @Override
                    protected List<SavedSearchParams> doInBackground(Bundle... params) {
                        List<SavedSearchParams> list = new LinkedList<>();

                        Parcelable[] parcels = params[0].getParcelableArray("LIST");
                        for (Parcelable parcel : parcels) {
                            try {
                                Log.v(STAG, parcel.getClass().getName());
                                list.add((SavedSearchParams) parcel);
                            } catch (Exception ex) {
                            }
                        }

                        return list;
                    }

                    @Override
                    protected void onPostExecute(List<SavedSearchParams> savedSearchParams) {
                        list(savedSearchParams);
                    }
                }.executeEx((Bundle) payload);
            } else if (topicId.startsWith(TOPIC_ID_SAVED)) {
                new AsyncTaskEx<Bundle, Object, SavedSearchParams>() {

                    @Override
                    protected SavedSearchParams doInBackground(Bundle... params) {
                        SavedSearchParams ssp = (SavedSearchParams) params[0].getParcelable("SavedSearchParams");
                        return ssp;
                    }

                    @Override
                    protected void onPostExecute(SavedSearchParams savedSearchParams) {
                        saved(savedSearchParams);
                    }
                }.executeEx((Bundle) payload);
            }
        }

        public void list(List<SavedSearchParams> savedSearchParams) {
        }

        public void saved(SavedSearchParams savedSearchParams) {
        }
    }
}
