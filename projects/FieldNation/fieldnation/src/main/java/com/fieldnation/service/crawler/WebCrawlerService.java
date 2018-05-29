package com.fieldnation.service.crawler;

import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;

import com.fieldnation.App;
import com.fieldnation.AppMessagingClient;
import com.fieldnation.BuildConfig;
import com.fieldnation.NotificationDef;
import com.fieldnation.R;
import com.fieldnation.analytics.trackers.UUIDGroup;
import com.fieldnation.data.profile.Message;
import com.fieldnation.data.profile.Notification;
import com.fieldnation.data.profile.Profile;
import com.fieldnation.fnjson.JsonObject;
import com.fieldnation.fnlog.Log;
import com.fieldnation.fntools.Stopwatch;
import com.fieldnation.fntools.misc;
import com.fieldnation.service.auth.AuthClient;
import com.fieldnation.service.data.documents.DocumentClient;
import com.fieldnation.service.data.documents.DocumentConstants;
import com.fieldnation.service.data.photo.PhotoClient;
import com.fieldnation.service.data.profile.ProfileClient;
import com.fieldnation.service.transaction.WebTransaction;
import com.fieldnation.v2.data.client.BundlesWebApi;
import com.fieldnation.v2.data.client.GetWorkOrdersOptions;
import com.fieldnation.v2.data.client.WorkordersWebApi;
import com.fieldnation.v2.data.listener.TransactionParams;
import com.fieldnation.v2.data.model.Attachment;
import com.fieldnation.v2.data.model.AttachmentFolder;
import com.fieldnation.v2.data.model.AttachmentFolders;
import com.fieldnation.v2.data.model.ListEnvelope;
import com.fieldnation.v2.data.model.SavedList;
import com.fieldnation.v2.data.model.WorkOrder;
import com.fieldnation.v2.data.model.WorkOrders;

import java.io.File;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

/**
 * Created by Michael Carver on 4/21/2015.
 */
public class WebCrawlerService extends Service {
    private static final String TAG = "WebCrawlerService";

    public final static String UPDATE_OFFLINE_MODE = "UPDATE_OFFLINE_MODE ";
    public final static String BROADCAST_ACTION = "BROADCAST_ACTION ";
    public final static String TOTAL_ASSIGNED_WOS = "TOTAL_ASSIGNED_WOS";
    public final static String TOTAL_LEFT_DOWNLOADING = "TOTAL_LEFT_DOWNLOADING";
    private final int NO_VALUE_ASSIGNED = Integer.MIN_VALUE;
    private Intent intent = new Intent(BROADCAST_ACTION);


    private final Object LOCK = new Object();
    private final HashSet<String> _uploadingFiles = new HashSet<>();


    /**
     * When set to true, this will force the web crawler to run on startup. Only possible when
     * running a debug build.
     */
    private static final boolean FORCE_RUN = false && BuildConfig.DEBUG;
    /**
     * When set to true, will force the web crawler to only download the first page of assigned work.
     * Only possible when running a debug build.
     */
    private static final boolean LIMIT_ONE_PAGE = false && BuildConfig.DEBUG;

    private Handler _activityHandler;

    private boolean _haveProfile = false;
    private long _lastRequestTime;
    private long _requestCounter = 0;
    private boolean _isRunning = false;
    private boolean _monitorRunning = false;
    private int _pendingRequests = 0;
    private static int NOTIFICATION_ID = App.secureRandom.nextInt();
    // sometimes getWorkOrder list api call happens and thus value gets reset
    private int _totalAssignedWOs = NO_VALUE_ASSIGNED;
    private int _totalLeftDownloading = NO_VALUE_ASSIGNED;

    /**
     * We are tracking download progress using the following
     */

    private HashSet<DownloadTuple> downloads = new HashSet<>();

    public class DownloadTuple {
        private int workOrderId = 0;
        private int totalAttachments = 0;
        private int countCompletedDownloading = 0;
        private HashSet<Integer> documentIdList = new HashSet<>();

        public DownloadTuple(int workOrderId) {
            this.workOrderId = workOrderId;
        }

    }

