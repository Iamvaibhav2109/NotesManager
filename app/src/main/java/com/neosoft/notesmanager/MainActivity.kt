package com.neosoft.notesmanager

import android.os.Bundle
import android.view.Menu
import android.widget.CompoundButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SwitchCompat
import androidx.lifecycle.ViewModelProvider
import com.neosoft.notesmanager.databinding.ActivityMainBinding
import com.neosoft.notesmanager.ui.notes.compose.ComposeNotesFragment
import com.neosoft.notesmanager.ui.notes.xml.fragment.XmlNotesFragment
import com.neosoft.notesmanager.viewmodel.NotesViewModel

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var viewmodel: NotesViewModel

    private var useCompose = false
    private val PREFS = "ui_prefs"
    private val KEY_COMPOSE = "use_compose"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewmodel = ViewModelProvider(this)[NotesViewModel::class.java]

        setSupportActionBar(binding.toolbar)

        useCompose = getSharedPreferences(PREFS, MODE_PRIVATE)
            .getBoolean(KEY_COMPOSE, false)

        showFragment(useCompose)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return true
    }

    override fun onPrepareOptionsMenu(menu: Menu): Boolean {
        val item = menu.findItem(R.id.action_switch)
        val switchView = item.actionView?.findViewById<SwitchCompat>(R.id.switch_for_action_bar)

        switchView?.apply {
            isChecked = useCompose

            setOnCheckedChangeListener { _: CompoundButton, checked: Boolean ->
                useCompose = checked

                getSharedPreferences(PREFS, MODE_PRIVATE)
                    .edit()
                    .putBoolean(KEY_COMPOSE, checked)
                    .apply()

                Toast.makeText(
                    this@MainActivity,
                    if (checked) "Switched to COMPOSE UI" else "Switched to XML UI",
                    Toast.LENGTH_SHORT
                ).show()

                showFragment(checked)
            }
        }

        return super.onPrepareOptionsMenu(menu)
    }

    private fun showFragment(compose: Boolean) {
        val frag = if (compose) ComposeNotesFragment() else ComposeNotesFragment()

        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, frag)
            .commit()
    }
}
