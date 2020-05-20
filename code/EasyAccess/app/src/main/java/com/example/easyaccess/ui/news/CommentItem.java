package com.example.easyaccess.ui.news;

public class CommentItem {
    private String _id;
    private String headUrl;
    private String nickname;
    private String releaseTime;
    private String content;
    private int totalStars;


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
