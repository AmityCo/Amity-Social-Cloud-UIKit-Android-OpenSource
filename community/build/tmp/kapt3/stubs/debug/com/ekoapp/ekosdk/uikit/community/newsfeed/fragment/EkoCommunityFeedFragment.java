package com.ekoapp.ekosdk.uikit.community.newsfeed.fragment;

import java.lang.System;

@kotlin.Metadata(mv = {1, 1, 16}, bv = {1, 0, 3}, k = 1, d1 = {"\u0000T\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\u0018\u00002\u00020\u0001:\u0001\u001cB\u0005\u00a2\u0006\u0002\u0010\u0002J\b\u0010\b\u001a\u00020\tH\u0002J\b\u0010\n\u001a\u00020\u000bH\u0002J\b\u0010\f\u001a\u00020\tH\u0016J\b\u0010\r\u001a\u00020\u000eH\u0016J\b\u0010\u000f\u001a\u00020\tH\u0002J\b\u0010\u0010\u001a\u00020\u0011H\u0016J \u0010\u0012\u001a\u00020\u000b2\u0006\u0010\u0013\u001a\u00020\u00142\u0006\u0010\u0015\u001a\u00020\u00162\u0006\u0010\u0017\u001a\u00020\u0018H\u0016J\u0012\u0010\u0019\u001a\u00020\u000b2\b\u0010\u001a\u001a\u0004\u0018\u00010\u001bH\u0016R\u0016\u0010\u0003\u001a\n \u0005*\u0004\u0018\u00010\u00040\u0004X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0006\u001a\u00020\u0007X\u0082.\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u001d"}, d2 = {"Lcom/ekoapp/ekosdk/uikit/community/newsfeed/fragment/EkoCommunityFeedFragment;", "Lcom/ekoapp/ekosdk/uikit/community/newsfeed/fragment/EkoBaseFeedFragment;", "()V", "TAG", "", "kotlin.jvm.PlatformType", "mViewModel", "Lcom/ekoapp/ekosdk/uikit/community/newsfeed/viewmodel/EkoCommunityTimelineViewModel;", "getAdminUserEmptyFeed", "Landroid/view/View;", "getCommunityDetails", "", "getEmptyView", "getFeedType", "Lcom/ekoapp/ekosdk/uikit/community/newsfeed/util/EkoTimelineType;", "getOtherUserEmptyFeed", "getViewModel", "Lcom/ekoapp/ekosdk/uikit/community/newsfeed/viewmodel/EkoBaseFeedViewModel;", "onClickUserAvatar", "feed", "Lcom/ekoapp/ekosdk/feed/EkoPost;", "user", "Lcom/ekoapp/ekosdk/user/EkoUser;", "position", "", "onCreate", "savedInstanceState", "Landroid/os/Bundle;", "Builder", "community_debug"})
public final class EkoCommunityFeedFragment extends com.ekoapp.ekosdk.uikit.community.newsfeed.fragment.EkoBaseFeedFragment {
    private final java.lang.String TAG = null;
    private com.ekoapp.ekosdk.uikit.community.newsfeed.viewmodel.EkoCommunityTimelineViewModel mViewModel;
    private java.util.HashMap _$_findViewCache;
    
    @java.lang.Override()
    public void onCreate(@org.jetbrains.annotations.Nullable()
    android.os.Bundle savedInstanceState) {
    }
    
    private final void getCommunityDetails() {
    }
    
    @org.jetbrains.annotations.NotNull()
    @java.lang.Override()
    public com.ekoapp.ekosdk.uikit.community.newsfeed.util.EkoTimelineType getFeedType() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    @java.lang.Override()
    public com.ekoapp.ekosdk.uikit.community.newsfeed.viewmodel.EkoBaseFeedViewModel getViewModel() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    @java.lang.Override()
    public android.view.View getEmptyView() {
        return null;
    }
    
    private final android.view.View getOtherUserEmptyFeed() {
        return null;
    }
    
    private final android.view.View getAdminUserEmptyFeed() {
        return null;
    }
    
    @java.lang.Override()
    public void onClickUserAvatar(@org.jetbrains.annotations.NotNull()
    com.ekoapp.ekosdk.feed.EkoPost feed, @org.jetbrains.annotations.NotNull()
    com.ekoapp.ekosdk.user.EkoUser user, int position) {
    }
    
