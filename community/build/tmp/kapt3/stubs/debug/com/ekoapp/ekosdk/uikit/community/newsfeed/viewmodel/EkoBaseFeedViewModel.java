package com.ekoapp.ekosdk.uikit.community.newsfeed.viewmodel;

import java.lang.System;

@kotlin.Metadata(mv = {1, 1, 16}, bv = {1, 0, 3}, k = 1, d1 = {"\u0000\\\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\u0010\u0002\n\u0002\b\b\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u000b\n\u0002\b\u0006\b&\u0018\u00002\u00020\u00012\u00020\u0002B\u0005\u00a2\u0006\u0002\u0010\u0003J\u0016\u0010\u001f\u001a\u00020\u00182\u0006\u0010 \u001a\u00020!2\u0006\u0010\"\u001a\u00020#J\u000e\u0010$\u001a\u00020%2\u0006\u0010\"\u001a\u00020#J\u000e\u0010&\u001a\u00020%2\u0006\u0010\'\u001a\u00020!J\u000e\u0010(\u001a\u00020\u00182\u0006\u0010 \u001a\u00020!J\u000e\u0010)\u001a\u00020\u00182\u0006\u0010\'\u001a\u00020!J\u0016\u0010*\u001a\u0010\u0012\n\u0012\b\u0012\u0004\u0012\u00020!0,\u0018\u00010+H&J\u0016\u0010-\u001a\u00020%2\u0006\u0010.\u001a\u00020/2\u0006\u00100\u001a\u00020!J\u000e\u00101\u001a\u00020%2\u0006\u0010\"\u001a\u00020#J\u000e\u00102\u001a\u00020%2\u0006\u0010\'\u001a\u00020!J\u000e\u00103\u001a\u00020%2\u0006\u0010\"\u001a\u00020#J\u000e\u00104\u001a\u00020%2\u0006\u0010\'\u001a\u00020!R\u001c\u0010\u0004\u001a\u0004\u0018\u00010\u0005X\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\u0006\u0010\u0007\"\u0004\b\b\u0010\tR\u001c\u0010\n\u001a\u0004\u0018\u00010\u000bX\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\f\u0010\r\"\u0004\b\u000e\u0010\u000fR\u001c\u0010\u0010\u001a\u0004\u0018\u00010\u0011X\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\u0012\u0010\u0013\"\u0004\b\u0014\u0010\u0015R\u001a\u0010\u0016\u001a\b\u0012\u0004\u0012\u00020\u00180\u0017X\u0096\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0019\u0010\u001aR\u001a\u0010\u001b\u001a\b\u0012\u0004\u0012\u00020\u00180\u0017X\u0096\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\b\u001c\u0010\u001aR\u001a\u0010\u001d\u001a\b\u0012\u0004\u0012\u00020\u00180\u0017X\u0096\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\b\u001e\u0010\u001a\u00a8\u00065"}, d2 = {"Lcom/ekoapp/ekosdk/uikit/community/newsfeed/viewmodel/EkoBaseFeedViewModel;", "Lcom/ekoapp/ekosdk/uikit/base/EkoBaseViewModel;", "Lcom/ekoapp/ekosdk/uikit/community/newsfeed/listener/IPostShareListener;", "()V", "postItemClickListener", "Lcom/ekoapp/ekosdk/uikit/community/newsfeed/listener/IPostItemClickListener;", "getPostItemClickListener", "()Lcom/ekoapp/ekosdk/uikit/community/newsfeed/listener/IPostItemClickListener;", "setPostItemClickListener", "(Lcom/ekoapp/ekosdk/uikit/community/newsfeed/listener/IPostItemClickListener;)V", "postOptionClickListener", "Lcom/ekoapp/ekosdk/uikit/community/newsfeed/listener/IPostOptionClickListener;", "getPostOptionClickListener", "()Lcom/ekoapp/ekosdk/uikit/community/newsfeed/listener/IPostOptionClickListener;", "setPostOptionClickListener", "(Lcom/ekoapp/ekosdk/uikit/community/newsfeed/listener/IPostOptionClickListener;)V", "postShareClickListener", "Lcom/ekoapp/ekosdk/uikit/feed/settings/IPostShareClickListener;", "getPostShareClickListener", "()Lcom/ekoapp/ekosdk/uikit/feed/settings/IPostShareClickListener;", "setPostShareClickListener", "(Lcom/ekoapp/ekosdk/uikit/feed/settings/IPostShareClickListener;)V", "shareToExternalAppActionRelay", "Lcom/ekoapp/ekosdk/uikit/utils/SingleLiveData;", "", "getShareToExternalAppActionRelay", "()Lcom/ekoapp/ekosdk/uikit/utils/SingleLiveData;", "shareToGroupActionRelay", "getShareToGroupActionRelay", "shareToMyTimelineActionRelay", "getShareToMyTimelineActionRelay", "commentShowMoreActionClicked", "feed", "Lcom/ekoapp/ekosdk/feed/EkoPost;", "comment", "Lcom/ekoapp/ekosdk/comment/EkoComment;", "deleteComment", "Lio/reactivex/Completable;", "deletePost", "post", "feedShowMoreActionClicked", "feedShowShareOptionsActionClicked", "getFeed", "Lio/reactivex/Flowable;", "Landroidx/paging/PagedList;", "postReaction", "liked", "", "ekoPost", "reportComment", "reportPost", "unreportComment", "unreportPost", "community_debug"})
public abstract class EkoBaseFeedViewModel extends com.ekoapp.ekosdk.uikit.base.EkoBaseViewModel implements com.ekoapp.ekosdk.uikit.community.newsfeed.listener.IPostShareListener {
    @org.jetbrains.annotations.Nullable()
    private com.ekoapp.ekosdk.uikit.community.newsfeed.listener.IPostOptionClickListener postOptionClickListener;
    @org.jetbrains.annotations.Nullable()
    private com.ekoapp.ekosdk.uikit.community.newsfeed.listener.IPostItemClickListener postItemClickListener;
    @org.jetbrains.annotations.Nullable()
    private com.ekoapp.ekosdk.uikit.feed.settings.IPostShareClickListener postShareClickListener;
    @org.jetbrains.annotations.NotNull()
    private final com.ekoapp.ekosdk.uikit.utils.SingleLiveData<kotlin.Unit> shareToMyTimelineActionRelay = null;
    @org.jetbrains.annotations.NotNull()
    private final com.ekoapp.ekosdk.uikit.utils.SingleLiveData<kotlin.Unit> shareToGroupActionRelay = null;
    @org.jetbrains.annotations.NotNull()
    private final com.ekoapp.ekosdk.uikit.utils.SingleLiveData<kotlin.Unit> shareToExternalAppActionRelay = null;
    
