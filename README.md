# EasyAccess
A software developed for Software Engineer class by a HZAU 
**后端源代码[Easy_access_backend](https://github.com/JiengupXing/EasyAccess_backend)**


## UPDATE

### 2020/05/06

第一次上传了源代码文件，整个应用的框架已经搭建好，包括开发环境的版本

可以下载/code文件夹用Android Studio导入项目打开

通过gradle可以自动同步(sync)开发环境和变量索引等等

**页面请参考\code\app\src\main\res\layout文件夹存放**

**ui类统一放置到\code\app\src\main\java\com\example\easyaccess\ui文件夹下对应的页面包中**

**图标/图片请使用png格式存放到code\app\src\main\res\drawable**

**自己写的工具方法存放到到\code\app\src\main\java\com\example\easyaccess\utils**

已经完成的文件：

- LoginActivity.java
- RegisterActivity.java

### 2020/05/12

完成了几乎全部的xml页面设计

已经在pixel2虚拟机中测试正常

后端已经导入测试数据

将马上开始页面跳转逻辑开发

以实现程序的基本逻辑跳转

已经完成的文件：

- activity_login.xml
- activity_main.xml
- activity_register.xml
- activity_team_pub.xml
- fragment_download.xml
- fragment_mine.xml
- fragment_news.xml
- fragment_team.xml
- layout_download_popup.xml

### 2020/05/17

已经完成了基础页面之间的**全部跳转**

已经将**后端代码框架搭建好**,并且已经实现了**目前用到的所有后端接口**

详情跳转[Easy_access_backend](https://github.com/JiengupXing/EasyAccess_backend)

已经**完成了资讯(news)及其相关功能的开发**

包括：

- 获取资讯列表
- 查看某一条资讯的详细内容
- 对某一条资讯发表评论
- 对某一条资讯点赞
- 跳转到某一条资讯的原文地址
- 对某一条评论点赞
- 。。。。。。

**将很快完成剩余部分功能的开发工作**

已经完成的文件：

- CommentActivity.java
- NewsDetailActivity.java
- mine
  - MineFragment.java
- news
  - CommentAdapter.java
  - CommentItem.java
  - LoadListView.java
  - NewsAdapter.java
  - NewsFragment.java
  - NewsItem.java

目前**正在安排测试计划**，一切工作都在有序进行中~

### 2020/06/17

测试完成，测试报告完成

### 2020/06/17

项目完成，所有文档已经修改完成，准备交付

## TODO

- [x] 页面xml全部设计完成(DeadLIne: 05/08)
- [x] APP的基本跳转和显示逻辑全部实现(DeadLine:05/15)
- [x] 页面和功能细节全部完成，和后端网络通讯打通，第一个发布版本完成(DeadLine:05\22)
- [x] 全面测试修改，完成最终提交的版本(DeadLine:05\25 )
- [x] 课程收尾工作（文档）(DeadLine:05\29)



## PACKAGE

目前已经导入项目的外部包：

- [Volley](https://github.com/google/volley)(配合JSON文件, 用于网络通讯)
