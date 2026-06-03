package ui

import data.SettingsRepository
import org.example.main
import services.LanguageManager
import java.awt.GridLayout
import java.awt.Window
import javax.swing.JButton
import javax.swing.JCheckBox
import javax.swing.JComboBox
import javax.swing.JDialog
import javax.swing.JFrame
import javax.swing.JLabel
import javax.swing.JOptionPane
import javax.swing.JTextField
import javax.swing.SwingUtilities

class SettingsDialog(
    parent: JFrame
): JDialog(parent, true){

    private val repository = SettingsRepository()
    private val settings = repository.getSettings()

    init {
        title = LanguageManager.get("settings")

        setSize(300, 200)

        layout = GridLayout(5, 2)

        val minutesField = JTextField("5")

        val languageBox = JComboBox(
            arrayOf("uk", "en")
        )

        val themeBox = JComboBox(arrayOf(
            "dark",
            "light"
        ))

        themeBox.selectedItem = settings.theme

        val trayCheck = JCheckBox()
        trayCheck.isSelected = settings.isTray

        add(JLabel(LanguageManager.get("remind_minutes")))
        add(minutesField)

        add(JLabel(LanguageManager.get("language")))
        add(languageBox)

        add(JLabel(LanguageManager.get("theme")))
        add(themeBox)

        add(JLabel(LanguageManager.get("background")))
        add(trayCheck)

        val saveButton = JButton(LanguageManager.get("save"))

        add(saveButton)

        minutesField.text = settings.notificationMinutes.toString()

        languageBox.selectedItem = settings.language

        saveButton.addActionListener {
            settings.notificationMinutes = minutesField.text.toInt()
            settings.language = languageBox.selectedItem!!.toString()
            settings.theme = themeBox.selectedItem!!.toString()
            settings.isTray = trayCheck.isSelected

            repository.save(settings)

            if(settings.theme == "dark"){
                ThemeManager.applyDark()
            } else{
                ThemeManager.applyLight()
            }

            Window.getWindows().forEach {
                SwingUtilities.updateComponentTreeUI(it)

            }

            LanguageManager.setLanguage(settings.language)

            parent.revalidate()
            parent.repaint()

            dispose()

            parent.defaultCloseOperation = DISPOSE_ON_CLOSE

            main()

            parent.dispose()
        }
    }
}