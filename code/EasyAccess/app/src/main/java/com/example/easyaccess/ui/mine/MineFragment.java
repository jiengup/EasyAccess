package com.example.easyaccess.ui.mine;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.easyaccess.LoginActivity;
import com.example.easyaccess.R;
import com.example.easyaccess.utils.Constants;

public class MineFragment extends Fragment {

    private MineViewModel mViewModel;
    private ImageView mIblogin;
    private SharedPreferences mSharedPreferences;
    private SharedPreferences.Editor mEditor;
    private TextView mTvLogin;
    private Button mBtnLogout;
    private ImageButton mBtnSetting;
    private boolean isLogioned;

    public static MineFragment newInstance(){ return new MineFragment(); }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_mine, container, false);
        mIblogin = root.findViewById(R.id.ib_login);
        mTvLogin = root.findViewById(R.id.tv_login);
        mBtnLogout = root.findViewById(R.id.btn_logout);
        return root;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {

        super.onActivityCreated(savedInstanceState);
        mIblogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), LoginActivity.class);
                startActivity(intent);
            }
        });
        mSharedPreferences = this.getActivity().getSharedPreferences("user", Context.MODE_PRIVATE);
        mEditor = mSharedPreferences.edit();
        mBtnLogout.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                mEditor.clear();
                mEditor.apply();
                onResume();
                Intent intent = new Intent(getContext(), LoginActivity.class);
                startActivity(intent);
            }
        });
        initListeners();
    }

    public void initListeners(){
        //already logined
        //System.out.println(mSharedPreferences.getString("email", "null"));
        if(!mSharedPreferences.getString("email", "null").equals("null")){
            isLogioned = true;
            mIblogin.setEnabled(false);
            mBtnLogout.setEnabled(true);
            //mTvLogin.setText(mSharedPreferences.getString("email", "null"));
            String username = mSharedPreferences.getString("username", "");
            String head_portrait_url =mSharedPreferences.getString("headPortrait", "");
            System.out.println(Constants.BASE_URL + head_portrait_url);
            Glide.with(getContext()).load(Constants.BASE_URL + head_portrait_url).into(mIblogin);
            mTvLogin.setText(username);
        }
        else{
            //System.out.println("not login");
            isLogioned = false;
            mIblogin.setEnabled(true);
//            mBtnSetting.setOnClickListener(new View.OnClickListener(){
//
//                @Override
//                public void onClick(View v) {
//                    Intent intent = new Intent(getContext(), LoginActivity.class);
//                    startActivity(intent);
//                }
//            });
        }
    }
    @Override
    public void onResume() {
        super.onResume();
        initListeners();
    }

}
