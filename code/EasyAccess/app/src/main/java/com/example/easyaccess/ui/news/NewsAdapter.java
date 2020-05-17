package com.example.easyaccess.ui.news;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.NetworkImageView;
import com.example.easyaccess.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class NewsAdapter extends BaseAdapter {
    ArrayList<NewsItem> list;
    LayoutInflater inflater;
    RequestQueue requestQueue;

    public NewsAdapter(Context context, RequestQueue requestQueue, ArrayList<NewsItem> list){
        this.inflater = LayoutInflater.from(context);
        this.requestQueue = requestQueue;
        this.list = list;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = inflater.inflate(R.layout.news_item, null);
        TextView id = view.findViewById(R.id.news_id);
        TextView title = view.findViewById(R.id.news_title);
        TextView author = view.findViewById(R.id.news_author);
        TextView time = view.findViewById(R.id.news_pub_time);
        TextView cate = view.findViewById(R.id.news_cate);
        NetworkImageView niv = view.findViewById(R.id.news_img);
        id.setText(list.get(position).get_id());
        title.setText(list.get(position).getTitle());
        author.setText(list.get(position).getAuthor());
        cate.setText(list.get(position).getCategory());
        String nowTime = list.get(position).getPubTime();//某个时间戳;
        time.setText(nowTime);
        return view;
    }
}
