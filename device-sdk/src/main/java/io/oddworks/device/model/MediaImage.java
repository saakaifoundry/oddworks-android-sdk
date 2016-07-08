package io.oddworks.device.model;

import android.util.Log;

import java.net.URI;
import java.net.URISyntaxException;

public class MediaImage {
    public static final String TAG = MediaImage.class.getSimpleName();

    private String url;
    private String mimeType;
    private Integer width;
    private Integer height;
    private String label;

    public MediaImage(final String url, final String mimeType, final Integer width, final Integer height, final String label) {
        this.url = url;
        this.mimeType = mimeType;
        this.width = width;
        this.height = height;
        this.label = label;
    }

    public URI toURI() throws URISyntaxException {
        return URI.create(getUrl());
    }

    public String getUrl() {
        return url;
    }

    public String getMimeType() {
        return mimeType;
    }

    public Integer getWidth() {
        return width;
    }

    public Integer getHeight() {
        return height;
    }

    public String getLabel() {
        return label;
    }

    @Override
    public String toString() {
        return "MediaImage{" +
                "url='" + getUrl() + '\'' +
                ", mimeType='" + getMimeType() + '\'' +
                ", width='" + getWidth() + '\'' +
                ", height='" + getHeight() + '\'' +
                ", label='" + getLabel() + '\'' +
                '}';
    }
}
