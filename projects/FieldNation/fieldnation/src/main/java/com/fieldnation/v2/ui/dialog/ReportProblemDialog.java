package com.fieldnation.v2.ui.dialog;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.fieldnation.App;
import com.fieldnation.R;
import com.fieldnation.fndialog.Controller;
import com.fieldnation.fndialog.SimpleDialog;
import com.fieldnation.fnlog.Log;
import com.fieldnation.fntoast.ToastClient;
import com.fieldnation.fntools.misc;
import com.fieldnation.ui.HintArrayAdapter;
import com.fieldnation.ui.HintSpinner;
import com.fieldnation.ui.KeyedDispatcher;
import com.fieldnation.v2.data.client.WorkordersWebApi;
import com.fieldnation.v2.data.model.Problem;
import com.fieldnation.v2.data.model.ProblemType;
import com.fieldnation.v2.data.model.Problems;
import com.fieldnation.v2.data.model.WorkOrder;

/**
 * Created by Michael on 10/5/2016.
 */

public class ReportProblemDialog extends SimpleDialog {
    private static final String TAG = "ReportProblemDialog";

    private static final String DIALOG_CANCEL_WARNING = TAG + ".cancelWarningDialog";

    // State keys
    private static final String PARAM_WORKORDER = "workOrder";

    private static final String STATE_WORKORDER = "STATE_WORKORDER";
    private static final String STATE_PRIMARY_POS = "STATE_PRIMARY_POS";
    private static final String STATE_SECONDARY_POS = "STATE_SECONDARY_POS";
    private static final String STATE_SELECTED_PROBLEM = "STATE_SELECTED_PROBLEM";
    private static final String STATE_OK_ENABLED = "STATE_OK_ENABLED";

    private static final String[] TIMEFRAMES = new String[]{"5", "10", "15", "30", "60", "Other"};

    // Ui
    private TextView _titleTextView;
    private ProgressBar _loadingBar;
    private HintSpinner _primarySpinner;
    private HintSpinner _secondarySpinner;
    private HintSpinner _timeframeSpinner;
    private TextInputLayout _timeframeLayout;
    private EditText _timeframeEditText;
    private TextInputLayout _explanationLayout;
    private EditText _explanationEditText;
    private TextView _noteTextView;
    private Button _cancelButton;
    private Button _okButton;

    // Data
    private ProblemType[] _parentTypes = null;
    private ProblemType[] _childTypes = null;
    private WorkOrder _workOrder;

    // Saved Data
    private int _primaryPosition = -1;
    private int _secondaryPosition = -1;
    private int _timeframePosition = -1;
    private ProblemType _selectedProblem = null;
    private boolean _clear = false;

    public ReportProblemDialog(Context context, ViewGroup container) {
        super(context, container);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, Context context, ViewGroup container) {
        View v = inflater.inflate(R.layout.dialog_v2_report_problem, container, false);

        _titleTextView = (TextView) v.findViewById(R.id.title_textview);
        _loadingBar = (ProgressBar) v.findViewById(R.id.loadingBar);
        _primarySpinner = (HintSpinner) v.findViewById(R.id.primary_spinner);
        _secondarySpinner = (HintSpinner) v.findViewById(R.id.secondary_spinner);
        _timeframeSpinner = (HintSpinner) v.findViewById(R.id.timeframe_spinner);
        _timeframeLayout = (TextInputLayout) v.findViewById(R.id.timeframe_layout);
        _timeframeEditText = (EditText) v.findViewById(R.id.timeframe_edittext);
        _explanationLayout = (TextInputLayout) v.findViewById(R.id.explanation_layout);
        _explanationEditText = (EditText) v.findViewById(R.id.explanation_edittext);
        _noteTextView = (TextView) v.findViewById(R.id.note_textview);
        _cancelButton = (Button) v.findViewById(R.id.cancel_button);
        _okButton = (Button) v.findViewById(R.id.ok_button);

        return v;
    }

    @Override
    public void onStart() {
        super.onStart();
        _primarySpinner.setOnItemSelectedListener(_problem1_onItemClick);
        _secondarySpinner.setOnItemSelectedListener(_problem2_onItemClick);
        _timeframeSpinner.setOnItemSelectedListener(_timeframe_onItemClick);
        _timeframeEditText.addTextChangedListener(_timeframeEditText_watcher);
        _explanationEditText.addTextChangedListener(_textEditText_watcherListener);
        _cancelButton.setOnClickListener(_cancel_onClick);
        _okButton.setOnClickListener(_ok_onClick);
    }

