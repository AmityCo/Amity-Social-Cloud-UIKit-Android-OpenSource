package com.ekoapp.ekosdk.uikit.community.domain.repository;

import java.lang.System;

@kotlin.Metadata(mv = {1, 1, 16}, bv = {1, 0, 3}, k = 1, d1 = {"\u0000$\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\u0018\u0000 \u000b2\u00020\u0001:\u0001\u000bB\u0005\u00a2\u0006\u0002\u0010\u0002J\u000e\u0010\u0003\u001a\b\u0012\u0004\u0012\u00020\u00050\u0004H\u0016J\u0016\u0010\u0003\u001a\b\u0012\u0004\u0012\u00020\u00050\u00042\u0006\u0010\u0006\u001a\u00020\u0007H\u0016J\u000e\u0010\b\u001a\b\u0012\u0004\u0012\u00020\t0\u0004H\u0016J\u000e\u0010\n\u001a\b\u0012\u0004\u0012\u00020\t0\u0004H\u0016\u00a8\u0006\f"}, d2 = {"Lcom/ekoapp/ekosdk/uikit/community/domain/repository/EkoChannelRepository;", "Lcom/ekoapp/ekosdk/uikit/community/domain/repository/IChannelRepository;", "()V", "getChannelCategory", "", "Lcom/ekoapp/ekosdk/uikit/community/domain/model/ChannelCategory;", "parentCategory", "", "getRecommendedChannels", "Lcom/ekoapp/ekosdk/uikit/community/domain/model/Channel;", "getTrendingChannels", "Companion", "community_debug"})
public final class EkoChannelRepository implements com.ekoapp.ekosdk.uikit.community.domain.repository.IChannelRepository {
    private static boolean isTestMode = false;
    private static boolean showProfileAvatar = false;
    private static boolean showTimeLineFeed = false;
    private static boolean showUserProfileFeed = false;
    @org.jetbrains.annotations.NotNull()
    private static final androidx.lifecycle.MutableLiveData<androidx.paging.PagedList<com.ekoapp.ekosdk.uikit.community.domain.model.NewsFeed>> liveMessageData = null;
    private static boolean edited = false;
    private static final java.util.List<com.ekoapp.ekosdk.uikit.community.domain.model.Comment> ekoComments = null;
    @org.jetbrains.annotations.NotNull()
    private static final androidx.lifecycle.MutableLiveData<java.util.List<com.ekoapp.ekosdk.uikit.community.domain.model.Comment>> liveCommnetData = null;
    public static final com.ekoapp.ekosdk.uikit.community.domain.repository.EkoChannelRepository.Companion Companion = null;
    
    @org.jetbrains.annotations.NotNull()
    @java.lang.Override()
    public java.util.List<com.ekoapp.ekosdk.uikit.community.domain.model.Channel> getRecommendedChannels() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    @java.lang.Override()
    public java.util.List<com.ekoapp.ekosdk.uikit.community.domain.model.Channel> getTrendingChannels() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    @java.lang.Override()
    public java.util.List<com.ekoapp.ekosdk.uikit.community.domain.model.ChannelCategory> getChannelCategory() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    @java.lang.Override()
    public java.util.List<com.ekoapp.ekosdk.uikit.community.domain.model.ChannelCategory> getChannelCategory(@org.jetbrains.annotations.NotNull()
    java.lang.String parentCategory) {
        return null;
    }
    
    public EkoChannelRepository() {
        super();
    }
    
