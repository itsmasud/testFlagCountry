package com.fieldnation.ui.dialog;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;

import com.fieldnation.GlobalTopicClient;
import com.fieldnation.R;
import com.fieldnation.UniqueTag;

/**
 * Created by Michael Carver on 1/16/2015.
 */
public class UpdateDialog extends DialogFragmentBase {
    private final String TAG = UniqueTag.makeTag("UpdateDialog");

    // Ui
    private Button _okButton;

    // Data

    /*-*************************************-*/
    /*-				Life Cycle				-*/
    /*-*************************************-*/
    public static UpdateDialog getInstance(FragmentManager fm, String tag) {
        return getInstance(fm, tag, UpdateDialog.class);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NO_TITLE, 0);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);

        View v = inflater.inflate(R.layout.dialog_need_update, container, false);

        _okButton = (Button) v.findViewById(R.id.ok_button);
        _okButton.setOnClickListener(_ok_onClick);

        return v;
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);
        GlobalTopicClient.dispatchAppShutdown(getActivity());
    }

    private final View.OnClickListener _ok_onClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Uri marketUri = Uri.parse("market://details?id=com.fieldnation.android");
            startActivity(new Intent(Intent.ACTION_VIEW).setData(marketUri));
            dismiss();
        }
    };
}
