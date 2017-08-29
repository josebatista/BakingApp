package com.example.jpereira.bakingapp;

import android.app.ProgressDialog;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;

import com.example.jpereira.bakingapp.adapter.RecipeListAdapter;
import com.example.jpereira.bakingapp.databinding.ActivityMainBinding;
import com.example.jpereira.bakingapp.domain.Recipe;
import com.example.jpereira.bakingapp.interfaces.BackingAppService;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity implements RecipeListAdapter.
        RecipeListClickListener {

    private static final String TAG = MainActivity.class.getSimpleName();
    private static final String RECIPES = "recipes";
    public static final String RECIPE = "recipe";

    private ArrayList<Recipe> mRecipes;
    private ProgressDialog mProgressDialog;
    private ActivityMainBinding mBinding;
    private RecipeListAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_main);

        mAdapter = new RecipeListAdapter(this);
        mBinding.rvRecipes.setHasFixedSize(true);
        mBinding.rvRecipes.setLayoutManager(new LinearLayoutManager(this));
        mBinding.rvRecipes.setAdapter(mAdapter);

        if (savedInstanceState != null && savedInstanceState.containsKey(RECIPES)) {
            mRecipes = savedInstanceState.getParcelableArrayList(RECIPES);
            mAdapter.swapList(mRecipes);
        } else {
            loadRecipes();
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putParcelableArrayList(RECIPES, mRecipes);
        super.onSaveInstanceState(outState);
    }

    private void loadRecipes() {
        mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        mProgressDialog.setIndeterminate(true);
        mProgressDialog.setMessage("Fetching Data...");
        mProgressDialog.show();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BackingAppService.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        BackingAppService service = retrofit.create(BackingAppService.class);
        service.listRecipes().enqueue(new Callback<ArrayList<Recipe>>() {
            @Override
            public void onResponse(Call<ArrayList<Recipe>> call,
                                   Response<ArrayList<Recipe>> response) {
                mRecipes = response.body();
                mAdapter.swapList(mRecipes);

                Log.d(TAG, "Recipes: " + mRecipes);
                mProgressDialog.dismiss();
            }

            @Override
            public void onFailure(Call<ArrayList<Recipe>> call, Throwable t) {
                Log.e(TAG, "ERROR: " + t.getMessage());
            }
        });
    }

    @Override
    public void onClick(int position) {
        Intent intent = new Intent(this, StepsActivity.class);
        intent.putExtra(RECIPE, mRecipes.get(position));

        startActivity(intent);
    }
}
