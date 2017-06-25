package com.freelance.samuelagbede.ubake;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.util.Config;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import com.freelance.samuelagbede.ubake.Adapters.SelectRecipesRecyclerAdapter;
import com.freelance.samuelagbede.ubake.Models.Recipes;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;


/**
 * An activity representing a list of Recipes. This activity
 * has different presentations for handset and tablet-size devices. On
 * handsets, the activity presents a list of items, which when touched,
 * lead to a {@link RecipesDetailActivity} representing
 * item details. On tablets, the activity presents the list of items and
 * item details side-by-side using two vertical panes.
 */
//I use the same interface for listening for all the recyclerviews
public class RecipesListActivity extends AppCompatActivity implements SelectRecipesRecyclerAdapter.recyclerListener {

    public static final String INDIVIDUAL_STEPS_CLICKED = "individual_steps_clicked";
    public static final String TOTAL_STEPS_ARRAYLIST = "total_steps_arraylist";
    public static final String RECIPE_POSITION = "recipe_position";
    /**
     * Whether or not the activity is in two-pane mode, i.e. running on a tablet
     * device.
     */
    private boolean mTwoPane;
    Recipes recipes;
    ArrayList<Recipes.Steps> steps;
    ArrayList<Recipes.Ingredients> ingredients;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipes_list);

        if (getSupportActionBar() != null)
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        if (getIntent().hasExtra(MainActivity.RECIPE_DETAIL_MODEL))
        {
            Intent intent = getIntent();
            recipes = intent.getParcelableExtra(MainActivity.RECIPE_DETAIL_MODEL);
            steps = intent.getParcelableArrayListExtra(MainActivity.RECIPE_STEPS_MODEL);
            ingredients = intent.getParcelableArrayListExtra(MainActivity.RECIPE_INGREDIENTS_MODEL);
        }

        getSupportActionBar().setTitle(recipes.getName());

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recipes_list);
        setupRecyclerView(recyclerView);

        if (findViewById(R.id.recipes_detail_container) != null) {
            // The detail container view will be present only in the
            // large-screen layouts (res/values-w900dp).
            // If this view is present, then the
            // activity should be in two-pane mode.
            mTwoPane = true;
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()){
            case android.R.id.home:
        }
        return super.onOptionsItemSelected(item);
    }

    private void setupRecyclerView(@NonNull RecyclerView recyclerView) {
        recyclerView.setAdapter(new SimpleItemRecyclerViewAdapter(recipes, steps, ingredients, this));
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        //Check for support for landscape here.
        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE){

        }
    }

    @Override
    public void onRecyclerItemClick(int position) {
        //It is -1 because of the extra view at the top I am taking into consideration
        if (position > 0){
            Recipes.Steps step = steps.get(position-1);
            if (mTwoPane) {
                Bundle arguments = new Bundle();
                arguments.putParcelable(INDIVIDUAL_STEPS_CLICKED, step);
                RecipesDetailFragment fragment = new RecipesDetailFragment();
                fragment.setArguments(arguments);
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.recipes_detail_container, fragment)
                        .commit();
            } else {
                Intent intent = new Intent(this, RecipesDetailActivity.class);
                intent.putExtra(TOTAL_STEPS_ARRAYLIST, steps);
                intent.putExtra(INDIVIDUAL_STEPS_CLICKED, step);
                intent.putExtra(RECIPE_POSITION, position-1);
                startActivity(intent);
            }
        }

    }



    public class SimpleItemRecyclerViewAdapter
            extends RecyclerView.Adapter<SimpleItemRecyclerViewAdapter.ViewHolder> {

        SelectRecipesRecyclerAdapter.recyclerListener listener;

        private final Recipes recipes;
        private ArrayList<Recipes.Steps> steps;
        private ArrayList<Recipes.Ingredients> ingredients;

        public SimpleItemRecyclerViewAdapter(Recipes recipes, ArrayList<Recipes.Steps> steps, ArrayList<Recipes.Ingredients> ingredients, SelectRecipesRecyclerAdapter.recyclerListener listener) {
            this.recipes = recipes;
            this.steps = steps;
            this.ingredients = ingredients;
            this.listener = listener;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.recipes_list_content, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(final ViewHolder holder, int position) {
            StringBuilder stringBuilder = new StringBuilder();
            for (int i = 0; i < ingredients.size(); i++){
                stringBuilder.append(ingredients.get(i).getQuantity() + " ").append(ingredients.get(i).getMeasure()+ " ").append(ingredients.get(i).getIngredient()+"\n");
            }

            if (position == 0){
                holder.recipe_clicked_items.setText(stringBuilder.toString());
            }
            else {
                holder.itemView.setBackgroundColor(getResources().getColor(R.color.colorAccent));

                holder.recipe_clicked_items.setText(steps.get(position-1).getDescription());
            }

        }

        @Override
        public int getItemCount() {
            if (recipes == null || steps == null || ingredients == null){
                return 0;
            }
            else {
                //the one is for the extra view on the top that shows just the ingredients
                return steps.size()+ 1;
            }


        }

        public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
            @BindView(R.id.tv_recipe_clicked_items)
            public TextView recipe_clicked_items;

            public ViewHolder(View view) {
                super(view);
                ButterKnife.bind(this, view);
                view.setOnClickListener(this);
            }

            @Override
            public void onClick(View v) {
                listener.onRecyclerItemClick(getAdapterPosition());
            }
        }
    }
}
