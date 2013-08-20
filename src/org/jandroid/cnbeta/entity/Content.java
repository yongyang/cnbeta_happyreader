package org.jandroid.cnbeta.entity;

import java.util.ArrayList;
import java.util.List;

/**
 * @author <a href="mailto:jfox.young@gmail.com">Young Yang</a>
 */
public class Content {

    //阅读次数
    private int viewNum;
    //评论次数
    private int commentNum;
    // 稿源
    private String deliverBy;
    
    //用于发起请求的验证信息
    private String token;
    private String sn;
    //所属分类主题
    private int topic;
    private String topicImage;
    
    //文章中的图片地址列表
    private List<String> images = new ArrayList<String>();

    // 重新 format 之后的 html 内容
    private String content;
    
    private List<Comment> comments = new ArrayList<Comment>();
}
