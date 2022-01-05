package com.ekoapp.ekosdk.uikit.community.newsfeed.adapter;

import java.lang.System;

@kotlin.Metadata(mv = {1, 1, 16}, bv = {1, 0, 3}, k = 1, d1 = {"\u0000B\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0002\b\u0002\u0018\u00002\u00020\u0001B\u0017\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\b\u0010\u0004\u001a\u0004\u0018\u00010\u0005\u00a2\u0006\u0002\u0010\u0006J\u001a\u0010\u0011\u001a\u00020\u00122\b\u0010\u0013\u001a\u0004\u0018\u00010\u00142\u0006\u0010\u0015\u001a\u00020\u0016H\u0016J\b\u0010\u0017\u001a\u00020\u0016H\u0016R\u000e\u0010\u0007\u001a\u00020\bX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\t\u001a\u00020\nX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u0013\u0010\u0004\u001a\u0004\u0018\u00010\u0005\u00a2\u0006\b\n\u0000\u001a\u0004\b\u000b\u0010\fR\u000e\u0010\r\u001a\u00020\u0003X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u000e\u001a\u00020\u000fX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0010\u001a\u00020\bX\u0082\u0004\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u0018"}, d2 = {"Lcom/ekoapp/ekosdk/uikit/community/newsfeed/adapter/EkoCreatePostFileViewHolder;", "Lcom/ekoapp/ekosdk/uikit/community/newsfeed/adapter/EkoBasePostAttachmentViewHolder;", "itemView", "Landroid/view/View;", "listener", "Lcom/ekoapp/ekosdk/uikit/community/newsfeed/listener/ICreatePostFileActionListener;", "(Landroid/view/View;Lcom/ekoapp/ekosdk/uikit/community/newsfeed/listener/ICreatePostFileActionListener;)V", "errorFile", "Landroid/widget/ImageView;", "layoutPreparingFile", "Landroid/widget/LinearLayout;", "getListener", "()Lcom/ekoapp/ekosdk/uikit/community/newsfeed/listener/ICreatePostFileActionListener;", "overlayView", "progressBar", "Landroid/widget/ProgressBar;", "removeFile", "bind", "", "data", "Lcom/ekoapp/ekosdk/uikit/community/domain/model/FileAttachment;", "position", "", "getMaxCharacterLimit", "community_debug"})
public final class EkoCreatePostFileViewHolder extends com.ekoapp.ekosdk.uikit.community.newsfeed.adapter.EkoBasePostAttachmentViewHolder {
    private final android.widget.ImageView removeFile = null;
    private final android.widget.ImageView errorFile = null;
    private final android.view.View overlayView = null;
    private final android.widget.ProgressBar progressBar = null;
    private android.widget.LinearLayout layoutPreparingFile;
    @org.jetbrains.annotations.Nullable()
    private final com.ekoapp.ekosdk.uikit.community.newsfeed.listener.ICreatePostFileActionListener listener = null;
    
    @java.lang.Override()
    public void bind(@org.jetbrains.annotations.Nullable()
    com.ekoapp.ekosdk.uikit.community.domain.model.FileAttachment data, int position) {
    }
    
    @java.lang.Override()
    public int getMaxCharacterLimit() {
        return 0;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final com.ekoapp.ekosdk.uikit.community.newsfeed.listener.ICreatePostFileActionListener getListener() {
        return null;
    }
    
    public EkoCreatePostFileViewHolder(@org.jetbrains.annotations.NotNull()
    android.view.View itemView, @org.jetbrains.annotations.Nullable()
    com.ekoapp.ekosdk.uikit.community.newsfeed.listener.ICreatePostFileActionListener listener) {
        super(null, null);
    }
}