package com.ekoapp.ekosdk.uikit.community.notificationsettings;

import java.lang.System;

@kotlin.Metadata(mv = {1, 1, 16}, bv = {1, 0, 3}, k = 1, d1 = {"\u00000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0000\n\u0002\u0018\u0002\n\u0002\u0010\u000b\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\u0018\u00002\u00020\u0001B\r\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\u0002\u0010\u0004J\u001e\u0010\u0005\u001a\u00020\u00062\u0006\u0010\u0007\u001a\u00020\b2\f\u0010\t\u001a\b\u0012\u0004\u0012\u00020\u000b0\nH\u0016J\u0010\u0010\f\u001a\u00020\r2\u0006\u0010\u0007\u001a\u00020\bH\u0016J\u0010\u0010\u000e\u001a\u00020\r2\u0006\u0010\u0007\u001a\u00020\bH\u0016R\u000e\u0010\u0002\u001a\u00020\u0003X\u0082\u0004\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u000f"}, d2 = {"Lcom/ekoapp/ekosdk/uikit/community/notificationsettings/PushNotificationMenuCreatorImpl;", "Lcom/ekoapp/ekosdk/uikit/community/notificationsettings/PushNotificationMenuCreator;", "fragment", "Lcom/ekoapp/ekosdk/uikit/community/notificationsettings/EkoPushNotificationSettingsFragment;", "(Lcom/ekoapp/ekosdk/uikit/community/notificationsettings/EkoPushNotificationSettingsFragment;)V", "createAllNotificationsMenu", "Lcom/ekoapp/ekosdk/uikit/community/setting/SettingsItem$ToggleContent;", "communityId", "", "isToggled", "Lio/reactivex/Flowable;", "", "createCommentMenu", "Lcom/ekoapp/ekosdk/uikit/community/setting/SettingsItem$NavigationContent;", "createPostMenu", "community_debug"})
public final class PushNotificationMenuCreatorImpl implements com.ekoapp.ekosdk.uikit.community.notificationsettings.PushNotificationMenuCreator {
    private final com.ekoapp.ekosdk.uikit.community.notificationsettings.EkoPushNotificationSettingsFragment fragment = null;
    
    @org.jetbrains.annotations.NotNull()
    @java.lang.Override()
    public com.ekoapp.ekosdk.uikit.community.setting.SettingsItem.ToggleContent createAllNotificationsMenu(@org.jetbrains.annotations.NotNull()
    java.lang.String communityId, @org.jetbrains.annotations.NotNull()
    io.reactivex.Flowable<java.lang.Boolean> isToggled) {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    @java.lang.Override()
    public com.ekoapp.ekosdk.uikit.community.setting.SettingsItem.NavigationContent createPostMenu(@org.jetbrains.annotations.NotNull()
    java.lang.String communityId) {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    @java.lang.Override()
    public com.ekoapp.ekosdk.uikit.community.setting.SettingsItem.NavigationContent createCommentMenu(@org.jetbrains.annotations.NotNull()
    java.lang.String communityId) {
        return null;
    }
    
    public PushNotificationMenuCreatorImpl(@org.jetbrains.annotations.NotNull()
    com.ekoapp.ekosdk.uikit.community.notificationsettings.EkoPushNotificationSettingsFragment fragment) {
        super();
    }
}