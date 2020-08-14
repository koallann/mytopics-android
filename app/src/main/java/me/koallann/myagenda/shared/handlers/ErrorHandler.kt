package me.koallann.myagenda.shared.handlers

interface ErrorHandler {

    /**
     * Show a message for the given error.
     *
     * @return true if a message was shown or false otherwise.
     */
    fun showMessageForError(error: Throwable): Boolean

}
