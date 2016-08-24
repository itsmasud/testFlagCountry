package com.fieldnation.ui.nav;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import com.fieldnation.R;
import com.fieldnation.data.profile.Profile;
import com.fieldnation.fnlog.Log;
import com.fieldnation.ui.AuthSimpleActivity;
import com.fieldnation.ui.IconFontTextView;

/**
 * Created by Michael on 8/19/2016.
 */
public class NavActivity extends AuthSimpleActivity {
    private static final String TAG = "NavActivity";

    // Ui
    private RecyclerView _recyclerView;
    private Toolbar _toolbar;
    private View _searchesView;
    private IconFontTextView _arrowTextView;

    // Animations
    private Animation _ccw;
    private Animation _cw;

    @Override
    public int getLayoutResource() {
        return R.layout.activity_v3_nav;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        _toolbar = (Toolbar) findViewById(R.id.toolbar);
        _toolbar.setOnClickListener(_toolbar_onClick);

        _arrowTextView = (IconFontTextView) findViewById(R.id.arrow_textview);

        _searchesView = findViewById(R.id.searchesView);
        _searchesView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideDrawer();
            }
        });

        _recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        _recyclerView.setLayoutManager(new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL));
        _recyclerView.setAdapter(_recyclerView_adapter);
        _recyclerView.addOnScrollListener(_recycler_onScroll);

        _ccw = AnimationUtils.loadAnimation(this, R.anim.rotate_180_ccw);
        _ccw.setAnimationListener(_ccw_animationListener);
        _cw = AnimationUtils.loadAnimation(this, R.anim.rotate_180_cw);
        _cw.setAnimationListener(_ccw_animationListener);
        hideDrawer();
    }

    private final Animation.AnimationListener _ccw_animationListener = new Animation.AnimationListener() {
        @Override
        public void onAnimationStart(Animation animation) {
        }

        @Override
        public void onAnimationEnd(Animation animation) {
            _ccw.cancel();
        }

        @Override
        public void onAnimationRepeat(Animation animation) {
        }
    };

    @Override
    public void onFinishCreate(Bundle savedInstanceState) {
    }

    @Override
    public int getToolbarId() {
        return R.id.toolbar;
    }

    @Override
    public void onProfile(Profile profile) {
    }

    private void showDrawer() {
        if (_searchesView.getVisibility() != View.VISIBLE) {
            Log.v(TAG, "showDrawer");
            setTitle("Field Nation");
            _searchesView.setVisibility(View.VISIBLE);
            _arrowTextView.startAnimation(_cw);
        }
    }

    private void hideDrawer() {
        if (_searchesView.getVisibility() != View.GONE) {
            Log.v(TAG, "hideDrawer");
            setTitle("Field Nation");
            _searchesView.setVisibility(View.GONE);
            _recyclerView.forceLayout();
            _arrowTextView.startAnimation(_ccw);
        }
    }

    private final View.OnClickListener _toolbar_onClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (_searchesView.getVisibility() == View.GONE) {
                showDrawer();
            } else {
                hideDrawer();
            }
        }
    };

    private final RecyclerView.OnScrollListener _recycler_onScroll = new RecyclerView.OnScrollListener() {
        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            hideDrawer();
        }
    };

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
            return 200;
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