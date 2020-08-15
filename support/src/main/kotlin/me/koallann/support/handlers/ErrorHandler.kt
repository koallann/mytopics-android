package me.koallann.support.handlers

import me.koallann.support.mvp.BasicView

interface ErrorHandler {

    /**
     * Show a message for the given error.
     */
    fun showMessageForError(view: BasicView, error: Throwable)

}
