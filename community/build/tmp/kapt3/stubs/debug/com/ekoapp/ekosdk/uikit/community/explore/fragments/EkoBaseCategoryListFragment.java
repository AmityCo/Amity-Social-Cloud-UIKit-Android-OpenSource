package com.ekoapp.ekosdk.uikit.community.explore.fragments;

import java.lang.System;

@kotlin.Metadata(mv = {1, 1, 16}, bv = {1, 0, 3}, k = 1, d1 = {"\u0000H\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0010\u0002\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0006\b&\u0018\u00002\u00020\u00012\u00020\u0002B\u0007\b\u0000\u00a2\u0006\u0002\u0010\u0003J\b\u0010\f\u001a\u00020\rH\u0002J\b\u0010\u000e\u001a\u00020\u0005H&J\b\u0010\u000f\u001a\u00020\rH\u0002J\u0010\u0010\u0010\u001a\u00020\r2\u0006\u0010\u0011\u001a\u00020\u0012H\u0016J\u0012\u0010\u0013\u001a\u00020\r2\b\u0010\u0014\u001a\u0004\u0018\u00010\u0015H\u0016J&\u0010\u0016\u001a\u0004\u0018\u00010\u00172\u0006\u0010\u0018\u001a\u00020\u00192\b\u0010\u001a\u001a\u0004\u0018\u00010\u001b2\b\u0010\u0014\u001a\u0004\u0018\u00010\u0015H\u0016J\u001a\u0010\u001c\u001a\u00020\r2\u0006\u0010\u001d\u001a\u00020\u00172\b\u0010\u0014\u001a\u0004\u0018\u00010\u0015H\u0016J\u000e\u0010\u001e\u001a\u00020\r2\u0006\u0010\u001f\u001a\u00020\u0002J\b\u0010 \u001a\u00020\rH\u0002R\u000e\u0010\u0004\u001a\u00020\u0005X\u0082.\u00a2\u0006\u0002\n\u0000R\u001a\u0010\u0006\u001a\u00020\u0007X\u0080.\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\b\u0010\t\"\u0004\b\n\u0010\u000b\u00a8\u0006!"}, d2 = {"Lcom/ekoapp/ekosdk/uikit/community/explore/fragments/EkoBaseCategoryListFragment;", "Lcom/ekoapp/ekosdk/uikit/base/EkoBaseFragment;", "Lcom/ekoapp/ekosdk/uikit/community/explore/listener/IEkoCategoryItemClickListener;", "()V", "adapter", "Lcom/ekoapp/ekosdk/uikit/community/explore/adapter/EkoCategoryListAdapter;", "mViewModel", "Lcom/ekoapp/ekosdk/uikit/community/explore/viewmodel/EkoCategoryListViewModel;", "getMViewModel$community_debug", "()Lcom/ekoapp/ekosdk/uikit/community/explore/viewmodel/EkoCategoryListViewModel;", "setMViewModel$community_debug", "(Lcom/ekoapp/ekosdk/uikit/community/explore/viewmodel/EkoCategoryListViewModel;)V", "getCategories", "", "getCategoryListAdapter", "initView", "onCategorySelected", "category", "Lcom/ekoapp/ekosdk/community/category/EkoCommunityCategory;", "onCreate", "savedInstanceState", "Landroid/os/Bundle;", "onCreateView", "Landroid/view/View;", "inflater", "Landroid/view/LayoutInflater;", "container", "Landroid/view/ViewGroup;", "onViewCreated", "view", "setCategoryItemClickListener", "listener", "setupToolBar", "community_debug"})
public abstract class EkoBaseCategoryListFragment extends com.ekoapp.ekosdk.uikit.base.EkoBaseFragment implements com.ekoapp.ekosdk.uikit.community.explore.listener.IEkoCategoryItemClickListener {
    @org.jetbrains.annotations.NotNull()
    public com.ekoapp.ekosdk.uikit.community.explore.viewmodel.EkoCategoryListViewModel mViewModel;
    private com.ekoapp.ekosdk.uikit.community.explore.adapter.EkoCategoryListAdapter adapter;
    private java.util.HashMap _$_findViewCache;
    
    @org.jetbrains.annotations.NotNull()
    public final com.ekoapp.ekosdk.uikit.community.explore.viewmodel.EkoCategoryListViewModel getMViewModel$community_debug() {
        return null;
    }
    
    public final void setMViewModel$community_debug(@org.jetbrains.annotations.NotNull()
    com.ekoapp.ekosdk.uikit.community.explore.viewmodel.EkoCategoryListViewModel p0) {
    }
    
    @java.lang.Override()
    public void onCreate(@org.jetbrains.annotations.Nullable()
    android.os.Bundle savedInstanceState) {
    }
    
    @org.jetbrains.annotations.NotNull()
    public abstract com.ekoapp.ekosdk.uikit.community.explore.adapter.EkoCategoryListAdapter getCategoryListAdapter();
    
    private final void setupToolBar() {
    }
    
    @org.jetbrains.annotations.Nullable()
    @java.lang.Override()
    public android.view.View onCreateView(@org.jetbrains.annotations.NotNull()
    android.view.LayoutInflater inflater, @org.jetbrains.annotations.Nullable()
    android.view.ViewGroup container, @org.jetbrains.annotations.Nullable()
    android.os.Bundle savedInstanceState) {
        return null;
    }
    
    @java.lang.Override()
    public void onViewCreated(@org.jetbrains.annotations.NotNull()
    android.view.View view, @org.jetbrains.annotations.Nullable()
    android.os.Bundle savedInstanceState) {
    }
    
    private final void initView() {
    }
    
    private final void getCategories() {
    }
    
    @java.lang.Override()
    public void onCategorySelected(@org.jetbrains.annotations.NotNull()
    com.ekoapp.ekosdk.community.category.EkoCommunityCategory category) {
    }
    
    public final void setCategoryItemClickListener(@org.jetbrains.annotations.NotNull()
    com.ekoapp.ekosdk.uikit.community.explore.listener.IEkoCategoryItemClickListener listener) {
    }
    
    public EkoBaseCategoryListFragment() {
        super();
    }
}