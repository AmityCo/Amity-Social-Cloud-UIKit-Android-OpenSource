package com.ekoapp.ekosdk.uikit.community.newsfeed.viewmodel;

import java.lang.System;

@kotlin.Metadata(mv = {1, 1, 16}, bv = {1, 0, 3}, k = 1, d1 = {"\u0000|\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\u0010\u0002\n\u0002\b\u0007\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u000b\n\u0002\b\b\u0018\u00002\u00020\u00012\u00020\u0002B\u0005\u00a2\u0006\u0002\u0010\u0003JJ\u0010#\u001a\b\u0012\u0004\u0012\u00020%0$2\b\u0010&\u001a\u0004\u0018\u00010\'2\u0006\u0010(\u001a\u00020\'2\u0006\u0010)\u001a\u00020\'2\u0006\u0010*\u001a\u00020\'2\f\u0010+\u001a\b\u0012\u0004\u0012\u00020\u001c0,2\f\u0010-\u001a\b\u0012\u0004\u0012\u00020\u001c0,J\u0016\u0010.\u001a\u00020\u001c2\u0006\u0010/\u001a\u00020\u000f2\u0006\u00100\u001a\u00020%J\u000e\u00101\u001a\u0002022\u0006\u00100\u001a\u00020%J\u000e\u00101\u001a\u0002022\u0006\u0010(\u001a\u00020\'J\u000e\u00103\u001a\u0002022\u0006\u0010/\u001a\u00020\u000fJ\u000e\u00104\u001a\u00020\u001c2\u0006\u0010/\u001a\u00020\u000fJ\u000e\u00105\u001a\u0002022\u0006\u0010)\u001a\u00020\'J\u001a\u00106\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020%08072\u0006\u0010)\u001a\u00020\'J\f\u00109\u001a\b\u0012\u0004\u0012\u00020:07J\u0014\u0010;\u001a\b\u0012\u0004\u0012\u00020\u000f072\u0006\u0010<\u001a\u00020\'J\u0006\u0010=\u001a\u00020>J\u0016\u0010?\u001a\u0002022\u0006\u0010@\u001a\u00020>2\u0006\u0010A\u001a\u00020\u000fJ\u000e\u0010B\u001a\u0002022\u0006\u00100\u001a\u00020%J\u000e\u0010C\u001a\u0002022\u0006\u0010/\u001a\u00020\u000fJ\u000e\u0010D\u001a\u0002022\u0006\u00100\u001a\u00020%J\u000e\u0010E\u001a\u0002022\u0006\u0010/\u001a\u00020\u000fR\u001c\u0010\u0004\u001a\u0004\u0018\u00010\u0005X\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\u0006\u0010\u0007\"\u0004\b\b\u0010\tR\u000e\u0010\n\u001a\u00020\u000bX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\f\u001a\u00020\rX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u001c\u0010\u000e\u001a\u0004\u0018\u00010\u000fX\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\u0010\u0010\u0011\"\u0004\b\u0012\u0010\u0013R\u001c\u0010\u0014\u001a\u0004\u0018\u00010\u0015X\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\u0016\u0010\u0017\"\u0004\b\u0018\u0010\u0019R\u001a\u0010\u001a\u001a\b\u0012\u0004\u0012\u00020\u001c0\u001bX\u0096\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\b\u001d\u0010\u001eR\u001a\u0010\u001f\u001a\b\u0012\u0004\u0012\u00020\u001c0\u001bX\u0096\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\b \u0010\u001eR\u001a\u0010!\u001a\b\u0012\u0004\u0012\u00020\u001c0\u001bX\u0096\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\b\"\u0010\u001e\u00a8\u0006F"}, d2 = {"Lcom/ekoapp/ekosdk/uikit/community/newsfeed/viewmodel/EkoPostDetailsViewModel;", "Lcom/ekoapp/ekosdk/uikit/base/EkoBaseViewModel;", "Lcom/ekoapp/ekosdk/uikit/community/newsfeed/listener/IPostShareListener;", "()V", "avatarClickListener", "Lcom/ekoapp/ekosdk/uikit/community/newsfeed/listener/IAvatarClickListener;", "getAvatarClickListener", "()Lcom/ekoapp/ekosdk/uikit/community/newsfeed/listener/IAvatarClickListener;", "setAvatarClickListener", "(Lcom/ekoapp/ekosdk/uikit/community/newsfeed/listener/IAvatarClickListener;)V", "commentRepository", "Lcom/ekoapp/ekosdk/EkoCommentRepository;", "feedRepository", "Lcom/ekoapp/ekosdk/EkoFeedRepository;", "newsFeed", "Lcom/ekoapp/ekosdk/feed/EkoPost;", "getNewsFeed", "()Lcom/ekoapp/ekosdk/feed/EkoPost;", "setNewsFeed", "(Lcom/ekoapp/ekosdk/feed/EkoPost;)V", "postShareClickListener", "Lcom/ekoapp/ekosdk/uikit/feed/settings/IPostShareClickListener;", "getPostShareClickListener", "()Lcom/ekoapp/ekosdk/uikit/feed/settings/IPostShareClickListener;", "setPostShareClickListener", "(Lcom/ekoapp/ekosdk/uikit/feed/settings/IPostShareClickListener;)V", "shareToExternalAppActionRelay", "Lcom/ekoapp/ekosdk/uikit/utils/SingleLiveData;", "", "getShareToExternalAppActionRelay", "()Lcom/ekoapp/ekosdk/uikit/utils/SingleLiveData;", "shareToGroupActionRelay", "getShareToGroupActionRelay", "shareToMyTimelineActionRelay", "getShareToMyTimelineActionRelay", "addComment", "Lio/reactivex/Single;", "Lcom/ekoapp/ekosdk/comment/EkoComment;", "parentId", "", "commentId", "postId", "message", "onSuccess", "Lkotlin/Function0;", "onError", "commentShowMoreActionClicked", "feed", "comment", "deleteComment", "Lio/reactivex/Completable;", "deletePost", "feedShowMoreActionClicked", "fetchPostData", "getComments", "Lio/reactivex/Flowable;", "Landroidx/paging/PagedList;", "getCurrentUser", "Lcom/ekoapp/ekosdk/user/EkoUser;", "getPostDetails", "id", "isReadOnlyPage", "", "postReaction", "liked", "ekoPost", "reportComment", "reportPost", "unreportComment", "unreportPost", "community_debug"})
public final class EkoPostDetailsViewModel extends com.ekoapp.ekosdk.uikit.base.EkoBaseViewModel implements com.ekoapp.ekosdk.uikit.community.newsfeed.listener.IPostShareListener {
    @org.jetbrains.annotations.Nullable()
    private com.ekoapp.ekosdk.feed.EkoPost newsFeed;
    @org.jetbrains.annotations.Nullable()
    private com.ekoapp.ekosdk.uikit.community.newsfeed.listener.IAvatarClickListener avatarClickListener;
    @org.jetbrains.annotations.Nullable()
    private com.ekoapp.ekosdk.uikit.feed.settings.IPostShareClickListener postShareClickListener;
    private final com.ekoapp.ekosdk.EkoFeedRepository feedRepository = null;
    private final com.ekoapp.ekosdk.EkoCommentRepository commentRepository = null;
    @org.jetbrains.annotations.NotNull()
    private final com.ekoapp.ekosdk.uikit.utils.SingleLiveData<kotlin.Unit> shareToMyTimelineActionRelay = null;
    @org.jetbrains.annotations.NotNull()
    private final com.ekoapp.ekosdk.uikit.utils.SingleLiveData<kotlin.Unit> shareToGroupActionRelay = null;
    @org.jetbrains.annotations.NotNull()
    private final com.ekoapp.ekosdk.uikit.utils.SingleLiveData<kotlin.Unit> shareToExternalAppActionRelay = null;
    
