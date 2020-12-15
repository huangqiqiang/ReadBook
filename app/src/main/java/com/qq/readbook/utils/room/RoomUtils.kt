package com.qq.readbook.utils.room

import android.app.Application
import androidx.room.*
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.hqq.core.CoreConfig
import com.qq.readbook.bean.Book
import com.qq.readbook.bean.Chapter
import com.qq.readbook.weight.page.BookRecordBean


/**
 * @Author : huangqiqiang
 * @Package : com.qq.readbook.utils.room
 * @Date : 上午 9:36
 * @Email : qiqiang213@gmail.com
 * @Describe :
 */
@Dao
interface BookDao {
    @Query("SELECT * FROM Book")
    fun getAll(): List<Book>



    @Insert
    fun insertAll(vararg book: Book)


}

@Dao
interface ChapterDao {
    @Query("SELECT * FROM Chapter")
    fun getAll(): List<Chapter>

    @Insert
    fun insertAll(vararg chapter: Chapter)

    @Query("delete  FROM Chapter")
    fun deleteAll();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(chapter: ArrayList<Chapter>)

    @Update
    fun update(chapter: Chapter): Int
}

@Dao
interface BookRecordBeanDao {
    @Query("SELECT * FROM BookRecordBean")
    fun getAll(): List<BookRecordBean>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(bookRecordBeanDao: BookRecordBean)

    @Update
    fun update(bookRecordBeanDao: BookRecordBean): Int
}

@Database(entities = [Book::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun bookDao(): BookDao
}

@Database(entities = [Chapter::class, BookRecordBean::class], version = 1, exportSchema = false)
abstract class ChapterDatabase : RoomDatabase() {
    abstract fun chapterDao(): ChapterDao

    abstract fun bookRecordBeanDao(): BookRecordBeanDao
}


/**
 *  1. 一库 多表 表对应书籍
 *  2. 多库 多表   一库对应一本数据
 *  3.  缓存成本地文件 使用文件读取
 */
object RoomUtils {
    fun getDataBase(context: Application, name: String = "books.db"): AppDatabase {
        return Room.databaseBuilder(
            context,
            AppDatabase::class.java, name
        )
            //设置是否允许在主线程做查询操作
            .allowMainThreadQueries()
            //设置数据库升级(迁移)的逻辑
            .addMigrations(MIGRATION_1_2)
            //默认值是FrameworkSQLiteOpenHelperFactory，设置数据库的factory。比如我们想改变数据库的存储路径可以通过这个函数来实现
//                .openHelperFactory {  }
            //设置迁移数据库如果发生错误，将会重新创建数据库，而不是发生崩溃
            .fallbackToDestructiveMigration()
            //监听数据库，创建和打开的操作
//                .addCallback()
            .build()

    }

    fun getChapterDataBase(
        name: String,
        context: Application = CoreConfig.get().application!!

    ): ChapterDatabase {
        return Room.databaseBuilder(
            context,
            ChapterDatabase::class.java, name
        )
            //设置是否允许在主线程做查询操作
            .allowMainThreadQueries()
            //设置数据库升级(迁移)的逻辑
            .addMigrations(MIGRATION_1_2)
            //默认值是FrameworkSQLiteOpenHelperFactory，设置数据库的factory。比如我们想改变数据库的存储路径可以通过这个函数来实现
//                .openHelperFactory {  }
            //设置迁移数据库如果发生错误，将会重新创建数据库，而不是发生崩溃
            .fallbackToDestructiveMigration()
            //监听数据库，创建和打开的操作
//                .addCallback()
            .build()
    }

    fun getUserDao(appDatabase: AppDatabase): BookDao {
        return appDatabase.bookDao()
    }

    /**
     *  数据库版本升级使用的
     */
    val MIGRATION_1_2: Migration = object : Migration(1, 2) {
        override fun migrate(database: SupportSQLiteDatabase) {
        }
    }
}


