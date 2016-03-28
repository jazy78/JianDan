package com.example.hp.jiandan.view;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.widget.ImageView;

import com.example.hp.jiandan.net.ScreenSizeUtil;

/**
 * Created by hp on 2016/3/15.
 */
public class ShowMaxImageView extends ImageView {

    private float mHeight = 0;
    public ShowMaxImageView(Context context) {
        super(context);
    }

    public ShowMaxImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ShowMaxImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        if(mHeight!=0){
            int sizeWidth = MeasureSpec.getSize(widthMeasureSpec);
            int sizeHeight = MeasureSpec.getSize(heightMeasureSpec);
            int resultHeight = (int) Math.max(mHeight, sizeHeight);

            if(resultHeight> ScreenSizeUtil.getScreenHeight((Activity)getContext())){

                resultHeight = ScreenSizeUtil.getScreenHeight((Activity) getContext()) / 3;
            }
            setMeasuredDimension(sizeWidth, resultHeight);

        }else {

            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        }

    }

    //首先获得图片压缩的高度
    private void getHeight(Bitmap bitmap){
        float bitmapWidth=bitmap.getWidth();
        float bitmapHeight=bitmap.getHeight();

        if(bitmapWidth>0&&bitmapHeight>0){

            float scaleWidth = getWidth() / bitmapWidth;
            mHeight = bitmapHeight * scaleWidth;
        }

    }

    @Override
    public void setImageBitmap(Bitmap bm) {

        if(bm!=null){

            getHeight();
        }
        super.setImageBitmap(bm);
        requestLayout();//重新布局，高设为原来的1/3.
    }

    private Bitmap drawableToBitmap(Drawable drawable){

        if(drawable!=null){
            BitmapDrawable bd=(BitmapDrawable)drawable;
            return  bd.getBitmap();

        }else {
            return null;
        }
    }

    @Override
    public void setImageDrawable(Drawable drawable) {


        if (drawable != null) {
            getHeight(drawableToBitmap(drawable));
        }

        super.setImageDrawable(drawable);
        requestLayout();
    }

}
