package com.example.easyaccess.Item;
import com.example.easyaccess.utils.NetworkImageUtils;

import static com.example.easyaccess.utils.NetworkImageUtils.networkImageLoad;
public class NewsItem {
    private String _id;
    private String title;
    private String thumbnail;
    private String originalUrl;
    private String content;
    private String pubTime;
    private int totalStar;
    private String category;
    private String author;
    private String desc;
    private NetworkImageUtils networkImageUtils;
    public String get_id() { return _id; }
    public void set_id(String _id){ this._id = _id; }
    public String getTitle() { return title; }
    public void setTitle(String title){ this.title = title; }
    public String getThumbnail() { return thumbnail; }
    public void setThumbnail(String thumbnail) { this.thumbnail = thumbnail; }
    public String getOriginalUrl() { return originalUrl; }
    public void setOriginalUrl(String originalUrl) { this.originalUrl = originalUrl; }
    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }
    public String getPubTime() { return pubTime; }
    public void setPubTime(String pubTime) { this.pubTime = pubTime; }
    public int getTotalStar() { return totalStar; }
    public void setTotalStar(int totalStar) { this.totalStar = totalStar; }
    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }
    public String getAuthor() { return author; }
    public void setAuthor(String author) { this.author = author; }
    public String getDesc() { return desc; };
    public void setDesc(String desc) { this.desc = desc; }
}
