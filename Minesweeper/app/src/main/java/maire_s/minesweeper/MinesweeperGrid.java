package maire_s.minesweeper;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.provider.CalendarContract;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

/**
 * Created by maire_s on 04/04/2016.
 */
public class MinesweeperGrid extends View {
    private class Board
    {
        public Board(int width, int height, int bombCount)
        {
            Width = width;
            Height = height;
            BombCount = bombCount;
            Map = new boolean[Height][Width];
            DistanceMap = new char[Height][Width];
            Cover = new boolean[Height][Width];
            this._generateMap();
        }

        private void _incrementDistance(int x, int y)
        {
            if (x > 0 && x < Width &&
                    y > 0 && y < Height)
            {
                DistanceMap[y][x]++;
            }
        }

        private void _generateMap()
        {
            for (int iY = 0; iY < Height; ++iY)
                for (int iX = 0; iX < Width; ++iX)
                {
                    Map[iY][iX] = false;
                    DistanceMap[iY][iX] = 0;
                    Cover[iY][iX] = true;
                }
            for (int i = BombCount; i > 0; --i)
            {
                int x = (int)(Math.random() * Width);
                int y = (int)(Math.random() * Height);

                if (DistanceMap[y][x] == -1)
                {
                    ++i;
                    continue;
                }
                _incrementDistance(x - 1, y);
                _incrementDistance(x - 1, y + 1);
                _incrementDistance(x, y + 1);
                _incrementDistance(x + 1, y + 1);
                _incrementDistance(x + 1, y);
                _incrementDistance(x + 1, y - 1);
                _incrementDistance(x, y - 1);
                _incrementDistance(x - 1, y - 1);
                DistanceMap[y][x] = (char)-1;
            }
        }

        public final int BombCount;
        public final int Width;
        public final int Height;
        public boolean[][] Map;
        public boolean[][] Cover;
        public char[][] DistanceMap;
    }
    private Board _board;
    public MinesweeperGrid(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public void init()
    {
        //this.setBackgroundColor(Color.argb(255, 0, 0, 0));
        this.ResetGame(10, 10, 20);
        Log.d("BLOOP", "ZBRAAAA");
    }

    public void ResetGame(int width, int height, int bombCount)
    {
        _board = new Board(width, height, bombCount);
        this.invalidate();

    }


    // public method that needs to be overridden to draw the contents of this
    // widget
    @Override
    public void onDraw(Canvas canvas)
    {

        canvas.translate(0, 0);
        //canvas.getWidth() / _board.Width;
                        // call the superclass method
        super.onDraw(canvas);
        Paint black = new Paint(Paint.ANTI_ALIAS_FLAG);
        black.setColor(0xFF000000);

        Paint red = new Paint(Paint.ANTI_ALIAS_FLAG);
        red.setColor(0xFFFF0000);
        red.setTextSize(50);

        Paint gray = new Paint(Paint.ANTI_ALIAS_FLAG);
        gray.setColor(0xFF424242);
        int tileWidth = canvas.getWidth() / _board.Width;
        int tileHeight = canvas.getHeight() / _board.Height;
        int tileSize = (tileWidth < tileHeight ? tileWidth : tileHeight);
        for (int iY = 0; iY < _board.Height; ++iY)
        {
            for (int iX = 0; iX < _board.Width; ++iX) {
                if (_board.Cover[iY][iX])
                    canvas.drawRect(iX * tileSize, iY * tileSize, (iX + 1) * tileSize - 1, (iY + 1) * tileSize - 1, black);
                else
                {
                    canvas.drawRect(iX * tileSize, iY * tileSize, (iX + 1) * tileSize - 1, (iY + 1) * tileSize - 1, gray);
                    if (_board.DistanceMap[iY][iX] > 0) {
                        //String.valueOf(_board.DistanceMap[iY][iX])
                        canvas.drawText(Integer.toString(_board.DistanceMap[iY][iX]), (iX + 0.2f) * tileSize, (iY + 0.8f) * tileSize, red);
                    }
                }
            }
        }
    }
    @Override
    public boolean onTouchEvent(MotionEvent event) {

        int tileWidth = this.getWidth() / _board.Width;
        int tileHeight = this.getHeight() / _board.Height;
        int tileSize = (tileWidth < tileHeight ? tileWidth : tileHeight);
        int xidx = (int)(event.getX() / tileSize);
        int yidx = (int)(event.getY() / tileSize);
        _board.Cover[yidx][xidx] = false;
        this.invalidate();
        return super.onTouchEvent(event);
    }
}
