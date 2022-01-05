package com.ekoapp.ekosdk.uikit.community.notificationsettings;

import java.lang.System;

@kotlin.Metadata(mv = {1, 1, 16}, bv = {1, 0, 3}, k = 1, d1 = {"\u0000(\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0000\n\u0002\u0018\u0002\n\u0002\u0010\u000b\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\bf\u0018\u00002\u00020\u0001J\u001e\u0010\u0002\u001a\u00020\u00032\u0006\u0010\u0004\u001a\u00020\u00052\f\u0010\u0006\u001a\b\u0012\u0004\u0012\u00020\b0\u0007H&J\u0010\u0010\t\u001a\u00020\n2\u0006\u0010\u0004\u001a\u00020\u0005H&J\u0010\u0010\u000b\u001a\u00020\n2\u0006\u0010\u0004\u001a\u00020\u0005H&\u00a8\u0006\f"}, d2 = {"Lcom/ekoapp/ekosdk/uikit/community/notificationsettings/PushNotificationMenuCreator;", "", "createAllNotificationsMenu", "Lcom/ekoapp/ekosdk/uikit/community/setting/SettingsItem$ToggleContent;", "communityId", "", "isToggled", "Lio/reactivex/Flowable;", "", "createCommentMenu", "Lcom/ekoapp/ekosdk/uikit/community/setting/SettingsItem$NavigationContent;", "createPostMenu", "community_debug"})
public abstract interface PushNotificationMenuCreator {
    
    @org.jetbrains.annotations.NotNull()
    public abstract com.ekoapp.ekosdk.uikit.community.setting.SettingsItem.ToggleContent createAllNotificationsMenu(@org.jetbrains.annotations.NotNull()
    java.lang.String communityId, @org.jetbrains.annotations.NotNull()
    io.reactivex.Flowable<java.lang.Boolean> isToggled);
    
    @org.jetbrains.annotations.NotNull()
    public abstract com.ekoapp.ekosdk.uikit.community.setting.SettingsItem.NavigationContent createPostMenu(@org.jetbrains.annotations.NotNull()
    java.lang.String communityId);
    
    @org.jetbrains.annotations.NotNull()
    public abstract com.ekoapp.ekosdk.uikit.community.setting.SettingsItem.NavigationContent createCommentMenu(@org.jetbrains.annotations.NotNull()
    java.lang.String communityId);
}