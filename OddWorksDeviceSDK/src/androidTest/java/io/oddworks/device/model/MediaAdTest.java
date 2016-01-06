package io.oddworks.device.model;

import android.support.test.runner.AndroidJUnit4;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.HashMap;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

@RunWith(AndroidJUnit4.class)
public class MediaAdTest {
    private String provider = "provider";
    private String freewheel = "freewheel";
    private String vmap = "vmap";
    private String dfp = "dfp";
    private String url = "http://what.ev.er";
    private int networkId = 123;
    private String profileName = "profile123";
    private String siteSectionId = "section";
    private String vHost = "hosty";
    private String assetId = "asset";
    private HashMap<String, Object> attributes = new HashMap<>();
    private MediaAd mediaAd;

    @Before
    public void beforeEach() {
        attributes.put("url", url);
        attributes.put("networkId", Integer.toString(networkId));
        attributes.put("profileName", profileName);
        attributes.put("siteSectionId", siteSectionId);
        attributes.put("vHost", vHost);
        attributes.put("assetId", assetId);
    }

    @Test
    public void testGetProvider() throws Exception {
        attributes.put("provider", provider);
        MediaAd mediaAd = new MediaAd(attributes);

        assertEquals(provider, mediaAd.getProvider());
    }

    @Test
    public void testGetFormat() throws Exception {
        attributes.put("format", freewheel);
        MediaAd mediaAd = new MediaAd(attributes);

        assertEquals(freewheel, mediaAd.getFormat());
    }

    @Test
    public void testGetUrl() throws Exception {
        MediaAd mediaAd = new MediaAd(attributes);

        assertEquals(url, mediaAd.getUrl());
    }

    @Test
    public void testGetNetworkId() throws Exception {
        MediaAd mediaAd = new MediaAd(attributes);

        assertEquals(networkId, mediaAd.getNetworkId());
    }

    @Test
    public void testGetProfileName() throws Exception {
        MediaAd mediaAd = new MediaAd(attributes);

        assertEquals(profileName, mediaAd.getProfileName());
    }

    @Test
    public void testGetSiteSectionId() throws Exception {
        MediaAd mediaAd = new MediaAd(attributes);

        assertEquals(siteSectionId, mediaAd.getSiteSectionId());
    }

    @Test
    public void testGetVHost() throws Exception {
        MediaAd mediaAd = new MediaAd(attributes);

        assertEquals(vHost, mediaAd.getVHost());
    }

    @Test
    public void testGetAssetId() throws Exception {
        MediaAd mediaAd = new MediaAd(attributes);

        assertEquals(assetId, mediaAd.getAssetId());
    }

    @Test
    public void testIsAvailableFalse() throws Exception {
        MediaAd mediaAd = new MediaAd(attributes);

        assertFalse(mediaAd.isAvailable());
    }

    @Test
    public void testIsAvailableTrue() throws Exception {
        attributes.put("provider", provider);
        attributes.put("format", freewheel);
        MediaAd mediaAd = new MediaAd(attributes);

        assertTrue(mediaAd.isAvailable());
    }

    @Test
    public void testIsVMAPFalse() throws Exception {
        attributes.put("format", freewheel);
        MediaAd mediaAd = new MediaAd(attributes);

        assertFalse(mediaAd.isVMAP());
    }

    @Test
    public void testIsVMAPTrue() throws Exception {
        attributes.put("format", vmap);
        MediaAd mediaAd = new MediaAd(attributes);

        assertTrue(mediaAd.isVMAP());
    }

    @Test
    public void testIsFreeWheelTrue() throws Exception {
        attributes.put("format", freewheel);
        MediaAd mediaAd = new MediaAd(attributes);

        assertTrue(mediaAd.isFreeWheel());
    }

    @Test
    public void testIsFreeWheelFalse() throws Exception {
        attributes.put("format", vmap);
        MediaAd mediaAd = new MediaAd(attributes);

        assertFalse(mediaAd.isFreeWheel());
    }

    @Test
    public void testIsDFPTrue() throws Exception {
        attributes.put("format", dfp);
        MediaAd mediaAd = new MediaAd(attributes);

        assertTrue(mediaAd.isDFP());
    }

    @Test
    public void testIsDFPFalse() throws Exception {
        attributes.put("format", vmap);
        MediaAd mediaAd = new MediaAd(attributes);

        assertFalse(mediaAd.isDFP());
    }
}