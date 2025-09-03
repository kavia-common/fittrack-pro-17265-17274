package org.example.app.data

import android.content.Context
import android.content.SharedPreferences

/**
 * Stores step goals in SharedPreferences.
 */
class GoalRepository(context: Context) {
    private val prefs: SharedPreferences =
        context.getSharedPreferences("fittrack_goals", Context.MODE_PRIVATE)

    fun setDailyStepsGoal(steps: Int) {
        prefs.edit().putInt("daily_steps", steps.coerceAtLeast(0)).apply()
    }

    fun setWeeklyStepsGoal(steps: Int) {
        prefs.edit().putInt("weekly_steps", steps.coerceAtLeast(0)).apply()
    }

    fun getDailyStepsGoal(): Int = prefs.getInt("daily_steps", 8000)
    fun getWeeklyStepsGoal(): Int = prefs.getInt("weekly_steps", 56000)

    fun getActiveGoalSummary(): String {
        val d = getDailyStepsGoal()
        val w = getWeeklyStepsGoal()
        return "Daily $d • Weekly $w"
    }
}
