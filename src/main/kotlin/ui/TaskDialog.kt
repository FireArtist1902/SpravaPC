package ui

import data.TaskRepository
import models.TaskItem
import java.awt.GridLayout
import java.time.LocalDateTime
import javax.swing.JButton
import javax.swing.JCheckBox
import javax.swing.JDialog
import javax.swing.JFrame
import javax.swing.JLabel
import javax.swing.JScrollPane
import javax.swing.JTextArea
import javax.swing.JTextField
import com.github.lgooddatepicker.components.DateTimePicker
import kotlinx.coroutines.Job
import services.LanguageManager
import javax.swing.BoxLayout
import javax.swing.JOptionPane

class TaskDialog(
    parent: JFrame,
    private val task: TaskItem?
): JDialog(parent, true){
    private val repository = TaskRepository()

    private val titleField = JTextField()

    private val descriptionField = JTextArea()

    private val datedCheckBox = JCheckBox(LanguageManager.get("dated"))

    private val startField = DateTimePicker()

    private val endField = DateTimePicker()

    init {
        title = LanguageManager.get("tasks")

        setSize(400, 400)

        layout = BoxLayout(contentPane, BoxLayout.Y_AXIS)

        add(JLabel(LanguageManager.get("title")))
        add(titleField)

        add(JLabel(LanguageManager.get("desc")))
        add(JScrollPane(descriptionField))

        add(datedCheckBox)

        add(JLabel(LanguageManager.get("start_date")))
        add(startField)

        add(JLabel(LanguageManager.get("end_date")))
        add(endField)

        val saveButton = JButton(LanguageManager.get("save"))

        add(saveButton)

        if(task != null){
            titleField.text = task.title
            descriptionField.text = task.description
            datedCheckBox.isSelected = task.isDated
            task.startDate?.let {
                startField.dateTimeStrict = it
            }

            task.endDate?.let {
                endField.dateTimeStrict = it
            }
        }

        saveButton.addActionListener {
            saveTask()
        }
    }

    private fun saveTask(){

        if(!validateInput()){
            return
        }

        if(task == null){
            repository.addTask(
                TaskItem(
                    id = 0,
                    title = titleField.text,
                    description = descriptionField.text,
                    isCompleted = false,
                    isDated = datedCheckBox.isSelected,
                    startDate = startField.dateTimeStrict,
                    endDate = endField.dateTimeStrict
                )
            )
        } else{
            task.title = titleField.text
            task.description = descriptionField.text
            task.isDated = datedCheckBox.isSelected
            task.startDate = startField.dateTimeStrict
            task.endDate = endField.dateTimeStrict

            repository.updateTask(task)
        }
        dispose()
    }

    private fun validateInput(): Boolean{
        if(titleField.text.isBlank()){
            JOptionPane.showMessageDialog(this, LanguageManager.get("title_required"))
            return false
        }

        if(titleField.text.length > 100){
            JOptionPane.showMessageDialog(
                this,
                LanguageManager.get("title_too_long")
            )
            return false
        }

        if(datedCheckBox.isSelected){
            if(startField.dateTimeStrict == null){
                JOptionPane.showMessageDialog(
                    this,
                    LanguageManager.get("start_required")
                )
                return false
            }

            if(endField.dateTimeStrict == null){
                JOptionPane.showMessageDialog(
                    this,
                    LanguageManager.get("end_required")
                )
                return false
            }

            if(endField.dateTimeStrict!!.isBefore(startField.dateTimeStrict) ||
                startField.dateTimeStrict!!.isBefore(LocalDateTime.now())){
                JOptionPane.showMessageDialog(
                    this,
                    LanguageManager.get("wrong_dates")
                )
                return false
            }
        }

        return true
    }
}