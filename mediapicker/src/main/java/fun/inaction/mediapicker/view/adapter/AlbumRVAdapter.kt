package `fun`.inaction.mediapicker.view.adapter

import `fun`.inaction.mediapicker.R
import `fun`.inaction.mediapicker.core.ImageHelper
import android.net.Uri
import android.os.Parcelable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.widget.AppCompatImageView
import androidx.core.widget.ImageViewCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import kotlinx.android.parcel.Parcelize

class AlbumRVAdapter: RecyclerView.Adapter<AlbumRVAdapter.ViewHolder> {

    val itemList: List<Album>

    var onClickItem: ((position:Int)->Unit)? = null

    constructor(list:List<Album>):super(){
        itemList = list
    }

    class ViewHolder(val view:View): RecyclerView.ViewHolder(view){
        val imageView :AppCompatImageView
        val titleTextView: TextView
        val countTextView: TextView
        init {
            imageView = view.findViewById(R.id.image)
            titleTextView = view.findViewById(R.id.title)
            countTextView = view.findViewById(R.id.count)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_album,parent,false)
        val holder = ViewHolder(view)

        holder.view.setOnClickListener {
            onClickItem?.let { it(holder.adapterPosition) }
        }

        return holder
    }

    override fun getItemCount(): Int = itemList.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val album = itemList[position]
        with(holder){
            Glide.with(imageView)
                .load(album.profileUri)
                .into(imageView)
            titleTextView.text = album.name
            countTextView.text = "共${album.count}张"
        }
    }

    @Parcelize
    class Album(val name:String,val profileUri:Uri,val count:Int):Parcelable
}