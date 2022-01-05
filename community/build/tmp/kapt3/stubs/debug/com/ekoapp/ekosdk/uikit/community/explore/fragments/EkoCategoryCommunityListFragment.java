package com.ekoapp.ekosdk.uikit.community.explore.fragments;

import java.lang.System;

@kotlin.Metadata(mv = {1, 1, 16}, bv = {1, 0, 3}, k = 1, d1 = {"\u0000\\\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0005\u0018\u00002\u00020\u00012\u00020\u0002:\u0001&B\u0007\b\u0000\u00a2\u0006\u0002\u0010\u0003J\b\u0010\u0011\u001a\u00020\u0012H\u0002J\b\u0010\u0013\u001a\u00020\u0012H\u0002J\b\u0010\u0014\u001a\u00020\u0012H\u0002J\u0018\u0010\u0015\u001a\u00020\u00122\u0006\u0010\u0016\u001a\u00020\u00172\u0006\u0010\u0018\u001a\u00020\u0019H\u0016J\u0012\u0010\u001a\u001a\u00020\u00122\b\u0010\u001b\u001a\u0004\u0018\u00010\u001cH\u0016J&\u0010\u001d\u001a\u0004\u0018\u00010\u001e2\u0006\u0010\u001f\u001a\u00020 2\b\u0010!\u001a\u0004\u0018\u00010\"2\b\u0010\u001b\u001a\u0004\u0018\u00010\u001cH\u0016J\u001a\u0010#\u001a\u00020\u00122\u0006\u0010$\u001a\u00020\u001e2\b\u0010\u001b\u001a\u0004\u0018\u00010\u001cH\u0016J\b\u0010%\u001a\u00020\u0012H\u0002R\u000e\u0010\u0004\u001a\u00020\u0005X\u0082.\u00a2\u0006\u0002\n\u0000R\u0010\u0010\u0006\u001a\u0004\u0018\u00010\u0007X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u0010\u0010\b\u001a\u0004\u0018\u00010\u0007X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u001a\u0010\t\u001a\u00020\nX\u0086.\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\u000b\u0010\f\"\u0004\b\r\u0010\u000eR\u000e\u0010\u000f\u001a\u00020\u0010X\u0082.\u00a2\u0006\u0002\n\u0000\u00a8\u0006\'"}, d2 = {"Lcom/ekoapp/ekosdk/uikit/community/explore/fragments/EkoCategoryCommunityListFragment;", "Lcom/ekoapp/ekosdk/uikit/base/EkoBaseFragment;", "Lcom/ekoapp/ekosdk/uikit/community/explore/listener/IEkoCommunityItemClickListener;", "()V", "adapter", "Lcom/ekoapp/ekosdk/uikit/community/explore/adapter/EkoCategoryCommunityListAdapter;", "categoryId", "", "categoryName", "mBinding", "Lcom/ekoapp/ekosdk/uikit/community/databinding/AmityFragmentCategoryCommunityListBinding;", "getMBinding", "()Lcom/ekoapp/ekosdk/uikit/community/databinding/AmityFragmentCategoryCommunityListBinding;", "setMBinding", "(Lcom/ekoapp/ekosdk/uikit/community/databinding/AmityFragmentCategoryCommunityListBinding;)V", "mViewModel", "Lcom/ekoapp/ekosdk/uikit/community/explore/viewmodel/EkoCategoryCommunityListViewModel;", "getCategories", "", "handleListVisibility", "initView", "onClick", "community", "Lcom/ekoapp/ekosdk/community/EkoCommunity;", "position", "", "onCreate", "savedInstanceState", "Landroid/os/Bundle;", "onCreateView", "Landroid/view/View;", "inflater", "Landroid/view/LayoutInflater;", "container", "Landroid/view/ViewGroup;", "onViewCreated", "view", "setupToolBar", "Builder", "community_debug"})
public final class EkoCategoryCommunityListFragment extends com.ekoapp.ekosdk.uikit.base.EkoBaseFragment implements com.ekoapp.ekosdk.uikit.community.explore.listener.IEkoCommunityItemClickListener {
    private com.ekoapp.ekosdk.uikit.community.explore.viewmodel.EkoCategoryCommunityListViewModel mViewModel;
    private com.ekoapp.ekosdk.uikit.community.explore.adapter.EkoCategoryCommunityListAdapter adapter;
    private java.lang.String categoryId;
    private java.lang.String categoryName;
    @org.jetbrains.annotations.NotNull()
    public com.ekoapp.ekosdk.uikit.community.databinding.AmityFragmentCategoryCommunityListBinding mBinding;
    private java.util.HashMap _$_findViewCache;
    
