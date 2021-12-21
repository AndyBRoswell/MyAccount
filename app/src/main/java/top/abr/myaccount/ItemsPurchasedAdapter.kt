package top.abr.myaccount

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

open class ItemsPurchasedAdapter(): RecyclerView.Adapter<ItemsPurchasedAdapter.ItemPurchasedViewHolder>() {
    open inner class ItemPurchasedViewHolder(ItemView: View): RecyclerView.ViewHolder(ItemView) {

    }

    override fun onCreateViewHolder(Parent: ViewGroup, ViewType: Int): ItemPurchasedViewHolder {
        return ItemPurchasedViewHolder(LayoutInflater.from(Parent.context).inflate(R.layout.item_purchased_layout, Parent, false))
    }

    override fun onBindViewHolder(Holder: ItemPurchasedViewHolder, Position: Int) {

    }

    override fun getItemCount(): Int {

    }
}