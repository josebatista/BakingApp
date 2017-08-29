package com.example.jpereira.bakingapp.adapter;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.jpereira.bakingapp.R;
import com.example.jpereira.bakingapp.databinding.IngredientItemListBinding;
import com.example.jpereira.bakingapp.domain.Ingredient;

import java.util.ArrayList;

/**
 * Created by jpereira on 19/08/17.
 */

public class IngredientListAdapter extends
        RecyclerView.Adapter<IngredientListAdapter.IngredientViewHolder> {

    private Context mContext;
    private ArrayList<Ingredient> mIngredient;

    @Override
    public IngredientViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        mContext = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View view = inflater.inflate(R.layout.ingredient_item_list, parent, false);

        return new IngredientViewHolder(view);
    }

    @Override
    public void onBindViewHolder(IngredientViewHolder holder, int position) {
        Ingredient ingredient = mIngredient.get(position);
        holder.mBinding.ingredientTvQuantity.setText(String.valueOf(ingredient.getQuantity()));
        holder.mBinding.ingredientTvMeasure.setText(ingredient.getMeasure());
        holder.mBinding.ingredientTvIngredient.setText(ingredient.getIngredient());
    }

    @Override
    public int getItemCount() {
        if (mIngredient != null) {
            return mIngredient.size();
        } else {
            return 0;
        }
    }

    public void swapIngredient(ArrayList<Ingredient> ingredient) {
        mIngredient = ingredient;
        notifyDataSetChanged();
    }

    public class IngredientViewHolder extends RecyclerView.ViewHolder {

        IngredientItemListBinding mBinding;


        public IngredientViewHolder(View itemView) {
            super(itemView);

            mBinding = DataBindingUtil.bind(itemView);
        }
    }
}
