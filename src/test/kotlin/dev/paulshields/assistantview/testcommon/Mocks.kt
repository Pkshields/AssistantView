package dev.paulshields.assistantview.testcommon

import io.mockk.mockk

/**
 * Simple wee helper function to make all mocks as relaxed as possible without causing accidental false failures
 */
inline fun <reified T : Any> mock() = mockk<T>(relaxUnitFun = true)

inline fun <reified T : Any> relaxedMock() = mockk<T>(relaxed = true)
