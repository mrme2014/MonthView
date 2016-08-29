package com.ishow.ischool.widget.custom;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.widget.ImageView;

import com.commonlib.util.UIUtil;
import com.ishow.ischool.R;
import com.ishow.ischool.util.AppUtil;
import com.ishow.ischool.util.ColorUtil;
import com.ishow.ischool.util.PicUtils;

/**
 * Created by abel on 16/8/25.
 */
public class AvatarImageView extends ImageView {

    private int textColor;
    private final RectF roundRect = new RectF();
    private String text;
    private final Paint maskPaint = new Paint();
    private final Paint zonePaint = new Paint();
    private Paint textPaint;
    private float textSize;
    private float textHeight;

    public AvatarImageView(Context context) {
        super(context);
        init();
    }

    public AvatarImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    public AvatarImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs);
    }

    private void init(AttributeSet attrs) {
        TypedArray typedArray = getContext().obtainStyledAttributes(attrs, R.styleable.AvatarImageView);
        text = typedArray.getString(R.styleable.AvatarImageView_text);
        textColor = typedArray.getColor(R.styleable.AvatarImageView_text_color, 0xFF333333);
        textSize = typedArray.getDimension(R.styleable.AvatarImageView_text_size, UIUtil.dip2px(getContext(), 14));

        textPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        textPaint.setColor(textColor);
        textPaint.setTextSize(textSize);
        textPaint.setTextAlign(Paint.Align.CENTER);
        Paint.FontMetrics fontMetrics = textPaint.getFontMetrics();
        textHeight = fontMetrics.descent - fontMetrics.ascent;

        init();
    }

    private void init() {
        maskPaint.setAntiAlias(true);
        maskPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        zonePaint.setAntiAlias(true);
        zonePaint.setColor(Color.WHITE);
    }

    @Deprecated
    /**
     * use setText(String text, int id, String url)
     */
    public void setText(String text) {
        this.text = AppUtil.getFirstChar(text);
        invalidate();
    }

    public void setText(String text, int id, String url) {
        if (!TextUtils.isEmpty(url)) {
            this.text = AppUtil.getFirstChar(text);
            setBackgroundColor(ColorUtil.getColorById(id));
        } else {
            PicUtils.loadpic(getContext(), this, url);
        }

        invalidate();
    }

    @Deprecated
    /**
     * use setText(String text, int id, String url)
     */
    public void setImageUrl(String url) {
        if (!TextUtils.isEmpty(url)) {
            PicUtils.loadpic(getContext(), this, url);
        }
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right,
                            int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        int w = getWidth();
        int h = getHeight();
        roundRect.set(0, 0, w, h);
    }

    @Override
    public void draw(Canvas canvas) {
        int w = getWidth();
        int h = getHeight();

        Paint.FontMetrics fontMetrics = textPaint.getFontMetrics();
        float textCenterVerticalBaselineY = h / 2 - fontMetrics.descent + (fontMetrics.descent - fontMetrics.ascent) / 2;

        canvas.saveLayer(roundRect, zonePaint, Canvas.ALL_SAVE_FLAG);
        canvas.drawRoundRect(roundRect, w / 2, h / 2, zonePaint);
        canvas.saveLayer(roundRect, maskPaint, Canvas.ALL_SAVE_FLAG);
        super.draw(canvas);
        canvas.restore();
        if (!TextUtils.isEmpty(text)) {
            canvas.drawText(text, w / 2, textCenterVerticalBaselineY, textPaint);
        }
    }

}
