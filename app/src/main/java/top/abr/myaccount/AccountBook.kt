package top.abr.myaccount

import android.content.res.Resources
import java.util.Currency
import java.time.ZonedDateTime
import java.util.*
import kotlin.collections.HashMap

typealias ItemID = Long
typealias IDCollection = TreeSet<ItemID>
typealias AccountID = Long
typealias LabelType = String

open class AccountBook {
    open class Item {
        var Name: String = ""                                                           // The name of the item
        var Time: ZonedDateTime = ZonedDateTime.now()                                   // The time when the transaction related to this item happened
        var Site: String = ""                                                           // The site (typically bricks and mortar, or websites) where the transaction related to this item happened
        var Account: String = ""                                                        // The account used for this item
        var OriginalCurrency: Currency = Currency.getInstance(Locale.getDefault())      // The currency used by the transaction of this item
        var OriginalPrice: Double = 0.0                                                 // The nominal price given by the vendor using the specific (original) currency
        var ExchangeRate: Double = 1.0                                                  // The exchange rate from the original currency to the default currency of the account used
        var Labels: MutableSet<String> = HashSet()                                      // The labels of this item. These labels are used for quick search.
        var Details: String = ""                                                        // The details of this item

        constructor()

        constructor(Name: String = "", Time: ZonedDateTime = ZonedDateTime.now(), Site: String = "", Account: String = "", OriginalCurrency: Currency = Currency.getInstance(Locale.getDefault()), OriginalPrice: Double = 0.0, ExchangeRate: Double = 1.0, Labels: MutableSet<String> = HashSet(), Details: String = "") {
            if (Name == "" && Labels.isEmpty() && Details == "") {
                throw IllegalArgumentException("Item name, labels and details cannot be empty at the same time in this constructor.")
            }
        }
    }

    // Main records
    private val ItemByID: MutableMap<ItemID, Item> = TreeMap()                          // ID as primary key for each item
    private val DefaultCurrency: MutableMap<AccountID, Currency> = HashMap()            // Default currency of accounts
    // Extra indices for quick search
    private val IDByAccount: MutableMap<AccountID, IDCollection> = HashMap()            // Index item ID by account.
    private val IDByDateTime: MutableMap<ZonedDateTime, IDCollection> = TreeMap()       // Index item ID by the time of the transaction. Typically for the single purchase of multiple items.
    private val IDByLabel: MutableMap<LabelType, IDCollection> = HashMap()              // Index item ID by label.

    private fun GenerateItemID(): ItemID = System.nanoTime()

    fun GetItems(): Map<ItemID, Item> = ItemByID

    fun GetAccountDefaultCurrencies(): Map<AccountID, Currency> = DefaultCurrency
}