    @org.jetbrains.annotations.NotNull()
    public final com.ekoapp.ekosdk.uikit.community.databinding.AmityFragmentCategoryCommunityListBinding getMBinding() {
        return null;
    }
    
    public final void setMBinding(@org.jetbrains.annotations.NotNull()
    com.ekoapp.ekosdk.uikit.community.databinding.AmityFragmentCategoryCommunityListBinding p0) {
    }
    
    @java.lang.Override()
    public void onCreate(@org.jetbrains.annotations.Nullable()
    android.os.Bundle savedInstanceState) {
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
    
    private final void setupToolBar() {
    }
    
    private final void initView() {
    }
    
    private final void getCategories() {
    }
    
    private final void handleListVisibility() {
    }
    
    @java.lang.Override()
    public void onClick(@org.jetbrains.annotations.NotNull()
    com.ekoapp.ekosdk.community.EkoCommunity community, int position) {
    }
    
    public EkoCategoryCommunityListFragment() {
        super();
    }
    
    @kotlin.Metadata(mv = {1, 1, 16}, bv = {1, 0, 3}, k = 1, d1 = {"\u0000,\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\u0018\u00002\u00020\u0001B\u0005\u00a2\u0006\u0002\u0010\u0002J\u000e\u0010\t\u001a\u00020\n2\u0006\u0010\u000b\u001a\u00020\fJ\u000e\u0010\u0003\u001a\u00020\u00002\u0006\u0010\u0003\u001a\u00020\u0004J\u000e\u0010\u0005\u001a\u00020\u00002\u0006\u0010\u0005\u001a\u00020\u0006J\u0010\u0010\u0007\u001a\u00020\u00002\u0006\u0010\r\u001a\u00020\bH\u0002R\u0010\u0010\u0003\u001a\u0004\u0018\u00010\u0004X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u0010\u0010\u0005\u001a\u0004\u0018\u00010\u0006X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u0010\u0010\u0007\u001a\u0004\u0018\u00010\bX\u0082\u000e\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u000e"}, d2 = {"Lcom/ekoapp/ekosdk/uikit/community/explore/fragments/EkoCategoryCommunityListFragment$Builder;", "", "()V", "category", "Lcom/ekoapp/ekosdk/community/category/EkoCommunityCategory;", "categoryId", "", "communityItemClickListener", "Lcom/ekoapp/ekosdk/uikit/community/explore/listener/IEkoCommunityItemClickListener;", "build", "Lcom/ekoapp/ekosdk/uikit/community/explore/fragments/EkoCategoryCommunityListFragment;", "activity", "Landroidx/appcompat/app/AppCompatActivity;", "listener", "community_debug"})
    public static final class Builder {
        private java.lang.String categoryId;
        private com.ekoapp.ekosdk.community.category.EkoCommunityCategory category;
        private com.ekoapp.ekosdk.uikit.community.explore.listener.IEkoCommunityItemClickListener communityItemClickListener;
        
        private final com.ekoapp.ekosdk.uikit.community.explore.fragments.EkoCategoryCommunityListFragment.Builder communityItemClickListener(com.ekoapp.ekosdk.uikit.community.explore.listener.IEkoCommunityItemClickListener listener) {
            return null;
        }
        
        @org.jetbrains.annotations.NotNull()
        public final com.ekoapp.ekosdk.uikit.community.explore.fragments.EkoCategoryCommunityListFragment.Builder categoryId(@org.jetbrains.annotations.NotNull()
        java.lang.String categoryId) {
            return null;
        }
        
        @org.jetbrains.annotations.NotNull()
        public final com.ekoapp.ekosdk.uikit.community.explore.fragments.EkoCategoryCommunityListFragment.Builder category(@org.jetbrains.annotations.NotNull()
        com.ekoapp.ekosdk.community.category.EkoCommunityCategory category) {
            return null;
        }
        
        @org.jetbrains.annotations.NotNull()
        public final com.ekoapp.ekosdk.uikit.community.explore.fragments.EkoCategoryCommunityListFragment build(@org.jetbrains.annotations.NotNull()
        androidx.appcompat.app.AppCompatActivity activity) {
            return null;
        }
        
        public Builder() {
            super();
        }
    }
}