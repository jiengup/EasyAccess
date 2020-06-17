package com.example.easyaccess.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.easyaccess.ui.View.LoadListView;
import com.example.easyaccess.Activity.NewsDetailActivity;
import com.example.easyaccess.R;
import com.example.easyaccess.Adapter.NewsAdapter;
import com.example.easyaccess.Item.NewsItem;
import com.example.easyaccess.ui.ViweModel.NewsViewModel;
import com.example.easyaccess.utils.Constants;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class NewsFragment extends Fragment implements  LoadListView.ILoadListener{
    public static ArrayList<NewsItem> newsList;
    private LoadListView llv;
    private RequestQueue requestQueue;
    private TextView news_id;
    private NewsAdapter mAdapter;
    private NewsItem newsItem;
    private int news_start;
    private int news_end;
    private NewsViewModel newsViewModel;
    public static NewsFragment newInstance(){ return new NewsFragment(); }
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_news, container, false);
        llv = (LoadListView)root.findViewById(R.id.news_list);
        llv.setInterface(this);
        news_id = root.findViewById(R.id.news_id);
        requestQueue = Volley.newRequestQueue(getContext());
        newsList = new ArrayList<NewsItem>();
        news_start = 0;
        news_end = 6;
        return root;
    }

    @Override
    public void onActivityCreated(@NonNull Bundle savedInstanceState) {

        super.onActivityCreated(savedInstanceState);
        llv.setOnItemClickListener(new AdapterView.OnItemClickListener(){

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                news_id = view.findViewById(R.id.news_id);
                Intent intent = new Intent();
                intent.putExtra("news_id", news_id.getText().toString());
                intent.setClass(getContext(), NewsDetailActivity.class);
                startActivity(intent);
            }
        });
        obtainData();
        mAdapter = new NewsAdapter(getContext(), requestQueue, newsList);
        llv.setAdapter(mAdapter);
    }

    private void obtainData(){
        String url = Constants.NEWS_URL + "get_news_list/" + "?start=" + news_start + "&end=" + news_end;
        try{
            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(url, null, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    try {
                        int code = response.getInt("code");
                        if (code == 0) {
                            JSONArray data = response.getJSONArray("data");
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
                                newsList.add(newsItem);
                            }
                            news_start += 6;
                            news_end += 6;
                        }
                        else if(code == 2){
                            Toast.makeText(getContext(), "没有了...", Toast.LENGTH_LONG).show();
                        }
                        else{
                            Toast.makeText(getContext(), "获取新闻数据错误", Toast.LENGTH_LONG).show();
                        }
                        mAdapter.notifyDataSetChanged();
                    } catch (JSONException e) {
                        Toast.makeText(getContext(), e.toString(), Toast.LENGTH_LONG).show();
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(getContext(), "获取失败", Toast.LENGTH_LONG).show();
                }
            });
            requestQueue.add(jsonObjectRequest);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public void onLoad() {
        //System.out.println("scorlling!!");
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                obtainData();
                llv.LoadingComplete();
            }
        }, 1000);
    }
}
