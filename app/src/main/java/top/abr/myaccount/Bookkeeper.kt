package top.abr.myaccount

import java.util.*
import kotlin.collections.HashSet

class Bookkeeper {
	inner class InvolvedItem {
		var Place: String
		var ItemName: String
		var InvolvedAccount: String
		var Amount: Double
		var Label: HashSet<String>
		var Details: String
	}

	companion object {
		private val ItemByID: MutableMap<Long, InvolvedItem> = TreeMap()
		private val 
	}
}