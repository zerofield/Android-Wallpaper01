package zerofield.mywallpaper;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.PointF;
import android.service.wallpaper.WallpaperService;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;

import java.util.List;

/**
 */
public class MyWallpaperService extends WallpaperService {


    @Override
    public Engine onCreateEngine() {
        return new MyEngine();
    }


    private class MyEngine extends Engine implements Runnable {

        private final boolean DEBUG = true;

        private final String TAG = getClass().getSimpleName();

        private Paint mPaint = new Paint();

        private volatile boolean mRunning = false;

        private Thread mRenderThread;

        private PointF velocity = new PointF(100, 100);
        private PointF position = new PointF(100, 100);
        private float radius = 100;

        public MyEngine() {
            mRunning = false;

            float density = getResources().getDisplayMetrics().density;
            velocity.x *= density;
            velocity.y *= density;


            radius *= density;
            position.x = radius;
            position.y = radius;
        }

        @Override
        public void run() {
            Log.d(TAG, "run: start running");
            long startTime = System.currentTimeMillis();

            while (mRunning) {
                //do stuff


                SurfaceHolder holder = getSurfaceHolder();

                if (!holder.getSurface().isValid()) {
                    continue;
                }

                float dt = (System.currentTimeMillis() - startTime) * 0.001f;
                startTime = System.currentTimeMillis();
                Canvas canvas = holder.lockCanvas();
                update(canvas, dt);
                holder.unlockCanvasAndPost(canvas);

            }

            Log.d(TAG, "run: stop running");

        }


        private void update(Canvas canvas, float dt) {
            canvas.drawColor(Color.BLACK);
            mPaint.setColor(Color.WHITE);
            int width = canvas.getWidth();
            int height = canvas.getHeight();

            position.x += velocity.x * dt;
            position.y += velocity.y * dt;

            if (position.x <= radius) {
                position.x = radius;
                velocity.x *= -1;
            }

            if (position.x >= width - radius) {
                position.x = width - radius;
                velocity.x *= -1;
            }

            if (position.y <= radius) {
                position.y = radius;
                velocity.y *= -1;
            }

            if (position.y >= height - radius) {
                position.y = height - radius;
                velocity.y *= -1;
            }

            canvas.drawCircle(position.x, position.y, radius, mPaint);
        }


        @Override
        public void onVisibilityChanged(boolean visible) {
            super.onVisibilityChanged(visible);

            Log.d(TAG, "onVisibilityChanged: " + visible);

            if (visible) {
                startRendering();
            } else {
                stopRendering();
            }

        }

        @Override
        public void onSurfaceCreated(SurfaceHolder holder) {
            super.onSurfaceCreated(holder);
            Log.d(TAG, "onSurfaceCreated");
            startRendering();
        }

        private void startRendering() {

            if (mRunning) {
                return;
            }
            mRunning = true;
            mRenderThread = new Thread(this);
            mRenderThread.start();
        }

        private void stopRendering() {
            mRunning = false;
            if (mRenderThread == null) {
                return;
            }
            try {
                mRenderThread.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            mRenderThread = null;
        }


        @Override
        public void onSurfaceDestroyed(SurfaceHolder holder) {
            super.onSurfaceDestroyed(holder);
            Log.d(TAG, "onSurfaceDestroyed");
            stopRendering();
        }

        @Override
        public void onSurfaceChanged(SurfaceHolder holder, int format, int width, int height) {
            super.onSurfaceChanged(holder, format, width, height);
            Log.d(TAG, "onSurfaceChanged: ");
        }

        @Override
        public void onTouchEvent(MotionEvent event) {
            super.onTouchEvent(event);
            //Log.d(TAG, "onTouchEvent: ");
        }

        private void drawCircles(Canvas canvas, List<Point> circles) {
            canvas.drawColor(Color.BLACK);

            for (Point point : circles) {
                canvas.drawCircle(point.x, point.y, 20.0f, mPaint);
            }
        }

    }

}
