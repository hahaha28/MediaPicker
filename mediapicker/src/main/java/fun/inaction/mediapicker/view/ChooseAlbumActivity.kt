package `fun`.inaction.mediapicker.view

import `fun`.inaction.mediapicker.R
import `fun`.inaction.mediapicker.view.adapter.AlbumRVAdapter
import android.app.Activity
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class ChooseAlbumActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_choose_album)

        // 设置 Toolbar
        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
//        supportActionBar?.setDisplayShowTitleEnabled(false)
        toolbar.setNavigationIcon(R.drawable.ic_return)
        toolbar.setNavigationOnClickListener { finish() }


        val list = intent.getParcelableArrayListExtra<AlbumRVAdapter.Album>(DATA_KEY)

        val recyclerView:RecyclerView = findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)
        val adapter = AlbumRVAdapter(list)
        recyclerView.adapter = adapter
        adapter.onClickItem = {
            val intent = Intent()
            intent.putExtra(DATA_KEY,it)
            // 返回点击的索引，key为DATA_KEY
            setResult(Activity.RESULT_OK,intent)
            finish()
        }
    }

    companion object{

        const val DATA_KEY = "album_data"

        fun startActivity(activity: Activity,list: ArrayList<AlbumRVAdapter.Album>,code:Int){
            val intent = Intent(activity,ChooseAlbumActivity::class.java)
            intent.putParcelableArrayListExtra(DATA_KEY,list)
            activity.startActivityForResult(intent,code)
        }
    }

}
