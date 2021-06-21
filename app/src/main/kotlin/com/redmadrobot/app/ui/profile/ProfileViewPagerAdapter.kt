package com.redmadrobot.app.ui.profile

import androidx.annotation.StringRes
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.redmadrobot.app.R
import com.redmadrobot.app.ui.profile.friends.ProfileFriendsFragment
import com.redmadrobot.app.ui.profile.likes.ProfileLikesFragment
import com.redmadrobot.app.ui.profile.posts.ProfilePostsFragment

class ProfileViewPagerAdapter(
    fragmentManager: FragmentManager,
    lifecycle: Lifecycle,
) : FragmentStateAdapter(fragmentManager, lifecycle) {

    companion object {
        fun getTabText(position: Int) = Slide.values()[position].tabTextResourceId
    }

    override fun getItemCount(): Int = Slide.values().size

    override fun createFragment(position: Int): Fragment {
        return when (Slide.values()[position]) {
            Slide.POSTS -> ProfilePostsFragment()
            Slide.LIKES -> ProfileLikesFragment()
            Slide.FRIENDS -> ProfileFriendsFragment()
        }
    }

    enum class Slide(@StringRes val tabTextResourceId: Int) {
        POSTS(R.string.profile_tab_posts_title),
        LIKES(R.string.profile_tab_likes_title),
        FRIENDS(R.string.profile_tab_friends_title)
    }
}
