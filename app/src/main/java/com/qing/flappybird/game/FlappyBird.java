package com.qing.flappybird.game;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;

import com.qing.flappybird.R;
import com.qing.flappybird.Utils;

import java.util.ArrayList;
import java.util.List;

public class FlappyBird extends SurfaceView implements Runnable {
    private static final String TAG = "FlappyBird";
    private static final String SP = "Config";
    private static final String SP_BEST_SCORE = "score_best";
    private static final int TOUCH_UP_SIZE = -16;//dp
    private static final int SIZE_AUTO_DOWN = 2;//dp
    private static final int STATUS_START = 0;
    private static final int STATUS_GOING = 1;
    private static final int STATUS_PAUSE = 2;

    private Thread thread;
    private volatile boolean isRunning;

    private RectF mDestRect;

    private int sceneW;
    private int sceneH;

    private Bitmap mBgBm;
    private Bitmap mBirdBm;
    private Bitmap mFloorBm;
    private Bitmap mPipeTopBm;
    private Bitmap mPipeBottomBm;
    private Bitmap mPipeExtendBm;
    private List<Bitmap> mNumBmList;

    private Floor mFloor;
    private Bird mBird;
    private Pipe mPipe0;
    private Pipe mPipe1;
    private StartLabel mStartLabel;
    private PauseLabel mPauseLabel;

    private int mSpeed;

    private int mBirdUpDis;
    private int mBirdAutoDownDist;
    private int mTmpBirdDis;

    private int status_flag = STATUS_START;

    private int score_current;
    private int score_best;
    private SharedPreferences sp;

