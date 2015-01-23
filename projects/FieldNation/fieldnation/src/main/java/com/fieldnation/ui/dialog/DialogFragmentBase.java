package com.fieldnation.ui.dialog;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

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
    protected boolean _isDismissed = true;
    protected boolean _reset = false;
    protected boolean _isShowing = false;

    /*-*********************************-*/
    /*-             Life Cycle          -*/
    /*-*********************************-*/

    /**
     * This should not be called by the application, but called by overriding dialogs.
     * <p/>
     * A typical call would look something like this:
     * <p/>
     * public static MyDialog getInstance(FragmentManager fm, String tag){
     * return getInstance(fm, tag, MyDialog.class);
     * }
     *
     * @param fm
     * @param tag
     * @param clazz
     * @param <T>
     * @return
     */
    protected static <T extends DialogFragmentBase> T getInstance(FragmentManager fm, String tag, Class<? extends DialogFragmentBase> clazz) {
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
                d = (T) clazz.getConstructor((Class<?>[]) null).newInstance((Object[]) null);
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

        if (_fm == null) {
            _fm = getFragmentManager();
        }
    }

    @Override
    public void onResume() {
        super.onResume();

        if (_reset) {
            reset();
            _reset = false;
        }

        if (_isShowing) {
            _isShowing = false;
            init();
        }
    }

    public void init() {
    }

    public void reset() {
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        if (_tag != null)
            outState.putString(STATE_TAG, _tag);
        super.onSaveInstanceState(outState);
    }

    /**
     * Do not use! use show();
     *
     * @param transaction
     * @param tag
     * @return
     */
    @Override
    public int show(FragmentTransaction transaction, String tag) {
        if (!_isDismissed)
            return -1;

        _reset = true;
        _isDismissed = false;
        _isShowing = true;

        return super.show(transaction, tag);
    }

    /**
     * Do not use! use show();
     *
     * @param manager
     * @param tag
     */
    @Override
    public void show(FragmentManager manager, String tag) {
        if (!_isDismissed)
            return;

        _reset = true;
        _isShowing = true;

        super.show(manager, tag);

        _isDismissed = false;
    }

    public void show() {
        if (!_isDismissed)
            return;

        _reset = true;
        _isShowing = true;

        super.show(_fm, _tag);
        _isDismissed = false;
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);
        _isDismissed = true;
    }
}
