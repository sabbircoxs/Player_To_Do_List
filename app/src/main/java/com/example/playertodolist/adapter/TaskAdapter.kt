package com.example.playertodolist.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.playertodolist.data.Task
import com.example.playertodolist.databinding.ListItemBinding

class TaskAdapter(private val tasklist:MutableList<Task>, private val clicklisten:TaskClickLister):
RecyclerView.Adapter<TaskAdapter.TaskViewHolder>(){
    interface TaskClickLister {
        fun onEditClicked(position: Int)
        fun onDeleteClicked(position: Int)
    }

    class TaskViewHolder(val binding: ListItemBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(task: Task) {
            binding.textView.text = task.title
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskViewHolder {
        val binding = ListItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return TaskViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return tasklist.size
    }

    override fun onBindViewHolder(holder: TaskViewHolder, position: Int) {
        val task = tasklist[position]
        holder.bind(task)
        holder.binding.editBtn.setOnClickListener {
            clicklisten.onEditClicked(position)
        }
        holder.binding.deleteBtn.setOnClickListener {
            clicklisten.onDeleteClicked(position)

    }

        holder.binding.CheckBox.isChecked = task.isCompleted

        holder.binding.CheckBox.setOnCheckedChangeListener { _, isChecked ->
            task.isCompleted = isChecked
        }

}
}