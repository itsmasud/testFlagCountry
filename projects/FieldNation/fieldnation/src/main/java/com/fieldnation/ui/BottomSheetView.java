package com.fieldnation.ui;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.fieldnation.Log;
import com.fieldnation.R;

/**
 * Created by Michael Carver on 6/8/2015.
 */
public class BottomSheetView extends FrameLayout {
    private static final String TAG = "BottomSheetView";

    private RecyclerView _recyclerView;
    private StaggeredGridLayoutManager _layoutManager;

    public BottomSheetView(Context context) {
        super(context);
        init();
    }

    public BottomSheetView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public BottomSheetView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        LayoutInflater.from(getContext()).inflate(R.layout.view_bottom_sheet, this);

        if (isInEditMode())
            return;

        _recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        _recyclerView.setAdapter(_recyclerAdapter);
        _layoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        _recyclerView.setLayoutManager(_layoutManager);

        postDelayed(new Runnable() {
            @Override
            public void run() {
                try {
                    setTop(_recyclerView.getChildAt(4).getBottom());
                } catch (Exception ex) {
                    Log.v(TAG, ex);
                }
            }
        }, 1000);
    }

    private class MyViewHolder extends RecyclerView.ViewHolder {
        public MyViewHolder(View itemView) {
            super(itemView);
        }
    }

    private final RecyclerView.Adapter<MyViewHolder> _recyclerAdapter = new RecyclerView.Adapter<MyViewHolder>() {

        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup viewGroup, int position) {
            return new MyViewHolder(new CardView(getContext()));
        }

        @Override
        public void onBindViewHolder(MyViewHolder o, int position) {
        }

        @Override
        public int getItemCount() {
            return 100;
        }
    };

}
