package com.fieldnation.v2.ui;

import android.support.v7.widget.RecyclerView;

import com.fieldnation.fnlog.Log;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

/**
 * Created by mc on 3/6/17.
 */

public abstract class PagingAdapter<DataModel, ViewHolder extends RecyclerView.ViewHolder> extends RecyclerView.Adapter<ViewHolder> {
    private static final String TAG = "PagingAdapter";

    private List<List<DataModel>> _pages = new LinkedList<>();
    private Set<Integer> _loadingPages = new HashSet<>();
    private List<Object> _displayList = new LinkedList<>();

    private Class<DataModel> _objectType;
    private boolean _lastPage = false;


    public PagingAdapter(Class<DataModel> clazz) {
        super();
        _objectType = clazz;
        requestPage(1, false);
    }

    public Class<DataModel> getDataModelType() {
        return _objectType;
    }

    public void clear() {
        Log.v(TAG, "clear");
        _pages.clear();
        _displayList.clear();
        _loadingPages.clear();
        _lastPage = false;
        notifyDataSetChanged();
        rebuildList();
    }

    private void preRequestPage(int page, boolean allowCache) {
        if (_lastPage)
            return;

        if (!_loadingPages.contains(page)
                && (page >= _pages.size() || _pages.get(page) == null)) {
            _loadingPages.add(page);
            requestPage(page, allowCache);
        }
    }

    public void refreshAll() {
        if (_pages.size() == 0) {
            requestPage(1, false);
        } else {
            for (int i = 1; i < _pages.size(); i++) {
                _loadingPages.add(i);
                requestPage(i, false);
            }
        }
    }

    public void addObjects(int page, DataModel[] list) {
        if (list == null) {
            addObjects(page, (List<DataModel>) null);
            return;
        }

        List<DataModel> dest = new LinkedList<>();

        if (list.length > 0) {
            for (DataModel t : list) {
                dest.add(t);
            }
        }
        addObjects(page, dest);
    }

    public void addObjects(int page, List<DataModel> list) {
        _loadingPages.remove(page);

        if (list == null) {
            _lastPage = true;
            return;
        }
        if (page == 0 && list.size() == 0) {
            _pages.clear();
            rebuildList();
            return;
        }

        // we have a page, if there are not enough pages, we fill in the gaps with nulls
        while (page >= _pages.size())
            _pages.add(null);

        _pages.set(page, list);

        rebuildList();
    }

    final protected void rebuildList() {
        // Build the real list
        _displayList.clear();
        int location = 0;
        try {
            if (_pages.size() == 0 || _pages.get(1).size() == 0) {
                if (shouldInjectPlaceHolder(0))
                    _displayList.add(getInjectedPlaceHolder(location));

                _displayList.add(getEmptyPlaceHolder());
            } else {
                for (List<DataModel> page : _pages) {
                    if (page != null) {
                        for (DataModel t : page) {
                            if (shouldInjectPlaceHolder(location))
                                _displayList.add(getInjectedPlaceHolder(location));
                            location++;
                            _displayList.add(t);
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
    public void onBindViewHolder(ViewHolder holder, int position) {
        if (_pages.size() > 0 && getItemCount() > 0) {
            int itemsPerPage = (getItemCount() / _pages.size());
            if (itemsPerPage != 0 && position / itemsPerPage >= _pages.size() - 1) {
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

    public abstract void requestPage(int page, boolean allowCache);

    public abstract boolean shouldInjectPlaceHolder(int position);

    public abstract Object getInjectedPlaceHolder(int position);

    public abstract Object getEmptyPlaceHolder();
}