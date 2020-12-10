package com.qq.readbook.weight.page;


import android.content.Context;


import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.Reader;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by zlj
 * 网络页面加载器
 */
public class NetPageLoader extends PageLoader {
    private static final String TAG = "PageFactory";
    private Context mContext;

    public NetPageLoader(PageView pageView, CollBookBean collBook) {
        super(pageView, collBook);
        mContext = pageView.getContext();
    }

    private List<TxtChapter> convertTxtChapter(List<BookChapterBean> bookChapters) {
        List<TxtChapter> txtChapters = new ArrayList<>(bookChapters.size());
        for (BookChapterBean bean : bookChapters) {
            TxtChapter chapter = new TxtChapter();
            chapter.bookId = mCollBook.getBookId();
            chapter.title = bean.getTitle();
            chapter.link = bean.getLink();
            chapter.chapterId = bean.getId();
            txtChapters.add(chapter);
        }
        return txtChapters;
    }

    @Override
    public void refreshChapterList() {
        if (mCollBook.getBookChapters() == null) {
            return;
        }

        // 将 BookChapter 转换成当前可用的 Chapter
        mChapterList = convertTxtChapter(mCollBook.getBookChapters());
        isChapterListPrepare = true;

        // 目录加载完成，执行回调操作。
        if (mPageChangeListener != null) {
            mPageChangeListener.onCategoryFinish(mChapterList);
        }

        // 如果章节未打开
        if (!isChapterOpen()) {
            // 打开章节
            openChapter();
        }
    }

    @Override
    protected BufferedReader getChapterReader(TxtChapter chapter) throws Exception {
        // todo  需要改成用数据库存储
//        File file = new File(Constant.BOOK_CACHE_PATH + mCollBook.getId()
//                + File.separator + chapter.title + ".ipa");
//        if (!file.exists()) {
//            return null;
//        }
//      Reader reader = new FileReader(file);

        Reader  reader=   new StringReader("为了确保庭审顺利，南京中院也做足了准备：除了疫情期间的消杀工作、技术支持外，因为被害人母亲年纪较大，被告人在今年年初因脑梗住院，法医鉴定部门就和120急救中心沟通，庭审期间，救护车在楼下随时待命……为了确保庭审顺利，南京中院也做足了准备：除了疫情期间的消杀工作、技术支持外，因为被害人母亲年纪较大，被告人在今年年初因脑梗住院，法医鉴定部门就和120急救中心沟通，庭审期间，救护车在楼下随时待命……为了确保庭审顺利，南京中院也做足了准备：除了疫情期间的消杀工作、技术支持外，因为被害人母亲年纪较大，被告人在今年年初因脑梗住院，法医鉴定部门就和120急救中心沟通，庭审期间，救护车在楼下随时待命……为了确保庭审顺利，南京中院也做足了准备：除了疫情期间的消杀工作、技术支持外，因为被害人母亲年纪较大，被告人在今年年初因脑梗住院，法医鉴定部门就和120急救中心沟通，庭审期间，救护车在楼下随时待命……为了确保庭审顺利，南京中院也做足了准备：除了疫情期间的消杀工作、技术支持外，因为被害人母亲年纪较大，被告人在今年年初因脑梗住院，法医鉴定部门就和120急救中心沟通，庭审期间，救护车在楼下随时待命……为了确保庭审顺利，南京中院也做足了准备：除了疫情期间的消杀工作、技术支持外，因为被害人母亲年纪较大，被告人在今年年初因脑梗住院，法医鉴定部门就和120急救中心沟通，庭审期间，救护车在楼下随时待命……为了确保庭审顺利，南京中院也做足了准备：除了疫情期间的消杀工作、技术支持外，因为被害人母亲年纪较大，被告人在今年年初因脑梗住院，法医鉴定部门就和120急救中心沟通，庭审期间，救护车在楼下随时待命……为了确保庭审顺利，南京中院也做足了准备：除了疫情期间的消杀工作、技术支持外，因为被害人母亲年纪较大，被告人在今年年初因脑梗住院，法医鉴定部门就和120急救中心沟通，庭审期间，救护车在楼下随时待命……为了确保庭审顺利，南京中院也做足了准备：除了疫情期间的消杀工作、技术支持外，因为被害人母亲年纪较大，被告人在今年年初因脑梗住院，法医鉴定部门就和120急救中心沟通，庭审期间，救护车在楼下随时待命……为了确保庭审顺利，南京中院也做足了准备：除了疫情期间的消杀工作、技术支持外，因为被害人母亲年纪较大，被告人在今年年初因脑梗住院，法医鉴定部门就和120急救中心沟通，庭审期间，救护车在楼下随时待命……为了确保庭审顺利，南京中院也做足了准备：除了疫情期间的消杀工作、技术支持外，因为被害人母亲年纪较大，被告人在今年年初因脑梗住院，法医鉴定部门就和120急救中心沟通，庭审期间，救护车在楼下随时待命……为了确保庭审顺利，南京中院也做足了准备：除了疫情期间的消杀工作、技术支持外，因为被害人母亲年纪较大，被告人在今年年初因脑梗住院，法医鉴定部门就和120急救中心沟通，庭审期间，救护车在楼下随时待命……");

        return new BufferedReader(reader);
    }

