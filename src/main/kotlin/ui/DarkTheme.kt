package ui

import com.formdev.flatlaf.FlatDarkLaf
import com.formdev.flatlaf.FlatLightLaf
import data.SettingsRepository
import javax.swing.UIManager

object DarkTheme{
    fun setup(){

        val settings = SettingsRepository().getSettings()

        if(settings.theme == "dark"){
            ThemeManager.applyDark()
        } else{
            ThemeManager.applyLight()
        }

        UIManager.put("Button.arc", 15)
        UIManager.put("Component.arc", 15)
        UIManager.put("TextComponent.arc", 15)
    }
}

object ThemeManager{
    fun applyDark(){
        FlatDarkLaf.setup()
    }

    fun applyLight(){
        FlatLightLaf.setup()
    }
}