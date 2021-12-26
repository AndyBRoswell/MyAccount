package top.abr.myaccount

import com.dslplatform.json.DslJson
import com.dslplatform.json.runtime.Settings

class JSONProcessor {
    companion object {
        private val JSONProcessor: DslJson<Any> = DslJson(Settings.withRuntime<Any>().includeServiceLoader().allowArrayFormat(true))
    }
}