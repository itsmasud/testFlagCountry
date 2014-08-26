package com.fieldnation.ui.workorder.detail;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.LinkedList;
import java.util.List;

import javax.net.ssl.ManagerFactoryParameters;

import org.apache.http.conn.ManagedClientConnection;

import android.content.ComponentName;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.fieldnation.GlobalState;
import com.fieldnation.R;
import com.fieldnation.auth.client.AuthenticationClient;
import com.fieldnation.data.profile.Profile;
import com.fieldnation.data.workorder.Deliverable;
import com.fieldnation.data.workorder.Task;
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
import com.fieldnation.utils.misc;

public class DeliverableFragment extends WorkorderFragment {
	private static final String TAG = "ui.workorder.detail.DeliverableFragment";

	private static final int RESULT_CODE_GET_ATTACHMENT = 1;
	private static final int RESULT_CODE_GET_CAMERA_PIC = 2;

	private static final int WEB_GET_DOCUMENTS = 1;
	private static final int WEB_GET_PROFILE = 2;
	private static final int WEB_DELETE_DELIVERABLE = 3;
	private static final int WEB_SEND_DELIVERABLE = 4;
	private static final int WEB_GET_TASKS = 5;

	// UI
	private LinearLayout _reviewLayout;
	private LinearLayout _uploadLayout;
	private LinearLayout _filesLayout;
	private Button _uploadButton;
	private AppPickerDialog _dialog;

	// Data
	private GlobalState _gs;
	private Workorder _workorder;
	private WorkorderService _service;
	private ProfileService _profileService;
	private Profile _profile = null;
	private List<Deliverable> _deliverables = null;
	private List<Task> _tasks = null;

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

		_reviewLayout = (LinearLayout) view.findViewById(R.id.review_layout);
		_uploadLayout = (LinearLayout) view.findViewById(R.id.upload_layout);
		_filesLayout = (LinearLayout) view.findViewById(R.id.files_layout);
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
		_gs.startService(_service.getTasks(WEB_GET_TASKS, _workorder.getWorkorderId(), false));
	}

	private void populateUi() {
		if (_deliverables == null)
			return;
		if (_profile == null)
			return;
		if (_tasks == null)
			return;
		if (getActivity() == null)
			return;

		_reviewLayout.removeAllViews();
		_uploadLayout.removeAllViews();
		_filesLayout.removeAllViews();
		for (int i = 0; i < _deliverables.size(); i++) {
			Deliverable deliv = _deliverables.get(i);
			DeliverableView v = new DeliverableView(getActivity());
			v.setDeliverable(_profile.getUserId(), deliv);

			if (deliv.getUploadedBy().getUserId() == _profile.getUserId()) {
				boolean found = false;
				for (int j = 0; j < _tasks.size(); j++) {
					if (_tasks.get(j).getIdentifier() == null)
						continue;

					if (_tasks.get(j).getIdentifier().equals(deliv.getWorkorderUploadSlotId())) {
						found = true;
						break;
					}
				}
				if (found) {
					_uploadLayout.addView(v);
				} else {
					_filesLayout.addView(v);
				}
			} else {
				_reviewLayout.addView(v);
			}
		}

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

			try {
				// get temp path
				String packageName = _gs.getPackageName();
				File externalPath = Environment.getExternalStorageDirectory();
				File temppath = new File(externalPath.getAbsolutePath() + "/Android/data/" + packageName + "/temp");
				temppath.mkdirs();

				// create temp folder
				File tempfile = File.createTempFile("DATA", null, temppath);

				// copy the data
				InputStream in = _gs.getContentResolver().openInputStream(uri);
				OutputStream out = new FileOutputStream(tempfile);
				misc.copyStream(in, out, 1024, -1, 500);
				out.close();
				in.close();

				// send to the service
				_gs.startService(_service.uploadDeliverable(WEB_SEND_DELIVERABLE, _workorder.getWorkorderId(), 0L,
						tempfile));

			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		} else if (requestCode == RESULT_CODE_GET_CAMERA_PIC) {
			ContentResolver cr = getActivity().getContentResolver();
			String[] p1 = new String[] { MediaStore.Images.ImageColumns._ID, MediaStore.Images.ImageColumns.DATE_TAKEN };
			Cursor c1 = cr.query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, p1, null, null, p1[1] + " DESC");
			if (c1.moveToFirst()) {
				String uristringpic = "content://media/external/images/media/" + c1.getInt(0);
				Uri uri = Uri.parse(uristringpic);
				try {

					// find temp path
					String packageName = _gs.getPackageName();
					File externalPath = Environment.getExternalStorageDirectory();
					File temppath = new File(externalPath.getAbsolutePath() + "/Android/data/" + packageName + "/temp");
					temppath.mkdirs();

					// open temp file
					File tempfile = File.createTempFile("DATA", null, temppath);

					// write the image
					OutputStream out = new FileOutputStream(tempfile);
					Bitmap bm = android.provider.MediaStore.Images.Media.getBitmap(cr, uri);
					bm.compress(CompressFormat.PNG, 100, out);
					out.close();

					// send data to service
					_gs.startService(_service.uploadDeliverable(WEB_SEND_DELIVERABLE, _workorder.getWorkorderId(), 0L,
							tempfile));

				} catch (Exception ex) {
					ex.printStackTrace();
				}
			}
			c1.close();
			Log.v(TAG, "BP");
		}
	};

	/*-*********************************-*/
	/*-				Events				-*/
	/*-*********************************-*/
	private AppPickerDialog.Listener _dialog_listener = new AppPickerDialog.Listener() {

		@Override
		public void onClick(AppPickerPackage pack) {
			Intent src = pack.intent;

			ResolveInfo info = pack.resolveInfo;

			src.setComponent(new ComponentName(info.activityInfo.applicationInfo.packageName, info.activityInfo.name));

			if (src.getAction().equals(Intent.ACTION_GET_CONTENT)) {
				startActivityForResult(src, RESULT_CODE_GET_ATTACHMENT);
			} else {
				startActivityForResult(src, RESULT_CODE_GET_CAMERA_PIC);
			}
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
			} else if (resultCode == WEB_GET_TASKS) {
				_tasks = null;
				try {
					_tasks = new LinkedList<Task>();

					String data = new String(resultData.getByteArray(WebServiceConstants.KEY_RESPONSE_DATA));
					JsonArray ja = new JsonArray(data);
					for (int i = 0; i < ja.size(); i++) {
						Task task = Task.fromJson(ja.getJsonObject(i));
						_tasks.add(task);
					}
				} catch (Exception ex) {
					// TODO mulligan?
					ex.printStackTrace();
					_tasks = null;
				}
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
