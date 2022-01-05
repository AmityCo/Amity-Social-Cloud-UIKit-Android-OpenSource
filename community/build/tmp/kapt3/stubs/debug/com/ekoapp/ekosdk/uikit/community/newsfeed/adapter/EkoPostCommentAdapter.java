package com.ekoapp.ekosdk.uikit.community.newsfeed.adapter;

import java.lang.System;

@kotlin.Metadata(mv = {1, 1, 16}, bv = {1, 0, 3}, k = 1, d1 = {"\u0000b\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000b\n\u0002\b\u0002\n\u0002\u0010%\n\u0002\u0010\u000e\n\u0002\u0018\u0002\n\u0002\b\b\n\u0002\u0010\t\n\u0000\n\u0002\u0010\b\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0002\b\u0003\u0018\u00002\b\u0012\u0004\u0012\u00020\u00020\u0001:\u0001\'BU\u0012\b\u0010\u0003\u001a\u0004\u0018\u00010\u0004\u0012\b\u0010\u0005\u001a\u0004\u0018\u00010\u0006\u0012\b\u0010\u0007\u001a\u0004\u0018\u00010\b\u0012\b\u0010\t\u001a\u0004\u0018\u00010\n\u0012\u0006\u0010\u000b\u001a\u00020\f\u0012\b\b\u0002\u0010\r\u001a\u00020\f\u0012\u0014\b\u0002\u0010\u000e\u001a\u000e\u0012\u0004\u0012\u00020\u0010\u0012\u0004\u0012\u00020\u00110\u000f\u00a2\u0006\u0002\u0010\u0012J\u0010\u0010\u0019\u001a\u00020\u001a2\u0006\u0010\u001b\u001a\u00020\u001cH\u0016J\u001a\u0010\u001d\u001a\u00020\u001c2\u0006\u0010\u001b\u001a\u00020\u001c2\b\u0010\u001e\u001a\u0004\u0018\u00010\u0002H\u0014J\u0018\u0010\u001f\u001a\u00020 2\u0006\u0010!\u001a\u00020\"2\u0006\u0010#\u001a\u00020\u001cH\u0016J\u0018\u0010$\u001a\u00020%2\u0006\u0010&\u001a\u00020 2\u0006\u0010\u001b\u001a\u00020\u001cH\u0016J\u0010\u0010\u0017\u001a\u00020%2\u0006\u0010&\u001a\u00020 H\u0002R\u0010\u0010\t\u001a\u0004\u0018\u00010\nX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0013\u0010\u0003\u001a\u0004\u0018\u00010\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0013\u0010\u0014R\u001a\u0010\u000e\u001a\u000e\u0012\u0004\u0012\u00020\u0010\u0012\u0004\u0012\u00020\u00110\u000fX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u001a\u0010\r\u001a\u00020\fX\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\u0015\u0010\u0016\"\u0004\b\u0017\u0010\u0018R\u0010\u0010\u0005\u001a\u0004\u0018\u00010\u0006X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0010\u0010\u0007\u001a\u0004\u0018\u00010\bX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u000b\u001a\u00020\fX\u0082\u0004\u00a2\u0006\u0002\n\u0000\u00a8\u0006("}, d2 = {"Lcom/ekoapp/ekosdk/uikit/community/newsfeed/adapter/EkoPostCommentAdapter;", "Lcom/ekoapp/ekosdk/uikit/base/EkoBaseRecyclerViewPagedAdapter;", "Lcom/ekoapp/ekosdk/comment/EkoComment;", "itemClickListener", "Lcom/ekoapp/ekosdk/uikit/community/newsfeed/listener/IPostCommentItemClickListener;", "showAllReplyListener", "Lcom/ekoapp/ekosdk/uikit/community/newsfeed/listener/IPostCommentShowAllReplyListener;", "showMoreActionListener", "Lcom/ekoapp/ekosdk/uikit/community/newsfeed/listener/IPostCommentShowMoreActionListener;", "commentReplyClickListener", "Lcom/ekoapp/ekosdk/uikit/community/newsfeed/listener/IPostCommentReplyClickListener;", "showRepliesComment", "", "readOnlyMode", "loaderMap", "", "", "Lcom/ekoapp/ekosdk/uikit/community/newsfeed/adapter/EkoCommentReplyLoader;", "(Lcom/ekoapp/ekosdk/uikit/community/newsfeed/listener/IPostCommentItemClickListener;Lcom/ekoapp/ekosdk/uikit/community/newsfeed/listener/IPostCommentShowAllReplyListener;Lcom/ekoapp/ekosdk/uikit/community/newsfeed/listener/IPostCommentShowMoreActionListener;Lcom/ekoapp/ekosdk/uikit/community/newsfeed/listener/IPostCommentReplyClickListener;ZZLjava/util/Map;)V", "getItemClickListener", "()Lcom/ekoapp/ekosdk/uikit/community/newsfeed/listener/IPostCommentItemClickListener;", "getReadOnlyMode", "()Z", "setReadOnlyMode", "(Z)V", "getItemId", "", "position", "", "getLayoutId", "obj", "getViewHolder", "Landroidx/recyclerview/widget/RecyclerView$ViewHolder;", "view", "Landroid/view/View;", "viewType", "onBindViewHolder", "", "holder", "PostCommentDiffUtil", "community_debug"})
public final class EkoPostCommentAdapter extends com.ekoapp.ekosdk.uikit.base.EkoBaseRecyclerViewPagedAdapter<com.ekoapp.ekosdk.comment.EkoComment> {
    @org.jetbrains.annotations.Nullable()
    private final com.ekoapp.ekosdk.uikit.community.newsfeed.listener.IPostCommentItemClickListener itemClickListener = null;
    private final com.ekoapp.ekosdk.uikit.community.newsfeed.listener.IPostCommentShowAllReplyListener showAllReplyListener = null;
    private final com.ekoapp.ekosdk.uikit.community.newsfeed.listener.IPostCommentShowMoreActionListener showMoreActionListener = null;
    private final com.ekoapp.ekosdk.uikit.community.newsfeed.listener.IPostCommentReplyClickListener commentReplyClickListener = null;
    private final boolean showRepliesComment = false;
    private boolean readOnlyMode;
    private final java.util.Map<java.lang.String, com.ekoapp.ekosdk.uikit.community.newsfeed.adapter.EkoCommentReplyLoader> loaderMap = null;
    
