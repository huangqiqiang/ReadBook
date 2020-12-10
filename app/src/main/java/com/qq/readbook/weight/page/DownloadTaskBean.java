package com.qq.readbook.weight.page;


import java.util.ArrayList;
import java.util.List;

/**
 * Created by zlj
 */
public class DownloadTaskBean  {
    public static final int STATUS_LOADING = 1;
    public static final int STATUS_WAIT = 2;
    public static final int STATUS_PAUSE = 3;
    public static final int STATUS_ERROR = 4;
    public static final int STATUS_FINISH = 5;

    //任务名称 -> 名称唯一不重复
    private String taskName;
    //所属的bookId(外健)
    private String bookId;

    private List<BookChapterBean> bookChapterList = new ArrayList<>();
    //章节的下载进度,默认为初始状态
    private int currentChapter = 0;
    //最后的章节
    private int lastChapter = 0;
    //状态:正在下载、下载完成、暂停、等待、下载错误。

    private volatile int status = STATUS_WAIT;
    //总大小 -> (完成之后才会赋值)
    private long size = 0;
    private CollBookBean collBookBean;


    public static int getStatusLoading() {
        return STATUS_LOADING;
    }

    public static int getStatusWait() {
        return STATUS_WAIT;
    }

    public static int getStatusPause() {
        return STATUS_PAUSE;
    }

    public static int getStatusError() {
        return STATUS_ERROR;
    }

    public static int getStatusFinish() {
        return STATUS_FINISH;
    }

    public String getTaskName() {
        return taskName;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

    public String getBookId() {
        return bookId;
    }

    public void setBookId(String bookId) {
        this.bookId = bookId;
    }

    public List<BookChapterBean> getBookChapterList() {
        return bookChapterList;
    }

    public void setBookChapterList(List<BookChapterBean> bookChapterList) {
        this.bookChapterList = bookChapterList;
    }

    public int getCurrentChapter() {
        return currentChapter;
    }

    public void setCurrentChapter(int currentChapter) {
        this.currentChapter = currentChapter;
    }

    public int getLastChapter() {
        return lastChapter;
    }

    public void setLastChapter(int lastChapter) {
        this.lastChapter = lastChapter;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public long getSize() {
        return size;
    }

    public void setSize(long size) {
        this.size = size;
    }

    public CollBookBean getCollBookBean() {
        return collBookBean;
    }

    public void setCollBookBean(CollBookBean collBookBean) {
        this.collBookBean = collBookBean;
    }
}
