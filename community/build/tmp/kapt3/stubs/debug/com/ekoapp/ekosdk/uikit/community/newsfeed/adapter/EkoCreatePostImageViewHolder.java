package com.ekoapp.ekosdk.uikit.community.newsfeed.adapter;

import java.lang.System;

@kotlin.Metadata(mv = {1, 1, 16}, bv = {1, 0, 3}, k = 1, d1 = {"\u0000T\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\n\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0007\n\u0002\b\u0004\n\u0002\u0010\u0002\n\u0002\b\u0002\n\u0002\u0010\b\n\u0002\b\u0005\u0018\u00002\u00020\u00012\b\u0012\u0004\u0012\u00020\u00030\u0002B\u001d\u0012\u0006\u0010\u0004\u001a\u00020\u0005\u0012\u0006\u0010\u0006\u001a\u00020\u0007\u0012\u0006\u0010\b\u001a\u00020\t\u00a2\u0006\u0002\u0010\nJ\u001a\u0010!\u001a\u00020\"2\b\u0010#\u001a\u0004\u0018\u00010\u00032\u0006\u0010$\u001a\u00020%H\u0016J\u0010\u0010&\u001a\u00020%2\u0006\u0010\'\u001a\u00020%H\u0002J\u0010\u0010(\u001a\u00020%2\u0006\u0010\'\u001a\u00020%H\u0002J\b\u0010)\u001a\u00020\"H\u0002R\u0011\u0010\u000b\u001a\u00020\f\u00a2\u0006\b\n\u0000\u001a\u0004\b\r\u0010\u000eR\u001a\u0010\u000f\u001a\u00020\u0010X\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\u0011\u0010\u0012\"\u0004\b\u0013\u0010\u0014R\u000e\u0010\b\u001a\u00020\tX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u0011\u0010\u0006\u001a\u00020\u0007\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0015\u0010\u0016R\u001a\u0010\u0017\u001a\u00020\u0010X\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\u0018\u0010\u0012\"\u0004\b\u0019\u0010\u0014R\u000e\u0010\u001a\u001a\u00020\u001bX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u001c\u001a\u00020\u001dX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u001a\u0010\u001e\u001a\u00020\u0010X\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\u001f\u0010\u0012\"\u0004\b \u0010\u0014\u00a8\u0006*"}, d2 = {"Lcom/ekoapp/ekosdk/uikit/community/newsfeed/adapter/EkoCreatePostImageViewHolder;", "Landroidx/recyclerview/widget/RecyclerView$ViewHolder;", "Lcom/ekoapp/ekosdk/uikit/base/EkoBaseRecyclerViewAdapter$IBinder;", "Lcom/ekoapp/ekosdk/uikit/community/newsfeed/model/FeedImage;", "itemView", "Landroid/view/View;", "listener", "Lcom/ekoapp/ekosdk/uikit/community/newsfeed/listener/ICreatePostImageActionListener;", "itemChangeListener", "Lcom/ekoapp/ekosdk/uikit/community/newsfeed/adapter/IListItemChangeListener;", "(Landroid/view/View;Lcom/ekoapp/ekosdk/uikit/community/newsfeed/listener/ICreatePostImageActionListener;Lcom/ekoapp/ekosdk/uikit/community/newsfeed/adapter/IListItemChangeListener;)V", "container", "Landroidx/constraintlayout/widget/ConstraintLayout;", "getContainer", "()Landroidx/constraintlayout/widget/ConstraintLayout;", "errorPhoto", "Lcom/google/android/material/imageview/ShapeableImageView;", "getErrorPhoto", "()Lcom/google/android/material/imageview/ShapeableImageView;", "setErrorPhoto", "(Lcom/google/android/material/imageview/ShapeableImageView;)V", "getListener", "()Lcom/ekoapp/ekosdk/uikit/community/newsfeed/listener/ICreatePostImageActionListener;", "photo", "getPhoto", "setPhoto", "progrssBar", "Landroid/widget/ProgressBar;", "radius", "", "removePhoto", "getRemovePhoto", "setRemovePhoto", "bind", "", "data", "position", "", "getHeight", "itemCount", "getWidth", "setupShape", "community_debug"})
public final class EkoCreatePostImageViewHolder extends androidx.recyclerview.widget.RecyclerView.ViewHolder implements com.ekoapp.ekosdk.uikit.base.EkoBaseRecyclerViewAdapter.IBinder<com.ekoapp.ekosdk.uikit.community.newsfeed.model.FeedImage> {
    @org.jetbrains.annotations.NotNull()
    private final androidx.constraintlayout.widget.ConstraintLayout container = null;
    @org.jetbrains.annotations.NotNull()
    private com.google.android.material.imageview.ShapeableImageView photo;
    @org.jetbrains.annotations.NotNull()
    private com.google.android.material.imageview.ShapeableImageView removePhoto;
    @org.jetbrains.annotations.NotNull()
    private com.google.android.material.imageview.ShapeableImageView errorPhoto;
    private final android.widget.ProgressBar progrssBar = null;
    private final float radius = 0.0F;
    @org.jetbrains.annotations.NotNull()
    private final com.ekoapp.ekosdk.uikit.community.newsfeed.listener.ICreatePostImageActionListener listener = null;
    private com.ekoapp.ekosdk.uikit.community.newsfeed.adapter.IListItemChangeListener itemChangeListener;
    
    @org.jetbrains.annotations.NotNull()
    public final androidx.constraintlayout.widget.ConstraintLayout getContainer() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final com.google.android.material.imageview.ShapeableImageView getPhoto() {
        return null;
    }
    
    public final void setPhoto(@org.jetbrains.annotations.NotNull()
    com.google.android.material.imageview.ShapeableImageView p0) {
    }
    
    @org.jetbrains.annotations.NotNull()
    public final com.google.android.material.imageview.ShapeableImageView getRemovePhoto() {
        return null;
    }
    
    public final void setRemovePhoto(@org.jetbrains.annotations.NotNull()
    com.google.android.material.imageview.ShapeableImageView p0) {
    }
    
    @org.jetbrains.annotations.NotNull()
    public final com.google.android.material.imageview.ShapeableImageView getErrorPhoto() {
        return null;
    }
    
    public final void setErrorPhoto(@org.jetbrains.annotations.NotNull()
    com.google.android.material.imageview.ShapeableImageView p0) {
    }
    
    @java.lang.Override()
    public void bind(@org.jetbrains.annotations.Nullable()
    com.ekoapp.ekosdk.uikit.community.newsfeed.model.FeedImage data, int position) {
    }
    
    private final void setupShape() {
    }
    
    private final int getHeight(int itemCount) {
        return 0;
    }
    
    private final int getWidth(int itemCount) {
        return 0;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final com.ekoapp.ekosdk.uikit.community.newsfeed.listener.ICreatePostImageActionListener getListener() {
        return null;
    }
    
    public EkoCreatePostImageViewHolder(@org.jetbrains.annotations.NotNull()
    android.view.View itemView, @org.jetbrains.annotations.NotNull()
    com.ekoapp.ekosdk.uikit.community.newsfeed.listener.ICreatePostImageActionListener listener, @org.jetbrains.annotations.NotNull()
    com.ekoapp.ekosdk.uikit.community.newsfeed.adapter.IListItemChangeListener itemChangeListener) {
        super(null);
    }
}