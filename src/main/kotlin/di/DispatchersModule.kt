package di

import kotlinx.coroutines.Dispatchers
import org.koin.core.qualifier.named
import org.koin.dsl.module

const val IO = "io"
const val UNCONFINED = "default"

val dispatchersModule = module {
    single { Dispatchers.Default }
    single(named(IO)) { Dispatchers.IO }
    single(named(UNCONFINED)) { Dispatchers.Unconfined }
}
