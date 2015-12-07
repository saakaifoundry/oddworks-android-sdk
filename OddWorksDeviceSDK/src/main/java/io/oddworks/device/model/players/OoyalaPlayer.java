package io.oddworks.device.model.players;

/**
 * Created by dan on 12/7/15.
 */
public class OoyalaPlayer extends Player{
    private final String mPCode;
    private final String mEmbedCode;
    private final String mDomain;

    public OoyalaPlayer(PlayerType playerType, String PCode, String embedCode, String domain) {
        super(playerType);
        mPCode = PCode;
        mEmbedCode = embedCode;
        mDomain = domain;
    }

    public String getPCode() {
        return mPCode;
    }

    public String getEmbedCode() {
        return mEmbedCode;
    }

    public String getDomain() {
        return mDomain;
    }

    @Override
    public String toString() {
        return "OoyalaPlayer{" +
                "mPCode='" + mPCode + '\'' +
                ", mEmbedCode='" + mEmbedCode + '\'' +
                ", mDomain='" + mDomain + '\'' +
                '}';
    }
}
