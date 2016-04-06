package net;

/**
 * Created by Administrator on 2015/4/27.
 */
public interface NetInterface {
    public abstract interface SuccessCallback {
        void onSuccess(String result);
    }

    public abstract interface FailCallback {
        void onFail(String result);
    }
}
