package com.ekoapp.ekosdk.uikit.community.views.newsfeed;

import java.lang.System;

@kotlin.Metadata(mv = {1, 1, 16}, bv = {1, 0, 3}, k = 1, d1 = {"\u0000\u0080\u0001\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\b\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u000b\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0019\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0004\u0018\u00002\u00020\u0001B\u000f\b\u0016\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\u0002\u0010\u0004B\u0017\b\u0016\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0005\u001a\u00020\u0006\u00a2\u0006\u0002\u0010\u0007B\u001f\b\u0016\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0005\u001a\u00020\u0006\u0012\u0006\u0010\b\u001a\u00020\t\u00a2\u0006\u0002\u0010\nJ\b\u0010%\u001a\u00020&H\u0002J\b\u0010\'\u001a\u00020&H\u0002J\u0010\u0010(\u001a\u00020\u001d2\u0006\u0010)\u001a\u00020*H\u0002J\u0010\u0010+\u001a\u00020&2\u0006\u0010,\u001a\u00020\u001dH\u0002J.\u0010-\u001a\u00020&2\b\u0010.\u001a\u0004\u0018\u00010\f2\b\u0010 \u001a\u0004\u0018\u00010!2\b\u0010\"\u001a\u0004\u0018\u00010#2\b\u0010\r\u001a\u0004\u0018\u00010\u000eJ\u000e\u0010/\u001a\u00020&2\u0006\u0010\r\u001a\u00020\u000eJ\u000e\u00100\u001a\u00020&2\u0006\u0010\u0011\u001a\u00020\u0012J\u0010\u00101\u001a\u00020&2\b\u0010\u001e\u001a\u0004\u0018\u00010\u001fJ\u0010\u00102\u001a\u00020&2\b\u0010.\u001a\u0004\u0018\u00010\fJ\b\u00103\u001a\u00020&H\u0002J\u0010\u00104\u001a\u00020&2\u0006\u00105\u001a\u00020*H\u0002J\u0010\u00106\u001a\u00020&2\u0006\u00107\u001a\u00020\tH\u0002J\u0010\u00108\u001a\u00020&2\u0006\u00109\u001a\u00020\tH\u0002J\u000e\u0010:\u001a\u00020&2\u0006\u0010)\u001a\u00020*J\u0010\u0010;\u001a\u00020&2\b\u0010\u000f\u001a\u0004\u0018\u00010\u0010J\u0010\u0010<\u001a\u00020&2\b\u0010 \u001a\u0004\u0018\u00010!J\u0010\u0010=\u001a\u00020&2\b\u0010\"\u001a\u0004\u0018\u00010#J\u000e\u0010>\u001a\u00020&2\u0006\u0010$\u001a\u00020\u001dJ\u0010\u0010?\u001a\u00020&2\u0006\u00105\u001a\u00020*H\u0002J\u000e\u0010@\u001a\u00020&2\u0006\u0010A\u001a\u00020\u001dJ \u0010B\u001a\u00020&2\u000e\u0010C\u001a\n\u0012\u0004\u0012\u00020E\u0018\u00010D2\b\b\u0002\u0010F\u001a\u00020\u001dJ\u000e\u0010G\u001a\u00020&2\u0006\u0010H\u001a\u00020ER\u0010\u0010\u000b\u001a\u0004\u0018\u00010\fX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u0010\u0010\r\u001a\u0004\u0018\u00010\u000eX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u0010\u0010\u000f\u001a\u0004\u0018\u00010\u0010X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u001c\u0010\u0011\u001a\u0004\u0018\u00010\u0012X\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\u0013\u0010\u0014\"\u0004\b\u0015\u0010\u0016R\u000e\u0010\u0017\u001a\u00020\u0018X\u0082.\u00a2\u0006\u0002\n\u0000R\u0010\u0010\u0019\u001a\u0004\u0018\u00010\u001aX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u001b\u001a\u00020\u0010X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u001c\u001a\u00020\u001dX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u0010\u0010\u001e\u001a\u0004\u0018\u00010\u001fX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u0010\u0010 \u001a\u0004\u0018\u00010!X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u0010\u0010\"\u001a\u0004\u0018\u00010#X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010$\u001a\u00020\u001dX\u0082\u000e\u00a2\u0006\u0002\n\u0000\u00a8\u0006I"}, d2 = {"Lcom/ekoapp/ekosdk/uikit/community/views/newsfeed/EkoPostItemFooter;", "Landroidx/constraintlayout/widget/ConstraintLayout;", "context", "Landroid/content/Context;", "(Landroid/content/Context;)V", "attrs", "Landroid/util/AttributeSet;", "(Landroid/content/Context;Landroid/util/AttributeSet;)V", "defStyleAttr", "", "(Landroid/content/Context;Landroid/util/AttributeSet;I)V", "commentItemClickListener", "Lcom/ekoapp/ekosdk/uikit/community/newsfeed/listener/IPostCommentItemClickListener;", "commentReplyClickListener", "Lcom/ekoapp/ekosdk/uikit/community/newsfeed/listener/IPostCommentReplyClickListener;", "commentToExpand", "", "likeListener", "Lcom/ekoapp/ekosdk/uikit/community/newsfeed/listener/IPostActionLikeListener;", "getLikeListener", "()Lcom/ekoapp/ekosdk/uikit/community/newsfeed/listener/IPostActionLikeListener;", "setLikeListener", "(Lcom/ekoapp/ekosdk/uikit/community/newsfeed/listener/IPostActionLikeListener;)V", "mBinding", "Lcom/ekoapp/ekosdk/uikit/community/databinding/AmityItemPostFooterBinding;", "newsFeedCommentAdapter", "Lcom/ekoapp/ekosdk/uikit/community/newsfeed/adapter/EkoPostCommentAdapter;", "postId", "readOnlyView", "", "shareListener", "Lcom/ekoapp/ekosdk/uikit/community/newsfeed/listener/IPostActionShareListener;", "showAllReplyListener", "Lcom/ekoapp/ekosdk/uikit/community/newsfeed/listener/IPostCommentShowAllReplyListener;", "showMoreActionListener", "Lcom/ekoapp/ekosdk/uikit/community/newsfeed/listener/IPostCommentShowMoreActionListener;", "showRepliesComment", "createAdapter", "", "init", "isShowShareButton", "post", "Lcom/ekoapp/ekosdk/feed/EkoPost;", "refreshLikeView", "isLike", "setCommentActionListener", "itemClickListener", "setCommentClickListener", "setFeedLikeActionListener", "setFeedShareActionListener", "setItemClickListener", "setLikeCheckboxText", "setLikeClickListener", "feed", "setNumberOfComments", "commentCount", "setNumberOfLikes", "reactionCount", "setPost", "setPreExpandComment", "setShowAllReplyListener", "setShowMoreActionListener", "setShowRepliesComment", "setUpLikeView", "showViewAllComment", "isVisible", "submitComments", "commentList", "Landroidx/paging/PagedList;", "Lcom/ekoapp/ekosdk/comment/EkoComment;", "isScrollable", "updateComment", "comment", "community_debug"})
public final class EkoPostItemFooter extends androidx.constraintlayout.widget.ConstraintLayout {
    private com.ekoapp.ekosdk.uikit.community.databinding.AmityItemPostFooterBinding mBinding;
    private com.ekoapp.ekosdk.uikit.community.newsfeed.adapter.EkoPostCommentAdapter newsFeedCommentAdapter;
    private com.ekoapp.ekosdk.uikit.community.newsfeed.listener.IPostCommentItemClickListener commentItemClickListener;
    private com.ekoapp.ekosdk.uikit.community.newsfeed.listener.IPostActionShareListener shareListener;
    private com.ekoapp.ekosdk.uikit.community.newsfeed.listener.IPostCommentShowMoreActionListener showMoreActionListener;
    private com.ekoapp.ekosdk.uikit.community.newsfeed.listener.IPostCommentShowAllReplyListener showAllReplyListener;
    private com.ekoapp.ekosdk.uikit.community.newsfeed.listener.IPostCommentReplyClickListener commentReplyClickListener;
    private java.lang.String commentToExpand;
    private boolean readOnlyView = false;
    private boolean showRepliesComment = false;
    private java.lang.String postId = "";
    @org.jetbrains.annotations.Nullable()
    private com.ekoapp.ekosdk.uikit.community.newsfeed.listener.IPostActionLikeListener likeListener;
    private java.util.HashMap _$_findViewCache;
    
