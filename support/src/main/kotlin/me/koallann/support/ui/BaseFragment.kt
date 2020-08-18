package me.koallann.support.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.fragment.app.Fragment
import com.google.android.material.snackbar.Snackbar
import me.koallann.support.R
import me.koallann.support.mvp.BasicView

abstract class BaseFragment : Fragment(), BasicView {

    private lateinit var root: CoordinatorLayout

    private val progressBar: ProgressBar by lazy {
        root.findViewById<ProgressBar>(R.id.progressBar)
    }
    private val content: View by lazy {
        getContentView()
    }

    abstract fun getContentView(): View

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        root = inflater.inflate(R.layout.base, container, false) as CoordinatorLayout
        root.addView(content, CoordinatorLayout.LayoutParams(
            CoordinatorLayout.LayoutParams.MATCH_PARENT,
            CoordinatorLayout.LayoutParams.MATCH_PARENT
        ))
        return root
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
