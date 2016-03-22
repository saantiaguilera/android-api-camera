package com.santiago.camera.mocks.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.santiago.camera.R;

/**
 * Created by santiago on 21/03/16.
 */
public class MockInputAspectRatioView extends LinearLayout {

    private EditText editText;
    private ImageView doneView;

    public MockInputAspectRatioView(Context context) {
        this(context, null);
    }

    public MockInputAspectRatioView(Context context, AttributeSet attrs) {
        super(context, attrs);

        inflate(context, R.layout.mock_view_input_aspect_ratio, this);

        setOrientation(HORIZONTAL);
        setGravity(Gravity.CENTER);

        editText = (EditText) findViewById(R.id.mock_view_aspect_ratio_edit_text);
        doneView = (ImageView) findViewById(R.id.mock_view_set_aspect_ratio_image_view);
    }

    public double getAspectRatio() {
        return Double.valueOf(editText.getText().toString());
    }

    public void setDoneViewClickListener(View.OnClickListener listener) {
        doneView.setOnClickListener(listener);
    }

}
