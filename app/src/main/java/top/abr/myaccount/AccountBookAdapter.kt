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

open class AccountBookAdapter(val ActivityContext: AppCompatActivity, var AccountData: AccountBook) : RecyclerView.Adapter<AccountBookAdapter.ItemViewHolder>() {
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
                        when (CurrentMenuItem.title) {
                            ActivityContext.resources.getString(R.string.add_account_book_item) -> {
                                CurrentMenuItem.setOnMenuItemClickListener {
                                    val EditParams = Bundle().apply {
                                        putString("Mode", "New")
                                    }
                                    EditAccountBookItemActivityLauncher.launch(Pair(EditParams, AccountData.GetItem(ItemIDArrayForDisplay[layoutPosition])!!))
                                    true
                                }
                            }
                            ActivityContext.resources.getString(R.string.modify_account_book_item) -> {
                                CurrentMenuItem.setOnMenuItemClickListener {
                                    val EditParams = Bundle().apply {
                                        putString("Mode", "Edit")
                                    }
                                    EditAccountBookItemActivityLauncher.launch(Pair(EditParams, AccountData.GetItem(ItemIDArrayForDisplay[layoutPosition])!!))
                                    true
                                }
                            }
                            ActivityContext.resources.getString(R.string.delete_account_book_item) -> {
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

    open inner class EditAccountBookItemContract : ActivityResultContract<Pair<Bundle, AccountBook.Item>, Pair<Bundle, AccountBook.Item>>() {
        override fun createIntent(Context: Context, Input: Pair<Bundle, AccountBook.Item>) =
            Intent(ActivityContext, EditAccountBookItemActivity::class.java).apply {
                val EditParams = Input.first
                putExtra("EditParams", EditParams)
                if (EditParams.getString("Mode") == "Edit") {
                    val Item = Input.second
                    putExtra("Item", JSONProcessor.Serialize(Item))
//                    val ItemParams = Bundle().apply {
//                        putString("Name", Item.Name)
//                        putString("Time", Item.Time.toString())
//                        putString("Site", Item.Site)
//                        putString("Account", Item.Account)
//                        putString("Currency", Item.OriginalCurrency.currencyCode)
//                        putDouble("OriginalAmount", Item.OriginalAmount)
//                        putDouble("ExchangeRate", Item.ExchangeRate)
//                        putString("Details", Item.Details)
//                    }
//                    putExtra("ItemParams", ItemParams)
                }
            }

        override fun parseResult(ResultCode: Int, Intent: Intent?): Pair<Bundle, AccountBook.Item>? {
            if (ResultCode != Activity.RESULT_OK) return null
            val EditParams = Intent!!.extras!!.getBundle("EditParams")!!
            when (EditParams.getString("Mode")) {
                "New" -> {

                }
                "Edit" -> {

                }
            }
            return null
//            val ItemParams = Intent.extras!!.getBundle("ItemParams")!!
//            val Item = AccountBook.Item(
//                Name = ItemParams.getString("Name")!!,
//                Time = ZonedDateTime.parse(ItemParams.getString("Time")),
//                Site = ItemParams.getString("Site")!!,
//                Account = ItemParams.getString("Account")!!,
//
//                )
//            return Pair(EditParams, Item)
        }
    }

    private val ItemIDArrayForDisplay = ArrayList<ItemID>()

    private val EditAccountBookItemActivityLauncher = ActivityContext.registerForActivityResult(EditAccountBookItemContract()) {

    }

    init {
        for (Entry in AccountData.GetItemsByTime()) {
            for (ID in Entry.value) ItemIDArrayForDisplay.add(ID)
        }
    }

    override fun onCreateViewHolder(Parent: ViewGroup, ViewType: Int): ItemViewHolder {
        return ItemViewHolder(LayoutInflater.from(Parent.context).inflate(R.layout.account_book_item_layout, Parent, false))
    }

    override fun onBindViewHolder(Holder: ItemViewHolder, Position: Int) {
        val ItemID = ItemIDArrayForDisplay[ItemIDArrayForDisplay.size - 1 - Position] // Display in reverse order (later items are close to the top)
        val Item = AccountData.GetItem(ItemID)!!
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
        AccountData.DeleteItem(TargetedItemID)
        ItemIDArrayForDisplay.removeAt(Position)
        notifyItemRemoved(Position)
    }
}