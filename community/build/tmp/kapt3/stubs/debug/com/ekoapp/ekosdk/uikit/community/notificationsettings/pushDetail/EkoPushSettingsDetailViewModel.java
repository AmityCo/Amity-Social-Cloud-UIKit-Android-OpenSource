package com.ekoapp.ekosdk.uikit.community.notificationsettings.pushDetail;

import java.lang.System;

@kotlin.Metadata(mv = {1, 1, 16}, bv = {1, 0, 3}, k = 1, d1 = {"\u0000\u0088\u0001\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0002\b\u0005\n\u0002\u0010\b\n\u0002\b\u0011\n\u0002\u0018\u0002\n\u0002\u0010\u000b\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0013\n\u0002\u0010\u0002\n\u0002\b\u0003\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0007\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\b\u0018\u00002\u00020\u0001B\u0005\u00a2\u0006\u0002\u0010\u0002J\u0016\u00105\u001a\u0002062\u0006\u00107\u001a\u00020\u00042\u0006\u00108\u001a\u00020\nJ\u0014\u00109\u001a\b\u0012\u0004\u0012\u00020;0:2\u0006\u0010<\u001a\u00020=J\u0014\u0010>\u001a\b\u0012\u0004\u0012\u00020;0:2\u0006\u0010<\u001a\u00020?J\"\u0010@\u001a\u0014\u0012\u0010\u0012\u000e\u0012\u0004\u0012\u00020\n\u0012\u0004\u0012\u00020\u001d0A0:2\u0006\u0010B\u001a\u00020\"H\u0002JG\u0010C\u001a\u00020D2\u0006\u0010<\u001a\u00020=2\'\u0010E\u001a#\u0012\u0019\u0012\u0017\u0012\u0004\u0012\u00020;0:\u00a2\u0006\f\bG\u0012\b\bH\u0012\u0004\b\b(I\u0012\u0004\u0012\u0002060F2\f\u0010J\u001a\b\u0012\u0004\u0012\u0002060KH\u0002J\u001c\u0010L\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020;0:0M2\u0006\u0010<\u001a\u00020=H\u0002JM\u0010N\u001a\u00020D2\u0006\u0010O\u001a\u00020?2\u0006\u0010P\u001a\u00020=2\'\u0010E\u001a#\u0012\u0019\u0012\u0017\u0012\u0004\u0012\u00020;0:\u00a2\u0006\f\bG\u0012\b\bH\u0012\u0004\b\b(I\u0012\u0004\u0012\u0002060F2\f\u0010J\u001a\b\u0012\u0004\u0012\u0002060KJ\u001c\u0010Q\u001a\u000e\u0012\u0004\u0012\u00020\n\u0012\u0004\u0012\u00020\u001d0A2\u0006\u0010B\u001a\u00020\"H\u0002J\u001c\u0010R\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020;0:0M2\u0006\u0010<\u001a\u00020?H\u0002JG\u0010S\u001a\u00020D2\u0006\u0010<\u001a\u00020?2\'\u0010E\u001a#\u0012\u0019\u0012\u0017\u0012\u0004\u0012\u00020;0:\u00a2\u0006\f\bG\u0012\b\bH\u0012\u0004\b\b(I\u0012\u0004\u0012\u0002060F2\f\u0010J\u001a\b\u0012\u0004\u0012\u0002060KH\u0002J\u000e\u0010T\u001a\b\u0012\u0004\u0012\u00020U0MH\u0002J\u001c\u0010V\u001a\u000e\u0012\u0004\u0012\u00020\u001d\u0012\u0004\u0012\u00020W0A2\u0006\u0010X\u001a\u00020\nH\u0002J\u0006\u0010Y\u001a\u000206J\u0018\u0010Z\u001a\u0002062\b\u0010[\u001a\u0004\u0018\u00010\u00042\u0006\u00107\u001a\u00020\u0004J\b\u0010\\\u001a\u000206H\u0002J\"\u0010]\u001a\u00020D2\f\u0010^\u001a\b\u0012\u0004\u0012\u0002060K2\f\u0010J\u001a\b\u0012\u0004\u0012\u0002060KR\u001a\u0010\u0003\u001a\u00020\u0004X\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\u0005\u0010\u0006\"\u0004\b\u0007\u0010\bR\u001a\u0010\t\u001a\u00020\nX\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\u000b\u0010\f\"\u0004\b\r\u0010\u000eR\u001a\u0010\u000f\u001a\u00020\nX\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\u0010\u0010\f\"\u0004\b\u0011\u0010\u000eR\u001a\u0010\u0012\u001a\u00020\nX\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\u0013\u0010\f\"\u0004\b\u0014\u0010\u000eR\u001a\u0010\u0015\u001a\u00020\nX\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\u0016\u0010\f\"\u0004\b\u0017\u0010\u000eR\u001a\u0010\u0018\u001a\u00020\nX\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\u0019\u0010\f\"\u0004\b\u001a\u0010\u000eR\u0017\u0010\u001b\u001a\b\u0012\u0004\u0012\u00020\u001d0\u001c\u00a2\u0006\b\n\u0000\u001a\u0004\b\u001e\u0010\u001fR\u001a\u0010 \u001a\u000e\u0012\u0004\u0012\u00020\u0004\u0012\u0004\u0012\u00020\"0!X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u001a\u0010#\u001a\u00020\nX\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b$\u0010\f\"\u0004\b%\u0010\u000eR\u001a\u0010&\u001a\u00020\nX\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\'\u0010\f\"\u0004\b(\u0010\u000eR\u001a\u0010)\u001a\u00020\nX\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b*\u0010\f\"\u0004\b+\u0010\u000eR\u001a\u0010,\u001a\u00020\nX\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b-\u0010\f\"\u0004\b.\u0010\u000eR\u001a\u0010/\u001a\u00020\nX\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b0\u0010\f\"\u0004\b1\u0010\u000eR\u001a\u00102\u001a\u00020\u0004X\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b3\u0010\u0006\"\u0004\b4\u0010\b\u00a8\u0006_"}, d2 = {"Lcom/ekoapp/ekosdk/uikit/community/notificationsettings/pushDetail/EkoPushSettingsDetailViewModel;", "Lcom/ekoapp/ekosdk/uikit/community/notificationsettings/EkoPushNotificationBaseViewModel;", "()V", "communityId", "", "getCommunityId", "()Ljava/lang/String;", "setCommunityId", "(Ljava/lang/String;)V", "initialNewComment", "", "getInitialNewComment", "()I", "setInitialNewComment", "(I)V", "initialNewPost", "getInitialNewPost", "setInitialNewPost", "initialReactComment", "getInitialReactComment", "setInitialReactComment", "initialReactPost", "getInitialReactPost", "setInitialReactPost", "initialReplyComment", "getInitialReplyComment", "setInitialReplyComment", "initialStateChanged", "Landroidx/lifecycle/MutableLiveData;", "", "getInitialStateChanged", "()Landroidx/lifecycle/MutableLiveData;", "map", "Ljava/util/HashMap;", "Lcom/ekoapp/ekosdk/community/notification/EkoCommunityNotificationEvent;", "newComment", "getNewComment", "setNewComment", "newPost", "getNewPost", "setNewPost", "reactComment", "getReactComment", "setReactComment", "reactPost", "getReactPost", "setReactPost", "replyComment", "getReplyComment", "setReplyComment", "settingType", "getSettingType", "setSettingType", "changeState", "", "type", "value", "createCommentSettingsItem", "", "Lcom/ekoapp/ekosdk/uikit/community/setting/SettingsItem;", "menuCreator", "Lcom/ekoapp/ekosdk/uikit/community/notificationsettings/pushDetail/CommentMenuCreator;", "createPostSettingsItem", "Lcom/ekoapp/ekosdk/uikit/community/notificationsettings/pushDetail/PostMenuCreator;", "createPushChoices", "Lkotlin/Pair;", "notificationEvent", "getCommentSettingsItem", "Lio/reactivex/Completable;", "onResult", "Lkotlin/Function1;", "Lkotlin/ParameterName;", "name", "items", "onError", "Lkotlin/Function0;", "getCommentsSettingsBasedOnPermission", "Lio/reactivex/Single;", "getDetailSettingsItem", "postMenuCreator", "commentMenuCreator", "getInitialValue", "getPostSettingsBasedOnPermission", "getPostSettingsItem", "getPushNotificationSettings", "Lcom/ekoapp/ekosdk/community/notification/EkoCommunityNotificationSettings;", "getPushSettingUpdateModel", "Lcom/ekoapp/ekosdk/EkoRolesFilter;", "event", "resetState", "setInitialState", "id", "updateInitialState", "updatePushNotificationSettings", "onComplete", "community_debug"})
public final class EkoPushSettingsDetailViewModel extends com.ekoapp.ekosdk.uikit.community.notificationsettings.EkoPushNotificationBaseViewModel {
    @org.jetbrains.annotations.NotNull()
    private java.lang.String communityId = "";
    @org.jetbrains.annotations.NotNull()
    private java.lang.String settingType = "";
    private final java.util.HashMap<java.lang.String, com.ekoapp.ekosdk.community.notification.EkoCommunityNotificationEvent> map = null;
    @org.jetbrains.annotations.NotNull()
    private final androidx.lifecycle.MutableLiveData<java.lang.Boolean> initialStateChanged = null;
    private int initialReactPost = -1;
    private int initialNewPost = -1;
    private int initialReactComment = -1;
    private int initialNewComment = -1;
    private int initialReplyComment = -1;
    private int reactPost = -1;
    private int newPost = -1;
    private int reactComment = -1;
    private int replyComment = -1;
    private int newComment = -1;
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String getCommunityId() {
        return null;
    }
    
