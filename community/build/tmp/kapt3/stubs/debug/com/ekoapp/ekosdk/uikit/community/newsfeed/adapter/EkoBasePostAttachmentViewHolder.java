package com.ekoapp.ekosdk.uikit.community.newsfeed.adapter;

import java.lang.System;

@kotlin.Metadata(mv = {1, 1, 16}, bv = {1, 0, 3}, k = 1, d1 = {"\u0000D\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0002\b\u0002\n\u0002\u0010\b\n\u0002\b\u0004\n\u0002\u0010\u000e\n\u0000\b\u0016\u0018\u00002\u00020\u00012\b\u0012\u0004\u0012\u00020\u00030\u0002B\u0019\b\u0016\u0012\u0006\u0010\u0004\u001a\u00020\u0005\u0012\b\u0010\u0006\u001a\u0004\u0018\u00010\u0007\u00a2\u0006\u0002\u0010\bB\r\u0012\u0006\u0010\u0004\u001a\u00020\u0005\u00a2\u0006\u0002\u0010\tJ\u001a\u0010\u000f\u001a\u00020\u00102\b\u0010\u0011\u001a\u0004\u0018\u00010\u00032\u0006\u0010\u0012\u001a\u00020\u0013H\u0016J\b\u0010\u0014\u001a\u00020\u0013H\u0016J\u0010\u0010\u0015\u001a\u00020\u00102\u0006\u0010\u0011\u001a\u00020\u0003H\u0002J\u0010\u0010\u0016\u001a\u00020\u00102\u0006\u0010\u0017\u001a\u00020\u0018H\u0002R\u000e\u0010\n\u001a\u00020\u000bX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\f\u001a\u00020\rX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u000e\u001a\u00020\rX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u0010\u0010\u0006\u001a\u0004\u0018\u00010\u0007X\u0082\u000e\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u0019"}, d2 = {"Lcom/ekoapp/ekosdk/uikit/community/newsfeed/adapter/EkoBasePostAttachmentViewHolder;", "Landroidx/recyclerview/widget/RecyclerView$ViewHolder;", "Lcom/ekoapp/ekosdk/uikit/base/EkoBaseRecyclerViewAdapter$IBinder;", "Lcom/ekoapp/ekosdk/uikit/community/domain/model/FileAttachment;", "itemView", "Landroid/view/View;", "itemClickListener", "Lcom/ekoapp/ekosdk/uikit/community/newsfeed/listener/IPostFileItemClickListener;", "(Landroid/view/View;Lcom/ekoapp/ekosdk/uikit/community/newsfeed/listener/IPostFileItemClickListener;)V", "(Landroid/view/View;)V", "fileIcon", "Landroid/widget/ImageView;", "fileName", "Landroid/widget/TextView;", "fileSize", "bind", "", "data", "position", "", "getMaxCharacterLimit", "setFileIcon", "setFileName", "originalName", "", "community_debug"})
public class EkoBasePostAttachmentViewHolder extends androidx.recyclerview.widget.RecyclerView.ViewHolder implements com.ekoapp.ekosdk.uikit.base.EkoBaseRecyclerViewAdapter.IBinder<com.ekoapp.ekosdk.uikit.community.domain.model.FileAttachment> {
    private android.widget.TextView fileName;
    private android.widget.TextView fileSize;
    private final android.widget.ImageView fileIcon = null;
    private com.ekoapp.ekosdk.uikit.community.newsfeed.listener.IPostFileItemClickListener itemClickListener;
    
    @java.lang.Override()
    public void bind(@org.jetbrains.annotations.Nullable()
    com.ekoapp.ekosdk.uikit.community.domain.model.FileAttachment data, int position) {
    }
    
    public int getMaxCharacterLimit() {
        return 0;
    }
    
    private final void setFileIcon(com.ekoapp.ekosdk.uikit.community.domain.model.FileAttachment data) {
    }
    
    private final void setFileName(java.lang.String originalName) {
    }
    
    public EkoBasePostAttachmentViewHolder(@org.jetbrains.annotations.NotNull()
    android.view.View itemView) {
        super(null);
    }
    
    public EkoBasePostAttachmentViewHolder(@org.jetbrains.annotations.NotNull()
    android.view.View itemView, @org.jetbrains.annotations.Nullable()
    com.ekoapp.ekosdk.uikit.community.newsfeed.listener.IPostFileItemClickListener itemClickListener) {
        super(null);
    }
}