    public WebCrawlerService() {
        super();
        Log.v(TAG, "WebCrawlerService");
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.v(TAG, "onCreate");
        initNotification();
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.v(TAG, "onStartCommand");

        SharedPreferences settings = getSharedPreferences(getPackageName() + "_preferences",
                Context.MODE_MULTI_PROCESS | Context.MODE_PRIVATE);

        if (App.get().getAuth() == null) {
            Log.v(TAG, "not logged in, quiting");
            return START_NOT_STICKY;
        }

        boolean forceRun = FORCE_RUN;

        if (intent != null && intent.hasExtra("force")) {
            forceRun = true;
        }

        // we're not allowed to run, stop
        if (/*!settings.getBoolean(getString(R.string.pref_key_sync_enabled), false) && */ !forceRun
                && App.get().getOfflineState() != App.OfflineState.DOWNLOADING) {
            Log.v(TAG, "sync disabled, quiting");
            startActivityMonitor();
            return START_NOT_STICKY;
        }

        // schedule if sync is enabled
//        if (settings.getBoolean(getString(R.string.pref_key_sync_enabled), false))
//            scheduleNext();

        // if already running, then return
        if (_isRunning) {
            Log.v(TAG, "already running, stopping");
            return START_STICKY;
        }


        if (forceRun || App.get().getOfflineState() == App.OfflineState.DOWNLOADING) {
            Log.v(TAG, "force run/ offline mode enabled, running");

            startNotification();
            runCrawler();

            return START_STICKY;
        }

        // if not running then
        if (intent != null && intent.hasExtra("IS_ALARM") || forceRun) {
            Log.v(TAG, "alarm triggered");

            startNotification();
            runCrawler();

            return START_STICKY;
        }

        Log.v(TAG, "Do nothing");
        startActivityMonitor();
        return START_STICKY;
    }

    private void initNotification() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            android.app.Notification.Builder builder = new android.app.Notification.Builder(App.get(), NotificationDef.OTHER_CHANNEL);
            builder.setLargeIcon((Bitmap) null);
            builder.setSmallIcon(R.drawable.ic_notif_queued);
            builder.setContentTitle("Running background sync..");
            builder.setOnlyAlertOnce(true);
            builder.setCategory(android.app.Notification.CATEGORY_PROGRESS);
            builder.setOngoing(true);

