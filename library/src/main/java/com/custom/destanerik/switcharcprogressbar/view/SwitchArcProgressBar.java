package com.custom.destanerik.switcharcprogressbar.view;

import android.animation.AnimatorSet;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.LinearInterpolator;

import com.custom.destanerik.switcharcprogressbar.model.SwitchArcProgressModel;
import com.custom.destanerik.switcharcprogressbar.R;
import com.custom.destanerik.switcharcprogressbar.utils.TextUtils;

import java.util.ArrayList;
import java.util.List;

public class SwitchArcProgressBar extends View implements ValueAnimator.AnimatorUpdateListener
{
    private Context mContext;

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

    private int mCurrentIndex;

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

    private OnClickListener mClickListener;

    public OnChangedValueListener mValuelistener;

    private ArrayList<SwitchArcProgressModel> mDisplayedValues;

    public interface OnChangedValueListener{
        void onChangedValue(SwitchArcProgressBar bar, SwitchArcProgressModel model);
    }

    public SwitchArcProgressBar(Context context)
    {
        super(context);
        mContext = context;
    }
    public SwitchArcProgressBar(Context context, AttributeSet attrs)
    {
        super(context, attrs);
        init(context, attrs);
        mContext = context;

        this.setOnClickListener(new OnClickListener()
        {
            ValueAnimator valueAnimatorFadeIn;
            ValueAnimator valueAnimatorFadeOut;

            @Override
            public void onClick(View view)
            {
                valueAnimatorFadeIn = ValueAnimator.ofInt(255,0);
                valueAnimatorFadeIn.setDuration(100);
                valueAnimatorFadeIn.setInterpolator(new LinearInterpolator());
                valueAnimatorFadeIn.addUpdateListener(SwitchArcProgressBar.this);

                valueAnimatorFadeOut = ValueAnimator.ofInt(1,255);
                valueAnimatorFadeOut.setDuration(400);
                valueAnimatorFadeOut.setInterpolator(new LinearInterpolator());
                valueAnimatorFadeOut.addUpdateListener(SwitchArcProgressBar.this);

                AnimatorSet animatorSet = new AnimatorSet();
                animatorSet.playSequentially(valueAnimatorFadeIn,valueAnimatorFadeOut);
                animatorSet.start();
            }
        });
    }
    private void init(Context context, AttributeSet attrs)
    {
        if (attrs != null) {
            final TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.SwitchArcProgressBar, 0, 0);

            mBackgroundColor = ta.getColor(R.styleable.SwitchArcProgressBar_backgroundColor, Color.GRAY);
            mProgressColor = ta.getColor(R.styleable.SwitchArcProgressBar_progressColor,Color.RED);
            mProgressTextColor = ta.getColor(R.styleable.SwitchArcProgressBar_progressTextColor, Color.BLACK);
            mIndicantTextColor = ta.getColor(R.styleable.SwitchArcProgressBar_indicantTextColor, Color.BLACK);
            mStrokeWidth = ta.getDimension(R.styleable.SwitchArcProgressBar_strokeWidth, 10);
            mBackgroundWidth = ta.getDimension(R.styleable.SwitchArcProgressBar_backgroundWidth, 10);
            mMaxProgressValue = ta.getInteger(R.styleable.SwitchArcProgressBar_maxProgressValue, 100);
            mProgressValue = ta.getInteger(R.styleable.SwitchArcProgressBar_progressValue, 0);
            mProgressTextSize = ta.getDimensionPixelSize(R.styleable.SwitchArcProgressBar_progressTextSize, convertSpToPx(20));
            mIndicantTextSize = ta.getDimensionPixelSize(R.styleable.SwitchArcProgressBar_indicantTextSize, convertSpToPx(15));
            mProgressPrefix = ta.getString(R.styleable.SwitchArcProgressBar_progressPrefix);
            mProgressSuffix = ta.getString(R.styleable.SwitchArcProgressBar_progressSuffix);
            mIndicantText = ta.getString(R.styleable.SwitchArcProgressBar_indicantText);
            mRoundedCorners = ta.getBoolean(R.styleable.SwitchArcProgressBar_roundedCorners,false);
            mEnabled = ta.getBoolean(R.styleable.SwitchArcProgressBar_enabled,true);
            ta.recycle();

            mCurrentIndex = 0;
            mDisplayedValues = new ArrayList<>();

            if (isEnabled())
            {
                this.setClickable(mEnabled);
            }
            else
            {
                this.setClickable(mEnabled);
            }

            if (0 > mMaxProgressValue)
            {
                throw new IllegalArgumentException("Max progress value can't less than 0.");
            }

            if (0 > mProgressValue)
            {
                throw new IllegalArgumentException("Progress value can't less than 0.");
            }
            else
            {
                mProgressText = String.valueOf(mProgressValue);
            }

            if (TextUtils.checkEmptyOrNull(mProgressPrefix))
            {
                mProgressPrefix = "";
            }

            if (TextUtils.checkEmptyOrNull(mProgressSuffix))
            {
                mProgressSuffix = "";
            }

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
            mRadius = (int)(height / 2.4f);
        } else {
            mRadius = (int)(width / 2.4f);
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

        canvas.drawText(getDrawnText(), (getWidth() - mProgressTextPaint.measureText(getDrawnText())) / 2.0f, (getHeight() - progressTextHeight) / 2.0f, mProgressTextPaint);
        //mIndicantTextPaint.set
        canvas.drawText(getIndicantText(), (getWidth() - mIndicantTextPaint.measureText(getIndicantText())) / 2.0f, (getHeight() - indicantTextHeight) / 1.10f, mIndicantTextPaint);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        if(event.getAction() == MotionEvent.ACTION_UP)
        {
            if(mClickListener != null)
            {
                mClickListener.onClick(this);
            }
        }
        return super.dispatchTouchEvent(event);
    }

