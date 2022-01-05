package com.ekoapp.ekosdk.uikit.community.notificationsettings;

import java.lang.System;

@kotlin.Metadata(mv = {1, 1, 16}, bv = {1, 0, 3}, k = 1, d1 = {"\u0000X\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0010\u0002\n\u0000\n\u0002\u0010\b\n\u0002\b\u0002\n\u0002\u0010\u000b\n\u0002\b\u0004\n\u0002\u0010\u000e\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0002\b\b\u0018\u00002\u00020\u0001:\u0001(B\u0005\u00a2\u0006\u0002\u0010\u0002J \u0010\u000b\u001a\u00020\f2\u0006\u0010\r\u001a\u00020\u000e2\u0006\u0010\u000f\u001a\u00020\u000e2\u0006\u0010\u0010\u001a\u00020\u0011H\u0002J\b\u0010\u0012\u001a\u00020\fH\u0002J\u0010\u0010\u0013\u001a\u00020\f2\u0006\u0010\u0010\u001a\u00020\u0011H\u0002J\u0016\u0010\u0014\u001a\u00020\f2\u0006\u0010\u0015\u001a\u00020\u00162\u0006\u0010\u0017\u001a\u00020\u0018J\u001a\u0010\u0019\u001a\u00020\f2\u0006\u0010\u001a\u001a\u00020\u001b2\b\u0010\u001c\u001a\u0004\u0018\u00010\u001dH\u0016J\u0016\u0010\u001e\u001a\u00020\f2\f\u0010\u001f\u001a\b\u0012\u0004\u0012\u00020!0 H\u0002J\b\u0010\"\u001a\u00020\fH\u0002J\b\u0010#\u001a\u00020\fH\u0002J\u0015\u0010$\u001a\u00020\f2\u0006\u0010%\u001a\u00020\u0011H\u0000\u00a2\u0006\u0002\b&J\u0010\u0010\'\u001a\u00020\f2\u0006\u0010\u0010\u001a\u00020\u0011H\u0002R\u000e\u0010\u0003\u001a\u00020\u0004X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u001a\u0010\u0005\u001a\u00020\u0006X\u0086.\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\u0007\u0010\b\"\u0004\b\t\u0010\n\u00a8\u0006)"}, d2 = {"Lcom/ekoapp/ekosdk/uikit/community/notificationsettings/EkoPushNotificationSettingsFragment;", "Lcom/trello/rxlifecycle3/components/support/RxFragment;", "()V", "settingsListAdapter", "Lcom/ekoapp/ekosdk/uikit/community/setting/EkoSettingsItemAdapter;", "viewModel", "Lcom/ekoapp/ekosdk/uikit/community/notificationsettings/EkoPushNotificationSettingsViewModel;", "getViewModel", "()Lcom/ekoapp/ekosdk/uikit/community/notificationsettings/EkoPushNotificationSettingsViewModel;", "setViewModel", "(Lcom/ekoapp/ekosdk/uikit/community/notificationsettings/EkoPushNotificationSettingsViewModel;)V", "errorDialog", "", "title", "", "description", "value", "", "getPushNotificationMenuItems", "getSettingsItems", "navigateToNewPostSettings", "communityId", "", "type", "Lcom/ekoapp/ekosdk/uikit/community/notificationsettings/pushDetail/EkoPushSettingsDetailActivity$SettingType;", "onViewCreated", "view", "Landroid/view/View;", "savedInstanceState", "Landroid/os/Bundle;", "renderItems", "items", "", "Lcom/ekoapp/ekosdk/uikit/community/setting/SettingsItem;", "setUpRecyclerView", "showErrorLayout", "toggleAllSettings", "isChecked", "toggleAllSettings$community_debug", "updateGlobalPushSettings", "Builder", "community_debug"})
public final class EkoPushNotificationSettingsFragment extends com.trello.rxlifecycle3.components.support.RxFragment {
    @org.jetbrains.annotations.NotNull()
    public com.ekoapp.ekosdk.uikit.community.notificationsettings.EkoPushNotificationSettingsViewModel viewModel;
    private final com.ekoapp.ekosdk.uikit.community.setting.EkoSettingsItemAdapter settingsListAdapter = null;
    private java.util.HashMap _$_findViewCache;
    
