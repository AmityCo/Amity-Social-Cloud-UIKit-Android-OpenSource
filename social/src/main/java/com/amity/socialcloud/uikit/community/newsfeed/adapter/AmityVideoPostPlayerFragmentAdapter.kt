package com.amity.socialcloud.uikit.community.newsfeed.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import com.amity.socialcloud.sdk.model.social.post.AmityPost
import com.amity.socialcloud.uikit.community.newsfeed.fragment.AmityVideoPostPlayerFragment

class AmityVideoPostPlayerFragmentAdapter(fm: FragmentManager) :
    FragmentStatePagerAdapter(fm, BEHAVIOR_SET_USER_VISIBLE_HINT) {

    val videoDataList: ArrayList<AmityPost.Data.VIDEO> = arrayListOf()

    override fun getCount(): Int {
        return videoDataList.size
    }

    override fun getItem(position: Int): Fragment {
        val videoData = videoDataList[position]
        return AmityVideoPostPlayerFragment.newInstance(videoData).build()
    }

    fun setItems(newItems: List<AmityPost.Data.VIDEO>) {
        videoDataList.clear()
        videoDataList.addAll(newItems)
        notifyDataSetChanged()
    }
}