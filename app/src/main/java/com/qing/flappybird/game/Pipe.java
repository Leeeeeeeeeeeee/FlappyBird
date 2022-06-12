package com.qing.flappybird.game;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Shader;
import android.util.Log;

import com.qing.flappybird.R;
import com.qing.flappybird.Utils;

import java.util.Random;

public class Pipe extends DrawablePart {

    private static final String TAG = "Pipe";
    private static final float RADIO_MAX_Y_POS = 4 / 5F;
    private static final int HEIGHT_SPACING = 150;//dp
    private static final int WIDTH_PIPE = 50;//dp
    private static final int CODE_NORMAL = 0;
    public static final int CODE_NEW_PIPE = 1;
    public static final int CODE_DESTROY_PIPE = 2;

    private RectF mRect = new RectF();
    private boolean shouldCreate = true;

    private int mWidth;
    private int mTopHeight;
    private int mBottomHeight;
    private int maxY;
    private int x;
    private int topY;
    private int bottomY;
    private boolean isPassed = false;

    private Paint mPaint;

    public Pipe(Context context, int gameW, int gameH, Bitmap bitmap, Bitmap bitmap1, Bitmap bitmap2) {
        super(context, gameW, gameH, bitmap, bitmap1, bitmap2);

        maxY = (int) (gameH * RADIO_MAX_Y_POS);

        int heightSpacing = Utils.dp2px(context, HEIGHT_SPACING);
        int posY = (int) (Math.random() * (maxY - heightSpacing));
        topY = posY - heightSpacing / 2;
        bottomY = posY + heightSpacing / 2;

        mWidth = Utils.dp2px(context, WIDTH_PIPE);
        mTopHeight = posY;
        mBottomHeight = maxY - bottomY;

        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setDither(true);

    }

    @Override
    public void draw(Canvas canvas) {
        //绘制上方的管道
        int drawY0 = topY;
        drawY0 -= mBitmap.getHeight();
        mRect.set(x + mGameWidth, drawY0, x + mWidth + mGameWidth, drawY0 + mBitmap.getHeight());
        canvas.drawBitmap(mBitmap, null, mRect, null);
        while (drawY0 > 0) {
            drawY0 -= mBitmap2.getHeight();
            mRect.set(x + mGameWidth, drawY0, x + mWidth + mGameWidth, drawY0 + mBitmap2.getHeight());
            canvas.drawBitmap(mBitmap2, null, mRect, null);
        }
        //绘制下方的管道
        int drawY1 = bottomY;
        mRect.set(x + mGameWidth, drawY1, x + mWidth + mGameWidth, drawY1 + mBitmap1.getHeight());
        canvas.drawBitmap(mBitmap1, null, mRect, null);
        drawY1 += mBitmap1.getHeight();
        while (drawY1 < mBottomHeight + mBitmap2.getHeight()) {
            mRect.set(x + mGameWidth, drawY1, x + mWidth + mGameWidth, drawY1 + mBitmap2.getHeight());
            canvas.drawBitmap(mBitmap2, null, mRect, null);
            drawY1 += mBitmap2.getHeight();
        }
    }

    public int getX() {
        return x;
    }

    public int setX(int x) {
        if (-x > mGameWidth) {
            //管道已移动完毕
            return CODE_DESTROY_PIPE;
        } else if (-x > mGameWidth / 2) {
            //管道移动了一半
            this.x = x;
            if (shouldCreate) {
                shouldCreate = false;
                return CODE_NEW_PIPE;
            }
            return CODE_NORMAL;
        } else {
            this.x = x;
            return CODE_NORMAL;
        }
    }

    public int getTopY() {
        return topY;
    }

    public int getBottomY() {
        return bottomY;
    }

    public int getLocateX() {
        return x + mGameWidth;
    }

    public int getPipeWidth() {
        return mWidth;
    }

    public void pass() {
        isPassed = true;
    }

    public boolean isPass() {
        return isPassed;
    }
}
