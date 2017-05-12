package com.fieldnation.fndialog;

import android.os.Bundle;
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
     * All implementations of Dialog must have a constructor of the following form so that
     * DialogManager can create it.
     * @param context
     * @param container
     */
    /**
     * public OneButtonDialog(Context context, ViewGroup container) {
     */

    /**
     * Called when the containing activity has been started
     */
    void onStart();

    /**
     * Called when the containing activity has been resumed, will be called after onStart()
     */
    void onResume();

    /**
     * Called when the dialog is about to be displayed. Will always be called after onResume()
     *
     * @param params  the parameters that were passed through Client.show()
     * @param animate if true then animate the display. if false then skip the animation
     */
    void show(Bundle params, boolean animate);

    /**
     * Called when the dialog is being restored from a screen rotation or other similar event. If
     * called, it will be called after show()
     *
     * @param savedState the state that was returned from onSaveDialogState()
     */
    void onRestoreDialogState(Bundle savedState);

    /**
     * Called when the dialog is canceled. Usually triggered by the back button.
     */
    void cancel();

    /**
     * Called before dismiss if saving the state for screen orientation change or other similar
     * events
     *
     * @return The state of the dialog
     */
    void onSaveDialogState(Bundle outState);

    /**
     * Called when the dialog should go away, should eventually trigger a call to
     * DismissListener.onDismissed() to indicate that the dialog is ready for deconstruction
     *
     * @param animate if true then animate, if false then skip the animation
     */
    void dismiss(boolean animate);

    /**
     * Called when the activity is pausing
     */
    void onPause();

    /**
     * Called when the activity is stopping
     */
    void onStop();

    String getUid();

    void setUid(String uid);

    /**
     * @return the View that contains the dialog
     */
    View getView();

    /**
     * @return true if the dialog can be canceled, false if not
     */
    boolean isCancelable();

    void setDismissListener(DismissListener listener);

    interface DismissListener {
        void onDismissed(Dialog dialog);
    }
}
