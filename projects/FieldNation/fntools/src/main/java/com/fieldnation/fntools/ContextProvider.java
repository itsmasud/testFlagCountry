package com.fieldnation.fntools;

import android.content.Context;

/**
 * Created by Michael on 8/30/2016.
 */
public class ContextProvider {

    public static Provider _provider = null;

    public static void setProvider(Provider provider) {
        _provider = provider;
    }


    public static Context get() {
        if (_provider != null)
            return _provider.get();

        return null;
    }

    public interface Provider {
        Context get();
    }
}
