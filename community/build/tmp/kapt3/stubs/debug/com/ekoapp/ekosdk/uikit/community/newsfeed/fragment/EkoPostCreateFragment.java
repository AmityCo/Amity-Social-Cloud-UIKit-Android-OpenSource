package com.ekoapp.ekosdk.uikit.community.newsfeed.fragment;

import java.lang.System;

@kotlin.Metadata(mv = {1, 1, 16}, bv = {1, 0, 3}, k = 1, d1 = {"\u0000\"\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0004\u0018\u00002\u00020\u0001:\u0001\rB\u0007\b\u0000\u00a2\u0006\u0002\u0010\u0002J\b\u0010\u0003\u001a\u00020\u0004H\u0002J\b\u0010\u0005\u001a\u00020\u0006H\u0016J\b\u0010\u0007\u001a\u00020\u0006H\u0002J\u0010\u0010\b\u001a\u00020\u00042\u0006\u0010\t\u001a\u00020\nH\u0002J\b\u0010\u000b\u001a\u00020\u0004H\u0016J\b\u0010\f\u001a\u00020\u0004H\u0016\u00a8\u0006\u000e"}, d2 = {"Lcom/ekoapp/ekosdk/uikit/community/newsfeed/fragment/EkoPostCreateFragment;", "Lcom/ekoapp/ekosdk/uikit/community/newsfeed/fragment/EkoBaseCreatePostFragment;", "()V", "createPost", "", "getPostMenuText", "", "getToolbarTitleForCreatePost", "handleCreatePostSuccessResponse", "post", "Lcom/ekoapp/ekosdk/feed/EkoPost;", "handlePostMenuItemClick", "setToolBarText", "Builder", "community_debug"})
public final class EkoPostCreateFragment extends com.ekoapp.ekosdk.uikit.community.newsfeed.fragment.EkoBaseCreatePostFragment {
    private java.util.HashMap _$_findViewCache;
    
    @java.lang.Override()
    public void handlePostMenuItemClick() {
    }
    
    @java.lang.Override()
    public void setToolBarText() {
    }
    
    @org.jetbrains.annotations.NotNull()
    @java.lang.Override()
    public java.lang.String getPostMenuText() {
        return null;
    }
    
    private final java.lang.String getToolbarTitleForCreatePost() {
        return null;
    }
    
    private final void createPost() {
    }
    
    private final void handleCreatePostSuccessResponse(com.ekoapp.ekosdk.feed.EkoPost post) {
    }
    
    public EkoPostCreateFragment() {
        super();
    }
    
    @kotlin.Metadata(mv = {1, 1, 16}, bv = {1, 0, 3}, k = 1, d1 = {"\u0000&\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\u0018\u00002\u00020\u0001B\u0005\u00a2\u0006\u0002\u0010\u0002J\u000e\u0010\u0007\u001a\u00020\b2\u0006\u0010\t\u001a\u00020\nJ\u0010\u0010\u000b\u001a\u00020\u00002\b\u0010\u0003\u001a\u0004\u0018\u00010\u0004J\u000e\u0010\u000b\u001a\u00020\u00002\u0006\u0010\u0005\u001a\u00020\u0006J\u0006\u0010\f\u001a\u00020\u0000R\u0010\u0010\u0003\u001a\u0004\u0018\u00010\u0004X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u0010\u0010\u0005\u001a\u0004\u0018\u00010\u0006X\u0082\u000e\u00a2\u0006\u0002\n\u0000\u00a8\u0006\r"}, d2 = {"Lcom/ekoapp/ekosdk/uikit/community/newsfeed/fragment/EkoPostCreateFragment$Builder;", "", "()V", "community", "Lcom/ekoapp/ekosdk/community/EkoCommunity;", "communityId", "", "build", "Lcom/ekoapp/ekosdk/uikit/community/newsfeed/fragment/EkoPostCreateFragment;", "activity", "Landroidx/appcompat/app/AppCompatActivity;", "onCommunityFeed", "onMyFeed", "community_debug"})
    public static final class Builder {
        private com.ekoapp.ekosdk.community.EkoCommunity community;
        private java.lang.String communityId;
        
        @org.jetbrains.annotations.NotNull()
        public final com.ekoapp.ekosdk.uikit.community.newsfeed.fragment.EkoPostCreateFragment build(@org.jetbrains.annotations.NotNull()
        androidx.appcompat.app.AppCompatActivity activity) {
            return null;
        }
        
        @org.jetbrains.annotations.NotNull()
        public final com.ekoapp.ekosdk.uikit.community.newsfeed.fragment.EkoPostCreateFragment.Builder onMyFeed() {
            return null;
        }
        
        @org.jetbrains.annotations.NotNull()
        public final com.ekoapp.ekosdk.uikit.community.newsfeed.fragment.EkoPostCreateFragment.Builder onCommunityFeed(@org.jetbrains.annotations.NotNull()
        java.lang.String communityId) {
            return null;
        }
        
        @org.jetbrains.annotations.NotNull()
        public final com.ekoapp.ekosdk.uikit.community.newsfeed.fragment.EkoPostCreateFragment.Builder onCommunityFeed(@org.jetbrains.annotations.Nullable()
        com.ekoapp.ekosdk.community.EkoCommunity community) {
            return null;
        }
        
        public Builder() {
            super();
        }
    }
}