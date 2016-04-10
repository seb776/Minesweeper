package maire_s.minesweeper;

import android.graphics.Paint;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

public class MainActivity extends AppCompatActivity {
    private Button _buttonMode;
    private Button _buttonReset;
    private MinesweeperGrid _minesweeper;
    private TextView _minesCount;
    private TextView _minesMarked;
    private Toast _toastLose;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        _buttonMode = ((Button)findViewById(R.id.buttonMode));
        _minesweeper = (MinesweeperGrid)findViewById(R.id.MinesweeperGrid);
        _minesCount = (TextView)findViewById(R.id.minesCountText);
        _minesMarked = (TextView)findViewById(R.id.minesMarkedText);
        _buttonReset = (Button)findViewById(R.id.buttonReset);
        _buttonMode.setText(_minesweeper.SwitchAndGetMode(false));
        _toastLose = Toast.makeText(this, "You loose", Toast.LENGTH_LONG);
        SetMinesCount(_minesweeper.GetMinesCount());
        SetMarkedMines(0);
    }
    public void SetMarkedMines(int count)
    {
        _minesMarked.setText(count + " mines marked.");
    }

    public void SetMinesCount(int count)
    {
        _minesCount.setText(count + " mines.");
    }

    public void SetGameFinished()
    {
        _buttonMode.setClickable(false);
        _toastLose.show();
    }
    public void onButtonClickReset(View view) {
        _toastLose.cancel(); // To avoid the toast to stay after restarting
        _minesweeper.ResetGame(10,10,20);
        _buttonMode.setClickable(true);
        SetMarkedMines(0);
    }

    public void onButtonClickMode(View view) {
        _buttonMode.setText(_minesweeper.SwitchAndGetMode(true));
    }
}
