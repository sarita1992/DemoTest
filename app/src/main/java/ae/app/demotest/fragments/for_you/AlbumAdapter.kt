package ae.app.demotest.fragments.home

import ae.app.demotest.databinding.ItemAlbumBinding
import ae.app.demotest.network.response.AlbumModel
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

/**
Created by Sarita
 **/
interface AlbumAdapterCB {
    fun openAlbum(AlbumId: Int)
}

class AlbumAdapter(private val callback: AlbumAdapterCB, private var list: List<AlbumModel>) : RecyclerView.Adapter<AlbumAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder =
        ViewHolder(
            ItemAlbumBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )

    override fun onBindViewHolder(h: ViewHolder, position: Int) {
        val item = list[position]
        h.bind(item, callback)
    }

    override fun getItemCount(): Int = list.size

    inner class ViewHolder(b: ItemAlbumBinding) :
        RecyclerView.ViewHolder(b.root) {
        val binding: ItemAlbumBinding = b
        fun bind(item: AlbumModel, callback: AlbumAdapterCB) {
            binding.album = item
           binding.executePendingBindings()
        }
    }
}