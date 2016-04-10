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
    enum EMarkingMode
    {
        MARK_BOMB,
        UNCOVER
    };
    private MainActivity _parentActivity;
    private Paint[] _numbersPaints;
    private Paint _blackPaint;
    private Paint _grayPaint;
    private Paint _redPaint;
    private Paint _yellowPaint;
    private Paint _greenPaint;
    private Paint _bluePaint;

    private int _tileSize;
    private enum ECaseState
    {
        COVERED,
        UNCOVERED,
        MARKED
    };

    private class Board
    {
        public Board(int width, int height, int bombCount)
        {
            Width = width;
            Height = height;
            BombCount = bombCount;
            DistanceMap = new int[Height][Width];
            MapState = new ECaseState[Height][Width];
            MarkingMode = EMarkingMode.UNCOVER;
            GameFinished = false;
            MarkedMines = 0;
            this._generateMap();
        }

        private void _incrementDistance(int x, int y)
        {
            if (x >= 0 && x < Width &&
                    y >= 0 && y < Height && DistanceMap[y][x] < 9)
                DistanceMap[y][x]++;
        }

        private void _generateMap()
        {
            for (int iY = 0; iY < Height; ++iY)
                for (int iX = 0; iX < Width; ++iX)
                {
                    DistanceMap[iY][iX] = 0;
                    MapState[iY][iX] = ECaseState.COVERED;
                }
            for (int i = BombCount; i > 0; --i)
            {
                int x = (int)(Math.random() * Width);
                int y = (int)(Math.random() * Height);

                if (DistanceMap[y][x] > 8)
                {
                    ++i;
                    continue;
                }
                DistanceMap[y][x] = 9;
            }
            for (int y = 0; y < Height; ++y)
                for (int x = 0; x < Width; ++x) if (DistanceMap[y][x] > 8) {
                    _incrementDistance(x - 1, y);
                    _incrementDistance(x - 1, y + 1);
                    _incrementDistance(x, y + 1);
                    _incrementDistance(x + 1, y + 1);
                    _incrementDistance(x + 1, y);
                    _incrementDistance(x + 1, y - 1);
                    _incrementDistance(x, y - 1);
                    _incrementDistance(x - 1, y - 1);
                }
        }

        public final int BombCount;
        public final int Width;
        public final int Height;
        public ECaseState[][] MapState;
        public int[][] DistanceMap;
        public EMarkingMode MarkingMode;
        public boolean GameFinished;
        public int MarkedMines;
    }
    private Board _board;
    public MinesweeperGrid(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public void init()
    {
        //this.setBackgroundColor(Color.argb(255, 0, 0, 0));
        _parentActivity = (MainActivity)getContext();
        this.ResetGame(10, 10, 20);
        _blackPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        _blackPaint.setColor(0xFF000000);
        _grayPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        _grayPaint.setColor(0xFF424242);
        _bluePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        _bluePaint.setColor(0xFF0000FF);
        _greenPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        _greenPaint.setColor(0xFF00FF00);
        _yellowPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        _yellowPaint.setColor(0xFFFFFF00);
        _redPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        _redPaint.setColor(0xFFFF0000);
        _numbersPaints = new Paint[] {
            _bluePaint, _greenPaint, _yellowPaint, _redPaint
        };
        for (int i = 0; i < 4; ++i)
            _numbersPaints[i].setTextSize(50);
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


        for (int iY = 0; iY < _board.Height; ++iY)
        {
            for (int iX = 0; iX < _board.Width; ++iX)
            {
                switch (_board.MapState[iY][iX])
                {
                    case UNCOVERED:
                    {
                        if (_board.DistanceMap[iY][iX] == 9)
                        {
                            canvas.drawRect(iX * _tileSize, iY * _tileSize, (iX + 1) * _tileSize - 1, (iY + 1) * _tileSize - 1, _redPaint);
                            canvas.drawText("M", (iX + 0.2f) * _tileSize, (iY + 0.8f) * _tileSize, _blackPaint);
                        }
                        else
                        {
                            canvas.drawRect(iX * _tileSize, iY * _tileSize, (iX + 1) * _tileSize - 1, (iY + 1) * _tileSize - 1, _grayPaint);
                            if (_board.DistanceMap[iY][iX] > 0)
                            {
                                //String.valueOf(_board.DistanceMap[iY][iX])
                                Paint paintToDraw = _numbersPaints[Math.min(_board.DistanceMap[iY][iX] - 1, 3)];
                                canvas.drawText(Integer.toString(_board.DistanceMap[iY][iX]), (iX + 0.2f) * _tileSize, (iY + 0.8f) * _tileSize, paintToDraw);
                            }
                        }
                        break;
                    }
                    case COVERED:
                    {
                        canvas.drawRect(iX * _tileSize, iY * _tileSize, (iX + 1) * _tileSize - 1, (iY + 1) * _tileSize - 1, _blackPaint);
                        break;
                    }
                    case MARKED:
                    {
                        canvas.drawRect(iX * _tileSize, iY * _tileSize, (iX + 1) * _tileSize - 1, (iY + 1) * _tileSize - 1, _yellowPaint);
                        break;
                    }
                }
            }
        }
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        final int width = getMeasuredWidth();
        final int height = getMeasuredHeight();
        final int size = (width < height ? width : height);
        setMeasuredDimension(size, size);
        _tileSize = Math.min(size / _board.Width, size / _board.Height);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (!_board.GameFinished) {
            // Index of case clicked
            final int xidx = (int) (event.getX() / _tileSize);
            final int yidx = (int) (event.getY() / _tileSize);
            switch (_board.MarkingMode)
            {
                case MARK_BOMB:
                {
                    switch (_board.MapState[yidx][xidx])
                    {
                        case COVERED:
                        {
                            _board.MapState[yidx][xidx] = ECaseState.MARKED;
                            ++_board.MarkedMines;
                            _parentActivity.SetMarkedMines(_board.MarkedMines);
                            this.invalidate();
                            break;
                        }
                        case MARKED:
                        {
                            _board.MapState[yidx][xidx] = ECaseState.COVERED;
                            --_board.MarkedMines;
                            _parentActivity.SetMarkedMines(_board.MarkedMines);
                            this.invalidate();
                            break;
                        }
                    }
                    break;
                }
                case UNCOVER:
                {
                    if (_board.MapState[yidx][xidx] == ECaseState.COVERED) {
                        _board.MapState[yidx][xidx] = ECaseState.UNCOVERED;
                        if (_board.DistanceMap[yidx][xidx] == 9)
                            _setGameFinished();
                        this.invalidate();
                    }
                    break;
                }
            }
        }
        return super.onTouchEvent(event);
    }

    private void _setGameFinished()
    {
        _parentActivity.SetGameFinished();
        _board.GameFinished = true;
    }

    public String SwitchAndGetMode(boolean switchMode)
    {
        if (switchMode)
            _board.MarkingMode = (_board.MarkingMode == EMarkingMode.MARK_BOMB ? EMarkingMode.UNCOVER : EMarkingMode.MARK_BOMB);
        return (_board.MarkingMode == EMarkingMode.MARK_BOMB ? "Marking mode" : "Uncover mode");
    }

    public int GetMinesCount()
    {
        return _board.BombCount;
    }
}
