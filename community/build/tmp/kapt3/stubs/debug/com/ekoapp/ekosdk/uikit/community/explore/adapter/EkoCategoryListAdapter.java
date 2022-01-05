package com.ekoapp.ekosdk.uikit.community.explore.adapter;

import java.lang.System;

@kotlin.Metadata(mv = {1, 1, 16}, bv = {1, 0, 3}, k = 1, d1 = {"\u0000J\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000b\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0004\n\u0002\u0010\b\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0002\b\u0002\u0018\u00002\b\u0012\u0004\u0012\u00020\u00020\u00012\u00020\u0003:\u0001\u001bB+\b\u0016\u0012\u0006\u0010\u0004\u001a\u00020\u0005\u0012\b\u0010\u0006\u001a\u0004\u0018\u00010\u0007\u0012\u0006\u0010\b\u001a\u00020\t\u0012\b\u0010\n\u001a\u0004\u0018\u00010\u000b\u00a2\u0006\u0002\u0010\fB\u0017\u0012\u0006\u0010\u0004\u001a\u00020\u0005\u0012\b\u0010\u0006\u001a\u0004\u0018\u00010\u0007\u00a2\u0006\u0002\u0010\rJ\u001a\u0010\u000f\u001a\u00020\u00102\u0006\u0010\u0011\u001a\u00020\u00102\b\u0010\u0012\u001a\u0004\u0018\u00010\u0002H\u0014J\n\u0010\u0013\u001a\u0004\u0018\u00010\u000bH\u0016J\u0018\u0010\u0014\u001a\u00020\u00152\u0006\u0010\u0016\u001a\u00020\u00172\u0006\u0010\u0018\u001a\u00020\u0010H\u0016J\u0012\u0010\u0019\u001a\u00020\u001a2\b\u0010\u000e\u001a\u0004\u0018\u00010\u000bH\u0016R\u0010\u0010\u0006\u001a\u0004\u0018\u00010\u0007X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\b\u001a\u00020\tX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u0010\u0010\u000e\u001a\u0004\u0018\u00010\u000bX\u0082\u000e\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u001c"}, d2 = {"Lcom/ekoapp/ekosdk/uikit/community/explore/adapter/EkoCategoryListAdapter;", "Lcom/ekoapp/ekosdk/uikit/base/EkoBaseRecyclerViewPagedAdapter;", "Lcom/ekoapp/ekosdk/community/category/EkoCommunityCategory;", "Lcom/ekoapp/ekosdk/uikit/community/explore/adapter/ICategorySelectionListener;", "diffUtil", "Lcom/ekoapp/ekosdk/uikit/community/explore/adapter/EkoCategoryListAdapter$EkoCategoryListDiffUtil;", "itemClickListener", "Lcom/ekoapp/ekosdk/uikit/community/explore/listener/IEkoCategoryItemClickListener;", "modeSelection", "", "preSelectedCategory", "", "(Lcom/ekoapp/ekosdk/uikit/community/explore/adapter/EkoCategoryListAdapter$EkoCategoryListDiffUtil;Lcom/ekoapp/ekosdk/uikit/community/explore/listener/IEkoCategoryItemClickListener;ZLjava/lang/String;)V", "(Lcom/ekoapp/ekosdk/uikit/community/explore/adapter/EkoCategoryListAdapter$EkoCategoryListDiffUtil;Lcom/ekoapp/ekosdk/uikit/community/explore/listener/IEkoCategoryItemClickListener;)V", "selectedCategory", "getLayoutId", "", "position", "obj", "getSelection", "getViewHolder", "Landroidx/recyclerview/widget/RecyclerView$ViewHolder;", "view", "Landroid/view/View;", "viewType", "setSelection", "", "EkoCategoryListDiffUtil", "community_debug"})
public final class EkoCategoryListAdapter extends com.ekoapp.ekosdk.uikit.base.EkoBaseRecyclerViewPagedAdapter<com.ekoapp.ekosdk.community.category.EkoCommunityCategory> implements com.ekoapp.ekosdk.uikit.community.explore.adapter.ICategorySelectionListener {
    private java.lang.String selectedCategory;
    private boolean modeSelection = false;
    private final com.ekoapp.ekosdk.uikit.community.explore.listener.IEkoCategoryItemClickListener itemClickListener = null;
    
