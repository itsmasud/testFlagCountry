package com.fieldnation.ui.workorder;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.ResultReceiver;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.view.ActionMode;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.cocosw.undobar.UndoBarController;
import com.cocosw.undobar.UndoBarController.UndoBar;
import com.fieldnation.R;
import com.fieldnation.data.workorder.AdditionalExpense;
import com.fieldnation.data.workorder.Pay;
import com.fieldnation.data.workorder.Schedule;
import com.fieldnation.data.workorder.Workorder;
import com.fieldnation.data.workorder.WorkorderStatus;
import com.fieldnation.json.JsonObject;
import com.fieldnation.rpc.client.WorkorderService;
import com.fieldnation.ui.PagingListAdapter;
import com.fieldnation.ui.dialog.ConfirmDialog;
import com.fieldnation.ui.dialog.CounterOfferDialog;
import com.fieldnation.ui.dialog.DeviceCountDialog;
import com.fieldnation.ui.dialog.ExpiresDialog;
import com.fieldnation.utils.ISO8601;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.text.ParseException;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.LinkedList;
import java.util.List;

/**
 * A list adapter that loads work order lists.
 *
 * @author michael.carver
 */
public class WorkorderListAdapter extends PagingListAdapter<Workorder> {
    private static final String TAG = "ui.workorder.WorkorderListAdapter";

    // Intent Keys
    private static final String KEY_WORKORDER_ID = "com.fieldnation.ui.workorder.WorkorderListAdapter.WORKORDER_ID";

    // web states
    private static final int WEB_REMOVING_WORKRODER = 100;
    private static final int WEB_CHANGING_WORKORDER = 101;
    private static final int WEB_CHECKING_IN = 102;

    // Data
    private WorkorderService _workorderService = null;
    private Method _rpcMethod;
    private WorkorderDataSelector _dataSelection;
    private Hashtable<Long, Workorder> _pendingNotInterestedWorkorders = new Hashtable<Long, Workorder>();
    private Hashtable<Long, Workorder> _requestWorkingWorkorders = new Hashtable<Long, Workorder>();
    private ActionMode _actionMode = null;
    private Hashtable<Long, Workorder> _selectedWorkorders = new Hashtable<Long, Workorder>();
    private WorkorderUndoListener _wosumUndoListener;

    // Ui
    //private PayDialog _payDialog;
    private ExpiresDialog _expiresDialog;
    private ConfirmDialog _confirmDialog;
    private DeviceCountDialog _deviceCountDialog;
    private CounterOfferDialog _counterOfferDialog;

	/*-*****************************-*/
    /*-			Lifecycle			-*/
    /*-*****************************-*/

    public WorkorderListAdapter(FragmentActivity activity, WorkorderDataSelector selection) throws NoSuchMethodException {
        super(activity, Workorder.class);
        _dataSelection = selection;

        _rpcMethod = WorkorderService.class.getDeclaredMethod(selection.getCall(), new Class<?>[]{int.class,
                int.class, boolean.class});
        _rpcMethod.setAccessible(true);
    }

    public WorkorderListAdapter(FragmentActivity activity, WorkorderDataSelector selection, List<Workorder> workorders) throws NoSuchMethodException {
        super(activity, Workorder.class, workorders);
        _dataSelection = selection;

        _rpcMethod = WorkorderService.class.getDeclaredMethod(selection.getCall(), new Class<?>[]{int.class,
                int.class, boolean.class});
        _rpcMethod.setAccessible(true);
    }

    @Override
    public View getView(int position, Workorder object, View convertView, ViewGroup parent) {
        WorkorderCardView wosum = null;

        if (convertView == null) {
            wosum = new WorkorderCardView(parent.getContext());
        } else if (convertView instanceof WorkorderCardView) {
            wosum = (WorkorderCardView) convertView;
        } else {
            wosum = new WorkorderCardView(parent.getContext());
        }

        if (_pendingNotInterestedWorkorders.containsKey(object.getWorkorderId())) {
            // wosum.setDisplayMode(WorkorderCardView.MODE_UNDO_NOT_INTERESTED);
            return new View(getContext());
        } else if (_requestWorkingWorkorders.containsKey(object.getWorkorderId())) {
            wosum.setDisplayMode(WorkorderCardView.MODE_DOING_WORK);
        } else if (_selectedWorkorders.containsKey(object.getWorkorderId())) {
            wosum.setDisplayMode(WorkorderCardView.MODE_SELECTED);
        } else {
            wosum.setDisplayMode(WorkorderCardView.MODE_NORMAL);
        }

        wosum.setWorkorder(_dataSelection, object);
        wosum.setWorkorderSummaryListener(_wocv_listener);

        return wosum;
    }

