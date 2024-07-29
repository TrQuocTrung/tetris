package com.example.tetris;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Shader;
import android.view.View;

public class TetrisView extends View {
    private Paint paint = new Paint();
    private TetrisGame game;
    private boolean isFastDropping = false;

    public TetrisView(Context context) {
        super(context);
        game = new TetrisGame();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        drawGrid(canvas);
        drawBlock(canvas);
        if (game.isGameOver()) {
            drawGameOver(canvas);
        }
    }

    private void drawGrid(Canvas canvas) {
        int[][] grid = game.getGrid();
        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid[i].length; j++) {
                if (grid[i][j] != 0) {
                    paint.setColor(grid[i][j]);
                    canvas.drawRect(j * 80, i * 80, (j + 1) * 80, (i + 1) * 80, paint); // Thay đổi từ 60 thành 80
                    // Vẽ viền
                    paint.setColor(Color.BLACK);
                    paint.setStyle(Paint.Style.STROKE);
                    paint.setStrokeWidth(6); // Độ dày của viền
                    paint.setStrokeJoin(Paint.Join.ROUND); // Bo góc
                    canvas.drawRect(j * 80, i * 80, (j + 1) * 80, (i + 1) * 80, paint); // Thay đổi từ 60 thành 80
                    paint.setStyle(Paint.Style.FILL);
                }
            }
        }
    }

    private void drawBlock(Canvas canvas) {
        Block block = game.getCurrentBlock();
        int[][] shape = block.getShape();
        int blockSize = 80; // Kích thước của mỗi ô trong khối

        for (int i = 0; i < shape.length; i++) {
            for (int j = 0; j < shape[i].length; j++) {
                if (shape[i][j] != 0) {
                    int left = (block.getX() + j) * blockSize;
                    int top = (block.getY() + i) * blockSize;
                    int right = left + blockSize;
                    int bottom = top + blockSize;

                    int colorStart = block.getColor(); // Màu bắt đầu từ khối
                    int colorEnd = Color.WHITE; // Màu kết thúc (ví dụ trắng)

                    // Tạo gradient từ màu bắt đầu đến màu kết thúc
                    LinearGradient gradient = new LinearGradient(left, top, right, bottom,
                            colorStart, colorEnd, Shader.TileMode.CLAMP);

                    paint.setShader(gradient);
                    canvas.drawRect(left, top, right, bottom, paint);
                    paint.setShader(null); // Đặt lại Shader sau khi vẽ xong để tránh ảnh hưởng đến các vẽ sau

                    // Vẽ viền
                    paint.setColor(Color.BLACK);
                    paint.setStyle(Paint.Style.STROKE);
                    paint.setStrokeWidth(6); // Độ dày của viền
                    paint.setStrokeJoin(Paint.Join.ROUND); // Bo góc
                    canvas.drawRect(left, top, right, bottom, paint);
                    paint.setStyle(Paint.Style.FILL);
                }
            }
        }
    }



    private void drawGameOver(Canvas canvas) {
        paint.setColor(Color.BLACK);
        paint.setTextSize(100);
        canvas.drawText("Game Over", 50, 400, paint);
    }

    public void moveBlockLeft() {
        game.moveBlockLeft();
        invalidate();
    }

    public void moveBlockRight() {
        game.moveBlockRight();
        invalidate();
    }

    public void moveBlockDown() {
        game.moveBlockDown();
        invalidate();
    }

    public void dropBlock() {
        while (!game.isGameOver() && game.canMove(game.getCurrentBlock().getX(), game.getCurrentBlock().getY() + 1)) {
            game.moveBlockDown();
        }
        invalidate();
    }

    public void stopDropping() {
        isFastDropping = false;
        invalidate();
    }

    public void rotateBlock() {
        game.rotateBlock();
        invalidate();
    }

    public int getScore() {
        return game.getScore();
    }

    public boolean isGameOver() {
        return game.isGameOver();
    }

    public boolean isFastDropping() {
        return isFastDropping;
    }

    public void resetGame() {
        game.resetGame();
    }
}
