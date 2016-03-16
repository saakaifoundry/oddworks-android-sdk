package io.oddworks.device.request;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;
import android.test.AndroidTestCase;

import org.json.JSONObject;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.skyscreamer.jsonassert.JSONAssert;

import java.io.IOException;
import java.util.Iterator;
import java.util.List;

import io.oddworks.device.exception.UnhandledPlayerTypeException;
import io.oddworks.device.metric.OddAppInitMetric;
import io.oddworks.device.metric.OddVideoErrorMetric;
import io.oddworks.device.metric.OddVideoPlayMetric;
import io.oddworks.device.metric.OddVideoPlayingMetric;
import io.oddworks.device.metric.OddVideoStopMetric;
import io.oddworks.device.metric.OddViewLoadMetric;
import io.oddworks.device.model.Article;
import io.oddworks.device.model.AuthToken;
import io.oddworks.device.model.Config;
import io.oddworks.device.model.Event;
import io.oddworks.device.model.Identifier;
import io.oddworks.device.model.Media;
import io.oddworks.device.model.MediaImage;
import io.oddworks.device.model.OddCollection;
import io.oddworks.device.model.OddObject;
import io.oddworks.device.model.OddView;
import io.oddworks.device.model.Promotion;
import io.oddworks.device.model.players.ExternalPlayer;
import io.oddworks.device.model.players.OoyalaPlayer;
import io.oddworks.device.model.players.Player;
import io.oddworks.device.testutils.AssetUtils;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.isA;
import static org.hamcrest.CoreMatchers.notNullValue;
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
    public void testParseAuthToken() throws Exception {
        String authTokenResponseV2 = AssetUtils.readFileToString(mContext, "AuthTokenResponseV2.json");
        AuthToken authToken = oddParser.parseAuthToken(authTokenResponseV2);

        JSONObject json = new JSONObject(authTokenResponseV2);
        JSONObject data = json.getJSONObject("data");
        JSONObject attributes = data.getJSONObject("attributes");
        JSONObject deviceUserProfile = attributes.getJSONObject("deviceUserProfile");
        JSONObject entitlementCredentials = deviceUserProfile.getJSONObject("entitlementCredentials");

        assertThat(authToken.getToken(), is("hey-im-an-access-token-look-at-me"));
        assertThat(authToken.getTokenType(), is("Bearer"));
        JSONAssert.assertEquals(authToken.getEntitlementCredentials(), entitlementCredentials, true);
    }

    @Test
    public void testParseViewV2() throws Exception {
        String viewResponseV2 = AssetUtils.readFileToString(mContext, "ViewResponseV2.json");

        OddView view = oddParser.parseViewResponse(viewResponseV2);
        List<OddObject> promotions = view.getIncludedByRelationship("promotion");
        List<OddObject> featuredMedia = view.getIncludedByRelationship("featuredMedia");
        List<OddObject> featuredCollections = view.getIncludedByRelationship("featuredCollections");

        assertFalse(promotions.isEmpty());
        Promotion promotion = (Promotion) promotions.get(0);
        assertThat(promotion.getTitle(), is("Share your best poker face using #POKERCENTRALCONTEST"));
        assertThat(promotion.getUrl(), is("http://www.pokercentral.com"));

        assertFalse(featuredMedia.isEmpty());
        Media mediaSharingEnabled = (Media) featuredMedia.get(0);
        assertThat(mediaSharingEnabled.getDescription(), is("Louder, come on"));
        assertTrue(mediaSharingEnabled.getSharing().isEnabled());
        assertThat(mediaSharingEnabled.getSharing().getText(), is("Share this with your friends."));

        Media mediaNoSharing = (Media) featuredMedia.get(1);
        assertFalse(mediaNoSharing.getSharing().isEnabled());

        Media mediaSharingDisabled = (Media) featuredMedia.get(2);
        assertFalse(mediaSharingDisabled.getSharing().isEnabled());

        assertFalse(featuredCollections.isEmpty());
        OddCollection collection = (OddCollection) featuredCollections.get(0);
        assertThat(collection.getTitle(), is("PBR Featured Collections"));

        List<OddObject> subCollections = collection.getIncludedByRelationship("entities");
        OddCollection subCollection = (OddCollection) subCollections.get(0);
        assertThat(subCollection.getTitle(), is("The Black Eyed Peas"));
    }

    @Test
    public void testParseMenuViewResponseV2() throws Exception {
        String json = AssetUtils.readFileToString(mContext, "MenuViewResponseV2.json");

        OddView menu = oddParser.parseViewResponse(json);
        List<OddObject> items = menu.getIncludedByRelationship("items");

        assertFalse(items.isEmpty());

        OddCollection articles = (OddCollection) items.get(1);
        assertNull(articles.getMediaImage());
        assertThat(articles.getTitle(), is("News"));
        Article article = (Article) articles.getIncludedByRelationship("entities").get(0);
        assertThat(article.getTitle(), is("Stock Contractor of the Year Berger has bulls at NFR"));
        assertNull(article.getMediaImage());

        OddCollection events = (OddCollection) items.get(2);
        assertThat(events.getTitle(), is("Built Ford Tough Series - Schedule"));
        assertNull(events.getMediaImage());
        Event event = (Event) events.getIncludedByRelationship("entities").get(0);
        assertThat(event.getTitle(), is("2015 Built Ford Tough World Finals"));
        MediaImage eventImage = event.getMediaImage();
        assertThat(eventImage.getAspect16x9(), is("http://pbr.com/media/resized/96117_0_640x360.jpg"));
    }

    @Test
    public void testParsePBRMenuViewResponseV2() throws Exception {
        String json = AssetUtils.readFileToString(mContext, "PBRMenuViewResponseV2.json");

        OddView menu = oddParser.parseViewResponse(json);
        List<OddObject> items = menu.getIncludedByRelationship("items");
    }

    @Test
    public void testParsePBRHomepageViewResponseV2() throws Exception {
        String json = AssetUtils.readFileToString(mContext, "PBRHomepageViewResponseV2.json");

        OddView homepage = oddParser.parseViewResponse(json);
        List<OddObject> promotions = homepage.getIncludedByRelationship("promotion");
        List<OddObject> featuredMedia = homepage.getIncludedByRelationship("featuredMedia");
        List<OddObject> featuredCollections = homepage.getIncludedByRelationship("featuredCollections");
    }

    @Test
    public void testParseDeviceCodeResponse() throws Exception {

    }

    @Test
    public void testParseSearchV2() throws Exception {
        String json = AssetUtils.readFileToString(mContext, "PBRSearchResponseV2.json");

        List<OddObject> results = oddParser.parseSearch(json);
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
    }

    @Test
    public  void testParseConfigWithAuthNoAds() throws Exception {
        String json = AssetUtils.readFileToString(mContext, "ConfigWithAuthNoAdsAndSharing.json");
        Config config = oddParser.parseConfig(json);
        assertThat(config.getViews().size(), is(3));
        String spashpageId = "levintv-homepage";
        assertThat(config.getViews().get("homepage"), is(spashpageId));
        assertThat(config.getViews().values().iterator().next(), is(spashpageId));
        assertThat(config.getViews().get("splash"), is("levintv-splash"));
        assertThat(config.isAuthEnabled(), is(true));
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

        OddAppInitMetric oddAppInitMetric = new OddAppInitMetric();
        assertThat(oddAppInitMetric.getAction(), is("wacky:init"));
        assertTrue(oddAppInitMetric.getEnabled());

        OddViewLoadMetric oddViewLoadMetric = new OddViewLoadMetric();
        assertThat(oddViewLoadMetric.getAction(), is("wacky:load"));
        assertTrue(oddViewLoadMetric.getEnabled());

        OddVideoPlayMetric oddVideoPlayMetric = new OddVideoPlayMetric();
        assertThat(oddVideoPlayMetric.getAction(), is("wacky:play"));
        assertTrue(oddVideoPlayMetric.getEnabled());

        OddVideoPlayingMetric oddVideoPlayingMetric = new OddVideoPlayingMetric();
        assertThat(oddVideoPlayingMetric.getAction(), is("wacky:playing"));
        assertThat(oddVideoPlayingMetric.getInterval(), is(30000));
        assertTrue(oddVideoPlayingMetric.getEnabled());

        OddVideoStopMetric oddVideoStopMetric = new OddVideoStopMetric();
        assertThat(oddVideoStopMetric.getAction(), is("wacky:stop"));
        assertTrue(oddVideoStopMetric.getEnabled());

        OddVideoErrorMetric oddVideoErrorMetric = new OddVideoErrorMetric();
        assertThat(oddVideoErrorMetric.getAction(), is("wacky:error"));
        assertFalse(oddVideoErrorMetric.getEnabled());
    }

    @Test
    public void testParseConfigWithoutAuthAndWithoutAds() throws Exception {
        String json = AssetUtils.readFileToString(mContext, "ConfigWithoutAuthAndWithoutAds.json");
        Config config = oddParser.parseConfig(json);
        assertThat(config.isAuthEnabled(), is(false));
    }

    @Test
    public void testParseCollectionWithIncludedV2() throws Exception {
        String json = AssetUtils.readFileToString(mContext, "NestedCollectionWithIncludedV2.json");
        OddCollection mc = oddParser.parseCollectionResponse(json);
        assertThat(mc.getId(), is("ooyala-pbr-featured-collections"));
        assertThat(mc.getAttributes(), is(notNullValue()));
        assertThat(mc.getRelationships().size(), is(1));
        List<Identifier> relationshipIds = mc.getRelationships().get(0).getIdentifiers();
        Identifier firstRelationship = relationshipIds.get(0);
        assertThat(firstRelationship.getId(), is("ooyala-w4MGV5eDpoE0rmbrcHEeDEEuhQMypB0u"));
        assertThat(mc.findIncludedByIdentifier(firstRelationship), is(notNullValue()));
        Identifier secondRelationship = relationshipIds.get(1);
        assertThat(secondRelationship.getId(), is("ooyala-02ZWV5eDqBpEbc9ooAspnWFIIycMZWfK"));
        assertThat(mc.findIncludedByIdentifier(secondRelationship), is(notNullValue()));
    }

    @Test
    public void testMediaAdParsing() throws Exception {
        String json = AssetUtils.readFileToString(mContext, "CollectionWithIncludedV2.json");
        OddCollection collection = oddParser.parseCollectionResponse(json);

        List<OddObject> videos = collection.getIncludedByRelationship("entities");

        assertThat(collection.getSubtitle(), is("Curation implies manual"));
        Media vid1 = (Media) videos.get(0);
        Media vid2 = (Media) videos.get(1);
        Media vid3 = (Media) videos.get(2);
        Media vid4 = (Media) videos.get(3);

        assertThat(vid1.getTitle(), is("Let's Get It Started.mp4"));
        assertNull(vid1.getSubtitle());

        JSONObject meta = vid1.getMeta();
        Iterator<String> metaKeys = meta.keys();

        assertThat(vid1.getMeta(), isA(JSONObject.class));

        assertThat(metaKeys.next(), is("sourceId"));
        assertThat(metaKeys.next(), is("source"));
        assertFalse(vid1.getMediaAd().isEnabled());
        assertNull(vid1.getSubtitle());

        assertThat(vid2.getTitle(), is("Pump It.mp4"));
        assertThat(vid2.getSubtitle(), is("Pumping it"));
        assertTrue(vid2.getMediaAd().isEnabled());
        assertTrue(vid2.getMediaAd().isFreeWheel());

        assertThat(vid3.getTitle(), is("Free Bird (Live August 21st, 1976).mp4"));
        assertTrue(vid3.getMediaAd().isEnabled());
        assertTrue(vid3.getMediaAd().isDFP());
        assertNull(vid3.getSubtitle());

        assertThat(vid4.getTitle(), is("Simple Man.mp4"));
        assertTrue(vid4.getMediaAd().isEnabled());
        assertTrue(vid4.getMediaAd().isVMAP());
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

    @Test
    public void testParseMediaResponseTest() throws Exception {
        String json = AssetUtils.readFileToString(mContext, "LiveStreamWithIncluded.json");
        Media media = oddParser.parseMediaResponse(json);
        assertThat(media.getId(), is("test-1"));
    }

}