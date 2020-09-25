package me.koallann.support.mvp

import java.lang.ref.WeakReference

abstract class Presenter<View>(viewClass: Class<View>) {

    private var viewReference: WeakReference<View>? = null
    protected val view: View?
        get() = viewReference?.get()

    init {
        if (!viewClass.isInterface) {
            throw IllegalArgumentException("The view must be an interface!")
        }
    }

    open fun attachView(view: View) {
        detachView()
        viewReference = WeakReference(view)
    }

    open fun detachView() {
        viewReference?.clear()
    }

}
