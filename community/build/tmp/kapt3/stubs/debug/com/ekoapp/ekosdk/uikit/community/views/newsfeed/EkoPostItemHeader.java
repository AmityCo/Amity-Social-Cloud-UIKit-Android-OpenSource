package com.ekoapp.ekosdk.uikit.community.views.newsfeed;

import java.lang.System;

@kotlin.Metadata(mv = {1, 1, 16}, bv = {1, 0, 3}, k = 1, d1 = {"\u0000`\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\b\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000b\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\b\u0004\u0018\u00002\u00020\u0001B\u000f\b\u0016\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\u0002\u0010\u0004B\u0017\b\u0016\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0005\u001a\u00020\u0006\u00a2\u0006\u0002\u0010\u0007B\u001f\b\u0016\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0005\u001a\u00020\u0006\u0012\u0006\u0010\b\u001a\u00020\t\u00a2\u0006\u0002\u0010\nJ\u0018\u0010\u0013\u001a\u00020\u00142\u0006\u0010\u0015\u001a\u00020\u00162\u0006\u0010\u0017\u001a\u00020\u0016H\u0002J\u0006\u0010\u0018\u001a\u00020\u0019J\u0010\u0010\u001a\u001a\u00020\u00142\u0006\u0010\u001b\u001a\u00020\u001cH\u0002J\u0010\u0010\u001d\u001a\u00020\u00142\u0006\u0010\u001e\u001a\u00020\u001cH\u0002J\b\u0010\u001f\u001a\u00020\u0014H\u0002J\u0018\u0010 \u001a\u00020\u00142\u0006\u0010\u001b\u001a\u00020\u001c2\b\u0010!\u001a\u0004\u0018\u00010\"J\u000e\u0010#\u001a\u00020\u00142\u0006\u0010$\u001a\u00020\u000eJ\u000e\u0010%\u001a\u00020\u00142\u0006\u0010$\u001a\u00020\u0010J\u000e\u0010\u0011\u001a\u00020\u00142\u0006\u0010\u0011\u001a\u00020\u0012R\u000e\u0010\u000b\u001a\u00020\fX\u0082.\u00a2\u0006\u0002\n\u0000R\u0010\u0010\r\u001a\u0004\u0018\u00010\u000eX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u0010\u0010\u000f\u001a\u0004\u0018\u00010\u0010X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0011\u001a\u00020\u0012X\u0082\u000e\u00a2\u0006\u0002\n\u0000\u00a8\u0006&"}, d2 = {"Lcom/ekoapp/ekosdk/uikit/community/views/newsfeed/EkoPostItemHeader;", "Landroidx/constraintlayout/widget/ConstraintLayout;", "context", "Landroid/content/Context;", "(Landroid/content/Context;)V", "attrs", "Landroid/util/AttributeSet;", "(Landroid/content/Context;Landroid/util/AttributeSet;)V", "defStyleAttr", "", "(Landroid/content/Context;Landroid/util/AttributeSet;I)V", "mBinding", "Lcom/ekoapp/ekosdk/uikit/community/databinding/AmityItemPostHeaderBinding;", "newsFeedActionAvatarClickListener", "Lcom/ekoapp/ekosdk/uikit/community/newsfeed/listener/IPostActionAvatarClickListener;", "newsFeedActionCommunityClickListener", "Lcom/ekoapp/ekosdk/uikit/community/newsfeed/listener/IPostActionCommunityClickListener;", "showFeedAction", "", "getCommunityModerators", "", "communityId", "", "userIdPostCreator", "getFeedActionButton", "Landroid/widget/ImageButton;", "handleCommunityClick", "data", "Lcom/ekoapp/ekosdk/feed/EkoPost;", "handleUserClick", "feed", "init", "setFeed", "timelineType", "Lcom/ekoapp/ekosdk/uikit/community/newsfeed/util/EkoTimelineType;", "setNewsFeedActionAvatarClickListener", "listener", "setNewsFeedActionCommunityClickListener", "community_debug"})
public final class EkoPostItemHeader extends androidx.constraintlayout.widget.ConstraintLayout {
    private com.ekoapp.ekosdk.uikit.community.databinding.AmityItemPostHeaderBinding mBinding;
    private com.ekoapp.ekosdk.uikit.community.newsfeed.listener.IPostActionAvatarClickListener newsFeedActionAvatarClickListener;
    private com.ekoapp.ekosdk.uikit.community.newsfeed.listener.IPostActionCommunityClickListener newsFeedActionCommunityClickListener;
    private boolean showFeedAction = true;
    private java.util.HashMap _$_findViewCache;
    
    @org.jetbrains.annotations.NotNull()
    public final android.widget.ImageButton getFeedActionButton() {
        return null;
    }
    
    private final void init() {
    }
    
    public final void setNewsFeedActionAvatarClickListener(@org.jetbrains.annotations.NotNull()
    com.ekoapp.ekosdk.uikit.community.newsfeed.listener.IPostActionAvatarClickListener listener) {
    }
    
    public final void setNewsFeedActionCommunityClickListener(@org.jetbrains.annotations.NotNull()
    com.ekoapp.ekosdk.uikit.community.newsfeed.listener.IPostActionCommunityClickListener listener) {
    }
    
    public final void showFeedAction(boolean showFeedAction) {
    }
    
    public final void setFeed(@org.jetbrains.annotations.NotNull()
    com.ekoapp.ekosdk.feed.EkoPost data, @org.jetbrains.annotations.Nullable()
    com.ekoapp.ekosdk.uikit.community.newsfeed.util.EkoTimelineType timelineType) {
    }
    
    private final void getCommunityModerators(java.lang.String communityId, java.lang.String userIdPostCreator) {
    }
    
    private final void handleCommunityClick(com.ekoapp.ekosdk.feed.EkoPost data) {
    }
    
    private final void handleUserClick(com.ekoapp.ekosdk.feed.EkoPost feed) {
    }
    
    public EkoPostItemHeader(@org.jetbrains.annotations.NotNull()
    android.content.Context context) {
        super(null);
    }
    
    public EkoPostItemHeader(@org.jetbrains.annotations.NotNull()
    android.content.Context context, @org.jetbrains.annotations.NotNull()
    android.util.AttributeSet attrs) {
        super(null);
    }
    
    public EkoPostItemHeader(@org.jetbrains.annotations.NotNull()
    android.content.Context context, @org.jetbrains.annotations.NotNull()
    android.util.AttributeSet attrs, int defStyleAttr) {
        super(null);
    }
}