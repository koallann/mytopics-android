package me.koallann.support.ui

import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import androidx.appcompat.app.AppCompatActivity
import androidx.coordinatorlayout.widget.CoordinatorLayout
import com.google.android.material.snackbar.Snackbar
import me.koallann.support.R
import me.koallann.support.mvp.BasicView

abstract class BaseActivity : AppCompatActivity(), BasicView {

    private val root: CoordinatorLayout by lazy {
        findViewById<CoordinatorLayout>(R.id.root)
    }
    private val progressBar: ProgressBar by lazy {
        root.findViewById<ProgressBar>(R.id.progressBar)
    }
    private val content: View by lazy {
        getContentView()
    }

    abstract fun getContentView(): View

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        super.setContentView(R.layout.base)

        root.addView(content, CoordinatorLayout.LayoutParams(
            CoordinatorLayout.LayoutParams.MATCH_PARENT,
            CoordinatorLayout.LayoutParams.MATCH_PARENT
        ))
    }

    override fun setContentView(view: View?) {
        throw IllegalStateException("You must implement the getContentView abstract method!")
    }

    override fun setContentView(layoutResID: Int) {
        throw IllegalStateException("You must implement the getContentView abstract method!")
    }

    override fun setContentView(view: View?, params: ViewGroup.LayoutParams?) {
        throw IllegalStateException("You must implement the getContentView abstract method!")
    }

    override fun showLoading() {
        content.visibility = View.INVISIBLE
        progressBar.visibility = View.VISIBLE
    }

    override fun dismissLoading() {
        progressBar.visibility = View.INVISIBLE
        content.visibility = View.VISIBLE
    }

    override fun showMessage(message: String) {
        Snackbar.make(root, message, Snackbar.LENGTH_SHORT).show()
    }

    override fun showMessage(messageRes: Int) {
        showMessage(getString(messageRes))
    }

}
