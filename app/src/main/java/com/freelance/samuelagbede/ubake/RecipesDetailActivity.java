package com.freelance.samuelagbede.ubake;

import android.content.Intent;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.ActionBar;
import android.support.v4.app.NavUtils;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.freelance.samuelagbede.ubake.Models.Recipes;
import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.DefaultRenderersFactory;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;
import com.google.android.exoplayer2.upstream.DefaultHttpDataSourceFactory;
import com.google.android.exoplayer2.util.Util;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static android.R.attr.fragment;
import static android.R.attr.stepSize;
import static android.icu.lang.UCharacter.GraphemeClusterBreak.L;
import static android.view.ViewGroup.LayoutParams.WRAP_CONTENT;

/**
 * An activity representing a single Recipes detail screen. This
 * activity is only used narrow width devices. On tablet-size devices,
 * item details are presented side-by-side with a list of items
 * in a {@link RecipesListActivity}.
 */
public class RecipesDetailActivity extends AppCompatActivity {


    private static final String STEP_NUMBER_TO_SHOW = "STEP_NUMBER_TO_SHOW";
    private static final String STEPS_ARRAYLIST = "STEPS_ARRAYLIST";
    @BindView(R.id.exoplayer_view)
    SimpleExoPlayerView simpleExoPlayerView;
    @BindView(R.id.tv_recipe_step_title)
    TextView mStepTitle;
    @BindView(R.id.iv_recipe_step_picture)
    ImageView mStepImage;
    @BindView(R.id.btn_increment_steps_instructions)
    Button mStepsInstructionsNext;
    @BindView(R.id.btn_decrement_steps_instructions)
    Button mStepsInstructionsPrev;
    Recipes.Steps step;
    ArrayList<Recipes.Steps> stepsArrayList;
    int present_step_position;
    String videoUrl;
    String thumbnailUrl;
    private SimpleExoPlayer exoPlayer;
    private int currentWindow;
    private long playbackPosition;
    private boolean playWhenReady;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        // Show the Up button in the action bar.
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        if (getIntent().hasExtra(RecipesListActivity.INDIVIDUAL_STEPS_CLICKED))
        {
            step = getIntent().getParcelableExtra(RecipesListActivity.INDIVIDUAL_STEPS_CLICKED);
            stepsArrayList = getIntent().getParcelableArrayListExtra(RecipesListActivity.TOTAL_STEPS_ARRAYLIST);
            present_step_position = getIntent().getIntExtra(RecipesListActivity.RECIPE_POSITION, 0);



            //Todo : Get title of the food item and display it in the actionbar
        }

        if (getIntent().hasExtra(STEP_NUMBER_TO_SHOW)){
            overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
            present_step_position = getIntent().getIntExtra(STEP_NUMBER_TO_SHOW, 0);
            stepsArrayList = getIntent().getParcelableArrayListExtra(STEPS_ARRAYLIST);
            step = stepsArrayList.get(present_step_position);

        }
        setContentView(R.layout.activity_recipes_detail);
        ButterKnife.bind(this);
        videoUrl = step.getVideoURL();
        mStepTitle.setText(step.getDescription());
        thumbnailUrl = step.getThumbnailURL();

        if (thumbnailUrl.length() > 2){
            Glide.with(this).load(thumbnailUrl).into(mStepImage);
        }

        
        // savedInstanceState is non-null when there is fragment state
        // saved from previous configurations of this activity
        // (e.g. when rotating the screen from portrait to landscape).
        // In this case, the fragment will automatically be re-added
        // to its container so we don't need to manually add it.
        // For more information, see the Fragments API guide at:
        //
        //http://developer.android.com/guide/components/fragments.html
        //

        if (savedInstanceState == null)
        {
            // Create the detail fragment and add it to the activity
            // using a fragment transaction.
            /*Bundle arguments = new Bundle();
            arguments.putParcelable(RecipesListActivity.INDIVIDUAL_STEPS_CLICKED,
                    getIntent().getParcelableExtra(RecipesListActivity.INDIVIDUAL_STEPS_CLICKED));
            RecipesDetailFragment fragment = new RecipesDetailFragment();
            fragment.setArguments(arguments);
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.recipes_detail_container, fragment)
                    .commit();*/
        }
    }


    @OnClick(R.id.btn_increment_steps_instructions)
    void onClickIncrement(){
        if (present_step_position == stepsArrayList.size()-1){
            //Gotten to the end
            return;
        }
        present_step_position++;
        Intent intent = new Intent(RecipesDetailActivity.this, RecipesDetailActivity.class);
        intent.putExtra(STEP_NUMBER_TO_SHOW, present_step_position);
        intent.putExtra(STEPS_ARRAYLIST, stepsArrayList);
        overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
        startActivity(intent);

    }

    @OnClick(R.id.btn_decrement_steps_instructions)
    void onClickDecrement(){
        if (present_step_position == 0){
            //At the beginning
            return;
        }
        present_step_position--;
        Intent intent = new Intent(RecipesDetailActivity.this, RecipesDetailActivity.class);
        intent.putExtra(STEP_NUMBER_TO_SHOW, present_step_position);
        intent.putExtra(STEPS_ARRAYLIST, stepsArrayList);

        Recipes.Steps stap = stepsArrayList.get(present_step_position);
        startActivity(intent);
    }



    private void initializePlayer(String url) {
        exoPlayer = ExoPlayerFactory.newSimpleInstance(new DefaultRenderersFactory(this), new DefaultTrackSelector(), new DefaultLoadControl());
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
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            // This ID represents the Home or Up button. In the case of this
            // activity, the Up button is shown. Use NavUtils to allow users
            // to navigate up one level in the application structure. For
            // more details, see the Navigation pattern on Android Design:
            //
            // http://developer.android.com/design/patterns/navigation.html#up-vs-back
            //
            NavUtils.navigateUpTo(this, new Intent(this, RecipesListActivity.class));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onStart() {
        super.onStart();


    }

    @Override
    protected void onResume() {
        super.onResume();
        initializePlayer(videoUrl);


    }

    @Override
    protected void onPause() {
        super.onPause();

            releasePlayer();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        releasePlayer();
    }

    @Override
    protected void onStop() {
        super.onStop();
            releasePlayer();

    }



    private void releasePlayer() {
        if (exoPlayer != null){
            playbackPosition = exoPlayer.getCurrentPosition();
            currentWindow = exoPlayer.getCurrentWindowIndex();
            exoPlayer.setPlayWhenReady(false);
            exoPlayer.stop();
            exoPlayer.release();
            exoPlayer = null;
        }
    }
}
