package com.fieldnation.ui.dialog;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.fieldnation.R;
import com.fieldnation.ui.AppPickerAdapter;
import com.fieldnation.ui.AppPickerPackage;
import com.fieldnation.ui.AppPickerRowView;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

public class AppPickerDialog extends DialogFragment {
    private static final String TAG = "ui.AppPickerDialog";

    // Ui
    private ListView _items;

    // Data
    private List<AppPickerPackage> _activityList = new LinkedList<AppPickerPackage>();
    private Set<String> _packages = new HashSet<String>();
    private Listener _listener;
    private FragmentManager _fm;

    /*-*****************************-*/
    /*-         Life Cycle          -*/
    /*-*****************************-*/
    public static AppPickerDialog getInstance(FragmentManager fm, String tag) {
        AppPickerDialog d = null;
        List<Fragment> frags = fm.getFragments();
        if (frags != null) {
            for (int i = 0; i < frags.size(); i++) {
                Fragment frag = frags.get(i);
                if (frag instanceof AppPickerDialog && frag.getTag().equals(tag)) {
                    d = (AppPickerDialog) frag;
                    break;
                }
            }
        }
        if (d == null)
            d = new AppPickerDialog();
        d._fm = fm;
        return d;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.dialog_app_picker, container, false);

        _items = (ListView) v.findViewById(R.id.apps_listview);

        return v;
    }

    public void addIntent(Intent intent, String postfix) {
        PackageManager pm = getActivity().getPackageManager();

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

    public void show(String tag) {
        _items.setAdapter(new AppPickerAdapter(_activityList, _app_onClick));
        show(_fm, tag);
    }

    private AppPickerRowView.OnClickListener _app_onClick = new AppPickerRowView.OnClickListener() {
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
