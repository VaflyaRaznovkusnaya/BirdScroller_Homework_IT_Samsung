package ru.samsung.itschool.mdev.funnybirds;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.media.MediaPlayer;
import android.os.CountDownTimer;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;

public class GameView extends View {

    private Sprite playerBird;
    private Sprite enemyBird;
    private Sprite enemyClickBird;
    private Sprite powerUp;

    private  Sprite pauseIcon;
    private  Sprite pauseText;
    private  Sprite failText;
    //private  Bitmap pauseText;
    //private  Bitmap failText;
    private  Bitmap backgroundPic;
    private int backgroundX = 0;
    private int backNewWidth;
    private int viewWidth;
    private int viewHeight;

    private int points = 0;
    private int level = 0;

    //to pause the whole app
    private boolean isPaused = false;
    // to show a proper picture
    private boolean isFailed = false;

    private final int timerInterval = 30;

    public GameView(Context context) {
        super(context);
        //----------------------- player
        Bitmap b = BitmapFactory.decodeResource(getResources(), R.drawable.player_bird);
        int w = b.getWidth()/3;
        int h = b.getHeight()/3;
        Rect firstFrame = new Rect(0, 0, w, h);
        playerBird = new Sprite(10, 1000, 0, 100, firstFrame, b);

        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (i ==0 && j == 0) {
                    continue;
                }
                playerBird.addFrame(new Rect(j*w, i*h, j*w+w, i*h+h));
            }
        }
        //------------------------- enemy clickable

        b = BitmapFactory.decodeResource(getResources(), R.drawable.click_enemy);
        w = b.getWidth()/3;
        h = b.getHeight()/3;
        firstFrame = new Rect(2*w, 0, 3*w, h);

        enemyClickBird = new Sprite(2000, 1000, -400, 0, firstFrame, b);

        for (int i = 0; i < 3; i++) {
            for (int j = 2; j >= 0; j--) {
                if (i ==0 && j == 2) {
                    continue;
                }
                enemyClickBird.addFrame(new Rect(j*w, i*h, j*w+w, i*h+h));
            }
        }
        //------------------------- enemy

        b = BitmapFactory.decodeResource(getResources(), R.drawable.enemy_bird);
        w = b.getWidth()/3;
        h = b.getHeight()/3;
        firstFrame = new Rect(2*w, 0, 3*w, h);

        enemyBird = new Sprite(2000, 250, -300, 0, firstFrame, b);

        for (int i = 0; i < 3; i++) {
            for (int j = 2; j >= 0; j--) {
                if (i ==0 && j == 2) {
                    continue;
                }
                enemyBird.addFrame(new Rect(j*w, i*h, j*w+w, i*w+w));
            }
        }
        //---------------------- powerEgg
        b = BitmapFactory.decodeResource(getResources(), R.drawable.power_egg);
        w = b.getWidth()/9;
        h = b.getHeight();
        firstFrame = new Rect(0, 0, w, h);

        powerUp = new Sprite(2000, 550, -200, 0, firstFrame, b);

        for (int i = 0; i < 8; i++) {
            if (i ==0) {
                continue;
            }
            powerUp.addFrame(new Rect(i*w, 0, i*w+w, h));
        }

        //------------------- menus stuff
        b = BitmapFactory.decodeResource(getResources(), R.drawable.pause_icon);
        firstFrame = new Rect(0, 0, b.getWidth(), b.getHeight());
        pauseIcon = new Sprite(0, 40, 0, 0, firstFrame, b);

        b = BitmapFactory.decodeResource(getResources(), R.drawable.pause_text);
        firstFrame = new Rect(0, 0, b.getWidth(), b.getHeight());
        pauseText = new Sprite(200, 500, 0, 0, firstFrame, b);

        b = BitmapFactory.decodeResource(getResources(), R.drawable.fail);
        firstFrame = new Rect(0, 0, b.getWidth(), b.getHeight());
        failText = new Sprite(50, 500, 0, 0, firstFrame, b);
        //pauseIcon = BitmapFactory.decodeResource(getResources(), R.drawable.pause_icon);
        //pauseText = BitmapFactory.decodeResource(getResources(), R.drawable.pause_text);
        //failText =BitmapFactory.decodeResource(getResources(), R.drawable.fail);

        backgroundPic = BitmapFactory.decodeResource(getResources(), R.drawable.background);
        backNewWidth = backgroundPic.getWidth()/2;
        //backgroundPic = Bitmap.createScaledBitmap(backgroundPic, backNewWidth,viewHeight, false);
        //------------------- timer

        Timer t = new Timer();
        t.start();
    }

    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        viewWidth = w;
        viewHeight = h;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawARGB(250, 166, 115, 91);
        if (!isPaused && !isFailed){
            backgroundX -= 5;
            if (backgroundX < -backNewWidth){
                backgroundX = 0;
            }
        }
        canvas.drawBitmap(backgroundPic, backgroundX,0,null);

        pauseIcon.draw(canvas);

        Paint p = new Paint();

        p.setAntiAlias(true);
        p.setTextSize(55.0f);
        p.setColor(Color.WHITE);
        canvas.drawText(points + " points", viewWidth/2.0f, 70, p);
        canvas.drawText("level " + (level + 1), viewWidth/2.0f, 120, p);

        playerBird.draw(canvas);
        powerUp.draw(canvas);
        enemyBird.draw(canvas);
        enemyClickBird.draw(canvas);

        pauseIcon.draw(canvas);

        if (isPaused){
            //canvas.drawBitmap(pauseText,getWidth()/2.0f, getHeight()/2.0f,p);
            pauseText.draw(canvas);
            p.setColor(Color.BLACK);
            canvas.drawText("Pause", viewWidth/2.5f, 450, p);
            canvas.drawText("Click to continue", viewWidth/3.9f, getHeight() - 500, p);
        }
        else if (isFailed){
            //canvas.drawBitmap(failText,getWidth()/2.0f, getHeight()/2.0f,p);
            failText.draw(canvas);
            p.setColor(Color.BLACK);
            canvas.drawText("Click to restart", viewWidth/3.0f, getHeight() - 500,p);
        }
    }

    protected void update () {
        playerBird.update(timerInterval);
        enemyBird.update(timerInterval);
        powerUp.update(timerInterval);
        enemyClickBird.update(timerInterval);

        //lvl up!
        if (points < -100){
            isFailed = true;
            enemyBird.setVx(0);
            enemyClickBird.setVx(0);
            powerUp.setVx(0);
            playerBird.setVy(0);
            //invalidate();
        }

        if (points > 250) {
            points = 0;
            level++;

            //update speed
            enemyBird.setVx(-(300 + level * 10));
            enemyClickBird.setVx(-(400 + level * 10));
            powerUp.setVx(-(200 + level * 10));
        }

        if (playerBird.getY() + playerBird.getFrameHeight() > viewHeight) {
            playerBird.setY(viewHeight - playerBird.getFrameHeight());
            playerBird.setVy(-playerBird.getVy());
            points--;
        } else if (playerBird.getY() < 0) {
            playerBird.setY(0);
            playerBird.setVy(-playerBird.getVy());
            points--;
        }

        // Enemy positions
        if (enemyBird.getX() < -enemyBird.getFrameWidth()) {
            teleportEnemy();
            points += 12;
        }
        if (enemyBird.intersect(playerBird)) {
            teleportEnemy();
            points -= 50;
        }

        // Clickable Enemy positions
        if (enemyClickBird.getX() < -enemyClickBird.getFrameWidth()) {
            teleportClickEnemy();
            points += 1;
        }
        if (enemyClickBird.intersect(playerBird)) {
            teleportClickEnemy();
            points -= 40;
        }

        // Power Up positions
        if (powerUp.getX() < -powerUp.getFrameWidth()) {
            teleportPower();
        }
        if (powerUp.intersect(playerBird)) {
            teleportPower();
            points += 30;
        }
        //invalidate();
        invalidate();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        // Событие при касании экрана
        if (isPaused || isFailed){
            if (pauseText.getBoundingBoxRect().contains((int)event.getX(),(int)event.getY())){
                isPaused = false;
                enemyBird.setVx(-(300 + level*10));
                enemyClickBird.setVx(-(400 + level*10));
                powerUp.setVx(-(200 + level*10));
            }
            else if (failText.getBoundingBoxRect().contains((int)event.getX(),(int)event.getY())){
                isFailed = false;
                points = 0;
                level = 0;
                teleportClickEnemy();
                teleportEnemy();
                teleportPower();
                enemyBird.setVx(-(300 + level*10));
                enemyClickBird.setVx(-(400 + level*10));
                powerUp.setVx(-(200 + level*10));
            }
        }
        else{
            //game is fine
            int eventAction = event.getAction();
            if (eventAction == MotionEvent.ACTION_DOWN)  {
                //click on enemy
                if (enemyClickBird.getBoundingBoxRect().contains((int)event.getX(),(int)event.getY())){
                    teleportClickEnemy();
                    points += 30;
                }
                //click on pause icon
                else if (pauseIcon.getBoundingBoxRect().contains((int)event.getX(),(int)event.getY())){
                    isPaused = true;
                    //pause
                    enemyBird.setVx(0);
                    enemyClickBird.setVx(0);
                    powerUp.setVx(0);
                    playerBird.setVy(0);
                    //invalidate();
                }
                else if (event.getY() < playerBird.getBoundingBoxRect().top) {
                    playerBird.setVy(-(130 + level*10));
                    points--;
                }
                else if (event.getY() > (playerBird.getBoundingBoxRect().bottom)) {
                    playerBird.setVy((130 + level*10));
                    points--;
                }
            }
        }

        return true;
    }


    private void teleportEnemy () {
        enemyBird.setX(viewWidth + Math.random() * 500);
        enemyBird.setY(Math.random() * (viewHeight - enemyBird.getFrameHeight()));
    }
    private void teleportClickEnemy () {
        enemyClickBird.setX(viewWidth + Math.random() * 500);
        enemyClickBird.setY(Math.random() * (viewHeight - enemyClickBird.getFrameHeight()));
    }
    private void teleportPower () {
        powerUp.setX(viewWidth + Math.random() * 500);
        powerUp.setY(Math.random() * (viewHeight - powerUp.getFrameHeight()));
    }

    class Timer extends CountDownTimer {

        public Timer() {
            super(Integer.MAX_VALUE, timerInterval);
        }

        @Override
        public void onTick(long millisUntilFinished) {

            update ();
        }

        @Override
        public void onFinish() {

        }
    }
}
