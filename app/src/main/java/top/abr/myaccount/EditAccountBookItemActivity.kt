package top.abr.myaccount

import android.app.Activity
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Button
import android.widget.EditText
import top.abr.myaccount.databinding.ActivityEditAccountBookItemBinding

class EditAccountBookItemActivity : AppCompatActivity() {
    // UI components
    lateinit var ActivityEditAccountBookItem: ActivityEditAccountBookItemBinding

    lateinit var NameEdit: EditText
    lateinit var DateTimePickerButton: Button
    lateinit var SiteEdit: EditText
    lateinit var AccountEdit: EditText
    lateinit var OriginalAmountEdit: EditText
    lateinit var OriginalCurrencyEdit: EditText
    lateinit var ExchangeRateEdit: EditText
    lateinit var DetailsEdit: EditText

    override fun onCreate(SavedInstanceState: Bundle?) {
        super.onCreate(SavedInstanceState)

        // Inflate
        ActivityEditAccountBookItem = ActivityEditAccountBookItemBinding.inflate(layoutInflater)
        setContentView(ActivityEditAccountBookItem.root)

        NameEdit = ActivityEditAccountBookItem.ActivityEditAccountBookItemName
        DateTimePickerButton = ActivityEditAccountBookItem.ActivityEditAccountBookItemDateTimePicker
        SiteEdit = ActivityEditAccountBookItem.ActivityEditAccountBookItemSite
        AccountEdit = ActivityEditAccountBookItem.ActivityEditAccountBookItemAccount
        OriginalAmountEdit = ActivityEditAccountBookItem.ActivityEditAccountBookItemOriginalAmount
        OriginalCurrencyEdit = ActivityEditAccountBookItem.ActivityEditAccountBookItemOriginalCurrency
        ExchangeRateEdit = ActivityEditAccountBookItem.ActivityEditAccountBookItemExchangeRate
        DetailsEdit = ActivityEditAccountBookItem.ActivityEditAccountBookItemDetails

        // Load data
        val Params = intent.extras!!
        val EditParams = Params.getBundle("EditParams")!!
        when (EditParams.getString("Mode")) {
            "New" -> {

            }
            "Edit" -> {
                val Item = JSONProcessor.Deserialize(AccountBook.Item::class.java, Params.getString("Item")!!)!!
                NameEdit.setText(Item.Name)
                SiteEdit.setText(Item.Site)
                AccountEdit.setText(Item.Account)
                OriginalAmountEdit.setText(Item.OriginalAmount.toString())
                OriginalCurrencyEdit.setText(Item.OriginalCurrency.toString())
                ExchangeRateEdit.setText(Item.ExchangeRate.toString())
                DetailsEdit.setText(Item.Details)
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

    override fun onOptionsItemSelected(Item: MenuItem) = when (Item.itemId) {
        R.id.ActivityEditAccountBookItemDone -> {
            true
        }
        R.id.ActivityEditAccountBookItemCancel -> {
            onBackPressed()
            true
        }
        else -> super.onOptionsItemSelected(Item)
    }
}