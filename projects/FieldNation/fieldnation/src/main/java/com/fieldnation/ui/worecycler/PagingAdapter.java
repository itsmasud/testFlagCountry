package com.fieldnation.ui.worecycler;

import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import com.fieldnation.App;
import com.fieldnation.fnlog.Log;
import com.fieldnation.ui.RateMeView;

import java.util.HashSet;
import java.util.Hashtable;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

/**
 * Created by Michael on 10/4/2016.
 */

public abstract class PagingAdapter<T> extends RecyclerView.Adapter<BaseHolder> {
    private static final String TAG = "PagingAdapter";

    private static final Object RATEME = new Object();
    private static final Object HEADER = new Object();
    private static final Object EMPTY = new Object();

    private List<List<T>> _pages = new LinkedList<>();
    private Set<Integer> _loadingPages = new HashSet<>();
    private List<Object> _displayList = new LinkedList<>();

    private RateMeView _rateMeView = null;
    private Class<T> _objectType;
    private int _rateMePosition = 5;
    private boolean _showRateMe = false;


    public PagingAdapter(Class<T> clazz) {
        super();
        _objectType = clazz;
        requestPage(0, false);
    }

    public void clear() {
        Log.v(TAG, "clear");
        _pages.clear();
        _displayList.clear();
        _loadingPages.clear();
        notifyDataSetChanged();
        rebuildList();
        preRequestPage(0, false);
    }

    private void preRequestPage(int page, boolean allowCache) {
        if (!_loadingPages.contains(page)
                && (page >= _pages.size() || _pages.get(page) == null)) {
            _loadingPages.add(page);
            requestPage(page, allowCache);
        }
    }

    public void setRateMeAllowed(boolean allowed) {
        if (allowed) {
            _showRateMe = App.get().showRateMe();
        } else {
            _showRateMe = false;
        }
    }

    public void addObjects(int page, List<T> list) {
        _loadingPages.remove(page);

        if (list == null || list.size() == 0) {
            return;
        }

        // we have a page, if there are not enough pages, we fill in the gaps with nulls
        while (page >= _pages.size())
            _pages.add(null);

        _pages.set(page, list);

        rebuildList();
    }

    private void rebuildList() {
        // Build the real list
        _displayList.clear();
        if (useHeader())
            _displayList.add(HEADER);
        int location = 0;
        try {
            if (_pages.size() == 0 || _pages.get(0).size() == 0) {
                _displayList.add(EMPTY);
            } else {
                for (List<T> page : _pages) {
                    if (page != null) {
                        for (T t : page) {
                            location++;
                            _displayList.add(t);
                            if (location == _rateMePosition && _showRateMe)
                                _displayList.add(RATEME);
                        }
                    }
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
            case BaseHolder.TYPE_HEADER: {
                return onCreateHeaderViewHolder(parent);
            }
            case BaseHolder.TYPE_EMPTY: {
                return onCreateEmptyViewHolder(parent);
            }
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
            case BaseHolder.TYPE_HEADER: {
                onBindHeaderViewHolder(holder);
                break;
            }
            case BaseHolder.TYPE_EMPTY: {
                onBindEmptyViewHolder(holder);
                break;
            }
        }

        if (_pages.size() > 0 && getItemCount() > 0) {
            int itemsPerPage = (getItemCount() / _pages.size());
            if (position / itemsPerPage >= _pages.size() - 1) {
                preRequestPage(_pages.size(), false);
            }
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
        if (obj == HEADER) {
            return BaseHolder.TYPE_HEADER;
        } else if (obj == EMPTY) {
            return BaseHolder.TYPE_EMPTY;
        } else if (_objectType.isInstance(obj)) {
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

    public abstract BaseHolder onCreateHeaderViewHolder(ViewGroup parent);

    public abstract void onBindHeaderViewHolder(BaseHolder holder);

    public abstract BaseHolder onCreateEmptyViewHolder(ViewGroup parent);

    public abstract void onBindEmptyViewHolder(BaseHolder holder);

    public abstract boolean useHeader();
}
