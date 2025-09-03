package org.example.app.ui

import android.app.AlertDialog
import android.os.Bundle
import android.view.*
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import org.example.app.R
import org.example.app.data.ActivityRepository
import org.example.app.ui.adapters.ActivityLogAdapter

/**
 * PUBLIC_INTERFACE
 * ActivitiesFragment
 *
 * Displays daily activity logs (steps, distance). Allows adding a new activity entry.
 */
class ActivitiesFragment : Fragment() {

    private lateinit var repo: ActivityRepository
    private lateinit var recycler: RecyclerView
    private lateinit var adapter: ActivityLogAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        repo = ActivityRepository(requireContext())
        setHasOptionsMenu(true)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_add, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.action_add) {
            showAddDialog()
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val v = inflater.inflate(R.layout.fragment_activities, container, false)
        recycler = v.findViewById(R.id.recycler_activities)
        recycler.layoutManager = LinearLayoutManager(requireContext())
        adapter = ActivityLogAdapter(repo.getAll())
        recycler.adapter = adapter
        return v
    }

    override fun onResume() {
        super.onResume()
        adapter.update(repo.getAll())
    }

    private fun showAddDialog() {
        val view = layoutInflater.inflate(R.layout.dialog_add_activity, null)
        val steps: EditText = view.findViewById(R.id.input_steps)
        val distance: EditText = view.findViewById(R.id.input_distance)

        AlertDialog.Builder(requireContext())
            .setTitle(R.string.add_activity)
            .setView(view)
            .setPositiveButton(R.string.save) { d, _ ->
                val s = steps.text.toString().toIntOrNull() ?: 0
                val km = distance.text.toString().toFloatOrNull() ?: 0f
                repo.addToday(s, km)
                adapter.update(repo.getAll())
                Toast.makeText(requireContext(), R.string.saved, Toast.LENGTH_SHORT).show()
                d.dismiss()
            }
            .setNegativeButton(R.string.cancel, null)
            .show()
    }
}
