package com.example.prioritylist.data

class Status(
    val code: StatusCodes,
    val message: String = ""
) {

}

enum class StatusCodes {
    SUCCESS,
    FAILURE,
    DUPLICATED_TASK,
    NOT_FOUND,
    EMPTY_NAME
}