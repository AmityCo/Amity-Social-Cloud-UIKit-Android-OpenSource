package com.ekoapp.ekosdk.uikit.community.setting;

import java.lang.System;

@kotlin.Metadata(mv = {1, 1, 16}, bv = {1, 0, 3}, k = 1, d1 = {"\u0000N\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0005\n\u0002\u0010\b\n\u0002\b\u000f\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0002\b\u0004\u0018\u00002\u00020\u0001:\u0001+B\u0005\u00a2\u0006\u0002\u0010\u0002J\u0015\u0010\t\u001a\u00020\n2\u0006\u0010\u000b\u001a\u00020\fH\u0000\u00a2\u0006\u0002\b\rJ\u0015\u0010\u000e\u001a\u00020\n2\u0006\u0010\u000b\u001a\u00020\fH\u0000\u00a2\u0006\u0002\b\u000fJ\u0018\u0010\u0010\u001a\u00020\n2\u0006\u0010\u0011\u001a\u00020\u00122\u0006\u0010\u0013\u001a\u00020\u0012H\u0002J\b\u0010\u0014\u001a\u00020\nH\u0002J\b\u0010\u0015\u001a\u00020\nH\u0002J\b\u0010\u0016\u001a\u00020\nH\u0002J\u0015\u0010\u0017\u001a\u00020\n2\u0006\u0010\u000b\u001a\u00020\fH\u0000\u00a2\u0006\u0002\b\u0018J\u0015\u0010\u0019\u001a\u00020\n2\u0006\u0010\u000b\u001a\u00020\fH\u0000\u00a2\u0006\u0002\b\u001aJ\u0015\u0010\u001b\u001a\u00020\n2\u0006\u0010\u000b\u001a\u00020\fH\u0000\u00a2\u0006\u0002\b\u001cJ\u0015\u0010\u001d\u001a\u00020\n2\u0006\u0010\u000b\u001a\u00020\fH\u0000\u00a2\u0006\u0002\b\u001eJ\b\u0010\u001f\u001a\u00020\nH\u0016J\u001a\u0010 \u001a\u00020\n2\u0006\u0010!\u001a\u00020\"2\b\u0010#\u001a\u0004\u0018\u00010$H\u0016J\u0016\u0010%\u001a\u00020\n2\f\u0010&\u001a\b\u0012\u0004\u0012\u00020(0\'H\u0002J\b\u0010)\u001a\u00020\nH\u0002J\b\u0010*\u001a\u00020\nH\u0002R\u000e\u0010\u0003\u001a\u00020\u0004X\u0082.\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0005\u001a\u00020\u0006X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0007\u001a\u00020\bX\u0082.\u00a2\u0006\u0002\n\u0000\u00a8\u0006,"}, d2 = {"Lcom/ekoapp/ekosdk/uikit/community/setting/EkoCommunitySettingsFragment;", "Lcom/trello/rxlifecycle3/components/support/RxFragment;", "()V", "essentialViewModel", "Lcom/ekoapp/ekosdk/uikit/community/setting/EkoCommunitySettingEssentialViewModel;", "settingsListAdapter", "Lcom/ekoapp/ekosdk/uikit/community/setting/EkoSettingsItemAdapter;", "viewModel", "Lcom/ekoapp/ekosdk/uikit/community/setting/EkoCommunitySettingViewModel;", "confirmCloseCommunity", "", "communityId", "", "confirmCloseCommunity$community_debug", "confirmLeaveCommunity", "confirmLeaveCommunity$community_debug", "errorDialog", "title", "", "description", "getCommunitySettingsItems", "getGlobalPushNotificationSettings", "getPushNotificationSettings", "navigateToEkoCommunityMemberSettings", "navigateToEkoCommunityMemberSettings$community_debug", "navigateToEkoCommunityProfile", "navigateToEkoCommunityProfile$community_debug", "navigateToEkoPushNotificationSettings", "navigateToEkoPushNotificationSettings$community_debug", "navigateToPostReview", "navigateToPostReview$community_debug", "onResume", "onViewCreated", "view", "Landroid/view/View;", "savedInstanceState", "Landroid/os/Bundle;", "renderItems", "items", "", "Lcom/ekoapp/ekosdk/uikit/community/setting/SettingsItem;", "setupSettingsListRecyclerView", "showErrorLayout", "Builder", "community_debug"})
public final class EkoCommunitySettingsFragment extends com.trello.rxlifecycle3.components.support.RxFragment {
    private final com.ekoapp.ekosdk.uikit.community.setting.EkoSettingsItemAdapter settingsListAdapter = null;
    private com.ekoapp.ekosdk.uikit.community.setting.EkoCommunitySettingEssentialViewModel essentialViewModel;
    private com.ekoapp.ekosdk.uikit.community.setting.EkoCommunitySettingViewModel viewModel;
    private java.util.HashMap _$_findViewCache;
    
