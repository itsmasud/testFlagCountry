package com.fieldnation.v2.ui.dialog;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.view.menu.ActionMenuItemView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.fieldnation.App;
import com.fieldnation.R;
import com.fieldnation.fndialog.Controller;
import com.fieldnation.fndialog.FullScreenDialog;
import com.fieldnation.fnlog.Log;
import com.fieldnation.fntools.ForLoopRunnable;
import com.fieldnation.fntools.misc;
import com.fieldnation.ui.KeyedDispatcher;
import com.fieldnation.ui.workorder.detail.HoldReasonCard;
import com.fieldnation.v2.data.client.WorkordersWebApi;
import com.fieldnation.v2.data.model.Acknowledgment;
import com.fieldnation.v2.data.model.Hold;
import com.fieldnation.v2.data.model.WorkOrder;

import java.util.Random;

/**
 * Created by Shoaib on 05/25/2016.
 */

public class HoldReviewDialog extends FullScreenDialog {
    private static final String TAG = "HoldReviewDialog";

    // State
    private static final String PARAM_WORKORDER = "workOrder";


    // Dialogs
    private static final String UID_DIALOG_ETA = TAG + ".etaDialog";

    // Ui
    private Toolbar _toolbar;
    private ActionMenuItemView _finishMenu;

    private LinearLayout _holdLayout;
    private TextView _explanationTextView;

    // Passed Data
    private WorkOrder _workOrder;


    /*-*************************************-*/
    /*-             Life cycle              -*/
    /*-*************************************-*/
    public HoldReviewDialog(Context context, ViewGroup container) {
        super(context, container);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, Context context, ViewGroup container) {

        View v = inflater.inflate(R.layout.dialog_v2_hold_review, container, false);

        _toolbar = (Toolbar) v.findViewById(R.id.toolbar);
        _toolbar.setNavigationIcon(R.drawable.ic_signature_x);
        _toolbar.inflateMenu(R.menu.dialog);

        _finishMenu = (ActionMenuItemView) _toolbar.findViewById(R.id.primary_menu);

        _holdLayout = (LinearLayout) v.findViewById(R.id.holdReasons_list);
        _explanationTextView = (TextView) v.findViewById(R.id.explanation_textview);

        return v;
    }


