package top.abr.myaccount

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatDelegate
import androidx.drawerlayout.widget.DrawerLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import top.abr.myaccount.databinding.ActivityMainBinding
import java.time.ZonedDateTime
import java.util.*

class MainActivity : AppCompatActivity() {
    // Essential vars
    lateinit var InternalFilesDir: String
    lateinit var ExternalFilesDir: String

    // UI components
    lateinit var ActivityMain: ActivityMainBinding
    lateinit var MainDrawerLayout: DrawerLayout
    lateinit var NavDrawerListener: ActionBarDrawerToggle

    lateinit var AccountView: RecyclerView
    lateinit var AccountViewAdapter: AccountBookAdapter

    // Demo data
    val DemoAccountBook = AccountBook().apply {
        AddItems(
            AccountBook.Item(
                Name = "12900K",
                Time = ZonedDateTime.now(),
                Site = "京东",
                Account = "微信",
                OriginalCurrency = Currency.getInstance("CNY"),
                OriginalAmount = 4999.00,
                Details = """
                    Intel Core i9-12900K CPU
                    8P16T @ 3.20 GHz / 5.10 GHz
                    8E8T @ 2.40 GHz / 3.20 GHz
                    L2: 14 MB
                    Pwr: 125 W / 241 W
                    Litro: Intel 7 (10ESF)
                    Mem: 2CH DDR4 3200 (51.2 GB/s), 2CH DDR5 4800 (76.8 GB/s)
                    PCI-E 4.0/5.0 (20 Lanes)
                """.trimIndent()
            ),
            AccountBook.Item(
                Name = "1DX Mark 3",
                Time = ZonedDateTime.now(),
                Site = "Canon Online Store (USA)",
                Account = "PayPal",
                OriginalCurrency = Currency.getInstance("USD"),
                OriginalAmount = 6499.00,
                ExchangeRate = 6.37,
                Labels = HashSet(),
                Details = "Canon 1DX Mark III DSLR Camera, 5.5K@59.94 RAW (1.90:1)"
            ),
            AccountBook.Item(
                Name = "fripSide 24/96",
                Time = ZonedDateTime.now(),
                Site = "OTOTOY",
                Account = "PayPal",
                OriginalCurrency = Currency.getInstance("JPY"),
                OriginalAmount = 9900.00,
                ExchangeRate = 0.056,
                Labels = HashSet(),
                Details = """
                        the very best of fripSide 2009-2020
                        the very best of fripSide -moving ballads-
                    """.trimIndent()
            )
        )
    }

    override fun onCreate(SavedInstanceState: Bundle?) {
        super.onCreate(SavedInstanceState)

        // Load essential vars
        InternalFilesDir = filesDir.toString()
        ExternalFilesDir = if (Environment.getExternalStorageState() == Environment.MEDIA_MOUNTED) getExternalFilesDir(null).toString() else ""

        // Inflate
        ActivityMain = ActivityMainBinding.inflate(layoutInflater)
        setContentView(ActivityMain.root)
        MainDrawerLayout = ActivityMain.MainDrawerLayout.apply {
            NavDrawerListener = ActionBarDrawerToggle(this@MainActivity, this, R.string.open_nav_drawer, R.string.close_nav_drawer)
            addDrawerListener(NavDrawerListener) // Pass the Open and Close toggle for the drawer layout listener to toggle the button
        }
        supportActionBar!!.setDisplayHomeAsUpEnabled(true) // To make the navigation drawer icon always appear on the action bar

        // Theme
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES) // Dark theme as default

        AccountView = ActivityMain.AccountView
        AccountViewAdapter = AccountBookAdapter(this, DemoAccountBook)
        AccountView.adapter = AccountViewAdapter
        AccountView.layoutManager = LinearLayoutManager(this)
    }

    override fun onPostCreate(SavedInstanceState: Bundle?) {
        super.onPostCreate(SavedInstanceState)
        NavDrawerListener.syncState() // Synchronize the indicator with the state of the linked DrawerLayout after onRestoreInstanceState has occurred.
    }

    override fun onCreateOptionsMenu(M: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_options, M)
        return true
    }

    override fun onOptionsItemSelected(SelectedItem: MenuItem): Boolean {
        if (NavDrawerListener.onOptionsItemSelected(SelectedItem)) return true // Open and close the navigation drawer when the ≡ icon is clicked
        when (SelectedItem.itemId) {
            R.id.MainOptionsOrderByTime -> {}
            R.id.MainOptionsAddAccountBookItem -> AccountViewAdapter.OnAccountBookContextAddClicked()
            R.id.MainOptionsClearAccountBookItem -> AccountViewAdapter.ClearAccountItems()
            R.id.MainOptionsReadFromDefIDir -> {}
            R.id.MainOptionsReadFromDefEDir -> {}
            R.id.MainOptionsReadFromCustomDir -> {}
            R.id.MainOptionsSaveToDefIDir -> {}
            R.id.MainOptionsSaveToDefEDir -> {}
            R.id.MainOptionsSaveToCustomDir -> {}
        }
        return super.onOptionsItemSelected(SelectedItem)
    }

    fun OnReadFromDefIDirSelected() {

    }

    fun OnSaveToDefIDirSelected() {

    }
}