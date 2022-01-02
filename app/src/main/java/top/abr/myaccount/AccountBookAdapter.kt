package top.abr.myaccount

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.*
import android.widget.TextView
import androidx.activity.result.contract.ActivityResultContract
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.size
import androidx.recyclerview.widget.RecyclerView
import java.util.*
import kotlin.collections.ArrayList

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
                                    OnAccountBookContextAddClicked()
                                    true
                                }
                            }
                            R.id.AccounBookContextModify -> {
                                CurrentMenuItem.setOnMenuItemClickListener {
                                    OnAccountBookContextModifyClicked(layoutPosition)
                                    true
                                }
                            }
                            R.id.AccountBookContextDelete -> {
                                CurrentMenuItem.setOnMenuItemClickListener {
                                    OnAccountBookContextDeleteClicked(layoutPosition)
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
                    putExtra("OldItem", SerializationResult)
                }
            }

        override fun parseResult(ResultCode: Int, Intent: Intent?): Bundle? {
            if (ResultCode != Activity.RESULT_OK) return null
            return Intent!!.extras!!
        }
    }

    private val EditAccountBookItemActivityLauncher = ActivityContext.registerForActivityResult(EditAccountBookItemContract()) {
        if (it != null) {
            val EditParams = it.getBundle("EditParams")!!
            when (EditParams.getString("Mode")) {
                "New" -> {
                    val NewItem = JSONProcessor.Deserialize(AccountBook.Item::class.java, it.getString("NewItem")!!)!!
                    AddAccountItem(NewItem)
                }
                "Edit" -> {
                    val TargetedPosition = EditParams.getInt("TargetedPosition")
                    val TargetedItemID = EditParams.getLong("TargetedItemID")
                    val ModifiedItem = JSONProcessor.Deserialize(AccountBook.Item::class.java, it.getString("NewItem")!!)!!
                    EditAccountItem(TargetedPosition, TargetedItemID, ModifiedItem)
                }
            }
        }
    }

    private val ItemIDArrayForDisplay = ArrayList<ItemID>()

    private fun Init() {
        for (Entry in MAccountBook.GetItemsByTime()) {
            for (ID in Entry.value) ItemIDArrayForDisplay.add(ID)
        }
        ItemIDArrayForDisplay.reverse()     // Default order: In descending order of time
    }

    init {
        Init()
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

    fun AddAccountItem(I: AccountBook.Item) {
        val ID = MAccountBook.AddItem(I)
        val Pos = -(Collections.binarySearch(ItemIDArrayForDisplay, ID, { ID1, ID2 ->
            val I1 = MAccountBook.GetItem(ID1)!!
            val I2 = MAccountBook.GetItem(ID2)!!
            -I1.Time.compareTo(I2.Time)             // In reverse order of time
        }) + 1)
        ItemIDArrayForDisplay.add(Pos, ID)
        notifyItemInserted(Pos)
    }

    fun EditAccountItem(Position: Int, ID: ItemID, ModifiedItem: AccountBook.Item) {
//        val TargetedItem = MAccountBook.GetItem(ID) ?: return
//        TargetedItem.Name = ModifiedItem.Name
//        TargetedItem.Time = ModifiedItem.Time
//        TargetedItem.Site = ModifiedItem.Site
//        TargetedItem.Account = ModifiedItem.Account
//        TargetedItem.OriginalAmount = ModifiedItem.OriginalAmount
//        TargetedItem.OriginalCurrency = ModifiedItem.OriginalCurrency
//        TargetedItem.Details = ModifiedItem.Details
        MAccountBook.EditItem(ID, ModifiedItem)
        notifyItemChanged(Position)
    }

    fun DeleteAccountItem(Position: Int) {
        val TargetedItemID = ItemIDArrayForDisplay[Position]
        MAccountBook.DeleteItem(TargetedItemID)
        ItemIDArrayForDisplay.removeAt(Position)
        notifyItemRemoved(Position)
    }

    fun ClearAccountItems() {
        val Size = ItemIDArrayForDisplay.size
        ItemIDArrayForDisplay.clear()
        MAccountBook.ClearItems()
        notifyItemRangeRemoved(0, Size)
    }

    fun OnAccountBookContextAddClicked() {
        val EditParams = Bundle().apply {
            putString("Mode", "New")
        }
        EditAccountBookItemActivityLauncher.launch(Pair(EditParams, null))
    }

    fun OnAccountBookContextModifyClicked(TargetedPosition: Int) {
        val TargetedItemID = ItemIDArrayForDisplay[TargetedPosition]
        val EditParams = Bundle().apply {
            putString("Mode", "Edit")
            putInt("TargetedPosition", TargetedPosition)
            putLong("TargetedItemID", TargetedItemID)
        }
        val OldItem = MAccountBook.GetItem(TargetedItemID)!!
        EditAccountBookItemActivityLauncher.launch(Pair(EditParams, OldItem))
    }

    fun OnAccountBookContextDeleteClicked(TargetedPosition: Int) {
        DeleteAccountItem(TargetedPosition)
    }

    fun Refresh() {
        val OriginalSize = ItemIDArrayForDisplay.size
        ItemIDArrayForDisplay.clear()
        notifyItemRangeRemoved(0, OriginalSize) // If this step is skipped, then when the count of items of the account book shrinks, there will be an IndexOutOfBoundException.
        Init()
        notifyItemRangeChanged(0, ItemIDArrayForDisplay.size)
    }
}