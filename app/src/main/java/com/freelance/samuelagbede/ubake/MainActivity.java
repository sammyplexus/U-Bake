package com.freelance.samuelagbede.ubake;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.VisibleForTesting;
import android.support.design.widget.Snackbar;
import android.support.test.espresso.IdlingResource;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import com.freelance.samuelagbede.ubake.Adapters.SelectRecipesRecyclerAdapter;
import com.freelance.samuelagbede.ubake.IdlingResource.CustomIdlingResource;
import com.freelance.samuelagbede.ubake.Models.Recipes;
import com.freelance.samuelagbede.ubake.Utilities.NetworkUtils;
import com.freelance.samuelagbede.ubake.Utilities.RecipeUtils;
import com.google.android.exoplayer2.C;

import org.json.JSONException;

import java.io.IOException;
import java.util.ArrayList;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<ArrayList<Recipes>>, SelectRecipesRecyclerAdapter.recyclerListener{
    private static final String RECIPES_ARRAYLIST = "recipes_arraylist";
    CustomIdlingResource mIdlingResource;

    @VisibleForTesting
    @NonNull
    public IdlingResource getIdlingResource() {
        if (mIdlingResource == null) {
            mIdlingResource = new CustomIdlingResource();
        }
        return mIdlingResource;
    }

    public static final String RECIPE_DETAIL_MODEL = "recipe_detail_model";
    public static final String RECIPE_INGREDIENTS_MODEL = "recipe_ingredients_model";
    public static final String RECIPE_STEPS_MODEL = "recipe_steps_model";

    @BindView(R.id.recyclerview_select_recipes) RecyclerView mRecyclerView;
    @BindView(R.id.pb_main_activity_indicator)
    ProgressBar progressBar;
    @BindView(R.id.btn_retry)
    Button mRetryButton;
    SelectRecipesRecyclerAdapter recyclerAdapter;
    ArrayList<Recipes> mRecipesArraylist;
    GridLayoutManager layoutManager;
    static final int LOADER_ID = 2321;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        getSupportLoaderManager().initLoader(LOADER_ID, null, this);
        layoutManager = new GridLayoutManager(this, getResources().getInteger(R.integer.span_count));

        recyclerAdapter = new SelectRecipesRecyclerAdapter(this, this);
        mRecyclerView.setLayoutManager(layoutManager);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(
                mRecyclerView.getContext(),
                layoutManager.getOrientation());
        mRecyclerView.addItemDecoration(dividerItemDecoration);
        mRecyclerView.setAdapter(recyclerAdapter);

        if (savedInstanceState == null){
            makeConnection();
        }
        else {
            mRecipesArraylist = savedInstanceState.getParcelableArrayList(RECIPES_ARRAYLIST);
            if (mRecipesArraylist != null)
                recyclerAdapter.swapData(mRecipesArraylist);
        }


        getIdlingResource();
    }
    public void makeConnection(){
            getSupportLoaderManager().initLoader(LOADER_ID, null, this);

    }

    @OnClick (R.id.btn_retry)
    void retry()
    {
        makeConnection();
    }

    @Override
    public Loader<ArrayList<Recipes>> onCreateLoader(int id, Bundle args) {
        mRetryButton.setVisibility(View.GONE);
        progressBar.setVisibility(View.VISIBLE);
        return new AsyncTaskLoader<ArrayList<Recipes>>(this) {
            ArrayList<Recipes> mRecipesArraylistCache;
            @Override
            protected void onStartLoading() {

                if (mRecipesArraylistCache == null){
                    forceLoad();
                }
                else {
                    deliverResult(mRecipesArraylistCache);
                }
            }

            @Override
            public ArrayList<Recipes> loadInBackground() {
                ArrayList<Recipes> recipes = null;

                try {
                    String network = NetworkUtils.MakeNetworkCall();
                    recipes = RecipeUtils.parseHTTPResponse(network);

                } catch (IOException | JSONException e) {
                    e.printStackTrace();
                    Snackbar.make(getCurrentFocus(),  "Error in connection, please try again", Snackbar.LENGTH_LONG).show();

                    MainActivity.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run()
                        {
                            mRetryButton.setVisibility(View.VISIBLE);
                        }
                    });
                }
                return recipes;
            }

            @Override
            public void deliverResult(ArrayList<Recipes> data) {
                this.mRecipesArraylistCache = data;
                super.deliverResult(data);
            }
        };
    }

    @Override
    public void onLoadFinished(Loader<ArrayList<Recipes>> loader, ArrayList<Recipes> data) {
        progressBar.setVisibility(View.INVISIBLE);
        recyclerAdapter.swapData(data);
    }

    @Override
    public void onLoaderReset(Loader<ArrayList<Recipes>> loader) {
        recyclerAdapter.swapData(null);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (mRecipesArraylist != null)
        outState.putParcelableArrayList(RECIPES_ARRAYLIST, mRecipesArraylist);
    }

    @Override
    public void onRecyclerItemClick(int position) {
        Intent moveToNextActivity = new Intent(MainActivity.this, RecipesListActivity.class);
        Recipes recipes = recyclerAdapter.getData().get(position);
        ArrayList<Recipes.Ingredients> ingredients = recipes.getIngredients();
        ArrayList<Recipes.Steps> steps = recipes.getSteps();

        moveToNextActivity.putExtra(RECIPE_DETAIL_MODEL, recipes);
        moveToNextActivity.putExtra(RECIPE_INGREDIENTS_MODEL, ingredients);
        moveToNextActivity.putExtra(RECIPE_STEPS_MODEL, steps);
        startActivity(moveToNextActivity);

    }
}
