package com.wander.baseframe.view;
/*
 * Copyright (c) 2015-present, Facebook, Inc.
 * All rights reserved.
 *
 * This source code is licensed under the BSD-style license found in the
 * LICENSE file in the root directory of this source tree. An additional grant
 * of patent rights can be found in the PATENTS file in the same directory.
 */


import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.drawable.Animatable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Pair;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.facebook.common.logging.FLog;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.controller.ControllerListener;
import com.facebook.drawee.controller.ForwardingControllerListener;
import com.facebook.drawee.drawable.ScalingUtils;
import com.facebook.drawee.generic.GenericDraweeHierarchy;
import com.facebook.drawee.interfaces.DraweeController;
import com.facebook.drawee.view.SimpleDraweeView;
import com.facebook.imagepipeline.common.ImageDecodeOptions;
import com.facebook.imagepipeline.common.ResizeOptions;
import com.facebook.imagepipeline.image.ImageInfo;
import com.facebook.imagepipeline.request.ImageRequest;
import com.facebook.imagepipeline.request.ImageRequestBuilder;
import com.wander.baseframe.view.utils.ResourcesToolForPlugin;

import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.Map;

/**
 * 定制的DraweeView
 */
public class AutoDraweeView extends SimpleDraweeView {

    private static final String TAG = "QiyiDraweeView";
    // 缓存屏幕信息
    private static DisplayMetrics sDm = Resources.getSystem().getDisplayMetrics();

    public static Map<String, WeakReference<AttributeHelper>> mAttirbuteHelper =
            new HashMap<>();

    /**
     * 由于{@link ImageView#getMaxWidth()} and {@link ImageView#getMaxHeight()} 在API 16引入
     * 为了向下兼容到API 14， 因此需要在该类中保存最大的宽高信息
     */
    private int mMaxWidth = Integer.MAX_VALUE;
    private int mMaxHeight = Integer.MAX_VALUE;

    private int resourceImageWidth;
    private int resourceImageHeight;
    private boolean mInitialised = false;
    private boolean isPressed = false;
    private boolean enablePressState = false;
    private int mPressedOverlayColor;

    public AutoDraweeView(Context context, GenericDraweeHierarchy hierarchy) {
        super(context, hierarchy);
        mInitialised = true;
    }

    public AutoDraweeView(Context context) {
        super(context);
        mInitialised = true;
    }

    public AutoDraweeView(Context context, AttributeSet attrs) {
        super(context, attrs);
        fixWrapContent(context, attrs);
        mInitialised = true;
    }

