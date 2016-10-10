package com.fieldnation.ui.worecycler;

import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import com.fieldnation.App;
import com.fieldnation.fnlog.Log;
import com.fieldnation.ui.RateMeView;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

/**
 * Created by Michael on 10/4/2016.
 */

public abstract class PagingAdapter<T> extends RecyclerView.Adapter<BaseHolder> {
    private static final String TAG = "PagingAdapter";

    private static final Object RATEME = new Object();

    private RateMeView _rateMeView = null;
    private List<T> _objects = new LinkedList<>();
    private Class<T> _objectType;
    private List<Object> _displayList = new LinkedList<>();
    private int _rateMePosition = 5;
    private boolean _showRateMe = false;
    private int _lastPage = 0;
    private boolean _onLastPage = false;


    public PagingAdapter(Class<T> clazz) {
        super();
        _objectType = clazz;
        requestPage(0, false);
    }

    public void clear() {
        Log.v(TAG, "clear");
        _objects.clear();
        _displayList.clear();
        _onLastPage = false;
        notifyDataSetChanged();
        _lastPage = 0;
        requestPage(0, false);
    }

    public void setRateMeAllowed(boolean allowed) {
        if (allowed) {
            _showRateMe = App.get().showRateMe();
        } else {
            _showRateMe = false;
        }
    }

    public void addObjects(int page, List<T> list) {
        Set<Integer> ids = new HashSet<>();

        if (list == null || list.size() == 0) {
            _onLastPage = true;
            return;
        }

        List<T> newList = new LinkedList<>();
        for (int i = 0; i < list.size(); i++) {
            ids.add(list.get(i).hashCode());
            newList.add(list.get(i));
        }

        for (int i = 0; i < _objects.size(); i++) {
            T object = _objects.get(i);

            if (!ids.contains(object.hashCode())) {
                ids.add(object.hashCode());
                newList.add(object);
            }
        }
        _objects = newList;

        rebuildList();
    }

    private void rebuildList() {
        // Build the real list
        _displayList.clear();
        try {
            _displayList.add(_objects.get(0));
            for (int i = 1; i < _objects.size(); i++) {
                T object = _objects.get(i);

                _displayList.add(object);

                if (i == _rateMePosition && _showRateMe) {
                    _displayList.add(RATEME);
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        notifyDataSetChanged();
    }

    @Override
    public BaseHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType) {
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
            case BaseHolder.TYPE_OBJECT: {
                onBindObjectViewHolder(holder, (T) _displayList.get(position));
                break;
            }
            case BaseHolder.TYPE_RATE_ME: {
                // RateMeHolder h = (RateMeHolder) holder;
                // RateMeView v = h.getView();
                // Nothing to do.. no data needed
                break;
            }
        }

        if (position == (getItemCount() * 9) / 10 && !_onLastPage) {
            _lastPage++;
            requestPage(_lastPage + 1, false);
        }
    }

    public Object getObject(int index) {
        return _displayList.get(index);
    }

    @Override
    public int getItemCount() {
        return _displayList.size();
    }

    @Override
    public int getItemViewType(int position) {
        Object obj = _displayList.get(position);
        if (_objectType.isInstance(obj)) {
            return BaseHolder.TYPE_OBJECT;
        } else {
            return BaseHolder.TYPE_RATE_ME;
        }
    }

    private final RateMeView.Listener _rateMe_listener = new RateMeView.Listener() {
        @Override
        public void onHide() {
            _showRateMe = false;
            rebuildList();
        }
    };

    public abstract void requestPage(int page, boolean allowCache);

    public abstract BaseHolder onCreateObjectViewHolder(ViewGroup parent, int viewType);

    public abstract void onBindObjectViewHolder(BaseHolder holder, T object);
}
