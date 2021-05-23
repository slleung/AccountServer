package di

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestCoroutineDispatcher
import org.koin.core.qualifier.named
import org.koin.dsl.module

const val DEFAULT = "default"
const val IO = "io"
const val UNCONFINED = "default"

@ExperimentalCoroutinesApi
private val testCoroutineDispatcher = TestCoroutineDispatcher()

@ExperimentalCoroutinesApi
val dispatchersModule = module {
    single<CoroutineDispatcher>(named(DEFAULT), override = true) { testCoroutineDispatcher }
    single<CoroutineDispatcher>(named(IO), override = true) { testCoroutineDispatcher }
    single<CoroutineDispatcher>(named(UNCONFINED), override = true) { testCoroutineDispatcher }
}
