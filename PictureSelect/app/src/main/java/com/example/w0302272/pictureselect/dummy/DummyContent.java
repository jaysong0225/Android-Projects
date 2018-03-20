package com.example.w0302272.pictureselect.dummy;

import android.content.res.Resources;

import com.example.w0302272.pictureselect.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Helper class for providing sample content for user interfaces created by
 * Android template wizards.
 * <p>
 * TODO: Replace all uses of this class before publishing your app.
 */
public class DummyContent {
    static Integer[][] imageItem = {
            {R.drawable.pic1, R.string.image1},
            {R.drawable.pic2, R.string.image2},
            {R.drawable.pic3, R.string.image3},
            {R.drawable.pic4, R.string.image4},
            {R.drawable.pic5, R.string.image5},
            {R.drawable.pic6, R.string.image6},
            {R.drawable.pic7, R.string.image7}
    };
    /**
     * An array of sample (dummy) items.
     */
    public static final List<DummyItem> ITEMS = new ArrayList<DummyItem>();

    /**
     * A map of sample (dummy) items, by ID.
     */
    public static final Map<String, DummyItem> ITEM_MAP = new HashMap<String, DummyItem>();

    private static final int COUNT = 7;

    static {
        // Add some sample items.
        for (int i = 1; i <= COUNT; i++) {
            addItem(createDummyItem(i));
        }
    }

    private static void addItem(DummyItem item) {
        ITEMS.add(item);
        ITEM_MAP.put(item.id, item);
    }

    private static DummyItem createDummyItem(int position) {
        return new DummyItem(String.valueOf(position-1), imageItem[position-1][1] , makeDetails(position));
    }

    private static Integer makeDetails(int position) { return imageItem[position-1][0]; }

    /**
     * A dummy item representing a piece of content.
     */
    public static class DummyItem {
        public final String id;
        public final Integer content;
        public final Integer details;

        public DummyItem(String id, Integer content, Integer details) {
            this.id = id;
            this.content = content;
            this.details = details;
        }
    }
}
