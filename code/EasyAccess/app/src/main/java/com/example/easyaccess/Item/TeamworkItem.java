package com.example.easyaccess.Item;
import com.example.easyaccess.utils.NetworkImageUtils;

import static com.example.easyaccess.utils.NetworkImageUtils.networkImageLoad;
public class TeamworkItem {
    private String id;
    private String title;
    private NetworkImageUtils networkImageUtils;
    String getId() { return id; }
    void setId(String id) { this.id = id; }
    String getTitle() { return title; }
    void setTitle(String title) { this.title = title; }
}
