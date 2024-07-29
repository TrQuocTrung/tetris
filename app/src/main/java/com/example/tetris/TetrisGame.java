package com.example.tetris;

import android.graphics.Color;

import java.util.Random;

public class TetrisGame {
    public static final int WIDTH = 10; // Số lượng cột
    public static final int HEIGHT = 20; // Số lượng hàng
    private int[][] grid;
    private Block currentBlock;
    private Random random;
    private int score;
    private boolean gameOver;

    public TetrisGame() {
        grid = new int[HEIGHT][WIDTH];
        random = new Random();
        spawnBlock();
        score = 0;
        gameOver = false;
    }

    private void spawnBlock() {
        int[][][] shapes = {
                {{1, 1, 1, 1}, {0, 0, 0, 0}, {0, 0, 0, 0}, {0, 0, 0, 0}},
                {{1, 1}, {1, 1}},
                {{0, 1, 0}, {1, 1, 1}, {0, 0, 0}},
                {{1, 1, 0}, {0, 1, 1}, {0, 0, 0}},
                {{0, 1, 1}, {1, 1, 0}, {0, 0, 0}},
                {{1, 1, 1}, {1, 0, 0}, {0, 0, 0}},
                {{1, 1, 1}, {0, 0, 1}, {0, 0, 0}}
        };
        int[] colors = {
                Color.RED, Color.GREEN, Color.BLUE, Color.YELLOW, Color.CYAN, Color.MAGENTA, Color.DKGRAY
        };
        int index = random.nextInt(shapes.length);
        currentBlock = new Block(shapes[index], colors[index]);
        if (!canMove(currentBlock.getX(), currentBlock.getY())) {
            gameOver = true;
        }
    }


    public void moveBlockLeft() {
        if (!gameOver && canMove(currentBlock.getX() - 1, currentBlock.getY())) {
            currentBlock.moveLeft();
        }
    }

    public void moveBlockRight() {
        if (!gameOver && canMove(currentBlock.getX() + 1, currentBlock.getY())) {
            currentBlock.moveRight();
        }
    }

    public void moveBlockDown() {
        if (!gameOver) {
            if (canMove(currentBlock.getX(), currentBlock.getY() + 1)) {
                currentBlock.moveDown();
            } else {
                mergeBlockToGrid();
                clearFullRows();
                spawnBlock();
            }
        }
    }
    public void rotateBlock() {
        if (!gameOver) {
            currentBlock.rotate();
            if (!canMove(currentBlock.getX(), currentBlock.getY())) {
                currentBlock.rotate();
                currentBlock.rotate();
                currentBlock.rotate(); // Quay ngược lại
            }
        }

    }

    public void resetGame() {
        grid = new int[HEIGHT][WIDTH];
        score = 0;
        gameOver = false;
        spawnBlock();
    }

    public boolean canMove(int newX, int newY) {
        int[][] shape = currentBlock.getShape();
        for (int i = 0; i < shape.length; i++) {
            for (int j = 0; j < shape[i].length; j++) {
                if (shape[i][j] != 0) {
                    int x = newX + j;
                    int y = newY + i;
                    if (x < 0 || x >= grid[0].length || y < 0 || y >= grid.length || grid[y][x] != 0) {
                        return false;
                    }
                }
            }
        }
        return true;
    }


    private void mergeBlockToGrid() {
        int[][] shape = currentBlock.getShape();
        for (int i = 0; i < shape.length; i++) {
            for (int j = 0; j < shape[i].length; j++) {
                if (shape[i][j] != 0) {
                    grid[currentBlock.getY() + i][currentBlock.getX() + j] = currentBlock.getColor();
                }
            }
        }
    }


    private void clearFullRows() {
        for (int i = grid.length - 1; i >= 0; i--) {
            boolean fullRow = true;
            for (int j = 0; j < grid[i].length; j++) {
                if (grid[i][j] == 0) {
                    fullRow = false;
                    break;
                }
            }
            if (fullRow) {
                score += 100;
                for (int k = i; k > 0; k--) {
                    grid[k] = grid[k - 1];
                }
                grid[0] = new int[grid[0].length];
                i++;
            }
        }
    }

    public int[][] getGrid() {
        return grid;
    }

    public Block getCurrentBlock() {
        return currentBlock;
    }

    public int getScore() {
        return score;
    }

    public boolean isGameOver() {
        return gameOver;
    }
}