            android.app.Notification notification = builder.build();
            NotificationManager manager = (NotificationManager) App.get().getSystemService(Service.NOTIFICATION_SERVICE);
            manager.notify(NOTIFICATION_ID, notification);
            startForeground(NOTIFICATION_ID, notification);
        }
    }

    private void startNotification() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            android.app.Notification.Builder builder = new android.app.Notification.Builder(App.get(), NotificationDef.OTHER_CHANNEL);
            builder.setLargeIcon((Bitmap) null);
            builder.setSmallIcon(R.drawable.ic_notif_queued);
            builder.setContentTitle("Running background sync..");
            builder.setOnlyAlertOnce(true);
            builder.setCategory(android.app.Notification.CATEGORY_PROGRESS);
            builder.setOngoing(true);

            android.app.Notification notification = builder.build();
            NotificationManager manager = (NotificationManager) App.get().getSystemService(Service.NOTIFICATION_SERVICE);
            manager.notify(NOTIFICATION_ID, notification);
            startForeground(NOTIFICATION_ID, notification);
        } else {
            NotificationCompat.Builder builder = new NotificationCompat.Builder(this);
            builder.setLargeIcon((Bitmap) null);
            builder.setSmallIcon(R.drawable.ic_notif_queued);
            builder.setContentTitle("Running background sync..");
            builder.setOnlyAlertOnce(true);
            builder.setCategory(android.app.Notification.CATEGORY_PROGRESS);
            builder.setOngoing(true);

            NotificationManager manager = (NotificationManager) App.get().getSystemService(Service.NOTIFICATION_SERVICE);
            android.app.Notification notification = builder.build();
            manager.notify(NOTIFICATION_ID, notification);
            startForeground(NOTIFICATION_ID, notification);
        }
    }

    private void completeNotification() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            android.app.Notification.Builder builder = new android.app.Notification.Builder(App.get(), NotificationDef.OTHER_CHANNEL);
            builder.setLargeIcon((Bitmap) null);
            builder.setSmallIcon(R.drawable.ic_notif_queued);
            builder.setContentTitle("Background sync complete");
            builder.setOnlyAlertOnce(true);
            builder.setCategory(android.app.Notification.CATEGORY_PROGRESS);
            builder.setOngoing(true);

            android.app.Notification notification = builder.build();
            NotificationManager manager = (NotificationManager) App.get().getSystemService(Service.NOTIFICATION_SERVICE);
            manager.notify(NOTIFICATION_ID, notification);
        } else {
            NotificationCompat.Builder builder = new NotificationCompat.Builder(this);
            builder.setLargeIcon((Bitmap) null);
            builder.setSmallIcon(R.drawable.ic_notif_queued);
            builder.setContentTitle("Background sync complete");
            builder.setOnlyAlertOnce(true);
            builder.setCategory(android.app.Notification.CATEGORY_PROGRESS);
            builder.setOngoing(true);

            NotificationManager manager = (NotificationManager) App.get().getSystemService(Service.NOTIFICATION_SERVICE);
            android.app.Notification notification = builder.build();
            manager.notify(NOTIFICATION_ID, notification);
        }
    }

    private void cancelNotification() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N)
            stopForeground(STOP_FOREGROUND_REMOVE);
        else {
            NotificationManager manager = (NotificationManager) App.get().getSystemService(Service.NOTIFICATION_SERVICE);
            manager.cancel(NOTIFICATION_ID);
        }
    }

    private synchronized void incrementPendingRequestCounter(int val) {
        _pendingRequests += val;
/*
        if (_pendingRequests % 5 == 0)
            Log.v(TAG, "_pendingRequests = " + _pendingRequests);
*/
    }

    private synchronized void incRequestCounter(int val) {
        _lastRequestTime = System.currentTimeMillis();
        _requestCounter += val;
/*
        if (_requestCounter % 5 == 0)
            Log.v(TAG, "_requestCounter = " + _requestCounter);
*/
    }

    // TODO send the update of offline mode to App.java or can be configurable to any activity we want
    private synchronized void broadcastDownloadProgress() {
        Log.v(TAG, "broadcastDownloadProgress");

        Bundle bundle = new Bundle();
        bundle.putInt(TOTAL_ASSIGNED_WOS, _totalAssignedWOs);
        bundle.putInt(TOTAL_LEFT_DOWNLOADING, _totalLeftDownloading);

        intent.putExtra(UPDATE_OFFLINE_MODE, bundle);
        sendBroadcast(intent);
    }

    private void startActivityMonitor() {
        if (_activityHandler == null)
            _activityHandler = new Handler();

        if (!_monitorRunning) {
            _monitorRunning = true;
            _activityHandler.postDelayed(_activityMonitor_runnable, 10000);
        }
    }

    private final Runnable _activityMonitor_runnable = new Runnable() {
        @Override
        public void run() {
            _monitorRunning = false;

            if (!_isRunning) {
                Log.v(TAG, "_activityMonitor_runnable not running");
                return;
            }

            // check timer
            if (System.currentTimeMillis() - _lastRequestTime > 10000
                    && WebTransaction.getPaused(WebTransaction.Type.CRAWLER).size() == 0) {

                // shutdown
                Log.v(TAG, "activity killMe");
                killMe();
            } else {
                startActivityMonitor();
            }
        }
    };

    @Override
    public void onDestroy() {
        Log.v(TAG, "onDestroy");
        _workOrdersApi.unsub(true);
        _profileClient.unsubListMessages(true);
        _profileClient.unsubListNotifications(true);
        _profileClient.unsubGet(true);
        _documentClient.unsub();

        _bundlesApi.unsub();
        _authClient.unsubRemoveCommand();
        _isRunning = false;
        cancelNotification();

        Log.v(TAG, "onDestroy " + _crawlerWatch.finish());
        super.onDestroy();
    }

    private void killMe() {
        AppMessagingClient.setOfflineMode(App.OfflineState.OFFLINE);
        stopSelf();
    }

