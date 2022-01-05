package com.ekoapp.ekosdk.uikit.community.newsfeed.fragment;

import java.lang.System;

@kotlin.Metadata(mv = {1, 1, 16}, bv = {1, 0, 3}, k = 1, d1 = {"\u0000j\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0010\u000b\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\u0018\u00002\u00020\u0001:\u0001)B\u0007\b\u0000\u00a2\u0006\u0002\u0010\u0002J\b\u0010\u0011\u001a\u00020\u0012H\u0016J\b\u0010\u0013\u001a\u00020\u0014H\u0016J\b\u0010\u0015\u001a\u00020\u0016H\u0016J\u0010\u0010\u0017\u001a\u00020\u00182\u0006\u0010\u0019\u001a\u00020\u001aH\u0016J \u0010\u001b\u001a\u00020\u00182\u0006\u0010\u001c\u001a\u00020\u001d2\u0006\u0010\u001e\u001a\u00020\u001f2\u0006\u0010 \u001a\u00020!H\u0016J&\u0010\"\u001a\u0004\u0018\u00010\u00122\u0006\u0010#\u001a\u00020$2\b\u0010%\u001a\u0004\u0018\u00010&2\b\u0010\'\u001a\u0004\u0018\u00010(H\u0016R\u0010\u0010\u0003\u001a\u0004\u0018\u00010\u0004X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u001a\u0010\u0005\u001a\u00020\u0006X\u0086.\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\u0007\u0010\b\"\u0004\b\t\u0010\nR\u001b\u0010\u000b\u001a\u00020\f8BX\u0082\u0084\u0002\u00a2\u0006\f\n\u0004\b\u000f\u0010\u0010\u001a\u0004\b\r\u0010\u000e\u00a8\u0006*"}, d2 = {"Lcom/ekoapp/ekosdk/uikit/community/newsfeed/fragment/EkoGlobalFeedFragment;", "Lcom/ekoapp/ekosdk/uikit/community/newsfeed/fragment/EkoBaseFeedFragment;", "()V", "emptyViewBinding", "Lcom/ekoapp/ekosdk/uikit/community/databinding/AmityViewMyTimelineFeedEmptyBinding;", "mViewModel", "Lcom/ekoapp/ekosdk/uikit/community/newsfeed/viewmodel/EkoGlobalFeedViewModel;", "getMViewModel", "()Lcom/ekoapp/ekosdk/uikit/community/newsfeed/viewmodel/EkoGlobalFeedViewModel;", "setMViewModel", "(Lcom/ekoapp/ekosdk/uikit/community/newsfeed/viewmodel/EkoGlobalFeedViewModel;)V", "newsFeedViewModel", "Lcom/ekoapp/ekosdk/uikit/community/newsfeed/viewmodel/EkoNewsFeedViewModel;", "getNewsFeedViewModel", "()Lcom/ekoapp/ekosdk/uikit/community/newsfeed/viewmodel/EkoNewsFeedViewModel;", "newsFeedViewModel$delegate", "Lkotlin/Lazy;", "getEmptyView", "Landroid/view/View;", "getFeedType", "Lcom/ekoapp/ekosdk/uikit/community/newsfeed/util/EkoTimelineType;", "getViewModel", "Lcom/ekoapp/ekosdk/uikit/community/newsfeed/viewmodel/EkoBaseFeedViewModel;", "handleEmptyList", "", "isListEmpty", "", "onClickUserAvatar", "feed", "Lcom/ekoapp/ekosdk/feed/EkoPost;", "user", "Lcom/ekoapp/ekosdk/user/EkoUser;", "position", "", "onCreateView", "inflater", "Landroid/view/LayoutInflater;", "container", "Landroid/view/ViewGroup;", "savedInstanceState", "Landroid/os/Bundle;", "Builder", "community_debug"})
public final class EkoGlobalFeedFragment extends com.ekoapp.ekosdk.uikit.community.newsfeed.fragment.EkoBaseFeedFragment {
    @org.jetbrains.annotations.NotNull()
    public com.ekoapp.ekosdk.uikit.community.newsfeed.viewmodel.EkoGlobalFeedViewModel mViewModel;
    private final kotlin.Lazy newsFeedViewModel$delegate = null;
    private com.ekoapp.ekosdk.uikit.community.databinding.AmityViewMyTimelineFeedEmptyBinding emptyViewBinding;
    private java.util.HashMap _$_findViewCache;
    
    @org.jetbrains.annotations.NotNull()
    public final com.ekoapp.ekosdk.uikit.community.newsfeed.viewmodel.EkoGlobalFeedViewModel getMViewModel() {
        return null;
    }
    
    public final void setMViewModel(@org.jetbrains.annotations.NotNull()
    com.ekoapp.ekosdk.uikit.community.newsfeed.viewmodel.EkoGlobalFeedViewModel p0) {
    }
    
