package com.example.easyaccess.ui.mine;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;

import com.example.easyaccess.LoginActivity;
import com.example.easyaccess.R;

public class MineFragment extends Fragment {

    private MineViewModel mViewModel;
    private ImageView mIblogin;
    private SharedPreferences mSharedPreferences;
    private TextView mTvLogin;
    private ImageButton mBtnSetting;
    private boolean isLogioned;

    public static MineFragment newInstance(){ return new MineFragment(); }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_mine, container, false);
        mIblogin = root.findViewById(R.id.ib_login);
        mTvLogin = root.findViewById(R.id.tv_login);
        return root;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {

        super.onActivityCreated(savedInstanceState);
        mViewModel = ViewModelProviders.of(this).get(MineViewModel.class);
        //TODO: Use the ViewModel
        mIblogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), LoginActivity.class);
                startActivity(intent);
            }
        });
        mSharedPreferences = this.getActivity().getSharedPreferences("user", Context.MODE_PRIVATE);
        initListeners();
    }

    public void initListeners(){
        //already logined
        if(!mSharedPreferences.getString("email", "null").equals("null")){
            isLogioned = true;
            mIblogin.setEnabled(false);
            mTvLogin.setText(mSharedPreferences.getString("email", "null"));
            String username = mSharedPreferences.getString("username", "");
        }
    }
}
