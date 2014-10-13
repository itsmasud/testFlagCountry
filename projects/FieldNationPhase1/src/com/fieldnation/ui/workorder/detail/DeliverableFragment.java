package com.fieldnation.ui.workorder.detail;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.security.SecureRandom;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.BaseColumns;
import android.provider.MediaStore;
import android.provider.OpenableColumns;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.fieldnation.GlobalState;
import com.fieldnation.R;
import com.fieldnation.auth.client.AuthenticationClient;
import com.fieldnation.data.profile.Profile;
import com.fieldnation.data.workorder.Document;
import com.fieldnation.data.workorder.UploadSlot;
import com.fieldnation.data.workorder.UploadedDocument;
import com.fieldnation.data.workorder.Workorder;
import com.fieldnation.json.JsonObject;
import com.fieldnation.rpc.client.ProfileService;
import com.fieldnation.rpc.client.WorkorderService;
import com.fieldnation.rpc.common.WebServiceConstants;
import com.fieldnation.rpc.common.WebServiceResultReceiver;
import com.fieldnation.ui.AppPickerPackage;
import com.fieldnation.ui.dialog.AppPickerDialog;
import com.fieldnation.ui.workorder.WorkorderActivity;
import com.fieldnation.ui.workorder.WorkorderFragment;
import com.fieldnation.utils.misc;

public class DeliverableFragment extends WorkorderFragment {
	private static final String TAG = "ui.workorder.detail.DeliverableFragment";

	private static final int RESULT_CODE_BASE = 100;
	private static final int RESULT_CODE_GET_ATTACHMENT = RESULT_CODE_BASE + 1;
	private static final int RESULT_CODE_GET_CAMERA_PIC = RESULT_CODE_BASE + 2;

	// private static final int WEB_GET_DOCUMENTS = 1;
	private static final int WEB_GET_PROFILE = 2;
	private static final int WEB_DELETE_DELIVERABLE = 3;
	private static final int WEB_SEND_DELIVERABLE = 4;

	// UI
	private LinearLayout _reviewList;
	private LinearLayout _filesLayout;
	private View _bar1View;
	private AppPickerDialog _dialog;
	private RelativeLayout _loadingLayout;
	private TextView _noDocsTextView;

	// Data
	private GlobalState _gs;
	private Workorder _workorder;
	private WorkorderService _service;
	private ProfileService _profileService;
	private Profile _profile = null;

	// private List<Deliverable> _deliverables = null;
	// private List<Task> _tasks = null;
	// private int _loadingCounter = 0;
	private SecureRandom _rand = new SecureRandom();

	// temporary storage
	private UploadSlot _uploadingSlot;
	private UploadSlotView _uploadingSlotView;
	private int _uploadCount = 0;
	private int _deleteCount = 0;

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

		_reviewList = (LinearLayout) view.findViewById(R.id.review_list);
		_filesLayout = (LinearLayout) view.findViewById(R.id.files_layout);
		_bar1View = view.findViewById(R.id.bar1_view);
		_loadingLayout = (RelativeLayout) view.findViewById(R.id.loading_layout);
		_noDocsTextView = (TextView) view.findViewById(R.id.nodocs_textview);