    private final com.ekoapp.ekosdk.uikit.community.newsfeed.viewmodel.EkoNewsFeedViewModel getNewsFeedViewModel() {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    @java.lang.Override()
    public android.view.View onCreateView(@org.jetbrains.annotations.NotNull()
    android.view.LayoutInflater inflater, @org.jetbrains.annotations.Nullable()
    android.view.ViewGroup container, @org.jetbrains.annotations.Nullable()
    android.os.Bundle savedInstanceState) {
        return null;
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
    
    @java.lang.Override()
    public void handleEmptyList(boolean isListEmpty) {
    }
    
    @java.lang.Override()
    public void onClickUserAvatar(@org.jetbrains.annotations.NotNull()
    com.ekoapp.ekosdk.feed.EkoPost feed, @org.jetbrains.annotations.NotNull()
    com.ekoapp.ekosdk.user.EkoUser user, int position) {
    }
    
    public EkoGlobalFeedFragment() {
        super();
    }
    
    @kotlin.Metadata(mv = {1, 1, 16}, bv = {1, 0, 3}, k = 1, d1 = {"\u00002\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0006\u0018\u00002\u00020\u0001B\u0005\u00a2\u0006\u0002\u0010\u0002J\u000e\u0010\u000b\u001a\u00020\f2\u0006\u0010\r\u001a\u00020\u000eJ\u0010\u0010\u000f\u001a\u00020\u00002\b\u0010\u0010\u001a\u0004\u0018\u00010\u0004J\u0010\u0010\u0005\u001a\u00020\u00002\u0006\u0010\u0011\u001a\u00020\u0006H\u0002J\u0010\u0010\u0007\u001a\u00020\u00002\u0006\u0010\u0012\u001a\u00020\bH\u0002J\u000e\u0010\t\u001a\u00020\u00002\u0006\u0010\u0013\u001a\u00020\nR\u0010\u0010\u0003\u001a\u0004\u0018\u00010\u0004X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u0010\u0010\u0005\u001a\u0004\u0018\u00010\u0006X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u0010\u0010\u0007\u001a\u0004\u0018\u00010\bX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u0010\u0010\t\u001a\u0004\u0018\u00010\nX\u0082\u000e\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u0014"}, d2 = {"Lcom/ekoapp/ekosdk/uikit/community/newsfeed/fragment/EkoGlobalFeedFragment$Builder;", "", "()V", "avatarClickListener", "Lcom/ekoapp/ekosdk/uikit/community/newsfeed/listener/IAvatarClickListener;", "postItemClickListener", "Lcom/ekoapp/ekosdk/uikit/community/newsfeed/listener/IPostItemClickListener;", "postOptionClickListener", "Lcom/ekoapp/ekosdk/uikit/community/newsfeed/listener/IPostOptionClickListener;", "postShareClickListener", "Lcom/ekoapp/ekosdk/uikit/feed/settings/IPostShareClickListener;", "build", "Lcom/ekoapp/ekosdk/uikit/community/newsfeed/fragment/EkoGlobalFeedFragment;", "activity", "Landroidx/appcompat/app/AppCompatActivity;", "onClickUserAvatar", "onAvatarClickListener", "onPostItemClickListener", "onPostOptionClickListener", "onPostShareClickListener", "community_debug"})
    public static final class Builder {
        private com.ekoapp.ekosdk.uikit.community.newsfeed.listener.IPostItemClickListener postItemClickListener;
        private com.ekoapp.ekosdk.uikit.community.newsfeed.listener.IPostOptionClickListener postOptionClickListener;
        private com.ekoapp.ekosdk.uikit.community.newsfeed.listener.IAvatarClickListener avatarClickListener;
        private com.ekoapp.ekosdk.uikit.feed.settings.IPostShareClickListener postShareClickListener;
        
        @org.jetbrains.annotations.NotNull()
        public final com.ekoapp.ekosdk.uikit.community.newsfeed.fragment.EkoGlobalFeedFragment build(@org.jetbrains.annotations.NotNull()
        androidx.appcompat.app.AppCompatActivity activity) {
            return null;
        }
        
        private final com.ekoapp.ekosdk.uikit.community.newsfeed.fragment.EkoGlobalFeedFragment.Builder postItemClickListener(com.ekoapp.ekosdk.uikit.community.newsfeed.listener.IPostItemClickListener onPostItemClickListener) {
            return null;
        }
        
        private final com.ekoapp.ekosdk.uikit.community.newsfeed.fragment.EkoGlobalFeedFragment.Builder postOptionClickListener(com.ekoapp.ekosdk.uikit.community.newsfeed.listener.IPostOptionClickListener onPostOptionClickListener) {
            return null;
        }
        
        @org.jetbrains.annotations.NotNull()
        public final com.ekoapp.ekosdk.uikit.community.newsfeed.fragment.EkoGlobalFeedFragment.Builder onClickUserAvatar(@org.jetbrains.annotations.Nullable()
        com.ekoapp.ekosdk.uikit.community.newsfeed.listener.IAvatarClickListener onAvatarClickListener) {
            return null;
        }
        
        @org.jetbrains.annotations.NotNull()
        public final com.ekoapp.ekosdk.uikit.community.newsfeed.fragment.EkoGlobalFeedFragment.Builder postShareClickListener(@org.jetbrains.annotations.NotNull()
        com.ekoapp.ekosdk.uikit.feed.settings.IPostShareClickListener onPostShareClickListener) {
            return null;
        }
        
        public Builder() {
            super();
        }
    }
}