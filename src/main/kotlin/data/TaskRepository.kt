package data

import models.TaskItem
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.deleteWhere
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction
import org.jetbrains.exposed.sql.update
import java.time.LocalDateTime

class TaskRepository {
    fun getAllTasks(): List<TaskItem>{
        return transaction {
            TaskTable.selectAll().map {
                TaskItem(
                    id = it[TaskTable.id],
                    title = it[TaskTable.title],
                    description = it[TaskTable.description],
                    isCompleted = it[TaskTable.isCompleted],
                    isDated = it[TaskTable.isDated],
                    startDate = it[TaskTable.startDate]?.let(LocalDateTime::parse),
                    endDate = it[TaskTable.endDate]?.let(LocalDateTime::parse)
                )
            }
        }
    }

    fun addTask(task: TaskItem){
        transaction {
            TaskTable.insert {
                it[title] = task.title
                it[description] = task.description
                it[isCompleted] = task.isCompleted
                it[isDated] = task.isDated
                it[startDate] = task.startDate?.toString()
                it[endDate] = task.endDate?.toString()
            }
        }
    }

    fun updateTask(task: TaskItem){
        transaction {
            TaskTable.update(
                { TaskTable.id eq task.id}
            ){
                it[title] = task.title
                it[description] = task.description
                it[isCompleted] = task.isCompleted
                it[isDated] = task.isDated
                it[startDate] = task.startDate?.toString()
                it[endDate] = task.endDate?.toString()
            }
        }
    }

    fun deleteTask(id: Int){
        transaction {
            TaskTable.deleteWhere {
                TaskTable.id eq id
            }
        }
    }
}