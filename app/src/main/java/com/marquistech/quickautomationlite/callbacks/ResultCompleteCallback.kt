package com.marquistech.quickautomationlite.callbacks

interface ResultCompleteCallback<T> {
    fun onComplete(result: T)
}