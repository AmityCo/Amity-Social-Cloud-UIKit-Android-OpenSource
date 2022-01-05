package com.ekoapp.ekosdk.uikit.community.notificationsettings.pushDetail;

import java.lang.System;

@kotlin.Metadata(mv = {1, 1, 16}, bv = {1, 0, 3}, k = 1, d1 = {"\u00000\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0002\u0010\b\n\u0002\u0010\u000b\n\u0002\b\u0005\bf\u0018\u00002\u00020\u0001J\u0010\u0010\u0002\u001a\u00020\u00032\u0006\u0010\u0004\u001a\u00020\u0005H&J*\u0010\u0006\u001a\u00020\u00072\u0006\u0010\u0004\u001a\u00020\u00052\u0018\u0010\b\u001a\u0014\u0012\u0010\u0012\u000e\u0012\u0004\u0012\u00020\u000b\u0012\u0004\u0012\u00020\f0\n0\tH&J\u0010\u0010\r\u001a\u00020\u00032\u0006\u0010\u0004\u001a\u00020\u0005H&J*\u0010\u000e\u001a\u00020\u00072\u0006\u0010\u0004\u001a\u00020\u00052\u0018\u0010\b\u001a\u0014\u0012\u0010\u0012\u000e\u0012\u0004\u0012\u00020\u000b\u0012\u0004\u0012\u00020\f0\n0\tH&J\u0010\u0010\u000f\u001a\u00020\u00032\u0006\u0010\u0004\u001a\u00020\u0005H&J*\u0010\u0010\u001a\u00020\u00072\u0006\u0010\u0004\u001a\u00020\u00052\u0018\u0010\b\u001a\u0014\u0012\u0010\u0012\u000e\u0012\u0004\u0012\u00020\u000b\u0012\u0004\u0012\u00020\f0\n0\tH&\u00a8\u0006\u0011"}, d2 = {"Lcom/ekoapp/ekosdk/uikit/community/notificationsettings/pushDetail/CommentMenuCreator;", "", "createNewCommentsMenu", "Lcom/ekoapp/ekosdk/uikit/community/setting/SettingsItem$TextContent;", "communityId", "", "createNewCommentsRadioMenu", "Lcom/ekoapp/ekosdk/uikit/community/setting/SettingsItem$RadioContent;", "choices", "", "Lkotlin/Pair;", "", "", "createReactCommentsMenu", "createReactCommentsRadioMenu", "createReplyCommentsMenu", "createReplyCommentsRadioMenu", "community_debug"})
public abstract interface CommentMenuCreator {
    
    @org.jetbrains.annotations.NotNull()
    public abstract com.ekoapp.ekosdk.uikit.community.setting.SettingsItem.TextContent createReactCommentsMenu(@org.jetbrains.annotations.NotNull()
    java.lang.String communityId);
    
    @org.jetbrains.annotations.NotNull()
    public abstract com.ekoapp.ekosdk.uikit.community.setting.SettingsItem.RadioContent createReactCommentsRadioMenu(@org.jetbrains.annotations.NotNull()
    java.lang.String communityId, @org.jetbrains.annotations.NotNull()
    java.util.List<kotlin.Pair<java.lang.Integer, java.lang.Boolean>> choices);
    
    @org.jetbrains.annotations.NotNull()
    public abstract com.ekoapp.ekosdk.uikit.community.setting.SettingsItem.TextContent createNewCommentsMenu(@org.jetbrains.annotations.NotNull()
    java.lang.String communityId);
    
    @org.jetbrains.annotations.NotNull()
    public abstract com.ekoapp.ekosdk.uikit.community.setting.SettingsItem.RadioContent createNewCommentsRadioMenu(@org.jetbrains.annotations.NotNull()
    java.lang.String communityId, @org.jetbrains.annotations.NotNull()
    java.util.List<kotlin.Pair<java.lang.Integer, java.lang.Boolean>> choices);
    
    @org.jetbrains.annotations.NotNull()
    public abstract com.ekoapp.ekosdk.uikit.community.setting.SettingsItem.TextContent createReplyCommentsMenu(@org.jetbrains.annotations.NotNull()
    java.lang.String communityId);
    
    @org.jetbrains.annotations.NotNull()
    public abstract com.ekoapp.ekosdk.uikit.community.setting.SettingsItem.RadioContent createReplyCommentsRadioMenu(@org.jetbrains.annotations.NotNull()
    java.lang.String communityId, @org.jetbrains.annotations.NotNull()
    java.util.List<kotlin.Pair<java.lang.Integer, java.lang.Boolean>> choices);
}