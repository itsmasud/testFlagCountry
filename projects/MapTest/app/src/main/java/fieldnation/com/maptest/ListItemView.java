package fieldnation.com.maptest;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * Created by Michael Carver on 3/25/2015.
 */
public class ListItemView extends RelativeLayout {

    private TextView _text;

    public ListItemView(Context context) {
        super(context);
        init();
    }

    public ListItemView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public ListItemView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        LayoutInflater.from(getContext()).inflate(R.layout.view_list_item, this);

        if (isInEditMode())
            return;

        _text = (TextView) findViewById(R.id.text_view);
    }

    public void setText(String text) {
        _text.setText(text);
    }
}
