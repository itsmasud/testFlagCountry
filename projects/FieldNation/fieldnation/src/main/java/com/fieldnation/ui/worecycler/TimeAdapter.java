package com.fieldnation.ui.worecycler;

import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

/**
 * Created by Michael on 4/7/2016.
 */
public abstract class TimeAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final long ONE_DAY = 86400000L;
    private static final long ONE_YEAR = ONE_DAY * 365;
    private static final long TWO_YEARS = ONE_YEAR * 2;

    private long _start = ((System.currentTimeMillis() - TWO_YEARS) / ONE_DAY);
    private long _end = ((System.currentTimeMillis() + TWO_YEARS) / ONE_DAY);
    private long _today = (System.currentTimeMillis() / ONE_DAY) - _start;

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return null;
    }

    public long timestampToPosition(long timestamp) {
        return (timestamp / ONE_DAY) - _start;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        onBindViewHolder(holder, position, (position + _start) * ONE_DAY);
    }

    public abstract void onBindViewHolder(RecyclerView.ViewHolder holder, int position, long timestamp);

    @Override
    public int getItemCount() {
        return (int) (_end - _start);
    }
}
