package com.ekoapp.ekosdk.uikit.community.newsfeed.adapter;

import java.lang.System;

@kotlin.Metadata(mv = {1, 1, 16}, bv = {1, 0, 3}, k = 1, d1 = {"\u0000t\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000b\n\u0000\n\u0002\u0010%\n\u0002\u0010\u000e\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0010\u0002\n\u0002\b\u0002\n\u0002\u0010\b\n\u0002\b\n\n\u0002\u0010 \n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\u0018\u00002\u00020\u00012\b\u0012\u0004\u0012\u00020\u00030\u0002BY\u0012\u0006\u0010\u0004\u001a\u00020\u0005\u0012\b\u0010\u0006\u001a\u0004\u0018\u00010\u0007\u0012\b\u0010\b\u001a\u0004\u0018\u00010\t\u0012\b\u0010\n\u001a\u0004\u0018\u00010\u000b\u0012\b\u0010\f\u001a\u0004\u0018\u00010\r\u0012\u0006\u0010\u000e\u001a\u00020\u000f\u0012\u0012\u0010\u0010\u001a\u000e\u0012\u0004\u0012\u00020\u0012\u0012\u0004\u0012\u00020\u00130\u0011\u0012\u0006\u0010\u0014\u001a\u00020\u000f\u00a2\u0006\u0002\u0010\u0015J\u0018\u0010\u001f\u001a\u00020 2\u0006\u0010!\u001a\u00020\u00032\u0006\u0010\"\u001a\u00020#H\u0002J\u0018\u0010$\u001a\u00020 2\u0006\u0010!\u001a\u00020\u00032\u0006\u0010\"\u001a\u00020#H\u0002J\u001a\u0010%\u001a\u00020 2\b\u0010&\u001a\u0004\u0018\u00010\u00032\u0006\u0010\"\u001a\u00020#H\u0016J\b\u0010\'\u001a\u00020 H\u0002J\u0018\u0010(\u001a\u00020 2\u0006\u0010!\u001a\u00020\u00032\u0006\u0010\"\u001a\u00020#H\u0002J\u0010\u0010)\u001a\u00020\u000f2\u0006\u0010!\u001a\u00020\u0003H\u0002J\u0010\u0010*\u001a\u00020 2\u0006\u0010!\u001a\u00020\u0003H\u0002J\u0010\u0010+\u001a\u00020 2\u0006\u0010!\u001a\u00020\u0003H\u0002J\u0010\u0010,\u001a\u00020 2\u0006\u0010!\u001a\u00020\u0003H\u0002J\u0016\u0010-\u001a\u00020 2\f\u0010&\u001a\b\u0012\u0004\u0012\u00020\u00030.H\u0002J\u000e\u0010/\u001a\u00020 2\u0006\u00100\u001a\u000201R\u0010\u0010\u0016\u001a\u0004\u0018\u00010\u0017X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0010\u0010\u0018\u001a\u0004\u0018\u00010\u0013X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u0010\u0010\f\u001a\u0004\u0018\u00010\rX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0010\u0010\u0006\u001a\u0004\u0018\u00010\u0007X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u001a\u0010\u0010\u001a\u000e\u0012\u0004\u0012\u00020\u0012\u0012\u0004\u0012\u00020\u00130\u0011X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0010\u0010\u0019\u001a\u0004\u0018\u00010\u001aX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u001a\u0010\u0014\u001a\u00020\u000fX\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\u001b\u0010\u001c\"\u0004\b\u001d\u0010\u001eR\u0010\u0010\b\u001a\u0004\u0018\u00010\tX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0010\u0010\n\u001a\u0004\u0018\u00010\u000bX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u000e\u001a\u00020\u000fX\u0082\u0004\u00a2\u0006\u0002\n\u0000\u00a8\u00062"}, d2 = {"Lcom/ekoapp/ekosdk/uikit/community/newsfeed/adapter/EkoPostCommentViewHolder;", "Landroidx/recyclerview/widget/RecyclerView$ViewHolder;", "Lcom/ekoapp/ekosdk/uikit/base/EkoBaseRecyclerViewPagedAdapter$Binder;", "Lcom/ekoapp/ekosdk/comment/EkoComment;", "itemView", "Landroid/view/View;", "itemClickListener", "Lcom/ekoapp/ekosdk/uikit/community/newsfeed/listener/IPostCommentItemClickListener;", "showAllReplyListener", "Lcom/ekoapp/ekosdk/uikit/community/newsfeed/listener/IPostCommentShowAllReplyListener;", "showMoreActionListener", "Lcom/ekoapp/ekosdk/uikit/community/newsfeed/listener/IPostCommentShowMoreActionListener;", "commentReplyClickListener", "Lcom/ekoapp/ekosdk/uikit/community/newsfeed/listener/IPostCommentReplyClickListener;", "showRepliesComment", "", "loaderMap", "", "", "Lcom/ekoapp/ekosdk/uikit/community/newsfeed/adapter/EkoCommentReplyLoader;", "readOnlyMode", "(Landroid/view/View;Lcom/ekoapp/ekosdk/uikit/community/newsfeed/listener/IPostCommentItemClickListener;Lcom/ekoapp/ekosdk/uikit/community/newsfeed/listener/IPostCommentShowAllReplyListener;Lcom/ekoapp/ekosdk/uikit/community/newsfeed/listener/IPostCommentShowMoreActionListener;Lcom/ekoapp/ekosdk/uikit/community/newsfeed/listener/IPostCommentReplyClickListener;ZLjava/util/Map;Z)V", "binding", "Lcom/ekoapp/ekosdk/uikit/community/databinding/AmityItemCommentPostBinding;", "commentLoader", "newsFeedCommentAdapter", "Lcom/ekoapp/ekosdk/uikit/community/newsfeed/adapter/EkoPostCommentAdapter;", "getReadOnlyMode", "()Z", "setReadOnlyMode", "(Z)V", "addCommentActionListener", "", "comment", "position", "", "addItemClickListener", "bind", "data", "createAdapter", "initialListener", "isReplyComment", "onClickAvatarListener", "setupRepliesView", "setupViewRepliesButton", "showReplies", "", "updateData", "commentData", "Lcom/ekoapp/ekosdk/comment/EkoComment$Data;", "community_debug"})
public final class EkoPostCommentViewHolder extends androidx.recyclerview.widget.RecyclerView.ViewHolder implements com.ekoapp.ekosdk.uikit.base.EkoBaseRecyclerViewPagedAdapter.Binder<com.ekoapp.ekosdk.comment.EkoComment> {
    private final com.ekoapp.ekosdk.uikit.community.databinding.AmityItemCommentPostBinding binding = null;
    private com.ekoapp.ekosdk.uikit.community.newsfeed.adapter.EkoPostCommentAdapter newsFeedCommentAdapter;
    private com.ekoapp.ekosdk.uikit.community.newsfeed.adapter.EkoCommentReplyLoader commentLoader;
    private final com.ekoapp.ekosdk.uikit.community.newsfeed.listener.IPostCommentItemClickListener itemClickListener = null;
    private final com.ekoapp.ekosdk.uikit.community.newsfeed.listener.IPostCommentShowAllReplyListener showAllReplyListener = null;
    private final com.ekoapp.ekosdk.uikit.community.newsfeed.listener.IPostCommentShowMoreActionListener showMoreActionListener = null;
    private final com.ekoapp.ekosdk.uikit.community.newsfeed.listener.IPostCommentReplyClickListener commentReplyClickListener = null;
    private final boolean showRepliesComment = false;
    private final java.util.Map<java.lang.String, com.ekoapp.ekosdk.uikit.community.newsfeed.adapter.EkoCommentReplyLoader> loaderMap = null;
    private boolean readOnlyMode;
    
