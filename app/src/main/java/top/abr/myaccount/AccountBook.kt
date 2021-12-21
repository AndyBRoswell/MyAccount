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
    // Main records
    val ItemByID: MutableMap<ItemID, ItemPurchased> = TreeMap()                         // ID as primary key for each item
    val DefaultCurrency: MutableMap<AccountID, Currency> = HashMap()                    // Default currency of accounts
    // Extra indices for quick search
    private val IDByAccount: MutableMap<AccountID, IDCollection> = HashMap()            // Index item ID by account.
    private val IDByDateTime: MutableMap<ZonedDateTime, IDCollection> = TreeMap()       // Index item ID by the time of the transaction. Typically for the single purchase of multiple items.
    private val IDByLabel: MutableMap<LabelType, IDCollection> = HashMap()              // Index item ID by label.
}