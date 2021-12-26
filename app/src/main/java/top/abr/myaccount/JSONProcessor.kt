package top.abr.myaccount

import com.dslplatform.json.DslJson
import com.dslplatform.json.runtime.Settings
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream

open class JSONProcessor {
    companion object {
        private val Core: DslJson<Any> = DslJson(Settings.withRuntime<Any>().includeServiceLoader().allowArrayFormat(true))

        fun Serialize(Instance: Any): String {
            val OStream = ByteArrayOutputStream()
            Core.serialize(Instance, OStream)
            return OStream.toString()
        }

        fun <T> Deserialize(Manif: Class<T>, Str: String): T? {
            val IStream = ByteArrayInputStream(Str.toByteArray())
            return Core.deserialize(Manif, IStream)
        }
    }
}