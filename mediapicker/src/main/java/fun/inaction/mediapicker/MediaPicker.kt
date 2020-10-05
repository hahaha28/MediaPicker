package `fun`.inaction.mediapicker

import android.net.Uri
import androidx.fragment.app.FragmentActivity

object MediaPicker {

    private const val TAG = "InvisibleFragment"

    /**
     * 选择照片
     * @param callback 这个回调有两个参数，第一个表示是否成功，第二个是照片的List<Uri>
     * @param limit 限制最多选择几张照片
     */
    fun pickImages(activity: FragmentActivity,callback:PickImagesCallback,limit:Int = 0){
        val fragmentManager = activity.supportFragmentManager
        val existedFragment = fragmentManager.findFragmentByTag(TAG)
        val fragment = if(existedFragment != null){
            existedFragment as InvisibleFragment
        }else {
            val invisibleFragment = InvisibleFragment()
            fragmentManager.beginTransaction().add(invisibleFragment,TAG).commitNow()
            invisibleFragment
        }

        fragment.callback = callback
        fragment.pickImage(limit)
    }

    /**
     * 选择照片
     * @param callback 这个回调有两个参数，第一个表示是否成功，第二个是照片的List<Uri>
     */
    fun pickImages(activity: FragmentActivity,callback:PickImagesCallback){
        val fragmentManager = activity.supportFragmentManager
        val existedFragment = fragmentManager.findFragmentByTag(TAG)
        val fragment = if(existedFragment != null){
            existedFragment as InvisibleFragment
        }else {
            val invisibleFragment = InvisibleFragment()
            fragmentManager.beginTransaction().add(invisibleFragment,TAG).commitNow()
            invisibleFragment
        }

        fragment.callback = callback
        fragment.pickImage()
    }

    /**
     * 选择照片
     * 专门为Java调用写的，如果使用Kotlin，请用另一个同名方法
     * @param limit 限制最多选取几张照片，0代表无限制
     * @param callback 这个回调有两个参数，第一个表示是否成功，第二个是照片的List<Uri>
     */
    @JvmStatic
    fun pickImages(activity: FragmentActivity,limit:Int,callback: PickCallback){
        pickImages(activity,{success,uriList ->
            callback.onResult(success,uriList)
        },limit)
    }

    /**
     * 选择照片
     * 专门为Java调用写的，如果使用Kotlin，请用另一个同名方法
     * @param callback 这个回调有两个参数，第一个表示是否成功，第二个是照片的List<Uri>
     */
    @JvmStatic
    fun pickImages(activity: FragmentActivity,callback: PickCallback){
        pickImages(activity) { success, uriList ->
            callback.onResult(success, uriList)
        }
    }

    /**
     * 选择照片的回调
     */
    interface PickCallback{
        fun onResult(success:Boolean,uriList: List<Uri>?)
    }

}