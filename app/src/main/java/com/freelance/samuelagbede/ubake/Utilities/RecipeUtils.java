package com.freelance.samuelagbede.ubake.Utilities;


import android.util.Log;

import com.freelance.samuelagbede.ubake.Models.Recipes;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Agbede Samuel D on 6/9/2017.
 */

public class RecipeUtils {
    public static ArrayList<Recipes> parseHTTPResponse(String response) throws JSONException {
        final String RECIPE_ID = "id";
        final String RECIPE_NAME = "name";
        final String RECIPE_INGREDIENTS = "ingredients";
        final String RECIPE_STEPS = "steps";
        final String RECIPE_SERVINGS = "servings";
        final String RECIPE_IMAGE = "image";
        final String INGREDIENTS_QUANTITY = "quantity";
        final String INGREDIENTS_MEASURE = "measure";
        final String INGREDIENTS_INGREDIENT = "ingredient";
        final String STEPS_ID = "id";
        final String STEPS_SHORT_DESCR = "shortDescription";
        final String STEPS_DESCRIPTION = "description";
        final String STEPS_VIDEO_URL = "videoURL";
        final String STEPS_THUMBNAIL_URL = "thumbnailURL";

        ArrayList<Recipes> recipesArrayList = new ArrayList<>();

        JSONArray server_response = new JSONArray(response);

        for (int i = 0; i < server_response.length(); i++)
        {
            JSONObject complete_individual_recipe = server_response.getJSONObject(i);
            Recipes recipes = new Recipes();
            recipes.setId(complete_individual_recipe.getInt(RECIPE_ID));
            recipes.setImage(complete_individual_recipe.getString(RECIPE_IMAGE));
            recipes.setName(complete_individual_recipe.getString(RECIPE_NAME));
            recipes.setServings(complete_individual_recipe.getInt(RECIPE_SERVINGS));

            ArrayList<Recipes.Ingredients> ingredientsArrayList = new ArrayList<>();

            JSONArray recipe_ingredients = complete_individual_recipe.getJSONArray(RECIPE_INGREDIENTS);
            for (int index = 0; index < recipe_ingredients.length(); index++ )
            {
                JSONObject individual_ingredients = recipe_ingredients.getJSONObject(index);
                Recipes.Ingredients ingredients  = new Recipes.Ingredients();

                ingredients.setIngredient(individual_ingredients.getString(INGREDIENTS_INGREDIENT));
                ingredients.setMeasure(individual_ingredients.getString(INGREDIENTS_MEASURE));
                ingredients.setQuantity(individual_ingredients.getInt(INGREDIENTS_QUANTITY));

                ingredientsArrayList.add(ingredients);
            }
         recipes.setIngredients(ingredientsArrayList);

            JSONArray recipe_steps =  complete_individual_recipe.getJSONArray(RECIPE_STEPS);


            ArrayList<Recipes.Steps> stepsArrayList = new ArrayList<>();
            for (int index = 0; index < recipe_steps.length(); index++ )
            {
                JSONObject individual_steps = recipe_steps.getJSONObject(index);

                Recipes.Steps steps  = new Recipes.Steps();

                steps.setId(individual_steps.getInt(STEPS_ID));
                steps.setDescription(individual_steps.getString(STEPS_DESCRIPTION));
                steps.setShortDescription(individual_steps.getString(STEPS_SHORT_DESCR));
                steps.setThumbnailURL(individual_steps.getString(STEPS_THUMBNAIL_URL));
                steps.setVideoURL(individual_steps.getString(STEPS_VIDEO_URL));

                stepsArrayList.add(steps);
            }

            recipes.setSteps(stepsArrayList);

            recipesArrayList.add(recipes);

        }

        return recipesArrayList;
    }
}
