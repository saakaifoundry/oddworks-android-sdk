package io.oddworks.device.request;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;
import android.test.AndroidTestCase;
import android.util.Log;

import org.json.JSONObject;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.IOException;
import java.util.ArrayList;

import io.oddworks.device.exception.UnhandledPlayerTypeException;
import io.oddworks.device.metric.OddAppInitMetric;
import io.oddworks.device.metric.OddVideoErrorMetric;
import io.oddworks.device.metric.OddVideoPlayMetric;
import io.oddworks.device.metric.OddVideoPlayingMetric;
import io.oddworks.device.metric.OddVideoStopMetric;
import io.oddworks.device.metric.OddViewLoadMetric;
import io.oddworks.device.model.AdsConfig;
import io.oddworks.device.model.Collection;
import io.oddworks.device.model.Config;
import io.oddworks.device.model.Identifier;
import io.oddworks.device.model.Media;
import io.oddworks.device.model.MediaCollection;
import io.oddworks.device.model.players.ExternalPlayer;
import io.oddworks.device.model.players.OoyalaPlayer;
import io.oddworks.device.model.players.Player;
import io.oddworks.device.model.OddObject;
import io.oddworks.device.model.OddView;
import io.oddworks.device.model.Promotion;
import io.oddworks.device.testutils.AssetUtils;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertThat;

@RunWith(AndroidJUnit4.class)
public class OddParserTest extends AndroidTestCase {

    private OddParser oddParser;
    private Context mContext;

    @Before
    public void beforeEach() throws IOException {
        oddParser = OddParser.getInstance();
        mContext = InstrumentationRegistry.getTargetContext();
    }

    @Test
    public void testParseViewV1() throws Exception {
        String viewResponseV1 = AssetUtils.readFileToString(mContext, "ViewResponseV1.json");

        OddView oddView = oddParser.parseView(viewResponseV1);

        ArrayList<OddObject> featuredMedia = oddView.getIncludedByRelationship("featuredMedia");
        ArrayList<OddObject> featured = oddView.getIncludedByRelationship("featured");
        ArrayList<OddObject> shows = oddView.getIncludedByRelationship("shows");

        assertFalse(featuredMedia.isEmpty());
        assertThat(((Media) featuredMedia.get(0)).getTitle(), is("Wasabi: Wasabi Went Bad"));

        assertFalse(featured.isEmpty());
        MediaCollection collection = (MediaCollection) featured.get(0);
        assertThat(collection.getTitle(), is("Videos"));
        ArrayList<OddObject> videos = collection.getIncludedByRelationship("videos");
        Media video = (Media) videos.get(0);
        assertThat(video.getTitle(), is("S1:E4 Doubles Poker Championship"));
    }

    @Test
    public void testParseViewV2() throws Exception {
        String viewResponseV2 = AssetUtils.readFileToString(mContext, "ViewResponseV2.json");

        OddView view = oddParser.parseView(viewResponseV2);
        ArrayList<OddObject> promotions = view.getIncludedByRelationship("promotion");
        ArrayList<OddObject> featuredMedia = view.getIncludedByRelationship("featuredMedia");
        ArrayList<OddObject> featuredCollections = view.getIncludedByRelationship("featuredCollections");

        assertFalse(promotions.isEmpty());
        assertThat(((Promotion) promotions.get(0)).getTitle(), is("Share your best poker face using #POKERCENTRALCONTEST"));

        assertFalse(featuredMedia.isEmpty());
        assertThat(((Media) featuredMedia.get(0)).getDescription(), is("Louder, come on"));

        assertFalse(featuredCollections.isEmpty());
        Collection collection = (Collection) featuredCollections.get(0);
        assertThat(collection.getTitle(), is("PBR Featured Collections"));

        ArrayList<OddObject> subCollections = collection.getIncludedByRelationship("entities");
        Collection subCollection = (Collection) subCollections.get(0);
        assertThat(subCollection.getTitle(), is("The Black Eyed Peas"));
    }

    @Test
    public void testParseAuthToken() throws Exception {

    }

    @Test
    public void testParseDeviceCodeResponse() throws Exception {

    }

    @Test
    public void testParseSearch() throws Exception {

    }

    @Test
    public void testParseErrorMessage() throws Exception {

    }

    @Test
    public  void testParseConfigWithAuthAndAds() throws Exception {
        String json = AssetUtils.readFileToString(mContext, "ConfigWithAuthAndAds.json");
        Config config = oddParser.parseConfig(json);
        assertThat(config.getViews().size(), is(2));
        String spashpageId = "ac4ece84-d872-4b98-b8b9-2840e060a6ea";
        assertThat(config.getViews().get("homepage"), is(spashpageId));
        assertThat(config.getViews().values().iterator().next(), is(spashpageId));
        assertThat(config.getViews().get("splash"), is("2a019789-2475-4674-84f0-764bca8b8f66"));
        assertThat(config.isAuthEnabled(), is(true));
        AdsConfig ads = config.getAdsConfig();
        assertThat(ads.getProvider(), is(AdsConfig.AdProvider.FREEWHEEL));
        assertThat(ads.getFormat(), is(AdsConfig.AdFormat.VAST));
        String adsUrl = "http://vp-validation.videoplaza.tv/proxy/distributor/v2" +
                "?tt=p&rt=vast_2.0&rnd={random}&t=standard&s=validation";
        assertThat(ads.getUrl(), is(adsUrl));
    }

