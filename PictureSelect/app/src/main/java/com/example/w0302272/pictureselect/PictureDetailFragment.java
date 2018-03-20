package com.example.w0302272.pictureselect;

import android.app.Activity;
import android.support.design.widget.CollapsingToolbarLayout;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.view.animation.*;

import com.example.w0302272.pictureselect.dummy.DummyContent;

/**
 * A fragment representing a single Picture detail screen.
 * This fragment is either contained in a {@link PictureListActivity}
 * in two-pane mode (on tablets) or a {@link PictureDetailActivity}
 * on handsets.
 */
public class PictureDetailFragment extends Fragment {
    /**
     * The fragment argument representing the item ID that this fragment
     * represents.
     */
    public static final String ARG_ITEM_ID = "item_id";

    /**
     * The dummy content this fragment is presenting.
     */
    private DummyContent.DummyItem mItem;

    // animation effect
    private Animation waveAnimation;
    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public PictureDetailFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments().containsKey(ARG_ITEM_ID)) {
            // Load the dummy content specified by the fragment
            // arguments. In a real-world scenario, use a Loader
            // to load content from a content provider.
            mItem = DummyContent.ITEM_MAP.get(getArguments().getString(ARG_ITEM_ID));

            Activity activity = this.getActivity();
            CollapsingToolbarLayout appBarLayout = (CollapsingToolbarLayout) activity.findViewById(R.id.toolbar_layout);
            if (appBarLayout != null) {
                appBarLayout.setTitle(getString(mItem.content));
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.picture_detail, container, false);

        // Show the dummy content as text in a TextView.
        if (mItem != null) {
            ((ImageView) rootView.findViewById(R.id.picture_detail)).setImageResource(mItem.details);
            // animation effect (fade in)
            waveFlag((ImageView) rootView);
        }

        return rootView;
    }

    //++ animation effect (fade in)
    public void waveFlag(ImageView imageView){
        waveAnimation = AnimationUtils.loadAnimation(getContext(),R.anim.wave);
        imageView.startAnimation(waveAnimation);

    }
    //-- animation effect
}
