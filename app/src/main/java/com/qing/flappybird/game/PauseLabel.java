package com.qing.flappybird.game;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;

import com.qing.flappybird.R;
import com.qing.flappybird.Utils;

import java.util.List;

public class PauseLabel extends DrawablePart {
    private static final String TAG = "PauseLabel";
    private static final String TEXT_SCORE = "SCORE";
    private static final String TEXT_BEST = "BEST";
    private static final String TEXT_RESTART = "点击以继续";
    private static final int ENG_TEXT_SIZE = 16;//dp
    private static final float ENG_LETTER_SPACING = 0.2f;
    private static final int RESTART_TEXT_SIZE = 24;//dp
    private static final float RESTART_LETTER_SPACING = 0.5f;
    private static final int MARGIN_TEXT = 2;//dp
    private static final int WIDTH_NUM = 20;//dp
    private static final float RADIO_NUM = 1.5f;
    private static final int PADDING_BG_SCORE = 20;//dp
    private static final int PADDING_WHITE = 4;//dp
    private static final int STROKE_WIDTH = 2;//dp


    private Paint mEgTextPaint;
    private Paint mBgScorePaint;
    private Paint mBgRestartOrangePaint;
    private Paint mRestartTextPaint;
    private Paint mBgRestartWhitePaint;
    private Paint mBgStrokePaint;

    private RectF mRectScore = new RectF();

    private final int center_x;
    private final int center_y;

    private final float margin_text;
    private final float padding_bg_score;

    private final int num_width;
    private final int num_height;

    private int score_best = 0;
    private int score_current = 0;
    private final Rect engTextRect;
    private final float stroke_width;
    private final float padding_white;
    private final Rect restartTextRect;

    public PauseLabel(Context context, int gameW, int gameH, List<Bitmap> bitmapList) {
        super(context, gameW, gameH, bitmapList);
        center_x = gameW / 2;
        center_y = gameH / 2;
        margin_text = Utils.dp2px(context, MARGIN_TEXT);
        //得分背景
        mBgScorePaint = new Paint();
        mBgScorePaint.setStyle(Paint.Style.FILL);
        mBgScorePaint.setColor(context.getColor(R.color.yellow));
        padding_bg_score = Utils.dp2px(context, PADDING_BG_SCORE);
        //边框
        mBgStrokePaint = new Paint();
        mBgStrokePaint.setStyle(Paint.Style.FILL);
        mBgStrokePaint.setColor(context.getColor(R.color.brown));
        stroke_width = Utils.dp2px(context, STROKE_WIDTH);
        //英语文字
        mEgTextPaint = new Paint();
        mEgTextPaint.setTextAlign(Paint.Align.CENTER);
        mEgTextPaint.setTextSize(Utils.dp2px(context, ENG_TEXT_SIZE));
        mEgTextPaint.setFakeBoldText(true);
        mEgTextPaint.setColor(context.getColor(R.color.magenta));
        mEgTextPaint.setTypeface(context.getResources().getFont(R.font.vonwaon));
        mEgTextPaint.setLetterSpacing(ENG_LETTER_SPACING);
        engTextRect = new Rect();
        mEgTextPaint.getTextBounds(TEXT_BEST, 0, TEXT_BEST.length(), engTextRect);
        //得分
        num_width = Utils.dp2px(context, WIDTH_NUM);
        num_height = (int) (num_width * RADIO_NUM);
        //点击继续橙色背景
        mBgRestartOrangePaint = new Paint();
        mBgRestartOrangePaint.setStyle(Paint.Style.FILL);
        mBgRestartOrangePaint.setColor(context.getColor(R.color.orange));
        //点击继续白色背景
        mBgRestartWhitePaint = new Paint();
        mBgRestartWhitePaint.setStyle(Paint.Style.FILL);
        mBgRestartWhitePaint.setColor(Color.WHITE);
        padding_white = Utils.dp2px(context, PADDING_WHITE);
        //点击继续文字
        mRestartTextPaint = new Paint();
        mRestartTextPaint.setTextAlign(Paint.Align.CENTER);
        mRestartTextPaint.setTextSize(Utils.dp2px(context, RESTART_TEXT_SIZE));
        mRestartTextPaint.setFakeBoldText(true);
        mRestartTextPaint.setColor(Color.WHITE);
        mRestartTextPaint.setLetterSpacing(RESTART_LETTER_SPACING);
        mRestartTextPaint.setTypeface(context.getResources().getFont(R.font.vonwaon));
        restartTextRect = new Rect();
        mRestartTextPaint.getTextBounds(TEXT_RESTART, 0, TEXT_RESTART.length(), restartTextRect);
//        text_x = gameW / 2;
//        text_y = gameH / 2 + bound.height() / 2 - mTextPaint.descent() / 2;
    }

