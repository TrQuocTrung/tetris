package com.example.tetris;

public class Block {
    private int[][] shape;
    private int x, y;
    private int color;

    public Block(int[][] shape, int color) {
        this.shape = shape;
        this.color = color;
        x = 4; // khởi tạo vị trí x ở giữa lưới
        y = 0; // khởi tạo vị trí y ở đầu lưới
    }
    public int[][] getShape() {
        return shape;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getColor() {
        return color;
    }

    public void moveLeft() {
        x--;
    }

    public void moveRight() {
        x++;
    }

    public void moveDown() {
        y++;
    }

    public void rotate() {
        int n = shape.length;
        int[][] rotatedShape = new int[n][n];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                rotatedShape[j][n - i - 1] = shape[i][j];
            }
        }
        shape = rotatedShape;
    }
}

