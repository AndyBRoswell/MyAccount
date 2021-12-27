package top.abr.myaccount

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.TextView
import androidx.activity.result.contract.ActivityResultContract
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.size
import androidx.recyclerview.widget.RecyclerView
import java.util.logging.Logger

open class AccountBookAdapter(val ActivityContext: AppCompatActivity, var MAccountBook: AccountBook) : RecyclerView.Adapter<AccountBookAdapter.ItemViewHolder>() {
    open inner class ItemViewHolder(ItemView: View) : RecyclerView.ViewHolder(ItemView) {
        val NameView: TextView = ItemView.findViewById(R.id.AccountBookItemLayoutName)
        val TimeView: TextView = ItemView.findViewById(R.id.AccountBookItemLayoutTime)
        val SiteView: TextView = ItemView.findViewById(R.id.AccountBookItemLayoutSite)
        val AccountView: TextView = ItemView.findViewById(R.id.AccountBookItemLayoutAccount)
        val OriginalPriceView: TextView = ItemView.findViewById(R.id.AccountBookItemLayoutOriginalAmount)
        val PriceView: TextView = ItemView.findViewById(R.id.AccountBookItemLayoutAmount)
        val DetailsView: TextView = ItemView.findViewById(R.id.AccountBookItemLayoutDetails)

        init {
            ItemView.apply { // Add context menu and the corresponding event handlers for the view of each item.
                setOnCreateContextMenuListener { TargetedContextMenu, _, _ ->
                    ActivityContext.menuInflater.inflate(R.menu.account_book_context, TargetedContextMenu)
                    for (i in 0 until TargetedContextMenu.size) {
                        val CurrentMenuItem = TargetedContextMenu.getItem(i)
                        when (CurrentMenuItem.itemId) {
                            R.id.AccountBookContextAdd -> {
                                CurrentMenuItem.setOnMenuItemClickListener {
                                    val EditParams = Bundle().apply {
                                        putString("Mode", "New")
                                    }
                                    EditAccountBookItemActivityLauncher.launch(Pair(EditParams, null))
                                    true
                                }
                            }
                            R.id.AccounBookContextModify -> {
                                CurrentMenuItem.setOnMenuItemClickListener {
                                    val EditParams = Bundle().apply {
                                        putString("Mode", "Edit")
                                    }
                                    val Item = MAccountBook.GetItem(ItemIDArrayForDisplay[layoutPosition])!!
                                    EditAccountBookItemActivityLauncher.launch(Pair(EditParams, Item))
                                    true
                                }
                            }
                            R.id.AccountBookContextDelete -> {
                                CurrentMenuItem.setOnMenuItemClickListener {
                                    DeleteAccountItem(layoutPosition)
                                    true
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    open inner class EditAccountBookItemContract : ActivityResultContract<Pair<Bundle, AccountBook.Item?>, Bundle?>() {
        override fun createIntent(Context: Context, Input: Pair<Bundle, AccountBook.Item?>) =
            Intent(ActivityContext, EditAccountBookItemActivity::class.java).apply {
                val EditParams = Input.first
                putExtra("EditParams", EditParams)
                if (EditParams.getString("Mode") == "Edit") {
                    val Item = Input.second!!
                    val SerializationResult = JSONProcessor.Serialize(Item)
                    putExtra("Item", SerializationResult)
                }
            }

        override fun parseResult(ResultCode: Int, Intent: Intent?): Bundle? {
            if (ResultCode != Activity.RESULT_OK) return null
            return Intent!!.extras!!
        }
    }

    private val ItemIDArrayForDisplay = ArrayList<ItemID>()

    private val EditAccountBookItemActivityLauncher = ActivityContext.registerForActivityResult(EditAccountBookItemContract()) {
        if (it != null) {
            val EditParams = it.getBundle("EditParams")!!
            when (EditParams.getString("Mode")) {
                "New" -> {
                    val NewItem = JSONProcessor.Deserialize(AccountBook.Item::class.java, it.getString("Item")!!)!!
                    MAccountBook.AddItem(NewItem)
                }
                "Edit" -> {
                    
                }
            }
        }
    }

    init {
        for (Entry in MAccountBook.GetItemsByTime()) {
            for (ID in Entry.value) ItemIDArrayForDisplay.add(ID)
        }
        ItemIDArrayForDisplay.reverse()     // Default order: In descending order of time
    }

    override fun onCreateViewHolder(Parent: ViewGroup, ViewType: Int): ItemViewHolder {
        return ItemViewHolder(LayoutInflater.from(Parent.context).inflate(R.layout.account_book_item_layout, Parent, false))
    }

    override fun onBindViewHolder(Holder: ItemViewHolder, Position: Int) {
        val ItemID = ItemIDArrayForDisplay[Position]
        val Item = MAccountBook.GetItem(ItemID)!!
        Holder.apply {
            NameView.text = Item.Name
            TimeView.text = Item.Time.toLocalDateTime().toString()
            SiteView.text = Item.Site
            AccountView.text = Item.Account
            OriginalPriceView.text = Item.OriginalAmount.toString() + ' ' + Item.OriginalCurrency.currencyCode
            DetailsView.text = Item.Details
        }
    }

    override fun getItemCount() = ItemIDArrayForDisplay.size

    fun AddAccountItem(Item: AccountBook.Item) {

    }

    fun DeleteAccountItem(Position: Int) {
        val TargetedItemID = ItemIDArrayForDisplay[Position]
        MAccountBook.DeleteItem(TargetedItemID)
        ItemIDArrayForDisplay.removeAt(Position)
        notifyItemRemoved(Position)
    }
}