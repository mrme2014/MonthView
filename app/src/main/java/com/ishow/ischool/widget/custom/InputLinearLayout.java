package com.ishow.ischool.widget.custom;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.os.Build;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ishow.ischool.R;

/**
 * Created by wqf on 16/8/12.
 */
public class InputLinearLayout extends LinearLayout {

    private String labeltext, text, hinttext;
    private boolean isMore;
    private TextView label;
    private EditText input;
    private ImageView next;
    private View line;
    private boolean drawLine;

    public InputLinearLayout(Context context) {
        super(context);
    }

    public InputLinearLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public InputLinearLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public InputLinearLayout(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        TypedArray t = context.obtainStyledAttributes(attrs, R.styleable.InputLayout);
        labeltext = t.getString(R.styleable.InputLayout_label_text);
        text = t.getString(R.styleable.InputLayout_inputLayout_text);
        hinttext = t.getString(R.styleable.InputLayout_inputLayout_texthint);
        isMore = t.getBoolean(R.styleable.InputLayout_next, false);
        drawLine = t.getBoolean(R.styleable.InputLayout_bottom_line, true);
        t.recycle();
        View view = LayoutInflater.from(context).inflate(R.layout.view_inputlayout, null);
        LayoutParams params = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        params.gravity = Gravity.CENTER_VERTICAL;
        view.setLayoutParams(params);
        addView(view);
        label = (TextView) view.findViewById(R.id.label);
        input = (EditText) view.findViewById(R.id.input);
        next = (ImageView) view.findViewById(R.id.next);
        line = view.findViewById(R.id.bottom_line);
        if (isMore) {
            next.setVisibility(VISIBLE);
        }
        label.setText(labeltext);
        input.setHint(hinttext);
        input.setText(text);
        if (drawLine) {
            line.setVisibility(VISIBLE);
        }
    }

}
