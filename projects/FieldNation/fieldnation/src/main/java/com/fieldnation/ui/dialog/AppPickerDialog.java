package com.fieldnation.ui.dialog;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ListView;

import com.fieldnation.R;
import com.fieldnation.ui.AppPickerAdapter;
import com.fieldnation.ui.AppPickerPackage;
import com.fieldnation.ui.AppPickerRowView;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

public class AppPickerDialog extends DialogFragmentBase {
    private static final String TAG = "ui.dialog.AppPickerDialog";

    // Ui
    private ListView _items;

    // Data
    private List<AppPickerPackage> _activityList = new LinkedList<AppPickerPackage>();
    private Set<String> _packages = new HashSet<String>();
    private Listener _listener;

    /*-*****************************-*/
    /*-         Life Cycle          -*/
    /*-*****************************-*/
    public static AppPickerDialog getInstance(FragmentManager fm, String tag) {
        return getInstance(fm, tag, AppPickerDialog.class);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setStyle(DialogFragment.STYLE_NO_TITLE, 0);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.dialog_app_picker, container, false);

        _items = (ListView) v.findViewById(R.id.apps_listview);

        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);

        return v;
    }

    public void addIntent(PackageManager pm, Intent intent, String postfix) {
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

    public void setListener(Listener listener) {
        _listener = listener;
    }

    @Override
    public void onResume() {
        super.onResume();
        _items.setAdapter(new AppPickerAdapter(_activityList, _app_onClick));
    }

    private final AppPickerRowView.OnClickListener _app_onClick = new AppPickerRowView.OnClickListener() {
        @Override
        public void onClick(AppPickerRowView row, AppPickerPackage pack) {
            dismiss();
            if (_listener != null)
                _listener.onClick(pack);
        }
    };

    public interface Listener {
        public void onClick(AppPickerPackage pack);
    }

}
