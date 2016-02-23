package io.oddworks.device.model;

import org.joda.time.DateTime;

import java.util.HashMap;
import java.util.Map;

import io.oddworks.device.model.players.Player;

import static io.oddworks.device.Util.getDateTime;
import static io.oddworks.device.Util.getInteger;
import static io.oddworks.device.Util.getString;

public class Media extends OddObject {
    public static final String TAG = Media.class.getSimpleName();
    private String title;
    private String subtitle;
    private String description;
    private MediaImage mediaImage;
    private DateTime releaseDate;
    private MediaAd mediaAd;
    private Integer duration;
    private String url;
    private Player player;
    private Sharing sharing;

    public Media(Identifier identifier) {
        super(identifier);
    }

    public Media(String id, String type) {
        super(id, type);
    }

    public String getTitle() {
        return title;
    }

    public String getSubtitle() {
        return subtitle;
    }

    public String getDescription() {
        return description;
    }

    public MediaImage getMediaImage() {
        return mediaImage;
    }

    public DateTime getReleaseDate() {
        return releaseDate;
    }

    public String getUrl() {
        return url;
    }

    public int getDuration() {
        if (duration == null) {
            duration = 0;
        }

        return duration;
    }

    public MediaAd getMediaAd() {
        return mediaAd;
    }

    public Boolean isLive() {
        return getType().equals(OddObject.TYPE_LIVE_STREAM);
    }

    @Override
    public void setAttributes(Map<String, Object> attributes) {
        this.title = getString(attributes, "title", null);
        this.subtitle = getString(attributes, "subtitle", null);
        this.description = getString(attributes, "description", null);
        this.mediaImage = (MediaImage) attributes.get("mediaImage");
        this.mediaAd = (MediaAd) attributes.get("mediaAd");
        this.releaseDate = getDateTime(attributes, "releaseDate", null);
        this.duration = getInteger(attributes, "duration", null);
        this.url = getString(attributes, "url", null);
    }

    @Override
    public Map<String, Object> getAttributes() {
        HashMap<String, Object> attributes = new HashMap<>();
        attributes.put("title", getTitle());
        attributes.put("subtitle", getSubtitle());
        attributes.put("description", getDescription());
        attributes.put("mediaImage", getMediaImage());
        attributes.put("mediaAd", getMediaAd());
        attributes.put("releaseDate", getReleaseDate());
        attributes.put("duration", getDuration());
        attributes.put("url", getUrl());
        return attributes;
    }

    @Override
    public boolean isPresentable() {
        return true;
    }

    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    public Sharing getSharing() {
        return sharing;
    }

    public void setSharing(Sharing sharing) {
        this.sharing = sharing;
    }

    @Override
    public Presentable toPresentable() {
        return new Presentable(title, description, mediaImage);
    }

    @Override
    public String toString() {
        return "Media{" +
                "title='" + title + '\'' +
                ", subtitle='" + subtitle + '\'' +
                ", description='" + description + '\'' +
                ", mediaImage=" + mediaImage +
                ", releaseDate=" + releaseDate +
                ", mediaAd=" + mediaAd +
                ", duration=" + duration +
                ", url='" + url + '\'' +
                ", player=" + player +
                ", sharing=" + sharing +
                '}';
    }
}
