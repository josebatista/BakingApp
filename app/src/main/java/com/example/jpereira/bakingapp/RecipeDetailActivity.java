package com.example.jpereira.bakingapp;

import android.content.Intent;
import android.content.res.Configuration;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.example.jpereira.bakingapp.databinding.ActivityRecipeDetailBinding;
import com.example.jpereira.bakingapp.domain.Recipe;
import com.example.jpereira.bakingapp.domain.Step;

public class RecipeDetailActivity extends AppCompatActivity {

    private static final String STEP_POSITION = "position";
    private static final String DETAIL_FRAGMENT = "step_fragment";

    private Recipe mRecipe;
    private int mPosition;
    private int mTotal;
    private ActivityRecipeDetailBinding mBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_detail);

        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_recipe_detail);

        Intent intent = getIntent();
        if (intent != null) {
            if (intent.hasExtra(RecipeStepsFragment.RECIPE)) {
                mRecipe = intent.getParcelableExtra(RecipeStepsFragment.RECIPE);
            }
            if (intent.hasExtra(RecipeStepsFragment.POSITION_CLICKED)) {
                mPosition = intent.getIntExtra(RecipeStepsFragment.POSITION_CLICKED, -1) - 1;
            }
            if (intent.hasExtra(RecipeStepsFragment.LENGTH)) {
                mTotal = intent.getIntExtra(RecipeStepsFragment.LENGTH, 0);
            }
        }

        verifyFullScreen();

        if (savedInstanceState != null) {
            mPosition = savedInstanceState.getInt(STEP_POSITION);
        } else {
            setFragment();
        }
    }

    public void setFragment() {
        if (mPosition < 0) { //ingredient
            mBinding.recipeDetailBtPrevious.setVisibility(View.INVISIBLE);
            IngredientListFragment mIngredientFragment = new IngredientListFragment();
            mIngredientFragment.setIngredients(mRecipe.getIngredients());
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.recipe_detail_fragment_container, mIngredientFragment)
                    .commit();
        } else { //steps
            mBinding.recipeDetailBtPrevious.setVisibility(View.VISIBLE);
            if (mPosition >= (mTotal - 2)) {
                mBinding.recipeDetailBtNext.setVisibility(View.INVISIBLE);
            } else {
                mBinding.recipeDetailBtNext.setVisibility(View.VISIBLE);
            }
            Step step = mRecipe.getSteps().get(mPosition);
            RecipeStepDetailFragment mStepFragment = new RecipeStepDetailFragment();
            mStepFragment.setStep(step);
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.recipe_detail_fragment_container, mStepFragment)
                    .commit();
        }
    }

    private void verifyFullScreen() {
        if (getResources().getConfiguration().orientation ==
                Configuration.ORIENTATION_LANDSCAPE) {
            mBinding.recipeDetailBtNext.setVisibility(View.GONE);
            mBinding.recipeDetailBtPrevious.setVisibility(View.GONE);
        } else {
            mBinding.recipeDetailBtNext.setVisibility(View.VISIBLE);
            mBinding.recipeDetailBtPrevious.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
        super.onSaveInstanceState(outState, outPersistentState);
        outState.putInt(STEP_POSITION, mPosition);
    }

    public void btnPrevious(View view) {
        mPosition--;
        setFragment();
    }

    public void btnNext(View view) {
        mPosition++;
        setFragment();
    }
}
