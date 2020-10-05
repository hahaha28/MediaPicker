package `fun`.inaction.mediapicker.view.custom

import `fun`.inaction.mediapicker.R
import android.content.Context
import android.graphics.*
import android.graphics.drawable.Drawable
import android.net.Uri
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.FrameLayout


class PImageView : FrameLayout {

    var imageView: SquareImageView
    var circlePicker: CirclePicker


    /**
     * 选中时的回调，返回值为选中的第几张图片
     */
    var onSelected: (() -> Int)? = null

    constructor(context: Context) : this(context,null)
    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs,0)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        LayoutInflater.from(context)
            .inflate(R.layout.image_picker, this)

        imageView = findViewById(R.id.SquareImageView)
        circlePicker = findViewById(R.id.circlePicker)


    }

    /**
     * 设置是否处于选中状态
     * 如果选中，是第几项
     */
    fun isSelected(b: Boolean, num:Int = 0){
        if(b){
            // 如果是选中状态
            circlePicker.pick = true
            circlePicker.num = num
            imageView.colorFilter = PorterDuffColorFilter(
                Color.rgb(123,123,123),
                PorterDuff.Mode.MULTIPLY
            )
        }else{
            // 如果是非选中状态
            circlePicker.pick = false
            imageView.clearColorFilter()
        }
    }

    fun setImageURI(uri: Uri) = imageView.setImageURI(uri)

    fun setImageBitmap(bitmap: Bitmap) = imageView.setImageBitmap(bitmap)

    fun setImageDrawable(drawable: Drawable) = imageView.setImageDrawable(drawable)

}