    @kotlin.Metadata(mv = {1, 1, 16}, bv = {1, 0, 3}, k = 1, d1 = {"\u0000V\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\u000b\n\u0002\b\u0005\n\u0002\u0010!\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\u0010 \n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u000b\n\u0002\u0010\u0002\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0006\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\b\u0086\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002J\u000e\u0010 \u001a\u00020!2\u0006\u0010\"\u001a\u00020#J\u000e\u0010$\u001a\u00020!2\u0006\u0010\"\u001a\u00020\u000bJ\u0010\u0010%\u001a\u0004\u0018\u00010\u000b2\u0006\u0010&\u001a\u00020#J\u0012\u0010\'\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u000b0\u00100\u000fJ\u000e\u0010(\u001a\b\u0012\u0004\u0012\u00020\u000b0\nH\u0002J\u000e\u0010)\u001a\n\u0012\u0004\u0012\u00020*\u0018\u00010\u0014J\f\u0010+\u001a\b\u0012\u0004\u0012\u00020\u000b0\u0010J\u0006\u0010,\u001a\u00020-J\u0006\u0010.\u001a\u00020!J\u000e\u0010/\u001a\u00020!2\u0006\u0010\"\u001a\u00020\u000bR\u001a\u0010\u0003\u001a\u00020\u0004X\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\u0005\u0010\u0006\"\u0004\b\u0007\u0010\bR\u0014\u0010\t\u001a\b\u0012\u0004\u0012\u00020\u000b0\nX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u001a\u0010\f\u001a\u00020\u0004X\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\f\u0010\u0006\"\u0004\b\r\u0010\bR\u001d\u0010\u000e\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u000b0\u00100\u000f\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0011\u0010\u0012R\u001d\u0010\u0013\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u00150\u00140\u000f\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0016\u0010\u0012R\u001a\u0010\u0017\u001a\u00020\u0004X\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\u0018\u0010\u0006\"\u0004\b\u0019\u0010\bR\u001a\u0010\u001a\u001a\u00020\u0004X\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\u001b\u0010\u0006\"\u0004\b\u001c\u0010\bR\u001a\u0010\u001d\u001a\u00020\u0004X\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\u001e\u0010\u0006\"\u0004\b\u001f\u0010\b\u00a8\u00060"}, d2 = {"Lcom/ekoapp/ekosdk/uikit/community/domain/repository/EkoChannelRepository$Companion;", "", "()V", "edited", "", "getEdited", "()Z", "setEdited", "(Z)V", "ekoComments", "", "Lcom/ekoapp/ekosdk/uikit/community/domain/model/Comment;", "isTestMode", "setTestMode", "liveCommnetData", "Landroidx/lifecycle/MutableLiveData;", "", "getLiveCommnetData", "()Landroidx/lifecycle/MutableLiveData;", "liveMessageData", "Landroidx/paging/PagedList;", "Lcom/ekoapp/ekosdk/uikit/community/domain/model/NewsFeed;", "getLiveMessageData", "showProfileAvatar", "getShowProfileAvatar", "setShowProfileAvatar", "showTimeLineFeed", "getShowTimeLineFeed", "setShowTimeLineFeed", "showUserProfileFeed", "getShowUserProfileFeed", "setShowUserProfileFeed", "addComment", "", "comment", "", "deleteComment", "getComment", "commentId", "getComments", "getDummyComment", "getEmptyFeed", "Lcom/ekoapp/ekosdk/feed/EkoPost;", "getTopTwoComments", "getUser", "Lcom/ekoapp/ekosdk/uikit/community/domain/model/User;", "sortComments", "updateComment", "community_debug"})
    public static final class Companion {
        
        public final boolean isTestMode() {
            return false;
        }
        
        public final void setTestMode(boolean p0) {
        }
        
        public final boolean getShowProfileAvatar() {
            return false;
        }
        
        public final void setShowProfileAvatar(boolean p0) {
        }
        
        public final boolean getShowTimeLineFeed() {
            return false;
        }
        
        public final void setShowTimeLineFeed(boolean p0) {
        }
        
        public final boolean getShowUserProfileFeed() {
            return false;
        }
        
        public final void setShowUserProfileFeed(boolean p0) {
        }
        
        @org.jetbrains.annotations.NotNull()
        public final androidx.lifecycle.MutableLiveData<androidx.paging.PagedList<com.ekoapp.ekosdk.uikit.community.domain.model.NewsFeed>> getLiveMessageData() {
            return null;
        }
        
        public final boolean getEdited() {
            return false;
        }
        
        public final void setEdited(boolean p0) {
        }
        
        @org.jetbrains.annotations.NotNull()
        public final androidx.lifecycle.MutableLiveData<java.util.List<com.ekoapp.ekosdk.uikit.community.domain.model.Comment>> getLiveCommnetData() {
            return null;
        }
        
        private final java.util.List<com.ekoapp.ekosdk.uikit.community.domain.model.Comment> getDummyComment() {
            return null;
        }
        
        public final void sortComments() {
        }
        
        public final void addComment(@org.jetbrains.annotations.NotNull()
        java.lang.String comment) {
        }
        
        @org.jetbrains.annotations.NotNull()
        public final androidx.lifecycle.MutableLiveData<java.util.List<com.ekoapp.ekosdk.uikit.community.domain.model.Comment>> getComments() {
            return null;
        }
        
        @org.jetbrains.annotations.NotNull()
        public final java.util.List<com.ekoapp.ekosdk.uikit.community.domain.model.Comment> getTopTwoComments() {
            return null;
        }
        
        public final void deleteComment(@org.jetbrains.annotations.NotNull()
        com.ekoapp.ekosdk.uikit.community.domain.model.Comment comment) {
        }
        
        @org.jetbrains.annotations.Nullable()
        public final androidx.paging.PagedList<com.ekoapp.ekosdk.feed.EkoPost> getEmptyFeed() {
            return null;
        }
        
        @org.jetbrains.annotations.NotNull()
        public final com.ekoapp.ekosdk.uikit.community.domain.model.User getUser() {
            return null;
        }
        
        @org.jetbrains.annotations.Nullable()
        public final com.ekoapp.ekosdk.uikit.community.domain.model.Comment getComment(@org.jetbrains.annotations.NotNull()
        java.lang.String commentId) {
            return null;
        }
        
        public final void updateComment(@org.jetbrains.annotations.NotNull()
        com.ekoapp.ekosdk.uikit.community.domain.model.Comment comment) {
        }
        
        private Companion() {
            super();
        }
    }
}