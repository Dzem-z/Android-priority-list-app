package com.example.prioritylist.data

import kotlin.reflect.KClass

/*
TODO(comments)
 */

enum class TaskTypes(val taskType: KClass<out Task>, val listType: KClass<out TaskList<*>>) {
    CATEGORY(CategoryTask::class, CategoryTaskList::class),
    PRIORITY(PriorityTask::class, PriorityTaskList::class),
    DEADLINE_PRIORITY_CATEGORY(DeadlinePriorityCategoryTask::class, DeadlinePriorityCategoryTaskList::class),
    DEADLINE_CATEGORY(DeadlineCategoryTask::class, DeadlineCategoryTaskList::class),
    DEADLINE_PRIORITY(DeadlinePriorityTask::class, DeadlinePriorityTaskList::class),
    DEADLINE(DeadlineTask::class, DeadlineTaskList::class)
}