    @java.lang.Override()
    public void onViewCreated(@org.jetbrains.annotations.NotNull()
    android.view.View view, @org.jetbrains.annotations.Nullable()
    android.os.Bundle savedInstanceState) {
    }
    
    private final void setupSettingsListRecyclerView() {
    }
    
    @java.lang.Override()
    public void onResume() {
    }
    
    private final void getGlobalPushNotificationSettings() {
    }
    
    private final void getPushNotificationSettings() {
    }
    
    private final void getCommunitySettingsItems() {
    }
    
    private final void showErrorLayout() {
    }
    
    private final void renderItems(java.util.List<? extends com.ekoapp.ekosdk.uikit.community.setting.SettingsItem> items) {
    }
    
    public final void confirmCloseCommunity$community_debug(@org.jetbrains.annotations.NotNull()
    java.lang.String communityId) {
    }
    
    public final void confirmLeaveCommunity$community_debug(@org.jetbrains.annotations.NotNull()
    java.lang.String communityId) {
    }
    
    public final void navigateToEkoCommunityProfile$community_debug(@org.jetbrains.annotations.NotNull()
    java.lang.String communityId) {
    }
    
    public final void navigateToEkoPushNotificationSettings$community_debug(@org.jetbrains.annotations.NotNull()
    java.lang.String communityId) {
    }
    
    public final void navigateToEkoCommunityMemberSettings$community_debug(@org.jetbrains.annotations.NotNull()
    java.lang.String communityId) {
    }
    
    public final void navigateToPostReview$community_debug(@org.jetbrains.annotations.NotNull()
    java.lang.String communityId) {
    }
    
    private final void errorDialog(int title, int description) {
    }
    
    public EkoCommunitySettingsFragment() {
        super();
    }
    
    @kotlin.Metadata(mv = {1, 1, 16}, bv = {1, 0, 3}, k = 1, d1 = {"\u0000&\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\u0018\u00002\u00020\u0001B\u0005\u00a2\u0006\u0002\u0010\u0002J\u000e\u0010\u0007\u001a\u00020\b2\u0006\u0010\t\u001a\u00020\nJ\u000e\u0010\u0003\u001a\u00020\u00002\u0006\u0010\u0003\u001a\u00020\u0004J\u000e\u0010\u0005\u001a\u00020\u00002\u0006\u0010\u000b\u001a\u00020\u0006R\u0010\u0010\u0003\u001a\u0004\u0018\u00010\u0004X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u0010\u0010\u0005\u001a\u0004\u0018\u00010\u0006X\u0082\u000e\u00a2\u0006\u0002\n\u0000\u00a8\u0006\f"}, d2 = {"Lcom/ekoapp/ekosdk/uikit/community/setting/EkoCommunitySettingsFragment$Builder;", "", "()V", "community", "Lcom/ekoapp/ekosdk/community/EkoCommunity;", "communityId", "", "build", "Lcom/ekoapp/ekosdk/uikit/community/setting/EkoCommunitySettingsFragment;", "activity", "Landroidx/appcompat/app/AppCompatActivity;", "id", "community_debug"})
    public static final class Builder {
        private java.lang.String communityId;
        private com.ekoapp.ekosdk.community.EkoCommunity community;
        
        @org.jetbrains.annotations.NotNull()
        public final com.ekoapp.ekosdk.uikit.community.setting.EkoCommunitySettingsFragment.Builder communityId(@org.jetbrains.annotations.NotNull()
        java.lang.String id) {
            return null;
        }
        
        @org.jetbrains.annotations.NotNull()
        public final com.ekoapp.ekosdk.uikit.community.setting.EkoCommunitySettingsFragment.Builder community(@org.jetbrains.annotations.NotNull()
        com.ekoapp.ekosdk.community.EkoCommunity community) {
            return null;
        }
        
        @org.jetbrains.annotations.NotNull()
        public final com.ekoapp.ekosdk.uikit.community.setting.EkoCommunitySettingsFragment build(@org.jetbrains.annotations.NotNull()
        androidx.appcompat.app.AppCompatActivity activity) {
            return null;
        }
        
        public Builder() {
            super();
        }
    }
}