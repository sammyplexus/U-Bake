package com.freelance.samuelagbede.ubake.Adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.Layout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.freelance.samuelagbede.ubake.Models.Recipes;
import com.freelance.samuelagbede.ubake.R;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Agbede Samuel D on 6/9/2017.
 */

public class SelectRecipesRecyclerAdapter extends RecyclerView.Adapter<SelectRecipesRecyclerAdapter.ViewHolder> {
    public interface recyclerListener{
        void onRecyclerItemClick(int position);
    }

    recyclerListener listener;
    Context context;
    ArrayList<Recipes> recipesArrayList;

    public SelectRecipesRecyclerAdapter(Context context, recyclerListener listener){
        this.context = context;
        this.listener = listener;
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.recipe_recyclerview_individual_layout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.title.setText(recipesArrayList.get(position).getName());
    }

    @Override
    public int getItemCount() {
        return recipesArrayList == null ? 0 : recipesArrayList.size();
    }

    public void swapData(ArrayList<Recipes> recipes){
        if (recipes != null){
            this.recipesArrayList = recipes;
            notifyDataSetChanged();
        }

    }

    public ArrayList<Recipes> getData(){
        return recipesArrayList;
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        @BindView(R.id.tv_title_recipes)
        public TextView title;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            listener.onRecyclerItemClick(getAdapterPosition());
        }
    }
}