    public FlappyBird(Context context, AttributeSet attrs) {
        super(context, attrs);

        SurfaceHolder holder = getHolder();
        holder.addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(@NonNull SurfaceHolder surfaceHolder) {
                //监听到Surface创建完毕
                isRunning = true;
                thread = new Thread(FlappyBird.this);
                thread.start();

                mBird.reset();
                mTmpBirdDis = 0;
            }

            @Override
            public void surfaceChanged(@NonNull SurfaceHolder surfaceHolder, int i, int i1, int i2) {

            }

            @Override
            public void surfaceDestroyed(@NonNull SurfaceHolder surfaceHolder) {
                isRunning = false;
            }
        });

        setFocusable(true);
        setFocusableInTouchMode(true);
        setKeepScreenOn(true);

        initVal();

        initRes();
    }

    private void initVal() {
        mSpeed = Utils.dp2px(getContext(), 2);
        mBirdUpDis = Utils.dp2px(getContext(), TOUCH_UP_SIZE);
        mBirdAutoDownDist = Utils.dp2px(getContext(), SIZE_AUTO_DOWN);
        score_current = 0;
        sp = getContext().getSharedPreferences(SP, Context.MODE_PRIVATE);
        score_best = sp.getInt(SP_BEST_SCORE, 0);
    }

    private void initRes() {
        mBgBm = loadBitmapByResId(R.drawable.bg1);
        mBirdBm = loadBitmapByResId(R.drawable.b1);
        mFloorBm = loadBitmapByResId(R.drawable.floor_bg);
        mPipeTopBm = loadBitmapByResId(R.drawable.g2);
        mPipeBottomBm = loadBitmapByResId(R.drawable.g1);
        mPipeExtendBm = loadBitmapByResId(R.drawable.g3);
        mNumBmList = new ArrayList<>();
        mNumBmList.add(loadBitmapByResId(R.drawable.n0));
        mNumBmList.add(loadBitmapByResId(R.drawable.n1));
        mNumBmList.add(loadBitmapByResId(R.drawable.n2));
        mNumBmList.add(loadBitmapByResId(R.drawable.n3));
        mNumBmList.add(loadBitmapByResId(R.drawable.n4));
        mNumBmList.add(loadBitmapByResId(R.drawable.n5));
        mNumBmList.add(loadBitmapByResId(R.drawable.n6));
        mNumBmList.add(loadBitmapByResId(R.drawable.n7));
        mNumBmList.add(loadBitmapByResId(R.drawable.n8));
        mNumBmList.add(loadBitmapByResId(R.drawable.n9));
    }

    private Bitmap loadBitmapByResId(@DrawableRes int resId) {
        return BitmapFactory.decodeResource(getResources(), resId);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        sceneW = w;
        sceneH = h;
        mDestRect = new RectF(0, 0, w, h);
        mFloor = new Floor(getContext(), w, h, mFloorBm);
        mBird = new Bird(getContext(), w, h, mBirdBm);
        mPipe0 = new Pipe(getContext(), w, h, mPipeTopBm, mPipeBottomBm, mPipeExtendBm);
        mStartLabel = new StartLabel(getContext(), w, h);
    }

    @Override
    public void run() {
        while (isRunning) {
            long start = System.currentTimeMillis();
            drawSelf();
            long end = System.currentTimeMillis();
            if (end - start < 50) {
                try {
                    Thread.sleep(50 - (end - start));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void drawSelf() {
        Canvas canvas = null;
        try {
            canvas = getHolder().lockCanvas();
            if (canvas != null) {
                drawBg(canvas);
                if (status_flag == STATUS_GOING) {
                    logic();
                }
                drawBird(canvas);
                drawPipe(canvas);
                drawFloor(canvas);
                drawLabel(canvas);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (canvas != null) {

            }
        }
        getHolder().unlockCanvasAndPost(canvas);
    }

    private void logic() {
        //设置底部横向平移
        mFloor.setX(mFloor.getX() - mSpeed);
        //移动并生成管道
        if (mPipe0 != null) {
            int res = mPipe0.setX(mPipe0.getX() - mSpeed);
            if (res == Pipe.CODE_NEW_PIPE) {
                mPipe1 = new Pipe(getContext(), sceneW, sceneH, mPipeTopBm, mPipeBottomBm, mPipeExtendBm);
            } else if (res == Pipe.CODE_DESTROY_PIPE) {
                mPipe0 = null;
            }
        }
        if (mPipe1 != null) {
            int res = mPipe1.setX(mPipe1.getX() - mSpeed);
            if (res == Pipe.CODE_NEW_PIPE) {
                mPipe0 = new Pipe(getContext(), sceneW, sceneH, mPipeTopBm, mPipeBottomBm, mPipeExtendBm);
            } else if (res == Pipe.CODE_DESTROY_PIPE) {
                mPipe1 = null;
            }
        }
        //小鸟的变化
        mTmpBirdDis += mBirdAutoDownDist;
        mBird.setY(mBird.getY() + mTmpBirdDis);
        int minY = 0;
        int maxY = mFloor.getY();
        //如果小鸟到达第一根管道
        if (mPipe0 != null && Math.abs(mBird.getX() - mPipe0.getLocateX()) < mPipe0.getPipeWidth() / 2f) {
            minY = Math.max(minY, mPipe0.getTopY());
            maxY = Math.min(maxY, mPipe0.getBottomY());
        }
        //如果小鸟到达第二根管道
        if (mPipe1 != null && Math.abs(mBird.getX() - mPipe1.getLocateX()) < mPipe1.getPipeWidth() / 2f) {
            minY = Math.max(minY, mPipe1.getTopY());
            maxY = Math.min(maxY, mPipe1.getBottomY());
        }
        //如果没有通过
        if (mBird.getY() < minY || mBird.getY() > maxY) {
            failed();
        }
        //获得得分
        if (mPipe0 != null && mBird.getX() > (mPipe0.getLocateX() + mPipe0.getPipeWidth() / 2f) && !mPipe0.isPass()) {
            score_current++;
            mPipe0.pass();
        }
        if (mPipe1 != null && mBird.getX() > (mPipe1.getLocateX() + mPipe1.getPipeWidth() / 2f) && !mPipe1.isPass()) {
            score_current++;
            mPipe1.pass();
        }
    }

    private void drawBg(Canvas canvas) {
        canvas.drawBitmap(mBgBm, null, mDestRect, null);
    }

    private void drawBird(Canvas canvas) {
        mBird.draw(canvas);
    }


    private void drawPipe(Canvas canvas) {
        if (mPipe0 != null) {
            mPipe0.draw(canvas);
        }
        if (mPipe1 != null) {
            mPipe1.draw(canvas);
        }
    }

    private void drawFloor(Canvas canvas) {
        mFloor.draw(canvas);
    }

    private void drawLabel(Canvas canvas) {
        if (mStartLabel != null) {
            mStartLabel.draw(canvas);
        }
        if (mPauseLabel != null) {
            mPauseLabel.draw(canvas);
        }
    }


    private void start() {
        mStartLabel = null;
        status_flag = STATUS_GOING;
    }

    private void failed() {
        status_flag = STATUS_PAUSE;
        mPauseLabel = new PauseLabel(getContext(), sceneW, sceneH, mNumBmList);
        mPauseLabel.setCurrentScore(score_current);
        mPauseLabel.setBestScore(Math.max(score_best, score_current));
        if (score_current > score_best) {
            score_best = score_current;
            SharedPreferences.Editor editor = sp.edit();
            editor.putInt(SP_BEST_SCORE, score_best);
            editor.apply();
        }
    }

    private void restart() {
        mPauseLabel = null;
        mBird.reset();
        score_current = 0;
        mTmpBirdDis = 0;
        mPipe0 = new Pipe(getContext(), sceneW, sceneH, mPipeTopBm, mPipeBottomBm, mPipeExtendBm);
        mPipe1 = null;
        status_flag = STATUS_GOING;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int action = event.getAction();
        if (action == MotionEvent.ACTION_DOWN) {
            if (status_flag == STATUS_START) {
                start();
                return true;
            } else if (status_flag == STATUS_GOING) {
                mTmpBirdDis = mBirdUpDis;
                return true;
            } else if (status_flag == STATUS_PAUSE) {
                restart();
                return true;
            }
        }
        return super.onTouchEvent(event);
    }
}
