package com.example.prioritylist.data.backend

import com.example.prioritylist.data.backend.Status
import com.example.prioritylist.data.backend.StatusCodes
import com.example.prioritylist.data.database.ListRepository
import com.example.prioritylist.data.database.TaskEntity
import java.util.Date

/*
TODO(comments)
 */


class HistoryList<TaskType: Task>(
    val listRepository: ListRepository,
    val list: TaskList<TaskType>,
    val listOfTasks: MutableList<HistoryTask<TaskType>> = mutableListOf()
) {

    private fun toTaskEntity(historyTask: HistoryTask<TaskType>): TaskEntity {
        val returnEntity = list.toTaskEntity(historyTask.it)
        return TaskEntity(
            name = returnEntity.name,
            description = returnEntity.description,
            dateOfCreation = returnEntity.dateOfCreation,
            priority = returnEntity.priority,
            category = returnEntity.category,
            deadline = returnEntity.deadline,
            listID = returnEntity.listID,
            type = returnEntity.type,
            dateOfCompletion = historyTask.completionDate
        )
    }

    private fun normalizeIndexes() {
        listOfTasks.forEachIndexed { index, element -> element.it.id = index }
    }

    suspend fun pushTask(newTask: TaskType, completionDate: Date): Status {
        if (listOfTasks.find{ newTask.name == it.it.name  } != null){
            return Status(StatusCodes.DUPLICATED_TASK)
        } else if (newTask.name.isEmpty()) {
            return Status(StatusCodes.EMPTY_NAME)
        }
        listOfTasks.add(HistoryTask(newTask, completionDate))
        listRepository.add(toTaskEntity(HistoryTask(newTask, completionDate)))

        listOfTasks.sortBy{ it.completionDate }
        listOfTasks.reverse()

        return Status(StatusCodes.SUCCESS)
    }

    fun getTaskByPos(id: Int): HistoryTask<TaskType> {  //old name: getTaskByID
        return listOfTasks[id];
    }

    fun getList(): MutableList<HistoryTask<TaskType>> {
        listOfTasks.sortBy{ it.completionDate }
        listOfTasks.reverse()
        return listOfTasks.toMutableList()
    }

    suspend fun deleteTask(deletedTask: HistoryTask<TaskType>): Status {
        if (listOfTasks.find { it == deletedTask } == null){
            throw NoSuchElementException()
        }
        listOfTasks.remove(deletedTask)
        listRepository.delete(toTaskEntity(deletedTask))
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