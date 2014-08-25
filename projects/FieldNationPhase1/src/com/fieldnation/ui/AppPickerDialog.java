package com.fieldnation.ui;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;

public class AppPickerDialog {
	private static final String TAG = "com.fieldnation.ui.AppPickerDialog";

	private List<AppPickerPackage> _activityList = new LinkedList<AppPickerPackage>();
	private Set<String> _packages = new HashSet<String>();
	private Activity _activity;
	private AlertDialog.Builder _builder;
	private AppPickerAdapter _adapter;
	private Listener _listener;

	public AppPickerDialog(Activity activity, Listener listener) {
		_activity = activity;
		_listener = listener;
	}

	public void addIntent(Intent intent, String postfix) {
		PackageManager pm = _activity.getPackageManager();

		List<ResolveInfo> infos = pm.queryIntentActivities(intent, 0);

		for (int i = 0; i < infos.size(); i++) {
			ResolveInfo info = infos.get(i);

			if (!_packages.contains(info.activityInfo.packageName + "." + info.activityInfo.name)) {
				AppPickerPackage data = new AppPickerPackage();

				data.postfix = postfix;
				data.intent = intent;
				data.resolveInfo = info;
				data.appName = pm.getApplicationLabel(info.activityInfo.applicationInfo).toString();
				data.icon = info.loadIcon(pm);

				_activityList.add(data);
				_packages.add(info.activityInfo.packageName + "." + info.activityInfo.name);
			}
		}
	}

	public void finish() {
		_adapter = new AppPickerAdapter(_activityList);
		_builder = new AlertDialog.Builder(_activity);
		_builder.setNegativeButton("Cancel", null);
		_builder.setSingleChoiceItems(_adapter, -1, _dialog_onClick);
	}

	public void show() {
		_builder.show();
	}

	private DialogInterface.OnClickListener _dialog_onClick = new DialogInterface.OnClickListener() {
		@Override
		public void onClick(DialogInterface dialog, int which) {
			_listener.onClick(_activityList.get(which));
			dialog.dismiss();
		}
	};

	public interface Listener {
		public void onClick(AppPickerPackage pack);
	}

}
