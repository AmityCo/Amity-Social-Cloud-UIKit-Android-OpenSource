package com.ekoapp.ekosdk.uikit.community.setting;

import java.lang.System;

@kotlin.Metadata(mv = {1, 1, 16}, bv = {1, 0, 3}, k = 1, d1 = {"\u0000Z\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0000\n\u0002\u0018\u0002\n\u0002\u0010\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0010\u000b\n\u0002\b\u0007\u0018\u00002\u00020\u0001B\u0005\u00a2\u0006\u0002\u0010\u0002J*\u0010\u0003\u001a\u00020\u00042\u0006\u0010\u0005\u001a\u00020\u00062\f\u0010\u0007\u001a\b\u0012\u0004\u0012\u00020\t0\b2\f\u0010\n\u001a\b\u0012\u0004\u0012\u00020\t0\bJ,\u0010\u000b\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u000e0\r0\f2\u0006\u0010\u0005\u001a\u00020\u00062\u0006\u0010\u000f\u001a\u00020\u00102\u0006\u0010\u0011\u001a\u00020\u0012H\u0002J\b\u0010\u0013\u001a\u00020\u0014H\u0002JU\u0010\u0015\u001a\u00020\u00042\u0006\u0010\u0005\u001a\u00020\u00062\u0006\u0010\u000f\u001a\u00020\u00102\u0006\u0010\u0011\u001a\u00020\u00122\'\u0010\u0016\u001a#\u0012\u0019\u0012\u0017\u0012\u0004\u0012\u00020\u000e0\r\u00a2\u0006\f\b\u0018\u0012\b\b\u0019\u0012\u0004\b\b(\u001a\u0012\u0004\u0012\u00020\t0\u00172\f\u0010\u001b\u001a\b\u0012\u0004\u0012\u00020\t0\bJ\u001e\u0010\u001c\u001a\b\u0012\u0004\u0012\u00020\u001d0\f2\u0006\u0010\u0005\u001a\u00020\u00062\u0006\u0010\u000f\u001a\u00020\u0010H\u0002J\u001e\u0010\u001e\u001a\b\u0012\u0004\u0012\u00020\u001d0\f2\u0006\u0010\u0005\u001a\u00020\u00062\u0006\u0010\u000f\u001a\u00020\u0010H\u0002J*\u0010\u001f\u001a\u00020\u00042\u0006\u0010\u0005\u001a\u00020\u00062\f\u0010 \u001a\b\u0012\u0004\u0012\u00020\t0\b2\f\u0010!\u001a\b\u0012\u0004\u0012\u00020\t0\bJ\u0016\u0010\"\u001a\u00020\u001d2\u0006\u0010\u000f\u001a\u00020\u00102\u0006\u0010#\u001a\u00020\u001d\u00a8\u0006$"}, d2 = {"Lcom/ekoapp/ekosdk/uikit/community/setting/EkoCommunitySettingViewModel;", "Lcom/ekoapp/ekosdk/uikit/community/notificationsettings/EkoPushNotificationBaseViewModel;", "()V", "closeCommunity", "Lio/reactivex/Completable;", "communityId", "", "onCloseSuccess", "Lkotlin/Function0;", "", "onCloseError", "getItemsBasedOnPermissions", "Lio/reactivex/Flowable;", "", "Lcom/ekoapp/ekosdk/uikit/community/setting/SettingsItem;", "community", "Lcom/ekoapp/ekosdk/community/EkoCommunity;", "menuCreator", "Lcom/ekoapp/ekosdk/uikit/community/setting/CommunitySettingsMenuCreator;", "getPushStatus", "", "getSettingsItems", "onResult", "Lkotlin/Function1;", "Lkotlin/ParameterName;", "name", "items", "onError", "hasDeleteCommunityPermission", "", "hasEditCommunityPermission", "leaveCommunity", "onLeaveSuccess", "onLeaveError", "validPermission", "permission", "community_debug"})
public final class EkoCommunitySettingViewModel extends com.ekoapp.ekosdk.uikit.community.notificationsettings.EkoPushNotificationBaseViewModel {
    
    @org.jetbrains.annotations.NotNull()
    public final io.reactivex.Completable getSettingsItems(@org.jetbrains.annotations.NotNull()
    java.lang.String communityId, @org.jetbrains.annotations.NotNull()
    com.ekoapp.ekosdk.community.EkoCommunity community, @org.jetbrains.annotations.NotNull()
    com.ekoapp.ekosdk.uikit.community.setting.CommunitySettingsMenuCreator menuCreator, @org.jetbrains.annotations.NotNull()
    kotlin.jvm.functions.Function1<? super java.util.List<? extends com.ekoapp.ekosdk.uikit.community.setting.SettingsItem>, kotlin.Unit> onResult, @org.jetbrains.annotations.NotNull()
    kotlin.jvm.functions.Function0<kotlin.Unit> onError) {
        return null;
    }
    
    private final io.reactivex.Flowable<java.util.List<com.ekoapp.ekosdk.uikit.community.setting.SettingsItem>> getItemsBasedOnPermissions(java.lang.String communityId, com.ekoapp.ekosdk.community.EkoCommunity community, com.ekoapp.ekosdk.uikit.community.setting.CommunitySettingsMenuCreator menuCreator) {
        return null;
    }
    
    public final boolean validPermission(@org.jetbrains.annotations.NotNull()
    com.ekoapp.ekosdk.community.EkoCommunity community, boolean permission) {
        return false;
    }
    
    private final int getPushStatus() {
        return 0;
    }
    
    private final io.reactivex.Flowable<java.lang.Boolean> hasEditCommunityPermission(java.lang.String communityId, com.ekoapp.ekosdk.community.EkoCommunity community) {
        return null;
    }
    
    private final io.reactivex.Flowable<java.lang.Boolean> hasDeleteCommunityPermission(java.lang.String communityId, com.ekoapp.ekosdk.community.EkoCommunity community) {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final io.reactivex.Completable leaveCommunity(@org.jetbrains.annotations.NotNull()
    java.lang.String communityId, @org.jetbrains.annotations.NotNull()
    kotlin.jvm.functions.Function0<kotlin.Unit> onLeaveSuccess, @org.jetbrains.annotations.NotNull()
    kotlin.jvm.functions.Function0<kotlin.Unit> onLeaveError) {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final io.reactivex.Completable closeCommunity(@org.jetbrains.annotations.NotNull()
    java.lang.String communityId, @org.jetbrains.annotations.NotNull()
    kotlin.jvm.functions.Function0<kotlin.Unit> onCloseSuccess, @org.jetbrains.annotations.NotNull()
    kotlin.jvm.functions.Function0<kotlin.Unit> onCloseError) {
        return null;
    }
    
    public EkoCommunitySettingViewModel() {
        super();
    }
}