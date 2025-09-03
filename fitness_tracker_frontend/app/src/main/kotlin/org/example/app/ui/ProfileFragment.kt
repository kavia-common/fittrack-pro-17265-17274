package org.example.app.ui

import android.app.TimePickerDialog
import android.os.Bundle
import android.view.*
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import org.example.app.R
import org.example.app.data.AuthRepository
import org.example.app.notifications.ReminderScheduler
import java.util.Calendar

/**
 * PUBLIC_INTERFACE
 * ProfileFragment
 *
 * Shows user profile, logout action, and notification reminder time configuration.
 */
class ProfileFragment : Fragment() {

    private lateinit var txtEmail: TextView
    private lateinit var btnLogout: Button
    private lateinit var btnReminder: Button
    private lateinit var txtReminder: TextView

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val v = inflater.inflate(R.layout.fragment_profile, container, false)
        txtEmail = v.findViewById(R.id.txt_email)
        btnLogout = v.findViewById(R.id.btn_logout)
        btnReminder = v.findViewById(R.id.btn_set_reminder)
        txtReminder = v.findViewById(R.id.txt_reminder_time)

        val auth = AuthRepository(requireContext())
        txtEmail.text = auth.getLoggedInEmail()

        btnLogout.setOnClickListener {
            auth.logout()
            requireActivity().finish()
        }

        val scheduler = ReminderScheduler(requireContext())
        btnReminder.setOnClickListener {
            val cal = scheduler.getReminderTime()
            TimePickerDialog(
                requireContext(),
                { _, hour, minute ->
                    scheduler.setReminderTime(hour, minute)
                    scheduler.scheduleDailyReminder(hour, minute)
                    bindReminderText()
                },
                cal.get(Calendar.HOUR_OF_DAY),
                cal.get(Calendar.MINUTE),
                true
            ).show()
        }

        bindReminderText()
        return v
    }

    private fun bindReminderText() {
        val cal = ReminderScheduler(requireContext()).getReminderTime()
        val h = cal.get(Calendar.HOUR_OF_DAY)
        val m = cal.get(Calendar.MINUTE)
        txtReminder.text = getString(R.string.reminder_time_value, "%02d:%02d".format(h, m))
    }
}
