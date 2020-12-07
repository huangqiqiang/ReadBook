package com.qq.readbook.bean;

import androidx.annotation.Nullable;

/**
 * @Author : huangqiqiang
 * @Package : com.qq.readbook
 * @Date : 下午 3:52
 * @Email : qiqiang213@gmail.com
 * @Descrive :
 */
public class Book {
    /**
     * 书名
     */
    private String name;
    /**
     * 书目Url
     */
    private String chapterUrl;
    /**
     * 封面图片url
     */
    private String imgUrl;
    /**
     * 简介
     */
    private String desc;
    /**
     * 作者
     */
    private String author;

    /**
     * 类型
     */
    private String type;
    /**
     * 更新时间
     */
    private String updateDate;
    /**
     * 最新章节id
     */
    @Nullable
    private String newestChapterId;
    /**
     * 最新章节标题
     */
    @Nullable
    private String newestChapterTitle;
    /**
     * 最新章节url
     */
    private String newestChapterUrl;
    /**
     *
     */
    @Nullable
    private String source;


    public String getNewestChapterUrl() {
        return newestChapterUrl;
    }

    public void setNewestChapterUrl(String newestChapterUrl) {
        this.newestChapterUrl = newestChapterUrl;
    }

    @Nullable
    public String getSource() {
        return source;
    }

    public void setSource(@Nullable String source) {
        this.source = source;
    }

    @Nullable
    public String getType() {
        return type;
    }

    public void setType(@Nullable String type) {
        this.type = type;
    }

    public String getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(String updateDate) {
        this.updateDate = updateDate;
    }

    @Nullable
    public String getNewestChapterId() {
        return newestChapterId;
    }

    public void setNewestChapterId(@Nullable String newestChapterId) {
        this.newestChapterId = newestChapterId;
    }

    @Nullable
    public String getNewestChapterTitle() {
        return newestChapterTitle;
    }

    public void setNewestChapterTitle(@Nullable String newestChapterTitle) {
        this.newestChapterTitle = newestChapterTitle;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getChapterUrl() {
        return chapterUrl;
    }

    public void setChapterUrl(String chapterUrl) {
        this.chapterUrl = chapterUrl;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }
}
