package org.example

import data.DatabaseFactory
import ui.DarkTheme
import ui.MainFrame
import javax.swing.SwingUtilities

fun main() {
    DatabaseFactory.init()
    DarkTheme.setup()
    SwingUtilities.invokeLater {
        MainFrame().isVisible = true
    }
}