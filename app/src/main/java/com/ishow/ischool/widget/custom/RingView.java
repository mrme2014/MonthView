package com.ishow.ischool.widget.custom;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.LinearInterpolator;

import com.commonlib.util.LogUtil;

import static com.loc.c.j;

/**
 * Created by mini on 16/11/10.
 */

public class RingView extends View {

    private int radus;              //半径
    private int defalutSize = 280;       //默认大小
    private Paint mPaint;           //刻度画笔
    private Paint outPaint;         //外层刻度画笔
    private Paint numPaint;         //数字画笔
    private float progress = 0;
    private float mLastProgress = 0;
    private int defaultAlpha = 150;
    //总进度
    private float mTotalAngle = 210f;

    public RingView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public RingView(Context context) {
        super(context);
    }

    public RingView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

//    public RingView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
//        super(context, attrs, defStyleAttr, defStyleRes);
//    }

    void init() {
        defalutSize = dp_px(defalutSize);
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setColor(Color.WHITE);
        mPaint.setAlpha(50);//透明度，取值范围为0~255，数值越小越透明
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeWidth(5f);

        outPaint = new Paint();
        outPaint.setAntiAlias(true);
        outPaint.setColor(Color.WHITE);
        outPaint.setAlpha(defaultAlpha);//透明度，取值范围为0~255，数值越小越透明
        outPaint.setStyle(Paint.Style.STROKE);
        outPaint.setStrokeWidth(5f);

        numPaint = new Paint();
        numPaint.setAntiAlias(true);
        numPaint.setColor(Color.WHITE);
        numPaint.setTextSize(100);
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int wMode = MeasureSpec.getMode(widthMeasureSpec);
        int wSize = MeasureSpec.getSize(widthMeasureSpec);
        int hMode = MeasureSpec.getMode(heightMeasureSpec);
        int hSize = MeasureSpec.getSize(heightMeasureSpec);
        int width, height;
        if (wMode == MeasureSpec.EXACTLY) {
            width = wSize;
        } else {
            width = Math.min(wSize, defalutSize);
        }

        if (hMode == MeasureSpec.EXACTLY) {
            height = hSize;
        } else {
            height = Math.min(hSize, defalutSize);
        }
        radus = width / 2;
        setMeasuredDimension(width, height);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        //绘制刻度
        canvas.save();

        int pl = getPaddingTop();
        float sw = mPaint.getStrokeWidth();
        LogUtil.d("RingView", "pl = " + pl + "; sw = " + sw);

        //计算刻度线的起点结束点
        int startDst = (int) (getPaddingTop() - mPaint.getStrokeWidth() / 2);
        int endDst = (int) (startDst + 20);
        for (int i = 1; i <= 120; i++) {
            //每旋转4度绘制一个小刻度
            canvas.drawLine(radus, startDst, radus, endDst, mPaint);
            canvas.rotate(3, radus, radus);
        }
        canvas.restore();

        //绘制信用等级
        Rect rt1 = new Rect();
        if (mMinNum != 0) {
            numPaint.getTextBounds("12345678", 0, "12345678".length(), rt1);
            float textLen1 = numPaint.measureText(mMinNum + "");
            canvas.drawText(mMinNum + "", radus - textLen1 / 2, radus, numPaint);
        }

//        canvas.rotate(-60, radus, radus);
        if (progress - mLastProgress >= 3) {
            for (j = 1; j <= progress/3 + 1; j++) {
                //每旋转4度绘制一个小刻度
                outPaint.setAlpha(defaultAlpha + j);
                canvas.drawLine(radus, startDst, radus, endDst, outPaint);
                canvas.rotate(3, radus, radus);
            }
        }
    }

    /**
     * dp转px
     *
     * @param values
     * @return
     */
    public int dp_px(int values) {
        float density = getResources().getDisplayMetrics().density;
        return (int) (values * density + 0.5f);
    }


    // 最小数字
    private int mMinNum = 0;
    // 最大数字
    private int mMaxNum = 12000;

    //数字动画
    public void startAnim() {

        ValueAnimator mAngleAnim = ValueAnimator.ofFloat(progress, mTotalAngle);
        mAngleAnim.setInterpolator(new AccelerateDecelerateInterpolator());
        mAngleAnim.setDuration(3000);
        mAngleAnim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {

            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                progress = (float) valueAnimator.getAnimatedValue();
                LogUtil.d("progress = " + progress);
                postInvalidate();
            }
        });
        mAngleAnim.start();

        ValueAnimator mNumAnim = ValueAnimator.ofInt(mMinNum, mMaxNum);
        mNumAnim.setDuration(3000);
        mNumAnim.setInterpolator(new LinearInterpolator());
        mNumAnim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {

            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                mMinNum = (int) valueAnimator.getAnimatedValue();
                postInvalidate();
            }
        });
        mNumAnim.start();

    }

    public void setDefault(int value) {
        mMinNum = value;
        startAnim();
    }
}
