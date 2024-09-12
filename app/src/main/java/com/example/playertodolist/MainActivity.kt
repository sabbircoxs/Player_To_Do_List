package com.example.playertodolist

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.playertodolist.adapter.TaskAdapter
import com.example.playertodolist.data.Task

class MainActivity : AppCompatActivity() {

    private lateinit var tasklist:MutableList<Task>
    private lateinit var recyclerView: RecyclerView
    private lateinit var taskAdapter: TaskAdapter
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var editTaskEditText: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        sharedPreferences = getSharedPreferences("TaskPrefs", Context.MODE_PRIVATE)

        recyclerView = findViewById(R.id.Recyclerview)
        editTaskEditText = findViewById(R.id.noteEt)
        tasklist = retrieveTasks()




        val saveButton: Button = findViewById(R.id.SaveBtn)

        saveButton.setOnClickListener {
            val taskText = editTaskEditText.text.toString()
            if (taskText.isNotEmpty()) {
                val newTask = Task(taskText, false)
                saveTask(newTask)
                tasklist.add(newTask)
                taskAdapter.notifyItemInserted(tasklist.size - 1)
                editTaskEditText.text.clear()
            } else {
                Toast.makeText(this, "Please enter a task", Toast.LENGTH_SHORT).show()
            }

        }

            taskAdapter = TaskAdapter(tasklist, object : TaskAdapter.TaskClickLister {
                override fun onEditClicked(position: Int) {
                    editTaskEditText.setText(tasklist[position].title)
                    tasklist.removeAt(position)
                    taskAdapter.notifyDataSetChanged()
                }

                override fun onDeleteClicked(position: Int) {
                    val alertDialog = AlertDialog.Builder(this@MainActivity)
                    alertDialog.setTitle("Delete Task")
                    alertDialog.setMessage("Are you sure you want to delete this task?")
                    alertDialog.setPositiveButton("Yes") { _, _ ->
                        deleteTask(position)
                    }

                    alertDialog.setNegativeButton("No") { _, _ ->}
                    alertDialog.show()
                }

            })

        recyclerView.adapter = taskAdapter
        recyclerView.layoutManager = LinearLayoutManager(this)
        }


    private fun saveTask(newTask: Task) {

        val editor = sharedPreferences.edit()
        val taskset = HashSet<String>()

        tasklist.forEach{taskset.add(it.title)}
        editor.putStringSet("tasks",taskset)
        editor.apply()
    }
    private fun deleteTask(position: Int) {
        tasklist.removeAt(position)
        taskAdapter.notifyItemRemoved(position)
        saveTasks(tasklist)
    }
    private fun saveTasks(tasklist: MutableList<Task>) {
    }
    private fun retrieveTasks(): MutableList<Task> {

        val tasks = sharedPreferences.getStringSet("tasks",HashSet()) ?: HashSet()
        return tasks.map { Task(it,false) }.toMutableList()

    }
}