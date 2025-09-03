package org.example.app.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.TextView
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import org.example.app.R
import org.example.app.data.ActivityRepository
import org.example.app.data.GoalRepository
import org.example.app.data.WorkoutRepository
import org.example.app.ui.adapters.SimpleCardAdapter
import org.example.app.ui.charts.MiniBarChart

/**
 * PUBLIC_INTERFACE
 * HomeFragment
 *
 * Displays a dashboard: today's activity summary, weekly chart, quick stats, and shortcuts.
 */
class HomeFragment : Fragment() {

    private lateinit var barChart: MiniBarChart
    private lateinit var progressWeekly: ProgressBar
    private lateinit var txtTodaySteps: TextView
    private lateinit var recyclerHighlights: RecyclerView

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return inflater.inflate(R.layout.fragment_home, container, false).apply {
            barChart = findViewById(R.id.mini_bar_chart)
            progressWeekly = findViewById(R.id.progress_weekly)
            txtTodaySteps = findViewById(R.id.txt_today_steps)
            recyclerHighlights = findViewById(R.id.recycler_highlights)
        }
    }

    override fun onResume() {
        super.onResume()
        val actRepo = ActivityRepository(requireContext())
        val workoutRepo = WorkoutRepository(requireContext())
        val goalRepo = GoalRepository(requireContext())

        val todaySteps = actRepo.getTodaySteps()
        txtTodaySteps.text = getString(R.string.today_steps_value, todaySteps)

        val weeklySteps = actRepo.getLast7DaysSteps()
        barChart.setData(weeklySteps)
        val weeklyGoal = goalRepo.getWeeklyStepsGoal()
        if (weeklyGoal > 0) {
            val progress = (weeklySteps.sum().toFloat() / weeklyGoal.toFloat() * 100f).toInt().coerceIn(0, 100)
            progressWeekly.progress = progress
            progressWeekly.isVisible = true
        } else {
            progressWeekly.isVisible = false
        }

        val highlights = listOf(
            getString(R.string.highlight_total_workouts, workoutRepo.countThisWeek()),
            getString(R.string.highlight_active_days, actRepo.countActiveDaysThisWeek()),
            getString(R.string.highlight_current_goal, goalRepo.getActiveGoalSummary())
        )
        recyclerHighlights.adapter = SimpleCardAdapter(highlights)
    }
}
