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
import com.fieldnation.fndialog.Controller;
import com.fieldnation.fndialog.SimpleDialog;
import com.fieldnation.fnlog.Log;
import com.fieldnation.ui.KeyedDispatcher;
import com.fieldnation.ui.v2.AppPickerAdapter;
import com.fieldnation.ui.v2.AppPickerPackage;
import com.fieldnation.ui.v2.AppPickerRowView;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

public class AppPickerDialog extends SimpleDialog {
    private static final String TAG = "AppPickerDialog";

    // Ui
    private ListView _items;
    private View _root;

    // Data
    private static final List<AppPickerPackage> _activityList = new LinkedList<>();
    private static final Set<String> _packages = new HashSet<>();

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
    public void onStart() {
        super.onStart();
        _items.setAdapter(new AppPickerAdapter(_activityList, _app_onClick));
    }


    private final AppPickerRowView.OnClickListener _app_onClick = new AppPickerRowView.OnClickListener() {
        @Override
        public void onClick(AppPickerRowView row, Intent intent) {
            _onOkDispatcher.dispatch(getUid(), intent);
            dismiss(true);
        }
    };


    public static void show(Context context, String uid) {
        Controller.show(context, uid, AppPickerDialog.class, null);
    }

    public static void dismiss(Context context) {
        Controller.dismiss(context, null);
    }


    /*-*********************************/
    /*-         Ok Listener           -*/
    /*-*********************************/
    public interface OnOkListener {
        void onOk(Intent pack);
    }

    private static KeyedDispatcher<OnOkListener> _onOkDispatcher = new KeyedDispatcher<OnOkListener>() {
        @Override
        public void onDispatch(OnOkListener listener, Object... parameters) {
            listener.onOk((Intent) parameters[0]);
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

}