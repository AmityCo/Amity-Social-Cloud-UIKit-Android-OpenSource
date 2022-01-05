package com.ekoapp.ekosdk.uikit.community.notificationsettings.pushDetail;

import java.lang.System;

@kotlin.Metadata(mv = {1, 1, 16}, bv = {1, 0, 3}, k = 1, d1 = {"\u0000.\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0010\u0002\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\b\u0004\u0018\u0000 \u00142\u00020\u00012\u00020\u0002:\u0002\u0014\u0015B\u0005\u00a2\u0006\u0002\u0010\u0003J\b\u0010\f\u001a\u00020\rH\u0002J\b\u0010\u000e\u001a\u00020\rH\u0016J\b\u0010\u000f\u001a\u00020\rH\u0002J\u0012\u0010\u0010\u001a\u00020\r2\b\u0010\u0011\u001a\u0004\u0018\u00010\u0012H\u0014J\b\u0010\u0013\u001a\u00020\rH\u0016R\u000e\u0010\u0004\u001a\u00020\u0005X\u0082.\u00a2\u0006\u0002\n\u0000R\u001b\u0010\u0006\u001a\u00020\u00078BX\u0082\u0084\u0002\u00a2\u0006\f\n\u0004\b\n\u0010\u000b\u001a\u0004\b\b\u0010\t\u00a8\u0006\u0016"}, d2 = {"Lcom/ekoapp/ekosdk/uikit/community/notificationsettings/pushDetail/EkoPushSettingsDetailActivity;", "Landroidx/appcompat/app/AppCompatActivity;", "Lcom/ekoapp/ekosdk/uikit/components/EkoToolBarClickListener;", "()V", "fragment", "Lcom/ekoapp/ekosdk/uikit/community/notificationsettings/pushDetail/EkoPushSettingDetailFragment;", "viewModel", "Lcom/ekoapp/ekosdk/uikit/community/notificationsettings/pushDetail/EkoPushSettingsDetailViewModel;", "getViewModel", "()Lcom/ekoapp/ekosdk/uikit/community/notificationsettings/pushDetail/EkoPushSettingsDetailViewModel;", "viewModel$delegate", "Lkotlin/Lazy;", "initToolbar", "", "leftIconClick", "loadFragment", "onCreate", "savedInstanceState", "Landroid/os/Bundle;", "rightIconClick", "Companion", "SettingType", "community_debug"})
public final class EkoPushSettingsDetailActivity extends androidx.appcompat.app.AppCompatActivity implements com.ekoapp.ekosdk.uikit.components.EkoToolBarClickListener {
    private final kotlin.Lazy viewModel$delegate = null;
    private com.ekoapp.ekosdk.uikit.community.notificationsettings.pushDetail.EkoPushSettingDetailFragment fragment;
    private static final java.lang.String ARG_COMMUNITY_ID = "ARG_COMMUNITY_ID";
    private static final java.lang.String ARG_SETTING_TYPE = "ARG_SETTING_TYPE";
    public static final com.ekoapp.ekosdk.uikit.community.notificationsettings.pushDetail.EkoPushSettingsDetailActivity.Companion Companion = null;
    private java.util.HashMap _$_findViewCache;
    
    private final com.ekoapp.ekosdk.uikit.community.notificationsettings.pushDetail.EkoPushSettingsDetailViewModel getViewModel() {
        return null;
    }
    
    @java.lang.Override()
    protected void onCreate(@org.jetbrains.annotations.Nullable()
    android.os.Bundle savedInstanceState) {
    }
    
    private final void initToolbar() {
    }
    
    private final void loadFragment() {
    }
    
    @java.lang.Override()
    public void leftIconClick() {
    }
    
    @java.lang.Override()
    public void rightIconClick() {
    }
    
    public EkoPushSettingsDetailActivity() {
        super();
    }
    
    @kotlin.Metadata(mv = {1, 1, 16}, bv = {1, 0, 3}, k = 1, d1 = {"\u0000\f\n\u0002\u0018\u0002\n\u0002\u0010\u0010\n\u0002\b\u0004\b\u0086\u0001\u0018\u00002\b\u0012\u0004\u0012\u00020\u00000\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002j\u0002\b\u0003j\u0002\b\u0004\u00a8\u0006\u0005"}, d2 = {"Lcom/ekoapp/ekosdk/uikit/community/notificationsettings/pushDetail/EkoPushSettingsDetailActivity$SettingType;", "", "(Ljava/lang/String;I)V", "POSTS", "COMMENTS", "community_debug"})
    public static enum SettingType {
        /*public static final*/ POSTS /* = new POSTS() */,
        /*public static final*/ COMMENTS /* = new COMMENTS() */;
        
        SettingType() {
        }
    }
    
    @kotlin.Metadata(mv = {1, 1, 16}, bv = {1, 0, 3}, k = 1, d1 = {"\u0000(\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\b\u0086\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002J\u001e\u0010\u0006\u001a\u00020\u00072\u0006\u0010\b\u001a\u00020\t2\u0006\u0010\n\u001a\u00020\u00042\u0006\u0010\u000b\u001a\u00020\fR\u000e\u0010\u0003\u001a\u00020\u0004X\u0082T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0005\u001a\u00020\u0004X\u0082T\u00a2\u0006\u0002\n\u0000\u00a8\u0006\r"}, d2 = {"Lcom/ekoapp/ekosdk/uikit/community/notificationsettings/pushDetail/EkoPushSettingsDetailActivity$Companion;", "", "()V", "ARG_COMMUNITY_ID", "", "ARG_SETTING_TYPE", "newIntent", "Landroid/content/Intent;", "context", "Landroid/content/Context;", "communityId", "type", "Lcom/ekoapp/ekosdk/uikit/community/notificationsettings/pushDetail/EkoPushSettingsDetailActivity$SettingType;", "community_debug"})
    public static final class Companion {
        
        @org.jetbrains.annotations.NotNull()
        public final android.content.Intent newIntent(@org.jetbrains.annotations.NotNull()
        android.content.Context context, @org.jetbrains.annotations.NotNull()
        java.lang.String communityId, @org.jetbrains.annotations.NotNull()
        com.ekoapp.ekosdk.uikit.community.notificationsettings.pushDetail.EkoPushSettingsDetailActivity.SettingType type) {
            return null;
        }
        
        private Companion() {
            super();
        }
    }
}