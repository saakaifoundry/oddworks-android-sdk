package io.oddworks.device.model;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import java.net.URI;
import java.net.URISyntaxException;

/**
 * Created by brkattk on 9/25/15.
 */
public class MediaImage implements Parcelable {
    private static final String TAG = MediaImage.class.getSimpleName();

    private String mAspect16x9;
    private String mAspect3x4;
    private String mAspect4x3;
    private String mAspect1x1;
    private String mAspect2x3;

    public MediaImage(final String aspect16x9, final String aspect3x4, final String aspect4x3,
                      final String aspect1x1, final String aspect2x3) {
        setAspect16x9(aspect16x9);
        setAspect3x4(aspect3x4);
        setAspect4x3(aspect4x3);
        setAspect1x1(aspect1x1);
        setAspect2x3(aspect2x3);
    }

    protected MediaImage(Parcel in) {
        setAspect16x9(in.readString());
        setAspect3x4(in.readString());
        setAspect4x3(in.readString());
        setAspect1x1(in.readString());
        setAspect2x3(in.readString());
    }

    public static final Creator<MediaImage> CREATOR = new Creator<MediaImage>() {
        @Override
        public MediaImage createFromParcel(Parcel in) {
            return new MediaImage(in);
        }

        @Override
        public MediaImage[] newArray(int size) {
            return new MediaImage[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(getAspect16x9());
        dest.writeString(getAspect3x4());
        dest.writeString(getAspect4x3());
        dest.writeString(getAspect1x1());
        dest.writeString(getAspect2x3());
    }

    @Override
    public String toString() {
        return TAG + "{" +
                "aspect16x9='" + getAspect16x9() + "'}";
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

    public void setAspect16x9(String aspect16x9) {
        mAspect16x9 = aspect16x9;
    }

    public String getAspect3x4() {
        return mAspect3x4;
    }

    public void setAspect3x4(String aspect3x4) {
        mAspect3x4 = aspect3x4;
    }

    public String getAspect4x3() {
        return mAspect4x3;
    }

    public void setAspect4x3(String aspect4x3) {
        mAspect4x3 = aspect4x3;
    }

    public String getAspect1x1() {
        return mAspect1x1;
    }

    public void setAspect1x1(String aspect1x1) {
        mAspect1x1 = aspect1x1;
    }

    public String getAspect2x3() {
        return mAspect2x3;
    }

    public void setAspect2x3(String aspect2x3) {
        mAspect2x3 = aspect2x3;
    }
}