    @java.lang.Override()
    protected int getLayoutId(int position, @org.jetbrains.annotations.Nullable()
    com.ekoapp.ekosdk.comment.EkoComment obj) {
        return 0;
    }
    
    @java.lang.Override()
    public void onBindViewHolder(@org.jetbrains.annotations.NotNull()
    androidx.recyclerview.widget.RecyclerView.ViewHolder holder, int position) {
    }
    
    @org.jetbrains.annotations.NotNull()
    @java.lang.Override()
    public androidx.recyclerview.widget.RecyclerView.ViewHolder getViewHolder(@org.jetbrains.annotations.NotNull()
    android.view.View view, int viewType) {
        return null;
    }
    
    @java.lang.Override()
    public long getItemId(int position) {
        return 0L;
    }
    
    private final void setReadOnlyMode(androidx.recyclerview.widget.RecyclerView.ViewHolder holder) {
    }
    
    @org.jetbrains.annotations.Nullable()
    public final com.ekoapp.ekosdk.uikit.community.newsfeed.listener.IPostCommentItemClickListener getItemClickListener() {
        return null;
    }
    
    public final boolean getReadOnlyMode() {
        return false;
    }
    
    public final void setReadOnlyMode(boolean p0) {
    }
    
    public EkoPostCommentAdapter(@org.jetbrains.annotations.Nullable()
    com.ekoapp.ekosdk.uikit.community.newsfeed.listener.IPostCommentItemClickListener itemClickListener, @org.jetbrains.annotations.Nullable()
    com.ekoapp.ekosdk.uikit.community.newsfeed.listener.IPostCommentShowAllReplyListener showAllReplyListener, @org.jetbrains.annotations.Nullable()
    com.ekoapp.ekosdk.uikit.community.newsfeed.listener.IPostCommentShowMoreActionListener showMoreActionListener, @org.jetbrains.annotations.Nullable()
    com.ekoapp.ekosdk.uikit.community.newsfeed.listener.IPostCommentReplyClickListener commentReplyClickListener, boolean showRepliesComment, boolean readOnlyMode, @org.jetbrains.annotations.NotNull()
    java.util.Map<java.lang.String, com.ekoapp.ekosdk.uikit.community.newsfeed.adapter.EkoCommentReplyLoader> loaderMap) {
        super(null);
    }
    
