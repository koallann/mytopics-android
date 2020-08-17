package me.koallann.myagenda.presentation.addtopic

import android.content.Context
import android.content.Intent
import android.view.View
import me.koallann.myagenda.databinding.ActivityAddTopicBinding
import me.koallann.support.ui.BaseActivity

class AddTopicActivity : BaseActivity() {

    companion object {
        fun createIntent(context: Context) = Intent(context, AddTopicActivity::class.java)
    }

    private val binding: ActivityAddTopicBinding by lazy {
        ActivityAddTopicBinding.inflate(layoutInflater)
    }

    override fun getContentView(): View = binding.root

}
