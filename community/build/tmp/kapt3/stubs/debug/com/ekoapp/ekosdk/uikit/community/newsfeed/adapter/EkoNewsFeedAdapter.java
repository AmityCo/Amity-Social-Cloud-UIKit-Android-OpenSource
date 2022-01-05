package com.ekoapp.ekosdk.uikit.community.newsfeed.adapter;

import java.lang.System;

@kotlin.Metadata(mv = {1, 1, 16}, bv = {1, 0, 3}, k = 1, d1 = {"\u0000P\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\t\n\u0000\n\u0002\u0010\b\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0003\u0018\u0000 \u001b2\u000e\u0012\u0004\u0012\u00020\u0002\u0012\u0004\u0012\u00020\u00030\u0001:\u0001\u001bB-\u0012\u0006\u0010\u0004\u001a\u00020\u0005\u0012\u0006\u0010\u0006\u001a\u00020\u0007\u0012\u0006\u0010\b\u001a\u00020\t\u0012\u0006\u0010\n\u001a\u00020\u000b\u0012\u0006\u0010\f\u001a\u00020\r\u00a2\u0006\u0002\u0010\u000eJ\u0010\u0010\u000f\u001a\u00020\u00102\u0006\u0010\u0011\u001a\u00020\u0012H\u0016J\u0010\u0010\u0013\u001a\u00020\u00122\u0006\u0010\u0011\u001a\u00020\u0012H\u0016J\u0018\u0010\u0014\u001a\u00020\u00152\u0006\u0010\u0016\u001a\u00020\u00032\u0006\u0010\u0011\u001a\u00020\u0012H\u0016J\u0018\u0010\u0017\u001a\u00020\u00032\u0006\u0010\u0018\u001a\u00020\u00192\u0006\u0010\u001a\u001a\u00020\u0012H\u0016R\u000e\u0010\f\u001a\u00020\rX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\b\u001a\u00020\tX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0006\u001a\u00020\u0007X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\n\u001a\u00020\u000bX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0004\u001a\u00020\u0005X\u0082\u0004\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u001c"}, d2 = {"Lcom/ekoapp/ekosdk/uikit/community/newsfeed/adapter/EkoNewsFeedAdapter;", "Landroidx/paging/PagedListAdapter;", "Lcom/ekoapp/ekosdk/feed/EkoPost;", "Lcom/ekoapp/ekosdk/uikit/community/newsfeed/adapter/EkoBasePostViewHolder;", "timelineType", "Lcom/ekoapp/ekosdk/uikit/community/newsfeed/util/EkoTimelineType;", "itemActionListener", "Lcom/ekoapp/ekosdk/uikit/community/newsfeed/listener/IPostItemActionListener;", "imageClickListener", "Lcom/ekoapp/ekosdk/uikit/community/newsfeed/listener/IPostImageClickListener;", "loadMoreFilesClickListener", "Lcom/ekoapp/ekosdk/uikit/community/newsfeed/adapter/EkoPostViewFileAdapter$ILoadMoreFilesClickListener;", "fileItemClickListener", "Lcom/ekoapp/ekosdk/uikit/community/newsfeed/listener/IPostFileItemClickListener;", "(Lcom/ekoapp/ekosdk/uikit/community/newsfeed/util/EkoTimelineType;Lcom/ekoapp/ekosdk/uikit/community/newsfeed/listener/IPostItemActionListener;Lcom/ekoapp/ekosdk/uikit/community/newsfeed/listener/IPostImageClickListener;Lcom/ekoapp/ekosdk/uikit/community/newsfeed/adapter/EkoPostViewFileAdapter$ILoadMoreFilesClickListener;Lcom/ekoapp/ekosdk/uikit/community/newsfeed/listener/IPostFileItemClickListener;)V", "getItemId", "", "position", "", "getItemViewType", "onBindViewHolder", "", "holder", "onCreateViewHolder", "parent", "Landroid/view/ViewGroup;", "viewType", "Companion", "community_debug"})
public final class EkoNewsFeedAdapter extends androidx.paging.PagedListAdapter<com.ekoapp.ekosdk.feed.EkoPost, com.ekoapp.ekosdk.uikit.community.newsfeed.adapter.EkoBasePostViewHolder> {
    private final com.ekoapp.ekosdk.uikit.community.newsfeed.util.EkoTimelineType timelineType = null;
    private final com.ekoapp.ekosdk.uikit.community.newsfeed.listener.IPostItemActionListener itemActionListener = null;
    private final com.ekoapp.ekosdk.uikit.community.newsfeed.listener.IPostImageClickListener imageClickListener = null;
    private final com.ekoapp.ekosdk.uikit.community.newsfeed.adapter.EkoPostViewFileAdapter.ILoadMoreFilesClickListener loadMoreFilesClickListener = null;
    private final com.ekoapp.ekosdk.uikit.community.newsfeed.listener.IPostFileItemClickListener fileItemClickListener = null;
    private static final androidx.recyclerview.widget.DiffUtil.ItemCallback<com.ekoapp.ekosdk.feed.EkoPost> diffCallBack = null;
    public static final com.ekoapp.ekosdk.uikit.community.newsfeed.adapter.EkoNewsFeedAdapter.Companion Companion = null;
    
