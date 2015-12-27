// Based on tutorial from https://www.youtube.com/watch?v=wOCNnniCJfY
package co.lolphp.ciwsgame.ciwsgame;

import android.app.Activity;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;


public class GameActivity extends Activity {

    // Drawing variables (DO NOT MODIFY)
    Thread t = null;
    SurfaceHolder holder;
    boolean isRunning = false;

    // Game variables
    float x = 50;
    float y = 0;

    public class CanvasView extends SurfaceView implements Runnable {

        public CanvasView(Context context) {
            super(context);
            holder = getHolder();
        }

        public void run() {
            while (isRunning) {

                // === Validate + lock canvas (DO NOT MODIFY) ===
                if (!holder.getSurface().isValid()) {
                    continue;
                }
                Canvas canvas = holder.lockCanvas();

                // === Draw stuff (DO MODIFY!) ===
                // Green background
                canvas.drawARGB(255, 0, 255, 0);

                // Red ball
                Paint red = new Paint();
                red.setARGB(255,255,0,0);
                canvas.drawCircle(x, y, 5, red);
                y += 1;

                // === Unlock + update canvas (DO NOT MODIFY) ===
                holder.unlockCanvasAndPost(canvas);

            }
        };

        // On-pause
        public void pause() {
            isRunning = false;

            // End current thread
            while (true) {
                try {
                    t.join();
                    break;
                } catch (InterruptedException ex) {
                    // Go around...
                }
            }

            // Cleanup current thread
            t = null;
        }

        // On-resume
        public void resume() {
            isRunning = true;

            // Re[create/start] thread
            t = new Thread(this);
            t.start();
        }
    }

    CanvasView view;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Create CanvasView
        view = new CanvasView(this);
        setContentView(view);
    }

    @Override
    protected void onPause() {
        super.onPause();
        view.pause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        view.resume();
    }

    @Override
    public boolean onTouchEvent(MotionEvent motionEvent) {
        x = motionEvent.getX();
        y = motionEvent.getY();
        return false;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_game, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
