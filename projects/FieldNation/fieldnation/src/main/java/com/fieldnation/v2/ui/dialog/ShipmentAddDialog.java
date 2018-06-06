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
import com.fieldnation.AppMessagingClient;
import com.fieldnation.R;
import com.fieldnation.analytics.CustomEvent;
import com.fieldnation.analytics.contexts.SpStackContext;
import com.fieldnation.analytics.contexts.SpStatusContext;
import com.fieldnation.analytics.contexts.SpTracingContext;
import com.fieldnation.analytics.contexts.SpUIContext;
import com.fieldnation.analytics.contexts.SpWorkOrderContext;
import com.fieldnation.analytics.trackers.UUIDGroup;
import com.fieldnation.analytics.trackers.WorkOrderTracker;
import com.fieldnation.fnactivityresult.ActivityResultListener;
import com.fieldnation.fnanalytics.Tracker;
import com.fieldnation.fndialog.Controller;
import com.fieldnation.fndialog.SimpleDialog;
import com.fieldnation.fnlog.Log;
import com.fieldnation.fnpermissions.PermissionsClient;
import com.fieldnation.fnpermissions.PermissionsResponseListener;
import com.fieldnation.fntoast.ToastClient;
import com.fieldnation.fntools.DebugUtils;
import com.fieldnation.fntools.FileUtils;
import com.fieldnation.fntools.KeyedDispatcher;
import com.fieldnation.fntools.misc;
import com.fieldnation.service.transaction.WebTransaction;
import com.fieldnation.ui.HintArrayAdapter;
import com.fieldnation.ui.HintSpinner;
import com.fieldnation.ui.share.SharedFile;
import com.fieldnation.v2.data.client.AttachmentHelper;
import com.fieldnation.v2.data.client.WorkordersWebApi;
import com.fieldnation.v2.data.model.Attachment;
import com.fieldnation.v2.data.model.AttachmentFolder;
import com.fieldnation.v2.data.model.AttachmentFolders;
import com.fieldnation.v2.data.model.Shipment;
import com.fieldnation.v2.data.model.ShipmentCarrier;
import com.fieldnation.v2.data.model.ShipmentTask;
import com.fieldnation.v2.data.model.Task;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import java.io.File;
import java.util.UUID;

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
    private int _workOrderId;
    private AttachmentFolders _attachmentFolders;
    private int _carrierPosition = -1;
    private int _directionPosition = -1;
    private String _shipmentDescription;
    private Task _task;
    private String _myUUID;

    // Barcode stuff
    private SharedFile _scannedFile;

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

        _titleTextView = v.findViewById(R.id.title_textview);
        _trackingIdEditText = v.findViewById(R.id.trackingid_edittext);
        _scanButton = v.findViewById(R.id.scanBarcode_button);
        _carrierSpinner = v.findViewById(R.id.carrier_spinner);
        _carrierEditText = v.findViewById(R.id.carrier_edittext);
        _carrierLayout = v.findViewById(R.id.carrier_layout);
        _descriptionEditText = v.findViewById(R.id.description_edittext);
        _directionSpinner = v.findViewById(R.id.direction_spinner);
        _okButton = v.findViewById(R.id.ok_button);
        _cancelButton = v.findViewById(R.id.cancel_button);

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

        _permissionsListener.sub();
    }

    @Override
    public void onResume() {
        super.onResume();

        _activityResultListener.sub();

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

        _workOrderId = payload.getInt("workOrderId");
        _attachmentFolders = payload.getParcelable("attachmentFolders");
        _myUUID = payload.getString("uuid");

        Tracker.event(App.get(), new CustomEvent.Builder()
                .addContext(new SpTracingContext(new UUIDGroup(null, _myUUID)))
                .addContext(new SpWorkOrderContext.Builder().workOrderId(_workOrderId).build())
                .addContext(new SpStackContext(DebugUtils.getStackTraceElement()))
                .addContext(new SpStatusContext(SpStatusContext.Status.START, "Shipment Dialog"))
                .build());

        WorkordersWebApi.getWorkOrder(App.get(), _workOrderId, true, WebTransaction.Type.NORMAL);

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
                _scannedFile = savedState.getParcelable(STATE_SCANNED_IMAGE);
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

        if (_scannedFile != null)
            outState.putParcelable(STATE_SCANNED_IMAGE, _scannedFile);

        if (_myUUID != null)
            outState.putString("uuid", _myUUID);
    }

    @Override
    public void onPause() {
        _activityResultListener.unsub();
        super.onPause();
    }

    @Override
    public void onStop() {
        Tracker.event(App.get(), new CustomEvent.Builder()
                .addContext(new SpTracingContext(new UUIDGroup(null, _myUUID)))
                .addContext(new SpWorkOrderContext.Builder().workOrderId(_workOrderId).build())
                .addContext(new SpStackContext(DebugUtils.getStackTraceElement()))
                .addContext(new SpStatusContext(SpStatusContext.Status.COMPLETE, "Shipment Dialog"))
                .build());

        _permissionsListener.unsub();
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
                ToastClient.toast(App.get(), R.string.toast_missing_tracking_number, Toast.LENGTH_SHORT);
                return;
            }

            if (_carrierPosition == -1) {
                ToastClient.toast(App.get(), R.string.toast_carrier_not_selected, Toast.LENGTH_SHORT);
                return;
            }

            if (misc.isEmptyOrNull(_descriptionEditText.getText().toString())) {
                ToastClient.toast(App.get(), R.string.toast_missing_description, Toast.LENGTH_SHORT);
                return;
            }

            if (_directionPosition == -1) {
                ToastClient.toast(App.get(), R.string.toast_direction_not_selected, Toast.LENGTH_SHORT);
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
                    saveShipment(
                            _trackingIdEditText.getText().toString(),
                            ShipmentCarrier.NameEnum.OTHER,
                            _carrierEditText.getText().toString(),
                            _descriptionEditText.getText().toString(),
                            _directionPosition == 0 ? Shipment.DirectionEnum.TO_SITE : Shipment.DirectionEnum.FROM_SITE,
                            _task.getId());
                } else {
                    uploadBarcodeImage();
                    saveShipment(
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
                    saveShipment(
                            _trackingIdEditText.getText().toString(),
                            ShipmentCarrier.NameEnum.OTHER,
                            _carrierEditText.getText().toString(),
                            _descriptionEditText.getText().toString(),
                            _directionPosition == 0 ? Shipment.DirectionEnum.TO_SITE : Shipment.DirectionEnum.FROM_SITE,
                            0);
                } else {
                    uploadBarcodeImage();
                    saveShipment(
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

    private void saveShipment(String trackingId, ShipmentCarrier.NameEnum carrier, String carrierName, String description, Shipment.DirectionEnum direction, int taskId) {
        WorkOrderTracker.onAddEvent(App.get(), WorkOrderTracker.WorkOrderDetailsSection.SHIPMENTS);

        try {
            ShipmentCarrier shipmentCarrier = new ShipmentCarrier();
            shipmentCarrier.name(carrier);
            if (carrier == ShipmentCarrier.NameEnum.OTHER)
                shipmentCarrier.other(carrierName);
            shipmentCarrier.tracking(trackingId);

            Shipment shipment = new Shipment();
            shipment.carrier(shipmentCarrier);
            shipment.name(description);
            shipment.direction(direction);

            if (taskId > 0)
                shipment.task(new ShipmentTask().id(taskId));

            SpUIContext uiContext = (SpUIContext) App.get().getSpUiContext().clone();
            uiContext.page += " - Shipment Add Dialog";
            WorkordersWebApi.addShipment(App.get(), _workOrderId, shipment, uiContext);

        } catch (Exception ex) {
            Log.v(TAG, ex);
        }
        if (App.get().getOfflineState() != App.OfflineState.OFFLINE
                && App.get().getOfflineState() != App.OfflineState.UPLOADING) {
            AppMessagingClient.setLoading(true);
        }
    }

    private void uploadBarcodeImage() {
        if (_scannedFile == null)
            return;

        if (_attachmentFolders == null)
            return;

        if (_attachmentFolders.getResults() == null || _attachmentFolders.getResults().length == 0)
            return;

        AttachmentFolder[] folders = _attachmentFolders.getResults();
        AttachmentFolder miscFolder = null;
        for (AttachmentFolder folder : folders) {
            if (folder.getType() == AttachmentFolder.TypeEnum.SLOT) {
                miscFolder = folder;
                if (folder.getName().equals("Misc"))
                    break;
            }
        }
        if (miscFolder != null) {
            try {
                Attachment attachment = new Attachment();
                attachment.folderId(miscFolder.getId())
                        .file(new com.fieldnation.v2.data.model.File().name(_scannedFile.getFileName()));

                AttachmentHelper.addAttachment(App.get(), _scannedFile.getUUID(), _workOrderId,
                        attachment, _scannedFile.getFileName(), _scannedFile.getUri(), true);
            } catch (Exception ex) {
                Log.v(TAG, ex);
            }
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
                PermissionsClient.requestPermissions(new String[]{Manifest.permission.CAMERA}, new boolean[]{false});
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


    private final PermissionsResponseListener _permissionsListener = new PermissionsResponseListener() {
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

    private final ActivityResultListener _activityResultListener = new ActivityResultListener() {
        @Override
        public boolean onActivityResult(int requestCode, int resultCode, Intent data) {
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
                        Uri imageUri = Uri.fromFile(new File(result.getBarcodeImagePath()));
                        _scannedFile = new SharedFile(_myUUID, FileUtils.getFileNameFromUri(App.get(), imageUri), imageUri);
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

    public static void show(Context context, String uid, int workOrderId, AttachmentFolders attachmentFolders, String title, String description, Task task) {
        Bundle params = new Bundle();
        params.putInt("workOrderId", workOrderId);
        params.putParcelable("attachmentFolders", attachmentFolders);
        params.putString("title", title);
        params.putString("description", description);
        params.putParcelable("task", task);
        params.putString("uuid", UUID.randomUUID().toString());

        Controller.show(context, uid, ShipmentAddDialog.class, params);
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
