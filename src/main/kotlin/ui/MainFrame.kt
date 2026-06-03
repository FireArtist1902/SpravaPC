package ui

import data.SettingsRepository
import data.TaskRepository
import models.TaskItem
import services.LanguageManager
import services.NotificationService
import java.awt.BorderLayout
import java.awt.GridLayout
import java.awt.Menu
import java.awt.MenuItem
import java.awt.PopupMenu
import java.awt.SystemTray
import java.awt.Toolkit
import java.awt.TrayIcon
import java.awt.event.WindowAdapter
import java.awt.event.WindowEvent
import javax.swing.*
import javax.swing.table.DefaultTableModel
import java.time.format.DateTimeFormatter

class MainFrame : JFrame(){
    private val repository = TaskRepository()

    private lateinit var trayIcon: TrayIcon

    private val undatedModel = DefaultTableModel()

    private val datedModel = DefaultTableModel()

    private val undatedTable = JTable(undatedModel)

    private val datedTable = JTable(datedModel)

    private val formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm")

    private val settings = SettingsRepository().getSettings()

    init {
        title = "Sprava"
        setSize(1000, 700)
        defaultCloseOperation = if(settings.isTray) JFrame.DO_NOTHING_ON_CLOSE else JFrame.EXIT_ON_CLOSE
        if(settings.theme == "dark"){
            ThemeManager.applyDark()
        } else{
            ThemeManager.applyLight()
        }

        LanguageManager.setLanguage(settings.language)

        if(settings.isTray){
            initializeTray()

            NotificationService(
                trayIcon,
                repository
            ).start()
        }

        addWindowListener(
            object : WindowAdapter(){
                override fun windowClosing(e: WindowEvent){
                    if(settings.isTray){
                        isVisible = false

                        trayIcon.displayMessage(
                            "Sprava",
                            LanguageManager.get("on_back"),
                            TrayIcon.MessageType.INFO
                        )
                    }
                }
            }
        )

        loadTask()


        layout = BorderLayout()
        createTables()

        val panel = JPanel(GridLayout(2, 1))

        panel.add(JScrollPane(undatedTable))
        panel.add(JScrollPane(datedTable))

        add(panel, BorderLayout.CENTER)

        val buttons = JPanel()

        val addButton = JButton(LanguageManager.get("add"))
        val editButton = JButton(LanguageManager.get("edit"))
        val deleteButton = JButton(LanguageManager.get("delete"))
        val completeButton = JButton(LanguageManager.get("complete"))
        val settingsButton = JButton(LanguageManager.get("settings"))

        buttons.add(addButton)
        buttons.add(editButton)
        buttons.add(deleteButton)
        buttons.add(completeButton)
        buttons.add(settingsButton)

        add(buttons, BorderLayout.SOUTH)

        addButton.addActionListener {
            TaskDialog(this, null).isVisible = true
            loadTask()
        }

        editButton.addActionListener {

            val id = getSelectedTaskId()

            if(id == null){
                JOptionPane.showMessageDialog(this, LanguageManager.get("select"))
                return@addActionListener
            }

            val task = repository
                .getAllTasks()
                .find { it.id == id }

            TaskDialog(this, task).isVisible = true
            loadTask()
        }

        deleteButton.addActionListener {
            val id = getSelectedTaskId()

            if(id == null){
                JOptionPane.showMessageDialog(this, LanguageManager.get("select"))
                return@addActionListener
            }

            repository.deleteTask(id)
            loadTask()
        }

        completeButton.addActionListener {

            val id = getSelectedTaskId()
            if(id == null){
                JOptionPane.showMessageDialog(this, LanguageManager.get("select"))
                return@addActionListener
            }

            val task = repository
                .getAllTasks()
                .find { it.id == id }

            if(task != null){
                task.isCompleted = true
                repository.updateTask(task)
                JOptionPane.showMessageDialog(
                    this,
                    LanguageManager.get("task_completed")
                )

                loadTask()
            }
        }

        settingsButton.addActionListener {
            SettingsDialog(this).isVisible = true
        }
        loadTask()

        if(settings.isTray)
            NotificationService(trayIcon, repository).start()
    }

    private fun  createTables(){
        val columns = arrayOf(
            "ID",
            LanguageManager.get("title"),
            LanguageManager.get("desc"),
            LanguageManager.get("completed"),
            LanguageManager.get("start_date"),
            LanguageManager.get("end_date")
        )

        undatedModel.setColumnIdentifiers(columns.copyOfRange(0, 4))
        datedModel.setColumnIdentifiers(columns)

        undatedTable.columnModel.getColumn(0).minWidth = 0
        undatedTable.columnModel.getColumn(0).maxWidth = 0
        undatedTable.columnModel.getColumn(0).width = 0

        datedTable.columnModel.getColumn(0).minWidth = 0
        datedTable.columnModel.getColumn(0).maxWidth = 0
        datedTable.columnModel.getColumn(0).width = 0
    }

    private fun loadTask(){
        undatedModel.rowCount = 0
        datedModel.rowCount = 0

        val task = repository.getAllTasks()

        task.forEach {
            val row = arrayOf(
                it.id,
                it.title,
                it.description,
                it.isCompleted,
                it.startDate?.format(formatter),
                it.endDate?.format(formatter)
            )

            if(it.isDated){
                datedModel.addRow(row)
            } else{
                undatedModel.addRow(row)
            }
        }

    }

    private fun initializeTray(){
        if(!SystemTray.isSupported())
            return

        val image = Toolkit.getDefaultToolkit().getImage("icon.png")

        val popup = PopupMenu()

        val openItem = MenuItem(LanguageManager.get("open"))

        val exitItem = MenuItem(LanguageManager.get("exit"))

        popup.add(openItem)
        popup.add(exitItem)

        trayIcon = TrayIcon(image, "Sprava", popup)

        trayIcon.isImageAutoSize = true

        openItem.addActionListener {
            isVisible = true
            state = JFrame.NORMAL
        }

        exitItem.addActionListener {
            SystemTray.getSystemTray().remove(trayIcon)
            dispose()
            System.exit(0)
        }
        SystemTray.getSystemTray().add(trayIcon)
    }

    private fun getSelectedTaskId(): Int? {
        val undatedRow = undatedTable.selectedRow
        if(undatedRow != -1){
            return undatedModel.getValueAt(undatedRow, 0) as Int
        }

        val datedRow = datedTable.selectedRow

        if(datedRow != -1){
            return datedModel.getValueAt(datedRow, 0) as Int
        }

        return null
    }
}