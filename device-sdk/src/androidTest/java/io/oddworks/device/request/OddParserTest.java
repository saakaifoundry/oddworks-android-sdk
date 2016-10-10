package io.oddworks.device.request;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;
import android.test.AndroidTestCase;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.IOException;

import io.oddworks.device.model.OddConfig;
import io.oddworks.device.testutils.AssetUtils;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

@RunWith(AndroidJUnit4.class)
public class OddParserTest extends AndroidTestCase {

    private OddParser oddParser;
    private Context mContext;

    @Before
    public void beforeEach() throws IOException {
        oddParser = OddParser.INSTANCE;
        mContext = InstrumentationRegistry.getTargetContext();
    }
//
//    @Test
//    public void testParseViewV2() throws Exception {
//        String viewResponseV2 = AssetUtils.readFileToString(mContext, "ViewResponseV2.json");
//
//        OddView view = oddParser.parseViewResponse(viewResponseV2);
//        List<OddObject> promotions = view.getIncludedByRelationship("oddPromotion");
//        List<OddObject> featuredMedia = view.getIncludedByRelationship("featuredMedia");
//        List<OddObject> featuredCollections = view.getIncludedByRelationship("featuredCollections");
//
//        assertFalse(promotions.isEmpty());
//        OddPromotion oddPromotion = (OddPromotion) promotions.get(0);
//        assertThat(oddPromotion.getTitle(), is("This is a oddPromotion title!"));
//        assertThat(oddPromotion.getUrl(), is("http://website.com"));
//
//        assertFalse(featuredMedia.isEmpty());
//        OddVideo oddVideoSharingEnabled = (OddVideo) featuredMedia.get(0);
//        assertThat(oddVideoSharingEnabled.getDescription(), is("Louder, come on"));
//        assertTrue(oddVideoSharingEnabled.getSharing().isEnabled());
//        assertThat(oddVideoSharingEnabled.getSharing().getText(), is("Share this with your friends."));
//
//        OddVideo oddVideoNoSharing = (OddVideo) featuredMedia.get(1);
//        assertFalse(oddVideoNoSharing.getSharing().isEnabled());
//
//        OddVideo oddVideoSharingDisabled = (OddVideo) featuredMedia.get(2);
//        assertFalse(oddVideoSharingDisabled.getSharing().isEnabled());
//
//        assertFalse(featuredCollections.isEmpty());
//        OddCollection collection = (OddCollection) featuredCollections.get(0);
//        assertThat(collection.getTitle(), is("Automotive Pros Featured Collections"));
//
//        List<OddObject> subCollections = collection.getIncludedByRelationship("entities");
//        OddCollection subCollection = (OddCollection) subCollections.get(0);
//        assertThat(subCollection.getTitle(), is("The Black Eyed Peas"));
//    }
//
//    @Test
//    public void testParseMenuViewResponseV2() throws Exception {
//        String json = AssetUtils.readFileToString(mContext, "MenuViewResponseV2.json");
//
//        OddView menu = oddParser.parseViewResponse(json);
//        List<OddObject> items = menu.getIncludedByRelationship("items");
//
//        assertFalse(items.isEmpty());
//
//        OddCollection articles = (OddCollection) items.get(1);
//        assertNull(articles.getMediaImage());
//        assertThat(articles.getTitle(), is("News"));
//        Article article = (Article) articles.getIncludedByRelationship("entities").get(0);
//        assertThat(article.getTitle(), is("Stock Contractor of the Year Berger has bulls at NFR"));
//        assertNull(article.getMediaImage());
//
//        OddCollection events = (OddCollection) items.get(2);
//        assertThat(events.getTitle(), is("Expert Series - Schedule"));
//        assertNull(events.getMediaImage());
//        Event event = (Event) events.getIncludedByRelationship("entities").get(0);
//        assertThat(event.getTitle(), is("2015 Expert thing"));
//        MediaImage eventImage = event.getMediaImage();
//        assertThat(eventImage.getAspect16x9(), is("http://vbr.com/media/resized/96117_0_640x360.jpg"));
//    }
//
//    @Test
//    public void testParseHomepageViewResponseV2() throws Exception {
//        String json = AssetUtils.readFileToString(mContext, "HomepageViewResponseV2.json");
//
//        OddView homepage = oddParser.parseViewResponse(json);
//        List<OddObject> promotions = homepage.getIncludedByRelationship("promotion");
//        List<OddObject> featuredMedia = homepage.getIncludedByRelationship("featuredMedia");
//        List<OddObject> featuredCollections = homepage.getIncludedByRelationship("featuredCollections");
//    }
//
//    @Test
//    public void testParseDeviceCodeResponse() throws Exception {
//
//    }
//
//    @Test
//    public void testParseSearchV2() throws Exception {
//        String json = AssetUtils.readFileToString(mContext, "SearchResponseV2.json");
//
//        List<OddObject> results = oddParser.parseSearch(json);
//    }
//
//    @Test
//    public void testParseErrorMessage() throws Exception {
//
//    }

    @Test
    public  void testParseConfig() throws Exception {
        String json = AssetUtils.readFileToString(mContext, "ConfigResponse.json");
        OddConfig oddConfig = oddParser.parseConfig(json);

        // Views
        assertThat(oddConfig.getViews().size(), is(2));
        String homepageId = "channel-id-homepage";
        assertThat(oddConfig.getViews().get("homepage"), is(homepageId));
        String splashId = "channel-id-splash";
        assertThat(oddConfig.getViews().get("splash"), is(splashId));

        // Features


    }
//
//
//    @Test
//    public void testParseCollectionWithIncludedV2() throws Exception {
//        String json = AssetUtils.readFileToString(mContext, "NestedCollectionWithIncludedV2.json");
//        OddCollection mc = oddParser.parseCollectionResponse(json);
//        assertThat(mc.getId(), is("custom-collection-1"));
//        assertThat(mc.getAttributes(), is(notNullValue()));
//        assertThat(mc.getRelationships().size(), is(1));
//        List<OddIdentifier> relationshipIds = mc.getRelationships().get(0).getIdentifiers();
//        OddIdentifier firstRelationship = relationshipIds.get(0);
//        assertThat(firstRelationship.getId(), is("DEEDDEEDDEEDDEEDDEEDDEEDDEEDDEED"));
//        assertThat(mc.findIncludedByIdentifier(firstRelationship), is(notNullValue()));
//        OddIdentifier secondRelationship = relationshipIds.get(1);
//        assertThat(secondRelationship.getId(), is("KKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKK"));
//        assertThat(mc.findIncludedByIdentifier(secondRelationship), is(notNullValue()));
//    }

//
//    @Test
//    public void testParseMediaResponseTest() throws Exception {
//        String json = AssetUtils.readFileToString(mContext, "LiveStreamWithIncluded.json");
//        OddVideo oddVideo = oddParser.parseMediaResponse(json);
//        assertThat(oddVideo.getId(), is("test-1"));
//    }

}
