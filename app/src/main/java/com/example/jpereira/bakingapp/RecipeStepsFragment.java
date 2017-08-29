package com.example.jpereira.bakingapp;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.jpereira.bakingapp.adapter.StepListAdapter;
import com.example.jpereira.bakingapp.databinding.FragmentRecipeStepsBinding;
import com.example.jpereira.bakingapp.domain.Recipe;

import java.util.ArrayList;

/**
 * Created by jpereira on 14/08/17.
 */

public class RecipeStepsFragment extends Fragment implements StepListAdapter.StepListClickListener {

    private static final String TAG = RecipeStepsFragment.class.getSimpleName();
    public static final String LENGTH = "length";
    public static final String POSITION_CLICKED = "position_clicked";
    public static final String RECIPE = "recipe";

    private StepListAdapter mAdapter;
    private ArrayList<Object> mSteps;
    private FragmentRecipeStepsBinding mBinding;
    private Recipe mRecipe;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View viewRoot = inflater.inflate(R.layout.fragment_recipe_steps, container, false);
        mBinding = DataBindingUtil.bind(viewRoot);
        mAdapter = new StepListAdapter(this);
        mBinding.fragmnetRvRecipeSteps.setHasFixedSize(true);
        mBinding.fragmnetRvRecipeSteps.setLayoutManager(
                new LinearLayoutManager(viewRoot.getContext())
        );
        mBinding.fragmnetRvRecipeSteps.setAdapter(mAdapter);

        if (mSteps != null) {
            mAdapter.swapSteps(mSteps);
        }

        return viewRoot;
    }

    public void setSteps(ArrayList<Object> steps) {
        mSteps = steps;
    }

    public void setRecipe(Recipe mRecipe) {
        this.mRecipe = mRecipe;
    }

    @Override
    public void onClick(int position) {
        Intent intent = new Intent(getContext(), RecipeDetailActivity.class);
        intent.putExtra(POSITION_CLICKED, position);
        intent.putExtra(LENGTH, mSteps.size());
        intent.putExtra(RECIPE, mRecipe);

        startActivity(intent);

    }
}