    @Override
    public void show(Bundle payload, boolean animate) {
        setLoading(true);
        if (payload.containsKey(PARAM_WORKORDER)) {
            _workOrder = payload.getParcelable(PARAM_WORKORDER);
        }

        populateUi();
        super.show(payload, animate);
    }

    private void setLoading(boolean loading) {
        if (loading) {
            _titleTextView.setVisibility(View.GONE);
            _loadingBar.setVisibility(View.VISIBLE);
            _primarySpinner.setVisibility(View.GONE);
            _secondarySpinner.setVisibility(View.GONE);
            _timeframeSpinner.setVisibility(View.GONE);
            _timeframeLayout.setVisibility(View.GONE);
            _explanationLayout.setVisibility(View.GONE);
            _noteTextView.setVisibility(View.GONE);
            _cancelButton.setVisibility(View.GONE);
            _okButton.setVisibility(View.GONE);
        } else {
            _titleTextView.setVisibility(View.VISIBLE);
            _loadingBar.setVisibility(View.GONE);
            _primarySpinner.setVisibility(View.VISIBLE);
            _secondarySpinner.setVisibility(View.GONE);
            _timeframeSpinner.setVisibility(View.GONE);
            _timeframeLayout.setVisibility(View.GONE);
            _explanationLayout.setVisibility(View.VISIBLE);
            _noteTextView.setVisibility(View.VISIBLE);
            _cancelButton.setVisibility(View.VISIBLE);
            _okButton.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void dismiss(boolean animate) {

        super.dismiss(animate);
    }

    @Override
    public void onRestoreDialogState(Bundle savedState) {
        super.onRestoreDialogState(savedState);

        if (savedState == null)
            return;

        if (savedState.containsKey(STATE_PRIMARY_POS))
            _primaryPosition = savedState.getInt(STATE_PRIMARY_POS);

        if (savedState.containsKey(STATE_SECONDARY_POS))
            _secondaryPosition = savedState.getInt(STATE_SECONDARY_POS);

        if (savedState.containsKey(STATE_WORKORDER))
            _workOrder = savedState.getParcelable(STATE_WORKORDER);

        if (savedState.containsKey(STATE_SELECTED_PROBLEM))
            _selectedProblem = savedState.getParcelable(STATE_SELECTED_PROBLEM);

        if (savedState.containsKey(STATE_OK_ENABLED))
            _okButton.setEnabled(savedState.getBoolean(STATE_OK_ENABLED));

        if (_workOrder != null) {
            Problems problems = _workOrder.getProblems();

            if (problems != null && problems.getTypes() != null && problems.getTypes().length > 0) {
                getPrimaryAdapter().clear();
                getPrimaryAdapter().addAll((Object[]) problems.getTypes());

                if (_primaryPosition != -1) {
                    getPrimarySpinner().setSelection(_primaryPosition);

                    _childTypes = problems.getTypes()[_primaryPosition].getChildren();
                    if (_childTypes != null && _childTypes.length > 0) {
                        getSecondaryAdapter().clear();
                        getSecondaryAdapter().addAll((Object[]) _childTypes);
                    }
                    if (_secondaryPosition != -1 && _childTypes != null && _childTypes.length > 0) {
                        getSecondarySpinner().setSelection(_secondaryPosition);
                    } else {
                        getSecondarySpinner().clearSelection();
                    }
                } else {
                    getPrimarySpinner().clearSelection();
                }
            } else {
                getPrimarySpinner().clearSelection();
            }
        } else {
            getPrimarySpinner().clearSelection();
        }
        populateUi();
    }

    @Override
    public void onSaveDialogState(Bundle outState) {
        outState.putInt(STATE_PRIMARY_POS, _primaryPosition);
        outState.putInt(STATE_SECONDARY_POS, _secondaryPosition);
        if (_workOrder != null)
            outState.putParcelable(STATE_WORKORDER, _workOrder);
        if (_selectedProblem != null)
            outState.putParcelable(STATE_SELECTED_PROBLEM, _selectedProblem);
        if (_okButton != null)
            outState.putBoolean(STATE_OK_ENABLED, _okButton.isEnabled());

        super.onSaveDialogState(outState);
    }

    private HintSpinner getPrimarySpinner() {
        if (_primarySpinner != null && _primarySpinner.getAdapter() == null) {
            HintArrayAdapter adapter = new HintArrayAdapter(getView().getContext(), R.layout.view_spinner_item);
            adapter.setDropDownViewResource(android.support.design.R.layout.support_simple_spinner_dropdown_item);
            _primarySpinner.setAdapter(adapter);
        }
        return _primarySpinner;
    }

    private HintArrayAdapter getPrimaryAdapter() {
        return (HintArrayAdapter) getPrimarySpinner().getAdapter();
    }

    private HintSpinner getSecondarySpinner() {
        if (_secondarySpinner != null && _secondarySpinner.getAdapter() == null) {
            HintArrayAdapter adapter = new HintArrayAdapter(getView().getContext(), R.layout.view_spinner_item);
            adapter.setDropDownViewResource(android.support.design.R.layout.support_simple_spinner_dropdown_item);
            _secondarySpinner.setAdapter(adapter);
        }
        return _secondarySpinner;
    }

    private HintArrayAdapter getSecondaryAdapter() {
        return (HintArrayAdapter) getSecondarySpinner().getAdapter();
    }

    private HintSpinner getTimeFrameSpinner() {
        if (_timeframeSpinner != null && _timeframeSpinner.getAdapter() == null) {
            HintArrayAdapter adapter = HintArrayAdapter.createFromArray(getView().getContext(), TIMEFRAMES, R.layout.view_spinner_item);
            adapter.setDropDownViewResource(android.support.design.R.layout.support_simple_spinner_dropdown_item);
            _timeframeSpinner.setAdapter(adapter);
        }
        return _timeframeSpinner;
    }

    private HintArrayAdapter getTimeFrameAdapter() {
        return (HintArrayAdapter) _timeframeSpinner.getAdapter();
    }

    private void populateUi() {
        if (_noteTextView == null)
            return;

        if (_workOrder == null)
            return;

        setLoading(false);

        { // put in a block to limit the scope of pList
            ProblemType[] types = _workOrder.getProblems().getTypes();

            if (types == null || types.length == 0) {
                _parentTypes = null;
                getPrimaryAdapter().clear();
                getPrimarySpinner().clearSelection();
            } else if (types != _parentTypes) {
                _parentTypes = types;
                getPrimaryAdapter().clear();
                getPrimaryAdapter().addAll((Object[]) _parentTypes);
                getPrimarySpinner().clearSelection();
            }
        }

        getTimeFrameSpinner();

        _noteTextView.setVisibility(View.GONE);
        _timeframeSpinner.setVisibility(View.GONE);

        if (_parentTypes == null) {
            return;
        }

        if (_primaryPosition >= 0) {
            ProblemType[] childTypes = _workOrder.getProblems().getTypes()[_primaryPosition].getChildren();

            if (childTypes == null || childTypes.length == 0) {
                _childTypes = null;
                _secondaryPosition = -1;
                getSecondarySpinner().setVisibility(View.GONE);
                getSecondaryAdapter().clear();
            } else if (childTypes != _childTypes) {
                _childTypes = childTypes;
                getSecondaryAdapter().clear();
                getSecondaryAdapter().addAll((Object[]) _childTypes);
                getSecondarySpinner().clearSelection();
            }
        }

        if (_childTypes != null) {
            getSecondarySpinner().setVisibility(View.VISIBLE);
            getSecondarySpinner().clearFocus();
        }

        if (!misc.isEmptyOrNull(_explanationEditText.getText().toString()) && _selectedProblem != null) {
            _okButton.setEnabled(true);
        } else {
            _okButton.setEnabled(false);
        }

        if (_selectedProblem == null) {
            return;
        }

        _explanationEditText.requestFocus();
        misc.showKeyboard(_explanationEditText);
    }

    private final AdapterView.OnItemSelectedListener _problem1_onItemClick = new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            _primaryPosition = position;
            _selectedProblem = (ProblemType) getPrimaryAdapter().getItem(position);
            _textEditText_watcherListener.onTextChanged(
                    _explanationEditText.getText().toString(),
                    0, _explanationEditText.getText().toString().length(),
                    _explanationEditText.getText().toString().length());
            populateUi();
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {
        }
    };

    private final AdapterView.OnItemSelectedListener _problem2_onItemClick = new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            _secondaryPosition = position;
            misc.showKeyboard(_explanationEditText);
            _selectedProblem = (ProblemType) getSecondaryAdapter().getItem(position);
            _textEditText_watcherListener.onTextChanged(
                    _explanationEditText.getText().toString(),
                    0, _explanationEditText.getText().toString().length(),
                    _explanationEditText.getText().toString().length());
            populateUi();

        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {
        }
    };

