package com.ekoapp.ekosdk.uikit.community.notificationsettings;

import java.lang.System;

@kotlin.Metadata(mv = {1, 1, 16}, bv = {1, 0, 3}, k = 1, d1 = {"\u0000H\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u000b\n\u0002\b\f\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u000e\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0004\b\u0016\u0018\u00002\u00020\u0001B\u0005\u00a2\u0006\u0002\u0010\u0002J\u0010\u0010\u0010\u001a\u00020\u00112\u0006\u0010\u0012\u001a\u00020\u0013H\u0002J\u0010\u0010\u0014\u001a\u00020\u00112\u0006\u0010\u0015\u001a\u00020\u0016H\u0002J\"\u0010\u0017\u001a\u00020\u00182\f\u0010\u0019\u001a\b\u0012\u0004\u0012\u00020\u00110\u001a2\f\u0010\u001b\u001a\b\u0012\u0004\u0012\u00020\u00110\u001aJ?\u0010\u001c\u001a\u00020\u00182\u0006\u0010\u001d\u001a\u00020\u001e2!\u0010\u001f\u001a\u001d\u0012\u0013\u0012\u00110\u0004\u00a2\u0006\f\b!\u0012\b\b\"\u0012\u0004\b\b(#\u0012\u0004\u0012\u00020\u00110 2\f\u0010$\u001a\b\u0012\u0004\u0012\u00020\u00110\u001aR\u001a\u0010\u0003\u001a\u00020\u0004X\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\u0003\u0010\u0005\"\u0004\b\u0006\u0010\u0007R\u001a\u0010\b\u001a\u00020\u0004X\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\b\u0010\u0005\"\u0004\b\t\u0010\u0007R\u001a\u0010\n\u001a\u00020\u0004X\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\n\u0010\u0005\"\u0004\b\u000b\u0010\u0007R\u001a\u0010\f\u001a\u00020\u0004X\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\f\u0010\u0005\"\u0004\b\r\u0010\u0007R\u001a\u0010\u000e\u001a\u00020\u0004X\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\u000e\u0010\u0005\"\u0004\b\u000f\u0010\u0007\u00a8\u0006%"}, d2 = {"Lcom/ekoapp/ekosdk/uikit/community/notificationsettings/EkoPushNotificationBaseViewModel;", "Lcom/ekoapp/ekosdk/uikit/base/EkoBaseViewModel;", "()V", "isCommentEnabled", "", "()Z", "setCommentEnabled", "(Z)V", "isCommunityPushEnabled", "setCommunityPushEnabled", "isGlobalModerator", "setGlobalModerator", "isGlobalPushEnabled", "setGlobalPushEnabled", "isPostEnabled", "setPostEnabled", "checkGlobalPushRole", "", "notification", "Lcom/ekoapp/ekosdk/user/notification/EkoUserNotificationSettings;", "checkPushSettings", "settings", "Lcom/ekoapp/ekosdk/community/notification/EkoCommunityNotificationSettings;", "getGlobalPushNotificationSettings", "Lio/reactivex/Completable;", "onSuccess", "Lkotlin/Function0;", "onError", "getPushNotificationSettings", "communityId", "", "onDataLoaded", "Lkotlin/Function1;", "Lkotlin/ParameterName;", "name", "value", "onDataError", "community_debug"})
public class EkoPushNotificationBaseViewModel extends com.ekoapp.ekosdk.uikit.base.EkoBaseViewModel {
    private boolean isPostEnabled = false;
    private boolean isCommentEnabled = false;
    private boolean isGlobalModerator = false;
    private boolean isGlobalPushEnabled = true;
    private boolean isCommunityPushEnabled = false;
    
    public final boolean isPostEnabled() {
        return false;
    }
    
    public final void setPostEnabled(boolean p0) {
    }
    
    public final boolean isCommentEnabled() {
        return false;
    }
    
    public final void setCommentEnabled(boolean p0) {
    }
    
    public final boolean isGlobalModerator() {
        return false;
    }
    
    public final void setGlobalModerator(boolean p0) {
    }
    
    public final boolean isGlobalPushEnabled() {
        return false;
    }
    
    public final void setGlobalPushEnabled(boolean p0) {
    }
    
    public final boolean isCommunityPushEnabled() {
        return false;
    }
    
    public final void setCommunityPushEnabled(boolean p0) {
    }
    
    @org.jetbrains.annotations.NotNull()
    public final io.reactivex.Completable getPushNotificationSettings(@org.jetbrains.annotations.NotNull()
    java.lang.String communityId, @org.jetbrains.annotations.NotNull()
    kotlin.jvm.functions.Function1<? super java.lang.Boolean, kotlin.Unit> onDataLoaded, @org.jetbrains.annotations.NotNull()
    kotlin.jvm.functions.Function0<kotlin.Unit> onDataError) {
        return null;
    }
    
    private final void checkPushSettings(com.ekoapp.ekosdk.community.notification.EkoCommunityNotificationSettings settings) {
    }
    
    @org.jetbrains.annotations.NotNull()
    public final io.reactivex.Completable getGlobalPushNotificationSettings(@org.jetbrains.annotations.NotNull()
    kotlin.jvm.functions.Function0<kotlin.Unit> onSuccess, @org.jetbrains.annotations.NotNull()
    kotlin.jvm.functions.Function0<kotlin.Unit> onError) {
        return null;
    }
    
    private final void checkGlobalPushRole(com.ekoapp.ekosdk.user.notification.EkoUserNotificationSettings notification) {
    }
    
    public EkoPushNotificationBaseViewModel() {
        super();
    }
}