package com.fieldnation.v2.ui.dialog;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.fieldnation.R;
import com.fieldnation.fndialog.Controller;
import com.fieldnation.fndialog.FullScreenDialog;
import com.fieldnation.v2.data.model.Problem;

/**
 * Created by mc on 5/25/17.
 */

public class ResolveProblemDialog extends FullScreenDialog {
    private static final String TAG = "ResolveProblemDialog";

    // Ui
    private Toolbar _toolbar;
    private TextView _titleTextView;
    private TextView _detailTextView;
    private EditText _commentsEditText;

    // Data
    private Problem _problem;

    public ResolveProblemDialog(Context context, ViewGroup container) {
        super(context, container);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, Context context, ViewGroup container) {
        View v = inflater.inflate(R.layout.dialog_v2_resolve_problem, container);

        _toolbar = (Toolbar) v.findViewById(R.id.toolbar);
        _titleTextView = (TextView) v.findViewById(R.id.title_textview);
        _detailTextView = (TextView) v.findViewById(R.id.detail_textview);
        _commentsEditText = (EditText) v.findViewById(R.id.comments_edittext);

        return v;
    }

    @Override
    public void show(Bundle params, boolean animate) {
        _problem = params.getParcelable("problem");

        super.show(params, animate);
    }

    public static void show(Context context, String uid, Problem problem) {
        Bundle params = new Bundle();
        params.putParcelable("problem", problem);

        Controller.show(context, uid, ResolveProblemDialog.class, params);
    }
}