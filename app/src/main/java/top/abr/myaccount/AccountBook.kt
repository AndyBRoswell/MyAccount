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
    private val ItemByID: MutableMap<ID, ItemPurchased> = TreeMap()                     // ID as primary key for each item purchased
    private val DefaultCurrency: MutableMap<AccountID, Currency> = HashMap()            // Default currency of accounts
    private val IDByDateTime: MutableMap<ZonedDateTime, IDCollection> = TreeMap()       // Index ID by purchase time. Typically for the single purchase of multiple items
    private val IDByAccount: MutableMap<AccountID, IDCollection> = HashMap()            // Account as index (special label)
    private val IDByLabel: MutableMap<LabelType, IDCollection> = HashMap()              // Label as index
}