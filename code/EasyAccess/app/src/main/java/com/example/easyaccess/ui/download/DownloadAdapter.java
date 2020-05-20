package com.example.easyaccess.ui.download;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.easyaccess.R;
import com.example.easyaccess.utils.Constants;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;


public class DownloadAdapter extends RecyclerView.Adapter<DownloadAdapter.LinearViewHolder> {
    private Context mContext;
    private ArrayList<DownloadItem> list;

    public DownloadAdapter(Context context, ArrayList<DownloadItem> list) {
        this.mContext = context;
        this.list = list;
    }

    @NonNull
    @Override
    public DownloadAdapter.LinearViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new LinearViewHolder(LayoutInflater.from(mContext).inflate(R.layout.download_item, parent, false));
    }

    public void onBindViewHolder(@NonNull LinearViewHolder holder, final int position) {
        final DownloadItem downloadItem = list.get(position);
        holder.tvName.setText(downloadItem.getName());
        holder.tvDesc.setText(downloadItem.getDesc());
        holder.tvCourse.setText(downloadItem.getBelongToCourse());
        Glide.with(holder.imageView.getContext()).load(Constants.BASE_URL + downloadItem.getIconUrl()).into(holder.imageView);
//        for (int i = 0; i < list.size(); i++) {
//            try {
//                DownloadItem downloadItem = list.get(position);
//                holder.tvtitle.setText(lan.getString("name"));
//                holder.tvcontent.setText(lan.getString("desc"));
//                Glide.with(holder.itemView.getContext()).load(lan.getString(("icon"))).into(holder.imageView);
//            } catch (JSONException e) {
//                e.printStackTrace();
//            }
//        }
        //设置点击事件，跳转至指定的官方下载界面
//        holder.itemView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                try {
//                    JSONObject lan = array1().getJSONObject(position);
//                    Intent intent = new Intent(Intent.ACTION_SEND);
//                    intent.setType("text/plain");
//                    String shareText = lan.getString("download_url");
//                    intent.putExtra(Intent.EXTRA_TEXT, shareText);
////                        Intent intent=new  Intent();
////                        intent.setAction(Intent.ACTION_VIEW);
////                        intent.setData(Uri.parse(lan.getString("download_url")));
//                    mcontext.startActivity(Intent.createChooser(intent, "分享"));
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//            }
//        });
        holder.btnShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setType("text/plain");
                String shareText = downloadItem.getDownloadUrl();
                intent.putExtra(Intent.EXTRA_TEXT, shareText);
//                        Intent intent=new  Intent();
//                        intent.setAction(Intent.ACTION_VIEW);
//                        intent.setData(Uri.parse(lan.getString("download_url")));
                mContext.startActivity(Intent.createChooser(intent, "分享"));
            }
        });
        holder.btnWeb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_VIEW);
                intent.setData(Uri.parse(downloadItem.getDownloadUrl()));
                mContext.startActivity(intent);
            }
        });
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    //得到item数目
    public int getItemCount() {
        return list.size();
    }


    class LinearViewHolder extends RecyclerView.ViewHolder {
        public ImageView imageView;
        public TextView tvName, tvDesc, tvCourse;
        public Button btnWeb, btnShare;


        public LinearViewHolder(View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.tv_picture);
            tvName = itemView.findViewById(R.id.tv_name);
            tvDesc = itemView.findViewById(R.id.tv_desc);
            tvCourse = itemView.findViewById(R.id.tv_course);
            btnWeb = itemView.findViewById(R.id.btn_web);
            btnShare = itemView.findViewById(R.id.btn_share);
        }
    }
}