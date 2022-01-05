package com.ekoapp.ekosdk.uikit.community.newsfeed.adapter;

import java.lang.System;

@kotlin.Metadata(mv = {1, 1, 16}, bv = {1, 0, 3}, k = 1, d1 = {"\u0000>\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\b\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0000\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0002\b\u0002\u0018\u00002\b\u0012\u0004\u0012\u00020\u00020\u0001:\u0001\u0014B\u000f\u0012\b\u0010\u0003\u001a\u0004\u0018\u00010\u0004\u00a2\u0006\u0002\u0010\u0005J\u001a\u0010\u0006\u001a\u00020\u00072\u0006\u0010\b\u001a\u00020\u00072\b\u0010\t\u001a\u0004\u0018\u00010\u0002H\u0014J\u0018\u0010\n\u001a\u00020\u000b2\u0006\u0010\f\u001a\u00020\r2\u0006\u0010\u000e\u001a\u00020\u0007H\u0016J\u0014\u0010\u000f\u001a\u00020\u00102\f\u0010\u0011\u001a\b\u0012\u0004\u0012\u00020\u00130\u0012R\u0010\u0010\u0003\u001a\u0004\u0018\u00010\u0004X\u0082\u0004\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u0015"}, d2 = {"Lcom/ekoapp/ekosdk/uikit/community/newsfeed/adapter/PostImageChildrenAdapter;", "Lcom/ekoapp/ekosdk/uikit/base/EkoBaseRecyclerViewAdapter;", "Lcom/ekoapp/ekosdk/uikit/community/newsfeed/adapter/PostImageChildrenItem;", "itemClickListener", "Lcom/ekoapp/ekosdk/uikit/community/newsfeed/listener/IPostImageClickListener;", "(Lcom/ekoapp/ekosdk/uikit/community/newsfeed/listener/IPostImageClickListener;)V", "getLayoutId", "", "position", "item", "getViewHolder", "Lcom/ekoapp/ekosdk/uikit/community/newsfeed/adapter/BasePostImageChildrenViewHolder;", "view", "Landroid/view/View;", "viewType", "submitList", "", "images", "", "Lcom/ekoapp/ekosdk/file/EkoImage;", "DiffCallback", "community_debug"})
public final class PostImageChildrenAdapter extends com.ekoapp.ekosdk.uikit.base.EkoBaseRecyclerViewAdapter<com.ekoapp.ekosdk.uikit.community.newsfeed.adapter.PostImageChildrenItem> {
    private final com.ekoapp.ekosdk.uikit.community.newsfeed.listener.IPostImageClickListener itemClickListener = null;
    
    @java.lang.Override()
    protected int getLayoutId(int position, @org.jetbrains.annotations.Nullable()
    com.ekoapp.ekosdk.uikit.community.newsfeed.adapter.PostImageChildrenItem item) {
        return 0;
    }
    
    @org.jetbrains.annotations.NotNull()
    @java.lang.Override()
    public com.ekoapp.ekosdk.uikit.community.newsfeed.adapter.BasePostImageChildrenViewHolder getViewHolder(@org.jetbrains.annotations.NotNull()
    android.view.View view, int viewType) {
        return null;
    }
    
    public final void submitList(@org.jetbrains.annotations.NotNull()
    java.util.List<com.ekoapp.ekosdk.file.EkoImage> images) {
    }
    
    public PostImageChildrenAdapter(@org.jetbrains.annotations.Nullable()
    com.ekoapp.ekosdk.uikit.community.newsfeed.listener.IPostImageClickListener itemClickListener) {
        super();
    }
    
    @kotlin.Metadata(mv = {1, 1, 16}, bv = {1, 0, 3}, k = 1, d1 = {"\u0000,\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u000b\n\u0000\n\u0002\u0010\b\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0005\u0018\u00002\u00020\u0001B!\u0012\f\u0010\u0002\u001a\b\u0012\u0004\u0012\u00020\u00040\u0003\u0012\f\u0010\u0005\u001a\b\u0012\u0004\u0012\u00020\u00040\u0003\u00a2\u0006\u0002\u0010\u0006J\u0018\u0010\u0007\u001a\u00020\b2\u0006\u0010\t\u001a\u00020\n2\u0006\u0010\u000b\u001a\u00020\nH\u0016J$\u0010\f\u001a\u00020\b2\f\u0010\r\u001a\b\u0012\u0004\u0012\u00020\u000e0\u00032\f\u0010\u000f\u001a\b\u0012\u0004\u0012\u00020\u000e0\u0003H\u0002J\u0018\u0010\u0010\u001a\u00020\b2\u0006\u0010\t\u001a\u00020\n2\u0006\u0010\u000b\u001a\u00020\nH\u0016J\b\u0010\u0011\u001a\u00020\nH\u0016J\b\u0010\u0012\u001a\u00020\nH\u0016R\u0014\u0010\u0005\u001a\b\u0012\u0004\u0012\u00020\u00040\u0003X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0014\u0010\u0002\u001a\b\u0012\u0004\u0012\u00020\u00040\u0003X\u0082\u0004\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u0013"}, d2 = {"Lcom/ekoapp/ekosdk/uikit/community/newsfeed/adapter/PostImageChildrenAdapter$DiffCallback;", "Landroidx/recyclerview/widget/DiffUtil$Callback;", "oldList", "", "Lcom/ekoapp/ekosdk/uikit/community/newsfeed/adapter/PostImageChildrenItem;", "newList", "(Ljava/util/List;Ljava/util/List;)V", "areContentsTheSame", "", "oldItemPosition", "", "newItemPosition", "areImagesTheSame", "oldImages", "Lcom/ekoapp/ekosdk/file/EkoImage;", "newImages", "areItemsTheSame", "getNewListSize", "getOldListSize", "community_debug"})
    public static final class DiffCallback extends androidx.recyclerview.widget.DiffUtil.Callback {
        private final java.util.List<com.ekoapp.ekosdk.uikit.community.newsfeed.adapter.PostImageChildrenItem> oldList = null;
        private final java.util.List<com.ekoapp.ekosdk.uikit.community.newsfeed.adapter.PostImageChildrenItem> newList = null;
        
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
        
        private final boolean areImagesTheSame(java.util.List<com.ekoapp.ekosdk.file.EkoImage> oldImages, java.util.List<com.ekoapp.ekosdk.file.EkoImage> newImages) {
            return false;
        }
        
        public DiffCallback(@org.jetbrains.annotations.NotNull()
        java.util.List<com.ekoapp.ekosdk.uikit.community.newsfeed.adapter.PostImageChildrenItem> oldList, @org.jetbrains.annotations.NotNull()
        java.util.List<com.ekoapp.ekosdk.uikit.community.newsfeed.adapter.PostImageChildrenItem> newList) {
            super();
        }
    }
}