    @org.jetbrains.annotations.Nullable()
    public final com.ekoapp.ekosdk.uikit.community.newsfeed.listener.IPostActionLikeListener getLikeListener() {
        return null;
    }
    
    public final void setLikeListener(@org.jetbrains.annotations.Nullable()
    com.ekoapp.ekosdk.uikit.community.newsfeed.listener.IPostActionLikeListener p0) {
    }
    
    private final void init() {
    }
    
    private final void setNumberOfComments(int commentCount) {
    }
    
    public final void setPost(@org.jetbrains.annotations.NotNull()
    com.ekoapp.ekosdk.feed.EkoPost post) {
    }
    
    private final void setUpLikeView(com.ekoapp.ekosdk.feed.EkoPost feed) {
    }
    
    private final void setLikeClickListener(com.ekoapp.ekosdk.feed.EkoPost feed) {
    }
    
    private final void refreshLikeView(boolean isLike) {
    }
    
    private final boolean isShowShareButton(com.ekoapp.ekosdk.feed.EkoPost post) {
        return false;
    }
    
    private final void setNumberOfLikes(int reactionCount) {
    }
    
    private final void setLikeCheckboxText() {
    }
    
    public final void setItemClickListener(@org.jetbrains.annotations.Nullable()
    com.ekoapp.ekosdk.uikit.community.newsfeed.listener.IPostCommentItemClickListener itemClickListener) {
    }
    
