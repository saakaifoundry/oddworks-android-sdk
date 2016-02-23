package io.oddworks.device.model;

import android.util.Log;

import java.net.URI;
import java.net.URISyntaxException;

public class MediaImage {
    public static final String TAG = MediaImage.class.getSimpleName();

    private String aspect16x9;
    private String aspect3x4;
    private String aspect4x3;
    private String aspect1x1;
    private String aspect2x3;

    public MediaImage(final String aspect16x9, final String aspect3x4, final String aspect4x3,
                      final String aspect1x1, final String aspect2x3) {
        this.aspect16x9 = aspect16x9;
        this.aspect3x4 = aspect3x4;
        this.aspect4x3 = aspect4x3;
        this.aspect1x1 = aspect1x1;
        this.aspect2x3 = aspect2x3;
    }

    public URI getBackgroundImageURI() {
        try {
            return new URI(getAspect16x9());
        } catch (URISyntaxException e) {
            Log.d("URI exception: ", aspect16x9);
            return null;
        }
    }

    public URI getCardImageURI() {
        try {
            return new URI(getAspect16x9());
        } catch (URISyntaxException e) {
            Log.d("URI exception: ", aspect16x9);
            return null;
        }
    }

    public String getAspect16x9() {
        return aspect16x9;
    }

    public String getAspect3x4() {
        return aspect3x4;
    }

    public String getAspect4x3() {
        return aspect4x3;
    }

    public String getAspect1x1() {
        return aspect1x1;
    }

    public String getAspect2x3() {
        return aspect2x3;
    }

    @Override
    public String toString() {
        return "MediaImage{" +
                "aspect16x9='" + aspect16x9 + '\'' +
                ", aspect3x4='" + aspect3x4 + '\'' +
                ", aspect4x3='" + aspect4x3 + '\'' +
                ", aspect1x1='" + aspect1x1 + '\'' +
                ", aspect2x3='" + aspect2x3 + '\'' +
                '}';
    }
}
