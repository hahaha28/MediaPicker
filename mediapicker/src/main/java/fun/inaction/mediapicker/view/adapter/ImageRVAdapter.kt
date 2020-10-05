package `fun`.inaction.mediapicker.view.adapter

import `fun`.inaction.mediapicker.R
import `fun`.inaction.mediapicker.view.custom.PImageView
import android.net.Uri
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

class ImageRVAdapter : RecyclerView.Adapter<ImageRVAdapter.ViewHolder> {

    var itemList: List<Uri>

    /**
     * 最多可选取几张图片，0代表无限制
     */
    private var limit: Int

    /**
     * 记录选中项的索引
     */
    var selectedItem: MutableList<Int> = mutableListOf()

    /**
     * 图片的点击事件
     */
    var onImageClick: (uri: Uri) -> Unit = {}

    /**
     * 选中图片数量变化的监听
     */
    var onSelectedItemCountChanged: (count: Int)->Unit = {}

    /**
     * @param limit 最多可选取几张图片，0代表无限制
     */
    constructor(list: List<Uri>, limit: Int) : super() {
        itemList = list
        this.limit = limit
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        val imageView: PImageView

        init {
            imageView = view.findViewById(R.id.pImageView)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_image_picker, parent, false)
        val holder = ViewHolder(view)
        // 图片右上角圆圈的点击事件
        holder.imageView.circlePicker.setOnClickListener {
            val p = holder.adapterPosition
            if (p in selectedItem) {
                // 如果是取消选中
                // 移除记录中的选中项
                val index = selectedItem.indexOf(p)
                selectedItem.removeAt(index)
                // 更新之后的选中项
                for (i in index until selectedItem.size) {
                    notifyItemChanged(selectedItem[i])
                }
                holder.imageView.isSelected(false)
                onSelectedItemCountChanged(selectedItem.size)
            } else {
                // 如果是新选中项，检测是否达到上限
                if (limit == 0 || selectedItem.size < limit) {
                    selectedItem.add(p)
                    holder.imageView.isSelected(true, getSelectedNum())
                    onSelectedItemCountChanged(selectedItem.size)
                }
            }
        }
        // 图片的点击事件
        holder.imageView.imageView.setOnClickListener {
            onImageClick(itemList[holder.adapterPosition])
        }
        return holder
    }

    override fun getItemCount(): Int = itemList.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        if (position in selectedItem) {
            holder.imageView.isSelected(true, selectedItem.indexOf(position) + 1)
        } else {
            holder.imageView.isSelected(false)
        }
        Glide.with(holder.itemView)
            .load(itemList[position])
            .into(holder.imageView.imageView)
    }

    /**
     * 返回选中的总数
     */
    fun getSelectedNum(): Int = selectedItem.size


}
