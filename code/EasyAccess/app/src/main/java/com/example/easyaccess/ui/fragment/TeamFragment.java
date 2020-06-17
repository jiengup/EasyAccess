package com.example.easyaccess.ui.fragment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Rect;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.easyaccess.Activity.LoginActivity;
import com.example.easyaccess.Activity.PublishActivity;
import com.example.easyaccess.R;
import com.example.easyaccess.Activity.TeamworkDetailActivity;
import com.example.easyaccess.Item.TagItem;
import com.example.easyaccess.ui.ViweModel.TeamViewModel;
import com.example.easyaccess.Adapter.TeamworkAdapter;
import com.example.easyaccess.Item.TeamworkItem;
import com.example.easyaccess.utils.Constants;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.zhy.view.flowlayout.FlowLayout;
import com.zhy.view.flowlayout.TagAdapter;
import com.zhy.view.flowlayout.TagFlowLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.concurrent.CopyOnWriteArrayList;

public class TeamFragment extends Fragment {
    public static CopyOnWriteArrayList<TagItem> list_tag, list_teacher;
    private ArrayList<TeamworkItem> teamworkItems;
    private ArrayList<String> tags, teachers;
    private RecyclerView mrvmain;
    private TeamworkAdapter mAdapter;
    private TeamViewModel notificationsViewModel;
    private TagFlowLayout tagFlowLayout_tag, tagFlowLayout_teacher;
    private RequestQueue requestQueue;
    private FloatingActionButton fab;
    private boolean isLogin;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_team, container, false);
        tagFlowLayout_tag = root.findViewById(R.id.id_flowlayout_tag);
        tagFlowLayout_teacher = root.findViewById(R.id.id_flowlayout_teacher);
        fab = root.findViewById(R.id.fab);
        list_tag = new CopyOnWriteArrayList<TagItem>();
        list_teacher = new CopyOnWriteArrayList<TagItem>();
        tags = new ArrayList<String>();
        teachers = new ArrayList<String>();
        teamworkItems = new ArrayList<TeamworkItem>();
        requestQueue = Volley.newRequestQueue(getContext());
        SharedPreferences msp = getActivity().getSharedPreferences("user", Context.MODE_PRIVATE);
        if(!msp.getString("email", "null").equals("null")){
            isLogin = true;
        }
        else{
            isLogin = false;
        }
        return root;
    }


    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mrvmain = view.findViewById(R.id.rv_main);
        obtainData("get_tag_list", new VolleyCallback() {
            @Override
            public void onSuccessResponse(String result) {
                try{
                    JSONObject response = new JSONObject(result);
                    int code = response.getInt("code");
                    if(code == 0){
                        JSONArray data = response.getJSONArray("data");
                        for(int i=0; i<data.length(); i++){
                            JSONObject item = data.getJSONObject(i);
                            TagItem tagItem = new TagItem();
                            tagItem.setName(item.getString("name"));
                            tagItem.setId(item.getString("id"));
                            list_tag.add(tagItem);
                            //System.out.println(list_tag);
                        }
                    }
                    else{
                        Toast.makeText(getContext(), "获取资源数据错误", Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    Toast.makeText(getContext(), "网络错误", Toast.LENGTH_LONG).show();
                    e.printStackTrace();
                }
                InitTagView();
            }
        });
        obtainData("get_teacher_list", new VolleyCallback() {
            @Override
            public void onSuccessResponse(String result) {
                try{
                    JSONObject response = new JSONObject(result);
                    int code = response.getInt("code");
                    if(code == 0){
                        JSONArray data = response.getJSONArray("data");
                        for(int i=0; i<data.length(); i++){
                            JSONObject item = data.getJSONObject(i);
                            TagItem tagItem = new TagItem();
                            tagItem.setName(item.getString("name"));
                            tagItem.setId(item.getString("id"));
                            list_teacher.add(tagItem);
                            //System.out.println(list_tag);
                        }
                    }
                    else{
                        Toast.makeText(getContext(), "获取资源数据错误", Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    Toast.makeText(getContext(), "网路错误", Toast.LENGTH_LONG).show();
                    e.printStackTrace();
                }
                InitTeacherView();
            }
        });
        pullData(new VolleyCallback() {
            @Override
            public void onSuccessResponse(String result) {
                try{
                    JSONObject response = new JSONObject(result);
                    int code = response.getInt("code");
                    if(code == 0){
                        JSONArray data = response.getJSONArray("data");
                        for(int i=0; i<data.length(); i++){
                            JSONObject item = data.getJSONObject(i);
                            TeamworkItem teamworkItem = new TeamworkItem();
                            teamworkItem.setId(item.getString("id"));
                            teamworkItem.setTitle(item.getString("title"));
                            teamworkItems.add(teamworkItem);
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                mAdapter.notifyDataSetChanged();
            }
        });
        mrvmain.setLayoutManager(new LinearLayoutManager(getActivity()));
        mrvmain.addItemDecoration(new MyDecoration());
        mAdapter = new TeamworkAdapter(getActivity(), teamworkItems);
        mrvmain.setAdapter(mAdapter);
        mAdapter.setOnItemClickListener(new TeamworkAdapter.OnItemClickListener() {
            @Override
            public void onClick(int position) {
                Intent intent = new Intent();
                intent.putExtra("teamwork_id", teamworkItems.get(position).getId());
                intent.setClass(getContext(), TeamworkDetailActivity.class);
                startActivity(intent);
            }
        });
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!isLogin){
                    Toast.makeText(getContext(), "请先登陆", Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(getContext(), LoginActivity.class);
                    startActivity(intent);
                }
                else{
                    Intent intent = new Intent(getContext(), PublishActivity.class);
                    startActivity(intent);
                }
            }
        });
    }

    private void InitTagView() {
        final LayoutInflater layoutInflater = LayoutInflater.from(getContext());
        tagFlowLayout_tag.setAdapter(new TagAdapter<TagItem>(list_tag) {
            @Override
            public void onSelected(int position, View view){
                super.onSelected(position, view);
                tags.add(list_tag.get(position).getId());
                pullData(new VolleyCallback() {
                    @Override
                    public void onSuccessResponse(String result) {
                        teamworkItems.clear();
                        try{
                            JSONObject response = new JSONObject(result);
                            int code = response.getInt("code");
                            if(code == 0){
                                JSONArray data = response.getJSONArray("data");
                                for(int i=0; i<data.length(); i++){
                                    JSONObject item = data.getJSONObject(i);
                                    TeamworkItem teamworkItem = new TeamworkItem();
                                    teamworkItem.setId(item.getString("id"));
                                    teamworkItem.setTitle(item.getString("title"));
                                    teamworkItems.add(teamworkItem);
                                }
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        mAdapter.notifyDataSetChanged();
                    }
                });
            }
            @Override
            public void unSelected(int position, View view){
                super.unSelected(position, view);
                tags.remove(list_tag.get(position).getId());
                pullData(new VolleyCallback() {
                    @Override
                    public void onSuccessResponse(String result) {
                        teamworkItems.clear();
                        try{
                            JSONObject response = new JSONObject(result);
                            int code = response.getInt("code");
                            if(code == 0){
                                JSONArray data = response.getJSONArray("data");
                                for(int i=0; i<data.length(); i++){
                                    JSONObject item = data.getJSONObject(i);
                                    TeamworkItem teamworkItem = new TeamworkItem();
                                    teamworkItem.setId(item.getString("id"));
                                    teamworkItem.setTitle(item.getString("title"));
                                    teamworkItems.add(teamworkItem);
                                }
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        mAdapter.notifyDataSetChanged();
                    }
                });
            }
            @Override
            public View getView(FlowLayout parent, int position, TagItem tagItem) {
                TextView tv = (TextView) layoutInflater.inflate(R.layout.flow_tv,
                        tagFlowLayout_tag, false);
                tv.setText(tagItem.getName());
                return tv;
            }
        });

//        tagFlowLayout_tag.setOnTagClickListener(new TagFlowLayout.OnTagClickListener() {
//            @Override
//            public boolean onTagClick(View view, int position, FlowLayout parent) {
//                //得到点击的值
//                Toast.makeText(getContext(), list_tag.get(position).getName(), Toast.LENGTH_SHORT).show();
//                return true;
//            }
//        });
    }

    private void InitTeacherView() {
        final LayoutInflater layoutInflater = LayoutInflater.from(getContext());
        tagFlowLayout_teacher.setAdapter(new TagAdapter<TagItem>(list_teacher) {
            @Override
            public void onSelected(int position, View view){
                super.onSelected(position, view);
                teachers.add(list_teacher.get(position).getId());
                pullData(new VolleyCallback() {
                    @Override
                    public void onSuccessResponse(String result) {
                        teamworkItems.clear();
                        try{
                            JSONObject response = new JSONObject(result);
                            int code = response.getInt("code");
                            if(code == 0){
                                JSONArray data = response.getJSONArray("data");
                                for(int i=0; i<data.length(); i++){
                                    JSONObject item = data.getJSONObject(i);
                                    TeamworkItem teamworkItem = new TeamworkItem();
                                    teamworkItem.setId(item.getString("id"));
                                    teamworkItem.setTitle(item.getString("title"));
                                    teamworkItems.add(teamworkItem);
                                }
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        mAdapter.notifyDataSetChanged();
                    }
                });
            }
            @Override
            public void unSelected(int position, View view){
                super.unSelected(position, view);
                teachers.remove(list_teacher.get(position).getId());
                pullData(new VolleyCallback() {
                    @Override
                    public void onSuccessResponse(String result) {
                        teamworkItems.clear();
                        try{
                            JSONObject response = new JSONObject(result);
                            int code = response.getInt("code");
                            if(code == 0){
                                JSONArray data = response.getJSONArray("data");
                                for(int i=0; i<data.length(); i++){
                                    JSONObject item = data.getJSONObject(i);
                                    TeamworkItem teamworkItem = new TeamworkItem();
                                    teamworkItem.setId(item.getString("id"));
                                    teamworkItem.setTitle(item.getString("title"));
                                    teamworkItems.add(teamworkItem);
                                }
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        mAdapter.notifyDataSetChanged();
                    }
                });
            }
            @Override
            public View getView(FlowLayout parent, int position, TagItem tagItem) {
                TextView tv = (TextView)layoutInflater.inflate(R.layout.flow_tv,
                        tagFlowLayout_teacher, false);
                tv.setText(tagItem.getName());
                return tv;
            }
        });
//        tagFlowLayout_teacher.setOnTagClickListener(new TagFlowLayout.OnTagClickListener() {
//            @Override
//            public boolean onTagClick(View view, int position, FlowLayout parent) {
//                Toast.makeText(getContext(), list_teacher.get(position).getName(), Toast.LENGTH_SHORT).show();
//                return true;
//            }
//        });
    }


    private void obtainData(String tailUrl, final VolleyCallback callback){
        String url = Constants.TEAMWORK_URL + tailUrl;
        try{
            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(url, null, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    callback.onSuccessResponse(response.toString());
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


    public void pullData(final VolleyCallback callback){
        String url = Constants.TEAMWORK_URL + "get_wanted_list";
        try{
            JSONObject jsonBody = new JSONObject();
            JSONArray jsonTags = new JSONArray(tags);
            JSONArray jsonTeachers = new JSONArray(teachers);

            jsonBody.put("tags", jsonTags);
            jsonBody.put("teachers", jsonTeachers);

            final JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, jsonBody, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    callback.onSuccessResponse(response.toString());
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(getContext(), "Error" + error.toString(), Toast.LENGTH_LONG).show();
                }
            });
            requestQueue.add(jsonObjectRequest);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    public interface VolleyCallback {
        void onSuccessResponse(String result);
    }
    public class MyDecoration extends RecyclerView.ItemDecoration {
        @Override
        public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
            super.getItemOffsets(outRect, view, parent, state);
            outRect.set(0, 0, 0, 5);
        }
    }
    @Override
    public void onResume() {
        super.onResume();
        pullData(new VolleyCallback() {
            @Override
            public void onSuccessResponse(String result) {
                teamworkItems.clear();
                try{
                    JSONObject response = new JSONObject(result);
                    int code = response.getInt("code");
                    if(code == 0){
                        JSONArray data = response.getJSONArray("data");
                        for(int i=0; i<data.length(); i++){
                            JSONObject item = data.getJSONObject(i);
                            TeamworkItem teamworkItem = new TeamworkItem();
                            teamworkItem.setId(item.getString("id"));
                            teamworkItem.setTitle(item.getString("title"));
                            teamworkItems.add(teamworkItem);
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                mAdapter.notifyDataSetChanged();
            }
        });
    }
}