//    private void scheduleNext() {
//        Log.v(TAG, "scheduleNext");
//        SharedPreferences settings = getSharedPreferences(getPackageName() + "_preferences",
//                Context.MODE_MULTI_PROCESS | Context.MODE_PRIVATE);
//
//        // if clock is not set, set it
//        long runTime = settings.getLong(getString(R.string.pref_key_sync_start_time), 180);
//
//        Calendar cal = Calendar.getInstance();
//        if (BuildConfig.DEBUG) {
//            cal.set(Calendar.HOUR_OF_DAY, (int) (runTime / 60));
//            cal.set(Calendar.MINUTE, (int) (runTime % 60));
//        } else {
//            Random random = new Random();
//            cal.set(Calendar.HOUR_OF_DAY, (int) (runTime / 60) + random.nextInt(1)); // add 0-1 hour
//            cal.set(Calendar.MINUTE, (int) (runTime % 60) + random.nextInt(60)); // add 0-60 min
//        }
//
//        long nextTime = cal.getTimeInMillis();
//        if (nextTime < System.currentTimeMillis()) {
//            nextTime += 86400000;
//        }
//
//        Log.v(TAG, "register sync alarm " + ISO8601.fromUTC(nextTime));
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//            CrawlerJobService.schedule(this, nextTime);
//        } else {
//            AlarmBroadcastReceiver.registerCrawlerAlarm(this, nextTime);
//        }
//    }

    private synchronized void updateDownloadProgress() {
        Log.v(TAG, "updateDownloadProgress");
        --_totalLeftDownloading;
        broadcastDownloadProgress();

        if (_totalLeftDownloading == 0 && WebTransaction.getPaused(WebTransaction.Type.CRAWLER).size() == 0) {
            Log.v(TAG, "updateDownloadProgress kill");
            killMe();
        }
    }

    private boolean isDownloadable(Attachment attachment) {
        // We cannot download the link, printable badge. So ignoring.
        return attachment.getFile() != null && attachment.getFile().getType() == com.fieldnation.v2.data.model.File.TypeEnum.FILE;
    }

    private Stopwatch _crawlerWatch = new Stopwatch();

    public void runCrawler() {
        Log.v(TAG, "runCrawler");

        if (_isRunning) {
            Log.v(TAG, "crawler skipping");
            return;
        }

        _crawlerWatch.start();

        _lastRequestTime = System.currentTimeMillis();
        _isRunning = true;

        startActivityMonitor();

        Log.v(TAG, "_profileClient_listener.onConnected");
        _authClient.subRemoveCommand();
        _profileClient.subListMessages(true);
        _profileClient.subListNotifications(true);
        _profileClient.subGet(true);

        incrementPendingRequestCounter(3);
        incRequestCounter(3);
        _haveProfile = false;
        ProfileClient.get(WebCrawlerService.this, 0, true, false);
        ProfileClient.listMessages(WebCrawlerService.this, 0, true, false);
        ProfileClient.listNotifications(WebCrawlerService.this, 0, true, false);

        _bundlesApi.sub(true);

        _workOrdersApi.sub(true);
        _documentClient.sub();
        incrementPendingRequestCounter(1);
        incRequestCounter(1);
        WorkordersWebApi.getWorkOrderLists(WebCrawlerService.this, false, WebTransaction.Type.CRAWLER);
    }

    private final AuthClient _authClient = new AuthClient() {
        @Override
        public void onCommandRemove() {
            Log.v(TAG, "onCommandRemove");
            _workOrdersApi.unsub(true);
            _profileClient.unsubListMessages(true);
            _profileClient.unsubListNotifications(true);
            _profileClient.unsubGet(true);
            _bundlesApi.unsub(true);

            cancelNotification();

            try {
                List<WebTransaction> list = WebTransaction.getSyncing();
                for (WebTransaction webTransaction : list) {
                    WebTransaction.delete(webTransaction.getId());
                }
            } catch (Exception ex) {
                Log.v(TAG, ex);
            }
        }
    };

    private final WorkordersWebApi _workOrdersApi = new WorkordersWebApi() {
        @Override
        public boolean processTransaction(UUIDGroup uuidGroup, TransactionParams transactionParams, String methodName) {
            return methodName.equals("getWorkOrderLists")
                    || methodName.equals("getWorkOrders")
                    || methodName.equals("getWorkOrder")
                    || methodName.equals("getAttachments");
        }

        @Override
        public boolean onComplete(UUIDGroup uuidGroup, TransactionParams transactionParams, String methodName, Object successObject, boolean success, Object failObject, boolean isCached) {
            if (App.get().isDiskFull()) {
                Log.v(TAG, "WorkordersWebApi lowDiskSpace");
                AppMessagingClient.lowDiskSpace();
                AppMessagingClient.setOfflineMode(App.OfflineState.NORMAL);
                _workOrdersApi.unsub(true);
                _documentClient.unsub();
                stopSelf();
            }

            try {
                Log.v(TAG, "onComplete " + methodName);
                if (successObject != null && methodName.equals("getWorkOrderLists")) {
                    Log.v(TAG, "getWorkOrderLists");

                    SavedList[] savedList = (SavedList[]) successObject;
                    incrementPendingRequestCounter(-1);

                    for (SavedList list : savedList) {
                        incrementPendingRequestCounter(1);
                        incRequestCounter(1);
                        // only run on assigned work
                        if (list.getId().equals("workorders_assignments"))
                            WorkordersWebApi.getWorkOrders(WebCrawlerService.this, new GetWorkOrdersOptions().list(list.getId()).page(1), false, false, WebTransaction.Type.CRAWLER);
                    }

                } else if (successObject != null && methodName.equals("getWorkOrders")) {
                    WorkOrders workOrders = (WorkOrders) successObject;
                    boolean isFlightBoard = false;
                    try {
                        JsonObject options = new JsonObject(transactionParams.methodParams);
                        if (options.has("getWorkOrdersOptions.fFlightboardTomorrow") && options.getBoolean("getWorkOrdersOptions.fFlightboardTomorrow"))
                            isFlightBoard = true;
                    } catch (Exception ex) {
                        Log.v(TAG, ex);
                    }

                    if (isFlightBoard)
                        return super.onComplete(uuidGroup, transactionParams, methodName, successObject, success, failObject, isCached);

                    if (workOrders.getMetadata() == null)
                        return super.onComplete(uuidGroup, transactionParams, methodName, successObject, success, failObject, isCached);

                    Log.v(TAG, "getWorkOrders " + workOrders.getMetadata().getList() + ", " + workOrders.getMetadata().getPage());
                    incrementPendingRequestCounter(-1);

                    if (!workOrders.getMetadata().getList().equals("workorders_assignments")) {
                        Log.v(TAG, "!!!!!! Not assigned work !!!!!!");
                        return super.onComplete(uuidGroup, transactionParams, methodName, successObject, success, failObject, isCached);
                    }

                    // request the other lists
                    ListEnvelope metadata = workOrders.getMetadata();
                    if (metadata.getPage() == 1 && !LIMIT_ONE_PAGE) {
                        for (int i = 2; i <= workOrders.getMetadata().getPages(); i++) {
                            incrementPendingRequestCounter(1);
                            incRequestCounter(1);
                            WorkordersWebApi.getWorkOrders(WebCrawlerService.this, new GetWorkOrdersOptions().list(workOrders.getMetadata().getList()).page(i), true, false, WebTransaction.Type.CRAWLER);
                        }
                    }

                    // get the details
                    WorkOrder[] works = workOrders.getResults();
                    if (_totalAssignedWOs == NO_VALUE_ASSIGNED)
                        _totalAssignedWOs = _totalLeftDownloading = workOrders.getMetadata().getTotal();

                    for (WorkOrder workOrder : works) {
                        incrementPendingRequestCounter(1);
                        incRequestCounter(1);
                        WorkordersWebApi.getWorkOrder(WebCrawlerService.this, workOrder.getId(), false, WebTransaction.Type.CRAWLER);
                    }

                } else if (successObject != null && methodName.equals("getWorkOrder")) {
                    WorkOrder workOrder = (WorkOrder) successObject;

                    // download progress tracking
                    DownloadTuple tuple = new DownloadTuple(workOrder.getId());
                    AttachmentFolder[] attachmentFolders = workOrder.getAttachments().getResults();
                    for (AttachmentFolder attachmentFolder : attachmentFolders) {
                        for (Attachment attachment : attachmentFolder.getResults()) {
                            if (isDownloadable(attachment)) {
                                tuple.documentIdList.add(attachment.getId());
                                tuple.totalAttachments += 1;
                            }
                        }
                    }

                    downloads.add(tuple);

                    if (workOrder.getId() == null)
                        return super.onComplete(uuidGroup, transactionParams, methodName, successObject, success, failObject, isCached);

                    Log.v(TAG, "getWorkOrder " + workOrder.getId());
                    incrementPendingRequestCounter(-1);

                    incrementPendingRequestCounter(1);
                    incRequestCounter(1);
                    WorkordersWebApi.getAttachments(WebCrawlerService.this, workOrder.getId(), false, WebTransaction.Type.CRAWLER);

                    if (workOrder.isBundle()) {
                        incrementPendingRequestCounter(1);
                        incRequestCounter(1);
                        BundlesWebApi.getBundleWorkOrders(WebCrawlerService.this, workOrder.getBundle().getId(), false, WebTransaction.Type.CRAWLER);
                    }

                    incRequestCounter(1);
                    WorkordersWebApi.getMessages(WebCrawlerService.this, workOrder.getId(), false, WebTransaction.Type.CRAWLER);
                    incRequestCounter(1);
                    WorkordersWebApi.getBonuses(WebCrawlerService.this, workOrder.getId(), false, WebTransaction.Type.CRAWLER);
                    incRequestCounter(1);
                    WorkordersWebApi.getCustomFields(WebCrawlerService.this, workOrder.getId(), false, WebTransaction.Type.CRAWLER);
                    incRequestCounter(1);
                    WorkordersWebApi.getDiscounts(WebCrawlerService.this, workOrder.getId(), false, WebTransaction.Type.CRAWLER);
                    incRequestCounter(1);
                    WorkordersWebApi.getPay(WebCrawlerService.this, workOrder.getId(), false, WebTransaction.Type.CRAWLER);
                    incRequestCounter(1);
                    WorkordersWebApi.getExpenses(WebCrawlerService.this, workOrder.getId(), false, WebTransaction.Type.CRAWLER);
                    incRequestCounter(1);
                    WorkordersWebApi.getPenalties(WebCrawlerService.this, workOrder.getId(), false, WebTransaction.Type.CRAWLER);
                    incRequestCounter(1);
                    WorkordersWebApi.getQualifications(WebCrawlerService.this, workOrder.getId(), false, WebTransaction.Type.CRAWLER);
                    incRequestCounter(1);
                    WorkordersWebApi.getSignatures(WebCrawlerService.this, workOrder.getId(), false, WebTransaction.Type.CRAWLER);

                } else if (successObject != null && methodName.equals("getAttachments")) {
                    int downloadable = 0;
                    incrementPendingRequestCounter(-1);
                    // get attachments
                    AttachmentFolders folders = (AttachmentFolders) successObject;
//                    int workOrderId = transactionParams.getMethodParamInt("workOrderId"); // Used for debugging purposes
                    if (folders.getResults().length > 0) {
                        for (AttachmentFolder folder : folders.getResults()) {
                            Attachment[] attachments = folder.getResults();
                            if (attachments.length > 0) {
                                for (Attachment attachment : attachments) {
                                    if (isDownloadable(attachment)) {
//                                        if (workOrderId == 2105) { // Used for debugging purposes, deletes all the attachments on a work order
//                                            WorkordersWebApi.deleteAttachment(WebCrawlerService.this, workOrderId, attachment.getFolderId(), attachment, null);
//                                        } else {
                                        incRequestCounter(1);
                                        DocumentClient.downloadDocument(WebCrawlerService.this, attachment.getId(), attachment.getFile().getLink(), attachment.getFile().getName(), true);
                                        downloadable++;
//                                        }
                                    }
                                }
                            }
                        }
                    }
                    if (downloadable == 0 && downloads != null && downloads.size() > 0) {
                        int workOrderId = transactionParams.getMethodParamInt("workOrderId");
                        Iterator<DownloadTuple> itr = downloads.iterator();
                        while (itr.hasNext()) {
                            DownloadTuple dt = itr.next();
                            if (dt.workOrderId == workOrderId) {
                                updateDownloadProgress();
                            }
                        }
                    }
                }
            } catch (Exception ex) {
                Log.v(TAG, ex);
            }
            return super.onComplete(uuidGroup, transactionParams, methodName, successObject, success, failObject, isCached);
        }
    };

    private final ProfileClient _profileClient = new ProfileClient() {
        @Override
        public void onGet(Profile profile, boolean failed) {
            Log.v(TAG, "ProfileClient.onGet " + _haveProfile);
            if (!_haveProfile) {

                incrementPendingRequestCounter(-1);

                if (failed)
                    return;

                Log.v(TAG, "ProfileClient.onGet");
                incRequestCounter(2);
                PhotoClient.get(WebCrawlerService.this, profile.getPhoto().getLarge(), true, true);
                PhotoClient.get(WebCrawlerService.this, profile.getPhoto().getThumb(), true, true);
                _haveProfile = true;
            }
        }

        @Override
        public void onMessageList(List<Message> list, int page, boolean failed, boolean isCached) {
            Log.v(TAG, "ProfileClient.onMessageList");

            incrementPendingRequestCounter(-1);
            if (list == null || list.size() == 0 || failed) {
                return;
            }
            Log.v(TAG, "onMessageList(" + list.size() + "," + page + ")");

            incrementPendingRequestCounter(1);
            incRequestCounter(1);
            ProfileClient.listMessages(WebCrawlerService.this, page + 1, true, false);

            for (int i = 0; i < list.size(); i++) {
                Message message = list.get(i);
                if (message.getFromUser() != null) {
                    if (!misc.isEmptyOrNull(message.getFromUser().getPhotoUrl())) {
                        incRequestCounter(1);
                        PhotoClient.get(WebCrawlerService.this, message.getFromUser().getPhotoUrl(), true, true);
                    } else if (!misc.isEmptyOrNull(message.getFromUser().getPhotoThumbUrl())) {
                        incRequestCounter(1);
                        PhotoClient.get(WebCrawlerService.this, message.getFromUser().getPhotoThumbUrl(), true, true);
                    }
                }
            }
        }

        @Override
        public void onNotificationList(List<Notification> list, int page, boolean failed, boolean isCached) {
            Log.v(TAG, "onNotificationList");

            incrementPendingRequestCounter(-1);
            if (list == null || list.size() == 0 || failed) {
                return;
            }

            Log.v(TAG, "onNotificationList(" + list.size() + "," + page + ")");

            incrementPendingRequestCounter(1);
            incRequestCounter(1);
            ProfileClient.listNotifications(WebCrawlerService.this, page + 1, true, false);
        }
    };

    private final BundlesWebApi _bundlesApi = new BundlesWebApi() {
        @Override
        public boolean processTransaction(TransactionParams transactionParams, String methodName) {
            return methodName.equals("getBundleWorkOrders");
        }

        @Override
        public void onComplete(TransactionParams transactionParams, String methodName, Object successObject, boolean success, Object failObject) {
            if (methodName.equals("getBundleWorkOrders")) {
                incrementPendingRequestCounter(-1);
            }
        }
    };


    private final DocumentClient _documentClient = new DocumentClient() {
        @Override
        public boolean processDownload(int documentId) {
            return true;
        }

        @Override
        public void onDownload(int documentId, File file, int state, boolean isSync) {

            if (App.get().isDiskFull()) {
                Log.v(TAG, "DocumentClient lowDiskSpace");
                AppMessagingClient.lowDiskSpace();
                AppMessagingClient.setOfflineMode(App.OfflineState.NORMAL);
                _documentClient.unsub();
                _workOrdersApi.unsub(true);
                stopSelf();
            }

            if (file == null || state == DocumentConstants.PARAM_STATE_START) {
                Log.v(TAG, "Downloading not finished.");
                return;
            }

            if (documentId > 0) {
                if (downloads != null && downloads.size() > 0) {

                    Iterator<DownloadTuple> itr = downloads.iterator();
                    while (itr.hasNext()) {
                        DownloadTuple ob = itr.next();
                        if (ob.documentIdList.contains(documentId)) {
                            ++ob.countCompletedDownloading;

                            if (ob.countCompletedDownloading == ob.totalAttachments) {
                                updateDownloadProgress();
                            }
                            break;
                        }
                    }
                }
            }
        }
    };
}
