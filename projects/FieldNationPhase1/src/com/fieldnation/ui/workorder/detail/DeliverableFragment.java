package com.fieldnation.ui.workorder.detail;

import java.io.File;
import java.util.LinkedList;
import java.util.List;

import javax.net.ssl.ManagerFactoryParameters;

import org.apache.http.conn.ManagedClientConnection;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;

import com.fieldnation.GlobalState;
import com.fieldnation.R;
import com.fieldnation.auth.client.AuthenticationClient;
import com.fieldnation.data.profile.Profile;
import com.fieldnation.data.workorder.Deliverable;
import com.fieldnation.data.workorder.Workorder;
import com.fieldnation.json.JsonArray;
import com.fieldnation.json.JsonObject;
import com.fieldnation.rpc.client.ProfileService;
import com.fieldnation.rpc.client.WorkorderService;
import com.fieldnation.rpc.common.WebServiceConstants;
import com.fieldnation.rpc.common.WebServiceResultReceiver;
import com.fieldnation.ui.AppPickerDialog;
import com.fieldnation.ui.AppPickerPackage;
import com.fieldnation.ui.workorder.WorkorderFragment;

public class DeliverableFragment extends WorkorderFragment {
	private static final String TAG = "ui.workorder.detail.DeliverableFragment";

	private static final int RESULT_CODE_GET_ATTACHMENT = 1;

	private static final int WEB_GET_DOCUMENTS = 1;
	private static final int WEB_GET_PROFILE = 2;
	private static final int WEB_DELETE_DELIVERABLE = 3;

	// UI
	private ListView _listview;
	private Button _uploadButton;
	private AppPickerDialog _dialog;

	// Data
	private GlobalState _gs;
	private Workorder _workorder;
	private WorkorderService _service;
	private ProfileService _profileService;
	private Profile _profile = null;
	private List<Deliverable> _deliverables = null;
	private DeliverableListAdapter _adapter;

	/*-*************************************-*/
	/*-				LifeCycle				-*/
	/*-*************************************-*/
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		return inflater.inflate(R.layout.fragment_workorder_deliverables, container, false);
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);

		_gs = (GlobalState) getActivity().getApplicationContext();
		_gs.requestAuthentication(_authClient);

		_listview = (ListView) view.findViewById(R.id.listview);
		_adapter = new DeliverableListAdapter(_deliverableListener);
		_listview.setAdapter(_adapter);
		_uploadButton = (Button) view.findViewById(R.id.upload_button);
		_uploadButton.setOnClickListener(_upload_onClick);
	}

	@Override
	public void update() {
		getData();
	}

	@Override
	public void setWorkorder(Workorder workorder) {
		_workorder = workorder;
		getData();
	}

	private void getData() {
		if (_profileService != null && _profile == null)
			_gs.startService(_profileService.getMyUserInformation(WEB_GET_PROFILE, true));

		if (_service == null)
			return;

		if (_workorder == null || _workorder.getWorkorderId() == null)
			return;

		_gs.startService(_service.listDeliverables(WEB_GET_DOCUMENTS, _workorder.getWorkorderId(), false));
	}

	private void populateUi() {
		if (_deliverables == null)
			return;
		if (_profile == null)
			return;

		_adapter.setData(_profile.getUserId(), _deliverables);
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		if (_dialog == null) {
			_dialog = new AppPickerDialog(getActivity(), _dialog_listener);
			Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
			intent.setType("*/*");
			intent.addCategory(Intent.CATEGORY_OPENABLE);
			_dialog.addIntent(intent, "Get Content");

			if (getActivity().getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA)) {
				intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
				_dialog.addIntent(intent, "Take Picture");

			}

			_dialog.finish();
		}
	}

	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		Log.v(TAG, "onActivityResult() resultCode= " + resultCode);
		if (requestCode == RESULT_CODE_GET_ATTACHMENT) {
			Uri uri = Uri.parse(data.getData().toString());

			String[] projection = { MediaStore.Images.Media.DATA };
			Cursor cur = _gs.getContentResolver().query(uri, projection, null, null, null);
			cur.moveToFirst();
			String path = cur.getString(cur.getColumnIndex(MediaStore.Images.Media.DATA));
			File file = new File(path);

			Log.v(TAG, "BP");
		}
	};

	/*-*********************************-*/
	/*-				Events				-*/
	/*-*********************************-*/
	private AppPickerDialog.Listener _dialog_listener = new AppPickerDialog.Listener() {

		@Override
		public void onClick(AppPickerPackage pack) {
			// TODO Method Stub: onClick()
			Log.v(TAG, "Method Stub: onClick()");

		}
	};

	private View.OnClickListener _upload_onClick = new View.OnClickListener() {
		@Override
		public void onClick(View v) {
			_dialog.show();
		}
	};

	private DeliverableView.Listener _deliverableListener = new DeliverableView.Listener() {
		@Override
		public void onDelete(Deliverable deliverable) {
			_gs.startService(_service.deleteDeliverable(WEB_DELETE_DELIVERABLE, _workorder.getWorkorderId(),
					deliverable.getWorkorderUploadId()));
		}
	};
	private AuthenticationClient _authClient = new AuthenticationClient() {
		@Override
		public void onAuthentication(String username, String authToken) {
			_service = new WorkorderService(_gs, username, authToken, _resultReceiver);
			_profileService = new ProfileService(_gs, username, authToken, _resultReceiver);
			getData();
		}

		@Override
		public void onAuthenticationFailed(Exception ex) {
			_gs.requestAuthenticationDelayed(_authClient);
		}

		@Override
		public GlobalState getGlobalState() {
			return _gs;
		}
	};

	private WebServiceResultReceiver _resultReceiver = new WebServiceResultReceiver(new Handler()) {
		@Override
		public void onSuccess(int resultCode, Bundle resultData) {
			// TODO Method Stub: onSuccess()
			Log.v(TAG, "Method Stub: onSuccess()");
			if (resultCode == WEB_GET_DOCUMENTS) {
				_deliverables = null;
				try {
					_deliverables = new LinkedList<Deliverable>();

					String data = new String(resultData.getByteArray(WebServiceConstants.KEY_RESPONSE_DATA));
					JsonArray ja = new JsonArray(data);
					for (int i = 0; i < ja.size(); i++) {
						Deliverable deliverable = Deliverable.fromJson(ja.getJsonObject(i));
						_deliverables.add(deliverable);
					}
				} catch (Exception ex) {
					// TODO mulligan?
					ex.printStackTrace();
					_deliverables = null;
				}
			} else if (resultCode == WEB_GET_PROFILE) {
				_profile = null;
				try {
					_profile = Profile.fromJson(new JsonObject(new String(
							resultData.getByteArray(WebServiceConstants.KEY_RESPONSE_DATA))));
				} catch (Exception e) {
					// TODO mulligan?
					e.printStackTrace();
					_profile = null;
				}
			} else if (resultCode == WEB_DELETE_DELIVERABLE) {
				getData();
			}
			populateUi();
		}

		@Override
		public void onError(int resultCode, Bundle resultData, String errorType) {
			if (_service != null) {
				_gs.invalidateAuthToken(_service.getAuthToken());
			} else if (_profileService != null) {
				_gs.invalidateAuthToken(_profileService.getAuthToken());
			}
			_gs.requestAuthenticationDelayed(_authClient);
			_service = null;
			_profileService = null;
		}
	};

}