    @org.jetbrains.annotations.NotNull()
    public final com.ekoapp.ekosdk.uikit.community.notificationsettings.EkoPushNotificationSettingsViewModel getViewModel() {
        return null;
    }
    
    public final void setViewModel(@org.jetbrains.annotations.NotNull()
    com.ekoapp.ekosdk.uikit.community.notificationsettings.EkoPushNotificationSettingsViewModel p0) {
    }
    
    @java.lang.Override()
    public void onViewCreated(@org.jetbrains.annotations.NotNull()
    android.view.View view, @org.jetbrains.annotations.Nullable()
    android.os.Bundle savedInstanceState) {
    }
    
    public final void toggleAllSettings$community_debug(boolean isChecked) {
    }
    
    private final void setUpRecyclerView() {
    }
    
    private final void getPushNotificationMenuItems() {
    }
    
    private final void getSettingsItems(boolean value) {
    }
    
    private final void renderItems(java.util.List<? extends com.ekoapp.ekosdk.uikit.community.setting.SettingsItem> items) {
    }
    
    public final void navigateToNewPostSettings(@org.jetbrains.annotations.NotNull()
    java.lang.String communityId, @org.jetbrains.annotations.NotNull()
    com.ekoapp.ekosdk.uikit.community.notificationsettings.pushDetail.EkoPushSettingsDetailActivity.SettingType type) {
    }
    
    private final void updateGlobalPushSettings(boolean value) {
    }
    
    private final void showErrorLayout() {
    }
    
    private final void errorDialog(int title, int description, boolean value) {
    }
    
    public EkoPushNotificationSettingsFragment() {
        super();
    }
    
    @kotlin.Metadata(mv = {1, 1, 16}, bv = {1, 0, 3}, k = 1, d1 = {"\u0000&\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\u0018\u00002\u00020\u0001B\u0005\u00a2\u0006\u0002\u0010\u0002J\u000e\u0010\u0007\u001a\u00020\b2\u0006\u0010\t\u001a\u00020\nJ\u0010\u0010\u0003\u001a\u00020\u00002\b\u0010\u0003\u001a\u0004\u0018\u00010\u0004J\u000e\u0010\u0005\u001a\u00020\u00002\u0006\u0010\u000b\u001a\u00020\u0006R\u0010\u0010\u0003\u001a\u0004\u0018\u00010\u0004X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0005\u001a\u00020\u0006X\u0082\u000e\u00a2\u0006\u0002\n\u0000\u00a8\u0006\f"}, d2 = {"Lcom/ekoapp/ekosdk/uikit/community/notificationsettings/EkoPushNotificationSettingsFragment$Builder;", "", "()V", "community", "Lcom/ekoapp/ekosdk/community/EkoCommunity;", "communityId", "", "build", "Lcom/ekoapp/ekosdk/uikit/community/notificationsettings/EkoPushNotificationSettingsFragment;", "activity", "Landroidx/appcompat/app/AppCompatActivity;", "id", "community_debug"})
    public static final class Builder {
        private java.lang.String communityId = "";
        private com.ekoapp.ekosdk.community.EkoCommunity community;
        
        @org.jetbrains.annotations.NotNull()
        public final com.ekoapp.ekosdk.uikit.community.notificationsettings.EkoPushNotificationSettingsFragment build(@org.jetbrains.annotations.NotNull()
        androidx.appcompat.app.AppCompatActivity activity) {
            return null;
        }
        
        @org.jetbrains.annotations.NotNull()
        public final com.ekoapp.ekosdk.uikit.community.notificationsettings.EkoPushNotificationSettingsFragment.Builder communityId(@org.jetbrains.annotations.NotNull()
        java.lang.String id) {
            return null;
        }
        
        @org.jetbrains.annotations.NotNull()
        public final com.ekoapp.ekosdk.uikit.community.notificationsettings.EkoPushNotificationSettingsFragment.Builder community(@org.jetbrains.annotations.Nullable()
        com.ekoapp.ekosdk.community.EkoCommunity community) {
            return null;
        }
        
        public Builder() {
            super();
        }
    }
}