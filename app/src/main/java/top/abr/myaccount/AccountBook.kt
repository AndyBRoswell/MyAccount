package top.abr.myaccount

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
        var Details: String = ""                                                        // The details of this item
        var Labels: MutableSet<String> = HashSet()                                      // The labels of this item. These labels are used for quick search.
        var Site: String = ""                                                           // The site (typically bricks and mortar, or websites) where the transaction related to this item happened
        var Account: String = ""                                                        // The account used for this item
        var Time: ZonedDateTime = ZonedDateTime.now()                                   // The time when the transaction related to this item happened
        var OriginalCurrency: Currency = Currency.getInstance(Locale.getDefault())      // The currency used by the transaction of this item
        var OriginalPrice: Double = 0.0                                                 // The nominal price given by the vendor using the specific (original) currency
        var ExchangeRate: Double = 1.0                                                  // The exchange rate from the original currency to the default currency of the account used
    }

    // Main records
    val ItemByID: MutableMap<ItemID, Item> = TreeMap()                                  // ID as primary key for each item
    val DefaultCurrency: MutableMap<AccountID, Currency> = HashMap()                    // Default currency of accounts
    // Extra indices for quick search
    private val IDByAccount: MutableMap<AccountID, IDCollection> = HashMap()            // Index item ID by account.
    private val IDByDateTime: MutableMap<ZonedDateTime, IDCollection> = TreeMap()       // Index item ID by the time of the transaction. Typically for the single purchase of multiple items.
    private val IDByLabel: MutableMap<LabelType, IDCollection> = HashMap()              // Index item ID by label.
}