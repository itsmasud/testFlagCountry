package com.fieldnation.ui.workorder;

import android.content.Context;
import android.content.Intent;
import android.os.ResultReceiver;
import android.support.v4.app.FragmentActivity;
import android.support.v7.view.ActionMode;
import android.view.View;
import android.view.ViewGroup;

import com.cocosw.undobar.UndoBarController;
import com.fieldnation.data.workorder.Workorder;
import com.fieldnation.json.JsonObject;
import com.fieldnation.rpc.client.WorkorderService;
import com.fieldnation.ui.PagingListAdapter;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Hashtable;
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


    // Data
    private WorkorderService _workorderService = null;
    private Method _rpcMethod;
    private WorkorderDataSelector _dataSelection;
    private Hashtable<Long, Workorder> _pendingNotInterestedWorkorders = new Hashtable<Long, Workorder>();
    private Hashtable<Long, Workorder> _requestWorkingWorkorders = new Hashtable<Long, Workorder>();
    private ActionMode _actionMode = null;
    private Hashtable<Long, Workorder> _selectedWorkorders = new Hashtable<Long, Workorder>();
    private WorkorderUndoListener _wosumUndoListener;
    private WorkorderCardView.Listener _wocvListener;

    // Ui

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
        wosum.setWorkorderSummaryListener(_wocvListener);

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

    public void setWorkorderCardViewListener(WorkorderCardView.Listener wocvListener) {
        _wocvListener = wocvListener;
    }

    private class ItemState{
        public boolean isLoading;
        public boolean isSelected;
    }
}
