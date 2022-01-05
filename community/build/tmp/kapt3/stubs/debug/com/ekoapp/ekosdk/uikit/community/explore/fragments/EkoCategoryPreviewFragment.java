package com.ekoapp.ekosdk.uikit.community.explore.fragments;

import java.lang.System;

@kotlin.Metadata(mv = {1, 1, 16}, bv = {1, 0, 3}, k = 1, d1 = {"\u0000L\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0006\u0018\u00002\u00020\u00012\u00020\u0002:\u0001\u001dB\u0007\b\u0000\u00a2\u0006\u0002\u0010\u0003J\b\u0010\u000b\u001a\u00020\fH\u0002J\b\u0010\r\u001a\u00020\fH\u0002J\u0010\u0010\u000e\u001a\u00020\f2\u0006\u0010\u000f\u001a\u00020\u0010H\u0016J&\u0010\u0011\u001a\u0004\u0018\u00010\u00122\u0006\u0010\u0013\u001a\u00020\u00142\b\u0010\u0015\u001a\u0004\u0018\u00010\u00162\b\u0010\u0017\u001a\u0004\u0018\u00010\u0018H\u0016J\u001a\u0010\u0019\u001a\u00020\f2\u0006\u0010\u001a\u001a\u00020\u00122\b\u0010\u0017\u001a\u0004\u0018\u00010\u0018H\u0016J\r\u0010\u001b\u001a\u00020\fH\u0000\u00a2\u0006\u0002\b\u001cR\u0016\u0010\u0004\u001a\n \u0006*\u0004\u0018\u00010\u00050\u0005X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0007\u001a\u00020\bX\u0082.\u00a2\u0006\u0002\n\u0000R\u000e\u0010\t\u001a\u00020\nX\u0082.\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u001e"}, d2 = {"Lcom/ekoapp/ekosdk/uikit/community/explore/fragments/EkoCategoryPreviewFragment;", "Lcom/ekoapp/ekosdk/uikit/base/EkoBaseFragment;", "Lcom/ekoapp/ekosdk/uikit/community/explore/listener/IEkoCategoryItemClickListener;", "()V", "TAG", "", "kotlin.jvm.PlatformType", "mAdapter", "Lcom/ekoapp/ekosdk/uikit/community/explore/adapter/EkoCommunityCategoryAdapter;", "mViewModel", "Lcom/ekoapp/ekosdk/uikit/community/explore/viewmodel/EkoExploreCommunityViewModel;", "getCategories", "", "initializeRecyclerView", "onCategorySelected", "category", "Lcom/ekoapp/ekosdk/community/category/EkoCommunityCategory;", "onCreateView", "Landroid/view/View;", "inflater", "Landroid/view/LayoutInflater;", "container", "Landroid/view/ViewGroup;", "savedInstanceState", "Landroid/os/Bundle;", "onViewCreated", "view", "refresh", "refresh$community_debug", "Builder", "community_debug"})
public final class EkoCategoryPreviewFragment extends com.ekoapp.ekosdk.uikit.base.EkoBaseFragment implements com.ekoapp.ekosdk.uikit.community.explore.listener.IEkoCategoryItemClickListener {
    private final java.lang.String TAG = null;
    private com.ekoapp.ekosdk.uikit.community.explore.viewmodel.EkoExploreCommunityViewModel mViewModel;
    private com.ekoapp.ekosdk.uikit.community.explore.adapter.EkoCommunityCategoryAdapter mAdapter;
    private java.util.HashMap _$_findViewCache;
    
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
    
    public final void refresh$community_debug() {
    }
    
    private final void initializeRecyclerView() {
    }
    
    private final void getCategories() {
    }
    
    @java.lang.Override()
    public void onCategorySelected(@org.jetbrains.annotations.NotNull()
    com.ekoapp.ekosdk.community.category.EkoCommunityCategory category) {
    }
    
    public EkoCategoryPreviewFragment() {
        super();
    }
    
    @kotlin.Metadata(mv = {1, 1, 16}, bv = {1, 0, 3}, k = 1, d1 = {"\u0000$\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0010\u0002\n\u0002\b\u0002\u0018\u00002\u00020\u0001B\u0005\u00a2\u0006\u0002\u0010\u0002J\u000e\u0010\u0005\u001a\u00020\u00062\u0006\u0010\u0007\u001a\u00020\bJ\u0010\u0010\u0003\u001a\u00020\t2\u0006\u0010\n\u001a\u00020\u0004H\u0002R\u0010\u0010\u0003\u001a\u0004\u0018\u00010\u0004X\u0082\u000e\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u000b"}, d2 = {"Lcom/ekoapp/ekosdk/uikit/community/explore/fragments/EkoCategoryPreviewFragment$Builder;", "", "()V", "categoryItemClickListener", "Lcom/ekoapp/ekosdk/uikit/community/explore/listener/IEkoCategoryItemClickListener;", "build", "Lcom/ekoapp/ekosdk/uikit/community/explore/fragments/EkoCategoryPreviewFragment;", "activity", "Landroidx/appcompat/app/AppCompatActivity;", "", "listener", "community_debug"})
    public static final class Builder {
        private com.ekoapp.ekosdk.uikit.community.explore.listener.IEkoCategoryItemClickListener categoryItemClickListener;
        
        @org.jetbrains.annotations.NotNull()
        public final com.ekoapp.ekosdk.uikit.community.explore.fragments.EkoCategoryPreviewFragment build(@org.jetbrains.annotations.NotNull()
        androidx.appcompat.app.AppCompatActivity activity) {
            return null;
        }
        
        private final void categoryItemClickListener(com.ekoapp.ekosdk.uikit.community.explore.listener.IEkoCategoryItemClickListener listener) {
        }
        
        public Builder() {
            super();
        }
    }
}