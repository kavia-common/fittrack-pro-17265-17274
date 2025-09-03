package org.example.app.data

import android.content.Context
import android.content.SharedPreferences
import org.json.JSONArray
import org.json.JSONObject
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

data class WorkoutEntry(val dateTime: String, val type: String, val durationMin: Int, val calories: Int)

/**
 * Repository maintaining workout entries in SharedPreferences as JSON.
 */
class WorkoutRepository(context: Context) {
    private val prefs: SharedPreferences =
        context.getSharedPreferences("fittrack_workouts", Context.MODE_PRIVATE)
    private val dtFmt = SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.US)
    private val dFmt = SimpleDateFormat("yyyy-MM-dd", Locale.US)

    fun add(type: String, durationMin: Int, calories: Int) {
        val list = getAllMutable()
        list.add(0, WorkoutEntry(dtFmt.format(Date()), type, durationMin, calories))
        save(list)
    }

    fun getAll(): List<WorkoutEntry> = getAllMutable()

    fun countThisWeek(): Int {
        val today = dFmt.parse(dFmt.format(Date()))!!
        val millisInDay = 24 * 60 * 60 * 1000L
        val sevenDaysAgo = Date(today.time - 6 * millisInDay)
        return getAll().count { entry ->
            val d = dFmt.parse(entry.dateTime.substring(0, 10))!!
            d.after(sevenDaysAgo) || d == sevenDaysAgo
        }
    }

    private fun getAllMutable(): MutableList<WorkoutEntry> {
        val raw = prefs.getString("entries", "[]") ?: "[]"
        val arr = JSONArray(raw)
        val list = mutableListOf<WorkoutEntry>()
        for (i in 0 until arr.length()) {
            val o = arr.getJSONObject(i)
            list.add(
                WorkoutEntry(
                    o.getString("dateTime"),
                    o.getString("type"),
                    o.getInt("durationMin"),
                    o.getInt("calories")
                )
            )
        }
        return list
    }

    private fun save(list: List<WorkoutEntry>) {
        val arr = JSONArray()
        list.forEach {
            val o = JSONObject()
            o.put("dateTime", it.dateTime)
            o.put("type", it.type)
            o.put("durationMin", it.durationMin)
            o.put("calories", it.calories)
            arr.put(o)
        }
        prefs.edit().putString("entries", arr.toString()).apply()
    }
}
