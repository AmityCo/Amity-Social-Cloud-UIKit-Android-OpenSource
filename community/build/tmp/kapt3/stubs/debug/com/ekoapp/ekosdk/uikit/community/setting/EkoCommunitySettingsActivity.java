package com.ekoapp.ekosdk.uikit.community.setting;

import java.lang.System;

@kotlin.Metadata(mv = {1, 1, 16}, bv = {1, 0, 3}, k = 1, d1 = {"\u00000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\b\n\u0002\b\u0003\n\u0002\u0010\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0004\u0018\u0000 \u00122\u000e\u0012\u0004\u0012\u00020\u0002\u0012\u0004\u0012\u00020\u00030\u00012\u00020\u0004:\u0001\u0012B\u0005\u00a2\u0006\u0002\u0010\u0005J\b\u0010\u0006\u001a\u00020\u0007H\u0016J\b\u0010\b\u001a\u00020\u0007H\u0016J\b\u0010\t\u001a\u00020\u0003H\u0016J\b\u0010\n\u001a\u00020\u000bH\u0016J\b\u0010\f\u001a\u00020\u000bH\u0002J\u0012\u0010\r\u001a\u00020\u000b2\b\u0010\u000e\u001a\u0004\u0018\u00010\u000fH\u0014J\b\u0010\u0010\u001a\u00020\u000bH\u0016J\b\u0010\u0011\u001a\u00020\u000bH\u0002\u00a8\u0006\u0013"}, d2 = {"Lcom/ekoapp/ekosdk/uikit/community/setting/EkoCommunitySettingsActivity;", "Lcom/ekoapp/ekosdk/uikit/base/EkoBaseActivity;", "Lcom/ekoapp/ekosdk/uikit/community/databinding/AmityActivityCommunitySettingBinding;", "Lcom/ekoapp/ekosdk/uikit/community/setting/EkoCommunitySettingViewModel;", "Lcom/ekoapp/ekosdk/uikit/components/EkoToolBarClickListener;", "()V", "getBindingVariable", "", "getLayoutId", "getViewModel", "leftIconClick", "", "loadFragment", "onCreate", "savedInstanceState", "Landroid/os/Bundle;", "rightIconClick", "setUpToolbar", "Companion", "community_debug"})
public final class EkoCommunitySettingsActivity extends com.ekoapp.ekosdk.uikit.base.EkoBaseActivity<com.ekoapp.ekosdk.uikit.community.databinding.AmityActivityCommunitySettingBinding, com.ekoapp.ekosdk.uikit.community.setting.EkoCommunitySettingViewModel> implements com.ekoapp.ekosdk.uikit.components.EkoToolBarClickListener {
    private static final java.lang.String COMMUNITY = "COMMUNITY";
    private static final java.lang.String COMMUNITY_ID = "COMMUNITY_ID";
    public static final com.ekoapp.ekosdk.uikit.community.setting.EkoCommunitySettingsActivity.Companion Companion = null;
    private java.util.HashMap _$_findViewCache;
    
    @java.lang.Override()
    protected void onCreate(@org.jetbrains.annotations.Nullable()
    android.os.Bundle savedInstanceState) {
    }
    
    private final void setUpToolbar() {
    }
    
    private final void loadFragment() {
    }
    
    @java.lang.Override()
    public int getLayoutId() {
        return 0;
    }
    
    @org.jetbrains.annotations.NotNull()
    @java.lang.Override()
    public com.ekoapp.ekosdk.uikit.community.setting.EkoCommunitySettingViewModel getViewModel() {
        return null;
    }
    
    @java.lang.Override()
    public int getBindingVariable() {
        return 0;
    }
    
    @java.lang.Override()
    public void leftIconClick() {
    }
    
    @java.lang.Override()
    public void rightIconClick() {
    }
    
    public EkoCommunitySettingsActivity() {
        super();
    }
    
    @kotlin.Metadata(mv = {1, 1, 16}, bv = {1, 0, 3}, k = 1, d1 = {"\u0000(\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\b\u0086\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002J$\u0010\u0006\u001a\u00020\u00072\u0006\u0010\b\u001a\u00020\t2\b\u0010\n\u001a\u0004\u0018\u00010\u000b2\n\b\u0002\u0010\f\u001a\u0004\u0018\u00010\u0004R\u000e\u0010\u0003\u001a\u00020\u0004X\u0082T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0005\u001a\u00020\u0004X\u0082T\u00a2\u0006\u0002\n\u0000\u00a8\u0006\r"}, d2 = {"Lcom/ekoapp/ekosdk/uikit/community/setting/EkoCommunitySettingsActivity$Companion;", "", "()V", "COMMUNITY", "", "COMMUNITY_ID", "newIntent", "Landroid/content/Intent;", "context", "Landroid/content/Context;", "community", "Lcom/ekoapp/ekosdk/community/EkoCommunity;", "id", "community_debug"})
    public static final class Companion {
        
        @org.jetbrains.annotations.NotNull()
        public final android.content.Intent newIntent(@org.jetbrains.annotations.NotNull()
        android.content.Context context, @org.jetbrains.annotations.Nullable()
        com.ekoapp.ekosdk.community.EkoCommunity community, @org.jetbrains.annotations.Nullable()
        java.lang.String id) {
            return null;
        }
        
        private Companion() {
            super();
        }
    }
}