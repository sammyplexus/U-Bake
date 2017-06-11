package com.freelance.samuelagbede.ubake;

import android.content.Intent;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.TextView;

import com.freelance.samuelagbede.ubake.Adapters.SelectRecipesRecyclerAdapter;
import com.freelance.samuelagbede.ubake.Models.Recipes;
import com.freelance.samuelagbede.ubake.Utilities.NetworkUtils;
import com.freelance.samuelagbede.ubake.Utilities.RecipeUtils;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import org.json.JSONException;

import java.io.IOException;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.freelance.samuelagbede.ubake.Utilities.NetworkUtils.MakeNetworkCall;
import static com.freelance.samuelagbede.ubake.Utilities.RecipeUtils.parseHTTPResponse;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<ArrayList<Recipes>>, SelectRecipesRecyclerAdapter.recyclerListener{

    public static final String RECIPE_DETAIL_MODEL = "recipe_detail_model";
    public static final String RECIPE_INGREDIENTS_MODEL = "recipe_ingredients_model";
    public static final String RECIPE_STEPS_MODEL = "recipe_steps_model";
    @BindView(R.id.recyclerview_select_recipes) RecyclerView mRecyclerView;
    SelectRecipesRecyclerAdapter recyclerAdapter;
    GridLayoutManager layoutManager;
    static final int LOADER_ID = 2321;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        layoutManager = new GridLayoutManager(this, 1);
        //getResources().getInteger(R.integer.span_count)
        recyclerAdapter = new SelectRecipesRecyclerAdapter(this, this);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setAdapter(recyclerAdapter);

        Loader<ArrayList<Recipes>> loader = getSupportLoaderManager().getLoader(LOADER_ID);
        if (loader == null){
            getSupportLoaderManager().initLoader(LOADER_ID, null, this);
        }
        else {
            getSupportLoaderManager().restartLoader(LOADER_ID, null, this);
        }
    }


    @Override
    public Loader<ArrayList<Recipes>> onCreateLoader(int id, Bundle args) {
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
        recyclerAdapter.swapData(data);
    }

    @Override
    public void onLoaderReset(Loader<ArrayList<Recipes>> loader) {

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
