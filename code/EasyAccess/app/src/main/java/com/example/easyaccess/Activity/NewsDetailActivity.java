package com.example.easyaccess.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.NetworkImageView;
import com.android.volley.toolbox.Volley;
import com.example.easyaccess.R;
import com.example.easyaccess.Item.NewsItem;
import com.example.easyaccess.utils.Constants;
import com.sackcentury.shinebuttonlib.ShineButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import static com.example.easyaccess.utils.NetworkImageUtils.networkImageLoad;

public class NewsDetailActivity extends AppCompatActivity {
    private RequestQueue requestQueue;
    private LinearLayout vg;
    private NewsItem newsItem;
    private ShineButton btLike;
    private boolean isStar;
    private boolean isLogin;
    private Button toOriginal;
    private TextView tv_star;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        LayoutInflater layoutInflater = LayoutInflater.from(this);
        View view = layoutInflater.inflate(R.layout.activity_news_detail, null);
        vg = view.findViewById(R.id.news_detail_linear);
        vg.setPadding(5, 0, 5, 0);
        setContentView(view);
        btLike = (ShineButton) findViewById(R.id.bt_like);
        btLike.init(this);
        tv_star = findViewById(R.id.tv_star);
        toOriginal = findViewById(R.id.btn_original);
        Intent intent = getIntent();
        final String newsId = intent.getStringExtra("news_id");
        requestQueue = Volley.newRequestQueue(this);


        // TODO
        if(1 == 2){
            obtainData(newsId, "null");
        }else{
            obtainData(newsId, "-1");
        }

