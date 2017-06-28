package com.freelance.samuelagbede.ubake;

import android.content.Context;
import android.content.Intent;
import android.content.res.AssetManager;
import android.os.Binder;
import android.util.JsonReader;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.freelance.samuelagbede.ubake.Models.Recipes;
import com.freelance.samuelagbede.ubake.Utilities.RecipeUtils;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.util.ArrayList;

/**
 * Created by Agbede Samuel D on 6/26/2017.
 */

public class CustomWidgetFactory implements RemoteViewsService.RemoteViewsFactory {
    Context context;
    ArrayList<Recipes> recipes;

    public CustomWidgetFactory(Context context){
        this.context = context;
    }
    @Override
    public void onCreate() {
        final long identityToken = Binder.clearCallingIdentity();
        try {
            AssetManager assetManager = context.getAssets();
            InputStream inputStream;
            String string = "";
            try
            {
                inputStream = assetManager.open("bake.json");
                if(inputStream != null)
                {
                    BufferedReader br = null;
                    StringBuilder sb = new StringBuilder();

                    String line;
                    try {

                        br = new BufferedReader(new InputStreamReader(inputStream));
                        while ((line = br.readLine()) != null) {
                            sb.append(line);
                        }

                    } catch (IOException e) {
                        e.printStackTrace();
                    } finally {
                        if (br != null) {
                            try {
                                br.close();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                    string =  sb.toString();
                }
                recipes = RecipeUtils.parseHTTPResponse(string);
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
        catch (Exception e){
            e.printStackTrace();
        }
        Binder.restoreCallingIdentity(identityToken);

    }

    @Override
    public void onDataSetChanged() {
        /*final long identityToken = Binder.clearCallingIdentity();
        try {
            AssetManager assetManager = context.getAssets();
            InputStream inputStream;

            String string = "";
            try
            {
                inputStream = assetManager.open("bake.json");
                if(inputStream != null)
                {
                    BufferedReader br = null;
                    StringBuilder sb = new StringBuilder();

                    String line;
                    try {

                        br = new BufferedReader(new InputStreamReader(inputStream));
                        while ((line = br.readLine()) != null) {
                            sb.append(line);
                        }

                    } catch (IOException e) {
                        e.printStackTrace();
                    } finally {
                        if (br != null) {
                            try {
                                br.close();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                    string =  sb.toString();
                }
                recipes = RecipeUtils.parseHTTPResponse(string);
                Log.d("recipes", recipes.size() + " " + recipes.get(0).getName());
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
        catch (Exception e){
            e.printStackTrace();
        }
        Binder.restoreCallingIdentity(identityToken);*/
    }

    @Override
    public void onDestroy() {
        if(recipes != null)
            recipes = null;
    }

    @Override
    public int getCount() {
        return recipes == null ? 0 : recipes.size();
    }

    @Override
    public RemoteViews getViewAt(int position) {
        if (recipes == null) {
            return null;
        }
        RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.gridview_individual_items);
        remoteViews.setTextViewText (R.id.widget_grid_view_recipe_name, recipes.get(position).getName() +  " ingredients");

        ArrayList<Recipes.Ingredients> ingredients = recipes.get(position).getIngredients();

        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < ingredients.size(); i++){
            stringBuilder.append(ingredients.get(i).getQuantity() + " ").append(ingredients.get(i).getMeasure()+ " ").append(ingredients.get(i).getIngredient()+"\n");
            remoteViews.setTextViewText(R.id.widget_grid_view_recipe_description, stringBuilder.toString());
        }

        remoteViews.setOnClickFillInIntent(R.id.widget_grid_view_recipe_description, new Intent());
        return remoteViews;
    }

    @Override
    public RemoteViews getLoadingView() {
        return null;
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }
}
