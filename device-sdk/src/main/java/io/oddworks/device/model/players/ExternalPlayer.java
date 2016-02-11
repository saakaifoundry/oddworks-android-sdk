package io.oddworks.device.model.players;

/**
 * Created by dan on 12/7/15.
 */
public class ExternalPlayer extends Player{
    private final String mUrl;

    public ExternalPlayer(PlayerType playerType, String url) {
        super(playerType);
        mUrl = url;
    }

    public String getUrl() {
        return mUrl;
    }

    @Override
    public String toString() {
        return "ExternalPlayer{" +
                "mUrl='" + mUrl + '\'' +
                "} ";
    }
}
