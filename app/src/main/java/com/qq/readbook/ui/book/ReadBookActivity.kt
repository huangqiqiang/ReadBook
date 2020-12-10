package com.qq.readbook.ui.book

import android.app.Activity
import android.content.Intent
import com.hqq.core.ui.base.BaseVmActivity
import com.hqq.core.utils.GsonUtil
import com.qq.readbook.BR
import com.qq.readbook.R
import com.qq.readbook.databinding.ActivityReadBookBinding
import com.qq.readbook.weight.page.BookChapterBean
import com.qq.readbook.weight.page.CollBookBean
import com.qq.readbook.weight.page.PageView

/**
 * @Author : huangqiqiang
 * @Package : com.qq.readbook.ui.book
 * @Date  : 下午 2:25
 * @Email : qiqiang213@gmail.com
 * @Descrive :
 */
class ReadBookActivity : BaseVmActivity<ReadBookViewModel, ActivityReadBookBinding>() {

    companion object {
        fun open(context: Activity) {
            context.startActivityForResult(Intent(context, ReadBookActivity::class.java), -1)
        }
    }


    override val layoutId: Int = R.layout.activity_read_book
    override val bindingViewModelId: Int = BR.vm


    override fun initViews() {

        var str =
            "{\"author\":\"Y.A\",\"bookChapterList\":[],\"bookId\":\"7786\",\"chaptersCount\":0,\"cover\":\"http://novel.duoduvip.com//uploads/20200519/7e267394f67a653da9855fa9d9cee33b.jpg\",\"hasCp\":false,\"include_image\":2,\"isLocal\":false,\"isSelect\":false,\"isUpdate\":true,\"lastChapter\":\"第五话 仙蒂小姐，真的超有女人味！\",\"latelyFollower\":0,\"retentionRatio\":0.0,\"shortIntro\":\"一宫信吾是个平凡的二十五岁上班族，某天早上一觉醒来，却发现自己换了一个截然不同的人生！他置身彷佛欧洲中世纪的魔法异世界，一个下级贵族的家庭里，并转生为五岁男孩。然而他并非从此过著养尊处优的日子，因身为贫穷贵族排行第八的儿子，不但无法继承家门和领地，连吃不吃得饱都成问题，还得学习魔法自力更生才行……\",\"title\":\"八男？别闹了！\",\"updated\":\"1593379378\",\"baseObjId\":0}";

        var mCollBook = GsonUtil.fromJson(str, CollBookBean::class.java)


        mCollBook?.bookChapterList?.add( BookChapterBean("1","aaaaaa"))
        mCollBook?.bookChapterList?.add( BookChapterBean("2","bbbbbbbbbbbbb"))
        mCollBook?.bookChapterList?.add( BookChapterBean("3","ccccccccc"))


        var loader = binding.pageView.getPageLoader(mCollBook)

            binding.pageView.post {
                loader.refreshChapterList()
            }

        binding.pageView.setTouchListener(object : PageView.TouchListener {
            override fun onTouch(): Boolean {
                return true
            }

            override fun center() {
            }

            override fun prePage() {}

            override fun nextPage() {}

            override fun cancel() {}
        })
    }

}