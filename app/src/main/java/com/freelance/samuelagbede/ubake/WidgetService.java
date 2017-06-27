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
        return new CustomWidgetFactory(getApplicationContext());
    }
}
