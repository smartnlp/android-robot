package smartnlp.cn.sdk;

/**
 * Created by joymufeng on 2015/8/19.
 */
public interface AsyncHandler<T> {
    public void onComplete(T result);
}