    private final AdapterView.OnItemSelectedListener _timeframe_onItemClick = new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            _timeframePosition = position;
            Log.v(TAG, "onItemSelected " + position);
            populateUi();
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {
        }
    };

    private final View.OnClickListener _ok_onClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            misc.hideKeyboard(_okButton);
            Log.v(TAG, "problem1=" + _primaryPosition + ", problem2=" + _secondaryPosition);

            String explanation = null;

            if (!misc.isEmptyOrNull(_explanationEditText.getText().toString())) {
                explanation = _explanationEditText.getText().toString();
            }

            // TODO need to read a flag before showing this
            //CancelWarningDialog.show(App.get(), DIALOG_CANCEL_WARNING, _workOrder.getWorkorderId(), explanation);


            Problem problem = new Problem();
            try {
                problem.setComments(explanation);
                if (_secondaryPosition != -1) {
                    problem.setType((ProblemType) getSecondaryAdapter().getItem(_secondaryPosition));
                } else {
                    problem.setType((ProblemType) getPrimaryAdapter().getItem(_primaryPosition));
                }

                WorkordersWebApi.addProblem(App.get(), _workOrder.getId(), problem, App.get().getSpUiContext());

                ToastClient.toast(App.get(), R.string.buyer_has_been_notified, Toast.LENGTH_LONG);
            } catch (Exception ex) {
                Log.v(TAG, ex);
            }

            _onSendDispatcher.dispatch(getUid(), _workOrder.getId(), explanation, problem.getType());

            dismiss(true);
        }
    };

    private final View.OnClickListener _cancel_onClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            dismiss(true);
            misc.hideKeyboard(v);
        }
    };

    private final TextWatcher _textEditText_watcherListener = new TextWatcher() {
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        public void onTextChanged(CharSequence s, int start, int before, int count) {
            if (_primaryPosition < 0) return;
            if (getSecondarySpinner().isShown() && _secondaryPosition < 0) return;
            if (_explanationEditText.getText().toString().trim().length() > 0) {
                _okButton.setEnabled(true);
            } else {
                _okButton.setEnabled(false);
            }
        }

        public void afterTextChanged(Editable s) {
        }
    };

    private final TextWatcher _timeframeEditText_watcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            _timeframeSpinner.setVisibility(View.VISIBLE);

            if (_timeframePosition == -1 || _timeframePosition == 5) {
                _okButton.setEnabled(false);
            } else {
                _okButton.setEnabled(true);
            }

            if (_timeframePosition == 5) {
                _timeframeLayout.setVisibility(View.VISIBLE);
                if (misc.isEmptyOrNull(_timeframeEditText.getText().toString())) {
                    _okButton.setEnabled(false);
                }
            } else {
                _timeframeLayout.setVisibility(View.GONE);
            }

            if (_primaryPosition < 0) return;
            if (getSecondarySpinner().isShown() && _secondaryPosition < 0) return;
            if (_explanationEditText.getText().toString().trim().length() > 0) {
                _okButton.setEnabled(true);
            } else {
                _okButton.setEnabled(false);
            }
        }

        @Override
        public void afterTextChanged(Editable s) {
        }
    };

    public static void show(Context context, String uid, WorkOrder workorder) {
        Bundle params = new Bundle();
        params.putParcelable(PARAM_WORKORDER, workorder);
        Controller.show(context, uid, ReportProblemDialog.class, params);
    }

    /*-************************-*/
    /*-         Send           -*/
    /*-************************-*/
    public interface OnSendListener {
        void onSend(int workOrderId, String explanation, ProblemType type);
    }

    private static KeyedDispatcher<OnSendListener> _onSendDispatcher = new KeyedDispatcher<OnSendListener>() {
        @Override
        public void onDispatch(OnSendListener listener, Object... parameters) {
            listener.onSend((Integer) parameters[0], (String) parameters[1], (ProblemType) parameters[2]);
        }
    };

    public static void addOnSendListener(String uid, OnSendListener onSendListener) {
        _onSendDispatcher.add(uid, onSendListener);
    }

    public static void removeOnSendListener(String uid, OnSendListener onSendListener) {
        _onSendDispatcher.remove(uid, onSendListener);
    }

    public static void removeAll(String uid) {
        _onSendDispatcher.removeAll(uid);
    }

}
