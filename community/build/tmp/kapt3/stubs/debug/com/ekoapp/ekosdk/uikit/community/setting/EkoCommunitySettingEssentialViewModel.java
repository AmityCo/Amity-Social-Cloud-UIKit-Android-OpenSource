package com.ekoapp.ekosdk.uikit.community.setting;

import java.lang.System;

@kotlin.Metadata(mv = {1, 1, 16}, bv = {1, 0, 3}, k = 1, d1 = {"\u0000.\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0010\u000e\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0010\u0002\n\u0002\b\u0002\u0018\u00002\u00020\u0001B\u0005\u00a2\u0006\u0002\u0010\u0002J,\u0010\u0005\u001a\u00020\u000f2\u0006\u0010\t\u001a\u00020\n2\b\u0010\u0003\u001a\u0004\u0018\u00010\u00042\u0012\u0010\u0010\u001a\u000e\u0012\u0004\u0012\u00020\u0004\u0012\u0004\u0012\u00020\u00120\u0011J\u001a\u0010\u0013\u001a\u00020\u00122\b\u0010\t\u001a\u0004\u0018\u00010\n2\b\u0010\u0003\u001a\u0004\u0018\u00010\u0004R\u001c\u0010\u0003\u001a\u0004\u0018\u00010\u0004X\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\u0005\u0010\u0006\"\u0004\b\u0007\u0010\bR\u001a\u0010\t\u001a\u00020\nX\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\u000b\u0010\f\"\u0004\b\r\u0010\u000e\u00a8\u0006\u0014"}, d2 = {"Lcom/ekoapp/ekosdk/uikit/community/setting/EkoCommunitySettingEssentialViewModel;", "Lcom/ekoapp/ekosdk/uikit/base/EkoBaseViewModel;", "()V", "community", "Lcom/ekoapp/ekosdk/community/EkoCommunity;", "getCommunity", "()Lcom/ekoapp/ekosdk/community/EkoCommunity;", "setCommunity", "(Lcom/ekoapp/ekosdk/community/EkoCommunity;)V", "communityId", "", "getCommunityId", "()Ljava/lang/String;", "setCommunityId", "(Ljava/lang/String;)V", "Lio/reactivex/Completable;", "onCommunityLoaded", "Lkotlin/Function1;", "", "setupData", "community_debug"})
public final class EkoCommunitySettingEssentialViewModel extends com.ekoapp.ekosdk.uikit.base.EkoBaseViewModel {
    @org.jetbrains.annotations.NotNull()
    private java.lang.String communityId = "";
    @org.jetbrains.annotations.Nullable()
    private com.ekoapp.ekosdk.community.EkoCommunity community;
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String getCommunityId() {
        return null;
    }
    
    public final void setCommunityId(@org.jetbrains.annotations.NotNull()
    java.lang.String p0) {
    }
    
    @org.jetbrains.annotations.Nullable()
    public final com.ekoapp.ekosdk.community.EkoCommunity getCommunity() {
        return null;
    }
    
    public final void setCommunity(@org.jetbrains.annotations.Nullable()
    com.ekoapp.ekosdk.community.EkoCommunity p0) {
    }
    
    public final void setupData(@org.jetbrains.annotations.Nullable()
    java.lang.String communityId, @org.jetbrains.annotations.Nullable()
    com.ekoapp.ekosdk.community.EkoCommunity community) {
    }
    
    @org.jetbrains.annotations.NotNull()
    public final io.reactivex.Completable getCommunity(@org.jetbrains.annotations.NotNull()
    java.lang.String communityId, @org.jetbrains.annotations.Nullable()
    com.ekoapp.ekosdk.community.EkoCommunity community, @org.jetbrains.annotations.NotNull()
    kotlin.jvm.functions.Function1<? super com.ekoapp.ekosdk.community.EkoCommunity, kotlin.Unit> onCommunityLoaded) {
        return null;
    }
    
    public EkoCommunitySettingEssentialViewModel() {
        super();
    }
}