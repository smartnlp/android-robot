package smartnlp.cn.sdk;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

import java.net.URLEncoder;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

/**
 * Created by joymufeng on 2015/8/19.
 */
public class Robot {
    private long globalTimeOutInMilliSeconds = 90000;
    ExecutorService executor = Executors.newSingleThreadExecutor();
    ExecutorService checkExecutor = Executors.newSingleThreadExecutor();
    HttpClient client = new DefaultHttpClient();


    private String appKey = "";
    private String sessionId = "";
    public Robot(String appKey){
        this.appKey = appKey;
    }

    public Future<Answer> getAnswer(final String question, final AsyncHandler<Answer> handler){
        final Future<Answer> future =
            executor.submit(new Callable<Answer>() {
                @Override
                public Answer call() throws Exception {
                    Answer answer = new Answer();
                    try{
                        HttpGet httpGet = new HttpGet("http://api.smartnlp.cn/cloud/robot/" + appKey + "/answer?q=" + URLEncoder.encode(question, "UTF-8"));
                        HttpResponse response = client.execute(httpGet);
                        if(response.getStatusLine().getStatusCode() == 200){
                            answer.isSuccess = true;
                            String resStr = EntityUtils.toString(response.getEntity(), "UTF-8");
                            JSONObject jsonObj = new JSONObject(resStr);
                            answer.answer = jsonObj.getString("answer");
                            answer.question = jsonObj.getString("question");
                            answer.score = jsonObj.getDouble("score");
                        } else {
                            answer.isSuccess = false;
                            answer.errMessage = response.getStatusLine().getStatusCode() + ": " + EntityUtils.toString(response.getEntity(), "UTF-8");
                        }
                    }catch(Exception e){
                        answer.isSuccess = false;
                        answer.errMessage = e.getMessage();
                    }
                    return answer;
                }
            });

        //Check result and execute handler
        checkExecutor.execute(new Runnable() {
            @Override
            public void run() {
                while (!future.isDone()) {
                    try {
                        Thread.sleep(10);
                    } catch (Exception e) {
                    }
                }
                try {
                    handler.onComplete(future.get());
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                    e.printStackTrace();
                }
            }
        });

        return future;
    }

    public Answer getAnswer(final String question){
        final Future<Answer> future =
            executor.submit(new Callable<Answer>() {
                @Override
                public Answer call() throws Exception {
                    Answer answer = new Answer();
                    try{
                        HttpGet httpGet = new HttpGet("http://api.smartnlp.cn/cloud/robot/" + appKey + "/answer?q=" + URLEncoder.encode(question, "UTF-8"));
                        HttpResponse response = client.execute(httpGet);
                        if(response.getStatusLine().getStatusCode() == 200){
                            answer.isSuccess = true;
                            String resStr = EntityUtils.toString(response.getEntity(), "UTF-8");
                            JSONObject jsonObj = new JSONObject(resStr);
                            answer.answer = jsonObj.getString("answer");
                            answer.question = jsonObj.getString("question");
                            answer.score = jsonObj.getDouble("score");
                        } else {
                            answer.isSuccess = false;
                            answer.errMessage = response.getStatusLine().getStatusCode() + ": " + EntityUtils.toString(response.getEntity(), "UTF-8");
                        }
                    }catch(Exception e){
                        answer.isSuccess = false;
                        answer.errMessage = e.getMessage();
                    }
                    return answer;
                }
            });

        try {
            return future.get(globalTimeOutInMilliSeconds, TimeUnit.MILLISECONDS);
        }catch (Exception e){
            Answer answer = new Answer();
            answer.isSuccess = false;
            answer.errMessage = e.getMessage();
            return answer;
        }
    }

    public Answer getAnswer(final String question, long timeoutInMilliSeconds){
        final Future<Answer> future =
            executor.submit(new Callable<Answer>() {
                @Override
                public Answer call() throws Exception {
                    Answer answer = new Answer();
                    try{
                        HttpGet httpGet = new HttpGet("http://api.smartnlp.cn/cloud/robot/" + appKey + "/answer?q=" + question);
                        HttpResponse response = client.execute(httpGet);
                        if(response.getStatusLine().getStatusCode() == 200){
                            answer.isSuccess = true;
                            String resStr = EntityUtils.toString(response.getEntity(), "UTF-8");
                            JSONObject jsonObj = new JSONObject(resStr);
                            answer.answer = jsonObj.getString("answer");
                            answer.question = jsonObj.getString("question");
                            answer.score = jsonObj.getDouble("score");
                        } else {
                            answer.isSuccess = false;
                            answer.errMessage = response.getStatusLine().getStatusCode() + ": " + EntityUtils.toString(response.getEntity(), "UTF-8");
                        }
                    }catch(Exception e){
                        answer.isSuccess = false;
                        answer.errMessage = e.getMessage();
                    }
                    return answer;
                }
            });

        try {
            return future.get(timeoutInMilliSeconds, TimeUnit.MILLISECONDS);
        }catch (Exception e){
            Answer answer = new Answer();
            answer.isSuccess = false;
            answer.errMessage = e.getMessage();
            return answer;
        }
    }

    /*private String getAnswer(String question) throws IOException {
        HttpGet httpGet = new HttpGet("http://api.smartnlp.cn/cloud/robot/" + appKey + "/answer?q=" + question);
        httpGet.addHeader("session_id", sessionId);
        HttpResponse response = client.execute(httpGet);
        if(response.getStatusLine().getStatusCode() == 200){
            //Check session_id
            Header sessionHeader = response.getFirstHeader("session_id");
            if(sessionHeader != null){
                this.sessionId = sessionHeader.getValue();
            }

            return EntityUtils.toString(response.getEntity(), "UTF-8");
        } else {
            return null;
        }

    }*/

    public void setGlobalTimeOutInMilliSeconds(long globalTimeOutInMilliSeconds){
        this.globalTimeOutInMilliSeconds = globalTimeOutInMilliSeconds;
    }

    public void close(){
        checkExecutor.shutdown();
        executor.shutdown();
        client.getConnectionManager().shutdown();
    }
}
