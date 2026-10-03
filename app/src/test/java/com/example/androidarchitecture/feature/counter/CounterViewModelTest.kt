package com.example.androidarchitecture.feature.counter

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Test

class CounterViewModelTest {

    @Test
    fun `initial state is count 0 with no error`() {
        val vm = CounterViewModel()
        assertEquals(0, vm.count.value)
        assertNull(vm.error.value)
    }

    @Test
    fun `increment adds one`() {
        val vm = CounterViewModel()
        vm.increment()
        assertEquals(1, vm.count.value)
    }

    @Test
    fun `reset sets count to zero`() {
        val vm = CounterViewModel()
        repeat(5) { vm.increment() }
        vm.reset()
        assertEquals(0, vm.count.value)
        assertNull(vm.error.value)
    }

    @Test
    fun `setCount with negative value shows error and keeps count`() {
        val vm = CounterViewModel()
        vm.increment()
        vm.setCount(-1)
        assertEquals(1, vm.count.value)
        assertEquals("Negative counts not allowed", vm.error.value)
    }

    @Test
    fun `error clears on next valid action`() {
        val vm = CounterViewModel()
        vm.setCount(-1)
        assertNotNull(vm.error.value)
        vm.increment()
        assertNull(vm.error.value)
    }
}
