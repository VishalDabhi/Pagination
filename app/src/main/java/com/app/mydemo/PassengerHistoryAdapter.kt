package com.app.mydemo

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.app.mydemo.databinding.ItemPastHistoryBinding

class PassengerHistoryAdapter(
    private val context: Context,
    private val pastHistoryList: ArrayList<PassengerHistoryData>
) :
    RecyclerView.Adapter<PassengerHistoryAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding =
            ItemPastHistoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        with(holder.binding)
        {
            with(pastHistoryList[holder.adapterPosition])
            {
                tvId.text = String.format(context.resources.getString(R.string.passenger_id, this._id))
                tvName.text = String.format(context.resources.getString(R.string.passenger_name, this.name))
                tvTrips.text = String.format(context.resources.getString(R.string.trips, this.trips))
            }
        }
    }

    override fun getItemCount(): Int {
        return pastHistoryList.size
    }

    class ViewHolder(val binding: ItemPastHistoryBinding) : RecyclerView.ViewHolder(binding.root) {
    }
}