package com.fieldnation.ui.workorder.detail;

import com.fieldnation.R;
import com.fieldnation.data.workorder.UploadSlot;
import com.fieldnation.data.workorder.UploadedDocument;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class UploadSlotView extends RelativeLayout {
	private static final String TAG = "ui.workorder.detail.UploadSlotView";

	// UI
	private TextView _titleTextView;
	private LinearLayout _docsList;

	// DATA
	private UploadSlot _slot;

	/*-*************************************-*/
	/*-				Life Cycle				-*/
	/*-*************************************-*/
	public UploadSlotView(Context context) {
		super(context);
		init();
	}

	public UploadSlotView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}

	public UploadSlotView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init();
	}

	private void init() {
		LayoutInflater.from(getContext()).inflate(R.layout.view_wd_uploadslot, this);

		if (isInEditMode())
			return;

		_titleTextView = (TextView) findViewById(R.id.title_textview);
		_docsList = (LinearLayout) findViewById(R.id.docs_list);
	}

	public void setUploadSlot(long profileId, UploadSlot slot) {
		_slot = slot;

		UploadedDocument[] docs = _slot.getUploadedDocuments();
		_titleTextView.setText(_slot.getSlotName());

		_docsList.removeAllViews();
		for (int i = 0; i < docs.length; i++) {
			UploadedDocument doc = docs[i];
			UploadedDocumentView v = new UploadedDocumentView(getContext());
			v.setDeliverable(profileId, doc);
			v.setListener(_deliverableView_listener);
			_docsList.addView(v);
		}
	}

	private UploadedDocumentView.Listener _deliverableView_listener = new UploadedDocumentView.Listener() {
		@Override
		public void onDelete(UploadedDocumentView v, UploadedDocument document) {
			// TODO Method Stub: onDelete()
			Log.v(TAG, "Method Stub: onDelete()");
		}
	};
}
