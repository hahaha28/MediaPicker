package `fun`.inaction.mediapicker.view

import `fun`.inaction.centertitletoolbar.CenterTitleToolbar
import `fun`.inaction.mediapicker.R
import `fun`.inaction.mediapicker.view.adapter.ImageRVAdapter
import `fun`.inaction.mediapicker.core.ImageHelper
import `fun`.inaction.mediapicker.view.adapter.AlbumRVAdapter
import android.app.Activity
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.widget.TextView
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.activity_image_picker.*
import kotlin.properties.Delegates

class ImagePickerActivity : AppCompatActivity() {

    private val imageHelper: ImageHelper by lazy {
        val imageHelper = ImageHelper(this)
        imageHelper
    }

    private lateinit var adapter: ImageRVAdapter

    /**
     * 限制选取的图片数量
     */
    private var limit = 0

    private lateinit var toolbar:CenterTitleToolbar

    /**
     * 启动选择相册的启动码
     */
    private val CHOOSE_ALBUM = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_image_picker)

        // 设置Toolbar
        toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
//        supportActionBar?.setDisplayShowTitleEnabled(false)
        toolbar.setNavigationIcon(R.drawable.ic_return)
        toolbar.setNavigationOnClickListener { finish() }
        // 设置标题
        toolbar.setCenterTitle("最近")

        // 获取Intent传来的数据，照片选取数量
        limit = intent.getIntExtra(INTENT_KEY,0)

        // 设置完成按钮的显示文字
        setOkButtonText(0,limit)

        // 显示照片

        recyclerView.layoutManager = GridLayoutManager(this, 3)

        val album = imageHelper.getRecentAlbum(100)
        val list = mutableListOf<Uri>()
        album.imageList.forEach {
            list.add(it.uri)
        }

        adapter = ImageRVAdapter(list,limit)
        adapter.onImageClick = onImageClick
        adapter.onSelectedItemCountChanged = onSelectItemCountChanged
        recyclerView.adapter = adapter

    }

    /**
     * 设置完成按钮的显示文字
     */
    private fun setOkButtonText(curSize:Int,limit:Int){
        if(limit != 0){
            ok.text = "完成（$curSize/$limit）"
        }else if(curSize == 0){
            ok.text = "完成"
        }else{
            ok.text = "完成（$curSize）"
        }
    }

    /**
     * 照片的点击事件，跳转到显示照片页面
     */
    private val onImageClick: (uri:Uri)->Unit = {uri ->
        ShowImageActivity.startActivity(this,uri)
    }

    /**
     * 当选取的图片数量变化时的点击事件
     * 即更新完成按钮文字
     */
    private val onSelectItemCountChanged:(count:Int)->Unit = {
        setOkButtonText(it,limit)
    }

    /**
     * 当点击其他相册按钮时
     */
    fun onClickOtherAlbums(v: View) {
        val recentAlbum = imageHelper.getRecentAlbum(100)
        val albumList = ArrayList<AlbumRVAdapter.Album>()
        albumList.add(
            AlbumRVAdapter.Album(
                recentAlbum.name,
                recentAlbum.imageList[0].uri,
                recentAlbum.size
            )
        )
        imageHelper.albums.forEach {
            albumList.add(
                AlbumRVAdapter.Album(
                    it.name,
                    it.imageList[0].uri,
                    it.size
                )
            )
        }
        ChooseAlbumActivity.startActivity(this, albumList, CHOOSE_ALBUM)
    }

    /**
     * 当点击 OK 按钮时，返回选中图片的ArrayList<Uri>
     */
    fun onClickOk(v: View) {
        val uriList = ArrayList<Uri>()
        val dataList = adapter.itemList
        adapter.selectedItem.forEach {
            uriList.add(dataList[it])
        }
        val intent = Intent()
        if(uriList.size == 0){
            setResult(RESULT_CANCELED)
        }else{
            intent.putParcelableArrayListExtra(DATA_RETURN, uriList)
            setResult(Activity.RESULT_OK, intent)
        }
        finish()
    }

    /**
     * 根据索引，显示相册
     * 索引0，代表最近相册
     * 其他索引，根据ImageHelper中的相册排序
     */
    private fun showAlbum(index: Int) {
        val list = mutableListOf<Uri>()
        when (index) {
            0 -> {
                imageHelper
                    .getRecentAlbum(100)
                    .imageList.forEach {
                        list.add(it.uri)
                    }
                toolbar.setCenterTitle("最近")
            }
            else -> {
                imageHelper
                    .albums[index - 1]
                    .imageList.forEach {
                        list.add(it.uri)
                    }
                toolbar.setCenterTitle(imageHelper.albums[index - 1].name)
            }
        }
        // 更新数据
        adapter.itemList = list
        // 清除选中项
        adapter.selectedItem.clear()
        // 通知数据更改
        adapter.notifyDataSetChanged()
        // 将RecyclerView滑动到顶部
        recyclerView.scrollToPosition(0)
    }

    companion object {

        /**
         * 启动这个Activity的Intent的数据的key
         */
        const val INTENT_KEY = "limit"

        // 返回数据的Key
        const val DATA_RETURN = "Image_Picker_Data_Return"

        /**
         * 启动Activity
         * @param limit 选择几张照片，0代表无限制
         */
        fun startActivity(activity: Activity,limit:Int) {
            val intent = Intent(activity, ImagePickerActivity::class.java)
            intent.putExtra(INTENT_KEY,limit)
            activity.startActivity(intent)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            when (requestCode) {
                // 选择相册
                CHOOSE_ALBUM -> {
                    data?.getIntExtra(
                        ChooseAlbumActivity.DATA_KEY,
                        0
                    )?.let { index ->
                        showAlbum(index)
                        setOkButtonText(0,limit)
                    }
                }
            }
        }
    }
}
