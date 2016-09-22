package com.fieldnation.fndialog;

import android.os.Bundle;
import android.os.Parcelable;
import android.view.View;

/**
 * Created by Michael on 9/6/2016.
 * <p/>
 * The base Dialog interface. All views that want to be treated as a dialog must implement this
 * interface.
 * <p>
 * Dialogs should also contain a Controller object and Controller.Listener for users to use for
 * calling and listening for events from your dialog. See SimpleDialog and TwoButtonDialog for
 * examples.
 */
public interface Dialog {
    /**
     * @return the View that contains the dialog
     */
    View getView();

    /**
     * @return true if the dialog can be canceled, false if not
     */
    boolean isCancelable();

    /**
     * Called when the dialog should be displayed.
     *
     * @param params  the parameters that were passed through Client.show()
     * @param animate if true then animate the display. if false then skip the animation
     */
    void show(Bundle params, boolean animate);

    /**
     * Called instead of onSaveInstanceState() when the dialog is being restored from a screen
     * rotation or other similar event. If called, it will be called after show()
     *
     * @param savedState the state that was returned from onSaveDialogState()
     */
    void onRestoreDialogState(Parcelable savedState);

    /**
     * Called before dismiss if saving the state for screen orientation change or other similar
     * events
     *
     * @return The state of the dialog
     */
    Parcelable onSaveDialogState();

    /**
     * Called when the dialog should go away
     *
     * @param animate if true then animate, if false then skip the animation
     */
    void dismiss(boolean animate);

    /**
     * Called when the dialog is canceled. Usually triggered by the back button.
     */
    void cancel();
}
