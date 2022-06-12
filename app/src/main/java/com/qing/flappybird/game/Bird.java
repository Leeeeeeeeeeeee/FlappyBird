package com.qing.flappybird.game;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.RectF;

import com.qing.flappybird.Utils;

public class Bird extends DrawablePart {

    private static final float RADIO_Y_POS = 1 / 2f;
    private static final int WIDTH_BIRD = 30;//dp

    private int x;
    private int y;

    private int mWidth;
    private int mHeight;
    private RectF mRect = new RectF();

    public Bird(Context context, int gameW, int gameH, Bitmap bitmap) {
        super(context, gameW, gameH, bitmap);
        y = (int) (gameH * RADIO_Y_POS);
        mWidth = Utils.dp2px(context, WIDTH_BIRD);
        mHeight = (int) (mWidth * 1.0f / bitmap.getWidth() * bitmap.getHeight());
        x = gameW / 2 - mWidth / 2;
    }

    @Override
    public void draw(Canvas canvas) {
        mRect.set(x, y, x + mWidth, y + mHeight);
        canvas.drawBitmap(mBitmap, null, mRect, null);
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getX() {
        return x;
    }

    public void reset() {
        y = (int) (mGameHeight * RADIO_Y_POS);
    }
}