    @java.lang.Override()
    protected int getLayoutId(int position, @org.jetbrains.annotations.Nullable()
    com.ekoapp.ekosdk.community.category.EkoCommunityCategory obj) {
        return 0;
    }
    
    @org.jetbrains.annotations.NotNull()
    @java.lang.Override()
    public androidx.recyclerview.widget.RecyclerView.ViewHolder getViewHolder(@org.jetbrains.annotations.NotNull()
    android.view.View view, int viewType) {
        return null;
    }
    
    @java.lang.Override()
    public void setSelection(@org.jetbrains.annotations.Nullable()
    java.lang.String selectedCategory) {
    }
    
    @org.jetbrains.annotations.Nullable()
    @java.lang.Override()
    public java.lang.String getSelection() {
        return null;
    }
    
    public EkoCategoryListAdapter(@org.jetbrains.annotations.NotNull()
    com.ekoapp.ekosdk.uikit.community.explore.adapter.EkoCategoryListAdapter.EkoCategoryListDiffUtil diffUtil, @org.jetbrains.annotations.Nullable()
    com.ekoapp.ekosdk.uikit.community.explore.listener.IEkoCategoryItemClickListener itemClickListener) {
        super(null);
    }
    
    public EkoCategoryListAdapter(@org.jetbrains.annotations.NotNull()
    com.ekoapp.ekosdk.uikit.community.explore.adapter.EkoCategoryListAdapter.EkoCategoryListDiffUtil diffUtil, @org.jetbrains.annotations.Nullable()
    com.ekoapp.ekosdk.uikit.community.explore.listener.IEkoCategoryItemClickListener itemClickListener, boolean modeSelection, @org.jetbrains.annotations.Nullable()
    java.lang.String preSelectedCategory) {
        super(null);
    }
    
    @kotlin.Metadata(mv = {1, 1, 16}, bv = {1, 0, 3}, k = 1, d1 = {"\u0000\u0018\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u000b\n\u0002\b\u0004\u0018\u00002\b\u0012\u0004\u0012\u00020\u00020\u0001B\u0005\u00a2\u0006\u0002\u0010\u0003J\u0018\u0010\u0004\u001a\u00020\u00052\u0006\u0010\u0006\u001a\u00020\u00022\u0006\u0010\u0007\u001a\u00020\u0002H\u0016J\u0018\u0010\b\u001a\u00020\u00052\u0006\u0010\u0006\u001a\u00020\u00022\u0006\u0010\u0007\u001a\u00020\u0002H\u0016\u00a8\u0006\t"}, d2 = {"Lcom/ekoapp/ekosdk/uikit/community/explore/adapter/EkoCategoryListAdapter$EkoCategoryListDiffUtil;", "Landroidx/recyclerview/widget/DiffUtil$ItemCallback;", "Lcom/ekoapp/ekosdk/community/category/EkoCommunityCategory;", "()V", "areContentsTheSame", "", "oldItem", "newItem", "areItemsTheSame", "community_debug"})
    public static final class EkoCategoryListDiffUtil extends androidx.recyclerview.widget.DiffUtil.ItemCallback<com.ekoapp.ekosdk.community.category.EkoCommunityCategory> {
        
        @java.lang.Override()
        public boolean areItemsTheSame(@org.jetbrains.annotations.NotNull()
        com.ekoapp.ekosdk.community.category.EkoCommunityCategory oldItem, @org.jetbrains.annotations.NotNull()
        com.ekoapp.ekosdk.community.category.EkoCommunityCategory newItem) {
            return false;
        }
        
        @java.lang.Override()
        public boolean areContentsTheSame(@org.jetbrains.annotations.NotNull()
        com.ekoapp.ekosdk.community.category.EkoCommunityCategory oldItem, @org.jetbrains.annotations.NotNull()
        com.ekoapp.ekosdk.community.category.EkoCommunityCategory newItem) {
            return false;
        }
        
        public EkoCategoryListDiffUtil() {
            super();
        }
    }
}