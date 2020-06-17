package com.example.easyaccess.Item;

import com.example.easyaccess.utils.NetworkImageUtils;

import static com.example.easyaccess.utils.NetworkImageUtils.networkImageLoad;

public class TagItem {
    private String name;
    private String id;
    private NetworkImageUtils networkImageUtils;
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
}
