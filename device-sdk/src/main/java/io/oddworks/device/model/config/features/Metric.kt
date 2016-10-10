package io.oddworks.device.model.config.features

data class Metric(val enabled: Boolean, val action: String, val interval: Int = 0) {
    public static final String ACTION_APP_INIT = "appInit";
    public static final String ACTION_VIEW_LOAD = "viewLoad";
    public static final String ACTION_VIDEO_PLAY = "videoPlay";
    public static final String ACTION_VIDEO_PLAYING = "videoPlaying";
    public static final String ACTION_VIDEO_STOP = "videoStop";
    public static final String ACTION_VIDEO_ERROR = "videoError";
    enum class Metric
}