    @Override
    public void onAnimationUpdate(ValueAnimator valueAnimator)
    {
        int val = (Integer) valueAnimator.getAnimatedValue();
        mIndicantTextPaint.setAlpha(val);
        mProgressTextPaint.setAlpha(val);
        mProgressPaint.setAlpha(val);

        if (mValuelistener != null && val == 0)
        {
            mValuelistener.onChangedValue(this,mDisplayedValues.get(mCurrentIndex));
            iterateItem();
        }
        postInvalidate();
    }
    private int getSize(){
        if (mDisplayedValues != null && mDisplayedValues.size() > 0)
            return mDisplayedValues.size();

        return 0;
    }

    private void iterateItem(){
        if (mCurrentIndex < getSize() - 1)
            mCurrentIndex++;
        else
        {
            mCurrentIndex = 0;
        }
    }

    private float calculateSweepAngle(){
        return ((float)getProgressValue() / getMaxProgressValue()) * END_ANGLE;
    }

    public void setOnClickListener(OnClickListener listener) {
        mClickListener = listener;
    }

    public void setOnChangedValueListener(OnChangedValueListener listener)
    {
        mValuelistener = listener;

        if (mDisplayedValues.size() > 0)
        {
            mValuelistener.onChangedValue(this,mDisplayedValues.get(mCurrentIndex));
            iterateItem();
            invalidate();
        }
        else
        {
            mValuelistener.onChangedValue(this,new SwitchArcProgressModel("0","-","-","***"));
        }
    }
    private String getDrawnText()
    {
        return getProgressPrefix() + getProgressText() + getProgressSuffix();
    }
    public int getBackgroundColor() {
        return mBackgroundColor;
    }

    public void setBackgroundColor(int mBackgroundColor) {
        this.mBackgroundColor = mBackgroundColor;
        invalidate();
    }

    public int getProgressColor() {
        return mProgressColor;
    }

    public void setProgressColor(int mProgressColor) {
        this.mProgressColor = mProgressColor;
        invalidate();
    }

    public int getProgressTextColor() {
        return mProgressTextColor;
    }

    public void setProgressTextColor(int mProgressTextColor) {
        this.mProgressTextColor = mProgressTextColor;
        invalidate();
    }

