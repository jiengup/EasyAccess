package com.example.easyaccess.ui.news;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.example.easyaccess.LoginActivity;
import com.example.easyaccess.R;
import com.example.easyaccess.ui.news.adapter.NewsAdapter;

public class NewsFragment extends Fragment {

    private NewsViewModel homeViewModel;
    private Button mBtnCN, mBtnTN, mBtnSearch;
    private RecyclerView mRvList;
    private NewsAdapter adapter;
    private Context mContext;

    private LinearLayoutManager m_layoutManager;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        homeViewModel =
                ViewModelProviders.of(this).get(NewsViewModel.class);
        mContext = getContext();
        View root = inflater.inflate(R.layout.fragment_news, container, false);

        mRvList = root.findViewById(R.id.list_news);
        m_layoutManager = new LinearLayoutManager(mContext);
        mRvList.setLayoutManager(new LinearLayoutManager(mContext));
        mRvList.setAdapter(adapter);

        mBtnSearch = root.findViewById(R.id.btn_search);
        mBtnCN = root.findViewById(R.id.campus_news);
        mBtnTN = root.findViewById(R.id.teacher_news);
        return root;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mBtnCN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //这个是跳转到LoginActivity的代码，只是用来试一试
                Intent intent = new Intent(getContext(), LoginActivity.class);
                startActivity(intent);
            }
        });
    }
}
