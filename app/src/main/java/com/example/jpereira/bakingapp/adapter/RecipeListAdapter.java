package com.example.jpereira.bakingapp.adapter;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.jpereira.bakingapp.R;
import com.example.jpereira.bakingapp.databinding.RecipeItemListBinding;
import com.example.jpereira.bakingapp.domain.Recipe;

import java.util.ArrayList;

/**
 * Created by jpereira on 06/08/17.
 */

public class RecipeListAdapter extends RecyclerView.Adapter<RecipeListAdapter.RecipeViewHolder> {

    private ArrayList<Recipe> mRecipes;
    private Context mContext;
    private RecipeListClickListener mClickHandle;

    public interface RecipeListClickListener {
        void onClick(int position);
    }

    public RecipeListAdapter(RecipeListClickListener mClickHandle) {
        this.mClickHandle = mClickHandle;
    }

    @Override
    public RecipeViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        int layoutId = R.layout.recipe_item_list;
        mContext = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View view = inflater.inflate(layoutId, parent, false);
        view.setFocusable(true);

        return new RecipeViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecipeViewHolder holder, int position) {
        Recipe recipe = mRecipes.get(position);
        holder.mBinding.tvRecipeList.setText(recipe.getName());
    }

    public void swapList(ArrayList<Recipe> recipes) {
        mRecipes = recipes;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        if (mRecipes == null) {
            return 0;
        }
        return mRecipes.size();
    }

    public class RecipeViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        RecipeItemListBinding mBinding;

        public RecipeViewHolder(View itemView) {
            super(itemView);
            mBinding = DataBindingUtil.bind(itemView);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int position = getAdapterPosition();
            mClickHandle.onClick(position);
        }
    }
}