    @Override
    protected boolean hasChapterData(TxtChapter chapter) {
        //  return BookManager.isChapterCached(mCollBook.getId(), chapter.title);
        return true;
    }

    // 装载上一章节的内容
    @Override
    boolean parsePrevChapter() {
        boolean isRight = super.parsePrevChapter();

        if (mStatus == STATUS_FINISH) {
            loadPrevChapter();
        } else if (mStatus == STATUS_LOADING) {
            loadCurrentChapter();
        }
        return isRight;
    }

    // 装载当前章内容。
    @Override
    boolean parseCurChapter() {
        boolean isRight = super.parseCurChapter();

        if (mStatus == STATUS_LOADING) {
            loadCurrentChapter();
        }
        return isRight;
    }

    // 装载下一章节的内容
    @Override
    boolean parseNextChapter() {
        boolean isRight = super.parseNextChapter();

        if (mStatus == STATUS_FINISH) {
            loadNextChapter();
        } else if (mStatus == STATUS_LOADING) {
            loadCurrentChapter();
        }

        return isRight;
    }

    /**
     * 加载当前页的前面两个章节
     */
    private void loadPrevChapter() {
        if (mPageChangeListener != null) {
            int end = mCurChapterPos;
            int begin = end - 2;
            if (begin < 0) {
                begin = 0;
            }

            requestChapters(begin, end);
        }
    }

    /**
     * 加载前一页，当前页，后一页。
     */
    private void loadCurrentChapter() {
        if (mPageChangeListener != null) {
            int begin = mCurChapterPos;
            int end = mCurChapterPos;

            // 是否当前不是最后一章
            if (end < mChapterList.size()) {
                end = end + 1;
                if (end >= mChapterList.size()) {
                    end = mChapterList.size() - 1;
                }
            }

            // 如果当前不是第一章
            if (begin != 0) {
                begin = begin - 1;
                if (begin < 0) {
                    begin = 0;
                }
            }

            requestChapters(begin, end);
        }
    }

    /**
     * 加载当前页的后两个章节
     */
    private void loadNextChapter() {
        if (mPageChangeListener != null) {

            // 提示加载后两章
            int begin = mCurChapterPos + 1;
            int end = begin + 1;

            // 判断是否大于最后一章
            if (begin >= mChapterList.size()) {
                // 如果下一章超出目录了，就没有必要加载了
                return;
            }

            if (end > mChapterList.size()) {
                end = mChapterList.size() - 1;
            }

            requestChapters(begin, end);
        }
    }

    private void requestChapters(int start, int end) {
        // 检验输入值
        if (start < 0) {
            start = 0;
        }

        if (end >= mChapterList.size()) {
            end = mChapterList.size() - 1;
        }


        List<TxtChapter> chapters = new ArrayList<>();

        // 过滤，哪些数据已经加载了
        for (int i = start; i <= end; ++i) {
            TxtChapter txtChapter = mChapterList.get(i);
            if (!hasChapterData(txtChapter)) {
                chapters.add(txtChapter);
            }
        }

        if (!chapters.isEmpty()) {
            mPageChangeListener.requestChapters(chapters);
        }
    }

    @Override
    public void saveRecord() {
        super.saveRecord();
        if (mCollBook != null && isChapterListPrepare) {
            //表示当前CollBook已经阅读
            mCollBook.setIsUpdate(false);
            mCollBook.setLastRead(String.valueOf(System.currentTimeMillis()));
            //直接更新
            //  BookRepository.getInstance().saveCollBook(mCollBook);
        }
    }
}

