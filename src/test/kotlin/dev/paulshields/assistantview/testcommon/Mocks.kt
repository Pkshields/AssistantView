package dev.paulshields.assistantview.testcommon

import io.mockk.mockk

/**
 * Simple wee helper function to make all mocks as relaxed as possible by default without causing accidental false failures
 */
inline fun <reified T : Any> mock() = mockk<T>(relaxed = true)
