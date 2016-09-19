package com.fieldnation.fndialog;

import android.os.Bundle;
import android.os.Parcelable;
import android.view.View;

/**
 * Created by Michael on 9/6/2016.
 * <p/>
 * The base Dialog interface. All views that want to be treated as a dialog must implement this
 * interface.
 */
public interface Dialog {
    void show(Bundle payload);

    void dismiss();

    View getView();

    Parcelable onSaveDialogState();

    void onRestoreDialogState(Parcelable savedState);
}
