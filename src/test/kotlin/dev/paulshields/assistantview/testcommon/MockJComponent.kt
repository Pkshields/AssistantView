package dev.paulshields.assistantview.testcommon

import javax.swing.JComponent

/*
 * MockK seems to have issues with abstract Java classes. This provides a concrete class for MockK to work with.
 */
class MockJComponent : JComponent()
