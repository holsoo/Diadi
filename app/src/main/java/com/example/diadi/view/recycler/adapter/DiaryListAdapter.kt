import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.diadi.databinding.ActivityListBinding
import com.example.diadi.domain.PlaceWithDiaries

class DiaryListAdapter : RecyclerView.Adapter<DiaryListAdapter.DiaryViewHolder>() {
    private var placeWithDiaries: List<PlaceWithDiaries> = listOf()

    inner class DiaryViewHolder(private val binding: ActivityListBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(placeWithDiaries: PlaceWithDiaries) {
            // 데이터를 ViewHolder에 바인딩하는 로직 작성
            //binding.recyclerView. = placeWithDiaries.place.placeName 요기 처리 필요. 하나하나 binding
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DiaryViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ActivityListBinding.inflate(inflater, parent, false)
        return DiaryViewHolder(binding)
    }

    override fun onBindViewHolder(holder: DiaryViewHolder, position: Int) {
        val placeWithDiaries = placeWithDiaries[position]
        holder.bind(placeWithDiaries)
    }

    override fun getItemCount(): Int {
        return placeWithDiaries.size
    }

    fun setPlaceWithDiaries(placeWithDiaries: List<PlaceWithDiaries>) {
        this.placeWithDiaries = placeWithDiaries
        notifyDataSetChanged()
    }
}