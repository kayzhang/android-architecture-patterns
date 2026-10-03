package com.example.androidarchitecture.feature.counter

import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class CounterModelTest {

    @Test
    fun `initial count is zero`() {
        val model = CounterModel()
        assertEquals(0, model.getCount())
    }

    @Test
    fun `increment adds one`() {
        val model = CounterModel()
        model.increment()
        assertEquals(1, model.getCount())
    }

    @Test
    fun `reset sets count to zero`() {
        val model = CounterModel()
        model.increment()
        model.increment()
        model.reset()
        assertEquals(0, model.getCount())
    }

    @Test
    fun `setCount with positive value succeeds`() {
        val model = CounterModel()
        val result = model.setCount(42)
        assertTrue(result.isSuccess)
        assertEquals(42, model.getCount())
    }

    @Test
    fun `setCount with negative value fails and keeps count`() {
        val model = CounterModel()
        model.increment()
        val result = model.setCount(-1)
        assertTrue(result.isFailure)
        assertEquals(1, model.getCount())
    }
}
