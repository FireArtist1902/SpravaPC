package services

object LanguageManager {
    private var language = "uk"

    private val translations = mapOf(
        "uk" to mapOf(
            "add" to "Додати",
            "edit" to "Редагувати",
            "delete" to "Видалити",
            "complete" to "Виконання",
            "settings" to "Налаштування",
            "title_required" to "Введіть назву",
            "wrong_dates" to "Дата завершення або початку не правильна",
            "title" to "Назва",
            "desc" to "Опис",
            "completed" to "Виконано",
            "start_date" to "Дата початку",
            "end_date" to "Дата кінця",
            "remind_minutes" to "Хвилини до нагадування",
            "language" to "Мова",
            "theme" to "Тема",
            "background" to "Робота застосунку у фоні",
            "save" to "Зберегти",
            "tasks" to "Завдання",
            "dated" to "Датоване",
            "select" to "Оберіть завдання",
            "task_completed" to "Завдання виконано",
            "open" to "Відкрити",
            "exit" to "Вихід",
            "on_back" to "Програма працює у фоні",
            "remind" to "Нагадування",
            "completed" to "Виконано",
            "incompleted" to "Не виконано"
        ),
        "en" to mapOf(
            "add" to "Add",
            "edit" to "Edit",
            "delete" to "Delete",
            "complete" to "Complete",
            "settings" to "Settings",
            "title_required" to "Enter data",
            "wrong_dates" to "End or start date are wrong",
            "title" to "Title",
            "desc" to "Description",
            "completed" to "Is completed",
            "start_date" to "Start date",
            "end_date" to "End date",
            "remind_minutes" to "Remind before on",
            "language" to "Language",
            "theme" to "Theme",
            "background" to "Background work",
            "save" to "Save",
            "tasks" to "Tasks",
            "dated" to "Dated",
            "select" to "Select task",
            "task_completed" to "Task completed",
            "open" to "Open",
            "exit" to "Exit",
            "on_back" to "The application runs in the background",
            "remind" to "Remind",
            "completed" to "Completed",
            "incompleted" to "Incompleted"
        )
    )

    fun setLanguage(lang: String){
        this.language = lang
    }

    fun get(key: String): String{
        return translations[language]?.get(key)?: key
    }
}