package com.example.jpereira.bakingapp;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.jpereira.bakingapp.adapter.IngredientListAdapter;
import com.example.jpereira.bakingapp.databinding.FragmentIngredientListBinding;
import com.example.jpereira.bakingapp.domain.Ingredient;

import java.util.ArrayList;

/**
 * Created by jpereira on 19/08/17.
 */

public class IngredientListFragment extends Fragment {

    private FragmentIngredientListBinding mBinding;
    private IngredientListAdapter mAdapter;
    private ArrayList<Ingredient> mIngredients;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View viewRoot = inflater.inflate(R.layout.fragment_ingredient_list, container, false);

        mBinding = DataBindingUtil.bind(viewRoot);
        mAdapter = new IngredientListAdapter();
        mBinding.fragmentIngredientLabel.setText(getString(R.string.ingredient_label));
        mBinding.rvIngredients.setHasFixedSize(true);
        mBinding.rvIngredients.setLayoutManager(new LinearLayoutManager(getContext()));
        mBinding.rvIngredients.setAdapter(mAdapter);

        if (mIngredients != null) {
            mAdapter.swapIngredient(mIngredients);
        }

        return viewRoot;
    }

    public void setIngredients(ArrayList<Ingredient> mIngredients) {
        this.mIngredients = mIngredients;
    }
}
