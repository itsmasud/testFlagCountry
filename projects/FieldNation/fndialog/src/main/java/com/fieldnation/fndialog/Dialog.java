package com.fieldnation.fndialog;

import android.os.Bundle;

/**
 * Created by Michael on 9/6/2016.
 */
public interface Dialog {
    void onShow(Bundle payload);

    void onDismiss();
}