    @Override
    public void draw(Canvas canvas) {
        float currentScoreTop = center_y - margin_text - num_height - engTextRect.height() - mEgTextPaint.descent();
        float currentScoreBottom = center_y - margin_text - engTextRect.height() - mEgTextPaint.descent();
        float scoreBgLeft = center_x - num_width / 2f - num_width - margin_text - padding_bg_score;
        float scoreBgRight = center_x + num_width / 2f + num_width + margin_text + padding_bg_score;
        float scoreBgTop = currentScoreTop - margin_text - padding_bg_score - engTextRect.height() - mEgTextPaint.descent();
        float scoreBgBottom = center_y + margin_text + num_height + padding_bg_score;
        float restartLeft = center_x - restartTextRect.width() / 2f;
        float restartRight = center_x + restartTextRect.width() / 2f;
        float restartTop = scoreBgBottom + padding_bg_score * 3;
        float restartBottom = scoreBgBottom + restartTextRect.height() - mRestartTextPaint.descent() + padding_bg_score * 3;
        //restart
        canvas.drawRect(restartLeft - padding_bg_score - padding_white - stroke_width, restartTop - padding_bg_score - padding_white - stroke_width, restartRight + padding_bg_score + padding_white + stroke_width, restartBottom + padding_bg_score + padding_white + stroke_width, mBgStrokePaint);
        canvas.drawRect(restartLeft - padding_white - padding_bg_score, restartTop - padding_bg_score - padding_white, restartRight + padding_bg_score + padding_white, restartBottom + padding_bg_score + padding_white, mBgRestartWhitePaint);
        canvas.drawRect(restartLeft - padding_bg_score, restartTop - padding_bg_score, restartRight + padding_bg_score, restartBottom + padding_bg_score, mBgRestartOrangePaint);
        canvas.drawText(TEXT_RESTART, 0, TEXT_RESTART.length(), center_x, restartBottom, mRestartTextPaint);
        //score bg
        canvas.drawRect(scoreBgLeft - stroke_width, scoreBgTop - stroke_width, scoreBgRight + stroke_width, scoreBgBottom + stroke_width, mBgStrokePaint);
        canvas.drawRect(scoreBgLeft, scoreBgTop, scoreBgRight, scoreBgBottom, mBgScorePaint);
        //current text
        canvas.drawText(TEXT_SCORE, 0, TEXT_SCORE.length(), center_x, currentScoreTop - margin_text, mEgTextPaint);
        //current score
        if (score_current >= 999) {
            //左边字母
            mRectScore.set(center_x - num_width / 2 - num_width - margin_text, currentScoreTop, center_x + num_width / 2 - num_width - margin_text, currentScoreBottom);
            canvas.drawBitmap(mBitmapList.get(9), null, mRectScore, null);
            //中间字符
            mRectScore.set(center_x - num_width / 2, currentScoreTop, center_x + num_width / 2, currentScoreBottom);
            canvas.drawBitmap(mBitmapList.get(9), null, mRectScore, null);
            //右边字母
            mRectScore.set(center_x - num_width / 2 + num_width + margin_text, currentScoreTop, center_x + num_width / 2 + num_width + margin_text, currentScoreBottom);
            canvas.drawBitmap(mBitmapList.get(9), null, mRectScore, null);
        } else if (score_current > 99) {
            //左边字母
            mRectScore.set(center_x - num_width / 2 - num_width - margin_text, currentScoreTop, center_x + num_width / 2 - num_width - margin_text, currentScoreBottom);
            canvas.drawBitmap(mBitmapList.get(score_current / 100), null, mRectScore, null);
            //中间字符
            mRectScore.set(center_x - num_width / 2, currentScoreTop, center_x + num_width / 2, currentScoreBottom);
            canvas.drawBitmap(mBitmapList.get((score_current % 100) / 10), null, mRectScore, null);
            //右边字母
            mRectScore.set(center_x - num_width / 2 + num_width + margin_text, currentScoreTop, center_x + num_width / 2 + num_width + margin_text, currentScoreBottom);
            canvas.drawBitmap(mBitmapList.get(score_current % 10), null, mRectScore, null);
        } else if (score_current > 9) {
            //左边字母
            mRectScore.set(center_x - num_width - margin_text / 2, currentScoreTop, center_x - margin_text / 2, currentScoreBottom);
            canvas.drawBitmap(mBitmapList.get((score_current % 100) / 10), null, mRectScore, null);
            //右边字母
            mRectScore.set(center_x + margin_text / 2, currentScoreTop, center_x + num_width + margin_text / 2, currentScoreBottom);
            canvas.drawBitmap(mBitmapList.get(score_current % 10), null, mRectScore, null);
        } else {
            mRectScore.set(center_x - num_width / 2, currentScoreTop, center_x + num_width / 2, currentScoreBottom);
            canvas.drawBitmap(mBitmapList.get(score_current), null, mRectScore, null);
        }
        //best text
        canvas.drawText(TEXT_BEST, 0, TEXT_BEST.length(), center_x, center_y, mEgTextPaint);
        //best score
        if (score_best >= 999) {
            //左边字母
            mRectScore.set(center_x - num_width / 2 - num_width - margin_text, center_y + margin_text, center_x + num_width / 2 - num_width - margin_text, center_y + margin_text + num_height);
            canvas.drawBitmap(mBitmapList.get(9), null, mRectScore, null);
            //中间字符
            mRectScore.set(center_x - num_width / 2, center_y + margin_text, center_x + num_width / 2, center_y + margin_text + num_height);
            canvas.drawBitmap(mBitmapList.get(9), null, mRectScore, null);
            //右边字母
            mRectScore.set(center_x - num_width / 2 + num_width + margin_text, center_y + margin_text, center_x + num_width / 2 + num_width + margin_text, center_y + margin_text + num_height);
            canvas.drawBitmap(mBitmapList.get(9), null, mRectScore, null);
        } else if (score_best > 99) {
            //左边字母
            mRectScore.set(center_x - num_width / 2 - num_width - margin_text, center_y + margin_text, center_x + num_width / 2 - num_width - margin_text, center_y + margin_text + num_height);
            canvas.drawBitmap(mBitmapList.get(score_best / 100), null, mRectScore, null);
            //中间字符
            mRectScore.set(center_x - num_width / 2, center_y + margin_text, center_x + num_width / 2, center_y + margin_text + num_height);
            canvas.drawBitmap(mBitmapList.get((score_best % 100) / 10), null, mRectScore, null);
            //右边字母
            mRectScore.set(center_x - num_width / 2 + num_width + margin_text, center_y + margin_text, center_x + num_width / 2 + num_width + margin_text, center_y + margin_text + num_height);
            canvas.drawBitmap(mBitmapList.get(score_best % 10), null, mRectScore, null);
        } else if (score_best > 9) {
            //左边字母
            mRectScore.set(center_x - num_width - margin_text / 2, center_y + margin_text, center_x - margin_text / 2, center_y + margin_text + num_height);
            canvas.drawBitmap(mBitmapList.get((score_best % 100) / 10), null, mRectScore, null);
            //右边字母
            mRectScore.set(center_x + margin_text / 2, center_y + margin_text, center_x + num_width + margin_text / 2, center_y + margin_text + num_height);
            canvas.drawBitmap(mBitmapList.get(score_best % 10), null, mRectScore, null);
        } else {
            mRectScore.set(center_x - num_width / 2, center_y + margin_text, center_x + num_width / 2, center_y + margin_text + num_height);
            canvas.drawBitmap(mBitmapList.get(score_best), null, mRectScore, null);
        }
    }

    public void setCurrentScore(int score) {
        score_current = score;
    }

    public void setBestScore(int score) {
        score_best = score;
    }

}
