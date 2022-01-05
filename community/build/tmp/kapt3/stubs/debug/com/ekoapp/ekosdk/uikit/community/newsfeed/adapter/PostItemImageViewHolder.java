package com.ekoapp.ekosdk.uikit.community.newsfeed.adapter;

import java.lang.System;

@kotlin.Metadata(mv = {1, 1, 16}, bv = {1, 0, 3}, k = 1, d1 = {"\u0000V\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000b\n\u0000\n\u0002\u0010\b\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\n\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0000\u0018\u00002\u00020\u0001B\u0015\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0005\u00a2\u0006\u0002\u0010\u0006J\u001a\u0010\u0014\u001a\u00020\u00152\b\u0010\u0016\u001a\u0004\u0018\u00010\u00172\u0006\u0010\u0018\u001a\u00020\u0013H\u0016J\b\u0010\u0019\u001a\u00020\u0015H\u0002J\u0015\u0010\u001a\u001a\u00020\u00152\u0006\u0010\u001b\u001a\u00020\nH\u0000\u00a2\u0006\u0002\b\u001cJ\u0015\u0010\u001d\u001a\u00020\u00152\u0006\u0010\u001e\u001a\u00020\u0011H\u0000\u00a2\u0006\u0002\b\u001fJ\u0016\u0010 \u001a\u00020\u00152\f\u0010!\u001a\b\u0012\u0004\u0012\u00020#0\"H\u0002R\u0010\u0010\u0007\u001a\u0004\u0018\u00010\bX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u0010\u0010\t\u001a\u0004\u0018\u00010\nX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u0016\u0010\u000b\u001a\n \r*\u0004\u0018\u00010\f0\fX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u000e\u001a\u00020\u000fX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0010\u001a\u00020\u0011X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0012\u001a\u00020\u0013X\u0082\u0004\u00a2\u0006\u0002\n\u0000\u00a8\u0006$"}, d2 = {"Lcom/ekoapp/ekosdk/uikit/community/newsfeed/adapter/PostItemImageViewHolder;", "Lcom/ekoapp/ekosdk/uikit/community/newsfeed/adapter/EkoBasePostViewHolder;", "itemView", "Landroid/view/View;", "timelineType", "Lcom/ekoapp/ekosdk/uikit/community/newsfeed/util/EkoTimelineType;", "(Landroid/view/View;Lcom/ekoapp/ekosdk/uikit/community/newsfeed/util/EkoTimelineType;)V", "adapter", "Lcom/ekoapp/ekosdk/uikit/community/newsfeed/adapter/PostImageChildrenAdapter;", "imageClickListener", "Lcom/ekoapp/ekosdk/uikit/community/newsfeed/listener/IPostImageClickListener;", "imageRecyclerView", "Landroidx/recyclerview/widget/RecyclerView;", "kotlin.jvm.PlatformType", "itemDecor", "Lcom/ekoapp/ekosdk/uikit/base/SpacesItemDecoration;", "showCompleteText", "", "space", "", "bind", "", "data", "Lcom/ekoapp/ekosdk/feed/EkoPost;", "position", "initAdapter", "setImageClickListener", "listener", "setImageClickListener$community_debug", "showCompleteFeedText", "value", "showCompleteFeedText$community_debug", "submitImages", "images", "", "Lcom/ekoapp/ekosdk/file/EkoImage;", "community_debug"})
public final class PostItemImageViewHolder extends com.ekoapp.ekosdk.uikit.community.newsfeed.adapter.EkoBasePostViewHolder {
    private com.ekoapp.ekosdk.uikit.community.newsfeed.listener.IPostImageClickListener imageClickListener;
    private final androidx.recyclerview.widget.RecyclerView imageRecyclerView = null;
    private final int space = 0;
    private final com.ekoapp.ekosdk.uikit.base.SpacesItemDecoration itemDecor = null;
    private com.ekoapp.ekosdk.uikit.community.newsfeed.adapter.PostImageChildrenAdapter adapter;
    private boolean showCompleteText = false;
    
    public final void setImageClickListener$community_debug(@org.jetbrains.annotations.NotNull()
    com.ekoapp.ekosdk.uikit.community.newsfeed.listener.IPostImageClickListener listener) {
    }
    
    public final void showCompleteFeedText$community_debug(boolean value) {
    }
    
    @java.lang.Override()
    public void bind(@org.jetbrains.annotations.Nullable()
    com.ekoapp.ekosdk.feed.EkoPost data, int position) {
    }
    
    private final void initAdapter() {
    }
    
    private final void submitImages(java.util.List<com.ekoapp.ekosdk.file.EkoImage> images) {
    }
    
    public PostItemImageViewHolder(@org.jetbrains.annotations.NotNull()
    android.view.View itemView, @org.jetbrains.annotations.NotNull()
    com.ekoapp.ekosdk.uikit.community.newsfeed.util.EkoTimelineType timelineType) {
        super(null, null);
    }
}