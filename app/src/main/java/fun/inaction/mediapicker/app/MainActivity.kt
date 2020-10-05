package `fun`.inaction.mediapicker.app

import `fun`.inaction.mediapicker.MediaPicker
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import `fun`.inaction.mediapicker.core.ImageHelper
import `fun`.inaction.mediapicker.view.ChooseAlbumActivity
import `fun`.inaction.mediapicker.view.ImagePickerActivity
import `fun`.inaction.mediapicker.view.adapter.AlbumRVAdapter
import android.content.Intent
import android.net.Uri
import android.util.Log

import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.yanzhenjie.permission.AndPermission
import com.yanzhenjie.permission.runtime.Permission
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        button.setOnClickListener {
            AndPermission.with(this)
                .runtime()
                .permission(Permission.READ_EXTERNAL_STORAGE)
                .onGranted {
                    MediaPicker.pickImages(this,{boolean,list->
                        if(boolean){
                            list?.forEach {
                                Log.e("MyDebug",it.toString())
                            }
                        }else{
                            Log.e("MyDebug","no return")
                        }
                    })

                }
                .start()
        }


    }
}
