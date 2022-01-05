package com.ekoapp.ekosdk.uikit.community.detailpage;

import java.lang.System;

@kotlin.Metadata(mv = {1, 1, 16}, bv = {1, 0, 3}, k = 1, d1 = {"\u0000^\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0010\u000e\n\u0002\b\r\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u000b\n\u0002\b\t\n\u0002\u0018\u0002\n\u0002\b\u000b\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u0002\n\u0002\b\u0003\u0018\u00002\u00020\u0001B\u0005\u00a2\u0006\u0002\u0010\u0002J\u0006\u0010=\u001a\u00020>J\f\u0010?\u001a\b\u0012\u0004\u0012\u00020\u00190@J\f\u0010A\u001a\b\u0012\u0004\u0012\u00020(0@J\u0006\u0010B\u001a\u00020>J\u0006\u0010C\u001a\u00020DJ\u0006\u0010E\u001a\u00020DJ\u000e\u0010F\u001a\u00020D2\u0006\u0010\u0018\u001a\u00020\u0019R\u001f\u0010\u0003\u001a\u0010\u0012\f\u0012\n \u0006*\u0004\u0018\u00010\u00050\u00050\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0007\u0010\bR\u001f\u0010\t\u001a\u0010\u0012\f\u0012\n \u0006*\u0004\u0018\u00010\u00050\u00050\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\b\n\u0010\bR\u001a\u0010\u000b\u001a\u00020\u0005X\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\f\u0010\r\"\u0004\b\u000e\u0010\u000fR\u001f\u0010\u0010\u001a\u0010\u0012\f\u0012\n \u0006*\u0004\u0018\u00010\u00050\u00050\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0011\u0010\bR\u001c\u0010\u0012\u001a\u0004\u0018\u00010\u0013X\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\u0014\u0010\u0015\"\u0004\b\u0016\u0010\u0017R\u001c\u0010\u0018\u001a\u0004\u0018\u00010\u0019X\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\u001a\u0010\u001b\"\u0004\b\u001c\u0010\u001dR\u001c\u0010\u001e\u001a\u0004\u0018\u00010\u001fX\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b \u0010!\"\u0004\b\"\u0010#R\u0011\u0010$\u001a\u00020%\u00a2\u0006\b\n\u0000\u001a\u0004\b$\u0010&R\u001a\u0010\'\u001a\u00020(X\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\'\u0010)\"\u0004\b*\u0010+R\u0011\u0010,\u001a\u00020%\u00a2\u0006\b\n\u0000\u001a\u0004\b,\u0010&R\u0011\u0010-\u001a\u00020%\u00a2\u0006\b\n\u0000\u001a\u0004\b-\u0010&R\u0011\u0010.\u001a\u00020%\u00a2\u0006\b\n\u0000\u001a\u0004\b.\u0010&R\u001f\u0010/\u001a\u0010\u0012\f\u0012\n \u0006*\u0004\u0018\u00010\u00050\u00050\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\b0\u0010\bR\u001c\u00101\u001a\u0004\u0018\u000102X\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b3\u00104\"\u0004\b5\u00106R\u001f\u00107\u001a\u0010\u0012\f\u0012\n \u0006*\u0004\u0018\u00010\u00050\u00050\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\b8\u0010\bR\u001f\u00109\u001a\u0010\u0012\f\u0012\n \u0006*\u0004\u0018\u00010\u00050\u00050\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\b:\u0010\bR\u0017\u0010;\u001a\b\u0012\u0004\u0012\u00020\u00050\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\b<\u0010\b\u00a8\u0006G"}, d2 = {"Lcom/ekoapp/ekosdk/uikit/community/detailpage/EkoCommunityDetailViewModel;", "Lcom/ekoapp/ekosdk/uikit/base/EkoBaseViewModel;", "()V", "avatarUrl", "Landroidx/databinding/ObservableField;", "", "kotlin.jvm.PlatformType", "getAvatarUrl", "()Landroidx/databinding/ObservableField;", "category", "getCategory", "communityID", "getCommunityID", "()Ljava/lang/String;", "setCommunityID", "(Ljava/lang/String;)V", "description", "getDescription", "editCommunityProfileClickListener", "Lcom/ekoapp/ekosdk/uikit/community/detailpage/listener/IEditCommunityProfileClickListener;", "getEditCommunityProfileClickListener", "()Lcom/ekoapp/ekosdk/uikit/community/detailpage/listener/IEditCommunityProfileClickListener;", "setEditCommunityProfileClickListener", "(Lcom/ekoapp/ekosdk/uikit/community/detailpage/listener/IEditCommunityProfileClickListener;)V", "ekoCommunity", "Lcom/ekoapp/ekosdk/community/EkoCommunity;", "getEkoCommunity", "()Lcom/ekoapp/ekosdk/community/EkoCommunity;", "setEkoCommunity", "(Lcom/ekoapp/ekosdk/community/EkoCommunity;)V", "feedFragmentDelegate", "Lcom/ekoapp/ekosdk/uikit/community/profile/listener/IFeedFragmentDelegate;", "getFeedFragmentDelegate", "()Lcom/ekoapp/ekosdk/uikit/community/profile/listener/IFeedFragmentDelegate;", "setFeedFragmentDelegate", "(Lcom/ekoapp/ekosdk/uikit/community/profile/listener/IFeedFragmentDelegate;)V", "isMember", "Landroidx/databinding/ObservableBoolean;", "()Landroidx/databinding/ObservableBoolean;", "isMessageVisible", "", "()Z", "setMessageVisible", "(Z)V", "isModerator", "isOfficial", "isPublic", "members", "getMembers", "messageClickListener", "Lcom/ekoapp/ekosdk/uikit/community/detailpage/listener/IMessageClickListener;", "getMessageClickListener", "()Lcom/ekoapp/ekosdk/uikit/community/detailpage/listener/IMessageClickListener;", "setMessageClickListener", "(Lcom/ekoapp/ekosdk/uikit/community/detailpage/listener/IMessageClickListener;)V", "name", "getName", "posts", "getPosts", "userId", "getUserId", "assignRole", "Lio/reactivex/Completable;", "getCommunityDetail", "Lio/reactivex/Flowable;", "isModeratorPermission", "joinCommunity", "onEditProfileButtonClick", "", "onMessageButtonClick", "setCommunity", "community_debug"})
public final class EkoCommunityDetailViewModel extends com.ekoapp.ekosdk.uikit.base.EkoBaseViewModel {
    @org.jetbrains.annotations.NotNull()
    private java.lang.String communityID = "";
    private boolean isMessageVisible = false;
    @org.jetbrains.annotations.Nullable()
    private com.ekoapp.ekosdk.community.EkoCommunity ekoCommunity;
    @org.jetbrains.annotations.NotNull()
    private final androidx.databinding.ObservableField<java.lang.String> avatarUrl = null;
    @org.jetbrains.annotations.NotNull()
    private final androidx.databinding.ObservableField<java.lang.String> name = null;
    @org.jetbrains.annotations.NotNull()
    private final androidx.databinding.ObservableField<java.lang.String> category = null;
    @org.jetbrains.annotations.NotNull()
    private final androidx.databinding.ObservableField<java.lang.String> posts = null;
    @org.jetbrains.annotations.NotNull()
    private final androidx.databinding.ObservableField<java.lang.String> members = null;
    @org.jetbrains.annotations.NotNull()
    private final androidx.databinding.ObservableField<java.lang.String> description = null;
    @org.jetbrains.annotations.NotNull()
    private final androidx.databinding.ObservableBoolean isPublic = null;
    @org.jetbrains.annotations.NotNull()
    private final androidx.databinding.ObservableBoolean isMember = null;
    @org.jetbrains.annotations.NotNull()
    private final androidx.databinding.ObservableBoolean isOfficial = null;
    @org.jetbrains.annotations.NotNull()
    private final androidx.databinding.ObservableBoolean isModerator = null;
    @org.jetbrains.annotations.Nullable()
    private com.ekoapp.ekosdk.uikit.community.profile.listener.IFeedFragmentDelegate feedFragmentDelegate;
    @org.jetbrains.annotations.Nullable()
    private com.ekoapp.ekosdk.uikit.community.detailpage.listener.IMessageClickListener messageClickListener;
    @org.jetbrains.annotations.Nullable()
    private com.ekoapp.ekosdk.uikit.community.detailpage.listener.IEditCommunityProfileClickListener editCommunityProfileClickListener;
    @org.jetbrains.annotations.NotNull()
    private final androidx.databinding.ObservableField<java.lang.String> userId = null;
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String getCommunityID() {
        return null;
    }
    
