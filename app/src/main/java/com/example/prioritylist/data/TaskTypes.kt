package com.example.prioritylist.data

import kotlin.reflect.KClass

/*
TODO(comments)
 */

enum class TaskTypes(val taskType: KClass<out Task>, val listType: KClass<out TaskList<*>>) {
    CATEGORY(CategoryTask::class, CategoryTaskList::class){
        override fun hasPriority() = false
        override fun hasCategory() = true
        override fun hasDeadline() = false
                                                          },
    PRIORITY(PriorityTask::class, PriorityTaskList::class){
        override fun hasPriority() = true
        override fun hasCategory() = false
        override fun hasDeadline() = false
                                                          },
    DEADLINE_PRIORITY_CATEGORY(DeadlinePriorityCategoryTask::class, DeadlinePriorityCategoryTaskList::class){
        override fun hasPriority() = true
        override fun hasCategory() = true
        override fun hasDeadline() = true
                                                                                                            },
    DEADLINE_CATEGORY(DeadlineCategoryTask::class, DeadlineCategoryTaskList::class){
        override fun hasPriority() = false
        override fun hasCategory() = true
        override fun hasDeadline() = true
                                                                                    },
    DEADLINE_PRIORITY(DeadlinePriorityTask::class, DeadlinePriorityTaskList::class){
        override fun hasPriority() = true
        override fun hasCategory() = false
        override fun hasDeadline() = true
                                                                                   },
    DEADLINE(DeadlineTask::class, DeadlineTaskList::class){
        override fun hasPriority() = false
        override fun hasCategory() = false
        override fun hasDeadline() = true
                                                            };

    abstract fun hasPriority(): Boolean
    abstract fun hasCategory(): Boolean
    abstract fun hasDeadline(): Boolean
}