    @org.jetbrains.annotations.Nullable()
    public final com.ekoapp.ekosdk.uikit.community.newsfeed.listener.IPostOptionClickListener getPostOptionClickListener() {
        return null;
    }
    
    public final void setPostOptionClickListener(@org.jetbrains.annotations.Nullable()
    com.ekoapp.ekosdk.uikit.community.newsfeed.listener.IPostOptionClickListener p0) {
    }
    
    @org.jetbrains.annotations.Nullable()
    public final com.ekoapp.ekosdk.uikit.community.newsfeed.listener.IPostItemClickListener getPostItemClickListener() {
        return null;
    }
    
    public final void setPostItemClickListener(@org.jetbrains.annotations.Nullable()
    com.ekoapp.ekosdk.uikit.community.newsfeed.listener.IPostItemClickListener p0) {
    }
    
    @org.jetbrains.annotations.Nullable()
    public final com.ekoapp.ekosdk.uikit.feed.settings.IPostShareClickListener getPostShareClickListener() {
        return null;
    }
    
    public final void setPostShareClickListener(@org.jetbrains.annotations.Nullable()
    com.ekoapp.ekosdk.uikit.feed.settings.IPostShareClickListener p0) {
    }
    
    @org.jetbrains.annotations.NotNull()
    @java.lang.Override()
    public com.ekoapp.ekosdk.uikit.utils.SingleLiveData<kotlin.Unit> getShareToMyTimelineActionRelay() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    @java.lang.Override()
    public com.ekoapp.ekosdk.uikit.utils.SingleLiveData<kotlin.Unit> getShareToGroupActionRelay() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    @java.lang.Override()
    public com.ekoapp.ekosdk.uikit.utils.SingleLiveData<kotlin.Unit> getShareToExternalAppActionRelay() {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public abstract io.reactivex.Flowable<androidx.paging.PagedList<com.ekoapp.ekosdk.feed.EkoPost>> getFeed();
    
    @org.jetbrains.annotations.NotNull()
    public final io.reactivex.Completable deletePost(@org.jetbrains.annotations.NotNull()
    com.ekoapp.ekosdk.feed.EkoPost post) {
        return null;
    }
    
    public final void commentShowMoreActionClicked(@org.jetbrains.annotations.NotNull()
    com.ekoapp.ekosdk.feed.EkoPost feed, @org.jetbrains.annotations.NotNull()
    com.ekoapp.ekosdk.comment.EkoComment comment) {
    }
    
    public final void feedShowShareOptionsActionClicked(@org.jetbrains.annotations.NotNull()
    com.ekoapp.ekosdk.feed.EkoPost post) {
    }
    
    public final void feedShowMoreActionClicked(@org.jetbrains.annotations.NotNull()
    com.ekoapp.ekosdk.feed.EkoPost feed) {
    }
    
    @org.jetbrains.annotations.NotNull()
    public final io.reactivex.Completable deleteComment(@org.jetbrains.annotations.NotNull()
    com.ekoapp.ekosdk.comment.EkoComment comment) {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final io.reactivex.Completable postReaction(boolean liked, @org.jetbrains.annotations.NotNull()
    com.ekoapp.ekosdk.feed.EkoPost ekoPost) {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final io.reactivex.Completable reportPost(@org.jetbrains.annotations.NotNull()
    com.ekoapp.ekosdk.feed.EkoPost post) {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final io.reactivex.Completable unreportPost(@org.jetbrains.annotations.NotNull()
    com.ekoapp.ekosdk.feed.EkoPost post) {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final io.reactivex.Completable reportComment(@org.jetbrains.annotations.NotNull()
    com.ekoapp.ekosdk.comment.EkoComment comment) {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final io.reactivex.Completable unreportComment(@org.jetbrains.annotations.NotNull()
    com.ekoapp.ekosdk.comment.EkoComment comment) {
        return null;
    }
    
    public EkoBaseFeedViewModel() {
        super();
    }
    
    @org.jetbrains.annotations.NotNull()
    public com.ekoapp.ekosdk.uikit.utils.SingleLiveData<kotlin.Unit> observeShareToMyTimelinePage() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public com.ekoapp.ekosdk.uikit.utils.SingleLiveData<kotlin.Unit> observeShareToPage() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public com.ekoapp.ekosdk.uikit.utils.SingleLiveData<kotlin.Unit> observeShareToExternalApp() {
        return null;
    }
    
    public void navigateShareTo(@org.jetbrains.annotations.NotNull()
    com.ekoapp.ekosdk.uikit.community.utils.ShareType type) {
    }
}