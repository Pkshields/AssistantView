package dev.paulshields.assistantview.testcommon

import org.koin.core.module.Module
import org.koin.test.junit5.KoinTestExtension

fun mockKoinApplication(vararg module: Module) = KoinTestExtension.create {
    modules(*module)
}
