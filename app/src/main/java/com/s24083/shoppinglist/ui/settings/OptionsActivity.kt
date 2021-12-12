package com.s24083.shoppinglist.ui.settings

import android.os.Bundle
import android.view.View
import android.widget.Switch
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.slider.Slider
import com.s24083.shoppinglist.R

class OptionsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_options)

        val sp = this.getSharedPreferences("1", MODE_PRIVATE)

        val detailsKey = resources.getString(R.string.settingsShowDetails)
        val detailsValue = sp.getBoolean(detailsKey, false)
        findViewById<Switch>(R.id.show_details).isChecked = detailsValue

        val sizeKey = resources.getString(R.string.settingsTitleFontSize)
        val sizeValue = sp.getFloat(sizeKey, 1.0f)
        findViewById<Slider>(R.id.font_size).value = sizeValue
    }

    fun saveSettings(view: View){
        val sp = this.getSharedPreferences("1", MODE_PRIVATE)

        val detailsKey = resources.getString(R.string.settingsShowDetails)
        val detailsValue = findViewById<Switch>(R.id.show_details).isChecked
        val editor = sp.edit()
        editor.putBoolean(detailsKey, detailsValue)

        val sizeKey = resources.getString(R.string.settingsTitleFontSize)
        val sizeValue = findViewById<Slider>(R.id.font_size).value
        editor.putFloat(sizeKey, sizeValue)

        editor.apply()
    }
}