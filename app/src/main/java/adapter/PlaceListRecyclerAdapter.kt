package network.adapter

import activities.PlaceDetailActivity
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView.Adapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.daehankang.myandroidapplication.data.Place
import com.daehankang.myandroidapplication.databinding.RecyclerItemListFragmentBinding
import com.google.gson.Gson

class PlaceListRecyclerAdapter(val context: Context, val document : List<Place>) : Adapter<PlaceListRecyclerAdapter.VH>() {

    inner class VH(val binding: RecyclerItemListFragmentBinding) : ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        val layoutInflater = LayoutInflater.from(context)
        val binding = RecyclerItemListFragmentBinding.inflate(layoutInflater, parent, false)
        return VH(binding)
    }

    override fun getItemCount(): Int {
        return document.size
    }

    override fun onBindViewHolder(holder: VH, position: Int) {
        val place: Place = document[position]

        holder.binding.tvPlaceName.text = place.place_name
        holder.binding.tvAddress.text = if (place.road_address_name == "") place.address_name else place.road_address_name
        holder.binding.tvDistance.text = "${place.distance}m"

        holder.binding.root.setOnClickListener {
            val intent = Intent(context, PlaceDetailActivity::class.java)

            // 장소정보에 대한 데이터를 추가로 보내기 [ 객체는 추가데이터로 전송불가 --> json문자열로 변환 ]
            val gson = Gson()
            val s: String = gson.toJson(place) // 객체 --> json stirng
            intent.putExtra("place", s)

            context.startActivity(intent)
        }
    }
}