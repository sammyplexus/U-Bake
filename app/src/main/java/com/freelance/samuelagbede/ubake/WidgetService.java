package com.freelance.samuelagbede.ubake;

import android.content.Intent;
import android.content.res.AssetManager;
import android.os.Binder;
import android.util.JsonReader;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.freelance.samuelagbede.ubake.Models.Recipes;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.util.ArrayList;

/**
 * Created by Agbede Samuel D on 6/25/2017.
 */

public class WidgetService extends RemoteViewsService {
    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        return new RemoteViewsFactory() {
            @Override
            public void onCreate() {

            }

            ArrayList<Recipes> recipes;

            @Override
            public void onDataSetChanged() {
                final long identityToken = Binder.clearCallingIdentity();
                try {
                    AssetManager assetManager = getAssets();
                    InputStream inputStream = assetManager.open("bake.json");
                    JsonReader jsonReader = new JsonReader(new InputStreamReader(inputStream, "UTF-8"));
                    Type type = new TypeToken<ArrayList<Recipes>>(){}.getType();
                    recipes = new Gson().fromJson(String.valueOf(jsonReader), type);
                }
                catch (Exception e){
                    e.printStackTrace();
                }
                Binder.restoreCallingIdentity(identityToken);
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
                if (recipes == null || position > recipes.size()) {
                    return null;
                }
                RemoteViews remoteViews = new RemoteViews(getPackageName(), R.layout.gridview_individual_items);
                remoteViews.setTextViewText (R.id.widget_grid_view_recipe_name, recipes.get(position).getName());
                remoteViews.setTextViewText(R.id.widget_grid_view_recipe_description, recipes.get(position).getIngredients().toString());
                remoteViews.setOnClickFillInIntent(R.id.widget_grid_view_recipe_description, new Intent());
                return remoteViews;
            }

            @Override
            public RemoteViews getLoadingView() {
                return null;
            }

            @Override
            public int getViewTypeCount() {
                return 0;
            }

            @Override
            public long getItemId(int position) {
                return position;
            }

            @Override
            public boolean hasStableIds() {
                return false;
            }
        };
    }
}
