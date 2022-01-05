package com.ekoapp.ekosdk.uikit.community.newsfeed.viewmodel;

import java.lang.System;

@kotlin.Metadata(mv = {1, 1, 16}, bv = {1, 0, 3}, k = 1, d1 = {"\u0000,\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\b\u0005\u0018\u00002\u00020\u0001B\u0005\u00a2\u0006\u0002\u0010\u0002R\u001c\u0010\u0003\u001a\u0004\u0018\u00010\u0004X\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\u0005\u0010\u0006\"\u0004\b\u0007\u0010\bR\u0011\u0010\t\u001a\u00020\n\u00a2\u0006\b\n\u0000\u001a\u0004\b\u000b\u0010\fR\u0011\u0010\r\u001a\u00020\n\u00a2\u0006\b\n\u0000\u001a\u0004\b\u000e\u0010\fR\u001c\u0010\u000f\u001a\u0004\u0018\u00010\u0010X\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\u0011\u0010\u0012\"\u0004\b\u0013\u0010\u0014R\u001c\u0010\u0015\u001a\u0004\u0018\u00010\u0016X\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\u0017\u0010\u0018\"\u0004\b\u0019\u0010\u001a\u00a8\u0006\u001b"}, d2 = {"Lcom/ekoapp/ekosdk/uikit/community/newsfeed/viewmodel/EkoNewsFeedViewModel;", "Lcom/ekoapp/ekosdk/uikit/base/EkoBaseViewModel;", "()V", "createPostButtonClickListener", "Lcom/ekoapp/ekosdk/uikit/community/newsfeed/listener/ICreatePostButtonClickListener;", "getCreatePostButtonClickListener", "()Lcom/ekoapp/ekosdk/uikit/community/newsfeed/listener/ICreatePostButtonClickListener;", "setCreatePostButtonClickListener", "(Lcom/ekoapp/ekosdk/uikit/community/newsfeed/listener/ICreatePostButtonClickListener;)V", "emptyCommunityList", "Landroidx/databinding/ObservableBoolean;", "getEmptyCommunityList", "()Landroidx/databinding/ObservableBoolean;", "emptyGlobalFeed", "getEmptyGlobalFeed", "globalFeedFragmentDelegate", "Lcom/ekoapp/ekosdk/uikit/community/home/listener/IGlobalFeedFragmentDelegate;", "getGlobalFeedFragmentDelegate", "()Lcom/ekoapp/ekosdk/uikit/community/home/listener/IGlobalFeedFragmentDelegate;", "setGlobalFeedFragmentDelegate", "(Lcom/ekoapp/ekosdk/uikit/community/home/listener/IGlobalFeedFragmentDelegate;)V", "myCommunityListPreviewFragmentDelegate", "Lcom/ekoapp/ekosdk/uikit/community/home/listener/IMyCommunityListPreviewFragmentDelegate;", "getMyCommunityListPreviewFragmentDelegate", "()Lcom/ekoapp/ekosdk/uikit/community/home/listener/IMyCommunityListPreviewFragmentDelegate;", "setMyCommunityListPreviewFragmentDelegate", "(Lcom/ekoapp/ekosdk/uikit/community/home/listener/IMyCommunityListPreviewFragmentDelegate;)V", "community_debug"})
public final class EkoNewsFeedViewModel extends com.ekoapp.ekosdk.uikit.base.EkoBaseViewModel {
    @org.jetbrains.annotations.Nullable()
    private com.ekoapp.ekosdk.uikit.community.newsfeed.listener.ICreatePostButtonClickListener createPostButtonClickListener;
    @org.jetbrains.annotations.Nullable()
    private com.ekoapp.ekosdk.uikit.community.home.listener.IGlobalFeedFragmentDelegate globalFeedFragmentDelegate;
    @org.jetbrains.annotations.Nullable()
    private com.ekoapp.ekosdk.uikit.community.home.listener.IMyCommunityListPreviewFragmentDelegate myCommunityListPreviewFragmentDelegate;
    @org.jetbrains.annotations.NotNull()
    private final androidx.databinding.ObservableBoolean emptyCommunityList = null;
    @org.jetbrains.annotations.NotNull()
    private final androidx.databinding.ObservableBoolean emptyGlobalFeed = null;
    
    @org.jetbrains.annotations.Nullable()
    public final com.ekoapp.ekosdk.uikit.community.newsfeed.listener.ICreatePostButtonClickListener getCreatePostButtonClickListener() {
        return null;
    }
    
    public final void setCreatePostButtonClickListener(@org.jetbrains.annotations.Nullable()
    com.ekoapp.ekosdk.uikit.community.newsfeed.listener.ICreatePostButtonClickListener p0) {
    }
    
    @org.jetbrains.annotations.Nullable()
    public final com.ekoapp.ekosdk.uikit.community.home.listener.IGlobalFeedFragmentDelegate getGlobalFeedFragmentDelegate() {
        return null;
    }
    
    public final void setGlobalFeedFragmentDelegate(@org.jetbrains.annotations.Nullable()
    com.ekoapp.ekosdk.uikit.community.home.listener.IGlobalFeedFragmentDelegate p0) {
    }
    
    @org.jetbrains.annotations.Nullable()
    public final com.ekoapp.ekosdk.uikit.community.home.listener.IMyCommunityListPreviewFragmentDelegate getMyCommunityListPreviewFragmentDelegate() {
        return null;
    }
    
    public final void setMyCommunityListPreviewFragmentDelegate(@org.jetbrains.annotations.Nullable()
    com.ekoapp.ekosdk.uikit.community.home.listener.IMyCommunityListPreviewFragmentDelegate p0) {
    }
    
    @org.jetbrains.annotations.NotNull()
    public final androidx.databinding.ObservableBoolean getEmptyCommunityList() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final androidx.databinding.ObservableBoolean getEmptyGlobalFeed() {
        return null;
    }
    
    public EkoNewsFeedViewModel() {
        super();
    }
}