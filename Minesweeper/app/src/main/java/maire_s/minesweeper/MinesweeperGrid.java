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
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

/**
 * Created by maire_s on 04/04/2016.
 */
public class MinesweeperGrid extends View {
    class Board
    {
        public Board(int width, int height, int bombCount)
        {
            Width = width;
            Height = height;
            _bombCount = bombCount;
        }
        private final int _bombCount;
        public final int Width;
        public final int Height;
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
        Paint red = new Paint(Paint.ANTI_ALIAS_FLAG);
        red.setColor(0xFFFF0000);
        int tileWidth = canvas.getWidth() / _board.Width;
        int tileHeight = canvas.getHeight() / _board.Height;
        int tileSize = (tileWidth < tileHeight ? tileWidth : tileHeight);
        for (int iY = 0; iY < _board.Height; ++iY)
        {
            for (int iX = 0; iX < _board.Width; ++iX) {
                canvas.drawRect(iX * tileSize, iY * tileSize, (iX + 1) * tileSize - 1, (iY + 1) * tileSize - 1, red);
            }
        }
    }
}
