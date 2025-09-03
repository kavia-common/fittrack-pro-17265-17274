package org.example.app.ui

import android.os.Bundle
import android.view.*
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.TextView
import androidx.fragment.app.Fragment
import org.example.app.R
import org.example.app.data.ActivityRepository
import org.example.app.data.GoalRepository

/**
 * PUBLIC_INTERFACE
 * GoalsFragment
 *
 * Allows user to set and track daily and weekly steps goals.
 */
class GoalsFragment : Fragment() {

    private lateinit var goalRepo: GoalRepository
    private lateinit var activityRepo: ActivityRepository

    private lateinit var inputDaily: EditText
    private lateinit var inputWeekly: EditText
    private lateinit var progressDaily: ProgressBar
    private lateinit var progressWeekly: ProgressBar
    private lateinit var txtDaily: TextView
    private lateinit var txtWeekly: TextView
    private lateinit var btnSave: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        goalRepo = GoalRepository(requireContext())
        activityRepo = ActivityRepository(requireContext())
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val v = inflater.inflate(R.layout.fragment_goals, container, false)
        inputDaily = v.findViewById(R.id.input_goal_daily)
        inputWeekly = v.findViewById(R.id.input_goal_weekly)
        progressDaily = v.findViewById(R.id.progress_daily)
        progressWeekly = v.findViewById(R.id.progress_weekly)
        txtDaily = v.findViewById(R.id.txt_goal_daily)
        txtWeekly = v.findViewById(R.id.txt_goal_weekly)
        btnSave = v.findViewById(R.id.btn_save_goals)

        btnSave.setOnClickListener {
            val d = inputDaily.text.toString().toIntOrNull() ?: 0
            val w = inputWeekly.text.toString().toIntOrNull() ?: 0
            goalRepo.setDailyStepsGoal(d)
            goalRepo.setWeeklyStepsGoal(w)
            bind()
        }
        return v
    }

    override fun onResume() {
        super.onResume()
        bind()
    }

    private fun bind() {
        val dGoal = goalRepo.getDailyStepsGoal()
        val wGoal = goalRepo.getWeeklyStepsGoal()

        inputDaily.setText(if (dGoal > 0) dGoal.toString() else "")
        inputWeekly.setText(if (wGoal > 0) wGoal.toString() else "")

        val today = activityRepo.getTodaySteps()
        val weekly = activityRepo.getLast7DaysSteps().sum()

        txtDaily.text = getString(R.string.goal_daily_value, today, dGoal)
        txtWeekly.text = getString(R.string.goal_weekly_value, weekly, wGoal)

        progressDaily.progress = if (dGoal > 0) (today * 100 / dGoal).coerceIn(0, 100) else 0
        progressWeekly.progress = if (wGoal > 0) (weekly * 100 / wGoal).coerceIn(0, 100) else 0
    }
}
