package com.johnsonnyamweya.newsapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import retrofit2.http.Url;

public class NewsDetailActivity extends AppCompatActivity {

    String title, description, content, imageUrl, url;
    private TextView titleTv, subDescriptionTv, contentTv;
    private ImageView newsIv;
    private Button readNewsBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_detail);

        title = getIntent().getStringExtra("title");
        description = getIntent().getStringExtra("description");
        content = getIntent().getStringExtra("content");
        imageUrl = getIntent().getStringExtra("image");
        url = getIntent().getStringExtra("url");

        titleTv = findViewById(R.id.tv_title);
        subDescriptionTv = findViewById(R.id.tv_sub_description);
        contentTv = findViewById(R.id.tv_content);
        newsIv = findViewById(R.id.iv_news);
        readNewsBtn = findViewById(R.id.btn_read_news);

        titleTv.setText(title);
        subDescriptionTv.setText(description);
        contentTv.setText(content);
        Picasso.get().load(imageUrl).into(newsIv);

        readNewsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse(url));
                startActivity(intent);
            }
        });

    }
}