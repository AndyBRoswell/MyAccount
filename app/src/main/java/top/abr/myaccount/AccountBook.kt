package top.abr.myaccount

import java.util.Currency
import java.time.ZonedDateTime
import java.util.*
import kotlin.collections.HashMap
import kotlin.collections.HashSet

typealias ID = Long
typealias IDCollection = TreeSet<ID>
typealias AccountID = Long
typealias LabelType = String

open class AccountBook {
    open inner class InvolvedItem {
        var Name: String = ""                                                           // The name of the item purchased
        var Details: String = ""                                                        // The details of this item
        var Labels: MutableSet<String> = HashSet()                                      // The labels of this item. These labels are used for quick search.
        var Site: String = ""                                                           // The site where this item was purchased
        var Account: String = ""                                                        // The account used to pay for this purchase
        var OriginalCurrency: Currency = Currency.getInstance(Locale.getDefault())      // The currency used by the vendor of this item
        var OriginalPrice: Double = 0.0                                                 // The nominal price given by the vendor using the specific (original) currency
        var ExchangeRate: Double = 1.0                                                  // The exchange rate from the original currency to the default currency of the account used
    }

    private val ItemByID: MutableMap<ID, InvolvedItem> = TreeMap()                      // ID as primary key
    private val DefaultCurrency: MutableMap<AccountID, Currency> = HashMap()            // Default currency of accounts
    private val IDByDateTime: MutableMap<ZonedDateTime, IDCollection> = TreeMap()       // ZonedDateTime as index (special label)
    private val IDByAccount: MutableMap<AccountID, IDCollection> = HashMap()            // Account as index (special label)
    private val IDByLabel: MutableMap<LabelType, IDCollection> = HashMap()              // Label as index
}