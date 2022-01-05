package com.ekoapp.ekosdk.uikit.community.setting.postreview;

import java.lang.System;

@kotlin.Metadata(mv = {1, 1, 16}, bv = {1, 0, 3}, k = 1, d1 = {"\u0000d\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0010\u000b\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0002\b\b\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0002\u0018\u00002\u00020\u0001B\u0005\u00a2\u0006\u0002\u0010\u0002J$\u0010\u0007\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\n0\t0\b2\u0006\u0010\u000b\u001a\u00020\f2\u0006\u0010\r\u001a\u00020\u000eH\u0002J\u0016\u0010\u000f\u001a\b\u0012\u0004\u0012\u00020\u00050\u00102\u0006\u0010\u000b\u001a\u00020\fH\u0002J\u0016\u0010\u0011\u001a\b\u0012\u0004\u0012\u00020\u00050\u00102\u0006\u0010\u000b\u001a\u00020\fH\u0002J\u000e\u0010\u0012\u001a\b\u0012\u0004\u0012\u00020\u00050\u0010H\u0002J?\u0010\u0013\u001a\u00020\u00142\u0006\u0010\u000b\u001a\u00020\f2\u0006\u0010\r\u001a\u00020\u000e2\'\u0010\u0015\u001a#\u0012\u0019\u0012\u0017\u0012\u0004\u0012\u00020\n0\t\u00a2\u0006\f\b\u0017\u0012\b\b\u0018\u0012\u0004\b\b(\u0019\u0012\u0004\u0012\u00020\u001a0\u0016J\u0016\u0010\u001b\u001a\b\u0012\u0004\u0012\u00020\u00050\b2\u0006\u0010\u000b\u001a\u00020\fH\u0002J\u0006\u0010\u001c\u001a\u00020\u001aJ6\u0010\u001d\u001a\u00020\u001a2\u0006\u0010\u001e\u001a\u00020\u00052\u0012\u0010\u001f\u001a\u000e\u0012\u0004\u0012\u00020\u0005\u0012\u0004\u0012\u00020\u001a0\u00162\u0012\u0010 \u001a\u000e\u0012\u0004\u0012\u00020\u0005\u0012\u0004\u0012\u00020\u001a0\u0016J*\u0010!\u001a\u00020\u00142\u0006\u0010\u000b\u001a\u00020\f2\f\u0010\"\u001a\b\u0012\u0004\u0012\u00020\u001a0#2\f\u0010$\u001a\b\u0012\u0004\u0012\u00020\u001a0#J\u001c\u0010%\u001a\u00020\u00142\u0006\u0010\u000b\u001a\u00020\f2\f\u0010$\u001a\b\u0012\u0004\u0012\u00020\u001a0#J\u001e\u0010&\u001a\b\u0012\u0004\u0012\u00020\'0\b2\u0006\u0010\u000b\u001a\u00020\f2\u0006\u0010(\u001a\u00020\u0005H\u0002R\u001c\u0010\u0003\u001a\u0010\u0012\f\u0012\n \u0006*\u0004\u0018\u00010\u00050\u00050\u0004X\u0082\u0004\u00a2\u0006\u0002\n\u0000\u00a8\u0006)"}, d2 = {"Lcom/ekoapp/ekosdk/uikit/community/setting/postreview/EkoPostReviewSettingsViewModel;", "Lcom/ekoapp/ekosdk/uikit/base/EkoBaseViewModel;", "()V", "isToggleState", "Lio/reactivex/subjects/PublishSubject;", "", "kotlin.jvm.PlatformType", "getItemsBasedOnPermissions", "Lio/reactivex/Single;", "", "Lcom/ekoapp/ekosdk/uikit/community/setting/SettingsItem;", "communityId", "", "menuCreator", "Lcom/ekoapp/ekosdk/uikit/community/setting/postreview/PostReviewSettingsMenuCreator;", "getNeedApprovalState", "Lio/reactivex/Flowable;", "getNeedPostApprovalDataSource", "getReversionSource", "getSettingsItems", "Lio/reactivex/Completable;", "onResult", "Lkotlin/Function1;", "Lkotlin/ParameterName;", "name", "items", "", "hasEditCommunityPermission", "revertToggleState", "toggleDecision", "isChecked", "turnOffEvent", "turnOnEvent", "turnOff", "onSuccess", "Lkotlin/Function0;", "onError", "turnOn", "updateApproveMemberPost", "Lcom/ekoapp/ekosdk/community/EkoCommunity;", "isEnable", "community_debug"})
public final class EkoPostReviewSettingsViewModel extends com.ekoapp.ekosdk.uikit.base.EkoBaseViewModel {
    private final io.reactivex.subjects.PublishSubject<java.lang.Boolean> isToggleState = null;
    
    @org.jetbrains.annotations.NotNull()
    public final io.reactivex.Completable getSettingsItems(@org.jetbrains.annotations.NotNull()
    java.lang.String communityId, @org.jetbrains.annotations.NotNull()
    com.ekoapp.ekosdk.uikit.community.setting.postreview.PostReviewSettingsMenuCreator menuCreator, @org.jetbrains.annotations.NotNull()
    kotlin.jvm.functions.Function1<? super java.util.List<? extends com.ekoapp.ekosdk.uikit.community.setting.SettingsItem>, kotlin.Unit> onResult) {
        return null;
    }
    
    private final io.reactivex.Single<java.util.List<com.ekoapp.ekosdk.uikit.community.setting.SettingsItem>> getItemsBasedOnPermissions(java.lang.String communityId, com.ekoapp.ekosdk.uikit.community.setting.postreview.PostReviewSettingsMenuCreator menuCreator) {
        return null;
    }
    
    private final io.reactivex.Single<java.lang.Boolean> hasEditCommunityPermission(java.lang.String communityId) {
        return null;
    }
    
    public final void toggleDecision(boolean isChecked, @org.jetbrains.annotations.NotNull()
    kotlin.jvm.functions.Function1<? super java.lang.Boolean, kotlin.Unit> turnOffEvent, @org.jetbrains.annotations.NotNull()
    kotlin.jvm.functions.Function1<? super java.lang.Boolean, kotlin.Unit> turnOnEvent) {
    }
    
    @org.jetbrains.annotations.NotNull()
    public final io.reactivex.Completable turnOn(@org.jetbrains.annotations.NotNull()
    java.lang.String communityId, @org.jetbrains.annotations.NotNull()
    kotlin.jvm.functions.Function0<kotlin.Unit> onError) {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final io.reactivex.Completable turnOff(@org.jetbrains.annotations.NotNull()
    java.lang.String communityId, @org.jetbrains.annotations.NotNull()
    kotlin.jvm.functions.Function0<kotlin.Unit> onSuccess, @org.jetbrains.annotations.NotNull()
    kotlin.jvm.functions.Function0<kotlin.Unit> onError) {
        return null;
    }
    
    private final io.reactivex.Flowable<java.lang.Boolean> getNeedPostApprovalDataSource(java.lang.String communityId) {
        return null;
    }
    
    private final io.reactivex.Flowable<java.lang.Boolean> getNeedApprovalState(java.lang.String communityId) {
        return null;
    }
    
    private final io.reactivex.Flowable<java.lang.Boolean> getReversionSource() {
        return null;
    }
    
    public final void revertToggleState() {
    }
    
    private final io.reactivex.Single<com.ekoapp.ekosdk.community.EkoCommunity> updateApproveMemberPost(java.lang.String communityId, boolean isEnable) {
        return null;
    }
    
    public EkoPostReviewSettingsViewModel() {
        super();
    }
}