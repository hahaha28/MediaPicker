package `fun`.inaction.mediapicker.core

import android.content.Context
import android.database.Cursor
import android.net.Uri
import android.provider.MediaStore
import android.util.Log

private val TAG = "ImageHelper Debug"
fun log(message: String) {
//    Log.e(TAG,message)
}

class ImageHelper(private val context: Context) {

    /**
     * 所有照片
     */
    val imageList = arrayListOf<Image>()

    /**
     * 所有相册，不包括最近照片相册（因为最近照片相册不算系统相册）
     * key 是相册名
     */
    private val albumMap = mutableMapOf<String, Album>()

    /**
     * 所有相册，不包括最近照片相册。
     * 因为最近照片相册不算系统相册，
     * 要获取最近照片相册，使用 getRecentAlbum(size)
     */
    val albums: List<Album>
        get() {
            val albumList = mutableListOf<Album>()
            for ((key, value) in albumMap) {
                albumList.add(value)
            }
            return albumList
        }

    init {
        context.contentResolver.query(
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
            null,
            null,
            null,
            "${MediaStore.Images.Media.DATE_MODIFIED} DESC"
        )?.use { cursor ->

            while (cursor.moveToNext()) {
                val image = Image(cursor)
                imageList.add(image)
                toAlbum(image)
            }
            cursor.close()
        }
    }

    /**
     * 获取最近照片的相册
     */
    fun getRecentAlbum(size: Int): Album {
        val album = Album("最近照片")
        if (size > imageList.size) {
            album.addImage(imageList)
        } else {
            val images = imageList.subList(0, size - 1)
            album.addImage(images)
        }
        return album
    }

    /**
     * 将照片添加到指定相册
     */
    private fun toAlbum(image: Image) {
        val dirName = image.dirName
        if (albumMap.keys.contains(dirName)) {
            albumMap[dirName]!!.addImage(image)
        } else {
            val album = Album(dirName)
            album.addImage(image)
            albumMap[dirName] = album
        }
    }

    /**
     * 相册类
     * 每个相册都有一个名字和若干照片
     */
    class Album(val name: String) {

        /**
         * 相册中的所有照片
         */
        val imageList = arrayListOf<Image>()

        /**
         * 相册中照片的数量
         */
        val size: Int
            get() = imageList.size

        /**
         * 向相册中添加照片
         */
        fun addImage(image: Image) {
            imageList.add(image)
        }

        /**
         * 向相册中添加照片
         */
        fun addImage(images: Collection<Image>) {
            imageList.addAll(images)
        }
    }

    /**
     * 照片类
     */
    class Image( private val cursor: Cursor) {

        /**
         * 文件名
         */
        val displayName: String

        /**
         * 文件修改时间
         */
        val dateModified: Long

        /**
         * 文件大小
         */
        val size: Long

        /**
         * MIME类型
         */
        val mimeType: String

        /**
         * Uri的id
         */
        val id: String

        /**
         * 所属文件夹的名字
         */
        val dirName: String

        /**
         * 所属文件夹的文字，即相册名，这个值与dirName相同
         */
        val bucketName: String
            get() = dirName

        /**
         * Uri
         */
        val uri: Uri
            get() {
                return Uri.parse("${MediaStore.Images.Media.EXTERNAL_CONTENT_URI}/$id")
            }

        init {

            displayName = cursorResolver(MediaStore.Images.Media.DISPLAY_NAME)
            dateModified = cursorResolver(MediaStore.Images.Media.DATE_MODIFIED).toLong()
            size = cursorResolver(MediaStore.Images.Media.SIZE).toLong()
            mimeType = cursorResolver(MediaStore.Images.Media.MIME_TYPE)
            id = cursorResolver(MediaStore.Images.Media._ID)
            dirName = cursorResolver(MediaStore.Images.Media.BUCKET_DISPLAY_NAME)
        }

        /**
         * 根据指定的列名获取照片的数据
         */
        private fun cursorResolver(columnName:String) = cursor.getString(cursor.getColumnIndex(columnName))

    }
}