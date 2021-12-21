package top.abr.myaccount

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

open class ItemsPurchasedAdapter(): RecyclerView.Adapter<ItemsPurchasedAdapter.ItemPurchasedViewHolder>() {
    open inner class ItemPurchasedViewHolder(ItemView: View): RecyclerView.ViewHolder(ItemView) {
        val NameView: TextView = ItemView.findViewById(R.id.ItemPurchasedLayoutName)
        val DateTimeView: TextView = ItemView.findViewById(R.id.ItemPurchasedLayoutDateTime)
        val SiteView: TextView = ItemView.findViewById(R.id.ItemPurchasedLayoutSite)
        val AccountView: TextView = ItemView.findViewById(R.id.ItemPurchasedLayoutAccount)
        val OriginalPriceView: TextView = ItemView.findViewById(R.id.ItemPurchasedLayoutOriginalPrice)
        val PriceView: TextView = ItemView.findViewById(R.id.ItemPurchasedLayoutPrice)
    }

    override fun onCreateViewHolder(Parent: ViewGroup, ViewType: Int): ItemPurchasedViewHolder {
        return ItemPurchasedViewHolder(LayoutInflater.from(Parent.context).inflate(R.layout.item_purchased_layout, Parent, false))
    }

    override fun onBindViewHolder(Holder: ItemPurchasedViewHolder, Position: Int) {

    }

    override fun getItemCount(): Int {

    }
}