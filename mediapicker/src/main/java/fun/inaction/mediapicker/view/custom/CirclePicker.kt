package `fun`.inaction.mediapicker.view.custom

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import android.util.AttributeSet
import android.util.Log
import android.view.View

class CirclePicker : View {

    private val paint = Paint()

    /**
     * 是否处于选中状态
     */
    var pick: Boolean = false
        set(value) {
            field = value
            invalidate()
        }

    /**
     * 选中时的颜色
     */
    var pickColor: Int = Color.parseColor("#2196f3")

    /**
     * 未选中时的颜色
     */
    var unPickColor: Int = Color.parseColor("#bdbdbd")

    /**
     * 文字大小
     */
    var textSize: Float = 36f

    /**
     * 显示的数字
     */
    var num = 1

    /**
     * 这个是 onDraw()里用的，写在这里避免不停创建对象
     */
    private val rect = Rect()

    constructor(context: Context?) : this(context, null, 0)
    constructor(context: Context?, attrs: AttributeSet?) : this(context, attrs, 0)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ){
        paint.isAntiAlias = true
        setOnClickListener {
            pick = !pick
            invalidate()
        }
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        // 宽高相等
        super.onMeasure(widthMeasureSpec, widthMeasureSpec)
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)


        // 先画个白圆
        val whiteCircleWidth = width.toFloat()
        paint.color = Color.WHITE
        canvas?.drawCircle(whiteCircleWidth / 2, whiteCircleWidth / 2, whiteCircleWidth / 2, paint)

        if (!pick) {
            // 再画个灰圆
            val grayCircleWidth = whiteCircleWidth * 0.9f
            paint.color = unPickColor
            canvas?.drawCircle(
                whiteCircleWidth / 2,
                whiteCircleWidth / 2,
                grayCircleWidth / 2,
                paint
            )
        } else {
            // 选中状态则先画一个蓝圆
            val blueRadius = whiteCircleWidth * 0.9f / 2
            paint.color = pickColor
            canvas?.drawCircle(
                whiteCircleWidth / 2,
                whiteCircleWidth / 2,
                blueRadius,
                paint
            )
            // 绘制数字
            paint.color = Color.WHITE
            paint.textAlign = Paint.Align.CENTER
            paint.textSize = textSize
            paint.getTextBounds("$num", 0, "$num".length, rect)
            val y = rect.height() * 1.0 / 2 + whiteCircleWidth / 2
//            Log.e("mydebug", "height=" + rect.height().toString())
            canvas?.drawText("$num", whiteCircleWidth / 2, y.toFloat(), paint)
        }


    }


}