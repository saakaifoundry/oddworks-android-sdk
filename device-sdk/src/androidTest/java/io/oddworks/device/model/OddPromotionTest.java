package io.oddworks.device.model;

import android.support.test.runner.AndroidJUnit4;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.HashMap;

import io.oddworks.device.model.common.OddIdentifier;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

@RunWith(AndroidJUnit4.class)
public class OddPromotionTest {
    public OddPromotion mOddPromotion;

    @Before
    public void beforeEach() {
        mOddPromotion = new OddPromotion(new OddIdentifier("123", OddObject.TYPE_PROMOTION));
        HashMap<String, Object> attributes = new HashMap<>();
        attributes.put("title", "title1");
        attributes.put("description", "description1");
//        attributes.put("mediaImage", new MediaImage("a", "b", "c", "d", "e"));
        mOddPromotion.setAttributes(attributes);
    }

    @Test
    public void isPresentableShouldReturnTrue() {
        assertThat(mOddPromotion.isPresentable(), is(true));
    }

    @Test
    public void toPresentableFieldsShouldMatchThisFields() {
        Presentable presentable = mOddPromotion.toPresentable();
        assertThat("titles should match", presentable.getTitle(),
                is(equalTo(mOddPromotion.getTitle())));
        assertThat("descriptions should match", presentable.getDescription(),
                is(equalTo(mOddPromotion.getDescription())));
        assertThat("MediaImage should match", presentable.getMediaImage(),
                is(equalTo(mOddPromotion.getMediaImage())));
    }
}
