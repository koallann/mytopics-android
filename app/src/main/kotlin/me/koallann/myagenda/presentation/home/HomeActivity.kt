package me.koallann.myagenda.presentation.home

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.google.android.material.tabs.TabLayoutMediator
import me.koallann.myagenda.R
import me.koallann.myagenda.databinding.ActivityHomeBinding
import me.koallann.myagenda.presentation.addtopic.AddTopicActivity

class HomeActivity : AppCompatActivity() {

    companion object {
        fun createIntent(context: Context) = Intent(context, HomeActivity::class.java).apply {
            addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
        }
    }

    private val binding: ActivityHomeBinding by lazy {
        DataBindingUtil.setContentView<ActivityHomeBinding>(this, R.layout.activity_home)
    }
    private val tabLayoutMediator: TabLayoutMediator by lazy {
        TabLayoutMediator(binding.tab, binding.pager) { tab, position ->
            when (position) {
                0 -> tab.setText(R.string.label_open_topics)
                1 -> tab.setText(R.string.label_closed_topics)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupUI()
    }

    private fun setupUI() {
        binding.pager.adapter = HomePagerAdapter(this)
        binding.addTopic.setOnClickListener { startActivity(AddTopicActivity.createIntent(this)) }
        tabLayoutMediator.attach()
    }

}
