package com.ekoapp.ekosdk.uikit.community.newsfeed.adapter;

import java.lang.System;

@kotlin.Metadata(mv = {1, 1, 16}, bv = {1, 0, 3}, k = 1, d1 = {"\u0000B\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000b\n\u0002\b\u0003\n\u0002\u0010\b\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\u0018\u00002\u00020\u0001:\u0001\u0018B\u0011\b\u0016\u0012\b\u0010\u0002\u001a\u0004\u0018\u00010\u0003\u00a2\u0006\u0002\u0010\u0004B+\b\u0016\u0012\b\u0010\u0005\u001a\u0004\u0018\u00010\u0006\u0012\b\u0010\u0002\u001a\u0004\u0018\u00010\u0003\u0012\u0006\u0010\u0007\u001a\u00020\b\u0012\u0006\u0010\t\u001a\u00020\n\u00a2\u0006\u0002\u0010\u000bB\u0005\u00a2\u0006\u0002\u0010\fJ\b\u0010\r\u001a\u00020\u000eH\u0016J\u001a\u0010\u000f\u001a\u00020\u000e2\u0006\u0010\u0010\u001a\u00020\u000e2\b\u0010\u0011\u001a\u0004\u0018\u00010\u0012H\u0014J\u0018\u0010\u0013\u001a\u00020\u00142\u0006\u0010\u0015\u001a\u00020\u00162\u0006\u0010\u0017\u001a\u00020\u000eH\u0016R\u000e\u0010\t\u001a\u00020\nX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u0010\u0010\u0002\u001a\u0004\u0018\u00010\u0003X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u0010\u0010\u0005\u001a\u0004\u0018\u00010\u0006X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u0010\u0010\u0007\u001a\u0004\u0018\u00010\bX\u0082\u000e\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u0019"}, d2 = {"Lcom/ekoapp/ekosdk/uikit/community/newsfeed/adapter/EkoPostViewFileAdapter;", "Lcom/ekoapp/ekosdk/uikit/community/newsfeed/adapter/EkoBasePostAttachmentAdapter;", "fileItemClickListener", "Lcom/ekoapp/ekosdk/uikit/community/newsfeed/listener/IPostFileItemClickListener;", "(Lcom/ekoapp/ekosdk/uikit/community/newsfeed/listener/IPostFileItemClickListener;)V", "loadMoreFilesClickListener", "Lcom/ekoapp/ekosdk/uikit/community/newsfeed/adapter/EkoPostViewFileAdapter$ILoadMoreFilesClickListener;", "newsFeed", "Lcom/ekoapp/ekosdk/feed/EkoPost;", "collapsible", "", "(Lcom/ekoapp/ekosdk/uikit/community/newsfeed/adapter/EkoPostViewFileAdapter$ILoadMoreFilesClickListener;Lcom/ekoapp/ekosdk/uikit/community/newsfeed/listener/IPostFileItemClickListener;Lcom/ekoapp/ekosdk/feed/EkoPost;Z)V", "()V", "getItemCount", "", "getLayoutId", "position", "obj", "Lcom/ekoapp/ekosdk/uikit/community/domain/model/FileAttachment;", "getViewHolder", "Landroidx/recyclerview/widget/RecyclerView$ViewHolder;", "view", "Landroid/view/View;", "viewType", "ILoadMoreFilesClickListener", "community_debug"})
public final class EkoPostViewFileAdapter extends com.ekoapp.ekosdk.uikit.community.newsfeed.adapter.EkoBasePostAttachmentAdapter {
    private com.ekoapp.ekosdk.uikit.community.newsfeed.adapter.EkoPostViewFileAdapter.ILoadMoreFilesClickListener loadMoreFilesClickListener;
    private com.ekoapp.ekosdk.uikit.community.newsfeed.listener.IPostFileItemClickListener fileItemClickListener;
    private com.ekoapp.ekosdk.feed.EkoPost newsFeed;
    private boolean collapsible = false;
    
    @java.lang.Override()
    protected int getLayoutId(int position, @org.jetbrains.annotations.Nullable()
    com.ekoapp.ekosdk.uikit.community.domain.model.FileAttachment obj) {
        return 0;
    }
    
    @org.jetbrains.annotations.NotNull()
    @java.lang.Override()
    public androidx.recyclerview.widget.RecyclerView.ViewHolder getViewHolder(@org.jetbrains.annotations.NotNull()
    android.view.View view, int viewType) {
        return null;
    }
    
    @java.lang.Override()
    public int getItemCount() {
        return 0;
    }
    
    public EkoPostViewFileAdapter() {
        super();
    }
    
    public EkoPostViewFileAdapter(@org.jetbrains.annotations.Nullable()
    com.ekoapp.ekosdk.uikit.community.newsfeed.listener.IPostFileItemClickListener fileItemClickListener) {
        super();
    }
    
    public EkoPostViewFileAdapter(@org.jetbrains.annotations.Nullable()
    com.ekoapp.ekosdk.uikit.community.newsfeed.adapter.EkoPostViewFileAdapter.ILoadMoreFilesClickListener loadMoreFilesClickListener, @org.jetbrains.annotations.Nullable()
    com.ekoapp.ekosdk.uikit.community.newsfeed.listener.IPostFileItemClickListener fileItemClickListener, @org.jetbrains.annotations.NotNull()
    com.ekoapp.ekosdk.feed.EkoPost newsFeed, boolean collapsible) {
        super();
    }
    
    @kotlin.Metadata(mv = {1, 1, 16}, bv = {1, 0, 3}, k = 1, d1 = {"\u0000\u0016\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\bf\u0018\u00002\u00020\u0001J\u0010\u0010\u0002\u001a\u00020\u00032\u0006\u0010\u0004\u001a\u00020\u0005H&\u00a8\u0006\u0006"}, d2 = {"Lcom/ekoapp/ekosdk/uikit/community/newsfeed/adapter/EkoPostViewFileAdapter$ILoadMoreFilesClickListener;", "", "loadMoreFiles", "", "post", "Lcom/ekoapp/ekosdk/feed/EkoPost;", "community_debug"})
    public static abstract interface ILoadMoreFilesClickListener {
        
        public abstract void loadMoreFiles(@org.jetbrains.annotations.NotNull()
        com.ekoapp.ekosdk.feed.EkoPost post);
    }
}