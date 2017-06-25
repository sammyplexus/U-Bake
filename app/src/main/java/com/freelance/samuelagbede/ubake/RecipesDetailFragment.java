package com.freelance.samuelagbede.ubake;

import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.freelance.samuelagbede.ubake.Models.Recipes;
import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.DefaultRenderersFactory;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;
import com.google.android.exoplayer2.upstream.DefaultHttpDataSourceFactory;

import butterknife.BindView;
import butterknife.ButterKnife;

import static android.view.ViewGroup.LayoutParams.WRAP_CONTENT;

/**
 * A fragment representing a single Recipes detail screen.
 * This fragment is either contained in a {@link RecipesListActivity}
 * in two-pane mode (on tablets) or a {@link RecipesDetailActivity}
 * on handsets.
 */
public class RecipesDetailFragment extends Fragment {
    @BindView(R.id.exoplayer_view)
    SimpleExoPlayerView simpleExoPlayerView;
    @BindView(R.id.tv_recipe_step_title)
    TextView mStepTitle;
    Recipes.Steps step;
    String videoUrl;
    private SimpleExoPlayer exoPlayer;
    private int currentWindow;
    private long playbackPosition;
    private boolean playWhenReady;
    String stepInstruction;


    public RecipesDetailFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {



        if (savedInstanceState != null){
            step = savedInstanceState.getParcelable(RecipesListActivity.INDIVIDUAL_STEPS_CLICKED);
            videoUrl = step.getVideoURL();
            stepInstruction = step.getDescription();
        }
        else {
            if (getArguments().containsKey(RecipesListActivity.INDIVIDUAL_STEPS_CLICKED)) {
                step = getArguments().getParcelable(RecipesListActivity.INDIVIDUAL_STEPS_CLICKED);
                videoUrl = step.getVideoURL();
                stepInstruction = step.getDescription();
            }
        }


        View rootView = inflater.inflate(R.layout.recipes_detail, container, false);
        ButterKnife.bind(this, rootView);
        mStepTitle.setText(stepInstruction);
        initializePlayer(videoUrl);
        return rootView;
    }

    private void initializePlayer(String url) {
        exoPlayer = ExoPlayerFactory.newSimpleInstance(new DefaultRenderersFactory(getContext()), new DefaultTrackSelector(), new DefaultLoadControl());
        simpleExoPlayerView.setPlayer(exoPlayer);
        playWhenReady = true;
        //Remember to handle playWhenReady for saveInstanceState
        simpleExoPlayerView.setMinimumWidth(WRAP_CONTENT);
        exoPlayer.setPlayWhenReady(playWhenReady);
        exoPlayer.seekTo(currentWindow, playbackPosition);

        Uri uri = Uri.parse(url);
        MediaSource mediaSource = buildMediaSource(uri);
        exoPlayer.prepare(mediaSource, true, false);
    }

    private MediaSource buildMediaSource(Uri uri) {
        return new ExtractorMediaSource(uri,
                new DefaultHttpDataSourceFactory("factory_4"),
                new DefaultExtractorsFactory(), null, null);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable(RecipesListActivity.INDIVIDUAL_STEPS_CLICKED, step);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        if (exoPlayer!=null) {
            exoPlayer.stop();
            exoPlayer.release();
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (exoPlayer!=null) {
            exoPlayer.stop();
            exoPlayer.release();
            exoPlayer=null;
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if (exoPlayer!=null) {
            exoPlayer.stop();
            exoPlayer.release();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (exoPlayer!=null) {
            exoPlayer.stop();
            exoPlayer.release();
        }
    }
}
