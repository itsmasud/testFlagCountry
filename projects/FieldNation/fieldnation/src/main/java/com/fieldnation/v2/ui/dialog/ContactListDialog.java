package com.fieldnation.v2.ui.dialog;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.fieldnation.R;
import com.fieldnation.fndialog.Controller;
import com.fieldnation.fndialog.FullScreenDialog;
import com.fieldnation.fntools.ForLoopRunnable;
import com.fieldnation.fntools.misc;
import com.fieldnation.ui.workorder.detail.ContactTileView;
import com.fieldnation.v2.data.model.Contact;
import com.fieldnation.v2.data.model.WorkOrder;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by mc on 6/6/17.
 */

public class ContactListDialog extends FullScreenDialog {
    private static final String TAG = "ContactListDialog";

    // Ui
    private LinearLayout _contactList;
    private Toolbar _toolbar;

    // Data
    private WorkOrder _workOrder;
    private ForLoopRunnable _contactsRunnable = null;

    public ContactListDialog(Context context, ViewGroup container) {
        super(context, container);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, Context context, ViewGroup container) {
        View v = inflater.inflate(R.layout.dialog_v2_contact_list, container, false);

        _toolbar = v.findViewById(R.id.toolbar);
        _toolbar.setTitle("Contacts");
        _toolbar.setNavigationIcon(R.drawable.back_arrow);

        _contactList = v.findViewById(R.id.contact_list);

        return v;
    }

    @Override
    public void onStart() {
        super.onStart();

        _toolbar.setNavigationOnClickListener(_back_onClick);
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void show(Bundle params, boolean animate) {
        super.show(params, animate);

        _workOrder = params.getParcelable("workOrder");

        final List<Contact> contactList = new LinkedList<>();

/*
        {
            Contact contact = _workOrder.getLocation().getSavedLocation().getContact();
            if (!misc.isEmptyOrNull(contact.getName()) && !misc.isEmptyOrNull(contact.getPhone())) {
                contactList.add(contact);
            }
        }
*/

        if (_workOrder.getContacts().getResults().length > 0) {
            for (Contact contact : _workOrder.getContacts().getResults()) {
                if (!misc.isEmptyOrNull(contact.getName()) && !misc.isEmptyOrNull(contact.getPhone())) {
                    contactList.add(contact);
                }
            }
        }

        if (contactList.size() > 0) {
            if (_contactsRunnable != null)
                _contactsRunnable.cancel();

            _contactList.removeAllViews();
            _contactsRunnable = new ForLoopRunnable(contactList.size(), new Handler()) {
                @Override
                public void next(int i) throws Exception {
                    ContactTileView v = new ContactTileView(getContext());
                    if (contactList.get(i) instanceof Contact) {
                        Contact contact = contactList.get(i);
                        v.setData(contact.getName(), contact.getPhone(), contact.getExt(), contact.getRole());
                    }
                    _contactList.addView(v);
                }
            };
            getView().postDelayed(_contactsRunnable, 100);
        }
        // TODO if contacts list is 0 then we should have never gotten here
    }

    private final View.OnClickListener _back_onClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            dismiss(true);
        }
    };

    public static void show(Context context, String uid, WorkOrder workOrder) {
        Bundle params = new Bundle();
        params.putParcelable("workOrder", workOrder);

        Controller.show(context, uid, ContactListDialog.class, params);
    }
}
