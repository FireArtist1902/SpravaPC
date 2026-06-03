package models

import java.time.LocalDateTime

data class TaskItem(
    val id: Int,
    var title: String,
    var description: String,
    var isCompleted: Boolean,
    var isDated: Boolean,
    var startDate: LocalDateTime?,
    var endDate: LocalDateTime?
)