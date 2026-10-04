package com.example.androidarchitecture.feature.counter

/**
 * View interface — the core of what makes MVP different from MVC.
 *
 * In MVC, the Controller holds a concrete Activity reference (CounterActivity).
 * In MVP, the Presenter holds this INTERFACE instead. This means:
 * - Presenter doesn't know it's talking to an Activity (or anything Android-specific)
 * - You can substitute a mock/fake implementation for unit testing
 * - The Presenter is fully testable without the Android runtime
 */
interface CounterView {
    fun updateCounter(count: Int)
    fun showError(message: String)
    fun clearError()
}
