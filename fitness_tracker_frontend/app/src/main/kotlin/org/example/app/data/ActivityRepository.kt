package org.example.app.data

import android.content.Context
import android.content.SharedPreferences
import org.json.JSONArray
import org.json.JSONObject
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

data class ActivityEntry(val date: String, val steps: Int, val distanceKm: Float)

/**
 * Repository maintaining activity entries in SharedPreferences as JSON.
 */
class ActivityRepository(context: Context) {
    private val prefs: SharedPreferences =
        context.getSharedPreferences("fittrack_activities", Context.MODE_PRIVATE)
    private val dateFmt = SimpleDateFormat("yyyy-MM-dd", Locale.US)

    fun addToday(steps: Int, distanceKm: Float) {
        val today = dateFmt.format(Date())
        val list = getAllMutable()
        val idx = list.indexOfFirst { it.date == today }
        if (idx >= 0) {
            val prev = list[idx]
            list[idx] = prev.copy(steps = prev.steps + steps, distanceKm = prev.distanceKm + distanceKm)
        } else {
            list.add(ActivityEntry(today, steps, distanceKm))
        }
        save(list)
    }

    fun getAll(): List<ActivityEntry> = getAllMutable()

    fun getTodaySteps(): Int {
        val today = dateFmt.format(Date())
        return getAll().firstOrNull { it.date == today }?.steps ?: 0
    }

    fun countActiveDaysThisWeek(): Int {
        val cal = Calendar.getInstance()
        cal.add(Calendar.DAY_OF_YEAR, -6)
        val week = getAll().filter { parse(it.date).after(cal.time) || dateFmt.format(cal.time) == it.date }
        return week.count { it.steps > 0 }
    }

    fun getLast7DaysSteps(): List<Int> {
        val map = getAll().associateBy { it.date }
        val out = mutableListOf<Int>()
        val cal = Calendar.getInstance()
        for (i in 6 downTo 0) {
            val c = Calendar.getInstance()
            c.add(Calendar.DAY_OF_YEAR, -i)
            val key = dateFmt.format(c.time)
            out.add(map[key]?.steps ?: 0)
        }
        return out
    }

    private fun parse(s: String): Date = dateFmt.parse(s)!!

    private fun getAllMutable(): MutableList<ActivityEntry> {
        val raw = prefs.getString("entries", "[]") ?: "[]"
        val arr = JSONArray(raw)
        val list = mutableListOf<ActivityEntry>()
        for (i in 0 until arr.length()) {
            val o = arr.getJSONObject(i)
            list.add(
                ActivityEntry(
                    o.getString("date"),
                    o.getInt("steps"),
                    o.getDouble("distanceKm").toFloat()
                )
            )
        }
        return list
    }

    private fun save(list: List<ActivityEntry>) {
        val arr = JSONArray()
        list.forEach {
            val o = JSONObject()
            o.put("date", it.date)
            o.put("steps", it.steps)
            o.put("distanceKm", it.distanceKm)
            arr.put(o)
        }
        prefs.edit().putString("entries", arr.toString()).apply()
    }
}
