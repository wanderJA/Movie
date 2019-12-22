package com.wander.baseframe.view.loading;

import android.animation.ValueAnimator;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.os.Build;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;

import androidx.annotation.ColorInt;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.wander.baseframe.R;


/**
 * 加载动图
 * 可以独立使用
 * 设置staticPlay与autoAnimation属性为true
 *
 * @author zhaokaiyuan
 */
public class CircleLoadingView extends View {
    /**
     * 默认颜色
     */
    public static final int DEFAULT_COLOR = 0xff0bbe06;
    /**
     * 默认画笔宽度
     */
    private static final float DEFAULT_STROKE_WIDTH = 2.5f;

    /**
     * 默认动画时长
     * 即转一圈的时间
     */
    private static final long DEFAULT_DURATION = 1375L;

    /**
     * 动画终止点
     * 分为三个阶段：
     * 展开、转圈、消失
     */
    private static final float DEFAULT_ANIM_END = 1f;
    /**
     * 展开占比
     */
    private static final float STATE_EXPAND_ANIM = 0.21163636363636363636363636363636f;
    /**
     * 转圈占比
     */
    private static final float STATE_CIRCLE_ANIM = 0.84436363636363636363636363636364f;

    /**
     * 解耦需要 添加FloatUtils
     */
    private static final float EPSILON = 1.0E-5F;

    /**
     * 默认高度
     */
    private static final float DEFAULT_HEADER_THRESH_DP = 22;

    /**
     * View中心点横坐标
     */
    private float mWidthCenter;
    /**
     * View中心点纵坐标
     */
    private float mHeightCenter;

    /**
     * 圈显示的高度，包含padding
     */
    private int mVisibleHeight;
    /**
     * 绘制最大高度
     */
    private int mHeaderThresh;
    /**
     * 圈的半径
     */
    private float mRadius = 0;
    /**
     * 上下padding
     */
    private int mPaddingVertical = 0;

    /**
     * 是否自动播放动画
     */
    private boolean mAutoAnimation = false;
    /**
     * 是否自动
     */
    private boolean mStaticPlay = false;

    /**
     * 实际使用的颜色
     */
    private int mLoadingColor = DEFAULT_COLOR;
    /**
     * 实际的画笔宽度
     */
    private float mStrokeWidth = DEFAULT_STROKE_WIDTH;

    private RectF mBound;
    private RectF mTmpBound;
    private Paint mPaint;
    private Paint mFadePaint;

    /**
     * 控制动画是否无效执行
     */
    private long currentTimeMillis = -1;
    /**
     * 动画进行进度
     */
    private float mCurrentPosition;
    /**
     * 动画
     */
    private ValueAnimator mAnimator = null;
    /**
     * 动画监听调用位置监听
     */
    private CircleAnimatorUpdateListener mAnimatorUpdateListener = new CircleAnimatorUpdateListener();
    /**
     * 位置变化监听重绘界面
     */
    private ICirclePositionListener mPositionListener = new ICirclePositionListener() {
        @Override
        public void onPositionUpdate(float position) {
            if (currentTimeMillis == -1) {
                currentTimeMillis = System.currentTimeMillis();
            }
            if (System.currentTimeMillis() - currentTimeMillis > DEFAULT_DURATION * 6) { //六周空转就不转了
                reset();
                return;
            }
            mCurrentPosition = position;
            if (!parentVisible()) {
                reset();
                return;
            }
            invalidateSelf();
        }
    };

    public CircleLoadingView(Context context) {
        super(context);
        init(null, 0, 0);
    }

