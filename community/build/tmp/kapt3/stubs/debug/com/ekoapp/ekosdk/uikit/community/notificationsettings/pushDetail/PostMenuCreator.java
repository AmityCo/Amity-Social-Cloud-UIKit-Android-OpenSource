package com.ekoapp.ekosdk.uikit.community.notificationsettings.pushDetail;

import java.lang.System;

@kotlin.Metadata(mv = {1, 1, 16}, bv = {1, 0, 3}, k = 1, d1 = {"\u00000\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0002\u0010\b\n\u0002\u0010\u000b\n\u0002\b\u0003\bf\u0018\u00002\u00020\u0001J\u0010\u0010\u0002\u001a\u00020\u00032\u0006\u0010\u0004\u001a\u00020\u0005H&J*\u0010\u0006\u001a\u00020\u00072\u0006\u0010\u0004\u001a\u00020\u00052\u0018\u0010\b\u001a\u0014\u0012\u0010\u0012\u000e\u0012\u0004\u0012\u00020\u000b\u0012\u0004\u0012\u00020\f0\n0\tH&J\u0010\u0010\r\u001a\u00020\u00032\u0006\u0010\u0004\u001a\u00020\u0005H&J*\u0010\u000e\u001a\u00020\u00072\u0006\u0010\u0004\u001a\u00020\u00052\u0018\u0010\b\u001a\u0014\u0012\u0010\u0012\u000e\u0012\u0004\u0012\u00020\u000b\u0012\u0004\u0012\u00020\f0\n0\tH&\u00a8\u0006\u000f"}, d2 = {"Lcom/ekoapp/ekosdk/uikit/community/notificationsettings/pushDetail/PostMenuCreator;", "", "createNewPostMenu", "Lcom/ekoapp/ekosdk/uikit/community/setting/SettingsItem$TextContent;", "communityId", "", "createNewPostRadioMenu", "Lcom/ekoapp/ekosdk/uikit/community/setting/SettingsItem$RadioContent;", "choices", "", "Lkotlin/Pair;", "", "", "createReactPostMenu", "createReactPostRadioMenu", "community_debug"})
public abstract interface PostMenuCreator {
    
    @org.jetbrains.annotations.NotNull()
    public abstract com.ekoapp.ekosdk.uikit.community.setting.SettingsItem.TextContent createReactPostMenu(@org.jetbrains.annotations.NotNull()
    java.lang.String communityId);
    
    @org.jetbrains.annotations.NotNull()
    public abstract com.ekoapp.ekosdk.uikit.community.setting.SettingsItem.RadioContent createReactPostRadioMenu(@org.jetbrains.annotations.NotNull()
    java.lang.String communityId, @org.jetbrains.annotations.NotNull()
    java.util.List<kotlin.Pair<java.lang.Integer, java.lang.Boolean>> choices);
    
    @org.jetbrains.annotations.NotNull()
    public abstract com.ekoapp.ekosdk.uikit.community.setting.SettingsItem.TextContent createNewPostMenu(@org.jetbrains.annotations.NotNull()
    java.lang.String communityId);
    
    @org.jetbrains.annotations.NotNull()
    public abstract com.ekoapp.ekosdk.uikit.community.setting.SettingsItem.RadioContent createNewPostRadioMenu(@org.jetbrains.annotations.NotNull()
    java.lang.String communityId, @org.jetbrains.annotations.NotNull()
    java.util.List<kotlin.Pair<java.lang.Integer, java.lang.Boolean>> choices);
}