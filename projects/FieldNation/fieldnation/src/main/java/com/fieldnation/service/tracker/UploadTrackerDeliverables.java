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
            if (action.equals(ACTION_DELETE)) {
                if (tuples.containsKey(webTransaction.getUUID().uuid))
                    tuples.remove(webTransaction.getUUID().uuid);
            } else if (tuples.containsKey(webTransaction.getUUID().uuid)) {
                tuple = tuples.get(webTransaction.getUUID().uuid);
                tuple.action = action;
                tuple.webTransaction = webTransaction;
            } else {
                tuple = new Tuple(action, webTransaction);
                tuples.put(webTransaction.getUUID().uuid, tuple);
            }
        }

        boolean updateNotification(Context context) {
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

            //Log.v(TAG, "t" + total + " q" + queued + " u" + uploading + " r" + retries + " s" + success + " f" + failed);

            NotificationManager manager = (NotificationManager) App.get().getSystemService(Service.NOTIFICATION_SERVICE);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                Notification.Builder builder = new Notification.Builder(App.get(), NotificationDef.FILE_UPLOAD_CHANNEL);
                builder.setLargeIcon((Bitmap) null);
                builder.setContentIntent(pendingIntent);

                //builder.setSmallIcon(R.drawable.ic_notif_queued);

                if (uploading > 0) {
                    builder.setSmallIcon(R.drawable.ic_anim_upload_start);
                    builder.setContentTitle(context.getResources().getString(
                            R.string.uploading_to_wo, _workOrderId));
                    if (success > 0) {
                        builder.setContentText(context.getString(R.string.num_num_uploaded, success, total));
                    } else {
                        builder.setContentText(context.getString(R.string.num_uploading, uploading));
                    }

                } else if (retries > 0) {
                    builder.setSmallIcon(R.drawable.ic_notif_queued);
                    long timeleft = _timeout - System.currentTimeMillis();
                    if (timeleft >= 0) {
                        builder.setContentTitle(context.getResources().getQuantityString(
                                R.plurals.num_uploads_failed_title, retries, _workOrderId));
                        builder.setContentText(context.getResources().getQuantityString(
                                R.plurals.num_uploads_retrying, retries, retries, misc.convertMsToHuman(timeleft)));
                        if (!_isTiming) {
                            _isTiming = true;
                            _handler.postDelayed(_timer, 1000);
                        }
                    } else {
                        builder.setContentTitle(context.getResources().getString(
                                R.string.uploading_to_wo, _workOrderId));
                        builder.setContentText(context.getString(R.string.waiting_for_network));
                    }

                } else if (failed > 0) {
                    builder.setSmallIcon(R.drawable.ic_notif_fail);
                    builder.setContentTitle(context.getResources().getQuantityString(
                            R.plurals.num_uploads_failed_title, failed, _workOrderId));
                    builder.setContentText(context.getString(R.string.num_num_failed_to_upload, failed, total));
                } else if (queued > 0) {
                    builder.setSmallIcon(R.drawable.ic_notif_queued);
                    builder.setContentTitle(context.getResources().getString(
                            R.string.uploading_to_wo, _workOrderId));
                    builder.setContentText(context.getString(
                            R.string.waiting_for_network));
                } else if (success > 0) {
                    builder.setSmallIcon(R.drawable.ic_notif_success);
                    builder.setContentTitle(context.getResources().getQuantityString(
                            R.plurals.num_uploads_complete_title, success, _workOrderId));
                    builder.setContentText(context.getString(
                            R.string.num_num_successfully_uploaded, success, total));
                }

                if (retries > 0 || failed > 0 || uploading > 0 || queued > 0 || success > 0)
                    manager.notify(_notificationId, builder.build());
            } else {
                NotificationCompat.Builder builder = new NotificationCompat.Builder(App.get());
                builder.setLargeIcon(null);
                builder.setContentIntent(pendingIntent);

                //builder.setSmallIcon(R.drawable.ic_notif_queued);
                if (uploading > 0) {
                    builder.setSmallIcon(R.drawable.ic_anim_upload_start);
                    builder.setContentTitle(context.getResources().getString(
                            R.string.uploading_to_wo, _workOrderId));
                    if (success > 0) {
                        builder.setContentText(context.getString(R.string.num_num_uploaded, success, total));
                    } else {
                        builder.setContentText(context.getString(R.string.num_uploading, uploading));
                    }

                } else if (retries > 0) {
                    builder.setSmallIcon(R.drawable.ic_notif_queued);
                    long timeleft = _timeout - System.currentTimeMillis();
                    if (timeleft >= 0) {
                        builder.setContentTitle(context.getResources().getQuantityString(
                                R.plurals.num_uploads_failed_title, retries, _workOrderId));
                        builder.setContentText(context.getResources().getQuantityString(
                                R.plurals.num_uploads_retrying, retries, retries, misc.convertMsToHuman(timeleft, true)));
                        if (!_isTiming) {
                            _isTiming = true;
                            _handler.postDelayed(_timer, 1000);
                        }
                    } else {
                        builder.setContentTitle(context.getResources().getString(
                                R.string.uploading_to_wo, _workOrderId));
                        builder.setContentText(context.getString(R.string.waiting_for_network));
                    }

                } else if (failed > 0) {
                    builder.setSmallIcon(R.drawable.ic_notif_fail);
                    builder.setContentTitle(context.getResources().getQuantityString(
                            R.plurals.num_uploads_failed_title, failed, _workOrderId));
                    builder.setContentText(context.getString(R.string.num_num_failed_to_upload, failed, total));
                } else if (queued > 0) {
                    builder.setSmallIcon(R.drawable.ic_notif_queued);
                    builder.setContentTitle(context.getResources().getString(
                            R.string.uploading_to_wo, _workOrderId));
                    builder.setContentText(context.getString(
                            R.string.waiting_for_network));
                } else if (success > 0) {
                    builder.setSmallIcon(R.drawable.ic_notif_success);
                    builder.setContentTitle(context.getResources().getQuantityString(
                            R.plurals.num_uploads_complete_title, success, _workOrderId));
                    builder.setContentText(context.getString(
                            R.string.num_num_successfully_uploaded, success, total));
                }

                if (retries > 0 || failed > 0 || uploading > 0 || queued > 0 || success > 0)
                    manager.notify(_notificationId, builder.build());
            }

            if (retries == 0 && failed == 0 && uploading == 0 && queued == 0 && success >= 0) {
                return true;
            }
            return false;
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
            if (woNotif.updateNotification(context)) {
                _notifications.remove(workOrderId);
            }
        } catch (Exception ex) {
            Log.v(TAG, ex);
        }
    }

    public boolean isViable() {
        return _notifications.size() > 0;
    }
}