    public final void setCommunityId(@org.jetbrains.annotations.NotNull()
    java.lang.String p0) {
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String getSettingType() {
        return null;
    }
    
    public final void setSettingType(@org.jetbrains.annotations.NotNull()
    java.lang.String p0) {
    }
    
    @org.jetbrains.annotations.NotNull()
    public final androidx.lifecycle.MutableLiveData<java.lang.Boolean> getInitialStateChanged() {
        return null;
    }
    
    public final int getInitialReactPost() {
        return 0;
    }
    
    public final void setInitialReactPost(int p0) {
    }
    
    public final int getInitialNewPost() {
        return 0;
    }
    
    public final void setInitialNewPost(int p0) {
    }
    
    public final int getInitialReactComment() {
        return 0;
    }
    
    public final void setInitialReactComment(int p0) {
    }
    
    public final int getInitialNewComment() {
        return 0;
    }
    
    public final void setInitialNewComment(int p0) {
    }
    
    public final int getInitialReplyComment() {
        return 0;
    }
    
    public final void setInitialReplyComment(int p0) {
    }
    
    public final int getReactPost() {
        return 0;
    }
    
    public final void setReactPost(int p0) {
    }
    
    public final int getNewPost() {
        return 0;
    }
    
    public final void setNewPost(int p0) {
    }
    
    public final int getReactComment() {
        return 0;
    }
    
    public final void setReactComment(int p0) {
    }
    
    public final int getReplyComment() {
        return 0;
    }
    
    public final void setReplyComment(int p0) {
    }
    
    public final int getNewComment() {
        return 0;
    }
    
    public final void setNewComment(int p0) {
    }
    
    public final void setInitialState(@org.jetbrains.annotations.Nullable()
    java.lang.String id, @org.jetbrains.annotations.NotNull()
    java.lang.String type) {
    }
    
    @org.jetbrains.annotations.NotNull()
    public final io.reactivex.Completable getDetailSettingsItem(@org.jetbrains.annotations.NotNull()
    com.ekoapp.ekosdk.uikit.community.notificationsettings.pushDetail.PostMenuCreator postMenuCreator, @org.jetbrains.annotations.NotNull()
    com.ekoapp.ekosdk.uikit.community.notificationsettings.pushDetail.CommentMenuCreator commentMenuCreator, @org.jetbrains.annotations.NotNull()
    kotlin.jvm.functions.Function1<? super java.util.List<? extends com.ekoapp.ekosdk.uikit.community.setting.SettingsItem>, kotlin.Unit> onResult, @org.jetbrains.annotations.NotNull()
    kotlin.jvm.functions.Function0<kotlin.Unit> onError) {
        return null;
    }
    
    private final io.reactivex.Completable getPostSettingsItem(com.ekoapp.ekosdk.uikit.community.notificationsettings.pushDetail.PostMenuCreator menuCreator, kotlin.jvm.functions.Function1<? super java.util.List<? extends com.ekoapp.ekosdk.uikit.community.setting.SettingsItem>, kotlin.Unit> onResult, kotlin.jvm.functions.Function0<kotlin.Unit> onError) {
        return null;
    }
    
    private final io.reactivex.Single<java.util.List<com.ekoapp.ekosdk.uikit.community.setting.SettingsItem>> getPostSettingsBasedOnPermission(com.ekoapp.ekosdk.uikit.community.notificationsettings.pushDetail.PostMenuCreator menuCreator) {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.util.List<com.ekoapp.ekosdk.uikit.community.setting.SettingsItem> createPostSettingsItem(@org.jetbrains.annotations.NotNull()
    com.ekoapp.ekosdk.uikit.community.notificationsettings.pushDetail.PostMenuCreator menuCreator) {
        return null;
    }
    
    private final java.util.List<kotlin.Pair<java.lang.Integer, java.lang.Boolean>> createPushChoices(com.ekoapp.ekosdk.community.notification.EkoCommunityNotificationEvent notificationEvent) {
        return null;
    }
    
    private final kotlin.Pair<java.lang.Integer, java.lang.Boolean> getInitialValue(com.ekoapp.ekosdk.community.notification.EkoCommunityNotificationEvent notificationEvent) {
        return null;
    }
    
    private final io.reactivex.Completable getCommentSettingsItem(com.ekoapp.ekosdk.uikit.community.notificationsettings.pushDetail.CommentMenuCreator menuCreator, kotlin.jvm.functions.Function1<? super java.util.List<? extends com.ekoapp.ekosdk.uikit.community.setting.SettingsItem>, kotlin.Unit> onResult, kotlin.jvm.functions.Function0<kotlin.Unit> onError) {
        return null;
    }
    
    private final io.reactivex.Single<java.util.List<com.ekoapp.ekosdk.uikit.community.setting.SettingsItem>> getCommentsSettingsBasedOnPermission(com.ekoapp.ekosdk.uikit.community.notificationsettings.pushDetail.CommentMenuCreator menuCreator) {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.util.List<com.ekoapp.ekosdk.uikit.community.setting.SettingsItem> createCommentSettingsItem(@org.jetbrains.annotations.NotNull()
    com.ekoapp.ekosdk.uikit.community.notificationsettings.pushDetail.CommentMenuCreator menuCreator) {
        return null;
    }
    
    private final io.reactivex.Single<com.ekoapp.ekosdk.community.notification.EkoCommunityNotificationSettings> getPushNotificationSettings() {
        return null;
    }
    
    public final void changeState(@org.jetbrains.annotations.NotNull()
    java.lang.String type, int value) {
    }
    
    public final void resetState() {
    }
    
    private final void updateInitialState() {
    }
    
    @org.jetbrains.annotations.NotNull()
    public final io.reactivex.Completable updatePushNotificationSettings(@org.jetbrains.annotations.NotNull()
    kotlin.jvm.functions.Function0<kotlin.Unit> onComplete, @org.jetbrains.annotations.NotNull()
    kotlin.jvm.functions.Function0<kotlin.Unit> onError) {
        return null;
    }
    
    private final kotlin.Pair<java.lang.Boolean, com.ekoapp.ekosdk.EkoRolesFilter> getPushSettingUpdateModel(int event) {
        return null;
    }
    
    public EkoPushSettingsDetailViewModel() {
        super();
    }
}