package com.example.prioritylist.data

import androidx.compose.ui.graphics.Color

/*
TODO(comments)
 */

class CategoryManager(
    listOfCategories: MutableList<Category>
) {
    fun deleteCategory(category: Category) {}
    fun addCategory(category: Category) {}
}

/*
TODO(comments)
 */

data class Category(
    val name: String,
    val color: Color,
    val description: String,
    val priority: Int
)