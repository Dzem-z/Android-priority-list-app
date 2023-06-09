package com.example.prioritylist.data

/*
TODO(comments)
 */

class Storage<TaskType: Task>() {

    private val listOfActions: MutableList<Action<TaskType>> = mutableListOf<Action<TaskType>>()

   fun pop(): Action<TaskType> {
        return listOfActions.removeLast()
    }
    fun push(action: Action<TaskType>) {
        listOfActions.add(action)
    }
}

open class Action<TaskType: Task>(
    protected open val oldTask: TaskType
){
}

class Delete<TaskType: Task>(
    override val oldTask: TaskType
    ) : Action<TaskType>(
    oldTask = oldTask
) {
}

class Edit<TaskType: Task>(
    override val oldTask: TaskType
) : Action<TaskType>(
    oldTask = oldTask
) {
}

class Add<TaskType: Task>(
    override val oldTask: TaskType
) : Action<TaskType>(
    oldTask = oldTask
) {
}