    public final void setCommunityID(@org.jetbrains.annotations.NotNull()
    java.lang.String p0) {
    }
    
    public final boolean isMessageVisible() {
        return false;
    }
    
    public final void setMessageVisible(boolean p0) {
    }
    
    @org.jetbrains.annotations.Nullable()
    public final com.ekoapp.ekosdk.community.EkoCommunity getEkoCommunity() {
        return null;
    }
    
    public final void setEkoCommunity(@org.jetbrains.annotations.Nullable()
    com.ekoapp.ekosdk.community.EkoCommunity p0) {
    }
    
    @org.jetbrains.annotations.NotNull()
    public final androidx.databinding.ObservableField<java.lang.String> getAvatarUrl() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final androidx.databinding.ObservableField<java.lang.String> getName() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final androidx.databinding.ObservableField<java.lang.String> getCategory() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final androidx.databinding.ObservableField<java.lang.String> getPosts() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final androidx.databinding.ObservableField<java.lang.String> getMembers() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final androidx.databinding.ObservableField<java.lang.String> getDescription() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final androidx.databinding.ObservableBoolean isPublic() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final androidx.databinding.ObservableBoolean isMember() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final androidx.databinding.ObservableBoolean isOfficial() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final androidx.databinding.ObservableBoolean isModerator() {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final com.ekoapp.ekosdk.uikit.community.profile.listener.IFeedFragmentDelegate getFeedFragmentDelegate() {
        return null;
    }
    
    public final void setFeedFragmentDelegate(@org.jetbrains.annotations.Nullable()
    com.ekoapp.ekosdk.uikit.community.profile.listener.IFeedFragmentDelegate p0) {
    }
    
    @org.jetbrains.annotations.Nullable()
    public final com.ekoapp.ekosdk.uikit.community.detailpage.listener.IMessageClickListener getMessageClickListener() {
        return null;
    }
    
    public final void setMessageClickListener(@org.jetbrains.annotations.Nullable()
    com.ekoapp.ekosdk.uikit.community.detailpage.listener.IMessageClickListener p0) {
    }
    
    @org.jetbrains.annotations.Nullable()
    public final com.ekoapp.ekosdk.uikit.community.detailpage.listener.IEditCommunityProfileClickListener getEditCommunityProfileClickListener() {
        return null;
    }
    
    public final void setEditCommunityProfileClickListener(@org.jetbrains.annotations.Nullable()
    com.ekoapp.ekosdk.uikit.community.detailpage.listener.IEditCommunityProfileClickListener p0) {
    }
    
    @org.jetbrains.annotations.NotNull()
    public final androidx.databinding.ObservableField<java.lang.String> getUserId() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final io.reactivex.Flowable<java.lang.Boolean> isModeratorPermission() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final io.reactivex.Flowable<com.ekoapp.ekosdk.community.EkoCommunity> getCommunityDetail() {
        return null;
    }
    
    public final void setCommunity(@org.jetbrains.annotations.NotNull()
    com.ekoapp.ekosdk.community.EkoCommunity ekoCommunity) {
    }
    
    @org.jetbrains.annotations.NotNull()
    public final io.reactivex.Completable joinCommunity() {
        return null;
    }
    
    public final void onMessageButtonClick() {
    }
    
    public final void onEditProfileButtonClick() {
    }
    
    @org.jetbrains.annotations.NotNull()
    public final io.reactivex.Completable assignRole() {
        return null;
    }
    
    public EkoCommunityDetailViewModel() {
        super();
    }
}