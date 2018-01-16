package com.fieldnation.service.tracker;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.support.v4.app.NotificationCompat;

import com.fieldnation.App;
import com.fieldnation.NotificationDef;
import com.fieldnation.R;
import com.fieldnation.fnjson.JsonObject;
import com.fieldnation.fnlog.Log;
import com.fieldnation.fntools.misc;
import com.fieldnation.service.transaction.WebTransaction;
import com.fieldnation.ui.SplashActivity;
import com.fieldnation.v2.data.listener.TransactionParams;

import java.util.Enumeration;
import java.util.Hashtable;

/**
 * Created by mc on 2/20/17.
 */

public class UploadTrackerDeliverables implements UploadTrackerConstants, UploadTracker {
    private static final String TAG = "UploadTrackerDeliverables";

    private static class Tuple {
        String action;
        WebTransaction webTransaction;

        public Tuple(String action, WebTransaction webTransaction) {
            this.action = action;
            this.webTransaction = webTransaction;
        }
    }

    private static class WoNotif {
        public int _notificationId = App.secureRandom.nextInt();
        public int _workOrderId;
        public long _timeout;
        private boolean _isTiming = false;
        private Handler _handler = new Handler(Looper.getMainLooper());

        private Hashtable<String, Tuple> tuples = new Hashtable<>();

        private Runnable _timer = new Runnable() {
            @Override
            public void run() {
                _isTiming = false;
                updateNotification(App.get());
            }
        };

        public WoNotif(int workOrderId) {
            this._workOrderId = workOrderId;
        }

        public void updateNotification(String action, WebTransaction webTransaction) {
            Tuple tuple;
            if (tuples.containsKey(webTransaction.getUUID().uuid)) {
                tuple = tuples.get(webTransaction.getUUID().uuid);
                tuple.action = action;
                tuple.webTransaction = webTransaction;
            } else {
                tuple = new Tuple(action, webTransaction);
                tuples.put(webTransaction.getUUID().uuid, tuple);
            }
        }

