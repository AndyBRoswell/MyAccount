package top.abr.myaccount

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
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
    lateinit var ActivityMain: ActivityMainBinding
    lateinit var MainDrawerLayout: DrawerLayout
    lateinit var NavDrawerListener: ActionBarDrawerToggle

    lateinit var AccountView: RecyclerView

    override fun onCreate(SavedInstanceState: Bundle?) {
        super.onCreate(SavedInstanceState)

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

        // Demo data
        val A = AccountBook()
        A.apply {
            AddItems(
                AccountBook.Item(
                    Name = "12900K",
                    Site = "京东",
                    Account = "微信",
                    OriginalCurrency = Currency.getInstance("CNY"),
                    OriginalPrice = 4999.00,
                    Details = "Intel Core i9-12900K CPU"
                ),
                AccountBook.Item(
                    Name = "1DX Mark 3",
                    Site = "Canon Online Store (USA)",
                    Account = "PayPal",
                    OriginalCurrency = Currency.getInstance("USD"),
                    OriginalPrice = 6499.00,
                    ExchangeRate = 6.37,
                    Details = "Canon 1DX Mark III DSLR Camera, 5.5K@59.94 RAW (1.90:1)"
                ),
                AccountBook.Item(
                    Name = "fripSide专辑",
                    Site="OTOTOY",
                    Account = "PayPal",
                    OriginalCurrency = Currency.getInstance("JPY"),
                    OriginalPrice = 9900.00,
                    ExchangeRate = 0.056,
                    Details = """
                        the very best of fripSide 2009-2020
                        the very best of fripSide -moving ballads-
                    """.trimIndent()
                )
            )
        }
        AccountView = ActivityMain.AccountView
        AccountView.adapter = AccountBookAdapter(A)
        AccountView.layoutManager = LinearLayoutManager(this)
    }

    override fun onPostCreate(SavedInstanceState: Bundle?) {
        super.onPostCreate(SavedInstanceState)
        NavDrawerListener.syncState() // Synchronize the indicator with the state of the linked DrawerLayout after onRestoreInstanceState has occurred.
    }

    // Open and close the navigation drawer when the ≡ icon is clicked
    override fun onOptionsItemSelected(MenuItem: MenuItem): Boolean {
        if (NavDrawerListener.onOptionsItemSelected(MenuItem)) return true
        return super.onOptionsItemSelected(MenuItem)
    }
}