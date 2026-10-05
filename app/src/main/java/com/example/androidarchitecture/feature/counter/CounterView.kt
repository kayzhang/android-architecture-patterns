package com.example.androidarchitecture.feature.counter

/**
 * View interface for the Loader-based MVC variant.
 *
 * The Controller uses this interface instead of referencing CounterActivity
 * directly. This enables re-attaching a new Activity instance after rotation
 * while the Loader keeps the same Controller alive.
 *
 * Note: This is the same approach MVP uses (View interface), but here it's
 * introduced specifically to solve the Loader re-attachment problem, not as
 * the core architectural pattern. The Controller still orchestrates everything.
 */
interface CounterView {
    fun updateCounter(count: Int)
    fun showError(message: String)
    fun clearError()
}
