package ru.ifmo.ctddev.skripnikov.androidhm1;

import java.util.Random;
import android.graphics.Color;
import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

public class MyActivity extends Activity {
    final int w = 240;
    final int h = 320;
    int[] pixels = new int[w * h];
    float wk = 3;
    float hk = 3;
    long time = 1;
    int cn = 16;
    int[] colors = new int[cn];
    int k = 255 / cn;
    int[][] field = new int[h + 2][w + 2];
    int[][] newField = new int[h + 2][w + 2];
    int[][] tmpField;
    Paint p = new Paint();

    public class MySurfaceView extends SurfaceView implements SurfaceHolder.Callback {
        private DrawThread drawThread;

        public MySurfaceView(Context context) {
            super(context);
            getHolder().addCallback(this);
        }

        @Override
        public void surfaceChanged(SurfaceHolder holder, int format, int width,
                                   int height) {
        }

        @Override
        public void surfaceCreated(SurfaceHolder holder) {
            wk = getWidth() / (float) 240.0;
            hk = getHeight() / (float) 320.0;
            drawThread = new DrawThread(getHolder());
            drawThread.setRunning(true);
            drawThread.start();
        }

        @Override
        public void surfaceDestroyed(SurfaceHolder holder) {
            boolean retry = true;
            drawThread.setRunning(false);
            while (retry) {
                try {
                    drawThread.join();
                    retry = false;
                } catch (InterruptedException e) {
                }
            }
        }


        class DrawThread extends Thread {
            private boolean runFlag = false;
            private SurfaceHolder surfaceHolder;
            Runnable r1 = new QThread(1,4);
            Runnable r2 = new QThread(2,4);
            Runnable r3 = new QThread(3,4);
            Runnable r4 = new QThread(4,4);

            public DrawThread(SurfaceHolder surfaceHolder) {
                this.surfaceHolder = surfaceHolder;
            }

            public void setRunning(boolean run) {
                runFlag = run;
            }

            @Override
            public void run() {
                Canvas canvas;
                while (runFlag) {
                    canvas = null;
                    try {
                        canvas = surfaceHolder.lockCanvas(null);
                        if (canvas != null) {
                            synchronized (surfaceHolder) {
                                long newTime = System.currentTimeMillis();
                                long d = newTime - time;
                                time = newTime;
                                long fps = 1000 / d;
                                nextField();
                                long time2 = System.currentTimeMillis();
                                drawField(canvas);
                                long time3 = System.currentTimeMillis();
                                canvas.drawText(Long.toString(fps) + " FPS", 20, 20, p);
                                canvas.drawText("U: " + Long.toString(time2 - time), 20, 50, p);
                                canvas.drawText("D: " + Long.toString(time3 - time2), 20, 80, p);
                            }
                        }
                    } finally {
                        if (canvas != null) {
                            surfaceHolder.unlockCanvasAndPost(canvas);
                        }
                    }
                }
            }

            private void nextField() {
                newBorder();
                Thread t1 = new Thread(r1);
                Thread t2 = new Thread(r2);
                Thread t3 = new Thread(r3);
                Thread t4 = new Thread(r4);
                t1.start();
                t2.start();
                t3.start();
                t4.start();

                try {
                    t1.join();
                    t2.join();
                    t3.join();
                    t4.join();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                tmpField = field;
                field = newField;
                newField = tmpField;
            }

            private void drawField(Canvas canvas) {
                canvas.scale(wk, hk);
                canvas.drawBitmap(pixels, 0, w, 0, 0, w, h, false, p);
            }

            private void newBorder() {
                for (int i = 1; i <= w; i++) {
                    field[0][i] = field[h][i];
                    field[h + 1][i] = field[1][i];
                }
                for (int i = 1; i <= h; i++) {
                    field[i][0] = field[i][w];
                    field[i][w + 1] = field[i][1];
                }
                field[0][0] = field[h][w];
                field[h + 1][0] = field[1][w];
                field[h + 1][w + 1] = field[1][1];
                field[0][w + 1] = field[h][1];
            }
            class QThread implements Runnable {
                int part;
                int size;
                public QThread(int part, int size) {
                    this.part = part;
                    this.size = size;
                }

                public void run() {
                    int next;
                    int start = h/size*(part-1) + 1;
                    int end = h/size*part;
                    for (int i = start; i <= end; i++)
                        for (int j = 1; j <= w; j++) {
                            next = field[i][j] + 1;
                            if (next == 16) {
                                next = 0;
                            }
                            if (next == field[i - 1][j - 1]
                                    || next == field[i - 1][j]
                                    || next == field[i - 1][j + 1]
                                    || next == field[i][j - 1]
                                    || next == field[i][j + 1]
                                    || next == field[i + 1][j - 1]
                                    || next == field[i + 1][j]
                                    || next == field[i + 1][j + 1]) {
                                newField[i][j] = next;
                                pixels[(i - 1) * w + j - 1] = colors[field[i][j]];
                            } else {
                                newField[i][j] = field[i][j];
                            }
                        };
                }
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Random r = new Random();
        p.setTextSize(20);
        p.setARGB(255, 255, 255, 255);
        for (int i = 0; i < cn; i++)
            colors[i] = Color.argb(255, i * k, 128, 255 - i * k);
        for (int i = 1; i <= h; i++)
            for (int j = 1; j <= w; j++) {
                field[i][j] = r.nextInt(cn);
                pixels[(i - 1) * w + j - 1] = colors[field[i][j]];
            }
        super.onCreate(savedInstanceState);
        setContentView(new MySurfaceView(this));
    }
}
