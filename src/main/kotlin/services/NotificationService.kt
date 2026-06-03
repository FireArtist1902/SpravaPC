package services

import data.SettingsRepository
import data.TaskRepository
import java.awt.TrayIcon
import java.time.LocalDateTime
import java.util.Timer
import java.util.TimerTask
import java.time.Duration

class NotificationService(
    private val trayIcon: TrayIcon,

    private val repository: TaskRepository
) {
    private val timer = Timer()
    private val settingsRepository = SettingsRepository()

    fun start(){
        timer.scheduleAtFixedRate(
            object : TimerTask(){
                override fun run() {
                    checkTasks()
                }
            },
            0,
            60000
        )
    }

    private fun checkTasks(){
        val tasks = repository.getAllTasks()
        val settings = settingsRepository.getSettings()

        tasks.forEach { task->
            if(
                task.isCompleted ||
                !task.isDated ||
                task.startDate == null
            ){
                return@forEach
            }

            val minutes = Duration
                .between(LocalDateTime.now(),
                    task.startDate).toMinutes()

            if(minutes in 0..settings.notificationMinutes){
                trayIcon.displayMessage(
                    LanguageManager.get("remind"),
                    task.title,
                    TrayIcon.MessageType.INFO
                )
            }
        }
    }
}