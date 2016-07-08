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
import java.util.List;

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
import io.oddworks.device.testutils.AssetUtils;

import static org.hamcrest.CoreMatchers.is;
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
    public void testParseViewWithIncluded() throws Exception {
        String viewResponseV2 = AssetUtils.readFileToString(mContext, "ViewWithIncluded.json");

        OddView view = oddParser.parseViewResponse(viewResponseV2);
        List<OddObject> promotions = view.getIncludedByRelationship("promotion");
        List<OddObject> featuredMedia = view.getIncludedByRelationship("featuredMedia");
        List<OddObject> featuredCollections = view.getIncludedByRelationship("featuredCollections");

        assertFalse(promotions.isEmpty());
        Promotion promotion = (Promotion) promotions.get(0);
        assertThat(promotion.getTitle(), is("NASA's Daily Show"));
        assertThat(promotion.getImages().get(0).getUrl(), is("http://dummyimage.com/16:9x1440&text=Promotion"));

        assertFalse(featuredCollections.isEmpty());
        OddCollection collection = (OddCollection) featuredCollections.get(0);
        assertThat(collection.getTitle(), is("NASA: Featured Collections"));

        assertFalse(featuredMedia.isEmpty());
        Media media = (Media) featuredMedia.get(0);
        assertThat(media.getTitle(), is("Trek to Mount Sharp Begins"));
        assertThat(media.getUrl(), is("http://www.nasa.gov/sites/default/files/com20130711-320-jpl.mp4"));
        assertThat(media.getImages().get(0).getUrl(), is("http://image.oddworks.io/NASA/space1.jpeg"));
    }
}
