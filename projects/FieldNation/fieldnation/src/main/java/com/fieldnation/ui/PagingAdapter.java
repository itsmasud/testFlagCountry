package com.fieldnation.ui;

import android.database.DataSetObserver;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.Enumeration;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.List;
import java.util.Set;

/**
 * Created by michael.carver on 11/21/2014.
 */
public abstract class PagingAdapter<T> extends BaseAdapter {
    private static final String TAG = "ui.PagingAdapter";

    private boolean _noMorePages = false;
    private Hashtable<Integer, List<T>> _pages = new Hashtable<Integer, List<T>>();
    private Set<Integer> _loadingPages = new HashSet<Integer>();
    private int _size;
    private Listener _listener;

    public void setPage(int page, List<T> items, boolean isCached) {
        Log.v(TAG, "setPage()");
        _pages.put(page, items);

        countItems();

        if (_loadingPages.contains(page)) {
            _loadingPages.remove(page);
        }

        // request an update if results are cached
        if (isCached)
            preRequestPage(page, false);

        if (_loadingPages.size() == 0 && _listener != null) {
            _listener.onLoadingComplete();
        }

        notifyDataSetChanged();
    }

    public void setListener(Listener listener) {
        _listener = listener;
    }

    public void refreshPages() {
        for (int i = 0; i < _pages.size(); i++) {
            preRequestPage(i, false);
        }
    }

    private void countItems() {
        int count = 0;
        Enumeration<Integer> e = _pages.keys();
        while (e.hasMoreElements()) {
            Integer i = e.nextElement();

            count += _pages.get(i).size();
        }

        _size = count;
    }

    public void setNoMorePages() {
        Log.v(TAG, "setNoMorePages()");
        _noMorePages = true;
        notifyDataSetChanged();
    }

    @Override
    public void registerDataSetObserver(DataSetObserver observer) {
        Log.v(TAG, "registerDataSetObserver()");
        super.registerDataSetObserver(observer);
        if (!_noMorePages && !_loadingPages.contains(_pages.size())) {
            requestPage(_pages.size(), true);
        }
    }

    @Override
    public int getCount() {
        Log.v(TAG, "getCount()");
        return _size;
    }

    @Override
    public Object getItem(int position) {
        Log.v(TAG, "getItem()");

        // find the page that has this item
        int count = 0;
        List<T> page = null;
        for (int i = 0; i < _pages.size(); i++) {
            page = _pages.get(i);
            count = count + page.size();
            if (count > position)
                break;
        }

        // if  not last page, and there could be more pages then request the next page
        if (_pages.get(_pages.size() - 1) == page && !_noMorePages) {
            preRequestPage(_pages.size(), true);
        }

        // find the begining of the page
        count = count - page.size();
        // pull out the item
        return page.get(position - count);
    }

    @Override
    public long getItemId(int position) {
        Log.v(TAG, "getItemId()");
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Log.v(TAG, "getView()");

        // find the page
        int count = 0;
        List<T> page = null;
        int page_num = 0;
        for (page_num = 0; page_num < _pages.size(); page_num++) {
            page = _pages.get(page_num);
            count = count + page.size();
            if (count > position)
                break;
        }

        // if  not last page, and there could be more pages then request the next page
        if (_pages.get(_pages.size() - 1) == page && !_noMorePages) {
            preRequestPage(_pages.size(), true);
        }


        // find the begining of the page
        count = count - page.size();
        // get the page
        T obj = page.get(position - count);

        // create the view
        return getView(page_num, position - count, obj, convertView, parent);
    }

    private void preRequestPage(int page, boolean allowCache) {
        if (!_loadingPages.contains(page)) {
            Log.v(TAG, "preRequestPage(), requesting");
            _loadingPages.add(page);
            requestPage(page, allowCache);
            return;
        }
        Log.v(TAG, "preRequestPage(), skipping");
    }

    public abstract View getView(int page, int position, T object, View convertView, ViewGroup parent);

    public abstract void requestPage(int page, boolean allowCache);

    public interface Listener {
        public void onLoadingComplete();
    }

}
