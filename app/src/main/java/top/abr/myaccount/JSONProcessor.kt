package top.abr.myaccount

import com.dslplatform.json.DslJson
import com.dslplatform.json.runtime.Settings
import java.io.ByteArrayOutputStream

class JSONProcessor {
    companion object {
        private val Core: DslJson<Any> = DslJson(Settings.withRuntime<Any>().includeServiceLoader().allowArrayFormat(true))

        fun Serialize(Instance: Any): String {
            val ostream = ByteArrayOutputStream()
            Core.serialize(Instance, ostream)
            return ostream.toString()
        }
    }
}