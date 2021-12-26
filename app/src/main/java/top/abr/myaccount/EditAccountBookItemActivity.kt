package top.abr.myaccount

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import top.abr.myaccount.databinding.ActivityEditAccountBookItemBinding

class EditAccountBookItemActivity : AppCompatActivity() {
    // UI components
    lateinit var ActivityEditAccountBookItem: ActivityEditAccountBookItemBinding

    lateinit var NameEdit: EditText
    lateinit var TimeEdit: EditText
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
        TimeEdit = ActivityEditAccountBookItem.ActivityEditAccountBookItemTime
        DateTimePickerButton = ActivityEditAccountBookItem.ActivityEditAccountBookItemDateTimePicker
        SiteEdit = ActivityEditAccountBookItem.ActivityEditAccountBookItemSite
        AccountEdit = ActivityEditAccountBookItem.ActivityEditAccountBookItemAccount
        OriginalAmountEdit = ActivityEditAccountBookItem.ActivityEditAccountBookItemOriginalAmount
        OriginalCurrencyEdit = ActivityEditAccountBookItem.ActivityEditAccountBookItemOriginalCurrency
        ExchangeRateEdit = ActivityEditAccountBookItem.ActivityEditAccountBookItemExchangeRate
        DetailsEdit = ActivityEditAccountBookItem.ActivityEditAccountBookItemDetails

        val EditParams = intent.extras!!.getBundle("EditParams")!!
        when (EditParams.getString("Mode")) {
            "New" -> {

            }
            "Edit" -> {

            }
        }
    }
}