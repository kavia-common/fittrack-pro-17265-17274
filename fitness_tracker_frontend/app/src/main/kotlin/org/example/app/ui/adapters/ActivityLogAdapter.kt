package org.example.app.ui.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import org.example.app.R
import org.example.app.data.ActivityEntry

class ActivityLogAdapter(private var items: List<ActivityEntry>) :
    RecyclerView.Adapter<ActivityLogAdapter.VH>() {

    fun update(newItems: List<ActivityEntry>) {
        items = newItems
        notifyDataSetChanged()
    }

    class VH(v: View) : RecyclerView.ViewHolder(v) {
        val title: TextView = v.findViewById(R.id.txt_title)
        val subtitle: TextView = v.findViewById(R.id.txt_subtitle)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.item_two_line, parent, false)
        return VH(v)
    }

    override fun getItemCount(): Int = items.size

    override fun onBindViewHolder(holder: VH, position: Int) {
        val e = items[position]
        holder.title.text = e.date
        holder.subtitle.text = holder.itemView.context.getString(R.string.activity_item_subtitle, e.steps, e.distanceKm)
    }
}
