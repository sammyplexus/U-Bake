package com.freelance.samuelagbede.ubake;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


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
public class RecipesListActivity extends AppCompatActivity {

    /**
     * Whether or not the activity is in two-pane mode, i.e. running on a tablet
     * device.
     */
    private boolean mTwoPane;
    Recipes recipes;
    ArrayList<Recipes.Steps> steps;
    ArrayList<Recipes.Ingredients> ingredients;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipes_list);

        if (getIntent().hasExtra(MainActivity.RECIPE_DETAIL_MODEL))
        {
            Intent intent = getIntent();
            recipes = intent.getParcelableExtra(MainActivity.RECIPE_DETAIL_MODEL);
            steps = intent.getParcelableArrayListExtra(MainActivity.RECIPE_STEPS_MODEL);
            ingredients = intent.getParcelableArrayListExtra(MainActivity.RECIPE_INGREDIENTS_MODEL);

        }

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle(getTitle());

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

    private void setupRecyclerView(@NonNull RecyclerView recyclerView) {
        recyclerView.setAdapter(new SimpleItemRecyclerViewAdapter(recipes, steps, ingredients));
    }

    public class SimpleItemRecyclerViewAdapter
            extends RecyclerView.Adapter<SimpleItemRecyclerViewAdapter.ViewHolder> {

        private final Recipes recipes;
        private ArrayList<Recipes.Steps> steps;
        private ArrayList<Recipes.Ingredients> ingredients;

        public SimpleItemRecyclerViewAdapter(Recipes recipes, ArrayList<Recipes.Steps> steps, ArrayList<Recipes.Ingredients> ingredients) {
            this.recipes = recipes;
            this.steps = steps;
            this.ingredients = ingredients;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.recipes_list_content, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(final ViewHolder holder, int position) {
            holder.recipe_clicked_items.setText(steps.get(position).getDescription());

            holder.mView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mTwoPane) {
                        Bundle arguments = new Bundle();
                        //arguments.putString(RecipesDetailFragment.ARG_ITEM_ID, holder.mItem.id);
                        RecipesDetailFragment fragment = new RecipesDetailFragment();
                        fragment.setArguments(arguments);
                        getSupportFragmentManager().beginTransaction()
                                .replace(R.id.recipes_detail_container, fragment)
                                .commit();
                    } else {
                        Context context = v.getContext();
                        Intent intent = new Intent(context, RecipesDetailActivity.class);
                        context.startActivity(intent);
                    }
                }
            });
        }

        @Override
        public int getItemCount() {
            if (recipes == null || steps == null || ingredients == null){
                return 0;
            }
            else {
                return steps.size();
            }


        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            @BindView(R.id.tv_recipe_clicked_items)
            public TextView recipe_clicked_items;
            public View mView;

            public ViewHolder(View view) {
                super(view);
                mView = view;
                ButterKnife.bind(this, view);

            }

        }
    }
}
