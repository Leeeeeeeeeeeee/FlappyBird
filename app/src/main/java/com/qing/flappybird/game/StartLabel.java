package com.qing.flappybird.game;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;

import com.qing.flappybird.R;
import com.qing.flappybird.Utils;

public class StartLabel extends DrawablePart {

    private static final String TEXT = "点击以开始";
    private static final float RADIO_OFFSET_Y = 1 / 5F;
    private static final int TEXT_SIZE = 24;//dp
    private static final float LETTER_SPACING = 0.5f;
    private static final int PADDING = 16;//dp
    private static final int PADDING_WHITE = 4;//dp
    private static final int STROKE_WIDTH = 2;//dp

    private Paint mTextPaint;
    private Paint mBgOrangePaint;
    private Paint mBgWhitePaint;
    private Paint mBgStrokePaint;

    private final float text_x;
    private final float text_y;
    private final float bg_left;
    private final float bg_top;
    private final float bg_right;
    private final float bg_bottom;
    private final float padding_white;
    private final float stroke_width;

    public StartLabel(Context context, int gameW, int gameH) {
        super(context, gameW, gameH);
        //文字
        mTextPaint = new Paint();
        mTextPaint.setTextAlign(Paint.Align.CENTER);
        mTextPaint.setTextSize(Utils.dp2px(context, TEXT_SIZE));
        mTextPaint.setFakeBoldText(true);
        mTextPaint.setColor(Color.WHITE);
        mTextPaint.setLetterSpacing(LETTER_SPACING);
        mTextPaint.setTypeface(context.getResources().getFont(R.font.vonwaon));
        Rect bound = new Rect();
        mTextPaint.getTextBounds(TEXT, 0, TEXT.length(), bound);
        text_x = gameW / 2;
        text_y = gameH / 2 + bound.height() / 2 - mTextPaint.descent() / 2;
        //橙色背景
        mBgOrangePaint = new Paint();
        mBgOrangePaint.setStyle(Paint.Style.FILL);
        mBgOrangePaint.setColor(context.getColor(R.color.orange));
        bg_left = gameW / 2 - bound.width() / 2 - Utils.dp2px(context, PADDING);
        bg_top = gameH / 2 - bound.height() / 2 - Utils.dp2px(context, PADDING);
        bg_right = gameW / 2 + bound.width() / 2 + Utils.dp2px(context, PADDING);
        bg_bottom = gameH / 2 + bound.height() / 2 + Utils.dp2px(context, PADDING);
        //白色背景
        mBgWhitePaint = new Paint();
        mBgWhitePaint.setStyle(Paint.Style.FILL);
        mBgWhitePaint.setColor(Color.WHITE);
        padding_white = Utils.dp2px(context, PADDING_WHITE);
        //褐色边框
        mBgStrokePaint = new Paint();
        mBgStrokePaint.setStyle(Paint.Style.FILL);
        mBgStrokePaint.setColor(context.getColor(R.color.brown));
        stroke_width = Utils.dp2px(context, STROKE_WIDTH);
    }

    @Override
    public void draw(Canvas canvas) {
        canvas.drawRect(bg_left - padding_white - stroke_width, bg_top - padding_white - stroke_width, bg_right + padding_white + stroke_width, bg_bottom + padding_white + stroke_width, mBgStrokePaint);
        canvas.drawRect(bg_left - padding_white, bg_top - padding_white, bg_right + padding_white, bg_bottom + padding_white, mBgWhitePaint);
        canvas.drawRect(bg_left, bg_top, bg_right, bg_bottom, mBgOrangePaint);
        canvas.drawText(TEXT, 0, TEXT.length(), text_x, text_y, mTextPaint);
    }
}
