package com.thecookiezen.bussiness.control;

import com.google.common.base.Preconditions;
import com.thecookiezen.bussiness.entity.Line;
import com.thecookiezen.bussiness.entity.Point;
import com.thecookiezen.bussiness.entity.Rectangle;

import java.util.Arrays;

public class Canvas {

    private static final char EMPTY_SPACE = ' ';

    private final char[][] drawableArea;

    public Canvas(final int width, final int height) {
        Preconditions.checkArgument(width > 1, "Canvas width must be greater than 1");
        Preconditions.checkArgument(height > 1, "Canvas height must be greater than 1");

        this.drawableArea = new char[width][height];

        for (char[] row : this.drawableArea) {
            Arrays.fill(row, EMPTY_SPACE);
        }
    }

    public void drawLine(Line line) {
        Arrays.stream(line.getPoints())
                .filter(this::isPointWithinBoundary)
                .forEach(p -> this.drawableArea[p.getX()][p.getY()] = Line.FILL_CHARACTER);
    }

    public char[][] getDrawableArea() {
        char[][] copyOf = new char[drawableArea.length][];
        for (int x = 0; x < drawableArea.length; x++) {
            copyOf[x] = new char[drawableArea[x].length];
            System.arraycopy(drawableArea[x], 0, copyOf[x], 0, drawableArea[x].length);
        }
        return copyOf;
    }

    public void drawRectangle(Rectangle rectangle) {
        for (Line line : rectangle.getLines()) {
            drawLine(line);
        }
    }

    public void fill(Point point, char color) {
        fillPoint(point.getX(), point.getY(), color);
    }

    private void fillPoint(int x, int y, char color) {
        if (!isPointWithinBoundary(x, y) || !isPointEmpty(x, y, color)) {
            return;
        }
        drawableArea[x][y] = color;

        fillPoint(x, y + 1, color);
        fillPoint(x, y - 1, color);
        fillPoint(x - 1, y, color);
        fillPoint(x + 1, y, color);
    }

    private boolean isPointEmpty(int x, int y, char color) {
        return  drawableArea[x][y] != color
                && (drawableArea[x][y] == EMPTY_SPACE || drawableArea[x][y] != Line.FILL_CHARACTER);
    }

    private boolean isPointWithinBoundary(Point p) {
        return isPointWithinBoundary(p.getX(), p.getY());
    }

    private boolean isPointWithinBoundary(int x, int y) {
        return x >= 0 && x < drawableArea.length && y >= 0 && y < drawableArea[0].length;
    }
}
