package com.ekoapp.ekosdk.uikit.community.explore.activity;

import java.lang.System;

@kotlin.Metadata(mv = {1, 1, 16}, bv = {1, 0, 3}, k = 1, d1 = {"\u0000&\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0004\u0018\u00002\u00020\u00012\u00020\u0002:\u0001\rB\u0005\u00a2\u0006\u0002\u0010\u0003J\u0010\u0010\u0004\u001a\u00020\u00052\u0006\u0010\u0006\u001a\u00020\u0007H\u0002J\b\u0010\b\u001a\u00020\u0005H\u0002J\b\u0010\t\u001a\u00020\nH\u0016J\b\u0010\u000b\u001a\u00020\u0005H\u0016J\u0010\u0010\f\u001a\u00020\u00052\u0006\u0010\u0006\u001a\u00020\u0007H\u0016\u00a8\u0006\u000e"}, d2 = {"Lcom/ekoapp/ekosdk/uikit/community/explore/activity/EkoCategoryListActivity;", "Lcom/ekoapp/ekosdk/uikit/base/EkoBaseToolbarFragmentContainerActivity;", "Lcom/ekoapp/ekosdk/uikit/community/explore/listener/IEkoCategoryItemClickListener;", "()V", "addCategoryCommunityFragment", "", "category", "Lcom/ekoapp/ekosdk/community/category/EkoCommunityCategory;", "addCategoryListFragment", "getContentFragment", "Landroidx/fragment/app/Fragment;", "initToolbar", "onCategorySelected", "EkoCategoryListActivityContract", "community_debug"})
public final class EkoCategoryListActivity extends com.ekoapp.ekosdk.uikit.base.EkoBaseToolbarFragmentContainerActivity implements com.ekoapp.ekosdk.uikit.community.explore.listener.IEkoCategoryItemClickListener {
    private java.util.HashMap _$_findViewCache;
    
    @java.lang.Override()
    public void initToolbar() {
    }
    
    @org.jetbrains.annotations.NotNull()
    @java.lang.Override()
    public androidx.fragment.app.Fragment getContentFragment() {
        return null;
    }
    
    private final void addCategoryListFragment() {
    }
    
    private final void addCategoryCommunityFragment(com.ekoapp.ekosdk.community.category.EkoCommunityCategory category) {
    }
    
    @java.lang.Override()
    public void onCategorySelected(@org.jetbrains.annotations.NotNull()
    com.ekoapp.ekosdk.community.category.EkoCommunityCategory category) {
    }
    
    public EkoCategoryListActivity() {
        super();
    }
    
    @kotlin.Metadata(mv = {1, 1, 16}, bv = {1, 0, 3}, k = 1, d1 = {"\u0000*\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\b\n\u0002\b\u0002\u0018\u00002\u0010\u0012\u0004\u0012\u00020\u0002\u0012\u0006\u0012\u0004\u0018\u00010\u00030\u0001B\u0005\u00a2\u0006\u0002\u0010\u0004J\u001a\u0010\u0005\u001a\u00020\u00062\u0006\u0010\u0007\u001a\u00020\b2\b\u0010\t\u001a\u0004\u0018\u00010\u0002H\u0016J\u001c\u0010\n\u001a\u0004\u0018\u00010\u00032\u0006\u0010\u000b\u001a\u00020\f2\b\u0010\r\u001a\u0004\u0018\u00010\u0006H\u0016\u00a8\u0006\u000e"}, d2 = {"Lcom/ekoapp/ekosdk/uikit/community/explore/activity/EkoCategoryListActivity$EkoCategoryListActivityContract;", "Landroidx/activity/result/contract/ActivityResultContract;", "Ljava/lang/Void;", "Lcom/ekoapp/ekosdk/community/EkoCommunity;", "()V", "createIntent", "Landroid/content/Intent;", "context", "Landroid/content/Context;", "input", "parseResult", "resultCode", "", "intent", "community_debug"})
    public static final class EkoCategoryListActivityContract extends androidx.activity.result.contract.ActivityResultContract<java.lang.Void, com.ekoapp.ekosdk.community.EkoCommunity> {
        
        @org.jetbrains.annotations.Nullable()
        @java.lang.Override()
        public com.ekoapp.ekosdk.community.EkoCommunity parseResult(int resultCode, @org.jetbrains.annotations.Nullable()
        android.content.Intent intent) {
            return null;
        }
        
        @org.jetbrains.annotations.NotNull()
        @java.lang.Override()
        public android.content.Intent createIntent(@org.jetbrains.annotations.NotNull()
        android.content.Context context, @org.jetbrains.annotations.Nullable()
        java.lang.Void input) {
            return null;
        }
        
        public EkoCategoryListActivityContract() {
            super();
        }
    }
}