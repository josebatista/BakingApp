package com.example.jpereira.bakingapp.interfaces;

import com.example.jpereira.bakingapp.domain.Recipe;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.http.GET;

/**
 * Created by jpereira on 06/08/17.
 */

public interface BackingAppService {

    String BASE_URL = "https://d17h27t6h515a5.cloudfront.net/topher/2017/May/59121517_baking/";

    @GET("baking.json")
    Call<ArrayList<Recipe>> listRecipes();
}
