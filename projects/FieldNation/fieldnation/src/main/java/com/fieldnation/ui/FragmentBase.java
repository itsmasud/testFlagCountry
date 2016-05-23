package com.fieldnation.ui;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;

import com.fieldnation.Log;

import java.util.List;

/**
 * Created by michael.carver on 12/2/2014.
 */
public class FragmentBase extends Fragment {
    private static final String TAG = "FragmentBase";

    // State
    private static final String STATE_TAG = "STATE_TAG";

    // Data
    protected FragmentManager _fm;
    protected String _tag;

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
    protected static <T extends FragmentBase> T getInstance(FragmentManager fm, String tag, Class<? extends FragmentBase> clazz) {
        T d = null;
        List<Fragment> frags = fm.getFragments();
        if (frags != null) {
            for (int i = 0; i < frags.size(); i++) {
                Fragment frag = frags.get(i);
                if (clazz.isInstance(frag)) {
                    if (((FragmentBase) frag)._tag.equals(tag)) {
                        d = (T) frag;
                        d._fm = fm;
                        d._tag = tag;
                        break;
                    }
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
                Log.v(TAG, ex);
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
    public void onSaveInstanceState(Bundle outState) {
        if (_tag != null)
            outState.putString(STATE_TAG, _tag);
        super.onSaveInstanceState(outState);
    }

}
