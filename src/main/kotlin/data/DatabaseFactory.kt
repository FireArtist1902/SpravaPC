package data

import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.transactions.transaction

object TaskTable: Table("tasks"){
    val id = integer("id").autoIncrement()
    val title = varchar("title", 255)
    val description = text("description")
    val isCompleted = bool("is_completed")
    val isDated = bool("is_dated")
    val startDate = varchar("start_date", 255).nullable()
    val endDate = varchar("end_date", 255).nullable()
    override val primaryKey = PrimaryKey(id)
}

object SettingsTable: Table("settings"){
    val id = integer("id").autoIncrement()
    val notification = integer("notification_minutes")
    val language = varchar("language", 10)
    val theme = varchar("theme", 10)
    val isTray = bool("is_tray")
    override val primaryKey = PrimaryKey(id)
}

object DatabaseFactory {
    fun init(){
        Database.connect(
            "jdbc:sqlite:tasks.db",
            driver = "org.sqlite.JDBC"
        )

        transaction {
            SchemaUtils.create(TaskTable, SettingsTable)
        }
    }
}