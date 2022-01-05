package com.ekoapp.ekosdk.uikit.community.newsfeed.adapter;

import java.lang.System;

@kotlin.Metadata(mv = {1, 1, 16}, bv = {1, 0, 3}, k = 1, d1 = {"\u0000R\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\b\n\u0002\u0010\u0007\n\u0000\n\u0002\u0010\u000b\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\t\n\u0002\u0010\u000e\n\u0000\n\u0002\u0010\b\n\u0002\b\u0002\b&\u0018\u00002\u00020\u00012\b\u0012\u0004\u0012\u00020\u00030\u0002B%\u0012\u0006\u0010\u0004\u001a\u00020\u0005\u0012\f\u0010\u0006\u001a\b\u0012\u0004\u0012\u00020\b0\u0007\u0012\b\u0010\t\u001a\u0004\u0018\u00010\n\u00a2\u0006\u0002\u0010\u000bJ\u0010\u0010\u0012\u001a\u00020\u00132\u0006\u0010\u0014\u001a\u00020\u0015H\u0002J\u0015\u0010\u0016\u001a\u00020\u00172\u0006\u0010\u0018\u001a\u00020\u0019H\u0000\u00a2\u0006\u0002\b\u001aJ5\u0010\u001b\u001a\u00020\u00172\u0006\u0010\u0018\u001a\u00020\u00192\u0006\u0010\u001c\u001a\u00020\u00152\u0006\u0010\u001d\u001a\u00020\u00152\u0006\u0010\u001e\u001a\u00020\u00152\u0006\u0010\u001f\u001a\u00020\u0015H\u0000\u00a2\u0006\u0002\b J%\u0010!\u001a\u00020\u00172\u0006\u0010\u0018\u001a\u00020\u00192\u0006\u0010\"\u001a\u00020#2\u0006\u0010$\u001a\u00020%H\u0000\u00a2\u0006\u0002\b&R\u0017\u0010\u0006\u001a\b\u0012\u0004\u0012\u00020\b0\u0007\u00a2\u0006\b\n\u0000\u001a\u0004\b\f\u0010\rR\u0013\u0010\t\u001a\u0004\u0018\u00010\n\u00a2\u0006\b\n\u0000\u001a\u0004\b\u000e\u0010\u000fR\u0011\u0010\u0004\u001a\u00020\u0005\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0010\u0010\u0011\u00a8\u0006\'"}, d2 = {"Lcom/ekoapp/ekosdk/uikit/community/newsfeed/adapter/BasePostImageChildrenViewHolder;", "Landroidx/recyclerview/widget/RecyclerView$ViewHolder;", "Lcom/ekoapp/ekosdk/uikit/base/EkoBaseRecyclerViewAdapter$IBinder;", "Lcom/ekoapp/ekosdk/uikit/community/newsfeed/adapter/PostImageChildrenItem;", "view", "Landroid/view/View;", "images", "", "Lcom/ekoapp/ekosdk/file/EkoImage;", "itemClickListener", "Lcom/ekoapp/ekosdk/uikit/community/newsfeed/listener/IPostImageClickListener;", "(Landroid/view/View;Ljava/util/List;Lcom/ekoapp/ekosdk/uikit/community/newsfeed/listener/IPostImageClickListener;)V", "getImages", "()Ljava/util/List;", "getItemClickListener", "()Lcom/ekoapp/ekosdk/uikit/community/newsfeed/listener/IPostImageClickListener;", "getView", "()Landroid/view/View;", "getImageCornerRadius", "", "isRounded", "", "setBackgroundColor", "", "imageView", "Lcom/google/android/material/imageview/ShapeableImageView;", "setBackgroundColor$community_debug", "setCornerRadius", "topLeft", "topRight", "bottomLeft", "bottomRight", "setCornerRadius$community_debug", "setImage", "imageUrl", "", "position", "", "setImage$community_debug", "community_debug"})
public abstract class BasePostImageChildrenViewHolder extends androidx.recyclerview.widget.RecyclerView.ViewHolder implements com.ekoapp.ekosdk.uikit.base.EkoBaseRecyclerViewAdapter.IBinder<com.ekoapp.ekosdk.uikit.community.newsfeed.adapter.PostImageChildrenItem> {
    @org.jetbrains.annotations.NotNull()
    private final android.view.View view = null;
    @org.jetbrains.annotations.NotNull()
    private final java.util.List<com.ekoapp.ekosdk.file.EkoImage> images = null;
    @org.jetbrains.annotations.Nullable()
    private final com.ekoapp.ekosdk.uikit.community.newsfeed.listener.IPostImageClickListener itemClickListener = null;
    
    public final void setCornerRadius$community_debug(@org.jetbrains.annotations.NotNull()
    com.google.android.material.imageview.ShapeableImageView imageView, boolean topLeft, boolean topRight, boolean bottomLeft, boolean bottomRight) {
    }
    
    public final void setBackgroundColor$community_debug(@org.jetbrains.annotations.NotNull()
    com.google.android.material.imageview.ShapeableImageView imageView) {
    }
    
    public final void setImage$community_debug(@org.jetbrains.annotations.NotNull()
    com.google.android.material.imageview.ShapeableImageView imageView, @org.jetbrains.annotations.NotNull()
    java.lang.String imageUrl, int position) {
    }
    
    private final float getImageCornerRadius(boolean isRounded) {
        return 0.0F;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final android.view.View getView() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.util.List<com.ekoapp.ekosdk.file.EkoImage> getImages() {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final com.ekoapp.ekosdk.uikit.community.newsfeed.listener.IPostImageClickListener getItemClickListener() {
        return null;
    }
    
    public BasePostImageChildrenViewHolder(@org.jetbrains.annotations.NotNull()
    android.view.View view, @org.jetbrains.annotations.NotNull()
    java.util.List<com.ekoapp.ekosdk.file.EkoImage> images, @org.jetbrains.annotations.Nullable()
    com.ekoapp.ekosdk.uikit.community.newsfeed.listener.IPostImageClickListener itemClickListener) {
        super(null);
    }
}