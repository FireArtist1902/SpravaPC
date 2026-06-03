package models

data class Settings(
    val id: Int = 1,
    var notificationMinutes: Int = 5,
    var language: String = "uk",
    var theme: String = "dark",
    var isTray: Boolean = true
)