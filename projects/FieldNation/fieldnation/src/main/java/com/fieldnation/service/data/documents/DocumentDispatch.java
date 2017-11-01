package com.fieldnation.service.data.documents;

import android.content.Context;
import android.os.Bundle;

import com.fieldnation.fnpigeon.PigeonRoost;
import com.fieldnation.fnpigeon.Sticky;

import java.io.File;

/**
 * Created by Michael Carver on 5/28/2015.
 */
public class DocumentDispatch implements DocumentConstants {

    public static void download(Context context, long documentId, File file, int state, boolean isSync) {
        Bundle bundle = new Bundle();
        bundle.putString(PARAM_ACTION, PARAM_ACTION_DOWNLOAD_DOCUMENT);
        bundle.putLong(PARAM_DOCUMENT_ID, documentId);
        bundle.putBoolean(PARAM_IS_SYNC, isSync);

        bundle.putInt(PARAM_STATE, state);
        if (file != null)
            bundle.putSerializable(PARAM_FILE, file);

        PigeonRoost.sendMessage(ADDRESS_DOWNLOAD_DOCUMENT, bundle, Sticky.NONE);
    }
}