    public EkoCommunityFeedFragment() {
        super();
    }
    
    @kotlin.Metadata(mv = {1, 1, 16}, bv = {1, 0, 3}, k = 1, d1 = {"\u00002\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0004\u0018\u00002\u00020\u0001B\u0005\u00a2\u0006\u0002\u0010\u0002J\u000e\u0010\u000b\u001a\u00020\f2\u0006\u0010\r\u001a\u00020\u000eJ\u0010\u0010\u0005\u001a\u00020\u00002\b\u0010\u0005\u001a\u0004\u0018\u00010\u0006J\u000e\u0010\u0007\u001a\u00020\u00002\u0006\u0010\u0007\u001a\u00020\bJ\u0010\u0010\u000f\u001a\u00020\u00002\b\u0010\u0010\u001a\u0004\u0018\u00010\u0004J\u000e\u0010\t\u001a\u00020\u00002\u0006\u0010\u0011\u001a\u00020\nR\u0010\u0010\u0003\u001a\u0004\u0018\u00010\u0004X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u0010\u0010\u0005\u001a\u0004\u0018\u00010\u0006X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u0010\u0010\u0007\u001a\u0004\u0018\u00010\bX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u0010\u0010\t\u001a\u0004\u0018\u00010\nX\u0082\u000e\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u0012"}, d2 = {"Lcom/ekoapp/ekosdk/uikit/community/newsfeed/fragment/EkoCommunityFeedFragment$Builder;", "", "()V", "avatarClickListener", "Lcom/ekoapp/ekosdk/uikit/community/newsfeed/listener/IAvatarClickListener;", "community", "Lcom/ekoapp/ekosdk/community/EkoCommunity;", "communityId", "", "postShareClickListener", "Lcom/ekoapp/ekosdk/uikit/feed/settings/IPostShareClickListener;", "build", "Lcom/ekoapp/ekosdk/uikit/community/newsfeed/fragment/EkoCommunityFeedFragment;", "activity", "Landroidx/appcompat/app/AppCompatActivity;", "onClickUserAvatar", "onAvatarClickListener", "onPostShareClickListener", "community_debug"})
    public static final class Builder {
        private java.lang.String communityId;
        private com.ekoapp.ekosdk.community.EkoCommunity community;
        private com.ekoapp.ekosdk.uikit.community.newsfeed.listener.IAvatarClickListener avatarClickListener;
        private com.ekoapp.ekosdk.uikit.feed.settings.IPostShareClickListener postShareClickListener;
        
        @org.jetbrains.annotations.NotNull()
        public final com.ekoapp.ekosdk.uikit.community.newsfeed.fragment.EkoCommunityFeedFragment build(@org.jetbrains.annotations.NotNull()
        androidx.appcompat.app.AppCompatActivity activity) {
            return null;
        }
        
        @org.jetbrains.annotations.NotNull()
        public final com.ekoapp.ekosdk.uikit.community.newsfeed.fragment.EkoCommunityFeedFragment.Builder communityId(@org.jetbrains.annotations.NotNull()
        java.lang.String communityId) {
            return null;
        }
        
        @org.jetbrains.annotations.NotNull()
        public final com.ekoapp.ekosdk.uikit.community.newsfeed.fragment.EkoCommunityFeedFragment.Builder community(@org.jetbrains.annotations.Nullable()
        com.ekoapp.ekosdk.community.EkoCommunity community) {
            return null;
        }
        
        @org.jetbrains.annotations.NotNull()
        public final com.ekoapp.ekosdk.uikit.community.newsfeed.fragment.EkoCommunityFeedFragment.Builder onClickUserAvatar(@org.jetbrains.annotations.Nullable()
        com.ekoapp.ekosdk.uikit.community.newsfeed.listener.IAvatarClickListener onAvatarClickListener) {
            return null;
        }
        
        @org.jetbrains.annotations.NotNull()
        public final com.ekoapp.ekosdk.uikit.community.newsfeed.fragment.EkoCommunityFeedFragment.Builder postShareClickListener(@org.jetbrains.annotations.NotNull()
        com.ekoapp.ekosdk.uikit.feed.settings.IPostShareClickListener onPostShareClickListener) {
            return null;
        }
        
        public Builder() {
            super();
        }
    }
}