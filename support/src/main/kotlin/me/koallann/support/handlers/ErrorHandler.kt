package me.koallann.support.handlers

import me.koallann.support.mvp.BasicView

interface ErrorHandler {

    /**
     * Show a message for the given error.
     *
     * @return true if a message was shown or false otherwise.
     */
    fun showMessageForError(view: BasicView?, error: Throwable): Boolean

}
