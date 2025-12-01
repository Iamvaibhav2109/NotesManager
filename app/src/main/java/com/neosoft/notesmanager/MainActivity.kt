package com.neosoft.notesmanager

import android.os.Bundle
import android.view.Menu
import android.widget.CompoundButton
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SwitchCompat
import androidx.lifecycle.ViewModelProvider
import com.neosoft.notesmanager.ui.notes.compose.ComposeNotesFragment
import com.neosoft.notesmanager.ui.notes.xml.fragment.XmlNotesFragment
import com.neosoft.notesmanager.viewmodel.NotesViewModel


class MainActivity : AppCompatActivity() {
    private lateinit var viewmodel: NotesViewModel
    private var useCompose = false
    private val PREFS = "ui_prefs"
    private val KEY_COMPOSE = "use_compose"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewmodel = ViewModelProvider(this).get(NotesViewModel::class.java)
        setContentView(R.layout.activity_main)
        setSupportActionBar(findViewById(R.id.toolbar))
        useCompose = getSharedPreferences(PREFS, MODE_PRIVATE).getBoolean(KEY_COMPOSE, false)
        showFragment(useCompose)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        val item = menu.findItem(R.id.action_switch)
        val switch = item.actionView?.findViewById<SwitchCompat>(R.id.switch_for_action_bar)
        switch?.isChecked = useCompose
        switch?.setOnCheckedChangeListener { _: CompoundButton, checked: Boolean ->
            useCompose = checked
            getSharedPreferences(PREFS, MODE_PRIVATE).edit().putBoolean(KEY_COMPOSE, checked).apply()
            showFragment(checked)
        }
        return true
    }

    private fun showFragment(compose: Boolean) {
        val frag = if (compose) ComposeNotesFragment() else XmlNotesFragment()
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, frag)
            .commit()
    }
}
