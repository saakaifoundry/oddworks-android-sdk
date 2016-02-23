package io.oddworks.device.model;

public class Sharing {
    private boolean enabled;
    private String text;

    public Sharing(boolean enabled, String text) {
        this.enabled = enabled;
        this.text = text;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public String getText() {
        if (text == null) {
            return "";
        }
        return text;
    }

    @Override
    public String toString() {
        return "Sharing{" +
                "enabled=" + enabled +
                ", text=" + text +
                '}';
    }
}
