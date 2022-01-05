package com.ekoapp.ekosdk.uikit.community.explore.adapter;

import java.lang.System;

@kotlin.Metadata(mv = {1, 1, 16}, bv = {1, 0, 3}, k = 1, d1 = {"\u0000:\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0002\b\u0002\u0018\u00002\u00020\u0001B\u001f\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\b\u0010\u0004\u001a\u0004\u0018\u00010\u0005\u0012\u0006\u0010\u0006\u001a\u00020\u0007\u00a2\u0006\u0002\u0010\bJ\u001a\u0010\r\u001a\u00020\u000e2\b\u0010\u000f\u001a\u0004\u0018\u00010\u00102\u0006\u0010\u0011\u001a\u00020\u0012H\u0016J\u0010\u0010\u0013\u001a\u00020\u000e2\u0006\u0010\u000f\u001a\u00020\u0010H\u0002R\u000e\u0010\t\u001a\u00020\nX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0011\u0010\u0006\u001a\u00020\u0007\u00a2\u0006\b\n\u0000\u001a\u0004\b\u000b\u0010\f\u00a8\u0006\u0014"}, d2 = {"Lcom/ekoapp/ekosdk/uikit/community/explore/adapter/EkoCategoryItemTypeSelectorViewHolder;", "Lcom/ekoapp/ekosdk/uikit/community/explore/adapter/EkoCategoryItemViewHolder;", "itemView", "Landroid/view/View;", "itemClickListener", "Lcom/ekoapp/ekosdk/uikit/community/explore/listener/IEkoCategoryItemClickListener;", "selectionListener", "Lcom/ekoapp/ekosdk/uikit/community/explore/adapter/ICategorySelectionListener;", "(Landroid/view/View;Lcom/ekoapp/ekosdk/uikit/community/explore/listener/IEkoCategoryItemClickListener;Lcom/ekoapp/ekosdk/uikit/community/explore/adapter/ICategorySelectionListener;)V", "rbCategorySelection", "Lcom/google/android/material/radiobutton/MaterialRadioButton;", "getSelectionListener", "()Lcom/ekoapp/ekosdk/uikit/community/explore/adapter/ICategorySelectionListener;", "bind", "", "data", "Lcom/ekoapp/ekosdk/community/category/EkoCommunityCategory;", "position", "", "handleCategorySelection", "community_debug"})
public final class EkoCategoryItemTypeSelectorViewHolder extends com.ekoapp.ekosdk.uikit.community.explore.adapter.EkoCategoryItemViewHolder {
    private final com.google.android.material.radiobutton.MaterialRadioButton rbCategorySelection = null;
    @org.jetbrains.annotations.NotNull()
    private final com.ekoapp.ekosdk.uikit.community.explore.adapter.ICategorySelectionListener selectionListener = null;
    
    @java.lang.Override()
    public void bind(@org.jetbrains.annotations.Nullable()
    com.ekoapp.ekosdk.community.category.EkoCommunityCategory data, int position) {
    }
    
    private final void handleCategorySelection(com.ekoapp.ekosdk.community.category.EkoCommunityCategory data) {
    }
    
    @org.jetbrains.annotations.NotNull()
    public final com.ekoapp.ekosdk.uikit.community.explore.adapter.ICategorySelectionListener getSelectionListener() {
        return null;
    }
    
    public EkoCategoryItemTypeSelectorViewHolder(@org.jetbrains.annotations.NotNull()
    android.view.View itemView, @org.jetbrains.annotations.Nullable()
    com.ekoapp.ekosdk.uikit.community.explore.listener.IEkoCategoryItemClickListener itemClickListener, @org.jetbrains.annotations.NotNull()
    com.ekoapp.ekosdk.uikit.community.explore.adapter.ICategorySelectionListener selectionListener) {
        super(null, null);
    }
}