    public AutoDraweeView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        fixWrapContent(context, attrs);
        mInitialised = true;
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public AutoDraweeView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        fixWrapContent(context, attrs);
        mInitialised = true;
    }

    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);
        if (isPressed && enablePressState
                && mPressedOverlayColor != 0) {
            canvas.drawColor(mPressedOverlayColor);
        }
    }

    @Override
    public void setPressed(boolean pressed) {
        super.setPressed(pressed);
        if (pressed != isPressed) {
            isPressed = pressed;
            invalidate();
        }
    }

    /**
     * 设置按压状态开关
     *
     * @param enable true - 按压
     */
    public void setEnablePressStateChange(boolean enable) {
        enablePressState = enable;
    }

    /**
     * 设置PressedStateOverlayColor
     *
     * @param color
     */
    public void setPressedStateOverlayColor(int color) {
        mPressedOverlayColor = color;
    }

    /**
     * 修复当layout_width和layout_height都是wrap_content，设置src无法显示的问题
     * 当没有设置fresco的<attr>placeholderImage</attr> or <attr>actualImageScaleType</attr>时，将
     * ImageView的src和scaleType转成fresco的相应属性
     *
     * @param context
     * @param attrs
     */
    private void fixWrapContent(Context context, AttributeSet attrs) {

        if (context == null || attrs == null) {
            return;
        }

        try {
            ScaleType scaleType = getScaleType();
            final TypedArray draweeViewTA = context.obtainStyledAttributes(attrs, com.facebook.drawee.R.styleable.SimpleDraweeView);
            if (!draweeViewTA.hasValue(com.facebook.drawee.R.styleable.SimpleDraweeView_actualImageScaleType)) {
                // xml没有配置fresco的actualImageScaleType，使用ImageView的scaleType兼容
                getHierarchy().setActualImageScaleType(transformScaleType(scaleType));
            }

            AttributeHelper attributeHelper = getAtrributeHelper(context.getApplicationContext(), "com.android.internal");
            if (attributeHelper != null) {
                Drawable srcDrawable = null;
                int[] imageViewAttrs = attributeHelper.getStyleables("ImageView");
                if (imageViewAttrs != null) {
                    final TypedArray imageViewTA = context.obtainStyledAttributes(
                            attrs, /*com.android.internal.R.styleable.ImageView*/imageViewAttrs);
                    srcDrawable = imageViewTA.getDrawable(/*com.android.internal.R.styleable.ImageView_src*/
                            attributeHelper.getStyleable("ImageView_src"));
                    imageViewTA.recycle();
                }

                if (srcDrawable != null) {
                    // 配置ImageView_src，没有配置SimpleDraweeView_placeholderImage，将src转成placeholderImage
                    Drawable placeholderImage = draweeViewTA.getDrawable(com.facebook.drawee.R.styleable.SimpleDraweeView_placeholderImage);
                    if (placeholderImage == null) {
                        // xml没有配置fresco的placeholderImageScaleType，使用ImageView的ScaleType兼容
                        if (draweeViewTA.hasValue(com.facebook.drawee.R.styleable.SimpleDraweeView_placeholderImageScaleType)) {
                            getHierarchy().setPlaceholderImage(srcDrawable);
                        } else {
                            getHierarchy().setPlaceholderImage(srcDrawable, transformScaleType(scaleType));
                        }
                    }
                }

                int[] viewGroupAttrs = attributeHelper.getStyleables("ViewGroup_Layout");
                if (viewGroupAttrs != null) {
                    TypedArray layoutTA = context.obtainStyledAttributes(
                            attrs, /*com.android.internal.R.styleable.ViewGroup_Layout*/viewGroupAttrs);

                    int layout_width = layoutTA.getLayoutDimension(attributeHelper.getStyleable("ViewGroup_Layout_layout_width"), "layout_width");
                    int layout_height = layoutTA.getLayoutDimension(attributeHelper.getStyleable("ViewGroup_Layout_layout_height"), "layout_height");
                    layoutTA.recycle();
                    if (srcDrawable != null) {
                        if (layout_width == ViewGroup.LayoutParams.WRAP_CONTENT) {
                            this.resourceImageWidth = srcDrawable.getMinimumWidth();
                        }
                        if (layout_height == ViewGroup.LayoutParams.WRAP_CONTENT) {
                            this.resourceImageHeight = srcDrawable.getMinimumHeight();
                        }
                    }
                }
            }
            draweeViewTA.recycle();

        } catch (Throwable t) {
            FLog.e(TAG, "QiyiDraweeView fixWrapContent Exception: " + t.getMessage());
            if (FLog.isLoggable(FLog.VERBOSE)) {
                // Debug模式
                throw new RuntimeException("QiyiDraweeView fixWrapContent exception", t);
            }
        }
    }


    @Override
    public void setScaleType(ScaleType scaleType) {
        super.setScaleType(scaleType);
        // ImageView解析完xml，QiyiDraweeView初始化完成之后才将ImageView的ScaleType转换成SimpleDraweeView的ScaleType
        try {
            if (mInitialised && hasHierarchy()) {
                ScalingUtils.ScaleType frescoScaleType = transformScaleType(scaleType);
                getHierarchy().setActualImageScaleType(frescoScaleType);
            }
        } catch (Exception e) {
            FLog.e(TAG, "QiyiDraweeView setScaleType Exception: " + e.getMessage());
            if (FLog.isLoggable(FLog.VERBOSE)) {
                // Debug模式
                throw new RuntimeException("QiyiDraweeView setScaleType exception", e);
            }
        }
    }

    /**
     * 从ImageView的ScaleType映射到fresco中的ScaleType
     *
     * @param scaleType
     * @return
     */
    private ScalingUtils.ScaleType transformScaleType(ScaleType scaleType) {
        ScalingUtils.ScaleType ret = ScalingUtils.ScaleType.FIT_CENTER;  // 使用ImageView默认的ScaleType

        if (scaleType == null) {
            return ret;
        }

        switch (scaleType) {
            case CENTER:
                ret = ScalingUtils.ScaleType.CENTER;
                break;
            case CENTER_CROP:
                ret = ScalingUtils.ScaleType.CENTER_CROP;
                break;
            case CENTER_INSIDE:
                ret = ScalingUtils.ScaleType.CENTER_INSIDE;
                break;
            case FIT_CENTER:
                ret = ScalingUtils.ScaleType.FIT_CENTER;
                break;
            case FIT_END:
                ret = ScalingUtils.ScaleType.FIT_END;
                break;
            case FIT_START:
                ret = ScalingUtils.ScaleType.FIT_START;
                break;
            case FIT_XY:
                ret = ScalingUtils.ScaleType.FIT_XY;
                break;
            case MATRIX:
                if (FLog.isLoggable(FLog.VERBOSE)) {
                    throw new RuntimeException("transformScaleType: unsupported ImageView ScaleType");
                }
                break;
        }

        return ret;
    }

    @Override
    public void setLayoutParams(ViewGroup.LayoutParams params) {

        Pair<Boolean, Boolean> notSupportParams = notSupportParams(params);

        if (notSupportParams.first && resourceImageWidth != 0) {
            params.width = resourceImageWidth;
        }

        if (notSupportParams.second && resourceImageHeight != 0) {
            params.height = resourceImageHeight;
        }

        super.setLayoutParams(params);
    }

    /**
     * 子类覆写该方法，决定是否需要对wrap_content进行适配
     *
     * @param params
     * @return
     */
    protected Pair<Boolean, Boolean> notSupportParams(ViewGroup.LayoutParams params) {

        Pair<Boolean, Boolean> ret;

        boolean useAspectRatio = getAspectRatio() > 0
                && !(params.width == ViewGroup.LayoutParams.WRAP_CONTENT
                && params.height == ViewGroup.LayoutParams.WRAP_CONTENT);

        //是否使用了缩放比，假如是则不能简单按图片宽高来设置LayoutParams
        if (useAspectRatio) {
            ret = new Pair<Boolean, Boolean>(false, false);
        } else {
            boolean notSupportWidth = params.width == ViewGroup.LayoutParams.WRAP_CONTENT
                    && Math.max(getMeasuredWidth(), getSuggestedMinimumWidth()) < 2;
            boolean notSupportHeight = params.height == ViewGroup.LayoutParams.WRAP_CONTENT
                    && Math.max(getMeasuredHeight(), getSuggestedMinimumHeight()) < 2;
            ret = new Pair<Boolean, Boolean>(notSupportWidth, notSupportHeight);
        }

        return ret;
    }

    /**
     * 该方法是{@link SimpleDraweeView}设置图片的入口，重载该方法
     * 将所有形式的图片加载代理到该类的实现
     *
     * @param uri
     * @param callerContext
     */
    @Override
    public void setImageURI(Uri uri, Object callerContext) {
        setImageURI(uri, callerContext, null);
    }


    /**
     * 为setImageURI()增加{@link ControllerListener}参数，供外部调用
     *
     * @param uri
     * @param outListener
     */
    public void setImageURI(Uri uri, ControllerListener<ImageInfo> outListener) {
        setImageURI(uri, null, outListener);
    }


    /**
     * setImageURI()的最终调用方法入口
     *
     * @param uri
     * @param callerContext
     * @param outsideListener
     */
    public void setImageURI(Uri uri, Object callerContext, final ControllerListener<ImageInfo> outsideListener) {

        final WeakReference<ImageView> reference = new WeakReference<ImageView>(this);
        final String url = String.valueOf(uri);

        ForwardingControllerListener<ImageInfo> innerListener = new ForwardingControllerListener<ImageInfo>() {
            @Override
            public void onFinalImageSet(String id, ImageInfo imageInfo, Animatable animatable) {
                updateViewSize(imageInfo, reference);
                // 动画控制
                if (animatable != null) {
                    animatable.start();
                    if (getContext() instanceof IAnimatable) {
                        ((IAnimatable) getContext()).collectAnimatables(animatable);
                    }
                }
                super.onFinalImageSet(id, imageInfo, animatable);
                onImageBeenSet();
            }

            @Override
            public void onFailure(String id, Throwable throwable) {
                super.onFailure(id, throwable);
            }

            /**
             * 根据图片大小更新控件的LayoutParams
             * @param imageInfo
             * @param reference
             */
            private void updateViewSize(ImageInfo imageInfo, WeakReference<ImageView> reference) {
                ImageView imageView = reference.get();
                if (imageView != null) {
                    Pair<Boolean, Boolean> notSupportParams = notSupportParams(getLayoutParams());
                    if (notSupportParams.first || notSupportParams.second) {
                        ViewGroup.LayoutParams layoutParams = imageView.getLayoutParams();
                        if (notSupportParams.first) {
                            layoutParams.width = imageInfo.getWidth();
                        }
                        if (notSupportParams.second) {
                            layoutParams.height = imageInfo.getHeight();
                        }
                        imageView.setLayoutParams(layoutParams);
                    }
                }
            }
        };

        if (outsideListener != null) {
            innerListener.addListener(outsideListener);
        }

        ImageRequest request = null;
        if (uri != null) {
            request = ImageRequestBuilder
                    .newBuilderWithSource(uri)
                    .setResizeOptions(getResizeOption())
                    .setProgressiveRenderingEnabled(url.endsWith(".jpg"))
                    .setImageDecodeOptions(ImageDecodeOptions.newBuilder().setDecodePreviewFrame(true).build())
                    .build();
        }

        DraweeController controller = Fresco.newDraweeControllerBuilder()
                .setImageRequest(request)
                .setCallerContext(callerContext)
                .setControllerListener(innerListener)
                .setOldController(getController())
                .build();

        setController(controller);
    }


    /**
     * 计算ResizeOptions参数，限制分辨率过大的图片
     * 1. 如果指定了LayoutParams为固定尺寸，则根据LayoutParams对图片进行resize
     * 2. 没有指定LayoutParams，则根据{maxWidth, maxHeight}与屏幕尺寸的关系进行裁剪
     *
     * @return resizeOption，决定了Decode Bitmap图片时的采样率
     */
    private ResizeOptions getResizeOption() {
        ViewGroup.LayoutParams layoutParams = getLayoutParams();
        int width = 1;
        int height = 1;

        //部分厂商机型 Resources.getSystem().getDisplayMetrics() 得不到屏幕物理尺寸
        if (sDm.widthPixels == 0 || sDm.heightPixels == 0) {
            DisplayMetrics dm = getResources().getDisplayMetrics();
            sDm.widthPixels = dm.widthPixels;
            sDm.heightPixels = dm.heightPixels;
        }

        if (layoutParams != null && layoutParams.width > 0) {
            width = layoutParams.width;
        } else {
            width = mMaxWidth > sDm.widthPixels ? sDm.widthPixels : mMaxWidth;
        }

        if (layoutParams != null && layoutParams.height > 0) {
            height = layoutParams.height;
        } else {
            height = mMaxHeight > sDm.heightPixels ? 1 : mMaxHeight;
        }

        return new ResizeOptions(width, height);
    }


    @Override
    public void setMaxWidth(int maxWidth) {
        super.setMaxWidth(maxWidth);
        mMaxWidth = maxWidth;
    }

    @Override
    public void setMaxHeight(int maxHeight) {
        super.setMaxHeight(maxHeight);
        mMaxHeight = maxHeight;
    }


    /**
     * 泡泡的类PPApp还在引用
     *
     * @param context
     */
    @Deprecated
    public static void initFresco(Context context) {
        //ImageLoader.initFresco(context);
    }


    /**
     * 实现了该接口的对象（Activity）可以收集QiyiDraweeView中的Animatable
     * 在恰当的时机（onResume、onPause）控制动画的开始和结束
     * 用于解决动画在新启动的Activity背后仍然执行的问题
     */
    public interface IAnimatable {
        void collectAnimatables(Animatable animatable);
    }

    protected void onImageBeenSet() {

    }


    protected AttributeHelper getAtrributeHelper(Context context, String attributeKye) {
        AttributeHelper attributeHelper = null;
        WeakReference<AttributeHelper> weakReference =
                mAttirbuteHelper.get(attributeKye);
        boolean rebuildHelper = false;
        if (weakReference == null) {
            rebuildHelper = true;
        } else {
            attributeHelper = weakReference.get();
            if (attributeHelper == null) {
                rebuildHelper = true;
            }
        }
        if (rebuildHelper) {
            attributeHelper = new AttributeHelper(context.getApplicationContext(), attributeKye);
            weakReference = new WeakReference<AttributeHelper>(attributeHelper);
            mAttirbuteHelper.put(attributeKye, weakReference);
        }
        return attributeHelper;
    }

    public static class AttributeHelper {

        private final Map<String, int[]> mStyleablesMap;
        private final Map<String, Integer> mStyleableMap;
        private final ResourcesToolForPlugin rtfp;

        public AttributeHelper(Context context, String packageName) {
            rtfp = new ResourcesToolForPlugin(context.getResources(),
                    packageName, context.getClassLoader(), true);
            mStyleablesMap = new HashMap<String, int[]>();
            mStyleableMap = new HashMap<String, Integer>();
        }

        public int[] getStyleables(String styleablesName) {
            int[] re = null;
            if (!styleablesName.isEmpty()) {
                re = mStyleablesMap.get(styleablesName);
                if (re == null) {
                    re = rtfp.getResourceForStyleables(styleablesName);
                    mStyleablesMap.put(styleablesName, re);
                }
            }
            return re;
        }

        public int getStyleable(String styleableName) {
            if (!styleableName.isEmpty()) {
                Integer integer = mStyleableMap.get(styleableName);
                if (integer == null) {
                    integer = rtfp.getResourceForStyleable(styleableName);
                    mStyleableMap.put(styleableName, integer);
                }
                return integer;
            }
            return NO_ID;
        }
    }
}
