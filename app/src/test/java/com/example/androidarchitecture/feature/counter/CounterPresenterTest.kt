package com.example.androidarchitecture.feature.counter

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class MockCounterView : CounterView {
    var lastCount: Int? = null
    var lastError: String? = null
    var errorCleared = false

    override fun updateCounter(count: Int) { lastCount = count }
    override fun showError(message: String) { lastError = message; errorCleared = false }
    override fun clearError() { lastError = null; errorCleared = true }

    fun reset() { lastCount = null; lastError = null; errorCleared = false }
}

class CounterPresenterTest {

    private lateinit var view: MockCounterView
    private lateinit var presenter: CounterPresenter

    @Before
    fun setUp() {
        view = MockCounterView()
        presenter = CounterPresenter(CounterModel())
        presenter.attachView(view)
    }

    @Test
    fun `attachView pushes current count to view`() {
        assertEquals(0, view.lastCount)
    }

    @Test
    fun `increment pushes updated count to view`() {
        presenter.onIncrementClicked()
        assertEquals(1, view.lastCount)
        assertTrue(view.errorCleared)
    }

    @Test
    fun `reset pushes zero to view`() {
        repeat(3) { presenter.onIncrementClicked() }
        presenter.onResetClicked()
        assertEquals(0, view.lastCount)
    }

    @Test
    fun `setCount with negative value pushes error to view`() {
        presenter.onSetCountClicked(-1)
        assertEquals("Negative counts not allowed", view.lastError)
    }

    @Test
    fun `error clears on next valid action`() {
        presenter.onSetCountClicked(-1)
        presenter.onIncrementClicked()
        assertNull(view.lastError)
        assertTrue(view.errorCleared)
    }

    @Test
    fun `detached view does not receive updates`() {
        presenter.detachView()
        view.reset()
        presenter.onIncrementClicked()
        assertNull(view.lastCount)
    }
}
