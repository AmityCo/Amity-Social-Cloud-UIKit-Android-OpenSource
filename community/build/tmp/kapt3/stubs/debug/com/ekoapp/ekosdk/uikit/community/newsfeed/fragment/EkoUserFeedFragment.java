package com.ekoapp.ekosdk.uikit.community.newsfeed.fragment;

import java.lang.System;

@kotlin.Metadata(mv = {1, 1, 16}, bv = {1, 0, 3}, k = 1, d1 = {"\u0000F\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\u0018\u00002\u00020\u0001:\u0001\u0016B\u0007\b\u0000\u00a2\u0006\u0002\u0010\u0002J\b\u0010\u0005\u001a\u00020\u0006H\u0016J\b\u0010\u0007\u001a\u00020\bH\u0016J\b\u0010\t\u001a\u00020\nH\u0016J \u0010\u000b\u001a\u00020\f2\u0006\u0010\r\u001a\u00020\u000e2\u0006\u0010\u000f\u001a\u00020\u00102\u0006\u0010\u0011\u001a\u00020\u0012H\u0016J\u0012\u0010\u0013\u001a\u00020\f2\b\u0010\u0014\u001a\u0004\u0018\u00010\u0015H\u0016R\u000e\u0010\u0003\u001a\u00020\u0004X\u0082.\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u0017"}, d2 = {"Lcom/ekoapp/ekosdk/uikit/community/newsfeed/fragment/EkoUserFeedFragment;", "Lcom/ekoapp/ekosdk/uikit/community/newsfeed/fragment/EkoBaseFeedFragment;", "()V", "mViewModel", "Lcom/ekoapp/ekosdk/uikit/community/newsfeed/viewmodel/EkoUserTimelineViewModel;", "getEmptyView", "Landroid/view/View;", "getFeedType", "Lcom/ekoapp/ekosdk/uikit/community/newsfeed/util/EkoTimelineType;", "getViewModel", "Lcom/ekoapp/ekosdk/uikit/community/newsfeed/viewmodel/EkoBaseFeedViewModel;", "onClickUserAvatar", "", "feed", "Lcom/ekoapp/ekosdk/feed/EkoPost;", "user", "Lcom/ekoapp/ekosdk/user/EkoUser;", "position", "", "onCreate", "savedInstanceState", "Landroid/os/Bundle;", "Builder", "community_debug"})
public final class EkoUserFeedFragment extends com.ekoapp.ekosdk.uikit.community.newsfeed.fragment.EkoBaseFeedFragment {
    private com.ekoapp.ekosdk.uikit.community.newsfeed.viewmodel.EkoUserTimelineViewModel mViewModel;
    private java.util.HashMap _$_findViewCache;
    
    @org.jetbrains.annotations.NotNull()
    @java.lang.Override()
    public com.ekoapp.ekosdk.uikit.community.newsfeed.viewmodel.EkoBaseFeedViewModel getViewModel() {
        return null;
    }
    
    @java.lang.Override()
    public void onCreate(@org.jetbrains.annotations.Nullable()
    android.os.Bundle savedInstanceState) {
    }
    
    @org.jetbrains.annotations.NotNull()
    @java.lang.Override()
    public com.ekoapp.ekosdk.uikit.community.newsfeed.util.EkoTimelineType getFeedType() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    @java.lang.Override()
    public android.view.View getEmptyView() {
        return null;
    }
    
    @java.lang.Override()
    public void onClickUserAvatar(@org.jetbrains.annotations.NotNull()
    com.ekoapp.ekosdk.feed.EkoPost feed, @org.jetbrains.annotations.NotNull()
    com.ekoapp.ekosdk.user.EkoUser user, int position) {
    }
    
    public EkoUserFeedFragment() {
        super();
    }
    
    @kotlin.Metadata(mv = {1, 1, 16}, bv = {1, 0, 3}, k = 1, d1 = {"\u00002\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0000\u0018\u00002\u00020\u0001B\u0005\u00a2\u0006\u0002\u0010\u0002J\u000e\u0010\t\u001a\u00020\n2\u0006\u0010\u000b\u001a\u00020\fJ\u0010\u0010\r\u001a\u00020\u00002\b\u0010\u000e\u001a\u0004\u0018\u00010\u0004J\u000e\u0010\u0005\u001a\u00020\u00002\u0006\u0010\u000f\u001a\u00020\u0006J\u000e\u0010\u0010\u001a\u00020\u00002\u0006\u0010\u0010\u001a\u00020\u0011J\u000e\u0010\u0007\u001a\u00020\u00002\u0006\u0010\u0007\u001a\u00020\bR\u0010\u0010\u0003\u001a\u0004\u0018\u00010\u0004X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u0010\u0010\u0005\u001a\u0004\u0018\u00010\u0006X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u0010\u0010\u0007\u001a\u0004\u0018\u00010\bX\u0082\u000e\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u0012"}, d2 = {"Lcom/ekoapp/ekosdk/uikit/community/newsfeed/fragment/EkoUserFeedFragment$Builder;", "", "()V", "avatarClickListener", "Lcom/ekoapp/ekosdk/uikit/community/newsfeed/listener/IAvatarClickListener;", "postShareClickListener", "Lcom/ekoapp/ekosdk/uikit/feed/settings/IPostShareClickListener;", "userId", "", "build", "Lcom/ekoapp/ekosdk/uikit/community/newsfeed/fragment/EkoUserFeedFragment;", "activity", "Landroidx/appcompat/app/AppCompatActivity;", "onClickUserAvatar", "onAvatarClickListener", "onPostShareClickListener", "user", "Lcom/ekoapp/ekosdk/user/EkoUser;", "community_debug"})
    public static final class Builder {
        private java.lang.String userId;
        private com.ekoapp.ekosdk.uikit.community.newsfeed.listener.IAvatarClickListener avatarClickListener;
        private com.ekoapp.ekosdk.uikit.feed.settings.IPostShareClickListener postShareClickListener;
        
        @org.jetbrains.annotations.NotNull()
        public final com.ekoapp.ekosdk.uikit.community.newsfeed.fragment.EkoUserFeedFragment build(@org.jetbrains.annotations.NotNull()
        androidx.appcompat.app.AppCompatActivity activity) {
            return null;
        }
        
        @org.jetbrains.annotations.NotNull()
        public final com.ekoapp.ekosdk.uikit.community.newsfeed.fragment.EkoUserFeedFragment.Builder userId(@org.jetbrains.annotations.NotNull()
        java.lang.String userId) {
            return null;
        }
        
        @org.jetbrains.annotations.NotNull()
        public final com.ekoapp.ekosdk.uikit.community.newsfeed.fragment.EkoUserFeedFragment.Builder user(@org.jetbrains.annotations.NotNull()
        com.ekoapp.ekosdk.user.EkoUser user) {
            return null;
        }
        
        @org.jetbrains.annotations.NotNull()
        public final com.ekoapp.ekosdk.uikit.community.newsfeed.fragment.EkoUserFeedFragment.Builder onClickUserAvatar(@org.jetbrains.annotations.Nullable()
        com.ekoapp.ekosdk.uikit.community.newsfeed.listener.IAvatarClickListener onAvatarClickListener) {
            return null;
        }
        
        @org.jetbrains.annotations.NotNull()
        public final com.ekoapp.ekosdk.uikit.community.newsfeed.fragment.EkoUserFeedFragment.Builder postShareClickListener(@org.jetbrains.annotations.NotNull()
        com.ekoapp.ekosdk.uikit.feed.settings.IPostShareClickListener onPostShareClickListener) {
            return null;
        }
        
        public Builder() {
            super();
        }
    }
}