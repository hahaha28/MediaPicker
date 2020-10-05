package `fun`.inaction.mediapicker.view

import `fun`.inaction.mediapicker.R
import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.annotation.RequiresApi
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.activity_show_image.*

class ShowImageActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_show_image)

        // 隐藏状态栏等
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT) {
            hideSystemUI()
        }

        // 获取图片 Uri
        val uri = intent.getParcelableExtra<Uri>(IMAGE_KEY_URI)

        // 显示图片
        Glide.with(this)
            .load(uri)
            .into(imageView)

    }



    @RequiresApi(Build.VERSION_CODES.KITKAT)
    fun hideSystemUI(){
        window.decorView.systemUiVisibility = (View.SYSTEM_UI_FLAG_IMMERSIVE
                // Set the content to appear under the system bars so that the
                // content doesn't resize when the system bars hide and show.
                or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                // Hide the nav bar and status bar
                or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                or View.SYSTEM_UI_FLAG_FULLSCREEN
                or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION)
    }

    companion object{

        const val IMAGE_KEY_URI = "image_uri"

        fun startActivity(activity: Activity,uri: Uri){
            val intent = Intent(activity,ShowImageActivity::class.java)
            intent.putExtra(IMAGE_KEY_URI,uri)
            activity.startActivity(intent)
        }
    }

}