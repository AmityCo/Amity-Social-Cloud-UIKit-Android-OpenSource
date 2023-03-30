package com.amity.socialcloud.uikit.community.newsfeed.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager.widget.ViewPager
import com.amity.socialcloud.uikit.community.R
import com.amity.socialcloud.uikit.community.databinding.AmityActivityVideoPreviewBinding
import com.amity.socialcloud.uikit.community.newsfeed.adapter.AmityVideoPostPlayerFragmentAdapter
import com.amity.socialcloud.uikit.community.newsfeed.viewmodel.AmityVideoPostPlayerViewModel
import com.ekoapp.rxlifecycle.extension.untilLifecycleEnd
import com.trello.rxlifecycle4.components.support.RxAppCompatActivity

class AmityVideoPostPlayerActivity : RxAppCompatActivity() {

    private val binding by lazy {
        AmityActivityVideoPreviewBinding.inflate(layoutInflater)
    }

    private lateinit var viewModel: AmityVideoPostPlayerViewModel
    private lateinit var videoFragmentAdapter: AmityVideoPostPlayerFragmentAdapter
    private lateinit var pageChangeCallback: ViewPager.OnPageChangeListener

    companion object {

        fun newIntent(
            context: Context,
            parentPostId: String,
            imagePosition: Int
        ): Intent {
            val intent = Intent(context, AmityVideoPostPlayerActivity::class.java)
            intent.putExtra(EXTRA_PARENT_POST_ID, parentPostId)
            intent.putExtra(EXTRA_VIDEO_POSITION, imagePosition)
            return intent
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(this).get(AmityVideoPostPlayerViewModel::class.java)
        intent.getStringExtra(EXTRA_PARENT_POST_ID)?.let { viewModel.postId = it }
        viewModel.videoPos = intent.getIntExtra(EXTRA_VIDEO_POSITION, 0)
        window.statusBarColor = ContextCompat.getColor(this, R.color.amityColorSecondary)
        setContentView(binding.root)
        initToolbar()
        initViewPager()
        getVideoData()
    }


    private fun initViewPager() {
        videoFragmentAdapter = AmityVideoPostPlayerFragmentAdapter(supportFragmentManager)
        pageChangeCallback = object : ViewPager.OnPageChangeListener {
            override fun onPageScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int
            ) {
            }

            override fun onPageSelected(position: Int) {
                setToolbarTitle(position)
            }

            override fun onPageScrollStateChanged(state: Int) {
            }
        }
        binding.videoViewPages.adapter = videoFragmentAdapter
    }

    private fun getVideoData() {
        viewModel.getVideoData { videoDataList ->
            videoFragmentAdapter.setItems(videoDataList)
            binding.videoViewPages.setCurrentItem(viewModel.videoPos, false)
            setToolbarTitle(viewModel.videoPos)
        }
            .untilLifecycleEnd(this)
            .subscribe()
    }

    override fun onResume() {
        super.onResume()
        binding.videoViewPages.addOnPageChangeListener(pageChangeCallback)
    }

    override fun onPause() {
        super.onPause()
        binding.videoViewPages.removeOnPageChangeListener(pageChangeCallback)
    }

    private fun initToolbar() {
        setSupportActionBar(binding.toolbar)
        supportActionBar?.setHomeAsUpIndicator(R.drawable.amity_ic_close)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        supportActionBar?.title = ""
    }

    private fun setToolbarTitle(position: Int) {
        supportActionBar?.title =
            String.format(
                getString(
                    R.string.amity_image_preview_title,
                    position + 1,
                    viewModel.videoDataList.size
                )
            )
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home)
            finish()
        return super.onOptionsItemSelected(item)
    }
}

private const val EXTRA_PARENT_POST_ID = "EXTRA_PARENT_POST_ID"
private const val EXTRA_VIDEO_POSITION = "EXTRA_VIDEO_POSITION"