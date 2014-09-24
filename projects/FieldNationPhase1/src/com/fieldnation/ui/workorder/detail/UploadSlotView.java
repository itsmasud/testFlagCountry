package com.fieldnation.ui.workorder.detail;

import com.fieldnation.R;
import com.fieldnation.data.workorder.UploadSlot;
import com.fieldnation.data.workorder.UploadedDocument;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class UploadSlotView extends RelativeLayout {
	private static final String TAG = "ui.workorder.detail.UploadSlotView";

	// UI
	private TextView _titleTextView;
	private LinearLayout _docsList;
	private TextView _uploadTextView;

	// DATA
	private UploadSlot _slot;
	private Listener _listener;

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
		_uploadTextView = (TextView) findViewById(R.id.upload_textview);
		_uploadTextView.setOnClickListener(_upload_onClick);
	}

	public void setUploadSlot(long profileId, UploadSlot slot, UploadedDocumentView.Listener listener) {
		_slot = slot;

		UploadedDocument[] docs = _slot.getUploadedDocuments();
		_titleTextView.setText(_slot.getSlotName());

		_docsList.removeAllViews();
		for (int i = 0; i < docs.length; i++) {
			UploadedDocument doc = docs[i];
			UploadedDocumentView v = new UploadedDocumentView(getContext());
			v.setDeliverable(profileId, doc);
			v.setListener(listener);
			_docsList.addView(v);
		}

		if ((slot.getMaxFiles() > 0 && docs.length >= slot.getMaxFiles())) {
			_uploadTextView.setVisibility(GONE);
		}
	}

	public void setListener(Listener listener) {
		_listener = listener;
	}

	/*-*************************-*/
	/*-			Events			-*/
	/*-*************************-*/
	private View.OnClickListener _upload_onClick = new View.OnClickListener() {
		@Override
		public void onClick(View v) {
			if (_listener != null)
				_listener.onUploadClick(UploadSlotView.this, _slot);
		}
	};

	public interface Listener {
		public void onUploadClick(UploadSlotView view, UploadSlot slot);
	}
}