    @org.jetbrains.annotations.Nullable()
    public final com.ekoapp.ekosdk.feed.EkoPost getNewsFeed() {
        return null;
    }
    
    public final void setNewsFeed(@org.jetbrains.annotations.Nullable()
    com.ekoapp.ekosdk.feed.EkoPost p0) {
    }
    
    @org.jetbrains.annotations.Nullable()
    public final com.ekoapp.ekosdk.uikit.community.newsfeed.listener.IAvatarClickListener getAvatarClickListener() {
        return null;
    }
    
    public final void setAvatarClickListener(@org.jetbrains.annotations.Nullable()
    com.ekoapp.ekosdk.uikit.community.newsfeed.listener.IAvatarClickListener p0) {
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
    
    @org.jetbrains.annotations.NotNull()
    public final io.reactivex.Flowable<androidx.paging.PagedList<com.ekoapp.ekosdk.comment.EkoComment>> getComments(@org.jetbrains.annotations.NotNull()
    java.lang.String postId) {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final io.reactivex.Flowable<com.ekoapp.ekosdk.feed.EkoPost> getPostDetails(@org.jetbrains.annotations.NotNull()
    java.lang.String id) {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final io.reactivex.Completable deletePost(@org.jetbrains.annotations.NotNull()
    com.ekoapp.ekosdk.feed.EkoPost feed) {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final io.reactivex.Single<com.ekoapp.ekosdk.comment.EkoComment> addComment(@org.jetbrains.annotations.Nullable()
    java.lang.String parentId, @org.jetbrains.annotations.NotNull()
    java.lang.String commentId, @org.jetbrains.annotations.NotNull()
    java.lang.String postId, @org.jetbrains.annotations.NotNull()
    java.lang.String message, @org.jetbrains.annotations.NotNull()
    kotlin.jvm.functions.Function0<kotlin.Unit> onSuccess, @org.jetbrains.annotations.NotNull()
    kotlin.jvm.functions.Function0<kotlin.Unit> onError) {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final io.reactivex.Completable deleteComment(@org.jetbrains.annotations.NotNull()
    java.lang.String commentId) {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final io.reactivex.Completable fetchPostData(@org.jetbrains.annotations.NotNull()
    java.lang.String postId) {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final io.reactivex.Completable deleteComment(@org.jetbrains.annotations.NotNull()
    com.ekoapp.ekosdk.comment.EkoComment comment) {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final io.reactivex.Flowable<com.ekoapp.ekosdk.user.EkoUser> getCurrentUser() {
        return null;
    }
    
    public final void commentShowMoreActionClicked(@org.jetbrains.annotations.NotNull()
    com.ekoapp.ekosdk.feed.EkoPost feed, @org.jetbrains.annotations.NotNull()
    com.ekoapp.ekosdk.comment.EkoComment comment) {
    }
    
    @org.jetbrains.annotations.NotNull()
    public final io.reactivex.Completable postReaction(boolean liked, @org.jetbrains.annotations.NotNull()
    com.ekoapp.ekosdk.feed.EkoPost ekoPost) {
        return null;
    }
    
    public final void feedShowMoreActionClicked(@org.jetbrains.annotations.NotNull()
    com.ekoapp.ekosdk.feed.EkoPost feed) {
    }
    
    public final boolean isReadOnlyPage() {
        return false;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final io.reactivex.Completable reportPost(@org.jetbrains.annotations.NotNull()
    com.ekoapp.ekosdk.feed.EkoPost feed) {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final io.reactivex.Completable unreportPost(@org.jetbrains.annotations.NotNull()
    com.ekoapp.ekosdk.feed.EkoPost feed) {
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
    
    public EkoPostDetailsViewModel() {
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