package io.oddworks.device.model;

/**
 * Created by brkattk on 9/15/15.
 */
public class Config {

    private String mViewId;

    public Config(String viewId) {
        mViewId = viewId;
    }

    public final String getViewId() {
        return mViewId;
    }

    @Override
    public String toString() {
        return "Config{" +
                "mViewId='" + mViewId + '\'' +
                '}';
    }
}
