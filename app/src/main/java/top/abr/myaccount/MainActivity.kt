package top.abr.myaccount

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatDelegate
import androidx.drawerlayout.widget.DrawerLayout
import top.abr.myaccount.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    lateinit var ActivityMain: ActivityMainBinding
    lateinit var MainDrawerLayout: DrawerLayout
    lateinit var NavDrawerListener: ActionBarDrawerToggle

    override fun onCreate(SavedInstanceState: Bundle?) {
        super.onCreate(SavedInstanceState)
        // Inflate
        ActivityMain = ActivityMainBinding.inflate(layoutInflater)
        setContentView(ActivityMain.root)
        MainDrawerLayout = ActivityMain.MainDrawerLayout.apply {
            NavDrawerListener = ActionBarDrawerToggle(this@MainActivity, this, R.string.open_nav_drawer, R.string.close_nav_drawer)
            addDrawerListener(NavDrawerListener) // pass the Open and Close toggle for the drawer layout listener to toggle the button
        }
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        // Theme
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)		// Dark theme as default
        // Main navigation drawer
    }

    override fun onPostCreate(SavedInstanceState: Bundle?) {
        super.onPostCreate(SavedInstanceState)
        NavDrawerListener.syncState()
    }
}