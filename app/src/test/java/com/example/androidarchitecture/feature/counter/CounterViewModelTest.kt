package com.example.androidarchitecture.feature.counter

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Test

class CounterViewModelTest {

    // --- Integration tests: processIntent end-to-end ---

    @Test
    fun `initial state is count 0 with no error`() {
        val vm = CounterViewModel()
        assertEquals(0, vm.state.value.count)
        assertNull(vm.state.value.error)
    }

    @Test
    fun `increment adds one`() {
        val vm = CounterViewModel()
        vm.processIntent(CounterIntent.Increment)
        assertEquals(1, vm.state.value.count)
    }

    @Test
    fun `reset sets count to zero`() {
        val vm = CounterViewModel()
        repeat(5) { vm.processIntent(CounterIntent.Increment) }
        vm.processIntent(CounterIntent.Reset)
        assertEquals(0, vm.state.value.count)
        assertNull(vm.state.value.error)
    }

    @Test
    fun `setCount with negative value shows error and keeps count`() {
        val vm = CounterViewModel()
        vm.processIntent(CounterIntent.Increment)
        vm.processIntent(CounterIntent.SetCount(-1))
        assertEquals(1, vm.state.value.count)
        assertEquals("Negative counts not allowed", vm.state.value.error)
    }

    @Test
    fun `error clears on next valid action`() {
        val vm = CounterViewModel()
        vm.processIntent(CounterIntent.SetCount(-1))
        assertNotNull(vm.state.value.error)
        vm.processIntent(CounterIntent.Increment)
        assertNull(vm.state.value.error)
    }
}
