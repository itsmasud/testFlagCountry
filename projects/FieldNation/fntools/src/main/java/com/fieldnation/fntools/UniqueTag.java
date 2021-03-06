package com.fieldnation.fntools;

import java.util.Hashtable;

/**
 * Created by michael.carver on 12/23/2014.
 * Warning! Be careful using this with fragments that use their tags to index themselves in services, and dialogs.
 */
public class UniqueTag {
    private static final Object LOCK = new Object();

    private static final Hashtable<String, Integer> _tags = new Hashtable<>();

    public static String makeTag(String root) {
        return android.os.Process.myPid() + "/" + root + "/" + getTagNumber(root);
    }

    private static int getTagNumber(String root) {
        synchronized (LOCK) {
            if (!_tags.containsKey(root)) {
                _tags.put(root, 0);
            }

            int id = _tags.get(root) + 1;
            _tags.put(root, id);

            return id;
        }
    }
}
