package top.abr.myaccount

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

open class AccountBookAdapter(var AccountBook: AccountBook): RecyclerView.Adapter<AccountBookAdapter.ItemViewHolder>() {
    open inner class ItemViewHolder(ItemView: View): RecyclerView.ViewHolder(ItemView) {
        val NameView: TextView = ItemView.findViewById(R.id.AccountBookItemLayoutName)
        val TimeView: TextView = ItemView.findViewById(R.id.AccountBookItemLayoutTime)
        val SiteView: TextView = ItemView.findViewById(R.id.AccountBookItemLayoutSite)
        val AccountView: TextView = ItemView.findViewById(R.id.AccountBookItemLayoutAccount)
        val OriginalPriceView: TextView = ItemView.findViewById(R.id.AccountBookItemLayoutOriginalPrice)
        val PriceView: TextView = ItemView.findViewById(R.id.AccountBookItemLayoutPrice)
        val DetailsView: TextView = ItemView.findViewById(R.id.AccountBookItemLayoutDetails)
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
        val ItemID = ItemIDArrayForDisplay[Position]
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
}