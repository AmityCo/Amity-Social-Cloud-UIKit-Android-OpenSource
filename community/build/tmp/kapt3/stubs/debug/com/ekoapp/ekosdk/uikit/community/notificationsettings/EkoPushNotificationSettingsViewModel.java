package com.ekoapp.ekosdk.uikit.community.notificationsettings;

import java.lang.System;

@kotlin.Metadata(mv = {1, 1, 16}, bv = {1, 0, 3}, k = 1, d1 = {"\u0000b\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0010\u000e\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\u0010\u000b\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0000\u0018\u00002\u00020\u0001B\u0005\u00a2\u0006\u0002\u0010\u0002J\u0016\u0010\u0013\u001a\b\u0012\u0004\u0012\u00020\u00110\u00142\u0006\u0010\u0015\u001a\u00020\u0011H\u0002J$\u0010\u0016\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u00180\u00170\u00142\u0006\u0010\u0019\u001a\u00020\u001a2\u0006\u0010\u0015\u001a\u00020\u0011H\u0002J?\u0010\u001b\u001a\u00020\u001c2\u0006\u0010\u0019\u001a\u00020\u001a2\u0006\u0010\u001d\u001a\u00020\u00112\'\u0010\u001e\u001a#\u0012\u0019\u0012\u0017\u0012\u0004\u0012\u00020\u00180\u0017\u00a2\u0006\f\b \u0012\b\b!\u0012\u0004\b\b(\"\u0012\u0004\u0012\u00020#0\u001fJ\u000e\u0010$\u001a\b\u0012\u0004\u0012\u00020\u00110\u0014H\u0002J\u000e\u0010%\u001a\u00020#2\u0006\u0010\u0015\u001a\u00020\u0011J\u001a\u0010\r\u001a\u00020#2\b\u0010\t\u001a\u0004\u0018\u00010\n2\b\u0010\u0003\u001a\u0004\u0018\u00010\u0004J\u001c\u0010&\u001a\u00020\u001c2\u0006\u0010\'\u001a\u00020\u00112\f\u0010(\u001a\b\u0012\u0004\u0012\u00020#0)R\u001c\u0010\u0003\u001a\u0004\u0018\u00010\u0004X\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\u0005\u0010\u0006\"\u0004\b\u0007\u0010\bR\u001a\u0010\t\u001a\u00020\nX\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\u000b\u0010\f\"\u0004\b\r\u0010\u000eR\u001c\u0010\u000f\u001a\u0010\u0012\f\u0012\n \u0012*\u0004\u0018\u00010\u00110\u00110\u0010X\u0082\u0004\u00a2\u0006\u0002\n\u0000\u00a8\u0006*"}, d2 = {"Lcom/ekoapp/ekosdk/uikit/community/notificationsettings/EkoPushNotificationSettingsViewModel;", "Lcom/ekoapp/ekosdk/uikit/community/notificationsettings/EkoPushNotificationBaseViewModel;", "()V", "community", "Lcom/ekoapp/ekosdk/community/EkoCommunity;", "getCommunity", "()Lcom/ekoapp/ekosdk/community/EkoCommunity;", "setCommunity", "(Lcom/ekoapp/ekosdk/community/EkoCommunity;)V", "communityId", "", "getCommunityId", "()Ljava/lang/String;", "setCommunityId", "(Ljava/lang/String;)V", "isToggleState", "Lio/reactivex/subjects/PublishSubject;", "", "kotlin.jvm.PlatformType", "getAllNotificationDataSource", "Lio/reactivex/Flowable;", "value", "getItemsBasedOnPermission", "", "Lcom/ekoapp/ekosdk/uikit/community/setting/SettingsItem;", "menuCreator", "Lcom/ekoapp/ekosdk/uikit/community/notificationsettings/PushNotificationMenuCreator;", "getPushNotificationItems", "Lio/reactivex/Completable;", "startValue", "onResult", "Lkotlin/Function1;", "Lkotlin/ParameterName;", "name", "items", "", "getReversionSource", "revertToggleState", "updatePushNotificationSettings", "enable", "onError", "Lkotlin/Function0;", "community_debug"})
public final class EkoPushNotificationSettingsViewModel extends com.ekoapp.ekosdk.uikit.community.notificationsettings.EkoPushNotificationBaseViewModel {
    @org.jetbrains.annotations.Nullable()
    private com.ekoapp.ekosdk.community.EkoCommunity community;
    @org.jetbrains.annotations.NotNull()
    private java.lang.String communityId = "";
    private final io.reactivex.subjects.PublishSubject<java.lang.Boolean> isToggleState = null;
    
    @org.jetbrains.annotations.Nullable()
    public final com.ekoapp.ekosdk.community.EkoCommunity getCommunity() {
        return null;
    }
    
    public final void setCommunity(@org.jetbrains.annotations.Nullable()
    com.ekoapp.ekosdk.community.EkoCommunity p0) {
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String getCommunityId() {
        return null;
    }
    
    public final void setCommunityId(@org.jetbrains.annotations.NotNull()
    java.lang.String p0) {
    }
    
    public final void setCommunityId(@org.jetbrains.annotations.Nullable()
    java.lang.String communityId, @org.jetbrains.annotations.Nullable()
    com.ekoapp.ekosdk.community.EkoCommunity community) {
    }
    
    @org.jetbrains.annotations.NotNull()
    public final io.reactivex.Completable getPushNotificationItems(@org.jetbrains.annotations.NotNull()
    com.ekoapp.ekosdk.uikit.community.notificationsettings.PushNotificationMenuCreator menuCreator, boolean startValue, @org.jetbrains.annotations.NotNull()
    kotlin.jvm.functions.Function1<? super java.util.List<? extends com.ekoapp.ekosdk.uikit.community.setting.SettingsItem>, kotlin.Unit> onResult) {
        return null;
    }
    
    private final io.reactivex.Flowable<java.util.List<com.ekoapp.ekosdk.uikit.community.setting.SettingsItem>> getItemsBasedOnPermission(com.ekoapp.ekosdk.uikit.community.notificationsettings.PushNotificationMenuCreator menuCreator, boolean value) {
        return null;
    }
    
    private final io.reactivex.Flowable<java.lang.Boolean> getAllNotificationDataSource(boolean value) {
        return null;
    }
    
    private final io.reactivex.Flowable<java.lang.Boolean> getReversionSource() {
        return null;
    }
    
    public final void revertToggleState(boolean value) {
    }
    
    @org.jetbrains.annotations.NotNull()
    public final io.reactivex.Completable updatePushNotificationSettings(boolean enable, @org.jetbrains.annotations.NotNull()
    kotlin.jvm.functions.Function0<kotlin.Unit> onError) {
        return null;
    }
    
    public EkoPushNotificationSettingsViewModel() {
        super();
    }
}