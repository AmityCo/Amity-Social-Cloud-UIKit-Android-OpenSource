package com.ekoapp.ekosdk.uikit.community.utils;

import java.lang.System;

@kotlin.Metadata(mv = {1, 1, 16}, bv = {1, 0, 3}, k = 1, d1 = {"\u0000\f\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0003\u0018\u0000 \u00032\u00020\u0001:\u0001\u0003B\u0005\u00a2\u0006\u0002\u0010\u0002\u00a8\u0006\u0004"}, d2 = {"Lcom/ekoapp/ekosdk/uikit/community/utils/EkoCommunityNavigation;", "", "()V", "Companion", "community_debug"})
public final class EkoCommunityNavigation {
    public static final com.ekoapp.ekosdk.uikit.community.utils.EkoCommunityNavigation.Companion Companion = null;
    
    public EkoCommunityNavigation() {
        super();
    }
    
    @kotlin.Metadata(mv = {1, 1, 16}, bv = {1, 0, 3}, k = 1, d1 = {"\u0000N\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0003\b\u0086\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002J\u0016\u0010\u0003\u001a\u00020\u00042\u0006\u0010\u0005\u001a\u00020\u00062\u0006\u0010\u0007\u001a\u00020\bJ\u000e\u0010\t\u001a\u00020\u00042\u0006\u0010\u0005\u001a\u00020\u0006J\u0016\u0010\t\u001a\u00020\u00042\u0006\u0010\u0005\u001a\u00020\u00062\u0006\u0010\u0007\u001a\u00020\bJ\u000e\u0010\n\u001a\u00020\u00042\u0006\u0010\u0005\u001a\u00020\u0006J\u0016\u0010\u000b\u001a\u00020\u00042\u0006\u0010\u0005\u001a\u00020\u00062\u0006\u0010\f\u001a\u00020\rJ\u000e\u0010\u000e\u001a\u00020\u00042\u0006\u0010\u0005\u001a\u00020\u0006J$\u0010\u000f\u001a\u00020\u00042\u0006\u0010\u0005\u001a\u00020\u00062\f\u0010\u0010\u001a\b\u0012\u0004\u0012\u00020\u00120\u00112\u0006\u0010\u0013\u001a\u00020\u0014J\u000e\u0010\u0015\u001a\u00020\u00042\u0006\u0010\u0005\u001a\u00020\u0006J&\u0010\u0016\u001a\u00020\u00042\u0006\u0010\u0005\u001a\u00020\u00062\u0006\u0010\f\u001a\u00020\r2\u0006\u0010\u0017\u001a\u00020\u00182\u0006\u0010\u0019\u001a\u00020\u001aJ\u001e\u0010\u0016\u001a\u00020\u00042\u0006\u0010\u0005\u001a\u00020\u00062\u0006\u0010\u001b\u001a\u00020\u001c2\u0006\u0010\u0019\u001a\u00020\u001aJ\u0016\u0010\u001d\u001a\u00020\u00042\u0006\u0010\u0005\u001a\u00020\u00062\u0006\u0010\u001e\u001a\u00020\u001c\u00a8\u0006\u001f"}, d2 = {"Lcom/ekoapp/ekosdk/uikit/community/utils/EkoCommunityNavigation$Companion;", "", "()V", "navigateToCommunityDetails", "", "context", "Landroid/content/Context;", "community", "Lcom/ekoapp/ekosdk/community/EkoCommunity;", "navigateToCreatePost", "navigateToCreatePostRoleSelection", "navigateToEditPost", "post", "Lcom/ekoapp/ekosdk/feed/EkoPost;", "navigateToEditProfile", "navigateToImagePreview", "images", "", "Lcom/ekoapp/ekosdk/file/EkoImage;", "position", "", "navigateToMyTimeline", "navigateToPostDetails", "comment", "Lcom/ekoapp/ekosdk/comment/EkoComment;", "timelineType", "Lcom/ekoapp/ekosdk/uikit/community/newsfeed/util/EkoTimelineType;", "postId", "", "navigateToUserProfile", "userId", "community_debug"})
    public static final class Companion {
        
        public final void navigateToMyTimeline(@org.jetbrains.annotations.NotNull()
        android.content.Context context) {
        }
        
        public final void navigateToCreatePost(@org.jetbrains.annotations.NotNull()
        android.content.Context context) {
        }
        
        public final void navigateToCreatePost(@org.jetbrains.annotations.NotNull()
        android.content.Context context, @org.jetbrains.annotations.NotNull()
        com.ekoapp.ekosdk.community.EkoCommunity community) {
        }
        
        public final void navigateToEditPost(@org.jetbrains.annotations.NotNull()
        android.content.Context context, @org.jetbrains.annotations.NotNull()
        com.ekoapp.ekosdk.feed.EkoPost post) {
        }
        
        public final void navigateToCreatePostRoleSelection(@org.jetbrains.annotations.NotNull()
        android.content.Context context) {
        }
        
        public final void navigateToPostDetails(@org.jetbrains.annotations.NotNull()
        android.content.Context context, @org.jetbrains.annotations.NotNull()
        java.lang.String postId, @org.jetbrains.annotations.NotNull()
        com.ekoapp.ekosdk.uikit.community.newsfeed.util.EkoTimelineType timelineType) {
        }
        
        public final void navigateToPostDetails(@org.jetbrains.annotations.NotNull()
        android.content.Context context, @org.jetbrains.annotations.NotNull()
        com.ekoapp.ekosdk.feed.EkoPost post, @org.jetbrains.annotations.NotNull()
        com.ekoapp.ekosdk.comment.EkoComment comment, @org.jetbrains.annotations.NotNull()
        com.ekoapp.ekosdk.uikit.community.newsfeed.util.EkoTimelineType timelineType) {
        }
        
        public final void navigateToImagePreview(@org.jetbrains.annotations.NotNull()
        android.content.Context context, @org.jetbrains.annotations.NotNull()
        java.util.List<com.ekoapp.ekosdk.file.EkoImage> images, int position) {
        }
        
        public final void navigateToUserProfile(@org.jetbrains.annotations.NotNull()
        android.content.Context context, @org.jetbrains.annotations.NotNull()
        java.lang.String userId) {
        }
        
        public final void navigateToEditProfile(@org.jetbrains.annotations.NotNull()
        android.content.Context context) {
        }
        
        public final void navigateToCommunityDetails(@org.jetbrains.annotations.NotNull()
        android.content.Context context, @org.jetbrains.annotations.NotNull()
        com.ekoapp.ekosdk.community.EkoCommunity community) {
        }
        
        private Companion() {
            super();
        }
    }
}