    @org.jetbrains.annotations.NotNull()
    @java.lang.Override()
    public com.ekoapp.ekosdk.uikit.community.newsfeed.adapter.EkoBasePostViewHolder onCreateViewHolder(@org.jetbrains.annotations.NotNull()
    android.view.ViewGroup parent, int viewType) {
        return null;
    }
    
    @java.lang.Override()
    public void onBindViewHolder(@org.jetbrains.annotations.NotNull()
    com.ekoapp.ekosdk.uikit.community.newsfeed.adapter.EkoBasePostViewHolder holder, int position) {
    }
    
    @java.lang.Override()
    public int getItemViewType(int position) {
        return 0;
    }
    
    @java.lang.Override()
    public long getItemId(int position) {
        return 0L;
    }
    
    public EkoNewsFeedAdapter(@org.jetbrains.annotations.NotNull()
    com.ekoapp.ekosdk.uikit.community.newsfeed.util.EkoTimelineType timelineType, @org.jetbrains.annotations.NotNull()
    com.ekoapp.ekosdk.uikit.community.newsfeed.listener.IPostItemActionListener itemActionListener, @org.jetbrains.annotations.NotNull()
    com.ekoapp.ekosdk.uikit.community.newsfeed.listener.IPostImageClickListener imageClickListener, @org.jetbrains.annotations.NotNull()
    com.ekoapp.ekosdk.uikit.community.newsfeed.adapter.EkoPostViewFileAdapter.ILoadMoreFilesClickListener loadMoreFilesClickListener, @org.jetbrains.annotations.NotNull()
    com.ekoapp.ekosdk.uikit.community.newsfeed.listener.IPostFileItemClickListener fileItemClickListener) {
        super(null);
    }
    
    @kotlin.Metadata(mv = {1, 1, 16}, bv = {1, 0, 3}, k = 1, d1 = {"\u0000(\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000b\n\u0000\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0002\b\u0007\b\u0086\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002J$\u0010\u0006\u001a\u00020\u00072\f\u0010\b\u001a\b\u0012\u0004\u0012\u00020\n0\t2\f\u0010\u000b\u001a\b\u0012\u0004\u0012\u00020\n0\tH\u0002J\u0018\u0010\f\u001a\u00020\u00072\u0006\u0010\r\u001a\u00020\n2\u0006\u0010\u000e\u001a\u00020\nH\u0002J\u0018\u0010\u000f\u001a\u00020\u00072\u0006\u0010\r\u001a\u00020\u00052\u0006\u0010\u000e\u001a\u00020\u0005H\u0002J\u0018\u0010\u0010\u001a\u00020\u00072\u0006\u0010\r\u001a\u00020\u00052\u0006\u0010\u000e\u001a\u00020\u0005H\u0002R\u0014\u0010\u0003\u001a\b\u0012\u0004\u0012\u00020\u00050\u0004X\u0082\u0004\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u0011"}, d2 = {"Lcom/ekoapp/ekosdk/uikit/community/newsfeed/adapter/EkoNewsFeedAdapter$Companion;", "", "()V", "diffCallBack", "Landroidx/recyclerview/widget/DiffUtil$ItemCallback;", "Lcom/ekoapp/ekosdk/feed/EkoPost;", "areLatestCommentsTheSame", "", "oldComments", "", "Lcom/ekoapp/ekosdk/comment/EkoComment;", "newComments", "isCommentDataTheSame", "oldItem", "newItem", "isDataTheSame", "isTargetTheSame", "community_debug"})
    public static final class Companion {
        
        private final boolean isDataTheSame(com.ekoapp.ekosdk.feed.EkoPost oldItem, com.ekoapp.ekosdk.feed.EkoPost newItem) {
            return false;
        }
        
        private final boolean isTargetTheSame(com.ekoapp.ekosdk.feed.EkoPost oldItem, com.ekoapp.ekosdk.feed.EkoPost newItem) {
            return false;
        }
        
        private final boolean areLatestCommentsTheSame(java.util.List<com.ekoapp.ekosdk.comment.EkoComment> oldComments, java.util.List<com.ekoapp.ekosdk.comment.EkoComment> newComments) {
            return false;
        }
        
        private final boolean isCommentDataTheSame(com.ekoapp.ekosdk.comment.EkoComment oldItem, com.ekoapp.ekosdk.comment.EkoComment newItem) {
            return false;
        }
        
        private Companion() {
            super();
        }
    }
}