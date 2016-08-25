package com.ishow.ischool.widget.custom;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.os.Build;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.commonlib.util.LogUtil;
import com.ishow.ischool.R;

/**
 * Created by wqf on 16/8/12.
 */
public class InputLinearLayout extends LinearLayout {
    private View mView;
    private String labeltext, text, hinttext;
    private boolean hasNext;
    private boolean isFocusable;
    private TextView label;
    private EditText input;
    private int lines;
    private ImageView next;
    private View line;
    private boolean drawLine;
    private GestureDetector mGestureDetector;
    private EidttextClick eidttextClick;

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
        mView = this;
        mGestureDetector = new GestureDetector(new Gesturelistener());
        TypedArray t = context.obtainStyledAttributes(attrs, R.styleable.InputLayout);
        labeltext = t.getString(R.styleable.InputLayout_label_text);
        text = t.getString(R.styleable.InputLayout_inputLayout_text);
        hinttext = t.getString(R.styleable.InputLayout_inputLayout_texthint);
        hasNext = t.getBoolean(R.styleable.InputLayout_next, false);
        drawLine = t.getBoolean(R.styleable.InputLayout_bottom_line, true);
        isFocusable = t.getBoolean(R.styleable.InputLayout_focusable, true);
        lines = t.getInteger(R.styleable.InputLayout_text_line, 1);
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
        if (hasNext) {
            next.setVisibility(VISIBLE);
            input.setFocusable(false);
            input.setLongClickable(false);
        }
        if (!isFocusable) {
            input.setEnabled(false);
        }
        if (lines > 1) {
            input.setSingleLine(false);
            input.setLines(lines);
        }
        label.setText(labeltext);
        input.setHint(hinttext);
        input.setText(text);
        if (drawLine) {
            line.setVisibility(VISIBLE);
        }

        input.setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (hasNext) {
                    return mGestureDetector.onTouchEvent(motionEvent);
                }
                return false;
            }
        });
    }

    private class Gesturelistener implements GestureDetector.OnGestureListener {

        public boolean onDown(MotionEvent e) {
            // TODO Auto-generated method stub
            return false;
        }

        public void onShowPress(MotionEvent e) {
            // TODO Auto-generated method stub
        }

        public boolean onSingleTapUp(MotionEvent e) {
            // TODO Auto-generated method stub
            input.clearFocus();
            LogUtil.d("InputLinearLayout click");
            eidttextClick.onEdittextClick(mView);
            return false;
        }

        public boolean onScroll(MotionEvent e1, MotionEvent e2,
                                float distanceX, float distanceY) {
            // TODO Auto-generated method stub
            return false;
        }

        public void onLongPress(MotionEvent e) {
            // TODO Auto-generated method stub
        }

        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
                               float velocityY) {
            // TODO Auto-generated method stub
            return false;
        }
    }

    public interface EidttextClick {
        void onEdittextClick(View view);
    }

    public void setOnEidttextClick(EidttextClick eidttextClick) {
        this.eidttextClick = eidttextClick;
    }

    public EditText getEdittext() {
        return input;
    }

    public void setContent(String content) {
        input.setText(content);
    }

    public String getContent() {
        return input.getText().toString();
    }


    /**
     * 设置edittextview无法点击
     */
    public void setDisable() {
        input.setEnabled(false);
    }
}
