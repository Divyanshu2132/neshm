package com.androhome.neshm.adpters

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.androhome.neshm.R
import com.androhome.neshm.model.scheduledataModel
import com.github.vipulasri.timelineview.TimelineView
import java.util.*

class ScheduleAdapter(var context: Context, var data: ArrayList<scheduledataModel>) :
    RecyclerView.Adapter<scheduleHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): scheduleHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.schedule_item, parent, false)
        return scheduleHolder(view, viewType)
    }

    override fun onBindViewHolder(holder: scheduleHolder, position: Int) {
        var temptime: String = data.get(position).starttime + "  -  " + data.get(position).endtime
        holder.setDetails(temptime, data.get(position).subcat)
    }

    override fun getItemCount(): Int {
        return data.size
    }

    override fun getItemViewType(position: Int): Int {
        return TimelineView.getTimeLineViewType(position, getItemCount())
    }
}

class scheduleHolder(itemView: View, viewType: Int) : RecyclerView.ViewHolder(itemView) {
    private val time: TextView = itemView.findViewById(R.id.time_text)
    private val subCatText: TextView = itemView.findViewById(R.id.subcat)
    var mTimelineView: TimelineView = itemView.findViewById(R.id.timeline)

    init {
        mTimelineView.initLine(viewType)
    }

    fun setDetails(timeValue: String, subcat: String) {
        subCatText.text = subcat
        time.text = timeValue
        mTimelineView.setMarkerColor(Color.parseColor("#9e9e9e"))
    }
}