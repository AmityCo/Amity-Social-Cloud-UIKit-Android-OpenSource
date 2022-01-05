package com.ekoapp.ekosdk.uikit.community.explore.fragments;

import java.lang.System;

@kotlin.Metadata(mv = {1, 1, 16}, bv = {1, 0, 3}, k = 1, d1 = {"\u0000T\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\b\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000b\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\u0018\u00002\u00020\u0001:\u0001\u001cB\u0007\b\u0000\u00a2\u0006\u0002\u0010\u0002J\b\u0010\t\u001a\u00020\nH\u0016J\u0010\u0010\u000b\u001a\u00020\f2\u0006\u0010\r\u001a\u00020\u000eH\u0016J\u0018\u0010\u000f\u001a\u00020\f2\u0006\u0010\u0010\u001a\u00020\u00112\u0006\u0010\u0012\u001a\u00020\u0013H\u0016J\u0010\u0010\u0014\u001a\u00020\u00152\u0006\u0010\u0016\u001a\u00020\u0006H\u0016J\u001a\u0010\u0017\u001a\u00020\f2\u0006\u0010\u0018\u001a\u00020\u00192\b\u0010\u001a\u001a\u0004\u0018\u00010\u001bH\u0016R\u000e\u0010\u0003\u001a\u00020\u0004X\u0082D\u00a2\u0006\u0002\n\u0000R\u0010\u0010\u0005\u001a\u0004\u0018\u00010\u0006X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u0010\u0010\u0007\u001a\u0004\u0018\u00010\bX\u0082\u000e\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u001d"}, d2 = {"Lcom/ekoapp/ekosdk/uikit/community/explore/fragments/EkoSelectCategoryListFragment;", "Lcom/ekoapp/ekosdk/uikit/community/explore/fragments/EkoBaseCategoryListFragment;", "()V", "ID_MENU_ITEM_SAVE_PROFILE", "", "menuItemDone", "Landroid/view/MenuItem;", "selectedCategory", "Lcom/ekoapp/ekosdk/uikit/community/data/SelectCategoryItem;", "getCategoryListAdapter", "Lcom/ekoapp/ekosdk/uikit/community/explore/adapter/EkoCategoryListAdapter;", "onCategorySelected", "", "category", "Lcom/ekoapp/ekosdk/community/category/EkoCommunityCategory;", "onCreateOptionsMenu", "menu", "Landroid/view/Menu;", "inflater", "Landroid/view/MenuInflater;", "onOptionsItemSelected", "", "item", "onViewCreated", "view", "Landroid/view/View;", "savedInstanceState", "Landroid/os/Bundle;", "Builder", "community_debug"})
public final class EkoSelectCategoryListFragment extends com.ekoapp.ekosdk.uikit.community.explore.fragments.EkoBaseCategoryListFragment {
    private final int ID_MENU_ITEM_SAVE_PROFILE = 122;
    private android.view.MenuItem menuItemDone;
    private com.ekoapp.ekosdk.uikit.community.data.SelectCategoryItem selectedCategory;
    private java.util.HashMap _$_findViewCache;
    
    @java.lang.Override()
    public void onViewCreated(@org.jetbrains.annotations.NotNull()
    android.view.View view, @org.jetbrains.annotations.Nullable()
    android.os.Bundle savedInstanceState) {
    }
    
    @java.lang.Override()
    public void onCreateOptionsMenu(@org.jetbrains.annotations.NotNull()
    android.view.Menu menu, @org.jetbrains.annotations.NotNull()
    android.view.MenuInflater inflater) {
    }
    
    @java.lang.Override()
    public boolean onOptionsItemSelected(@org.jetbrains.annotations.NotNull()
    android.view.MenuItem item) {
        return false;
    }
    
    @java.lang.Override()
    public void onCategorySelected(@org.jetbrains.annotations.NotNull()
    com.ekoapp.ekosdk.community.category.EkoCommunityCategory category) {
    }
    
    @org.jetbrains.annotations.NotNull()
    @java.lang.Override()
    public com.ekoapp.ekosdk.uikit.community.explore.adapter.EkoCategoryListAdapter getCategoryListAdapter() {
        return null;
    }
    
    public EkoSelectCategoryListFragment() {
        super();
    }
    
    @kotlin.Metadata(mv = {1, 1, 16}, bv = {1, 0, 3}, k = 1, d1 = {"\u0000&\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\u0018\u00002\u00020\u0001B\u0005\u00a2\u0006\u0002\u0010\u0002J\u000e\u0010\u0007\u001a\u00020\b2\u0006\u0010\t\u001a\u00020\nJ\u0010\u0010\u0003\u001a\u00020\u00002\u0006\u0010\u000b\u001a\u00020\u0004H\u0002J\u000e\u0010\u0005\u001a\u00020\u00002\u0006\u0010\f\u001a\u00020\u0006R\u0010\u0010\u0003\u001a\u0004\u0018\u00010\u0004X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u0010\u0010\u0005\u001a\u0004\u0018\u00010\u0006X\u0082\u000e\u00a2\u0006\u0002\n\u0000\u00a8\u0006\r"}, d2 = {"Lcom/ekoapp/ekosdk/uikit/community/explore/fragments/EkoSelectCategoryListFragment$Builder;", "", "()V", "categoryItemClickListener", "Lcom/ekoapp/ekosdk/uikit/community/explore/listener/IEkoCategoryItemClickListener;", "defaultSelection", "", "build", "Lcom/ekoapp/ekosdk/uikit/community/explore/fragments/EkoSelectCategoryListFragment;", "activity", "Landroidx/appcompat/app/AppCompatActivity;", "listener", "category", "community_debug"})
    public static final class Builder {
        private java.lang.String defaultSelection;
        private com.ekoapp.ekosdk.uikit.community.explore.listener.IEkoCategoryItemClickListener categoryItemClickListener;
        
        @org.jetbrains.annotations.NotNull()
        public final com.ekoapp.ekosdk.uikit.community.explore.fragments.EkoSelectCategoryListFragment build(@org.jetbrains.annotations.NotNull()
        androidx.appcompat.app.AppCompatActivity activity) {
            return null;
        }
        
        @org.jetbrains.annotations.NotNull()
        public final com.ekoapp.ekosdk.uikit.community.explore.fragments.EkoSelectCategoryListFragment.Builder defaultSelection(@org.jetbrains.annotations.NotNull()
        java.lang.String category) {
            return null;
        }
        
        private final com.ekoapp.ekosdk.uikit.community.explore.fragments.EkoSelectCategoryListFragment.Builder categoryItemClickListener(com.ekoapp.ekosdk.uikit.community.explore.listener.IEkoCategoryItemClickListener listener) {
            return null;
        }
        
        public Builder() {
            super();
        }
    }
}