        SharedPreferences msp = getSharedPreferences("user", MODE_PRIVATE);
        if(!msp.getString("email", "null").equals("null")){
            isLogin = true;
        }
        else{
            isLogin = false;
        }
        isStar = false;
        btLike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!isLogin){
                    Toast.makeText(getApplicationContext(), "请先登陆", Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                    startActivity(intent);
                }
                else{
                    if(!isStar){
                        newsItem.setTotalStar(newsItem.getTotalStar() + 1);
                        tv_star.setText("(" + newsItem.getTotalStar() + ")");
                        isStar = true;
                        modifyNewsStars(newsId, newsItem.getTotalStar());
                    }
                    else{
                        newsItem.setTotalStar(newsItem.getTotalStar() - 1);
                        tv_star.setText("(" + newsItem.getTotalStar() + ")");
                        isStar = false;
                        modifyNewsStars(newsId, newsItem.getTotalStar());
                    }
                }
            }
        });
        toOriginal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri uri = Uri.parse(newsItem.getOriginalUrl());
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(intent);
            }
        });
    }

    public void obtainData(final String newsId, String userID){
        String url = Constants.NEWS_URL + "get_news_detail/?_id=" + newsId;
        try{
            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(url, null, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    try {
                        JSONArray data = response.getJSONArray("data");
                        int code = response.getInt("code");
                        if (code == 0) {
                            //System.out.println(data.length());
                            for (int i = 0; i < data.length(); i++) {
                                newsItem = new NewsItem();
                                JSONObject item = data.getJSONObject(i);
                                newsItem.set_id(item.getString("_id"));
                                newsItem.setTitle(item.getString("title"));
                                newsItem.setThumbnail(item.getString("img_url"));
                                newsItem.setOriginalUrl(item.getString("original_url"));
                                newsItem.setContent(item.getString("content"));
                                newsItem.setPubTime(item.getString("pub_time"));
                                newsItem.setTotalStar(item.getInt("total_star"));
                                newsItem.setCategory(item.getString("cate"));
                                newsItem.setAuthor(item.getString("author"));
                                newsItem.setDesc(item.getString("desc"));
                            }
                            addView();
//                            Toast.makeText(getApplicationContext(), newsItem.getTitle(), Toast.LENGTH_LONG).show();
                        } else {
                            Toast.makeText(getApplicationContext(), "数据错误", Toast.LENGTH_LONG).show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(getApplicationContext(), "网络错误", Toast.LENGTH_LONG).show();
                }
            });
            requestQueue.add(jsonObjectRequest);
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    public void addView(){
        //Toast.makeText(getApplicationContext(),"执行add方法",Toast.LENGTH_LONG).show();
        tv_star.setText("(" + newsItem.getTotalStar() + ")");
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        lp.setMargins(0,100,0,100);
        TextView title = new TextView(NewsDetailActivity.this);
        title.setText(newsItem.getTitle());
        title.setTextSize(30);
        title.setTypeface(Typeface.DEFAULT_BOLD);
        title.setTextColor(Color.parseColor("#000000"));
        title.setLayoutParams(lp);
        vg.addView(title);
        LinearLayout linearLayout=new LinearLayout(NewsDetailActivity.this);
        linearLayout.setOrientation(LinearLayout.HORIZONTAL);

        LinearLayout.LayoutParams text = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT,1);

        TextView from = new TextView(NewsDetailActivity.this);
        from.setText(newsItem.getAuthor());
        from.setTextColor(Color.parseColor("#FF8C00"));
        from.setTextSize(16);
        from.setGravity(Gravity.CENTER);
        from.setLayoutParams(text);



        TextView tag = new TextView(NewsDetailActivity.this);
        tag.setText(newsItem.getCategory());
        tag.setTextColor(Color.parseColor("#006400"));
        tag.setTextSize(16);
        tag.setGravity(Gravity.CENTER);
        tag.setLayoutParams(text);
        TextView time= new TextView(NewsDetailActivity.this);

        String stringDate = newsItem.getPubTime();

        time.setText(stringDate);
        time.setTextColor(Color.parseColor("#9400D3"));
        time.setTextSize(16);
        time.setGravity(Gravity.CENTER);
        time.setLayoutParams(text);
        linearLayout.addView(from);
        linearLayout.addView(tag);
        linearLayout.addView(time);

        lp.setMargins(0,0,0,40);
        linearLayout.setLayoutParams(lp);
        vg.addView(linearLayout);

        TextView tv_desc = new TextView(NewsDetailActivity.this);
        tv_desc.setText("摘要：" + newsItem.getDesc());
        tv_desc.setTextSize(15);
        tv_desc.setTypeface(null, Typeface.ITALIC);
        lp.setMargins(0, 10, 0, 10);
        tv_desc.setLayoutParams(lp);
        vg.addView(tv_desc);

        NetworkImageView nv = new NetworkImageView(NewsDetailActivity.this);
        String imgUrl = newsItem.getThumbnail();
        networkImageLoad(requestQueue, imgUrl, nv);
        vg.addView(nv);


        TextView tv_content = new TextView(NewsDetailActivity.this);
        tv_content.setText("        " + newsItem.getContent());
        tv_content.setTextSize(20);
        tv_content.getPaint().setFakeBoldText(true);
        lp.setMargins(0, 10, 0, 10);
        tv_content.setLayoutParams(lp);
        vg.addView(tv_content);

        Button btn = new Button(NewsDetailActivity.this);
        btn.setText("获取用户评论");
        vg.addView(btn);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!isLogin){
                    Toast.makeText(getApplicationContext(), "请先登陆", Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                    startActivity(intent);
                }
                Intent intent = new Intent();
                intent.putExtra("news_id",getIntent().getStringExtra("news_id"));
                intent.setClass(NewsDetailActivity.this, CommentActivity.class);
                NewsDetailActivity.this.startActivity(intent);
            }
        });

    }

    public void modifyNewsStars(final String newsId, int modified_stars){
        String url = Constants.NEWS_URL + "modify_news_stars/?_id=" + newsId + "&modified_stars=" + modified_stars;
        try{
            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(url, null, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    try {
                        JSONArray data = response.getJSONArray("data");
                        int code = response.getInt("code");
                        if (code == 0) {
                            Toast.makeText(getApplicationContext(), "感谢！", Toast.LENGTH_LONG).show();
                        } else {
                            Toast.makeText(getApplicationContext(), "Ops!", Toast.LENGTH_LONG).show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(getApplicationContext(), "网络错误", Toast.LENGTH_LONG).show();
                }
            });
            requestQueue.add(jsonObjectRequest);
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