    @java.lang.Override()
    public void bind(@org.jetbrains.annotations.Nullable()
    com.ekoapp.ekosdk.comment.EkoComment data, int position) {
    }
    
    private final void setupViewRepliesButton(com.ekoapp.ekosdk.comment.EkoComment comment) {
    }
    
    private final boolean isReplyComment(com.ekoapp.ekosdk.comment.EkoComment comment) {
        return false;
    }
    
    private final void initialListener(com.ekoapp.ekosdk.comment.EkoComment comment, int position) {
    }
    
    private final void setupRepliesView(com.ekoapp.ekosdk.comment.EkoComment comment) {
    }
    
    private final void addItemClickListener(com.ekoapp.ekosdk.comment.EkoComment comment, int position) {
    }
    
    private final void onClickAvatarListener(com.ekoapp.ekosdk.comment.EkoComment comment) {
    }
    
    private final void addCommentActionListener(com.ekoapp.ekosdk.comment.EkoComment comment, int position) {
    }
    
    private final void showReplies(java.util.List<com.ekoapp.ekosdk.comment.EkoComment> data) {
    }
    
    private final void createAdapter() {
    }
    
    public final void updateData(@org.jetbrains.annotations.NotNull()
    com.ekoapp.ekosdk.comment.EkoComment.Data commentData) {
    }
    
    public final boolean getReadOnlyMode() {
        return false;
    }
    
    public final void setReadOnlyMode(boolean p0) {
    }
    
    public EkoPostCommentViewHolder(@org.jetbrains.annotations.NotNull()
    android.view.View itemView, @org.jetbrains.annotations.Nullable()
    com.ekoapp.ekosdk.uikit.community.newsfeed.listener.IPostCommentItemClickListener itemClickListener, @org.jetbrains.annotations.Nullable()
    com.ekoapp.ekosdk.uikit.community.newsfeed.listener.IPostCommentShowAllReplyListener showAllReplyListener, @org.jetbrains.annotations.Nullable()
    com.ekoapp.ekosdk.uikit.community.newsfeed.listener.IPostCommentShowMoreActionListener showMoreActionListener, @org.jetbrains.annotations.Nullable()
    com.ekoapp.ekosdk.uikit.community.newsfeed.listener.IPostCommentReplyClickListener commentReplyClickListener, boolean showRepliesComment, @org.jetbrains.annotations.NotNull()
    java.util.Map<java.lang.String, com.ekoapp.ekosdk.uikit.community.newsfeed.adapter.EkoCommentReplyLoader> loaderMap, boolean readOnlyMode) {
        super(null);
    }
}