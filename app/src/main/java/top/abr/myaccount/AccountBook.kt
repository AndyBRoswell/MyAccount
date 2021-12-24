package top.abr.myaccount

import android.accounts.Account
import java.util.Currency
import java.time.ZonedDateTime
import java.util.*
import kotlin.collections.HashMap

typealias ItemID = Long
typealias IDCollection = TreeSet<ItemID>
typealias AccountID = Long
typealias LabelID = Long
typealias SiteID = Long

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
    // Synonym dict for fields which may have synonyms
    private val AccountSynonym: SynonymDictionary = SynonymDictionary()                 // Synonym dict for accounts
    private val CurrencySynonym: SynonymDictionary = SynonymDictionary()                // Synonym dict for currencies
    private val LabelSynonym: SynonymDictionary = SynonymDictionary()                   // Synonym dict for labels
    private val SiteSynonym: SynonymDictionary = SynonymDictionary()                    // Synonym dict for sites
    // Extra indices for quick search
    private val IDByAccount: MutableMap<AccountID, IDCollection> = TreeMap()            // Index item ID by account.
    private val IDByDateTime: MutableMap<ZonedDateTime, IDCollection> = TreeMap()       // Index item ID by the time of the transaction. Typically for the single purchase of multiple items.
    private val IDByLabel: MutableMap<LabelID, IDCollection> = TreeMap()                // Index item ID by label.
    private val IDBySite: MutableMap<SiteID, IDCollection> = TreeMap()                  // Index item ID by site.

    private fun GenerateItemID(): ItemID = System.nanoTime()

    fun GetItems(): Map<ItemID, Item> = ItemByID

    fun GetAccountDefaultCurrencies(): Map<AccountID, Currency> = DefaultCurrency

    fun AddItem(Item: Item) {
        // Main procedure
        val ItemID = GenerateItemID()
        ItemByID[ItemID] = Item

        // Maintain indices
        var AccountID = AccountSynonym.GetCanonicalID(Item.Account)
        if (AccountID == null) {
            AccountSynonym.Insert(listOf(Item.Account))
            AccountID = AccountSynonym.GetCanonicalID(Item.Account)
        }
        IDByAccount.putIfAbsent(AccountID!!, sortedSetOf(ItemID))
        IDByAccount[AccountID]!!.add(ItemID)

        IDByDateTime.putIfAbsent(Item.Time, sortedSetOf(ItemID))
        IDByDateTime[Item.Time]!!.add(ItemID)

        for (Label in Item.Labels) {
            var LabelID = LabelSynonym.GetCanonicalID(Label)
            if (LabelID == null) {
                
            }
        }
    }
}