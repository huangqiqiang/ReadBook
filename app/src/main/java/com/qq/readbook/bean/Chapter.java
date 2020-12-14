package com.qq.readbook.bean;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.Nullable;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

/**
 * @Author : huangqiqiang
 * @Package : com.qq.readbook.bean
 * @Date : 下午 1:42
 * @Email : qiqiang213@gmail.com
 * @Descrive :
 */
@Entity
public class Chapter implements Parcelable {

    @PrimaryKey(autoGenerate = true)
    public int id = 0;
    /**
     * 章节所属书的ID
     */
    private String bookId;
    /**
     * 章节序号
     */
    private int number;
    /**
     * 章节标题
     */
    private String title;
    /**
     * 章节链接
     */
    private String url;
    /**
     * 章节正文
     */
    @Nullable
    private String content;

    /**
     *   创建表
     * @param name
     * @return
     */
    public static String getCreateTableName(String name) {
        return "CREATE TABLE IF NOT EXISTS "
                + name +
                "( id INTEGER PRIMARY KEY AUTOINCREMENT, bookId TEXT, number INTEGER, title TEXT,url TEXT,content TEXT)";
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getBookId() {
        return bookId;
    }

    public void setBookId(String bookId) {
        this.bookId = bookId;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    @Nullable
    public String getContent() {
        return content;
    }

    public void setContent(@Nullable String content) {
        this.content = content;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.id);
        dest.writeString(this.bookId);
        dest.writeInt(this.number);
        dest.writeString(this.title);
        dest.writeString(this.url);
        dest.writeString(this.content);
    }

    public Chapter() {
    }

    protected Chapter(Parcel in) {
        this.id = in.readInt();
        this.bookId = in.readString();
        this.number = in.readInt();
        this.title = in.readString();
        this.url = in.readString();
        this.content = in.readString();
    }

    public static final Parcelable.Creator<Chapter> CREATOR = new Parcelable.Creator<Chapter>() {
        @Override
        public Chapter createFromParcel(Parcel source) {
            return new Chapter(source);
        }

        @Override
        public Chapter[] newArray(int size) {
            return new Chapter[size];
        }
    };
}
