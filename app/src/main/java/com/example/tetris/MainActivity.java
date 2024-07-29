package com.example.tetris;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
    private TetrisView tetrisView;
    private TextView scoreTextView;
    private LinearLayout gameOverLayout; // Layout chứa thông báo game over và nút reset

    private Button resetButton;

    private Handler handler = new Handler();
    private int normalInterval = 500;
    private int fastInterval = 50;
    private MediaPlayer mediaPlayer;

    private Runnable gameTick = new Runnable() {
        @Override
        public void run() {
            if (!tetrisView.isGameOver()) {
                if (tetrisView.isFastDropping()) {
                    tetrisView.moveBlockDown();
                    scoreTextView.setText("Score: " + tetrisView.getScore());
                    handler.postDelayed(this, fastInterval);
                } else {
                    tetrisView.moveBlockDown();
                    scoreTextView.setText("Score: " + tetrisView.getScore());
                    handler.postDelayed(this, normalInterval);
                }
            } else {
                showGameOver();
            }
        }
    };

    private Runnable moveLeft = new Runnable() {
        @Override
        public void run() {
            tetrisView.moveBlockLeft();
            handler.postDelayed(this, fastInterval);
        }
    };

    private Runnable moveRight = new Runnable() {
        @Override
        public void run() {
            tetrisView.moveBlockRight();
            handler.postDelayed(this, fastInterval);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Thiết lập fullscreen
        //getWindow().getDecorView().setSystemUiVisibility(
                //View.SYSTEM_UI_FLAG_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);


        tetrisView = new TetrisView(this);
        LinearLayout gameLayout = findViewById(R.id.gameView);
        gameLayout.addView(tetrisView);

        scoreTextView = findViewById(R.id.score);

        gameOverLayout = findViewById(R.id.gameOverLayout);
        resetButton = findViewById(R.id.resetButton);

        resetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetGame();
            }
        });

        gameOverLayout.setVisibility(View.GONE);

        ImageButton leftButton = findViewById(R.id.leftButton);
        ImageButton downButton = findViewById(R.id.downButton);
        ImageButton rightButton = findViewById(R.id.rightButton);
        ImageButton rotateButton = findViewById(R.id.rotateButton);

        leftButton.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        handler.post(moveLeft);
                        break;
                    case MotionEvent.ACTION_UP:
                        handler.removeCallbacks(moveLeft);
                        break;
                }
                return true;
            }
        });

        rightButton.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        handler.post(moveRight);
                        break;
                    case MotionEvent.ACTION_UP:
                        handler.removeCallbacks(moveRight);
                        break;
                }
                return true;
            }
        });

        downButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tetrisView.dropBlock();
                scoreTextView.setText("Score: " + tetrisView.getScore());
            }
        });

        rotateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tetrisView.rotateBlock();
            }
        });

        // Khởi tạo và phát âm nhạc nền
        mediaPlayer = MediaPlayer.create(this, R.raw.music);
        mediaPlayer.setLooping(true);
        mediaPlayer.start();

        handler.postDelayed(gameTick, normalInterval);
    }

    private void showGameOver() {
        handler.removeCallbacks(gameTick);

        gameOverLayout.setVisibility(View.VISIBLE);
        scoreTextView.setText("Game Over! Final Score: " + tetrisView.getScore());

        // Dừng và giải phóng âm nhạc nền khi game over
        if (mediaPlayer != null && mediaPlayer.isPlaying()) {
            mediaPlayer.stop();
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }

    private void resetGame() {
        gameOverLayout.setVisibility(View.GONE);
        tetrisView.resetGame();
        scoreTextView.setText("Score: 0");

        // Khởi tạo lại âm nhạc nền khi reset game
        if (mediaPlayer == null) {
            mediaPlayer = MediaPlayer.create(this, R.raw.music);
            mediaPlayer.setLooping(true);
            mediaPlayer.start();
        }

        handler.postDelayed(gameTick, normalInterval);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        handler.removeCallbacks(gameTick);
    }
}
