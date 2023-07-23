import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.monkhood_assessment.ProfileData
import com.example.monkhood_assessment.R
import com.example.monkhood_assessment.databinding.ItemProfileBinding

class ProfileDataAdapter(private val profileDataList: List<ProfileData>) :
    RecyclerView.Adapter<ProfileDataAdapter.ProfileViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProfileViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_profile, parent, false)
        return ProfileViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ProfileViewHolder, position: Int) {
        val profileData = profileDataList[position]
        holder.bind(profileData)
    }

    override fun getItemCount(): Int {
        return profileDataList.size
    }

    class ProfileViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(profileData: ProfileData) {
            // Bind the data to the views in the item layout using ViewBinding
            val binding = ItemProfileBinding.bind(itemView)
            binding.textViewName.text = profileData.name
            binding.textViewPhoneNumber.text = profileData.phoneNumber
            binding.textViewEmail.text = profileData.email
            binding.textViewDateOfBirth.text = profileData.dateOfBirth
        }
    }
}
