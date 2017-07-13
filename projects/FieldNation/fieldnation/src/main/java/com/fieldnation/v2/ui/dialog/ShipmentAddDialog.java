package com.fieldnation.v2.ui.dialog;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.design.widget.TextInputLayout;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.fieldnation.App;
import com.fieldnation.R;
import com.fieldnation.fnactivityresult.ActivityResultClient;
import com.fieldnation.fndialog.Controller;
import com.fieldnation.fndialog.SimpleDialog;
import com.fieldnation.fnlog.Log;
import com.fieldnation.fnpermissions.PermissionsClient;
import com.fieldnation.fntoast.ToastClient;
import com.fieldnation.fntools.FileUtils;
import com.fieldnation.fntools.KeyedDispatcher;
import com.fieldnation.fntools.misc;
import com.fieldnation.service.data.workorder.WorkorderClient;
import com.fieldnation.ui.HintArrayAdapter;
import com.fieldnation.ui.HintSpinner;
import com.fieldnation.v2.data.model.AttachmentFolder;
import com.fieldnation.v2.data.model.Shipment;
import com.fieldnation.v2.data.model.ShipmentCarrier;
import com.fieldnation.v2.data.model.Task;
import com.fieldnation.v2.data.model.WorkOrder;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import java.io.File;

public class ShipmentAddDialog extends SimpleDialog {
    private static final String TAG = "ShipmentAddDialog";

    // State
    private static final String STATE_CARRIER_SELECTION = "STATE_CARRIER_SELECTION";
    private static final String STATE_DIRECTION_SELECTION = "STATE_DIRECTION_SELECTION";
    private static final String STATE_SCANNED_IMAGE = "STATE_SCANNED_IMAGE";

    private static final int RESULT_CODE_BARCODE_SCAN = 0;

    // UI
    private TextView _titleTextView;
    private EditText _trackingIdEditText;
    private Button _scanButton;
    private HintSpinner _carrierSpinner;
    private EditText _carrierEditText;
    private TextInputLayout _carrierLayout;
    private EditText _descriptionEditText;
    private HintSpinner _directionSpinner;
    private Button _okButton;
    private Button _cancelButton;

    // Data
    private String _title = null;
    private WorkOrder _workOrder;
    private int _carrierPosition = -1;
    private int _directionPosition = -1;
    private String _shipmentDescription;
    private Task _task;
    private ActivityResultClient _activityResultClient;
    private PermissionsClient _permissionsClient;

    // Barcode stuff
    private Uri _scannedImageUri;

    // Modes
    private static final int CARRIER_FEDEX = 0;
    private static final int CARRIER_UPS = 1;
    private static final int CARRIER_USPS = 2;
    private static final int CARRIER_OTHER = 3;

    private CharSequence[] _carriers = null;

    /*-*************************************-*/
    /*-				Life Cycle				-*/
    /*-*************************************-*/
    public ShipmentAddDialog(Context context, ViewGroup container) {
        super(context, container);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, Context context, ViewGroup container) {
        View v = inflater.inflate(R.layout.dialog_v2_add_shipment, container, false);

        _titleTextView = (TextView) v.findViewById(R.id.title_textview);
        _trackingIdEditText = (EditText) v.findViewById(R.id.trackingid_edittext);
        _scanButton = (Button) v.findViewById(R.id.scanBarcode_button);
        _carrierSpinner = (HintSpinner) v.findViewById(R.id.carrier_spinner);
        _carrierEditText = (EditText) v.findViewById(R.id.carrier_edittext);
        _carrierLayout = (TextInputLayout) v.findViewById(R.id.carrier_layout);
        _descriptionEditText = (EditText) v.findViewById(R.id.description_edittext);
        _directionSpinner = (HintSpinner) v.findViewById(R.id.direction_spinner);
        _okButton = (Button) v.findViewById(R.id.ok_button);
        _cancelButton = (Button) v.findViewById(R.id.cancel_button);

        return v;
    }