    @kotlin.Metadata(mv = {1, 1, 16}, bv = {1, 0, 3}, k = 1, d1 = {"\u0000,\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010$\n\u0002\u0010\u000e\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000b\n\u0002\b\u0007\n\u0002\u0010 \n\u0002\b\u0006\u0018\u00002\b\u0012\u0004\u0012\u00020\u00020\u0001B!\u0012\u0012\u0010\u0003\u001a\u000e\u0012\u0004\u0012\u00020\u0005\u0012\u0004\u0012\u00020\u00060\u0004\u0012\u0006\u0010\u0007\u001a\u00020\b\u00a2\u0006\u0002\u0010\tJ\u0018\u0010\n\u001a\u00020\b2\u0006\u0010\u000b\u001a\u00020\u00022\u0006\u0010\f\u001a\u00020\u0002H\u0016J\u0018\u0010\r\u001a\u00020\b2\u0006\u0010\u000b\u001a\u00020\u00022\u0006\u0010\f\u001a\u00020\u0002H\u0016J$\u0010\u000e\u001a\u00020\b2\f\u0010\u000f\u001a\b\u0012\u0004\u0012\u00020\u00020\u00102\f\u0010\u0011\u001a\b\u0012\u0004\u0012\u00020\u00020\u0010H\u0002J\u0018\u0010\u0012\u001a\u00020\b2\u0006\u0010\u000b\u001a\u00020\u00022\u0006\u0010\f\u001a\u00020\u0002H\u0002J\b\u0010\u0013\u001a\u00020\bH\u0002J\u0010\u0010\u0014\u001a\u00020\b2\u0006\u0010\u0015\u001a\u00020\u0002H\u0002R\u001a\u0010\u0003\u001a\u000e\u0012\u0004\u0012\u00020\u0005\u0012\u0004\u0012\u00020\u00060\u0004X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0007\u001a\u00020\bX\u0082\u0004\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u0016"}, d2 = {"Lcom/ekoapp/ekosdk/uikit/community/newsfeed/adapter/EkoPostCommentAdapter$PostCommentDiffUtil;", "Landroidx/recyclerview/widget/DiffUtil$ItemCallback;", "Lcom/ekoapp/ekosdk/comment/EkoComment;", "loaderMap", "", "", "Lcom/ekoapp/ekosdk/uikit/community/newsfeed/adapter/EkoCommentReplyLoader;", "showRepliesComment", "", "(Ljava/util/Map;Z)V", "areContentsTheSame", "oldItem", "newItem", "areItemsTheSame", "areRepliesTheSame", "oldReplies", "", "newReplies", "isDataTheSame", "shouldIgnoreData", "shouldIgnoreReplies", "comment", "community_debug"})
    public static final class PostCommentDiffUtil extends androidx.recyclerview.widget.DiffUtil.ItemCallback<com.ekoapp.ekosdk.comment.EkoComment> {
        private final java.util.Map<java.lang.String, com.ekoapp.ekosdk.uikit.community.newsfeed.adapter.EkoCommentReplyLoader> loaderMap = null;
        private final boolean showRepliesComment = false;
        
        private final boolean shouldIgnoreReplies(com.ekoapp.ekosdk.comment.EkoComment comment) {
            return false;
        }
        
        @java.lang.Override()
        public boolean areItemsTheSame(@org.jetbrains.annotations.NotNull()
        com.ekoapp.ekosdk.comment.EkoComment oldItem, @org.jetbrains.annotations.NotNull()
        com.ekoapp.ekosdk.comment.EkoComment newItem) {
            return false;
        }
        
        @java.lang.Override()
        public boolean areContentsTheSame(@org.jetbrains.annotations.NotNull()
        com.ekoapp.ekosdk.comment.EkoComment oldItem, @org.jetbrains.annotations.NotNull()
        com.ekoapp.ekosdk.comment.EkoComment newItem) {
            return false;
        }
        
        private final boolean isDataTheSame(com.ekoapp.ekosdk.comment.EkoComment oldItem, com.ekoapp.ekosdk.comment.EkoComment newItem) {
            return false;
        }
        
        private final boolean shouldIgnoreData() {
            return false;
        }
        
        private final boolean areRepliesTheSame(java.util.List<com.ekoapp.ekosdk.comment.EkoComment> oldReplies, java.util.List<com.ekoapp.ekosdk.comment.EkoComment> newReplies) {
            return false;
        }
        
        public PostCommentDiffUtil(@org.jetbrains.annotations.NotNull()
        java.util.Map<java.lang.String, com.ekoapp.ekosdk.uikit.community.newsfeed.adapter.EkoCommentReplyLoader> loaderMap, boolean showRepliesComment) {
            super();
        }
    }
}