package top.abr.myaccount

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Button
import android.widget.EditText
import top.abr.myaccount.databinding.ActivityEditAccountBookItemBinding
import java.time.ZoneId
import java.time.ZonedDateTime
import java.util.*

class EditAccountBookItemActivity : AppCompatActivity() {
    // UI components
    lateinit var ActivityEditAccountBookItem: ActivityEditAccountBookItemBinding

    lateinit var NameEdit: EditText
    lateinit var TimeOffEdit: EditText
    lateinit var TimeYrEdit: EditText
    lateinit var TimeMonEdit: EditText
    lateinit var TimeDayEdit: EditText
    lateinit var TimeHrEdit: EditText
    lateinit var TimeMinEdit: EditText
    lateinit var TimeSecEdit: EditText
    lateinit var DateTimePickerButton: Button
    lateinit var SiteEdit: EditText
    lateinit var AccountEdit: EditText
    lateinit var OriginalAmountEdit: EditText
    lateinit var OriginalCurrencyEdit: EditText
    //    lateinit var ExchangeRateEdit: EditText
    lateinit var DetailsEdit: EditText

    // Result
    val IntentWithResult = Intent()

    override fun onCreate(SavedInstanceState: Bundle?) {
        super.onCreate(SavedInstanceState)

        // Inflate
        ActivityEditAccountBookItem = ActivityEditAccountBookItemBinding.inflate(layoutInflater)
        setContentView(ActivityEditAccountBookItem.root)

        NameEdit = ActivityEditAccountBookItem.ActivityEditAccountBookItemName
        TimeOffEdit = ActivityEditAccountBookItem.ActivityEditAccountBookItemTimeOff
        TimeYrEdit = ActivityEditAccountBookItem.ActivityEditAccountBookItemTimeYr
        TimeMonEdit = ActivityEditAccountBookItem.ActivityEditAccountBookItemTimeMon
        TimeDayEdit = ActivityEditAccountBookItem.ActivityEditAccountBookItemTimeDay
        TimeHrEdit = ActivityEditAccountBookItem.ActivityEditAccountBookItemTimeHr
        TimeMinEdit = ActivityEditAccountBookItem.ActivityEditAccountBookItemTimeMin
        TimeSecEdit = ActivityEditAccountBookItem.ActivityEditAccountBookItemTimeSec
        DateTimePickerButton = ActivityEditAccountBookItem.ActivityEditAccountBookItemDateTimePicker
        SiteEdit = ActivityEditAccountBookItem.ActivityEditAccountBookItemSite
        AccountEdit = ActivityEditAccountBookItem.ActivityEditAccountBookItemAccount
        OriginalAmountEdit = ActivityEditAccountBookItem.ActivityEditAccountBookItemOriginalAmount
        OriginalCurrencyEdit = ActivityEditAccountBookItem.ActivityEditAccountBookItemOriginalCurrency
//        ExchangeRateEdit = ActivityEditAccountBookItem.ActivityEditAccountBookItemExchangeRate
        DetailsEdit = ActivityEditAccountBookItem.ActivityEditAccountBookItemDetails

        // Load data
        val Params = intent.extras!!
        val EditParams = Params.getBundle("EditParams")!!
        when (EditParams.getString("Mode")) {
            "New" -> {

            }
            "Edit" -> {
                val OldItem = JSONProcessor.Deserialize(AccountBook.Item::class.java, Params.getString("OldItem")!!)!!
                NameEdit.setText(OldItem.Name)
                val T = OldItem.Time
                TimeOffEdit.setText(T.offset.toString())
                TimeYrEdit.setText(T.year.toString())
                TimeMonEdit.setText(String.format("%02d", T.monthValue))
                TimeDayEdit.setText(String.format("%02d", T.dayOfMonth))
                TimeHrEdit.setText(String.format("%02d", T.hour))
                TimeMinEdit.setText(String.format("%02d", T.minute))
                TimeSecEdit.setText(String.format("%02d", T.second))
                SiteEdit.setText(OldItem.Site)
                AccountEdit.setText(OldItem.Account)
                OriginalAmountEdit.setText(OldItem.OriginalAmount.toString())
                OriginalCurrencyEdit.setText(OldItem.OriginalCurrency.toString())
//                ExchangeRateEdit.setText(Item.ExchangeRate.toString())
                DetailsEdit.setText(OldItem.Details)
            }
        }
    }

    override fun onBackPressed() {
        setResult(Activity.RESULT_CANCELED)
        finish()
    }

    override fun onCreateOptionsMenu(M: Menu?): Boolean {
        menuInflater.inflate(R.menu.edit_account_book_item_options, M)
        return true
    }

    override fun onOptionsItemSelected(SelectedItem: MenuItem) = when (SelectedItem.itemId) {
        R.id.ActivityEditAccountBookItemDone -> {
            IntentWithResult.apply {
                putExtra("EditParams", intent.getBundleExtra("EditParams"))     // Pass the edit params without modifying them
                val T = ZonedDateTime.of(
                    TimeYrEdit.text.toString().toInt(),
                    TimeMonEdit.text.toString().toInt(),
                    TimeDayEdit.text.toString().toInt(),
                    TimeHrEdit.text.toString().toInt(),
                    TimeMinEdit.text.toString().toInt(),
                    TimeSecEdit.text.toString().toInt(),
                    0,
                    ZoneId.of(TimeOffEdit.text.toString())
                )
                val NewItem = AccountBook.Item(
                    Name = NameEdit.text.toString(),
                    Time = T,
                    Site = SiteEdit.text.toString(),
                    Account = AccountEdit.text.toString(),
                    OriginalCurrency = Currency.getInstance(OriginalCurrencyEdit.text.toString()),
                    OriginalAmount = OriginalAmountEdit.text.toString().toDouble(),
                    Details = DetailsEdit.text.toString()
                )
                putExtra("NewItem", JSONProcessor.Serialize(NewItem))
            }
            setResult(Activity.RESULT_OK, IntentWithResult)
            finish()
            true
        }
        R.id.ActivityEditAccountBookItemCancel -> {
            onBackPressed()
            true
        }
        else -> super.onOptionsItemSelected(SelectedItem)
    }
}