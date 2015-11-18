package smartnlp.cn.sdk;

/**
 * Created by joymufeng on 2015/8/19.
 */
public class Answer {
    public boolean isSuccess;
    public String errMessage;
    public String answer;
    public String question;
    public Double score;

    @Override
    public String toString(){
        return "{isSuccess: " + isSuccess + ", answer: " + answer + ", question: " + question + ", score: " + score + ", errMessage: " + errMessage + "}";
    }
}
