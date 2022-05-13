package com.johnsonnyamweya.newsapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity implements CategoryRVAdapter.CategoryClickInterface{

//    news API Key
//    2bdba30f21c549cc94d4ed00a514f6a3

    private RecyclerView newsRv, categoryRv;
    private ProgressBar loadingPb;
    private ArrayList<Articles> articlesArrayList;
    private ArrayList<CategoryRVModel> categoryRVModelArrayList;
    private CategoryRVAdapter categoryRVAdapter;
    private NewsRVAdapter newsRVAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        newsRv = findViewById(R.id.rv_news);
        categoryRv = findViewById(R.id.rv_categories);
        loadingPb = findViewById(R.id.pb_loading);
        articlesArrayList = new ArrayList<>();
        categoryRVModelArrayList = new ArrayList<>();
        newsRVAdapter = new NewsRVAdapter(articlesArrayList, this);
        categoryRVAdapter = new CategoryRVAdapter(categoryRVModelArrayList,this, this::onCategoryClick);

        newsRv.setLayoutManager(new LinearLayoutManager(this));
        newsRv.setAdapter(newsRVAdapter);
        categoryRv.setAdapter(categoryRVAdapter);

        getCategories();
        getNews("All");
        newsRVAdapter.notifyDataSetChanged();

    }

    private void getCategories(){
        categoryRVModelArrayList.add(new CategoryRVModel("All", "https://unsplash.com/photos/cBb94xhYAXw"));
        categoryRVModelArrayList.add(new CategoryRVModel("Sports", "https://media.istockphoto.com/photos/various-sport-equipments-on-grass-picture-id949190756"));
        categoryRVModelArrayList.add(new CategoryRVModel("Technology", "https://www.istockphoto.com/photo/data-scientists-male-programmer-using-laptop-analyzing-and-developing-in-various-gm1295900106-389481184"));
        categoryRVModelArrayList.add(new CategoryRVModel("Science", "https://www.istockphoto.com/poto/digitally-generated-image-of-the-research-lab-gm1302609512-394296169"));
        categoryRVModelArrayList.add(new CategoryRVModel("General", "https://media.istockphoto.com/photos/blurred-holidays-lights-in-a-raw-bokeh-style-black-background-enough-picture-id1284238625?s=612x612"));
        categoryRVModelArrayList.add(new CategoryRVModel("Business", "https://media.istockphoto.com/photos/glad-to-work-with-you-picture-id951514270?s=612x612"));
        categoryRVModelArrayList.add(new CategoryRVModel("Entertainment", "https://media.istockphoto.com/photos/rock-band-playing-at-a-nightclub-picture-id1129172065?s=612x612"));
        categoryRVModelArrayList.add(new CategoryRVModel("Health", "https://media.istockphoto.com/photos/medicine-doctor-touching-electronic-medical-record-on-tablet-dna-picture-id1165067633"));

        categoryRVAdapter.notifyDataSetChanged();
    }

    private void getNews(String category){
        loadingPb.setVisibility(View.VISIBLE);
        articlesArrayList.clear();
        String categoryUrl = "https://newsapi.org/v2/everything?q=" + category + "&from=2022-03-15&sortBy=popularity&apiKey=2bdba30f21c549cc94d4ed00a514f6a3";
        String url = "https://newsapi.org/v2/everything?q=All&from=2022-03-15&sortBy=popularity&apiKey=2bdba30f21c549cc94d4ed00a514f6a3";
        String BASE_URL = "https://newsapi.org/";
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        RetrofitAPI retrofitAPI = retrofit.create(RetrofitAPI.class);
        Call<NewsModel> call;

        if (category.equals("All")){
            call = retrofitAPI.getAllNews(url);
        }else{
            call = retrofitAPI.getNewsByCategory(categoryUrl);
        }

        call.enqueue(new Callback<NewsModel>() {
            @Override
            public void onResponse(Call<NewsModel> call, Response<NewsModel> response) {
                NewsModel newsModel = response.body();
                loadingPb.setVisibility(View.GONE);
                ArrayList<Articles> articles = newsModel.getArticles();

                for (int i=0; i<articles.size(); i++){
                    articlesArrayList.add(new Articles(articles.get(i).getTitle(), articles.get(i).getDescription(),
                            articles.get(i).getUrlToImage(), articles.get(i).getUrl(), articles.get(i).getContent()));
                }

                newsRVAdapter.notifyDataSetChanged();

            }

            @Override
            public void onFailure(Call<NewsModel> call, Throwable t) {
                Toast.makeText(MainActivity.this, "fail to get news", Toast.LENGTH_SHORT).show();
            }
        });

    }

    @Override
    public void onCategoryClick(int position) {
        String category = categoryRVModelArrayList.get(position).getCategory();
        getNews(category);
    }
}