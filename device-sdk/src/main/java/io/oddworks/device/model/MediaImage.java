package io.oddworks.device.model;

import android.util.Log;

import java.net.URI;
import java.net.URISyntaxException;

public class MediaImage {
    public static final String TAG = MediaImage.class.getSimpleName();

    private String mAspect16x9;
    private String mAspect3x4;
    private String mAspect4x3;
    private String mAspect1x1;
    private String mAspect2x3;

    public MediaImage(final String aspect16x9, final String aspect3x4, final String aspect4x3,
                      final String aspect1x1, final String aspect2x3) {
        mAspect16x9 = aspect16x9;
        mAspect3x4 = aspect3x4;
        mAspect4x3 = aspect4x3;
        mAspect1x1 = aspect1x1;
        mAspect2x3 = aspect2x3;
    }

    public URI getBackgroundImageURI() {
        try {
            return new URI(getAspect16x9());
        } catch (URISyntaxException e) {
            Log.d("URI exception: ", mAspect16x9);
            return null;
        }
    }

    public URI getCardImageURI() {
        try {
            return new URI(getAspect16x9());
        } catch (URISyntaxException e) {
            Log.d("URI exception: ", mAspect16x9);
            return null;
        }
    }

    public String getAspect16x9() {
        return mAspect16x9;
    }

    public String getAspect3x4() {
        return mAspect3x4;
    }

    public String getAspect4x3() {
        return mAspect4x3;
    }

    public String getAspect1x1() {
        return mAspect1x1;
    }

    public String getAspect2x3() {
        return mAspect2x3;
    }

    @Override
    public String toString() {
        return "MediaImage{" +
                "mAspect16x9='" + mAspect16x9 + '\'' +
                ", mAspect3x4='" + mAspect3x4 + '\'' +
                ", mAspect4x3='" + mAspect4x3 + '\'' +
                ", mAspect1x1='" + mAspect1x1 + '\'' +
                ", mAspect2x3='" + mAspect2x3 + '\'' +
                '}';
    }
}
