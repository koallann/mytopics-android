package me.koallann.support.ui

import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.coordinatorlayout.widget.CoordinatorLayout
import com.google.android.material.snackbar.Snackbar
import me.koallann.support.R
import me.koallann.support.mvp.BasicView

abstract class BaseActivity : AppCompatActivity(), BasicView {

    private val root: CoordinatorLayout by lazy {
        findViewById<CoordinatorLayout>(R.id.root)
    }
    private val content: View by lazy {
        getContentView()
    }

    abstract fun getContentView(): View

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        super.setContentView(R.layout.base)

        supportActionBar?.let {
            it.setDisplayShowHomeEnabled(true)
            it.setDisplayHomeAsUpEnabled(true)
        }
        root.addView(content, CoordinatorLayout.LayoutParams(
            CoordinatorLayout.LayoutParams.MATCH_PARENT,
            CoordinatorLayout.LayoutParams.MATCH_PARENT
        ))
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
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
    }

    override fun dismissLoading() {
    }

    override fun showMessage(message: String) {
        Snackbar.make(root, message, Snackbar.LENGTH_SHORT).show()
    }

    override fun showMessage(messageRes: Int) {
        showMessage(getString(messageRes))
    }

}
