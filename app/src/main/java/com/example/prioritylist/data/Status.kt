package com.example.prioritylist.data

class Status(
    val code: StatusEnum,
    val message: String = ""
) {

}

enum class StatusEnum {
    SUCCESS,
    FAILURE
}