    @Override
    public Workorder fromJson(JsonObject obj) {
        return Workorder.fromJson(obj);
    }

    @Override
    public void invalidateWebService() {
        _workorderService = null;
    }

    @Override
    public void getWebService(Context context, String username, String authToken, ResultReceiver resultReceiver) {
        if (_workorderService == null) {
            _workorderService = new WorkorderService(context, username, authToken, resultReceiver);
        }
    }

    @Override
    public void rebuildWebService(Context context, String username, String authToken, ResultReceiver resultReceiver) {
        _workorderService = new WorkorderService(context, username, authToken, resultReceiver);
    }

    @Override
    public void executeWebService(int resultCode, int page, boolean allowCache) {
        try {
            if (!_dataSelection.allowCache())
                allowCache = false;
            getContext().startService((Intent) _rpcMethod.invoke(_workorderService, resultCode, page, allowCache));
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void update(boolean allowCache) {
        removeActionMode();
        super.update(allowCache);
    }

    // happens on page flip
    public void onPause() {
        removeActionMode();
        UndoBarController.clear(getActivity());
    }

    private void removeActionMode() {
        if (_actionMode != null) {
            _actionMode.finish();
            _actionMode = null;
            _selectedWorkorders.clear();
        }
    }

	/*-*********************************-*/
    /*-				Events				-*/
    /*-*********************************-*/

    @Override
    public void handleWebResult(int resultCode, Bundle resultData) {
        if (resultCode == WEB_REMOVING_WORKRODER) {
            long workorderId = resultData.getLong(KEY_WORKORDER_ID);
            _pendingNotInterestedWorkorders.remove(workorderId);
            _requestWorkingWorkorders.remove(workorderId);
            update(false);
        } else if (resultCode == WEB_CHANGING_WORKORDER) {
            long workorderId = resultData.getLong(KEY_WORKORDER_ID);
            _requestWorkingWorkorders.remove(workorderId);
            update(false);
        } else if (resultCode == WEB_CHECKING_IN) {
            long workorderId = resultData.getLong(KEY_WORKORDER_ID);
            _requestWorkingWorkorders.remove(workorderId);
            update(false);
            Intent intent = new Intent(getContext(), WorkorderActivity.class);
            intent.putExtra(WorkorderActivity.INTENT_FIELD_WORKORDER_ID, workorderId);
            intent.putExtra(WorkorderActivity.INTENT_FIELD_CURRENT_TAB, WorkorderActivity.TAB_TASKS);
            getContext().startActivity(intent);
        }
    }

    private DeviceCountDialog.Listener _deviceCountListener = new DeviceCountDialog.Listener() {
        @Override
        public void onOk(Workorder workorder, int count) {
            Intent intent = _workorderService.checkout(WEB_CHANGING_WORKORDER, workorder.getWorkorderId(), count);
            intent.putExtra(KEY_WORKORDER_ID, workorder.getWorkorderId());
            getContext().startService(intent);
            _requestWorkingWorkorders.put(workorder.getWorkorderId(), workorder);
        }
    };

    private WorkorderCardView.Listener _wocv_listener = new WorkorderCardView.Listener() {
        @Override
        public void actionRequest(WorkorderCardView view, Workorder workorder) {
            final Workorder _workorder = workorder;

            _expiresDialog = ExpiresDialog.getInstance(getActivity().getSupportFragmentManager(), TAG);
            _expiresDialog.setListener(_expiresDialog_listener);
            _expiresDialog.show(workorder);
        }

        @Override
        public void actionCheckout(WorkorderCardView view, Workorder workorder) {
            //set  loading mode
            WorkorderCardView woCardViewObj = new WorkorderCardView(getContext());
            woCardViewObj.setDisplayMode(woCardViewObj.MODE_DOING_WORK);

            Pay pay = workorder.getPay();
            if (pay != null && pay.isPerDeviceRate()) {
                _deviceCountDialog = DeviceCountDialog.getInstance(getActivity().getSupportFragmentManager(), TAG);
                _deviceCountDialog.setListener(_deviceCountListener);
                _deviceCountDialog.show(workorder, pay.getMaxDevice());
            } else {
                Intent intent = _workorderService.checkout(WEB_CHANGING_WORKORDER, workorder.getWorkorderId());
                intent.putExtra(KEY_WORKORDER_ID, workorder.getWorkorderId());
                getContext().startService(intent);
                _requestWorkingWorkorders.put(workorder.getWorkorderId(), workorder);
            }
        }


        @Override
        public void actionCheckin(WorkorderCardView view, Workorder workorder) {
            //TODO set loading mode

            Intent intent = _workorderService.checkin(WEB_CHECKING_IN, workorder.getWorkorderId());
            intent.putExtra(KEY_WORKORDER_ID, workorder.getWorkorderId());
            getContext().startService(intent);
            _requestWorkingWorkorders.put(workorder.getWorkorderId(), workorder);
        }

        @Override
        public void actionAssignment(WorkorderCardView view, Workorder workorder) {
            _confirmDialog = ConfirmDialog.getInstance(getActivity().getSupportFragmentManager(), TAG);
            _confirmDialog.setListener(_confirmDialog_listener);
            _confirmDialog.show(workorder, workorder.getSchedule());
        }

        @Override
        public void actionAcknowledgeHold(WorkorderCardView view, Workorder workorder) {
            //TODO set loading mode
            Intent intent = _workorderService.acknowledgeHold(WEB_CHANGING_WORKORDER, workorder.getWorkorderId());
            intent.putExtra(KEY_WORKORDER_ID, workorder.getWorkorderId());
            getContext().startService(intent);
            _requestWorkingWorkorders.put(workorder.getWorkorderId(), workorder);
        }

        @Override
        public void viewCounter(WorkorderCardView view, Workorder workorder) {
            //TODO set loading mode
            _counterOfferDialog = CounterOfferDialog.getInstance(getActivity().getSupportFragmentManager(), TAG);
            _counterOfferDialog.setListener(_counterOfferDialog_listener);
            _counterOfferDialog.show(workorder);
        }

        @Override
        public void onLongClick(WorkorderCardView view, Workorder workorder) {
            if (_selectedWorkorders.containsKey(workorder.getWorkorderId())) {
                _selectedWorkorders.remove(workorder.getWorkorderId());
                view.setDisplayMode(WorkorderCardView.MODE_NORMAL);
                if (_actionMode != null && _selectedWorkorders.size() == 0) {
                    _actionMode.finish();
                    _actionMode = null;
                }
            } else {
                _selectedWorkorders.put(workorder.getWorkorderId(), workorder);
                view.setDisplayMode(WorkorderCardView.MODE_SELECTED);
                if (_actionMode == null) {
                    _actionMode = ((ActionBarActivity) getActivity()).startSupportActionMode(_actionMode_Callback);
                }
            }
        }

        @Override
        public void onClick(WorkorderCardView view, Workorder workorder) {
            if (view.isBundle()) {
                Intent intent = new Intent(getContext(), WorkorderBundleDetailActivity.class);
                intent.putExtra(WorkorderBundleDetailActivity.INTENT_FIELD_WORKORDER_ID, workorder.getWorkorderId());
                intent.putExtra(WorkorderBundleDetailActivity.INTENT_FIELD_BUNDLE_ID, workorder.getBundleId());
                getContext().startActivity(intent);

            } else {
                Intent intent = new Intent(getContext(), WorkorderActivity.class);
                intent.putExtra(WorkorderActivity.INTENT_FIELD_WORKORDER_ID, workorder.getWorkorderId());
                if (workorder.getStatus().getWorkorderStatus() == WorkorderStatus.INPROGRESS || workorder.getStatus().getWorkorderStatus() == WorkorderStatus.ASSIGNED) {
                    intent.putExtra(WorkorderActivity.INTENT_FIELD_CURRENT_TAB, WorkorderActivity.TAB_TASKS);
                } else {
                    intent.putExtra(WorkorderActivity.INTENT_FIELD_CURRENT_TAB, WorkorderActivity.TAB_DETAILS);
                }
                getContext().startActivity(intent);
            }
        }

        @Override
        public void onViewPayments(WorkorderCardView view, Workorder workorder) {
            //set  loading mode
            WorkorderCardView woCardViewObj = new WorkorderCardView(getContext());
            woCardViewObj.setDisplayMode(woCardViewObj.MODE_DOING_WORK);
            // TODO Method Stub: onViewPayments()
            Log.v(TAG, "Method Stub: onViewPayments()");
        }
    };

    private ExpiresDialog.Listener _expiresDialog_listener = new ExpiresDialog.Listener() {

        @Override
        public void onOk(Workorder workorder, String dateTime) {
            long time = -1;
            if (dateTime != null) {
                try {
                    time = (ISO8601.toUtc(dateTime) - System.currentTimeMillis()) / 1000;
                } catch (ParseException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
            //TODO set  loading mode
            Intent intent = _workorderService.request(WEB_CHANGING_WORKORDER, workorder.getWorkorderId(), time);
            intent.putExtra(KEY_WORKORDER_ID, workorder.getWorkorderId());
            getContext().startService(intent);
            _requestWorkingWorkorders.put(workorder.getWorkorderId(), workorder);
        }
    };

    private ConfirmDialog.Listener _confirmDialog_listener = new ConfirmDialog.Listener() {
        public void onOk(Workorder workorder, String startDate, long durationMilliseconds) {
            //set  loading mode
            WorkorderCardView woCardViewObj = new WorkorderCardView(getContext());
            woCardViewObj.setDisplayMode(woCardViewObj.MODE_DOING_WORK);

            try {
                long end = durationMilliseconds + ISO8601.toUtc(startDate);
                Intent intent = _workorderService.confirmAssignment(WEB_CHANGING_WORKORDER,
                        workorder.getWorkorderId(), startDate, ISO8601.fromUTC(end));
                intent.putExtra(KEY_WORKORDER_ID, workorder.getWorkorderId());
                getContext().startService(intent);
                _requestWorkingWorkorders.put(workorder.getWorkorderId(), workorder);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }

        @Override
        public void onCancel(Workorder workorder) {
        }

        @Override
        public void termsOnClick(Workorder workorder) {
            // TODO STUB .termsOnClick()
            Log.v(TAG, "STUB .termsOnClick()");

        }

    };

    private ActionMode.Callback _actionMode_Callback = new ActionMode.Callback() {

        @Override
        public boolean onCreateActionMode(ActionMode mode, Menu menu) {
            MenuInflater inflater = mode.getMenuInflater();
            inflater.inflate(R.menu.workorder_card, menu);
            return true;
        }

        @Override
        public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
            return false;
        }

        @Override
        public void onDestroyActionMode(ActionMode mode) {
            _actionMode = null;
            _selectedWorkorders.clear();
            notifyDataSetChanged();
        }

        @Override
        public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
            if (item.getItemId() == R.id.notinterested_action) {
                Enumeration<Workorder> e = _selectedWorkorders.elements();
                List<Workorder> list = new LinkedList<Workorder>();
                while (e.hasMoreElements()) {
                    Workorder wo = e.nextElement();
                    _pendingNotInterestedWorkorders.put(wo.getWorkorderId(), wo);
                    list.add(wo);
                }
                _selectedWorkorders.clear();

                _wosumUndoListener = new WorkorderUndoListener(list, getContext(), getUsername(), getAuthToken(),
                        _undoListener);
                UndoBar undo = new UndoBar(getActivity());
                undo.message("Undo Not Interested");
                undo.listener(_wosumUndoListener);
                undo.duration(5000);
                undo.show();

                notifyDataSetChanged();

                return true;
            }
            return false;
        }
    };

    private WorkorderUndoListener.Listener _undoListener = new WorkorderUndoListener.Listener() {
        @Override
        public void onComplete(List<Workorder> success, List<Workorder> failed) {
            new Exception().printStackTrace();
            _pendingNotInterestedWorkorders.clear();
            update(false);
        }

        @Override
        public void onUndo() {
            new Exception().printStackTrace();
            _pendingNotInterestedWorkorders.clear();
            update(false);
        }
    };

    private CounterOfferDialog.Listener _counterOfferDialog_listener = new CounterOfferDialog.Listener() {
        @Override
        public void onOk(Workorder workorder, String reason, boolean expires, int expirationInSeconds, Pay pay, Schedule schedule, AdditionalExpense[] expenses) {
            getActivity().startService(
                    _workorderService.setCounterOffer(WEB_CHANGING_WORKORDER,
                            workorder.getWorkorderId(), expires, reason, expirationInSeconds, pay,
                            schedule, expenses));


        }
    };

}
