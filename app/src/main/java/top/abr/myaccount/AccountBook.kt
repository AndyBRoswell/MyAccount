package top.abr.myaccount

import java.util.Currency
import java.time.ZonedDateTime
import java.util.*
import kotlin.collections.HashMap
import kotlin.collections.HashSet

typealias IDType = Long
typealias IDTypeCollection = TreeSet<IDType>
typealias AccountType = String
typealias LabelType = String

open class AccountBook {
    open inner class InvolvedItem {
        var ItemName: String = ""
        var Place: String = ""
        var AccountUsed: String = ""
        var Label: MutableSet<String> = HashSet()
        var Details: String = ""
        var OriginalCurrency: Currency = Currency.getInstance(Locale.getDefault())
        var OriginalPrice: Double = 0.0
        var ExchangeRate: Double = 0.0
    }

    private val ItemByID: MutableMap<IDType, InvolvedItem> = TreeMap()                        // ID as primary key
    private val DefaultCurrency: MutableMap<AccountType, Currency> = HashMap()                // Default currency of accounts
    private val IDByDateTime: MutableMap<ZonedDateTime, IDTypeCollection> = TreeMap()         // ZonedDateTime as index (special label)
    private val IDByLabel: MutableMap<LabelType, IDTypeCollection> = HashMap()                // Label as index
    private val IDByAccount: MutableMap<AccountType, IDTypeCollection> = HashMap()            // Account as index
}