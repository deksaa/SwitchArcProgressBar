package com.custom.destanerik;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import com.custom.destanerik.arcprogressbar.R;

import java.util.ArrayList;
import java.util.List;

public class ArcProgressBar extends View
{
    private final int START_ANGLE = 135;
    private final int END_ANGLE = 270;

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

    private OnClickListener listener;

    private ArrayList<ArcProgressModel> displayedValues;

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

            this.setClickable(mEnabled);

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

        float left = center_x - mRadius;
        float top = center_y - mRadius;
        float right = center_x + mRadius;
        float bottom = center_y + mRadius;

        mArcBounds.set(left, top, right, bottom);

        canvas.drawArc(mArcBounds, START_ANGLE, END_ANGLE, false, mBackgroundPaint);
        canvas.drawArc(mArcBounds, START_ANGLE, calculateSweepAngle(), false, mProgressPaint);

        float progressTextHeight = mProgressTextPaint.descent() + mProgressTextPaint.ascent();
        float indicantTextHeight = mIndicantTextPaint.descent() + mIndicantTextPaint.ascent();

        canvas.drawText(mProgressText, (getWidth() - mProgressTextPaint.measureText(mProgressText)) / 2.0f, (getHeight() - progressTextHeight) / 2.0f, mProgressTextPaint);
        canvas.drawText(mIndicantText, (getWidth() - mIndicantTextPaint.measureText(mIndicantText)) / 2.0f, (getHeight() - indicantTextHeight) / 1.4f, mIndicantTextPaint);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        if(event.getAction() == MotionEvent.ACTION_UP) {
            if(listener != null)
                listener.onClick(this);
        }
        return super.dispatchTouchEvent(event);
    }

    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        if(event.getAction() == KeyEvent.ACTION_UP && (event.getKeyCode() == KeyEvent.KEYCODE_DPAD_CENTER || event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) {
            if(listener != null)
                listener.onClick(this);
        }
        return super.dispatchKeyEvent(event);
    }

    private float calculateSweepAngle(){
        return ((float)mProgressValue / mMaxProgressValue) * END_ANGLE;
    }

    public void setOnClickListener(OnClickListener listener) {
        this.listener = listener;
    }

    public int getmBackgroundColor() {
        return mBackgroundColor;
    }

    public void setmBackgroundColor(int mBackgroundColor) {
        this.mBackgroundColor = mBackgroundColor;
        invalidate();
    }

    public int getmProgressColor() {
        return mProgressColor;
    }

    public void setmProgressColor(int mProgressColor) {
        this.mProgressColor = mProgressColor;
        invalidate();
    }

    public int getmProgressTextColor() {
        return mProgressTextColor;
    }

    public void setmProgressTextColor(int mProgressTextColor) {
        this.mProgressTextColor = mProgressTextColor;
        invalidate();
    }

    public int getmIndicantTextColor() {
        return mIndicantTextColor;
    }

    public void setmIndicantTextColor(int mIndicantTextColor) {
        this.mIndicantTextColor = mIndicantTextColor;
        invalidate();
    }

    public float getmStrokeWidth() {
        return mStrokeWidth;
    }

    public void setmStrokeWidth(float mStrokeWidth) {
        this.mStrokeWidth = mStrokeWidth;
        invalidate();
    }

    public float getmBackgroundWidth() {
        return mBackgroundWidth;
    }

    public void setmBackgroundWidth(float mBackgroundWidth) {
        this.mBackgroundWidth = mBackgroundWidth;
        invalidate();
    }

    public int getmMaxProgressValue() {
        return mMaxProgressValue;
    }

    public void setmMaxProgressValue(int mMaxProgressValue) {
        this.mMaxProgressValue = mMaxProgressValue;
        invalidate();
    }

    public int getmProgressValue() {
        return mProgressValue;
    }

    public void setmProgressValue(int mProgressValue) {
        this.mProgressValue = mProgressValue;
        invalidate();
    }

    public int getmProgressTextSize() {
        return mProgressTextSize;
    }

    public void setmProgressTextSize(int mProgressTextSize) {
        this.mProgressTextSize = mProgressTextSize;
        invalidate();
    }

    public int getmIndicantTextSize() {
        return mIndicantTextSize;
    }

    public void setmIndicantTextSize(int mIndicantTextSize) {
        this.mIndicantTextSize = mIndicantTextSize;
        invalidate();
    }

    public String getmProgressText() {
        return mProgressText;
    }

    public void setmProgressText(String mProgressText) {
        this.mProgressText = mProgressText;
        invalidate();
    }

    public String getmProgressPrefix() {
        return mProgressPrefix;
    }

    public void setmProgressPrefix(String mProgressPrefix) {
        this.mProgressPrefix = mProgressPrefix;
        invalidate();
    }

    public String getmProgressSuffix() {
        return mProgressSuffix;
    }

    public void setmProgressSuffix(String mProgressSuffix) {
        this.mProgressSuffix = mProgressSuffix;
        invalidate();
    }

    public String getmIndicantText() {
        return mIndicantText;
    }

    public void setmIndicantText(String mIndicantText) {
        this.mIndicantText = mIndicantText;
        invalidate();
    }

    public boolean ismRoundedCorners() {
        return mRoundedCorners;
    }

    public void setmRoundedCorners(boolean mRoundedCorners) {
        this.mRoundedCorners = mRoundedCorners;
        invalidate();
    }

    public boolean ismEnabled() {
        return mEnabled;
    }

    public void setmEnabled(boolean mEnabled) {
        this.mEnabled = mEnabled;
        invalidate();
    }

    public Paint getmProgressPaint() {
        return mProgressPaint;
    }

    public void setmProgressPaint(Paint mProgressPaint) {
        this.mProgressPaint = mProgressPaint;
        invalidate();
    }

    public Paint getmBackgroundPaint() {
        return mBackgroundPaint;
    }

    public void setmBackgroundPaint(Paint mBackgroundPaint) {
        this.mBackgroundPaint = mBackgroundPaint;
        invalidate();
    }

    public Paint getmProgressTextPaint() {
        return mProgressTextPaint;
    }

    public void setmProgressTextPaint(Paint mProgressTextPaint) {
        this.mProgressTextPaint = mProgressTextPaint;
        invalidate();
    }

    public Paint getmIndicantTextPaint() {
        return mIndicantTextPaint;
    }

    public void setmIndicantTextPaint(Paint mIndicantTextPaint) {
        this.mIndicantTextPaint = mIndicantTextPaint;
        invalidate();
    }

    public List<ArcProgressModel> getDisplayedValues() {
        return displayedValues;
    }

    public void setDisplayedValues(ArrayList<ArcProgressModel> displayedValues) {
        this.displayedValues = displayedValues;
    }
}
