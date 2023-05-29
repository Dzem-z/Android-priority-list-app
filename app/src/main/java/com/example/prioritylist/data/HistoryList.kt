package com.example.prioritylist.data

import java.util.Date

/*
TODO(comments)
 */


class HistoryList<TaskType: Task>() {
    val listOfTasks: MutableList<HistoryTask<TaskType>> = mutableListOf()

    private fun normalizeIndexes() {
        listOfTasks.forEachIndexed { index, element -> element.it.id = index }
    }

    fun pushTask(newTask: TaskType, completionDate: Date): Status {
        if (listOfTasks.find{ newTask.name == it.it.name  } != null){
            return Status(StatusCodes.DUPLICATED_TASK)
        } else if (newTask.name.isEmpty()) {
            return Status(StatusCodes.EMPTY_NAME)
        }
        listOfTasks.add(HistoryTask(newTask, completionDate))
        //TODO(write to database)

        listOfTasks.sortBy{ it.completionDate }
        listOfTasks.reverse()

        return Status(StatusCodes.SUCCESS)
    }

    fun getTaskByPos(id: Int): HistoryTask<TaskType> {  //old name: getTaskByID
        return listOfTasks[id];
    }

    fun getList(): MutableList<HistoryTask<TaskType>> {
        return listOfTasks.toMutableList()
    }

    fun deleteTask(deletedTask: HistoryTask<TaskType>): Status {
        if (listOfTasks.find { it == deletedTask } == null){
            throw NoSuchElementException()
        }
        listOfTasks.remove(deletedTask)
        //TODO(write to database)
        return Status(StatusCodes.SUCCESS)
    }

    fun deleteUntil(date: Date) {
        TODO("not yet implemented")
    }

}

data class HistoryTask<TaskType: Task>(
    val it: TaskType,
    val completionDate: Date
)