		checkMedia();
	}

	private boolean checkMedia() {
		if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {
			return true;
		} else {
			return false;
		}
	}

	// private void startLoading() {
	// if (_loadingCounter == 0) {
	// _loadingLayout.setVisibility(View.VISIBLE);
	// }
	// _loadingCounter++;
	// Log.v(TAG, "startLoading(" + _loadingCounter + ")");
	// }
	//
	// private void stopLoading() {
	// _loadingCounter--;
	// if (_loadingCounter <= 0) {
	// _loadingCounter = 0;
	// _loadingLayout.setVisibility(View.GONE);
	// }
	// Log.v(TAG, "stopLoading(" + _loadingCounter + ")");
	// }

	@Override
	public void update() {
		getData();
		checkMedia();
	}

	@Override
	public void setWorkorder(Workorder workorder) {
		_workorder = workorder;
		getData();

	}

	private PendingIntent getNotificationIntent() {
		Intent intent = new Intent(_gs, WorkorderActivity.class);
		intent.putExtra(WorkorderActivity.INTENT_FIELD_CURRENT_TAB, WorkorderActivity.TAB_DELIVERABLES);
		intent.putExtra(WorkorderActivity.INTENT_FIELD_WORKORDER_ID, _workorder.getWorkorderId());

		return PendingIntent.getActivity(_gs, _rand.nextInt(), intent, PendingIntent.FLAG_UPDATE_CURRENT);
	}

	private void getData() {
		if (_profileService == null)
			return;

		// startLoading();
		_profile = null;
		_gs.startService(_profileService.getMyUserInformation(WEB_GET_PROFILE, true));
	}

	private void populateUi() {
		if (_profile == null)
			return;

		if (_workorder == null)
			return;

		if (getActivity() == null)
			return;

		_reviewList.removeAllViews();
		Document[] docs = _workorder.getDocuments();
		if (docs != null && docs.length > 0) {
			for (int i = 0; i < docs.length; i++) {
				Document doc = docs[i];
				DocumentView v = new DocumentView(getActivity());
				_reviewList.addView(v);
				v.setDocument(doc);
			}
			_noDocsTextView.setVisibility(View.GONE);
		} else {
			_noDocsTextView.setVisibility(View.VISIBLE);
		}

		_filesLayout.removeAllViews();
		UploadSlot[] slots = _workorder.getUploadSlots();
		if (slots != null) {
			for (int i = 0; i < slots.length; i++) {
				UploadSlot slot = slots[i];
				UploadSlotView v = new UploadSlotView(getActivity());
				v.setUploadSlot(_profile.getUserId(), slot, _uploaded_document_listener);
				v.setListener(_uploadSlot_listener);
				_filesLayout.addView(v);
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

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		Log.v(TAG, "onActivityResult() resultCode= " + resultCode);
		if (requestCode == RESULT_CODE_GET_ATTACHMENT) {
			if (data == null)
				return;

			Uri uri = data.getData();
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

				Cursor c = _gs.getContentResolver().query(uri, null, null, null, null);
				int nameIndex = c.getColumnIndex(OpenableColumns.DISPLAY_NAME);
				c.moveToFirst();

				// send to the service
				// startLoading();
				_uploadingSlotView.addUploading(c.getString(nameIndex));

				_gs.startService(_service.uploadDeliverable(WEB_SEND_DELIVERABLE, _workorder.getWorkorderId(),
						_uploadingSlot.getSlotId(), c.getString(nameIndex), tempfile, getNotificationIntent()));
				c.close();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		} else if (requestCode == RESULT_CODE_GET_CAMERA_PIC) {
			ContentResolver cr = getActivity().getContentResolver();
			String[] p1 = new String[] { BaseColumns._ID, MediaStore.Images.ImageColumns.DATE_TAKEN };
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
					InputStream in = _gs.getContentResolver().openInputStream(uri);
					OutputStream out = new FileOutputStream(tempfile);
					misc.copyStream(in, out, 1024, -1, 500);
					out.close();
					in.close();

					Cursor c = _gs.getContentResolver().query(uri, null, null, null, null);
					int nameIndex = c.getColumnIndex(OpenableColumns.DISPLAY_NAME);
					c.moveToFirst();

					// send data to service
					// startLoading();
					_uploadingSlotView.addUploading(c.getString(nameIndex));
					_gs.startService(_service.uploadDeliverable(WEB_SEND_DELIVERABLE, _workorder.getWorkorderId(),
							_uploadingSlot.getSlotId(), c.getString(nameIndex), tempfile, getNotificationIntent()));

					c.close();
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

	private UploadedDocumentView.Listener _uploaded_document_listener = new UploadedDocumentView.Listener() {
		@Override
		public void onDelete(UploadedDocumentView v, UploadedDocument document) {
			_deleteCount++;
			// startLoading();
			_gs.startService(_service.deleteDeliverable(WEB_DELETE_DELIVERABLE, _workorder.getWorkorderId(),
					document.getWorkorderUploadId()));
		}
	};

	private UploadSlotView.Listener _uploadSlot_listener = new UploadSlotView.Listener() {
		@Override
		public void onUploadClick(UploadSlotView view, UploadSlot slot) {
			if (checkMedia()) {
				_uploadCount++;
				_uploadingSlot = slot;
				_uploadingSlotView = view;
				_dialog.show();
			} else {
				Toast.makeText(getActivity(), "Need External Storage, pleaswe insert storage device before continuing",
						Toast.LENGTH_LONG).show();
			}
		}
	};

	// Web
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
			// stopLoading();
			// TODO Method Stub: onSuccess()
			Log.v(TAG, "Method Stub: onSuccess()");
			if (resultCode == WEB_GET_PROFILE) {
				_profile = null;
				try {
					_profile = Profile.fromJson(new JsonObject(new String(
							resultData.getByteArray(WebServiceConstants.KEY_RESPONSE_DATA))));
				} catch (Exception e) {
					// TODO mulligan?
					e.printStackTrace();
					_profile = null;
				}
				populateUi();
			} else if (resultCode == WEB_DELETE_DELIVERABLE || resultCode == WEB_SEND_DELIVERABLE) {
				if (resultCode == WEB_DELETE_DELIVERABLE)
					_deleteCount--;

				if (resultCode == WEB_SEND_DELIVERABLE)
					_uploadCount--;

				if (_deleteCount < 0)
					_deleteCount = 0;
				if (_uploadCount < 0)
					_uploadCount = 0;

				// TODO, update individual UI elements when complete.
				if (_deleteCount == 0 && _uploadCount == 0)
					_workorder.dispatchOnChange();
			}
		}

		@Override
		public void onError(int resultCode, Bundle resultData, String errorType) {
			// stopLoading();
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
