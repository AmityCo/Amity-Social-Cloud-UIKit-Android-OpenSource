package com.ekoapp.ekosdk.uikit.community.setting;

import java.lang.System;

@kotlin.Metadata(mv = {1, 1, 16}, bv = {1, 0, 3}, k = 1, d1 = {"\u0000&\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0010\b\n\u0002\b\u0002\bf\u0018\u00002\u00020\u0001J\u0010\u0010\u0002\u001a\u00020\u00032\u0006\u0010\u0004\u001a\u00020\u0005H&J\u0010\u0010\u0006\u001a\u00020\u00072\u0006\u0010\u0004\u001a\u00020\u0005H&J\u0010\u0010\b\u001a\u00020\u00032\u0006\u0010\u0004\u001a\u00020\u0005H&J\u0010\u0010\t\u001a\u00020\u00072\u0006\u0010\u0004\u001a\u00020\u0005H&J\u0018\u0010\n\u001a\u00020\u00072\u0006\u0010\u0004\u001a\u00020\u00052\u0006\u0010\u000b\u001a\u00020\fH&J\u0010\u0010\r\u001a\u00020\u00072\u0006\u0010\u0004\u001a\u00020\u0005H&\u00a8\u0006\u000e"}, d2 = {"Lcom/ekoapp/ekosdk/uikit/community/setting/CommunitySettingsMenuCreator;", "", "createCloseCommunityMenu", "Lcom/ekoapp/ekosdk/uikit/community/setting/SettingsItem$TextContent;", "communityId", "", "createEditProfileMenu", "Lcom/ekoapp/ekosdk/uikit/community/setting/SettingsItem$NavigationContent;", "createLeaveCommunityMenu", "createMembersMenu", "createNotificationMenu", "value", "", "createPostReviewMenu", "community_debug"})
public abstract interface CommunitySettingsMenuCreator {
    
    @org.jetbrains.annotations.NotNull()
    public abstract com.ekoapp.ekosdk.uikit.community.setting.SettingsItem.NavigationContent createEditProfileMenu(@org.jetbrains.annotations.NotNull()
    java.lang.String communityId);
    
    @org.jetbrains.annotations.NotNull()
    public abstract com.ekoapp.ekosdk.uikit.community.setting.SettingsItem.NavigationContent createMembersMenu(@org.jetbrains.annotations.NotNull()
    java.lang.String communityId);
    
    @org.jetbrains.annotations.NotNull()
    public abstract com.ekoapp.ekosdk.uikit.community.setting.SettingsItem.NavigationContent createNotificationMenu(@org.jetbrains.annotations.NotNull()
    java.lang.String communityId, int value);
    
    @org.jetbrains.annotations.NotNull()
    public abstract com.ekoapp.ekosdk.uikit.community.setting.SettingsItem.NavigationContent createPostReviewMenu(@org.jetbrains.annotations.NotNull()
    java.lang.String communityId);
    
    @org.jetbrains.annotations.NotNull()
    public abstract com.ekoapp.ekosdk.uikit.community.setting.SettingsItem.TextContent createLeaveCommunityMenu(@org.jetbrains.annotations.NotNull()
    java.lang.String communityId);
    
    @org.jetbrains.annotations.NotNull()
    public abstract com.ekoapp.ekosdk.uikit.community.setting.SettingsItem.TextContent createCloseCommunityMenu(@org.jetbrains.annotations.NotNull()
    java.lang.String communityId);
}