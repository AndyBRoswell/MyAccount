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
		var ItemName: String = ""
		var Place: String = ""
		var AccountUsed: String = ""
		var Label: MutableSet<String> = HashSet()
		var Details: String = ""
		var OriginalCurrency: Currency = Currency.getInstance(Locale.getDefault())
		var OriginalPrice: Double = 0.0
		var ExchangeRate: Double = 0.0
	}

	companion object {
		private val ItemByID: MutableMap<IDType, InvolvedItem> = TreeMap()
		private val IDByDateTime: MutableMap<ZonedDateTime, IDTypeCollection> = TreeMap()
		private val IDByLabel: MutableMap<LabelType, IDTypeCollection> = HashMap()
		private val IDByAccount: MutableMap<AccountType, IDTypeCollection> = HashMap()
		private val DefaultCurrency: MutableMap<AccountType, Currency> = HashMap()
	}
}