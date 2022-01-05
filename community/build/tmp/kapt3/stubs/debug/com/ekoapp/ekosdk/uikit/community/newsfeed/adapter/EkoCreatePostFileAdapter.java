package com.ekoapp.ekosdk.uikit.community.newsfeed.adapter;

import java.lang.System;

@kotlin.Metadata(mv = {1, 1, 16}, bv = {1, 0, 3}, k = 1, d1 = {"\u0000.\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0010\b\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\u0018\u00002\u00020\u0001B\u000f\u0012\b\u0010\u0002\u001a\u0004\u0018\u00010\u0003\u00a2\u0006\u0002\u0010\u0004J\u001a\u0010\u0007\u001a\u00020\b2\u0006\u0010\t\u001a\u00020\b2\b\u0010\n\u001a\u0004\u0018\u00010\u000bH\u0014J\u0018\u0010\f\u001a\u00020\r2\u0006\u0010\u000e\u001a\u00020\u000f2\u0006\u0010\u0010\u001a\u00020\bH\u0016R\u0013\u0010\u0002\u001a\u0004\u0018\u00010\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0005\u0010\u0006\u00a8\u0006\u0011"}, d2 = {"Lcom/ekoapp/ekosdk/uikit/community/newsfeed/adapter/EkoCreatePostFileAdapter;", "Lcom/ekoapp/ekosdk/uikit/community/newsfeed/adapter/EkoBasePostAttachmentAdapter;", "listener", "Lcom/ekoapp/ekosdk/uikit/community/newsfeed/listener/ICreatePostFileActionListener;", "(Lcom/ekoapp/ekosdk/uikit/community/newsfeed/listener/ICreatePostFileActionListener;)V", "getListener", "()Lcom/ekoapp/ekosdk/uikit/community/newsfeed/listener/ICreatePostFileActionListener;", "getLayoutId", "", "position", "obj", "Lcom/ekoapp/ekosdk/uikit/community/domain/model/FileAttachment;", "getViewHolder", "Landroidx/recyclerview/widget/RecyclerView$ViewHolder;", "view", "Landroid/view/View;", "viewType", "community_debug"})
public final class EkoCreatePostFileAdapter extends com.ekoapp.ekosdk.uikit.community.newsfeed.adapter.EkoBasePostAttachmentAdapter {
    @org.jetbrains.annotations.Nullable()
    private final com.ekoapp.ekosdk.uikit.community.newsfeed.listener.ICreatePostFileActionListener listener = null;
    
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
    
    @org.jetbrains.annotations.Nullable()
    public final com.ekoapp.ekosdk.uikit.community.newsfeed.listener.ICreatePostFileActionListener getListener() {
        return null;
    }
    
    public EkoCreatePostFileAdapter(@org.jetbrains.annotations.Nullable()
    com.ekoapp.ekosdk.uikit.community.newsfeed.listener.ICreatePostFileActionListener listener) {
        super();
    }
}