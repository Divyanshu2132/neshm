package com.androhome.neshm.adpters

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.androhome.neshm.MainActivity
import com.androhome.neshm.ProviderActivity
import com.androhome.neshm.R
import com.androhome.neshm.model.NearbyProviderModel

class NearbyProviderListAdapter(var providerinfo: List<NearbyProviderModel.providerInfo>, var context: Context) :
    RecyclerView.Adapter<NearbyProviderListHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NearbyProviderListHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.provider_list_layout, parent, false)
        return NearbyProviderListHolder(view, context)
    }

    override fun getItemCount(): Int {
        return providerinfo.size
    }

    override fun onBindViewHolder(holder: NearbyProviderListHolder, position: Int) {
        var provider: NearbyProviderModel.providerInfo = providerinfo.get(position)
        holder.setDetails(
            provider.name,
            provider.genre,
            provider.subCategory,
            provider.fees,
            provider.email,
            provider.phoneNumber
        )
    }

}


class NearbyProviderListHolder(itemView: View, var context: Context) : RecyclerView.ViewHolder(itemView) {

    private var name: TextView = itemView.findViewById(R.id.provider_name)
    private var genre: TextView = itemView.findViewById(R.id.provider_genre)
    private var rating: TextView = itemView.findViewById(R.id.rating)
    private var subCategory: TextView = itemView.findViewById(R.id.provider_sub_category)
    private var fees: TextView = itemView.findViewById(R.id.provider_fees)
    private var phonenumber: Long? = 0
    private var email: String? = ""

    init {
        itemView.setOnClickListener(View.OnClickListener {

            var bundle: Bundle = Bundle()
            bundle.putString("name", name.text.toString())
            bundle.putString("genre", genre.text.toString())
            bundle.putString("subCat", subCategory.text.toString())
            bundle.putString("fees", fees.text.toString())
            bundle.putString("email", email)
            bundle.putLong("phoneNumber", phonenumber as Long)
            bundle.putDouble("latitude", MainActivity.latitude)
            bundle.putDouble("longitude", MainActivity.longitude)
            var intent: Intent = Intent(context, ProviderActivity::class.java)
            intent.putExtras(bundle)
            context.startActivity(intent)
        })
    }

    fun setDetails(
        name: String?,
        genre: String?,
        subCategory: String?,
        fees: String?,
        email: String?,
        phoneNumber: Long?
    ) {
        this.name.text = name
        this.genre.text = genre
        if (subCategory != null) {
            this.subCategory.text = subCategory.split(" ")[0]
        }
        this.fees.text = fees
        this.email = email
        this.phonenumber = phoneNumber
    }
}