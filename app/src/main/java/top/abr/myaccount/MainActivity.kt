package top.abr.myaccount

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.app.AppCompatDelegate
import top.abr.myaccount.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    lateinit var ActivityMain: ActivityMainBinding

    override fun onCreate(SavedInstanceState: Bundle?) {
        super.onCreate(SavedInstanceState)
        ActivityMain = ActivityMainBinding.inflate(layoutInflater)
        setContentView(ActivityMain.root)

        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)		// Dark theme as default
    }
}