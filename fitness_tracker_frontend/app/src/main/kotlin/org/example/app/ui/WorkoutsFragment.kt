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
import org.example.app.data.WorkoutRepository
import org.example.app.ui.adapters.WorkoutAdapter

/**
 * PUBLIC_INTERFACE
 * WorkoutsFragment
 *
 * Displays workout logs and enables adding a new workout entry.
 */
class WorkoutsFragment : Fragment() {

    private lateinit var repo: WorkoutRepository
    private lateinit var recycler: RecyclerView
    private lateinit var adapter: WorkoutAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        repo = WorkoutRepository(requireContext())
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
        val v = inflater.inflate(R.layout.fragment_workouts, container, false)
        recycler = v.findViewById(R.id.recycler_workouts)
        recycler.layoutManager = LinearLayoutManager(requireContext())
        adapter = WorkoutAdapter(repo.getAll())
        recycler.adapter = adapter
        return v
    }

    override fun onResume() {
        super.onResume()
        adapter.update(repo.getAll())
    }

    private fun showAddDialog() {
        val view = layoutInflater.inflate(R.layout.dialog_add_workout, null)
        val type: EditText = view.findViewById(R.id.input_type)
        val duration: EditText = view.findViewById(R.id.input_duration)
        val calories: EditText = view.findViewById(R.id.input_calories)

        AlertDialog.Builder(requireContext())
            .setTitle(R.string.add_workout)
            .setView(view)
            .setPositiveButton(R.string.save) { d, _ ->
                val t = type.text.toString().ifEmpty { getString(R.string.workout_generic) }
                val dur = duration.text.toString().toIntOrNull() ?: 0
                val cal = calories.text.toString().toIntOrNull() ?: 0
                repo.add(t, dur, cal)
                adapter.update(repo.getAll())
                Toast.makeText(requireContext(), R.string.saved, Toast.LENGTH_SHORT).show()
                d.dismiss()
            }
            .setNegativeButton(R.string.cancel, null)
            .show()
    }
}
