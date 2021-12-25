package top.abr.myaccount

import android.view.*
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.size
import androidx.recyclerview.widget.RecyclerView

open class AccountBookAdapter(val ActivityContext: AppCompatActivity, var AccountBook: AccountBook) : RecyclerView.Adapter<AccountBookAdapter.ItemViewHolder>() {
    open inner class ItemViewHolder(ItemView: View) : RecyclerView.ViewHolder(ItemView) {
        val NameView: TextView = ItemView.findViewById(R.id.AccountBookItemLayoutName)
        val TimeView: TextView = ItemView.findViewById(R.id.AccountBookItemLayoutTime)
        val SiteView: TextView = ItemView.findViewById(R.id.AccountBookItemLayoutSite)
        val AccountView: TextView = ItemView.findViewById(R.id.AccountBookItemLayoutAccount)
        val OriginalPriceView: TextView = ItemView.findViewById(R.id.AccountBookItemLayoutOriginalPrice)
        val PriceView: TextView = ItemView.findViewById(R.id.AccountBookItemLayoutPrice)
        val DetailsView: TextView = ItemView.findViewById(R.id.AccountBookItemLayoutDetails)

        init {
            ItemView.apply { // Add context menu and the corresponding event handlers for the view of each item.
                setOnCreateContextMenuListener { TargetedContextMenu, _, _ ->
                    ActivityContext.menuInflater.inflate(R.menu.account_book_context, TargetedContextMenu)
                    for (i in 0 until TargetedContextMenu.size) {
                        val CurrentMenuItem = TargetedContextMenu.getItem(i)
                        when (CurrentMenuItem.title) {
                            ActivityContext.resources.getString(R.string.add_account_book_item) -> {

                            }
                            ActivityContext.resources.getString(R.string.modify_account_book_item) -> {

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

    private val ItemIDArrayForDisplay = ArrayList<ItemID>()

    init {
        for (Entry in AccountBook.GetItemsByTime()) {
            for (ID in Entry.value) ItemIDArrayForDisplay.add(ID)
        }
    }

    override fun onCreateViewHolder(Parent: ViewGroup, ViewType: Int): ItemViewHolder {
        return ItemViewHolder(LayoutInflater.from(Parent.context).inflate(R.layout.account_book_item_layout, Parent, false))
    }

    override fun onBindViewHolder(Holder: ItemViewHolder, Position: Int) {
        val ItemID = ItemIDArrayForDisplay[ItemIDArrayForDisplay.size - 1 - Position] // Display in reverse order (later items are close to the top)
        val Item = AccountBook.GetItem(ItemID)!!
        Holder.apply {
            NameView.text = Item.Name
            TimeView.text = Item.Time.toLocalDateTime().toString()
            SiteView.text = Item.Site
            AccountView.text = Item.Account
            OriginalPriceView.text = Item.OriginalPrice.toString() + ' ' + Item.OriginalCurrency.currencyCode
            DetailsView.text = Item.Details
        }
    }

    override fun getItemCount() = ItemIDArrayForDisplay.size

    fun AddAccountItem(Item: AccountBook.Item) {

    }

    fun DeleteAccountItem(Position: Int) {
        val TargetedItemID = ItemIDArrayForDisplay[Position]
        AccountBook.DeleteItem(TargetedItemID)
        ItemIDArrayForDisplay.removeAt(Position)
        notifyItemRemoved(Position)
    }
}