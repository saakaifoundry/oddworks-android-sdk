package io.oddworks.device.request;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;
import android.test.AndroidTestCase;


import static org.junit.Assert.assertThat;
import static org.hamcrest.CoreMatchers.*;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.IOException;

import io.oddworks.device.model.AdsConfig;
import io.oddworks.device.model.Config;
import io.oddworks.device.testutils.AssetUtils;

@RunWith(AndroidJUnit4.class)
public class OddParserTest extends AndroidTestCase {

    private OddParser oddParser;
    private Context mContext;

    @Before
    public void beforeEach() throws IOException {
        OddParser.instance = new OddParser();
        oddParser = OddParser.instance;
        mContext = InstrumentationRegistry.getTargetContext();
    }

    @Test
    public void testParseView() throws Exception {
//        String viewResponseWithIncluded = AssetUtils.readFileToString(getContext(), "ViewResponseWithIncluded.json");

//        OddView oddView = oddParser.parseView(viewResponseWithIncluded);
//
//        ArrayList<OddObject> mediaObjects = oddView.getIncludedByType("video");
//
//        Media media = (Media) mediaObjects.get(0);
//
//        Log.d("TEST: ", media.getMediaAd().toString());
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
        assertThat(config.getViews().get("splashpage"), is("2a019789-2475-4674-84f0-764bca8b8f66"));
        assertThat(config.isAuthEnabled(), is(true));
        AdsConfig ads = config.getAds();
        assertThat(ads.getProvider(), is(AdsConfig.AdProvider.FREEWHEEL));
        assertThat(ads.getFormat(), is(AdsConfig.AdFormat.VAST));
        String adsUrl = "http://vp-validation.videoplaza.tv/proxy/distributor/v2" +
                "?tt=p&rt=vast_2.0&rnd={random}&t=standard&s=validation";
        assertThat(ads.getUrl(), is(adsUrl));
    }

    @Test
    public void testParseConfigWithoutAuthAndWithoutAds() throws Exception {
        String json = AssetUtils.readFileToString(mContext, "ConfigWithoutAuthAndWithoutAds.json");
        Config config = oddParser.parseConfig(json);
        assertThat(config.isAuthEnabled(), is(false));
        assertThat(config.getAds(), is(nullValue()));
    }
}