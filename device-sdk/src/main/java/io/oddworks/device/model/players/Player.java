package io.oddworks.device.model.players;

/**
 * Created by dan on 12/7/15.
 */
public class Player {
    public enum PlayerType { EXTERNAL, OOYALA, NATIVE, BRIGHTCOVE }
    private final PlayerType mPlayerType;

    public Player(PlayerType playerType) {
        mPlayerType = playerType;
    }

    public PlayerType getPlayerType() {
        return mPlayerType;
    }

    @Override
    public String toString() {
        return "Player{" +
                "mPlayerType=" + mPlayerType +
                '}';
    }
}
