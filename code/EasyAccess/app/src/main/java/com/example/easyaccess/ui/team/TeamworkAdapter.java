package com.example.easyaccess.ui.team;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.easyaccess.R;
import com.example.easyaccess.ui.download.DownloadAdapter;
import com.example.easyaccess.ui.download.DownloadItem;

import java.util.ArrayList;

public class TeamworkAdapter extends RecyclerView.Adapter<TeamworkAdapter.LinearViewHolder>{
    private Context mContext;
    private ArrayList<TeamworkItem> list;
    private OnItemClickListener mOnItemClickListener;

    public TeamworkAdapter(Context context, ArrayList<TeamworkItem> list){
        this.mContext = context;
        this.list = list;
    }

    @NonNull
    @Override
    public TeamworkAdapter.LinearViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new LinearViewHolder(LayoutInflater.from(mContext).inflate(R.layout.teamwork_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull LinearViewHolder holder, final int position) {
        final TeamworkItem teamworkItem = list.get(position);
        holder.tvName.setText(teamworkItem.getTitle());
        if(mOnItemClickListener != null){
            holder.itemView.setOnClickListener(new View.OnClickListener(){

                @Override
                public void onClick(View v) {
                    mOnItemClickListener.onClick(position);
                }
            });
        }
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
        public TextView tvName;

        public LinearViewHolder(View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.wanted_title);
        }
    }


    public interface OnItemClickListener{
        void onClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener){
        this.mOnItemClickListener = onItemClickListener;
    }
}
