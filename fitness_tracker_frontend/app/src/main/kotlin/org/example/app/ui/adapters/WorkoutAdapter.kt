package org.example.app.ui.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import org.example.app.R
import org.example.app.data.WorkoutEntry

class WorkoutAdapter(private var items: List<WorkoutEntry>) :
    RecyclerView.Adapter<WorkoutAdapter.VH>() {

    fun update(newItems: List<WorkoutEntry>) {
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
        holder.title.text = "${e.type} • ${e.durationMin} min"
        holder.subtitle.text = holder.itemView.context.getString(R.string.workout_item_subtitle, e.calories, e.dateTime)
    }
}
