package com.ekoapp.ekosdk.uikit.community.explore.activity;

import java.lang.System;

@kotlin.Metadata(mv = {1, 1, 16}, bv = {1, 0, 3}, k = 1, d1 = {"\u0000,\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\u0018\u00002\u00020\u00012\u00020\u0002:\u0001\rB\u0005\u00a2\u0006\u0002\u0010\u0003J\b\u0010\u0006\u001a\u00020\u0007H\u0016J\b\u0010\b\u001a\u00020\tH\u0016J\u0010\u0010\n\u001a\u00020\t2\u0006\u0010\u000b\u001a\u00020\fH\u0016R\u000e\u0010\u0004\u001a\u00020\u0005X\u0082.\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u000e"}, d2 = {"Lcom/ekoapp/ekosdk/uikit/community/explore/activity/EkoCategorySelectionActivity;", "Lcom/ekoapp/ekosdk/uikit/base/EkoBaseToolbarFragmentContainerActivity;", "Lcom/ekoapp/ekosdk/uikit/community/explore/listener/IEkoCategoryItemClickListener;", "()V", "defaultSelection", "Lcom/ekoapp/ekosdk/uikit/community/data/SelectCategoryItem;", "getContentFragment", "Landroidx/fragment/app/Fragment;", "initToolbar", "", "onCategorySelected", "category", "Lcom/ekoapp/ekosdk/community/category/EkoCommunityCategory;", "EkoCategorySelectionActivityContract", "community_debug"})
public final class EkoCategorySelectionActivity extends com.ekoapp.ekosdk.uikit.base.EkoBaseToolbarFragmentContainerActivity implements com.ekoapp.ekosdk.uikit.community.explore.listener.IEkoCategoryItemClickListener {
    private com.ekoapp.ekosdk.uikit.community.data.SelectCategoryItem defaultSelection;
    private java.util.HashMap _$_findViewCache;
    
    @java.lang.Override()
    public void initToolbar() {
    }
    
    @org.jetbrains.annotations.NotNull()
    @java.lang.Override()
    public androidx.fragment.app.Fragment getContentFragment() {
        return null;
    }
    
    @java.lang.Override()
    public void onCategorySelected(@org.jetbrains.annotations.NotNull()
    com.ekoapp.ekosdk.community.category.EkoCommunityCategory category) {
    }
    
    public EkoCategorySelectionActivity() {
        super();
    }
    
    @kotlin.Metadata(mv = {1, 1, 16}, bv = {1, 0, 3}, k = 1, d1 = {"\u0000&\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\b\n\u0002\b\u0002\u0018\u00002\u0010\u0012\u0004\u0012\u00020\u0002\u0012\u0006\u0012\u0004\u0018\u00010\u00020\u0001B\u0005\u00a2\u0006\u0002\u0010\u0003J\u001a\u0010\u0004\u001a\u00020\u00052\u0006\u0010\u0006\u001a\u00020\u00072\b\u0010\b\u001a\u0004\u0018\u00010\u0002H\u0016J\u001c\u0010\t\u001a\u0004\u0018\u00010\u00022\u0006\u0010\n\u001a\u00020\u000b2\b\u0010\f\u001a\u0004\u0018\u00010\u0005H\u0016\u00a8\u0006\r"}, d2 = {"Lcom/ekoapp/ekosdk/uikit/community/explore/activity/EkoCategorySelectionActivity$EkoCategorySelectionActivityContract;", "Landroidx/activity/result/contract/ActivityResultContract;", "Lcom/ekoapp/ekosdk/uikit/community/data/SelectCategoryItem;", "()V", "createIntent", "Landroid/content/Intent;", "context", "Landroid/content/Context;", "defaultSelection", "parseResult", "resultCode", "", "intent", "community_debug"})
    public static final class EkoCategorySelectionActivityContract extends androidx.activity.result.contract.ActivityResultContract<com.ekoapp.ekosdk.uikit.community.data.SelectCategoryItem, com.ekoapp.ekosdk.uikit.community.data.SelectCategoryItem> {
        
        @org.jetbrains.annotations.NotNull()
        @java.lang.Override()
        public android.content.Intent createIntent(@org.jetbrains.annotations.NotNull()
        android.content.Context context, @org.jetbrains.annotations.Nullable()
        com.ekoapp.ekosdk.uikit.community.data.SelectCategoryItem defaultSelection) {
            return null;
        }
        
        @org.jetbrains.annotations.Nullable()
        @java.lang.Override()
        public com.ekoapp.ekosdk.uikit.community.data.SelectCategoryItem parseResult(int resultCode, @org.jetbrains.annotations.Nullable()
        android.content.Intent intent) {
            return null;
        }
        
        public EkoCategorySelectionActivityContract() {
            super();
        }
    }
}