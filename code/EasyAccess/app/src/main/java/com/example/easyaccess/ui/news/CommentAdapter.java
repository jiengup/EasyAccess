package com.example.easyaccess.ui.news;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.NetworkImageView;

import com.example.easyaccess.LoginActivity;
import com.example.easyaccess.R;
import com.sackcentury.shinebuttonlib.ShineButton;

import java.util.List;

import static com.example.easyaccess.utils.NetworkImageUtils.networkImageLoad;

public class CommentAdapter extends BaseAdapter {

    private LayoutInflater inflater;
    private RequestQueue requestQueue;
    private List<CommentItem> commentItemList;
    private boolean isLogin;

    public CommentAdapter(Context context, RequestQueue requestQueue, List<CommentItem> commentItemList, boolean isLogin){
        this.requestQueue = requestQueue;
        this.inflater = LayoutInflater.from(context);
        this.commentItemList = commentItemList;
        this.isLogin = isLogin;
    }

    @Override
    public int getCount() {
        return commentItemList.size();
    }

    @Override
    public Object getItem(int position) {
        return commentItemList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view=inflater.inflate(R.layout.comment_item, null);
        ShineButton btHeart = (ShineButton)view.findViewById(R.id.bt_heart);
        NetworkImageView user_headepicture=(NetworkImageView)view.findViewById(R.id.user_headepicture);
        TextView user_name=(TextView)view.findViewById(R.id.user_name);
        TextView user_release_time=(TextView)view.findViewById(R.id.user_release_time);
        TextView comment_item_content=(TextView)view.findViewById(R.id.comment_item_content);
        TextView total_stars = (TextView)view.findViewById(R.id.star_num);
        networkImageLoad(requestQueue, commentItemList.get(position).getHeadUrl(),user_headepicture);
        user_name.setText(commentItemList.get(position).getNickname());
        user_release_time.setText(commentItemList.get(position).getReleaseTime());
        comment_item_content.setText(commentItemList.get(position).getContent());
        total_stars.setText("(" + commentItemList.get(position).getTotalStars() + ")");
        return view;
    }
}
