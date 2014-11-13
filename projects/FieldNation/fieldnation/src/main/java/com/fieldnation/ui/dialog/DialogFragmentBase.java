package com.fieldnation.ui.dialog;

import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;

import java.util.List;

/**
 * Created by michael.carver on 11/13/2014.
 */
public class DialogFragmentBase extends DialogFragment {
    private static final String TAG = "ui.dialog.DialogFragmentBase";

    // State
    private static final String STATE_TAG = "STATE_TAG";

    // Data
    protected FragmentManager _fm;
    protected String _tag;

    /*-*********************************-*/
    /*-             Life Cycle          -*/
    /*-*********************************-*/
    public static <T extends DialogFragmentBase> T getInstance(FragmentManager fm, String tag, Class<? extends DialogFragmentBase> clazz) {
        T d = null;
        List<Fragment> frags = fm.getFragments();
        if (frags != null) {
            for (int i = 0; i < frags.size(); i++) {
                Fragment frag = frags.get(i);
                if (clazz.isInstance(frag) && frag.getTag().equals(tag)) {
                    d = (T) frag;
                    d._fm = fm;
                    d._tag = tag;
                    break;
                }
            }
        }
        if (d == null) {
            try {
                d = (T) clazz.getConstructor(null).newInstance(null);
                d._tag = tag;
                d._fm = fm;
                return d;
            } catch (Exception ex) {
                ex.printStackTrace();
                return null;
            }
        }
        return d;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        if (savedInstanceState != null) {
            if (savedInstanceState.containsKey(STATE_TAG))
                _tag = savedInstanceState.getString(STATE_TAG);
        }
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        if (_tag != null)
            outState.putString(STATE_TAG, _tag);
        super.onSaveInstanceState(outState);
    }

    public void show() {
        show(_fm, _tag);
    }
}
