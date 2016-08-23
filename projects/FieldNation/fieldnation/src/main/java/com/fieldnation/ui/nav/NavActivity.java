package com.fieldnation.ui.nav;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.fieldnation.R;
import com.fieldnation.data.profile.Profile;
import com.fieldnation.fnlog.Log;
import com.fieldnation.ui.AuthSimpleActivity;

/**
 * Created by Michael on 8/19/2016.
 */
public class NavActivity extends AuthSimpleActivity {
    private static final String TAG = "NavActivity";

    // Ui
    private RecyclerView _recyclerView;
    private Toolbar _toolbar;
//    private View _searchesView;

    @Override
    public int getLayoutResource() {
        return R.layout.activity_v3_nav;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        CoordinatorLayout _coordinatorLayout = (CoordinatorLayout) findViewById(R.id.main_content);

        _toolbar = (Toolbar) findViewById(R.id.toolbar);
        //_toolbar.setOnClickListener(_toolbar_onClick);

        //_searchesView = findViewById(R.id.searchesView);

        _recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        _recyclerView.setLayoutManager(new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL));
        _recyclerView.setAdapter(_recyclerView_adapter);

        //setTitle("");
        //((TextView) _toolbar.findViewById(R.id.textview)).setText("Field Nation \uE006");
    }

    @Override
    public void onFinishCreate(Bundle savedInstanceState) {
        setTitle(R.string.app_name);
    }

    @Override
    public int getToolbarId() {
        return R.id.toolbar;
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onProfile(Profile profile) {
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    // \u25B2 Up arrow
    // \u25BC down arrow
/*
    private final View.OnClickListener _toolbar_onClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (_searchesView.getVisibility() == View.GONE) {
                setTitle("Field Nation \u25B2");
                _searchesView.setVisibility(View.VISIBLE);
            } else {
                setTitle("Field Nation \u25BC");
                _searchesView.setVisibility(View.GONE);
            }
        }
    };
*/

    private class MyViewHolder extends RecyclerView.ViewHolder {

        public MyViewHolder(View itemView) {
            super(itemView);
        }
    }

    private final RecyclerView.Adapter<MyViewHolder> _recyclerView_adapter = new RecyclerView.Adapter<MyViewHolder>() {
        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new MyViewHolder(new TextView(parent.getContext()));
        }

        @Override
        public void onBindViewHolder(MyViewHolder holder, int position) {
            TextView v = (TextView) holder.itemView;

            v.setText(position + "");
        }

        @Override
        public int getItemCount() {
            return 100;
        }
    };

    public static void startNew(Context context) {
        Log.v(TAG, "startNew");
        // misc.printStackTrace("startNew");
        Intent intent = new Intent(context, NavActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }
}