    @Override
    public void onStart() {
        super.onStart();
        _carriers = App.get().getResources().getTextArray(R.array.carrier_list);

        _trackingIdEditText.setOnEditorActionListener(_onEditor);
        _scanButton.setOnClickListener(_scan_onClick);
        _carrierSpinner.setOnItemSelectedListener(_carrier_selected);
        _carrierEditText.setOnEditorActionListener(_onEditor);
        _descriptionEditText.setOnEditorActionListener(_onEditor);
        _directionSpinner.setOnItemSelectedListener(_direction_selected);
        _okButton.setOnClickListener(_okButton_onClick);
        _cancelButton.setOnClickListener(_cancel_onClick);

        getCarrierSpinner().setHint(App.get().getString(R.string.dialog_shipment_career_spinner_default_text));
        getCarrierSpinner().clearSelection();
        getDirectionSpinner().setHint(App.get().getString(R.string.dialog_shipment_direction_spinner_default_text));
        getDirectionSpinner().clearSelection();

        _permissionsClient = new PermissionsClient(_permissions_response);
        _permissionsClient.connect(App.get());
    }

    @Override
    public void onResume() {
        super.onResume();

        _activityResultClient = new ActivityResultClient(_activityResultClient_onListener);
        _activityResultClient.connect(App.get());

        if (_title != null) {
            _titleTextView.setText(_title);
        }
    }

    @Override
    public void show(Bundle payload, boolean animate) {
        _title = payload.getString("title");
        if (payload.containsKey("task"))
            _task = payload.getParcelable("task");
        if (payload.containsKey("description"))
            _shipmentDescription = payload.getString("description");

        _workOrder = payload.getParcelable("workOrder");

        populateUi();
        super.show(payload, animate);
    }

    @Override
    public void onRestoreDialogState(Bundle savedState) {
        super.onRestoreDialogState(savedState);
        if (savedState != null) {
            if (savedState.containsKey(STATE_CARRIER_SELECTION)) {
                _carrierPosition = savedState.getInt(STATE_CARRIER_SELECTION);
                getCarrierSpinner().setSelection(_carrierPosition);
                switchCarrierTextEdit();
            } else {
                getCarrierSpinner().clearSelection();
            }

            if (savedState.containsKey(STATE_DIRECTION_SELECTION)) {
                _directionPosition = savedState.getInt(STATE_DIRECTION_SELECTION);
                getDirectionSpinner().setSelection(_directionPosition);
            } else {
                getDirectionSpinner().clearSelection();
            }

            if (savedState.containsKey(STATE_SCANNED_IMAGE)) {
                _scannedImageUri = savedState.getParcelable(STATE_SCANNED_IMAGE);
            }
        }
    }

    @Override
    public void onSaveDialogState(Bundle outState) {
        super.onSaveDialogState(outState);

        if (_carrierPosition != -1)
            outState.putInt(STATE_CARRIER_SELECTION, _carrierPosition);

        if (_directionPosition != -1)
            outState.putInt(STATE_DIRECTION_SELECTION, _directionPosition);

        if (_scannedImageUri != null)
            outState.putParcelable(STATE_SCANNED_IMAGE, _scannedImageUri);
    }

    @Override
    public void onPause() {
        if (_activityResultClient != null) _activityResultClient.disconnect(App.get());
        super.onPause();
    }

    @Override
    public void onStop() {
        if (_permissionsClient != null) _permissionsClient.disconnect(App.get());
        super.onStop();
    }

    private void populateUi() {
        if (_cancelButton == null)
            return;

        switchCarrierTextEdit();
        getCarrierSpinner();

        if (_shipmentDescription != null) {
            _descriptionEditText.setText(_shipmentDescription);
        } else if (_task != null) {
            _descriptionEditText.setText(_task.getLabel());
        }

        getDirectionSpinner();
    }

    private void switchCarrierTextEdit() {
        switch (_carrierPosition) {
            case CARRIER_OTHER:
                _carrierLayout.setVisibility(View.VISIBLE);
                break;
            case CARRIER_FEDEX:
            case CARRIER_UPS:
            case CARRIER_USPS:
            default:
                _carrierLayout.setVisibility(View.GONE);
                break;
        }
    }

