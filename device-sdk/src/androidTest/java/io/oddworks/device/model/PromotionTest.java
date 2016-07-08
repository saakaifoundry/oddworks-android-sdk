package io.oddworks.device.model;

import android.support.test.runner.AndroidJUnit4;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;
import java.util.HashMap;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

@RunWith(AndroidJUnit4.class)
public class PromotionTest {
    public Promotion mPromotion;

    @Before
    public void beforeEach() {
        mPromotion = new Promotion(new Identifier("123", OddObject.TYPE_PROMOTION));
        HashMap<String, Object> attributes = new HashMap<>();
        attributes.put("title", "title1");
        attributes.put("description", "description1");
        ArrayList<MediaImage> images = new ArrayList<>();
        images.add(new MediaImage("a", "b", 1, 1, "e"));
        attributes.put("images", images);
        mPromotion.setAttributes(attributes);
    }
}