        public void updateNotification(Context context) {
            Intent workorderIntent = SplashActivity.intentShowWorkOrder(App.get(), _workOrderId);
            PendingIntent pendingIntent = PendingIntent.getActivity(App.get(), App.secureRandom.nextInt(), workorderIntent, 0);

            int retries = 0;
            _timeout = System.currentTimeMillis();
            int failed = 0;
            int success = 0;
            int uploading = 0;
            int queued = 0;
            int total = 0;

            Enumeration<Tuple> ts = tuples.elements();
            while (ts.hasMoreElements()) {
                Tuple tuple = ts.nextElement();

                if (tuple.action.equals(ACTION_FAILED)) {
                    total++;
                    failed++;
                } else if (tuple.action.equals(ACTION_QUEUED)) {
                    total++;
                    queued++;
                } else if (tuple.action.equals(ACTION_RETRY)) {
                    total++;
                    retries++;
                    if (_timeout < tuple.webTransaction.getQueueTime())
                        _timeout = tuple.webTransaction.getQueueTime();
                } else if (tuple.action.equals(ACTION_STARTED)) {
                    total++;
                    uploading++;

                } else if (tuple.action.equals(ACTION_SUCCESS)) {
                    total++;
                    success++;
                }
            }

            NotificationManager manager = (NotificationManager) App.get().getSystemService(Service.NOTIFICATION_SERVICE);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                Notification.Builder builder = new Notification.Builder(App.get(), NotificationDef.FILE_UPLOAD_CHANNEL);
                builder.setLargeIcon((Bitmap) null);
                builder.setContentIntent(pendingIntent);

                //builder.setSmallIcon(R.drawable.ic_notif_queued);

                if (retries > 0) {
                    builder.setSmallIcon(R.drawable.ic_notif_queued);
                    long timeleft = _timeout - System.currentTimeMillis();
                    if (timeleft >= 0) {
                        builder.setContentTitle("WO" + _workOrderId + " | uploads failed");
                        builder.setContentText("Retry in " + misc.convertMSToDHMS(timeleft, true));
                        if (!_isTiming) {
                            _isTiming = true;
                            _handler.postDelayed(_timer, 1000);
                        }
                    } else {
                        builder.setContentTitle("WO" + _workOrderId + " | " + queued + " files queued");
                        builder.setContentText("Waiting for network");
                    }

                } else if (failed > 0) {
                    builder.setSmallIcon(R.drawable.ic_notif_fail);
                    builder.setContentTitle("WO" + _workOrderId + " | " + failed + " uploads failed");
                } else if (uploading > 0) {
                    builder.setSmallIcon(R.drawable.ic_anim_upload_start);
                    builder.setContentTitle("WO" + _workOrderId + " | uploading " + uploading);
                    if (queued > 0)
                        builder.setContentText(queued + " queued");

                } else if (queued > 0) {
                    builder.setSmallIcon(R.drawable.ic_notif_queued);
                    builder.setContentTitle("WO" + _workOrderId + " | " + queued + " files queued");
                    builder.setContentText("Waiting for network");
                } else if (success > 0) {
                    builder.setSmallIcon(R.drawable.ic_notif_success);
                    builder.setContentTitle("WO" + _workOrderId + " | " + success + " uploads complete!");
                }

                if (retries > 0 || failed > 0 || uploading > 0 || queued > 0 || success > 0)
                    manager.notify(_notificationId, builder.build());
            } else {
                NotificationCompat.Builder builder = new NotificationCompat.Builder(App.get());
                builder.setLargeIcon(null);
                builder.setContentIntent(pendingIntent);

                //builder.setSmallIcon(R.drawable.ic_notif_queued);

                if (retries > 0) {
                    builder.setSmallIcon(R.drawable.ic_notif_queued);
                    builder.setContentTitle("WO" + _workOrderId + " | uploads failed");
                    long timeleft = _timeout - System.currentTimeMillis();
                    if (timeleft >= 0) {
                        builder.setContentText("Retry in " + misc.convertMSToDHMS(timeleft, true));
                        if (!_isTiming) {
                            _isTiming = true;
                            _handler.postDelayed(_timer, 1000);
                        }
                    } else
                        builder.setContentText("Waiting for network");

                } else if (failed > 0) {
                    builder.setSmallIcon(R.drawable.ic_notif_fail);
                    builder.setContentTitle("WO" + _workOrderId + " | " + failed + " uploads failed");
                } else if (uploading > 0) {
                    builder.setSmallIcon(R.drawable.ic_anim_upload_start);
                    builder.setContentTitle("WO" + _workOrderId + " | uploading " + uploading);
                } else if (queued > 0) {
                    builder.setSmallIcon(R.drawable.ic_notif_queued);
                    builder.setContentTitle("WO" + _workOrderId + " | " + queued + " files queued");
                    builder.setContentText("Waiting for network");
                } else if (success > 0) {
                    builder.setSmallIcon(R.drawable.ic_notif_success);
                    builder.setContentTitle("WO" + _workOrderId + " | " + success + " uploads complete!");
                }

                if (retries > 0 || failed > 0 || uploading > 0 || queued > 0 || success > 0)
                    manager.notify(_notificationId, builder.build());
            }
        }
    }

    private Hashtable<Integer, WoNotif> _notifications = new Hashtable<>();

    public UploadTrackerDeliverables() {
    }

    public void update(Context context, String action, WebTransaction webTransaction) {
        try {
            TransactionParams tp = TransactionParams.fromJson(new JsonObject(webTransaction.getListenerParams()));
            JsonObject methodParams = new JsonObject(tp.methodParams);

            int workOrderId = methodParams.getInt("workOrderId");

            WoNotif woNotif;
            if (_notifications.containsKey(workOrderId)) {
                woNotif = _notifications.get(workOrderId);
            } else {
                woNotif = new WoNotif(workOrderId);
                _notifications.put(workOrderId, woNotif);
            }

            woNotif.updateNotification(action, webTransaction);
            woNotif.updateNotification(context);
        } catch (Exception ex) {
            Log.v(TAG, ex);
        }
    }

    public boolean isViable() {
        return _notifications.size() > 0;
    }
}