    private HintSpinner getCarrierSpinner() {
        if (_carrierSpinner != null && _carrierSpinner.getAdapter() == null) {
            HintArrayAdapter adapter = HintArrayAdapter.createFromResources(
                    getView().getContext(),
                    R.array.carrier_list,
                    R.layout.view_spinner_item);

            adapter.setDropDownViewResource(
                    android.support.design.R.layout.support_simple_spinner_dropdown_item);

            _carrierSpinner.setAdapter(adapter);
        }
        return _carrierSpinner;
    }

    private HintSpinner getDirectionSpinner() {
        if (_directionSpinner != null && _directionSpinner.getAdapter() == null) {
            HintArrayAdapter adapter = HintArrayAdapter.createFromResources(
                    getView().getContext(),
                    R.array.direction_list,
                    R.layout.view_spinner_item);

            adapter.setDropDownViewResource(
                    android.support.design.R.layout.support_simple_spinner_dropdown_item);

            _directionSpinner.setAdapter(adapter);
        }
        return _directionSpinner;
    }

    /*-*********************************-*/
    /*-				Events				-*/
    /*-*********************************-*/
    private final TextView.OnEditorActionListener _onEditor = new TextView.OnEditorActionListener() {
        @Override
        public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
            boolean handled = false;

            if (actionId == EditorInfo.IME_ACTION_NEXT) {
                if (v == _trackingIdEditText) {
                    if (_carrierLayout.getVisibility() == View.VISIBLE) {
                        _carrierEditText.requestFocus();
                        handled = true;
                    } else {
                        _descriptionEditText.requestFocus();
                        handled = true;
                    }
                } else if (v == _carrierEditText) {
                    _descriptionEditText.requestFocus();
                    handled = true;
                }
            }

            return handled;
        }
    };

    private final AdapterView.OnItemSelectedListener _carrier_selected = new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            _carrierPosition = position;
            switchCarrierTextEdit();
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {
            _carrierPosition = -1;
            switchCarrierTextEdit();
        }
    };

    private final AdapterView.OnItemSelectedListener _direction_selected = new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            _directionPosition = position;
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {
            _directionPosition = -1;
        }
    };

    private final View.OnClickListener _okButton_onClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (misc.isEmptyOrNull(_trackingIdEditText.getText().toString())) {
                ToastClient.toast(App.get(), App.get().getString(R.string.toast_missing_tracking_number), Toast.LENGTH_SHORT);
                return;
            }

            if (_carrierPosition == -1) {
                ToastClient.toast(App.get(), App.get().getString(R.string.toast_carrier_not_selected), Toast.LENGTH_SHORT);
                return;
            }

            if (misc.isEmptyOrNull(_descriptionEditText.getText().toString())) {
                ToastClient.toast(App.get(), App.get().getString(R.string.toast_missing_description), Toast.LENGTH_SHORT);
                return;
            }

            if (_directionPosition == -1) {
                ToastClient.toast(App.get(), App.get().getString(R.string.toast_direction_not_selected), Toast.LENGTH_SHORT);
                return;
            }

            if (_carrierPosition != CARRIER_OTHER && _carrierPosition != -1) {
                if (_carrierPosition == CARRIER_UPS && misc.getCarrierId(_trackingIdEditText.getText().toString()) != CARRIER_UPS) {
                    ToastClient.toast(App.get(), String.format(App.get().getString(R.string.toast_invalid_tracking_number), _carriers[_carrierPosition]), Toast.LENGTH_SHORT);
                    return;
                }
                if (_carrierPosition == CARRIER_FEDEX && misc.getCarrierId(_trackingIdEditText.getText().toString()) != CARRIER_FEDEX) {
                    ToastClient.toast(App.get(), String.format(App.get().getString(R.string.toast_invalid_tracking_number), _carriers[_carrierPosition]), Toast.LENGTH_SHORT);
                    return;
                }

                if (_carrierPosition == CARRIER_USPS && misc.getCarrierId(_trackingIdEditText.getText().toString()) != CARRIER_USPS) {
                    ToastClient.toast(App.get(), String.format(App.get().getString(R.string.toast_invalid_tracking_number), _carriers[_carrierPosition]), Toast.LENGTH_SHORT);
                    return;
                }
            } else {
                if (_carrierPosition == CARRIER_OTHER && misc.isEmptyOrNull(_carrierEditText.getText().toString())) {
                    ToastClient.toast(App.get(), App.get().getString(R.string.toast_missing_carrier_name), Toast.LENGTH_SHORT);
                    return;
                }
            }

            if (_task != null && _task.getId() != 0) {
                if (_carrierPosition == CARRIER_OTHER) {
                    uploadBarcodeImage();
                    _onOkDispatcher.dispatch(getUid(),
                            _trackingIdEditText.getText().toString(),
                            ShipmentCarrier.NameEnum.OTHER,
                            _carrierEditText.getText().toString(),
                            _descriptionEditText.getText().toString(),
                            _directionPosition == 0 ? Shipment.DirectionEnum.TO_SITE : Shipment.DirectionEnum.FROM_SITE,
                            _task.getId());
                } else {
                    uploadBarcodeImage();
                    _onOkDispatcher.dispatch(getUid(),
                            _trackingIdEditText.getText().toString(),
                            ShipmentCarrier.NameEnum.values()[_carrierPosition],
                            null,
                            _descriptionEditText.getText().toString(),
                            _directionPosition == 0 ? Shipment.DirectionEnum.TO_SITE : Shipment.DirectionEnum.FROM_SITE,
                            _task.getId());
                }

            } else {
                if (_carrierPosition == CARRIER_OTHER) {
                    uploadBarcodeImage();
                    _onOkDispatcher.dispatch(getUid(),
                            _trackingIdEditText.getText().toString(),
                            ShipmentCarrier.NameEnum.OTHER,
                            _carrierEditText.getText().toString(),
                            _descriptionEditText.getText().toString(),
                            _directionPosition == 0 ? Shipment.DirectionEnum.TO_SITE : Shipment.DirectionEnum.FROM_SITE,
                            0);
                } else {
                    uploadBarcodeImage();
                    _onOkDispatcher.dispatch(getUid(),
                            _trackingIdEditText.getText().toString(),
                            ShipmentCarrier.NameEnum.values()[_carrierPosition],
                            null,
                            _descriptionEditText.getText().toString(),
                            _directionPosition == 0 ? Shipment.DirectionEnum.TO_SITE : Shipment.DirectionEnum.FROM_SITE,
                            0);
                }
            }
            dismiss(true);
        }
    };

    private void uploadBarcodeImage() {
        if (_scannedImageUri == null)
            return;

        if (_workOrder.getAttachments() == null)
            return;

        if (_workOrder.getAttachments().getResults() == null || _workOrder.getAttachments().getResults().length == 0)
            return;

        AttachmentFolder[] folders = _workOrder.getAttachments().getResults();
        AttachmentFolder miscFolder = null;
        for (AttachmentFolder folder : folders) {
            if (folder.getType() == AttachmentFolder.TypeEnum.SLOT && folder.getActionsSet().contains(AttachmentFolder.ActionsEnum.UPLOAD)) {
                miscFolder = folder;
                break;
            }
        }
        if (miscFolder != null) {
            String fileName = FileUtils.getFileNameFromUri(App.get(), _scannedImageUri);
            WorkorderClient.uploadDeliverable(App.get(), _workOrder.getId(), miscFolder.getId(), fileName, _scannedImageUri);
        }

    }

    private final View.OnClickListener _cancel_onClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            dismiss(true);
            _onCancelDispatcher.dispatch(getUid());
        }
    };

    private final View.OnClickListener _scan_onClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            int grant = PermissionsClient.checkSelfPermission(App.get(), Manifest.permission.CAMERA);

            if (grant == PackageManager.PERMISSION_DENIED) {
                PermissionsClient.requestPermissions(App.get(), new String[]{Manifest.permission.CAMERA}, new boolean[]{false});
            } else {
                IntentIntegrator integrator = new IntentIntegrator((Activity) getContext());
                integrator.setPrompt(App.get().getString(R.string.dialog_scan_barcode_title));
                integrator.setCameraId(0);
                integrator.setBeepEnabled(false);
                integrator.setBarcodeImageEnabled(true);
                integrator.initiateScan();
            }
        }
    };

    public static void show(Context context, String uid, WorkOrder workOrder, String title, String description, Task task) {
        Bundle params = new Bundle();
        params.putParcelable("workOrder", workOrder);
        params.putString("title", title);
        params.putString("description", description);
        params.putParcelable("task", task);

        Controller.show(context, uid, ShipmentAddDialog.class, params);
    }

    private final PermissionsClient.ResponseListener _permissions_response = new PermissionsClient.ResponseListener() {
        @Override
        public PermissionsClient getClient() {
            return _permissionsClient;
        }

        @Override
        public void onComplete(String permission, int grantResult) {
            if (permission.equals(Manifest.permission.CAMERA)) {
                if (grantResult == PackageManager.PERMISSION_GRANTED) {
                    _scan_onClick.onClick(null);
                } else {
                    ToastClient.toast(App.get(), "Camera Access denied, can't scan barcode.", Toast.LENGTH_SHORT);
                }
            }
        }
    };

    private final ActivityResultClient.Listener _activityResultClient_onListener = new ActivityResultClient.ResultListener() {
        @Override
        public void onConnected() {
            _activityResultClient.subOnActivityResult();
        }

        @Override
        public void onEvent(String topicId, Parcelable payload) {
            Log.v(TAG, topicId);
            super.onEvent(topicId, payload);
        }

        @Override
        public ActivityResultClient getClient() {
            return _activityResultClient;
        }

        @Override
        public boolean onActivityResult(int requestCode, int resultCode, Intent data) {
            _activityResultClient.clearOnActivityResult();
            Log.v(TAG, "onActivityResult");

            IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);

            if (result != null) {
                Log.e(TAG, "onActivityResult: result not null");
                try {
                    _trackingIdEditText.setText(data.getStringExtra("SCAN_RESULT"));
                    String content = result.getContents();

                    if (content == null) {
                        Log.e(TAG, "onActivityResult: no image path");
                    } else {
                        Log.v(TAG, "onActivityResult");
                        _scannedImageUri = App.getUriFromFile(new File(result.getBarcodeImagePath()));
                        _trackingIdEditText.setText(content);
                        _carrierPosition = misc.getCarrierId(content);
                        getCarrierSpinner().setSelection(_carrierPosition);
                        populateUi();
                    }
                } catch (Exception ex) {
                    Log.v(TAG, ex);
                    populateUi();
                }
            }
            return true;
        }
    };

    /*-**********************-*/
    /*-         Ok           -*/
    /*-**********************-*/
    public interface OnOkListener {
        void onOk(String trackingId, ShipmentCarrier.NameEnum carrier, String carrierName, String description, Shipment.DirectionEnum directionEnum, int taskId);
    }

    private static KeyedDispatcher<OnOkListener> _onOkDispatcher = new KeyedDispatcher<OnOkListener>() {
        @Override
        public void onDispatch(OnOkListener listener, Object... parameters) {
            listener.onOk((String) parameters[0], (ShipmentCarrier.NameEnum) parameters[1], (String) parameters[2],
                    (String) parameters[3], (Shipment.DirectionEnum) parameters[4], (Integer) parameters[5]);
        }
    };

    public static void addOnOkListener(String uid, OnOkListener onOkListener) {
        _onOkDispatcher.add(uid, onOkListener);
    }

    public static void removeOnOkListener(String uid, OnOkListener onOkListener) {
        _onOkDispatcher.remove(uid, onOkListener);
    }

    public static void removeAllOnOkListener(String uid) {
        _onOkDispatcher.removeAll(uid);
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
