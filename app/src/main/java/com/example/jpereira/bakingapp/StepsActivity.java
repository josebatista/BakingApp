package com.example.jpereira.bakingapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;

import com.example.jpereira.bakingapp.domain.Recipe;
import com.example.jpereira.bakingapp.domain.Step;

import java.util.ArrayList;

public class StepsActivity extends AppCompatActivity {

    private Recipe mRecipe;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_steps);

        Intent intent = getIntent();
        if (intent != null && intent.hasExtra(MainActivity.RECIPE)) {
            mRecipe = intent.getParcelableExtra(MainActivity.RECIPE);

            FragmentManager fragmentManager = getSupportFragmentManager();

            ArrayList<Object> steps = new ArrayList<>();
            steps.add("Ingredients");
            for (Step step : mRecipe.getSteps()) {
                steps.add(step);
            }

            RecipeStepsFragment stepsFragment = new RecipeStepsFragment();
            stepsFragment.setSteps(steps);
            stepsFragment.setRecipe(mRecipe);

            fragmentManager.beginTransaction()
                    .replace(R.id.steps_fragment_container, stepsFragment)
                    .commit();
        }
    }
}
