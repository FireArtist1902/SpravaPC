package data

import models.Settings
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction
import org.jetbrains.exposed.sql.update

class SettingsRepository{
    fun getSettings(): Settings{
        return transaction {
            val row = SettingsTable.selectAll().firstOrNull()

            if(row == null){
                SettingsTable.insert {
                    it[notification] = 5
                    it[language] = "uk"
                    it[theme] = "dark"
                    it[isTray] = true
                }
                Settings()
            } else{
                Settings(
                    id = row[SettingsTable.id],
                    notificationMinutes = row[SettingsTable.notification],
                    language = row[SettingsTable.language],
                    theme = row[SettingsTable.theme],
                    isTray = row[SettingsTable.isTray]
                )
            }
        }
    }

    fun save(settings: Settings){
        transaction {
            SettingsTable.update({ SettingsTable.id eq settings.id}){
                it[notification] = settings.notificationMinutes
                it[language] = settings.language
                it[theme] = settings.theme
                it[isTray] = settings.isTray
            }
        }
    }
}