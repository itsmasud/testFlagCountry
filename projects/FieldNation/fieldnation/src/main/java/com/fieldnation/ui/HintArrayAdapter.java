package com.fieldnation.ui;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.fieldnation.R;

import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by Michael on 5/25/2016.
 */
public class HintArrayAdapter extends ArrayAdapter<Object> {
    private CharSequence _hint = "";

    public HintArrayAdapter(Context context, int resource) {
        super(context, resource);
        super.add(_hint);
    }

    private HintArrayAdapter(Context context, int resource, List<Object> objects) {
        super(context, resource, objects);
        super.add(_hint);
    }

    public static HintArrayAdapter createFromResources(Context context, int textArrayResId, int textViewResId) {
        List<Object> list = new LinkedList<>();

        Collections.addAll(list, context.getResources().getTextArray(textArrayResId));

        return new HintArrayAdapter(context, textViewResId, list);
    }

    public static HintArrayAdapter createFromArray(Context context, Object[] array, int textViewResId) {
        List<Object> list = new LinkedList<>();

        Collections.addAll(list, array);

        return new HintArrayAdapter(context, textViewResId, list);
    }

    public void setHint(CharSequence hint) {
        if (hint == null)
            hint = "";

        _hint = hint;
        if (getCount() > 0)
            super.remove(getItem(getCount() - 1));
        super.add(hint);
    }

    @Override
    public void add(Object object) {
        if (getCount() > 0)
            super.remove(getItem(getCount() - 1));
        super.add(object);
        super.add(_hint);
    }

    @Override
    public void addAll(Collection<?> collection) {
        if (getCount() > 0)
            super.remove(getItem(getCount() - 1));
        super.addAll(collection);
        super.add(_hint);
    }

    @Override
    public void addAll(Object... items) {
        if (getCount() > 0)
            super.remove(getItem(getCount() - 1));
        super.addAll(items);
        super.add(_hint);
    }

    @Override
    public void clear() {
        super.clear();
        super.add(_hint);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = super.getView(position, convertView, parent);

        if (v instanceof TextView) {
            if (position == (getCount() - 1)) {
                ((TextView) v).setTextColor(getContext().getResources().getColor(R.color.fn_light_text));
            } else {
                ((TextView) v).setTextColor(getContext().getResources().getColor(R.color.fn_dark_text));
            }
        }

        return v;
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        View v = null;

        if (position == (getCount() - 1)) {
            TextView tv = new TextView(getContext());
            tv.setHeight(0);
            tv.setVisibility(View.GONE);
            v = tv;
        } else {
            v = super.getDropDownView(position, null, parent);
        }

        parent.setVerticalScrollBarEnabled(false);
        return v;
    }

}
