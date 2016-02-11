package io.oddworks.device.model;

public class Sharing {
    private boolean mEnabled;
    private String mText;

    public Sharing(boolean enabled, String text) {
        mEnabled = enabled;
        mText = text;
    }

    public boolean isEnabled() {
        return mEnabled;
    }

    public String getText() {
        if (mText == null) {
            return "";
        }
        return mText;
    }

    @Override
    public String toString() {
        return "Sharing{" +
                "mEnabled=" + mEnabled +
                ", mText=" + mText +
                '}';
    }
}