    @Test
    public  void testParseConfigWithMetrics() throws Exception {
        String json = AssetUtils.readFileToString(mContext, "ConfigWithMetrics.json");
        Config config = oddParser.parseConfig(json);
        assertThat(config.getViews().size(), is(1));
        String homepage = "a23e156c-5df3-45bb-812e-1bfa79fbd008";
        assertThat(config.getViews().get("homepage"), is(homepage));
        assertThat(config.getViews().values().iterator().next(), is(homepage));

        assertFalse(config.isAuthEnabled());

        assertThat(OddAppInitMetric.getInstance().getAction(), is("wacky:init"));
        assertTrue(OddAppInitMetric.getInstance().getEnabled());

        assertThat(OddViewLoadMetric.getInstance().getAction(), is("wacky:load"));
        assertTrue(OddViewLoadMetric.getInstance().getEnabled());

        assertThat(OddVideoPlayMetric.getInstance().getAction(), is("wacky:play"));
        assertTrue(OddVideoPlayMetric.getInstance().getEnabled());

        assertThat(OddVideoPlayingMetric.getInstance().getAction(), is("wacky:playing"));
        assertThat(OddVideoPlayingMetric.getInstance().getInterval(), is(30000));
        assertTrue(OddVideoPlayingMetric.getInstance().getEnabled());

        assertThat(OddVideoStopMetric.getInstance().getAction(), is("wacky:stop"));
        assertTrue(OddVideoStopMetric.getInstance().getEnabled());

        assertThat(OddVideoErrorMetric.getInstance().getAction(), is("wacky:error"));
        assertFalse(OddVideoErrorMetric.getInstance().getEnabled());
    }

    @Test
    public void testParseConfigWithoutAuthAndWithoutAds() throws Exception {
        String json = AssetUtils.readFileToString(mContext, "ConfigWithoutAuthAndWithoutAds.json");
        Config config = oddParser.parseConfig(json);
        assertThat(config.isAuthEnabled(), is(false));
        assertThat(config.getAdsConfig(), is(nullValue()));
    }

    @Test
    public void testParseMediaCollectionWithRelated() throws Exception {
        String json = AssetUtils.readFileToString(mContext, "MediaCollectionWithIncluded.json");
        MediaCollection mc = oddParser.parseMediaCollectionResponse(json);
        assertThat(mc.getId(), is("ooyala-8zbHZrdzp4s-iQtsn5V_KWt1NUutoiMx"));
        assertThat(mc.getAttributes(), is(notNullValue()));
        assertThat(mc.getRelationships().size(), is(1));
        ArrayList<Identifier> relationshipIds = mc.getRelationships().get(0).getIdentifiers();
        Identifier firstVidId = relationshipIds.get(0);
        assertThat(firstVidId.getId(), is("ooyala-wyN2FzdzrDIT7uOhpq_ZMX1fSv5k7T9k"));
        assertThat(mc.findIncludedByIdentifier(firstVidId), is(notNullValue()));
        Identifier secondVidId = relationshipIds.get(1);
        assertThat(secondVidId.getId(), is("ooyala-p0NmFzdzp2HFJfXPruqOCXJ5VEwqjVU-"));
        assertThat(mc.findIncludedByIdentifier(secondVidId), is(notNullValue()));
    }

    @Test
    public void testParsePlayerWithNative() throws Exception {
        String json = AssetUtils.readFileToString(mContext, "players/NativePlayer.json");
        JSONObject rawPlayer = new JSONObject(json);
        Player player = oddParser.parsePlayer(rawPlayer);
        assertThat(player.getPlayerType(), is(Player.PlayerType.NATIVE));
    }

    @Test
    public void testParsePlayerWithExternal() throws Exception {
        String json = AssetUtils.readFileToString(mContext, "players/ExternalPlayer.json");
        JSONObject rawPlayer = new JSONObject(json);
        ExternalPlayer player = (ExternalPlayer)oddParser.parsePlayer(rawPlayer);
        assertThat(player.getPlayerType(), is(Player.PlayerType.EXTERNAL));
        assertThat(player.getUrl(), is("http://link.to/a/webpage/for/a/video/asset.htm"));
    }

    @Test
    public void testParsePlayerWithOoyala() throws Exception {
        String json = AssetUtils.readFileToString(mContext, "players/OoyalaPlayer.json");
        JSONObject rawPlayer = new JSONObject(json);
        OoyalaPlayer player = (OoyalaPlayer)oddParser.parsePlayer(rawPlayer);
        assertThat(player.getPlayerType(), is(Player.PlayerType.OOYALA));
        assertThat(player.getPCode(), is("pcode1"));
        assertThat(player.getEmbedCode(), is("embedCode1"));
        assertThat(player.getDomain(), is("domain1"));
    }

    @Test(expected=UnhandledPlayerTypeException.class)
    public void testParsePlayerWithUnhandledType() throws Exception {
        String json = "{\"type\":\"unhandled type\"}";
        JSONObject rawPlayer = new JSONObject(json);
        oddParser.parsePlayer(rawPlayer);
    }
}