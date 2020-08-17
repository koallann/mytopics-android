package me.koallann.myagenda.presentation.home

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.google.android.material.tabs.TabLayoutMediator
import me.koallann.myagenda.R
import me.koallann.myagenda.databinding.ActivityHomeBinding
import me.koallann.myagenda.domain.Topic
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
    private val pagerAdapter: HomePagerAdapter by lazy {
        HomePagerAdapter(this)
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
        setupLayout()
    }

    /**
     * Prevent memory leak in Android Q.
     * See: https://issuetracker.google.com/issues/139738913
    */
    override fun onBackPressed() {
        finishAfterTransition()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == AddTopicActivity.REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            val topic = data?.extras?.getParcelable<Topic>(AddTopicActivity.RESULT_EXTRA_TOPIC)
            topic?.let { pagerAdapter.onNewTopic(it) }
        }
    }

    private fun setupLayout() {
        binding.pager.adapter = pagerAdapter
        binding.addTopic.setOnClickListener {
            startActivityForResult(
                AddTopicActivity.createIntent(this),
                AddTopicActivity.REQUEST_CODE
            )
        }
        tabLayoutMediator.attach()
    }

}
