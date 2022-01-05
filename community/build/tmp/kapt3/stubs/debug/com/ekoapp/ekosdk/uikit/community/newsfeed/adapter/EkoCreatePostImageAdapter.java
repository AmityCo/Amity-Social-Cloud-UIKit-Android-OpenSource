package com.ekoapp.ekosdk.uikit.community.newsfeed.adapter;

import java.lang.System;

@kotlin.Metadata(mv = {1, 1, 16}, bv = {1, 0, 3}, k = 1, d1 = {"\u0000>\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\b\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u0002\n\u0000\n\u0002\u0010 \n\u0002\b\u0002\u0018\u00002\b\u0012\u0004\u0012\u00020\u00020\u00012\u00020\u0003:\u0001\u0015B\r\u0012\u0006\u0010\u0004\u001a\u00020\u0005\u00a2\u0006\u0002\u0010\u0006J\u001a\u0010\u0007\u001a\u00020\b2\u0006\u0010\t\u001a\u00020\b2\b\u0010\n\u001a\u0004\u0018\u00010\u0002H\u0014J\u0018\u0010\u000b\u001a\u00020\f2\u0006\u0010\r\u001a\u00020\u000e2\u0006\u0010\u000f\u001a\u00020\bH\u0016J\b\u0010\u0010\u001a\u00020\bH\u0016J\u0014\u0010\u0011\u001a\u00020\u00122\f\u0010\u0013\u001a\b\u0012\u0004\u0012\u00020\u00020\u0014R\u000e\u0010\u0004\u001a\u00020\u0005X\u0082\u0004\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u0016"}, d2 = {"Lcom/ekoapp/ekosdk/uikit/community/newsfeed/adapter/EkoCreatePostImageAdapter;", "Lcom/ekoapp/ekosdk/uikit/base/EkoBaseRecyclerViewAdapter;", "Lcom/ekoapp/ekosdk/uikit/community/newsfeed/model/FeedImage;", "Lcom/ekoapp/ekosdk/uikit/community/newsfeed/adapter/IListItemChangeListener;", "listener", "Lcom/ekoapp/ekosdk/uikit/community/newsfeed/listener/ICreatePostImageActionListener;", "(Lcom/ekoapp/ekosdk/uikit/community/newsfeed/listener/ICreatePostImageActionListener;)V", "getLayoutId", "", "position", "obj", "getViewHolder", "Landroidx/recyclerview/widget/RecyclerView$ViewHolder;", "view", "Landroid/view/View;", "viewType", "itemCount", "submitList", "", "newList", "", "DiffCallback", "community_debug"})
public final class EkoCreatePostImageAdapter extends com.ekoapp.ekosdk.uikit.base.EkoBaseRecyclerViewAdapter<com.ekoapp.ekosdk.uikit.community.newsfeed.model.FeedImage> implements com.ekoapp.ekosdk.uikit.community.newsfeed.adapter.IListItemChangeListener {
    private final com.ekoapp.ekosdk.uikit.community.newsfeed.listener.ICreatePostImageActionListener listener = null;
    
    @java.lang.Override()
    protected int getLayoutId(int position, @org.jetbrains.annotations.Nullable()
    com.ekoapp.ekosdk.uikit.community.newsfeed.model.FeedImage obj) {
        return 0;
    }
    
    @org.jetbrains.annotations.NotNull()
    @java.lang.Override()
    public androidx.recyclerview.widget.RecyclerView.ViewHolder getViewHolder(@org.jetbrains.annotations.NotNull()
    android.view.View view, int viewType) {
        return null;
    }
    
    public final void submitList(@org.jetbrains.annotations.NotNull()
    java.util.List<com.ekoapp.ekosdk.uikit.community.newsfeed.model.FeedImage> newList) {
    }
    
    @java.lang.Override()
    public int itemCount() {
        return 0;
    }
    
    public EkoCreatePostImageAdapter(@org.jetbrains.annotations.NotNull()
    com.ekoapp.ekosdk.uikit.community.newsfeed.listener.ICreatePostImageActionListener listener) {
        super();
    }
    
    @kotlin.Metadata(mv = {1, 1, 16}, bv = {1, 0, 3}, k = 1, d1 = {"\u0000$\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u000b\n\u0000\n\u0002\u0010\b\n\u0002\b\u0005\u0018\u00002\u00020\u0001B!\u0012\f\u0010\u0002\u001a\b\u0012\u0004\u0012\u00020\u00040\u0003\u0012\f\u0010\u0005\u001a\b\u0012\u0004\u0012\u00020\u00040\u0003\u00a2\u0006\u0002\u0010\u0006J\u0018\u0010\u0007\u001a\u00020\b2\u0006\u0010\t\u001a\u00020\n2\u0006\u0010\u000b\u001a\u00020\nH\u0016J\u0018\u0010\f\u001a\u00020\b2\u0006\u0010\t\u001a\u00020\n2\u0006\u0010\u000b\u001a\u00020\nH\u0016J\b\u0010\r\u001a\u00020\nH\u0016J\b\u0010\u000e\u001a\u00020\nH\u0016R\u0014\u0010\u0005\u001a\b\u0012\u0004\u0012\u00020\u00040\u0003X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0014\u0010\u0002\u001a\b\u0012\u0004\u0012\u00020\u00040\u0003X\u0082\u0004\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u000f"}, d2 = {"Lcom/ekoapp/ekosdk/uikit/community/newsfeed/adapter/EkoCreatePostImageAdapter$DiffCallback;", "Landroidx/recyclerview/widget/DiffUtil$Callback;", "oldList", "", "Lcom/ekoapp/ekosdk/uikit/community/newsfeed/model/FeedImage;", "newList", "(Ljava/util/List;Ljava/util/List;)V", "areContentsTheSame", "", "oldItemPosition", "", "newItemPosition", "areItemsTheSame", "getNewListSize", "getOldListSize", "community_debug"})
    public static final class DiffCallback extends androidx.recyclerview.widget.DiffUtil.Callback {
        private final java.util.List<com.ekoapp.ekosdk.uikit.community.newsfeed.model.FeedImage> oldList = null;
        private final java.util.List<com.ekoapp.ekosdk.uikit.community.newsfeed.model.FeedImage> newList = null;
        
        @java.lang.Override()
        public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
            return false;
        }
        
        @java.lang.Override()
        public int getOldListSize() {
            return 0;
        }
        
        @java.lang.Override()
        public int getNewListSize() {
            return 0;
        }
        
        @java.lang.Override()
        public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
            return false;
        }
        
        public DiffCallback(@org.jetbrains.annotations.NotNull()
        java.util.List<com.ekoapp.ekosdk.uikit.community.newsfeed.model.FeedImage> oldList, @org.jetbrains.annotations.NotNull()
        java.util.List<com.ekoapp.ekosdk.uikit.community.newsfeed.model.FeedImage> newList) {
            super();
        }
    }
}