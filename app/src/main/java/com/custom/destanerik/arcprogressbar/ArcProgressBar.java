package com.custom.destanerik.arcprogressbar;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

public class ArcProgressBar extends View
{
    private int mRadius;

    private int mBackgroundColor;
    private int mProgressColor;
    private int mProgressTextColor;
    private int mIndicantTextColor;

    private float mStrokeWidth;
    private float mBackgroundWidth;

    private int mMaxProgressValue;
    private int mProgressValue;

    private int mProgressTextSize;
    private int mIndicantTextSize;

    private String mProgressText;
    private String mProgressPrefix;
    private String mProgressSuffix;
    private String mIndicantText;

    private boolean mRoundedCorners;
    private boolean mEnabled;

    private Paint mProgressPaint;
    private Paint mBackgroundPaint;
    private Paint mProgressTextPaint;
    private Paint mIndicantTextPaint;

    private RectF mArcBounds;


    public ArcProgressBar(Context context)
    {
        super(context);
    }

    public ArcProgressBar(Context context, AttributeSet attrs)
    {
        super(context, attrs);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs)
    {
        if (attrs != null) {
            final TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.ArcProgressBar, 0, 0);

            mBackgroundColor = ta.getColor(R.styleable.ArcProgressBar_backgroundColor, Color.GRAY);
            mProgressColor = ta.getColor(R.styleable.ArcProgressBar_progressColor,Color.RED);
            mProgressTextColor = ta.getColor(R.styleable.ArcProgressBar_progressTextColor, Color.BLACK);
            mIndicantTextColor = ta.getColor(R.styleable.ArcProgressBar_indicantTextColor, Color.BLACK);
            mStrokeWidth = ta.getDimension(R.styleable.ArcProgressBar_strokeWidth, 10);
            mBackgroundWidth = ta.getDimension(R.styleable.ArcProgressBar_backgroundWidth, 10);
            mMaxProgressValue = ta.getInteger(R.styleable.ArcProgressBar_maxProgressValue, 100);
            mProgressValue = ta.getInteger(R.styleable.ArcProgressBar_progressValue, 40);
            mProgressTextSize = ta.getInteger(R.styleable.ArcProgressBar_progressTextSize, 20);
            mIndicantTextSize = ta.getInteger(R.styleable.ArcProgressBar_indicantTextSize, 16);
            mProgressText = ta.getString(R.styleable.ArcProgressBar_progressText);
            mProgressPrefix = ta.getString(R.styleable.ArcProgressBar_progressPrefix);
            mProgressSuffix = ta.getString(R.styleable.ArcProgressBar_progressSuffix);
            mIndicantText = ta.getString(R.styleable.ArcProgressBar_indicantText);
            mRoundedCorners = ta.getBoolean(R.styleable.ArcProgressBar_roundedCorners,false);
            mEnabled = ta.getBoolean(R.styleable.ArcProgressBar_enabled,true);
            ta.recycle();

            mArcBounds  = new RectF();

            mBackgroundPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
            mBackgroundPaint.setColor(mBackgroundColor);
            mBackgroundPaint.setStyle(Paint.Style.STROKE);
            mBackgroundPaint.setStrokeWidth(mBackgroundWidth);

            mProgressPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
            mProgressPaint.setColor(mProgressColor);
            mProgressPaint.setStyle(Paint.Style.STROKE);
            mProgressPaint.setStrokeWidth(mStrokeWidth);

            mProgressTextPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
            mProgressTextPaint.setColor(mProgressTextColor);
            mProgressTextPaint.setStyle(Paint.Style.FILL);
            mProgressTextPaint.setTextSize(mProgressTextSize);

            mIndicantTextPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
            mIndicantTextPaint.setColor(mIndicantTextColor);
            mIndicantTextPaint.setStyle(Paint.Style.FILL);
            mIndicantTextPaint.setTextSize(mIndicantTextSize);

            if(mRoundedCorners){
                mProgressPaint.setStrokeCap(Paint.Cap.ROUND);
                mBackgroundPaint.setStrokeCap(Paint.Cap.ROUND);
            }else{
                mProgressPaint.setStrokeCap(Paint.Cap.BUTT);
                mBackgroundPaint.setStrokeCap(Paint.Cap.BUTT);
            }
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int w = MeasureSpec.getSize(widthMeasureSpec);
        int h = MeasureSpec.getSize(heightMeasureSpec);

        int size = Math.min(w, h);
        setMeasuredDimension(size, size);

        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mRadius = Math.min(w, h);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        float width = (float) getWidth();
        float height = (float) getHeight();

        if (width > height) {
            mRadius = (int)(height / 2.5f);
        } else {
            mRadius = (int)(width / 2.5f);
        }

        float center_x = width / 2;
        float center_y = height / 2;

//        if (width > height) {
//            mRadius = (int)(height);
//        } else {
//            mRadius = (int)(width);
//        }
//
//        float center_x = width;
//        float center_y = height;

        float left = center_x - mRadius;
        float top = center_y - mRadius;
        float right = center_x + mRadius;
        float bottom = center_y + mRadius;

        mArcBounds.set(left, top, right, bottom);

        canvas.drawArc(mArcBounds, 135, 270, false, mBackgroundPaint);
        canvas.drawArc(mArcBounds, 135, ((float)mProgressValue / mMaxProgressValue) * 270, false, mProgressPaint);

        float progressTextHeight = mProgressTextPaint.descent() + mProgressTextPaint.ascent();
        float indicantTextHeight = mIndicantTextPaint.descent() + mIndicantTextPaint.ascent();

        canvas.drawText(mProgressText, (getWidth() - mProgressTextPaint.measureText(mProgressText)) / 2.0f, (getHeight() - progressTextHeight) / 2.0f, mProgressTextPaint);
        canvas.drawText(mIndicantText, (getWidth() - mIndicantTextPaint.measureText(mIndicantText)) / 2.0f, (getHeight() - indicantTextHeight) / 1.45f, mIndicantTextPaint);
    }
}
