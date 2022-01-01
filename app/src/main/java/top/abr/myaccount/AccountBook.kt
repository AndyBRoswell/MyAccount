package top.abr.myaccount

import com.dslplatform.json.CompiledJson
import com.dslplatform.json.JsonConverter
import com.dslplatform.json.JsonReader
import com.dslplatform.json.JsonWriter
import java.util.Currency
import java.time.ZonedDateTime
import java.util.*
import kotlin.collections.HashMap

typealias ItemID = Long
typealias IDCollection = TreeSet<ItemID>
typealias AccountID = Long
typealias LabelID = Long
typealias SiteID = Long

@CompiledJson(onUnknown = CompiledJson.Behavior.IGNORE)
open class AccountBook {
    @CompiledJson(onUnknown = CompiledJson.Behavior.IGNORE) // Ignore unknown properties (default for objects) to disallow unknown properties in JSON set it to FAIL which will result in exception instead
    open class Item(
        var Name: String = "",                                                          // The name of the item
        var Time: ZonedDateTime = ZonedDateTime.now(),                                  // The time when the transaction related to this item happened
        var Site: String = "",                                                          // The site (typically bricks and mortar, or websites) where the transaction related to this item happened
        var Account: String = "",                                                       // The account used for this item
        var OriginalCurrency: Currency = Currency.getInstance(Locale.getDefault()),     // The currency used by the transaction of this item
        var OriginalAmount: Double = 0.0,                                               // The amount of this transaction using the specific (original) currency
        var ExchangeRate: Double = 1.0,                                                 // The exchange rate from the original currency to the default currency of the account used
        var Labels: MutableSet<String> = HashSet(),                                     // The labels of this item. These labels are used for quick search.
        var Details: String = ""                                                        // The details of this item
    ) {
        @JsonConverter(target = ZonedDateTime::class)
        object ZonedDateTimeConverter {
            val JSON_READER = JsonReader.ReadObject {
                ZonedDateTime.parse(it.readSimpleString())
            }
            val JSON_WRITER = JsonWriter.WriteObject { JSONWriter, Value: ZonedDateTime? ->
                JSONWriter.writeString(Value.toString())
            }
        }

        @JsonConverter(target = Currency::class)
        object CurrencyConverter {
            val JSON_READER = JsonReader.ReadObject {
                Currency.getInstance(it.readSimpleString())
            }
            val JSON_WRITER = JsonWriter.WriteObject { JSONWriter, Value: Currency? ->
                if (Value != null) JSONWriter.writeString(Value.currencyCode)
                else JSONWriter.writeNull()
            }
        }
        // If enable this constraint, serialization will fail due to unknown reasons. Thus leaving it commented.
//        init {
//            if (Name == "" && Labels.isEmpty() && Details == "") {
//                throw IllegalArgumentException("Item name, labels and details cannot be empty at the same time in this constructor.")
//            }
//        }
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
    private val IDByTime: MutableMap<ZonedDateTime, IDCollection> = TreeMap()           // Index item ID by the time of the transaction. Typically for the single purchase of multiple items.
    private val IDByLabel: MutableMap<LabelID, IDCollection> = TreeMap()                // Index item ID by label.
    private val IDBySite: MutableMap<SiteID, IDCollection> = TreeMap()                  // Index item ID by site.

    private fun GenerateItemID(): ItemID = System.nanoTime()

    fun GetItems(): Map<ItemID, Item> = ItemByID

    fun GetItemsByTime(): Map<ZonedDateTime, IDCollection> = IDByTime

    fun GetItemsByTime(Time: ZonedDateTime) = IDByTime[Time]

    fun GetAccountDefaultCurrencies(): Map<AccountID, Currency> = DefaultCurrency

    fun GetAccountDefaultCurrency(AID: AccountID) = DefaultCurrency[AID]

    fun GetItem(ID: ItemID) = ItemByID[ID]

    /**
     * Add an item and return its ID.
     */
    fun AddItem(Item: Item): ItemID {
        // Main procedure
        val ItemID = GenerateItemID()
        ItemByID[ItemID] = Item

        // TODO: Maintain indices
        IDByTime.putIfAbsent(Item.Time, sortedSetOf(ItemID))
        IDByTime[Item.Time]!!.add(ItemID)
        return ItemID
    }

    /**
     * Add items and return their IDs.
     */
    fun AddItems(vararg Items: Item): Set<ItemID> {
        val IDs = IDCollection()
        for (Item in Items) IDs.add(AddItem(Item))
        return IDs
    }

    /**
     * Delete an item with the specified ID and return the deleted item.
     */
    fun DeleteItem(ID: ItemID): Item? {
        // TODO: Maintain indices
        val Item = ItemByID[ID] ?: return null
        IDByTime.remove(Item.Time)

        // Main procedure
        return ItemByID.remove(ID)
    }

    fun ClearItems() {
        ItemByID.clear()

        // TODO: Clear indices
        IDByTime.clear()
    }
}