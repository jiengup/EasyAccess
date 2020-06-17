package com.example.easyaccess.ui.fragment;

import android.graphics.Rect;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.PopupWindow;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.easyaccess.R;
import com.example.easyaccess.Adapter.DownloadAdapter;
import com.example.easyaccess.Item.DownloadItem;
import com.example.easyaccess.ui.ViweModel.DownloadViewModel;
import com.example.easyaccess.utils.Constants;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class DownloadFragment extends Fragment {

    private RecyclerView mrvmain;
    private TextView mrvscreen;
    private PopupWindow mWindow;
    private RequestQueue requestQueue;
    private DownloadAdapter mAdapter;
    private ArrayList<DownloadItem> downloadItems;
    private String grade, major;
    private RadioGroup radioGroupGrade, radioGroupMajor;
    private DownloadViewModel downloadViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_download, container, false);
        downloadItems = new ArrayList<DownloadItem>();
        requestQueue = Volley.newRequestQueue(getContext());
        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        grade = "null";
        major = "null";
        mrvmain = view.findViewById(R.id.rv_main);
        mrvscreen = view.findViewById(R.id.rv_screen);
        mrvscreen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                View view = getLayoutInflater().inflate(R.layout.layout_download_popup, null);


                Button ok_btn = (Button)view.findViewById(R.id.ok_button);
                Button reset_btn = (Button)view.findViewById(R.id.resetting_button);


                radioGroupMajor = view.findViewById(R.id.rg_major);
                radioGroupGrade = view.findViewById(R.id.rg_grade);
                if(major.equals("DS")){
                    radioGroupMajor.check(R.id.radio_ds);
                }
                else if(major.equals("CS")){
                    radioGroupMajor.check(R.id.radio_cs);
                }
                else if(major.equals("BI")){
                    radioGroupMajor.check(R.id.radio_bi);
                }

                if(grade.equals("2016")){
                    radioGroupGrade.check(R.id.radio_2016);
                }
                else if(grade.equals("2017")){
                    radioGroupGrade.check(R.id.radio_2017);
                }
                else if(grade.equals("2018")){
                    radioGroupGrade.check(R.id.radio_2018);
                }
                else if(grade.equals("2019")){
                    radioGroupGrade.check(R.id.radio_2019);
                }

                int width = getActivity().getWindowManager().getDefaultDisplay().getWidth()/4;
                int height = getActivity().getWindowManager().getDefaultDisplay().getHeight();

                int statusBarHeight = 0;
                int resourceId = getContext().getResources().getIdentifier("status_bar_height", "dimen", "android");
                if (resourceId > 0) {
                    statusBarHeight = getContext().getResources().getDimensionPixelSize(resourceId);
                }

                mWindow = new PopupWindow(view, 3*width, height-statusBarHeight);
                mWindow.setBackgroundDrawable(getResources().getDrawable(android.R.color.transparent));
                mWindow.setOutsideTouchable(true);
                mWindow.setTouchable(true);
                mWindow.setFocusable(true);
                mWindow.update();
                View parent = getLayoutInflater().inflate(R.layout.fragment_download, null);
                mWindow.setAnimationStyle(R.style.popWindow_animation);
                mWindow.showAtLocation(parent, Gravity.RIGHT, 0,statusBarHeight);
                final WindowManager.LayoutParams params = getActivity().getWindow().getAttributes();
                params.alpha = 0.5f;
                getActivity().getWindow().setAttributes(params);
                mWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
                    @Override
                    public void onDismiss() {
                        params.alpha = 1.0f;
                        getActivity().getWindow().setAttributes(params);
                    }
                });
                radioGroupGrade.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(RadioGroup group, int checkedId) {
                        switch(checkedId){
                            case R.id.radio_2016:
                                grade = "2016";
                                break;
                            case R.id.radio_2017:
                                grade = "2017";
                                break;
                            case R.id.radio_2018:
                                grade = "2018";
                                break;
                            case R.id.radio_2019:
                                grade = "2019";
                                break;
                            default:
                                grade = "null";
                                break;
                        }
                    }
                });
                radioGroupMajor.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(RadioGroup group, int checkedId) {
                        switch(checkedId){
                            case R.id.radio_ds:
                                major = "DS";
                                break;
                            case R.id.radio_cs:
                                major = "CS";
                                break;
                            case R.id.radio_bi:
                                major = "BI";
                                break;
                            default:
                                major = "null";
                                break;
                        }
                    }
                });
                ok_btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        getData(grade, major);
                        mAdapter.notifyDataSetChanged();
                        mWindow.dismiss();
                    }
                });
                reset_btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        radioGroupMajor.clearCheck();
                        radioGroupGrade.clearCheck();
                        major = "null";
                        grade = "null";
                        getData(grade, major);
                        mAdapter.notifyDataSetChanged();
                        mWindow.dismiss();
                    }
                });
            }
        });
        getData(grade, major);
        mrvmain.setLayoutManager(new LinearLayoutManager(getActivity()));
        mrvmain.addItemDecoration(new MyDecoration());
        mAdapter = new DownloadAdapter(getActivity(), downloadItems);
        mrvmain.setAdapter(mAdapter);
    }

    private void getData(String grade, String major) {
        downloadItems.clear();
        String url = Constants.DOWNLOAD_URL + "get_resource_list/" + "?grade=" + grade + "&major=" + major;
        try {
            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(url, null, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    try {
                        int code = response.getInt("code");
                        if (code == 0) {
                            JSONArray data = response.getJSONArray("data");
                            //System.out.println(data.length());
                            for (int i = 0; i < data.length(); i++) {
                                DownloadItem downloadItem = new DownloadItem();
                                JSONObject item = data.getJSONObject(i);
                                downloadItem.setName(item.getString("name"));
                                downloadItem.setDesc(item.getString("desc"));
                                downloadItem.setIconUrl(item.getString("icon_url"));
                                downloadItem.setDownloadUrl(item.getString("download_url"));
                                downloadItem.setBelongToCourse(item.getString("belong_to_course"));
                                downloadItems.add(downloadItem);
                            }
                        } else {
                            Toast.makeText(getContext(), "获取资源数据错误", Toast.LENGTH_LONG).show();
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
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public class MyDecoration extends RecyclerView.ItemDecoration {
        @Override
        public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
            super.getItemOffsets(outRect, view, parent, state);
            outRect.set(0, 0, 0, getResources().getDimensionPixelOffset(R.dimen.DividerHeight));
        }
    }
}