    public final void setShowAllReplyListener(@org.jetbrains.annotations.Nullable()
    com.ekoapp.ekosdk.uikit.community.newsfeed.listener.IPostCommentShowAllReplyListener showAllReplyListener) {
    }
    
    public final void setShowMoreActionListener(@org.jetbrains.annotations.Nullable()
    com.ekoapp.ekosdk.uikit.community.newsfeed.listener.IPostCommentShowMoreActionListener showMoreActionListener) {
    }
    
    public final void setFeedLikeActionListener(@org.jetbrains.annotations.NotNull()
    com.ekoapp.ekosdk.uikit.community.newsfeed.listener.IPostActionLikeListener likeListener) {
    }
    
    public final void setCommentClickListener(@org.jetbrains.annotations.NotNull()
    com.ekoapp.ekosdk.uikit.community.newsfeed.listener.IPostCommentReplyClickListener commentReplyClickListener) {
    }
    
    public final void setFeedShareActionListener(@org.jetbrains.annotations.Nullable()
    com.ekoapp.ekosdk.uikit.community.newsfeed.listener.IPostActionShareListener shareListener) {
    }
    
    public final void setCommentActionListener(@org.jetbrains.annotations.Nullable()
    com.ekoapp.ekosdk.uikit.community.newsfeed.listener.IPostCommentItemClickListener itemClickListener, @org.jetbrains.annotations.Nullable()
    com.ekoapp.ekosdk.uikit.community.newsfeed.listener.IPostCommentShowAllReplyListener showAllReplyListener, @org.jetbrains.annotations.Nullable()
    com.ekoapp.ekosdk.uikit.community.newsfeed.listener.IPostCommentShowMoreActionListener showMoreActionListener, @org.jetbrains.annotations.Nullable()
    com.ekoapp.ekosdk.uikit.community.newsfeed.listener.IPostCommentReplyClickListener commentReplyClickListener) {
    }
    
    private final void createAdapter() {
    }
    
    public final void submitComments(@org.jetbrains.annotations.Nullable()
    androidx.paging.PagedList<com.ekoapp.ekosdk.comment.EkoComment> commentList, boolean isScrollable) {
    }
    
    public final void setPreExpandComment(@org.jetbrains.annotations.Nullable()
    java.lang.String commentToExpand) {
    }
    
    public final void setShowRepliesComment(boolean showRepliesComment) {
    }
    
    public final void showViewAllComment(boolean isVisible) {
    }
    
    public final void updateComment(@org.jetbrains.annotations.NotNull()
    com.ekoapp.ekosdk.comment.EkoComment comment) {
    }
    
    public EkoPostItemFooter(@org.jetbrains.annotations.NotNull()
    android.content.Context context) {
        super(null);
    }
    
    public EkoPostItemFooter(@org.jetbrains.annotations.NotNull()
    android.content.Context context, @org.jetbrains.annotations.NotNull()
    android.util.AttributeSet attrs) {
        super(null);
    }
    
    public EkoPostItemFooter(@org.jetbrains.annotations.NotNull()
    android.content.Context context, @org.jetbrains.annotations.NotNull()
    android.util.AttributeSet attrs, int defStyleAttr) {
        super(null);
    }
}