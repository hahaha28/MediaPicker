package `fun`.inaction.mediapicker

import `fun`.inaction.mediapicker.view.ImagePickerActivity
import android.app.Activity
import android.content.Intent
import android.net.Uri
import androidx.fragment.app.Fragment


typealias PickImagesCallback = (Boolean,List<Uri>?)->Unit

class InvisibleFragment: Fragment(){

    /**
     * 打开 ImagePickerActivity 的请求码
     */
    private val PICK_IMAGE = 1

    var callback: PickImagesCallback? = null

    /**
     * 选择图片
     */
    fun pickImage(limit:Int = 0){
        val intent = Intent(context,ImagePickerActivity::class.java)
        intent.putExtra(ImagePickerActivity.INTENT_KEY,limit)
        startActivityForResult(intent,PICK_IMAGE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

            when(requestCode){
                PICK_IMAGE -> {
                    if(resultCode == Activity.RESULT_OK) {
                        data?.getParcelableArrayListExtra<Uri>(
                            ImagePickerActivity.DATA_RETURN
                        )?.let {uriList->
                            callback?.let { it(true,uriList) }
                        }
                    }else{
                        callback?.let { it(false,null) }
                    }
                }
            }

    }
}