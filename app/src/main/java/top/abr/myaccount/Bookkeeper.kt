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

class Bookkeeper {
	open inner class InvolvedItem {
		var ItemName: String
		var Place: String
		var AccountUsed: String
		var Label: HashSet<String>
		var Details: String
		var OriginalCurrency: Currency
		var OriginalPrice: Double
		var ExchangeRate: Double
	}

	companion object {
		private val ItemByID: MutableMap<IDType, InvolvedItem> = TreeMap()
		private val IDByDateTime: TreeMap<ZonedDateTime, IDTypeCollection> = TreeMap()
		private val IDByLabel: HashMap<LabelType, IDTypeCollection> = HashMap()
		private val IDByAccount: HashMap<AccountType, IDTypeCollection> = HashMap()
		private val DefaultCurrency: HashMap<AccountType, Currency> = HashMap()
	}
}