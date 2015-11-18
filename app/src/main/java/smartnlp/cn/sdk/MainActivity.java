package smartnlp.cn.sdk;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import org.apache.http.util.VersionInfo;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.FutureTask;
import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity {
    Robot robot = new Robot("test");
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        robot.getAnswer("你好！", new AsyncHandler<Answer>() {
            @Override
            public void onComplete(Answer result) {
                Log.d("Robot", "Answer: " + result);
            }
        });

        robot.setGlobalTimeOutInMilliSeconds(1000);
        Answer answer1 = robot.getAnswer("你好！");
        System.out.println(answer1);
        Answer answer2 = robot.getAnswer("你好！", 6000);
        System.out.println(answer2);



        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Button button = (Button) findViewById(R.id.button_send);
    }

    public void onSend(View view){
        EditText inputText = (EditText) findViewById(R.id.inputText);
        final TextView messageView = (TextView) findViewById(R.id.messageView);
        String q = inputText.getText().toString().trim();
        if(!q.equals("")){
            if(messageView.getText().toString().split("\r\n").length > 10){
                messageView.setText("Q: " + q + "\r\n");
            } else {
                messageView.append("Q: " + q + "\r\n");
            }

            robot.getAnswer(q, new AsyncHandler<Answer>() {
                @Override
                public void onComplete(final Answer result) {
                    MainActivity.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if(result.isSuccess){
                                messageView.append("A: " + result.answer + "\r\n");
                            } else {
                                messageView.append("A: " + result.errMessage + "\r\n");
                            }

                        }
                    });
                }
            });
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        robot.close();
    }
}
