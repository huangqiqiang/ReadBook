package com.qq.readbook.bean;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

/**
 * @Author : huangqiqiang
 * @Package : com.qq.readbook.bean
 * @Date : 下午 2:53
 * @Email : qiqiang213@gmail.com
 * @Describe :  书籍资源
 */
@Entity
public class BookSources {
    @PrimaryKey(autoGenerate = true)
    public int id = 0;
    /**
     * 书籍id
     */
    String bookId;

    /**
     * 源名称
     */
    String sourcesName;

    /**
     * 书籍详情URL
     */
    String bookDetailUrl;

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

    public String getSourcesName() {
        return sourcesName;
    }

    public void setSourcesName(String sourcesName) {
        this.sourcesName = sourcesName;
    }

    public String getBookDetailUrl() {
        return bookDetailUrl;
    }

    public void setBookDetailUrl(String bookDetailUrl) {
        this.bookDetailUrl = bookDetailUrl;
    }
}
