package top.abr.myaccount

import java.util.*
import kotlin.collections.HashSet

open class ItemPurchased {
    var Name: String = ""                                                           // The name of the item purchased
    var Details: String = ""                                                        // The details of this item
    var Labels: MutableSet<String> = HashSet()                                      // The labels of this item. These labels are used for quick search.
    var Site: String = ""                                                           // The site (typically bricks and mortar, or websites) where this item was purchased
    var Account: String = ""                                                        // The account used to pay for this purchase
    var OriginalCurrency: Currency = Currency.getInstance(Locale.getDefault())      // The currency used by the vendor of this item
    var OriginalPrice: Double = 0.0                                                 // The nominal price given by the vendor using the specific (original) currency
    var ExchangeRate: Double = 1.0                                                  // The exchange rate from the original currency to the default currency of the account used
}