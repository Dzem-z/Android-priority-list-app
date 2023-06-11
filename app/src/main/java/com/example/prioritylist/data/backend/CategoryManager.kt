package com.example.prioritylist.data.backend

import androidx.compose.ui.graphics.Color

/*
TODO(comments)
 */

class CategoryManager(
    listOfCategories: MutableList<Category> = mutableListOf<Category>()
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