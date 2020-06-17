package com.example.easyaccess.Item;

import com.example.easyaccess.utils.NetworkImageUtils;

import static com.example.easyaccess.utils.NetworkImageUtils.networkImageLoad;

public class CommentItem {
    private String _id;
    private String headUrl;
    private String nickname;
    private String releaseTime;
    private String content;
    private int totalStars;
    private NetworkImageUtils networkImageUtils;

    public String get_id() { return _id; }
    public void set_id(String _id) { this._id = _id; }
    public String getHeadUrl() { return headUrl; }
    public void setHeadUrl(String headUrl) { this.headUrl = headUrl; }
    public String getNickname() { return nickname; }
    public void setNickname(String nickname) { this.nickname = nickname; }
    public String getReleaseTime() { return releaseTime; };
    public void setReleaseTime(String releaseTime) { this.releaseTime = releaseTime; }
    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }
    public int getTotalStars() { return totalStars; }
    public void setTotalStars(int totalStars) { this.totalStars = totalStars; }
}