    @Override
    public void onStart() {
        super.onStart();
        _toolbar.setOnMenuItemClickListener(_menu_onClick);
        _toolbar.setNavigationOnClickListener(_toolbar_onClick);
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.v(TAG, "onResume");
        _toolbar.setOnMenuItemClickListener(_menu_onClick);
        _toolbar.setNavigationOnClickListener(_toolbar_onClick);
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    public void show(Bundle params, boolean animate) {
        Log.v(TAG, "Show");
        super.show(params, animate);
        _workOrder = params.getParcelable(PARAM_WORKORDER);
        populateUi();

    }

    @Override
    public void onSaveDialogState(Bundle outState) {
        super.onSaveDialogState(outState);
    }

    @Override
    public void onRestoreDialogState(Bundle savedState) {
        Log.v(TAG, "onRestoreDialogState");
        super.onRestoreDialogState(savedState);
        populateUi();
    }


    /*-********************************************-*/
    /*-             Internal Mutators              -*/
    /*-********************************************-*/
    private void populateUi() {
        if (_workOrder == null)
            return;

        if (_workOrder.getHolds() == null)
            return;

        Log.v(TAG, "populateUi");
        _toolbar.setTitle(_toolbar.getContext().getString(R.string.dialog_review_hold_title));
        _finishMenu.setTitle(App.get().getString(R.string.btn_acknowledge));

        _holdLayout.removeAllViews();

        ForLoopRunnable r = new ForLoopRunnable(_workOrder.getHolds().getResults().length, new Handler()) {
            private final Hold[] holds = _workOrder.getHolds().getResults();

            @Override
            public void next(int i) throws Exception {
                HoldReasonCard v = null;
                if (i < _holdLayout.getChildCount()) {
                    v = (HoldReasonCard) _holdLayout.getChildAt(i);
                } else {
                    v = new HoldReasonCard(getView().getContext());
                    _holdLayout.addView(v);
                }
                v.setData(holds[i].getType().getName());
            }
        };
        _holdLayout.postDelayed(r, new Random().nextInt(1000));

        final String explanation = _workOrder.getHolds().getResults()[0].getReason();
        if (!misc.isEmptyOrNull(explanation))
            _explanationTextView.setText(_workOrder.getHolds().getResults()[0].getReason());
    }


    /*-*************************************-*/
    /*-             Ui Events               -*/
    /*-*************************************-*/

    private final View.OnClickListener _toolbar_onClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            _onCancelDispatcher.dispatch(getUid());
            dismiss(true);
        }
    };

    private final Toolbar.OnMenuItemClickListener _menu_onClick = new Toolbar.OnMenuItemClickListener() {
        @Override
        public boolean onMenuItemClick(MenuItem item) {
            try {
                Hold unAck = _workOrder.getUnAcknowledgedHold();
                Hold param = new Hold();
                param.acknowledgment(new Acknowledgment().status(Acknowledgment.StatusEnum.ACKNOWLEDGED));
                param.id(unAck.getId());

                WorkordersWebApi.updateHold(App.get(), _workOrder.getId(), unAck.getId(), param, App.get().getSpUiContext());
            } catch (Exception ex) {
                Log.v(TAG, ex);
            }
            _onAcknowledgeDispatcher.dispatch(getUid(), _workOrder.getId());

            dismiss(true);
            return true;
        }
    };

    public static void show(Context context, String uid, WorkOrder workOrder) {
        Bundle params = new Bundle();
        params.putParcelable(PARAM_WORKORDER, workOrder);
        Controller.show(context, uid, HoldReviewDialog.class, params);
    }

    /*-*************************************-*/
    /*-         Accepted Listener           -*/
    /*-*************************************-*/
    public interface OnAcknowledgeListener {
        void onAcknowledge(int workOrderId);
    }

    private static KeyedDispatcher<OnAcknowledgeListener> _onAcknowledgeDispatcher = new KeyedDispatcher<OnAcknowledgeListener>() {
        @Override
        public void onDispatch(OnAcknowledgeListener listener, Object... parameters) {
            listener.onAcknowledge((Integer) parameters[0]);
        }
    };

    public static void addOnAcknowledgeListener(String uid, OnAcknowledgeListener onAcknowledgeListener) {
        _onAcknowledgeDispatcher.add(uid, onAcknowledgeListener);
    }

    public static void removeOnAcknowledgeListener(String uid, OnAcknowledgeListener onAcknowledgeListener) {
        _onAcknowledgeDispatcher.remove(uid, onAcknowledgeListener);
    }

    public static void removeAllOnAcceptedListener(String uid) {
        _onAcknowledgeDispatcher.removeAll(uid);
    }

    /*-**************************-*/
    /*-         Cancel           -*/
    /*-**************************-*/
    public interface OnCancelListener {
        void onCancel();
    }

    private static KeyedDispatcher<OnCancelListener> _onCancelDispatcher = new KeyedDispatcher<OnCancelListener>() {
        @Override
        public void onDispatch(OnCancelListener listener, Object... parameters) {
            listener.onCancel();
        }
    };

    public static void addOnCancelListener(String uid, OnCancelListener onCancelListener) {
        _onCancelDispatcher.add(uid, onCancelListener);
    }

    public static void removeOnCancelListener(String uid, OnCancelListener onCancelListener) {
        _onCancelDispatcher.remove(uid, onCancelListener);
    }

    public static void removeAllOnCancelListener(String uid) {
        _onCancelDispatcher.removeAll(uid);
    }


}