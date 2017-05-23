package com.fieldnation.v2.ui.worecycler;

import android.view.ViewGroup;

import com.fieldnation.App;
import com.fieldnation.analytics.AnswersWrapper;
import com.fieldnation.analytics.SimpleEvent;
import com.fieldnation.fnanalytics.Tracker;
import com.fieldnation.fnlog.Log;
import com.fieldnation.ui.RateMeView;
import com.fieldnation.v2.data.model.WorkOrder;

/**
 * Created by mc on 3/7/17.
 */

public abstract class WoPagingAdapter extends com.fieldnation.v2.ui.PagingAdapter<WorkOrder, BaseHolder> {
    private static final String TAG = "WoPagingAdapter";

    private static final Object RATEME = new Object();
    private static final Object HEADER = new Object();
    private static final Object EMPTY = new Object();

    private RateMeView _rateMeView = null;
    private int _rateMePosition = 5;
    private boolean _showRateMe = false;

    public WoPagingAdapter() {
        super(WorkOrder.class);
    }

    public void setRateMeAllowed(boolean allowed) {
        if (allowed) {
            _showRateMe = App.get().showRateMe();
        } else {
            _showRateMe = false;
        }
    }

    @Override
    public boolean shouldInjectPlaceHolder(int position) {
        if (position == 0)
            return useHeader();

        if (_showRateMe)
            return position == _rateMePosition;

        return false;
    }

    @Override
    public Object getInjectedPlaceHolder(int position) {
        if (position == 0)
            return HEADER;

        if (_showRateMe)
            return RATEME;

        return null;
    }

    @Override
    public int getItemViewType(int position) {
        try {
            Object obj = getObject(position);
            if (obj == RATEME) {
                return BaseHolder.TYPE_RATE_ME;
            } else if (obj == HEADER) {
                return BaseHolder.TYPE_HEADER;
            } else if (obj == EMPTY) {
                return BaseHolder.TYPE_EMPTY;
            } else {
                return BaseHolder.TYPE_OBJECT;
            }
        } catch (Exception ex) {
            Log.v(TAG, ex);
            return -1;
        }
    }

    @Override
    public BaseHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType) {
            case BaseHolder.TYPE_EMPTY:
                return onCreateEmptyViewHolder(parent);

            case BaseHolder.TYPE_HEADER:
                return onCreateHeaderViewHolder(parent);

            case BaseHolder.TYPE_OBJECT:
                return onCreateObjectViewHolder(parent, viewType);

            case BaseHolder.TYPE_RATE_ME:
                if (_rateMeView == null) {
                    _rateMeView = new RateMeView(parent.getContext());
                    _rateMeView.setListener(_rateMe_listener);
                }
                return new RateMeHolder(_rateMeView);
        }
        return null;
    }

    @Override
    public void onBindViewHolder(BaseHolder holder, int position) {
        switch (holder.type) {
            case BaseHolder.TYPE_OBJECT:
                try {
                    onBindObjectViewHolder(holder, (WorkOrder) getObject(position));
                    break;
                } catch (Exception ex) {
                    Log.v(TAG, ex);
                }

            case BaseHolder.TYPE_RATE_ME:
                Tracker.event(App.get(),
                        new SimpleEvent.Builder()
                                .tag(AnswersWrapper.TAG)
                                .category("RateMeCardShown")
                                .build());
                App.get().setRateMeShown();
                break;

            case BaseHolder.TYPE_HEADER:
                onBindHeaderViewHolder(holder);
                break;

            case BaseHolder.TYPE_EMPTY:
                onBindEmptyViewHolder(holder);
                break;
        }

        super.onBindViewHolder(holder, position);
    }

    @Override
    public Object getEmptyPlaceHolder() {
        return EMPTY;
    }

    public abstract BaseHolder onCreateObjectViewHolder(ViewGroup parent, int viewType);

    public abstract void onBindObjectViewHolder(BaseHolder holder, WorkOrder object);

    public abstract BaseHolder onCreateHeaderViewHolder(ViewGroup parent);

    public abstract void onBindHeaderViewHolder(BaseHolder holder);

    public abstract BaseHolder onCreateEmptyViewHolder(ViewGroup parent);

    public abstract void onBindEmptyViewHolder(BaseHolder holder);

    public abstract boolean useHeader();

    private final RateMeView.Listener _rateMe_listener = new RateMeView.Listener() {
        @Override
        public void onHide() {
            _showRateMe = false;
            rebuildList();
        }
    };
}
