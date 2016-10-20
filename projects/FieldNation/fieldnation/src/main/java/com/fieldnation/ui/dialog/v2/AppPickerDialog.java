package com.fieldnation.ui.dialog.v2;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.fieldnation.App;
import com.fieldnation.R;
import com.fieldnation.fndialog.SimpleDialog;
import com.fieldnation.fnlog.Log;
import com.fieldnation.ui.v2.AppPickerAdapter;
import com.fieldnation.ui.v2.AppPickerPackage;
import com.fieldnation.ui.v2.AppPickerRowView;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

public class AppPickerDialog extends SimpleDialog {
    private static final String TAG = "AppPickerDialog";

    // Params
    private static final String PARAM_RESULT = "result";
    private static final int PARAM_RESULT_OK = 0;
    private static final String PARAM_VALUE = "value";


    // Ui
    private ListView _items;
    private View _root;

    // Data
    private static final List<AppPickerPackage> _activityList = new LinkedList<>();
    private static final Set<String> _packages = new HashSet<>();
//    private static Listener _listener;

    public AppPickerDialog(Context context, ViewGroup container) {
        super(context, container);
    }

    /*-*****************************-*/
    /*-         Life Cycle          -*/
    /*-*****************************-*/
    @Override
    public View onCreateView(LayoutInflater inflater, Context context, ViewGroup container) {
        _root = inflater.inflate(R.layout.dialog_v2_app_picker, container, false);
        _items = (ListView) _root.findViewById(R.id.apps_listview);
        return _root;
    }

    public static void addIntent(Intent intent, String postfix) {
        PackageManager pm = App.get().getPackageManager();
        List<ResolveInfo> infos = pm.queryIntentActivities(intent, 0);

        for (int i = 0; i < infos.size(); i++) {
            ResolveInfo info = infos.get(i);

            try {
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
            } catch (Exception ex) {
                Log.v(TAG, info.toString());
                Log.v(TAG, ex);
            }
        }
    }

    @Override
    public void onAdded() {
        super.onAdded();
        _items.setAdapter(new AppPickerAdapter(_activityList, _app_onClick));
    }


    private final AppPickerRowView.OnClickListener _app_onClick = new AppPickerRowView.OnClickListener() {
        @Override
        public void onClick(AppPickerRowView row, AppPickerPackage pack) {
            Bundle response = new Bundle();
            response.putInt(PARAM_RESULT, PARAM_RESULT_OK);
            response.putParcelable(PARAM_VALUE, pack);
            onResult(response);
            dismiss(true);
        }
    };


    public static class Controller extends com.fieldnation.fndialog.Controller {

        public Controller(Context context, String uid) {
            super(context, AppPickerDialog.class, uid);
        }

        public static void show(Context context, String uid) {
//            addIntent(intent, postfix);
            show(context, uid, AppPickerDialog.class, null);
        }

        public static void dismiss(Context context) {
            dismiss(context, null);
        }
    }


    public static abstract class ControllerListener implements com.fieldnation.fndialog.Controller.Listener {
        @Override
        public void onComplete(Bundle response) {
            switch (response.getInt(PARAM_RESULT)) {
                case PARAM_RESULT_OK:
                    Log.e(TAG, "inside PARAM_RESULT_OK");
                    onOk((AppPickerPackage) response.getParcelable(PARAM_VALUE));
                    break;
            }
        }

        public abstract void onOk(AppPickerPackage pack);

    }


}
