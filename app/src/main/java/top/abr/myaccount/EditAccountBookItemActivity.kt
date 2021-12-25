package top.abr.myaccount

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import top.abr.myaccount.databinding.ActivityEditAccountBookItemBinding

class EditAccountBookItemActivity : AppCompatActivity() {
    // UI components
    lateinit var ActivityEditAccountBookItem: ActivityEditAccountBookItemBinding

    override fun onCreate(SavedInstanceState: Bundle?) {
        super.onCreate(SavedInstanceState)

        // Inflate
        ActivityEditAccountBookItem = ActivityEditAccountBookItemBinding.inflate(layoutInflater)
        setContentView(ActivityEditAccountBookItem.root)
    }
}