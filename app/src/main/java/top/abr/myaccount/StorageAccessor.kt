package top.abr.myaccount

import com.dslplatform.json.DslJson
import com.dslplatform.json.runtime.Settings

open class StorageAccessor {
    companion object {
        val JSONProcessor: DslJson<Any> = DslJson(Settings.withRuntime<Any>().includeServiceLoader().allowArrayFormat(true))
    }
}