package com.qq.readbook.bean;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.Nullable;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author : huangqiqiang
 * @Package : com.qq.readbook
 * @Date : 下午 3:52
 * @Email : qiqiang213@gmail.com
 * @Describe :
 */
@Entity
public class Book implements Parcelable {
    @PrimaryKey(autoGenerate = true)
    public int id = 0;
    /**
     * 书籍id
     */
    private String bookId;

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
     * 资源
     */
    @Nullable
    private String source;

    /**
     * 最新阅读日期
     */
    String lastRead = "";
    /**
     * 是否更新或未阅读
     */
    boolean isUpdate = true;

    /**
     * 章节列表
     */
    @Ignore
    List<Chapter> bookChapterList = new ArrayList();


    public List<Chapter> getBookChapterList() {
        if (bookChapterList == null) {
            return new ArrayList<>();
        }
        return bookChapterList;
    }

    public void setBookChapterList(List<Chapter> bookChapterList) {
        this.bookChapterList = bookChapterList;
    }

    public String getLastRead() {
        return lastRead == null ? "" : lastRead;
    }

    public void setLastRead(String lastRead) {
        this.lastRead = lastRead;
    }

    public boolean isUpdate() {
        return isUpdate;
    }

    public void setUpdate(boolean update) {
        isUpdate = update;
    }

    public String getBookId() {
        return bookId == null ? "" : bookId;
    }

    public void setBookId(String bookId) {
        this.bookId = bookId;
    }

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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.name);
        dest.writeString(this.chapterUrl);
        dest.writeString(this.imgUrl);
        dest.writeString(this.desc);
        dest.writeString(this.author);
        dest.writeString(this.type);
        dest.writeString(this.updateDate);
        dest.writeString(this.newestChapterId);
        dest.writeString(this.newestChapterTitle);
        dest.writeString(this.newestChapterUrl);
        dest.writeString(this.source);
    }

    public Book() {
    }

    protected Book(Parcel in) {
        this.name = in.readString();
        this.chapterUrl = in.readString();
        this.imgUrl = in.readString();
        this.desc = in.readString();
        this.author = in.readString();
        this.type = in.readString();
        this.updateDate = in.readString();
        this.newestChapterId = in.readString();
        this.newestChapterTitle = in.readString();
        this.newestChapterUrl = in.readString();
        this.source = in.readString();
    }

    public static final Parcelable.Creator<Book> CREATOR = new Parcelable.Creator<Book>() {
        @Override
        public Book createFromParcel(Parcel source) {
            return new Book(source);
        }

        @Override
        public Book[] newArray(int size) {
            return new Book[size];
        }
    };
}