    public int getIndicantTextColor() {
        return mIndicantTextColor;
    }

    public void setIndicantTextColor(int mIndicantTextColor) {
        this.mIndicantTextColor = mIndicantTextColor;
        invalidate();
    }

    public float getmStrokeWidth() {
        return mStrokeWidth;
    }

    public void setStrokeWidth(float mStrokeWidth) {
        this.mStrokeWidth = mStrokeWidth;
        invalidate();
    }

    public float getBackgroundWidth() {
        return mBackgroundWidth;
    }

    public void setBackgroundWidth(float mBackgroundWidth) {
        this.mBackgroundWidth = mBackgroundWidth;
        invalidate();
    }

    public int getMaxProgressValue() {
        return mMaxProgressValue;
    }

    public void setMaxProgressValue(int mMaxProgressValue) {
        this.mMaxProgressValue = mMaxProgressValue;
        invalidate();
    }

    public int getProgressValue() {
        return mProgressValue;
    }

    public void setProgressValue(int mProgressValue) {
        this.mProgressValue = mProgressValue;
        invalidate();
    }

    public int getProgressTextSize() {
        return mProgressTextSize;
    }

    public void setProgressTextSize(int mProgressTextSize) {
        this.mProgressTextSize = mProgressTextSize;
        invalidate();
    }

    public int getIndicantTextSize() {
        return mIndicantTextSize;
    }

    public void setIndicantTextSize(int mIndicantTextSize) {
        this.mIndicantTextSize = mIndicantTextSize;
        invalidate();
    }

    public String getProgressText() {
        return mProgressText;
    }

    public void setProgressText(String mProgressText) {
        this.mProgressText = mProgressText;
        invalidate();
    }

    public String getProgressPrefix() {
        return mProgressPrefix;
    }

    public void setProgressPrefix(String mProgressPrefix) {
        this.mProgressPrefix = mProgressPrefix;
        invalidate();
    }

    public String getProgressSuffix() {
        return mProgressSuffix;
    }

    public void setProgressSuffix(String mProgressSuffix) {
        this.mProgressSuffix = mProgressSuffix;
        invalidate();
    }

    public String getIndicantText() {
        return mIndicantText;
    }

    public void setIndicantText(String mIndicantText) {
        this.mIndicantText = mIndicantText;
        invalidate();
    }

    public boolean isRoundedCorners() {
        return mRoundedCorners;
    }

    public void setRoundedCorners(boolean mRoundedCorners) {
        this.mRoundedCorners = mRoundedCorners;
        invalidate();
    }

    public boolean isEnabled() {
        return mEnabled;
    }

    public void setEnabled(boolean mEnabled) {
        this.mEnabled = mEnabled;
        invalidate();
    }

    public Paint getProgressPaint() {
        return mProgressPaint;
    }

    public void setProgressPaint(Paint mProgressPaint) {
        this.mProgressPaint = mProgressPaint;
        invalidate();
    }

    public Paint getBackgroundPaint() {
        return mBackgroundPaint;
    }

    public void setBackgroundPaint(Paint mBackgroundPaint) {
        this.mBackgroundPaint = mBackgroundPaint;
        invalidate();
    }

    public Paint getProgressTextPaint() {
        return mProgressTextPaint;
    }

    public void setProgressTextPaint(Paint mProgressTextPaint) {
        this.mProgressTextPaint = mProgressTextPaint;
        invalidate();
    }

    public Paint getIndicantTextPaint() {
        return mIndicantTextPaint;
    }

    public void setmIndicantTextPaint(Paint mIndicantTextPaint) {
        this.mIndicantTextPaint = mIndicantTextPaint;
        invalidate();
    }

    public List<SwitchArcProgressModel> getDisplayedValues() {
        return mDisplayedValues;
    }

    public void setDisplayedValues(ArrayList<SwitchArcProgressModel> displayedValues) {
        this.mDisplayedValues = displayedValues;
    }

    private int convertSpToPx(int sp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, sp, Resources.getSystem().getDisplayMetrics());
    }
}
