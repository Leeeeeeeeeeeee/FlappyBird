package com.qing.flappybird.game;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;

import java.util.List;

public abstract class DrawablePart {

    protected Context mContext;
    protected int mGameWidth;
    protected int mGameHeight;
    protected Bitmap mBitmap;
    protected Bitmap mBitmap1;
    protected Bitmap mBitmap2;
    protected List<Bitmap> mBitmapList;

    public DrawablePart(Context context, int gameW, int gameH) {
        mContext = context;
        mGameWidth = gameW;
        mGameHeight = gameH;
    }

    public DrawablePart(Context context, int gameW, int gameH, Bitmap bitmap) {
        mContext = context;
        mGameWidth = gameW;
        mGameHeight = gameH;
        mBitmap = bitmap;
    }

    public DrawablePart(Context context, int gameW, int gameH, Bitmap bitmap, Bitmap bitmap1, Bitmap bitmap2) {
        mContext = context;
        mGameWidth = gameW;
        mGameHeight = gameH;
        mBitmap = bitmap;
        mBitmap1 = bitmap1;
        mBitmap2 = bitmap2;
    }

    public DrawablePart(Context context, int gameW, int gameH, List<Bitmap> bitmapList) {
        mContext = context;
        mGameWidth = gameW;
        mGameHeight = gameH;
        mBitmapList = bitmapList;
    }

    public abstract void draw(Canvas canvas);

}
