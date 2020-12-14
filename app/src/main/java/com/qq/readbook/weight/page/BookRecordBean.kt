package com.qq.readbook.weight.page


import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

/**
 * Created by zlj
 */

@Entity
class BookRecordBean : Serializable {

    @PrimaryKey(autoGenerate = true)
    var id = -1

    //所属的书的id
    var bookId: String = ""

    //阅读到了第几章
    var chapter: Int = 0

    //当前的页码
    var pagePos: Int = 0

}