    public CircleLoadingView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs, 0, 0);
    }

    public CircleLoadingView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(attrs, defStyle, 0);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public CircleLoadingView(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(attrs, defStyleAttr, defStyleRes);
    }

    /**
     * 比较两个浮点数大小
     *
     * @param f1 浮点数1
     * @param f2 浮点数2
     * @return 两个浮点数是否相等
     */
    private static boolean floatsEqual(float f1, float f2) {
        return !Float.isNaN(f1) && !Float.isNaN(f2) ? (Math.abs(f2 - f1) < EPSILON) : (Float.isNaN(f1) && Float.isNaN(f2));
    }

    protected void init(AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        DisplayMetrics dm = getResources().getDisplayMetrics();
        mStrokeWidth = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, mStrokeWidth, dm);
        mHeaderThresh = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, DEFAULT_HEADER_THRESH_DP, dm);

        // Load attributes
        final TypedArray a = getContext().obtainStyledAttributes(
                attrs, R.styleable.CircleLoadingView, defStyleAttr, defStyleRes);
        if (a != null) {
            mHeaderThresh = a.getDimensionPixelSize(R.styleable.CircleLoadingView_size, mHeaderThresh);
            mPaddingVertical = a.getDimensionPixelSize(R.styleable.CircleLoadingView_padding_vertical, 0);
            mLoadingColor = a.getColor(R.styleable.CircleLoadingView_color_round, DEFAULT_COLOR);
            mStaticPlay = a.getBoolean(R.styleable.CircleLoadingView_static_play, false);
            mAutoAnimation = a.getBoolean(R.styleable.CircleLoadingView_auto_animation, false);
            mStrokeWidth = a.getDimension(R.styleable.CircleLoadingView_stroke_width, mStrokeWidth);
            a.recycle();
        }

        mBound = new RectF();
        mTmpBound = new RectF();
        // Set up a default paint
        mPaint = new Paint();
        mPaint.setStrokeWidth(mStrokeWidth);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeCap(Paint.Cap.ROUND);
        mPaint.setAntiAlias(true);
        mFadePaint = new Paint();
        mFadePaint.setStrokeWidth(mStrokeWidth);
        mFadePaint.setStyle(Paint.Style.STROKE);
        mFadePaint.setStrokeCap(Paint.Cap.ROUND);
        mFadePaint.setAntiAlias(true);
        invalidatePaint();

        // Set up a default animator
        mAnimator = ValueAnimator.ofFloat(0);
        mAnimator.setDuration(DEFAULT_DURATION);
        mAnimator.setRepeatCount(ValueAnimator.INFINITE);
        mAnimator.setRepeatMode(ValueAnimator.RESTART);
        mAnimator.setInterpolator(new AccelerateDecelerateInterpolator());
        mAnimatorUpdateListener.setPositionListener(mPositionListener);
        mAnimator.addUpdateListener(mAnimatorUpdateListener);
    }

    private void invalidatePaint() {
        mPaint.setColor(mLoadingColor);
        mFadePaint.setColor(mLoadingColor);
    }

    /**
     * 限制绘制的最大高度
     */
    public void setHeaderThresh(int headerThresh) {
        mHeaderThresh = headerThresh;
    }

    public int getVisibleHeight() {
        return mVisibleHeight;
    }

    /**
     * 设置圈的高度
     * 会根据高度宽度padding计算半径
     *
     * @param height 高度
     */
    public void setVisibleHeight(int height) {
        if (height == mVisibleHeight) {
            return;
        }
        mVisibleHeight = height;
        setVisibleHeightInternal();
    }

    private void setVisibleHeightInternal() {
        float halfStrokeWidth = mStrokeWidth / 2;
        float paddingLeft = getPaddingLeft() + halfStrokeWidth;
        float paddingTop = getPaddingTop() + halfStrokeWidth;
        float paddingRight = getPaddingRight() + halfStrokeWidth;
        float paddingBottom = getPaddingBottom() + halfStrokeWidth;

        float contentWidth = Math.max(getWidth() - paddingLeft - paddingRight, 0);
        float contentHeight = Math.max(getHeight() - paddingTop - paddingBottom, 0);

        float realHeight = Math.max(Math.min(Math.min(mVisibleHeight, mHeaderThresh), contentHeight) - mPaddingVertical * 2, 0);
        float radius = Math.min(realHeight, contentWidth) / 2f;
        if (floatsEqual(radius, mRadius)) {
            return;
        }
        mRadius = radius;

        if (floatsEqual(mRadius, 0) || mRadius < 0) {
            reset();
            invalidateSelf();
            return;
        } else {
            startAnimation();
        }

        mWidthCenter = paddingLeft + contentWidth / 2f;
        mHeightCenter = paddingTop + contentHeight / 2f;
        mBound.set(mWidthCenter - mRadius, mHeightCenter - mRadius, mWidthCenter + mRadius, mHeightCenter + mRadius);
        invalidateSelf();
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        reset();
    }

    @Override
    public void setEnabled(boolean enabled) {
        super.setEnabled(enabled);
        if (!enabled) {
            reset();
        }
    }

    /**
     * @param color 颜色
     * @deprecated use {@link #setLoadingColor(int)} instead
     */
    @Deprecated
    public void setAnimColor(@ColorInt int color) {
        setLoadingColor(color);
    }

    /**
     * 开始动画
     */
    public void startAnimation() {
        currentTimeMillis = -1;
        if (mVisibleHeight == 0) {
            reset();
            return;
        }
        if (!mAnimator.isRunning()) {
            mAnimatorUpdateListener.setPositionListener(mPositionListener);
            mAnimator.cancel();
            mAnimator.start();
        }
    }

    /**
     * 重置
     * 停止动画，移除Listener
     */
    public void reset() {
        mAnimator.cancel();
        mAnimatorUpdateListener.setPositionListener(null);
    }

    @Override
    public void setVisibility(int visibility) {
        super.setVisibility(visibility);
        if (visibility != VISIBLE) {
            reset();
        }
    }

    @Override
    protected void onVisibilityChanged(@NonNull View changedView, int visibility) {
        super.onVisibilityChanged(changedView, visibility);
        checkAutoAnimation(visibility);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        checkStaticPlay();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        currentTimeMillis = -1;

        if (mAutoAnimation && parentVisible() && !mAnimator.isRunning()) {
            startAnimation();
        }

        if (floatsEqual(mRadius, 0) || mRadius < 0) {
            return;
        }

        float position = mCurrentPosition;
        if (position < STATE_EXPAND_ANIM) {
            // 绘制展开的过程
            float ratio = position / STATE_EXPAND_ANIM;
            float pointOffset = mRadius * ratio;
            canvas.drawPoint(mWidthCenter - pointOffset, mHeightCenter, mPaint);
            canvas.drawPoint(mWidthCenter + pointOffset, mHeightCenter, mPaint);
        } else if (position < STATE_CIRCLE_ANIM) {
            // 绘制圆形
            float ratio = (position - STATE_EXPAND_ANIM) / (STATE_CIRCLE_ANIM - STATE_EXPAND_ANIM);
            float angle = ratio * 360;
            float newAngle = (angle > 180 ? 360 - angle : angle) * 0.9f;

            final int saveCount = canvas.save();
            canvas.rotate(angle - newAngle / 2, mWidthCenter, mHeightCenter);
            canvas.drawArc(mBound, 0, newAngle, false, mPaint);
            canvas.drawArc(mBound, 180, newAngle, false, mPaint);
            canvas.restoreToCount(saveCount);
        } else {
            // 绘制逐渐消失的过程
            float ratio = (position - STATE_CIRCLE_ANIM) / (DEFAULT_ANIM_END - STATE_CIRCLE_ANIM);
            float minusRatio = 1 - ratio;
            float angle = ratio * 180;
            mFadePaint.setStrokeWidth(mStrokeWidth * minusRatio);
            mFadePaint.setAlpha((int) (255 * minusRatio));
            float tmpRadius = mRadius * minusRatio;
            mTmpBound.set(mWidthCenter - tmpRadius, mHeightCenter - tmpRadius,
                    mWidthCenter + tmpRadius, mHeightCenter + tmpRadius);
            float newAngle = angle * 0.5f;

            final int saveCount = canvas.save();
            canvas.rotate(angle - newAngle / 2, mWidthCenter, mHeightCenter);
            canvas.drawArc(mTmpBound, 0, newAngle, false, mFadePaint);
            canvas.drawArc(mTmpBound, 180, newAngle, false, mFadePaint);
            canvas.restoreToCount(saveCount);
        }
    }

    /**
     * 对区域进行重绘
     */
    private void invalidateSelf() {
        invalidate((int) (mBound.left - mStrokeWidth), (int) (mBound.top - mStrokeWidth),
                (int) (mBound.right + mStrokeWidth), (int) (mBound.bottom + mStrokeWidth));
    }

    /**
     * 父控件是否显示
     *
     * @return 父控件是否显示
     */
    private boolean parentVisible() {
        return getParent() instanceof View && ((View) getParent()).getVisibility() == VISIBLE;
    }

    /**
     * Gets the loading color attribute value.
     *
     * @return The loading color attribute value.
     */
    public int getLoadingColor() {
        return mLoadingColor;
    }

    /**
     * Sets the view's loading color attribute value. In the circleLoadingView, this color
     * is the circle color.
     *
     * @param loadingColor The loading color attribute value to use.
     */
    public void setLoadingColor(@ColorInt int loadingColor) {
        mLoadingColor = loadingColor;
        invalidatePaint();
    }

    public int getPaddingVertical() {
        return mPaddingVertical;
    }

    public void setPaddingVertical(int paddingVertical) {
        mPaddingVertical = paddingVertical;
        setVisibleHeightInternal();
    }

    public boolean isAutoAnimation() {
        return mAutoAnimation;
    }

    public void setAutoAnimation(boolean autoAnimation) {
        mAutoAnimation = autoAnimation;
        checkAutoAnimation(getVisibility());
    }

    private void checkAutoAnimation(int visibility) {
        if (mAutoAnimation) {
            // let's be nice with the UI thread
            if (visibility == VISIBLE) {
                startAnimation();
            } else {
                reset();
            }
        }
    }

    public boolean isStaticPlay() {
        return mStaticPlay;
    }

    public void setStaticPlay(boolean staticPlay) {
        mStaticPlay = staticPlay;
        checkStaticPlay();
    }

    private void checkStaticPlay() {
        if (mStaticPlay) {
            // 维持兼容 理论上staticPlay与paddingVertical没有任何关系
            mPaddingVertical = 0;
            setVisibleHeight(getMeasuredWidth());
        }
    }

    private interface ICirclePositionListener {
        void onPositionUpdate(float position);
    }

    private static class CircleAnimatorUpdateListener implements ValueAnimator.AnimatorUpdateListener {

        private ICirclePositionListener mPositionListener;

        private CircleAnimatorUpdateListener() {
        }

        @Override
        public void onAnimationUpdate(ValueAnimator animation) {
            if (mPositionListener != null) {
                mPositionListener.onPositionUpdate(animation.getAnimatedFraction());
            }
        }

        ICirclePositionListener getPositionListener() {
            return mPositionListener;
        }

        void setPositionListener(ICirclePositionListener positionListener) {
            mPositionListener = positionListener;
        }
    }

}

