package com.ekoapp.ekosdk.uikit.community.newsfeed.adapter;

import java.lang.System;

@kotlin.Metadata(mv = {1, 1, 16}, bv = {1, 0, 3}, k = 1, d1 = {"\u0000J\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000b\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0002\b\u0002\n\u0002\u0010\b\n\u0002\b\u000f\b\u0016\u0018\u00002\u00020\u00012\b\u0012\u0004\u0012\u00020\u00030\u0002B\u0015\u0012\u0006\u0010\u0004\u001a\u00020\u0005\u0012\u0006\u0010\u0006\u001a\u00020\u0007\u00a2\u0006\u0002\u0010\bJ\u001a\u0010\u0012\u001a\u00020\u00132\b\u0010\u0014\u001a\u0004\u0018\u00010\u00032\u0006\u0010\u0015\u001a\u00020\u0016H\u0017J\u0015\u0010\u0017\u001a\u00020\u00132\u0006\u0010\u0018\u001a\u00020\nH\u0000\u00a2\u0006\u0002\b\u0019J\u0015\u0010\u001a\u001a\u00020\u00132\u0006\u0010\u001b\u001a\u00020\fH\u0000\u00a2\u0006\u0002\b\u001cJ\u0018\u0010\u001d\u001a\u00020\u00132\u0006\u0010\u0014\u001a\u00020\u00032\u0006\u0010\u0015\u001a\u00020\u0016H\u0002J\u0018\u0010\u001e\u001a\u00020\u00132\u0006\u0010\u0014\u001a\u00020\u00032\u0006\u0010\u0015\u001a\u00020\u0016H\u0002J\u0015\u0010\u001f\u001a\u00020\u00132\u0006\u0010 \u001a\u00020\u000eH\u0000\u00a2\u0006\u0002\b!J%\u0010\"\u001a\u00020\u00132\u0006\u0010\u0014\u001a\u00020\u00032\u0006\u0010\u0015\u001a\u00020\u00162\u0006\u0010#\u001a\u00020\u0010H\u0000\u00a2\u0006\u0002\b$R\u0010\u0010\t\u001a\u0004\u0018\u00010\nX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u0010\u0010\u000b\u001a\u0004\u0018\u00010\fX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u0010\u0010\r\u001a\u0004\u0018\u00010\u000eX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0006\u001a\u00020\u0007X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u000f\u001a\u00020\u0010X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0011\u001a\u00020\u0010X\u0082\u000e\u00a2\u0006\u0002\n\u0000\u00a8\u0006%"}, d2 = {"Lcom/ekoapp/ekosdk/uikit/community/newsfeed/adapter/EkoBasePostViewHolder;", "Landroidx/recyclerview/widget/RecyclerView$ViewHolder;", "Lcom/ekoapp/ekosdk/uikit/base/EkoBaseRecyclerViewPagedAdapter$Binder;", "Lcom/ekoapp/ekosdk/feed/EkoPost;", "itemView", "Landroid/view/View;", "timelineType", "Lcom/ekoapp/ekosdk/uikit/community/newsfeed/util/EkoTimelineType;", "(Landroid/view/View;Lcom/ekoapp/ekosdk/uikit/community/newsfeed/util/EkoTimelineType;)V", "ekoFooter", "Lcom/ekoapp/ekosdk/uikit/community/views/newsfeed/EkoPostItemFooter;", "ekoHeader", "Lcom/ekoapp/ekosdk/uikit/community/views/newsfeed/EkoPostItemHeader;", "itemActionLister", "Lcom/ekoapp/ekosdk/uikit/community/newsfeed/listener/IPostItemActionListener;", "useFooterLayout", "", "useHeaderLayout", "bind", "", "data", "position", "", "enableFooterLayout", "footer", "enableFooterLayout$community_debug", "enableHeaderLayout", "header", "enableHeaderLayout$community_debug", "setFooterLayout", "setHeaderLayout", "setItemActionListener", "listener", "setItemActionListener$community_debug", "setPostText", "showCompleteText", "setPostText$community_debug", "community_debug"})
public class EkoBasePostViewHolder extends androidx.recyclerview.widget.RecyclerView.ViewHolder implements com.ekoapp.ekosdk.uikit.base.EkoBaseRecyclerViewPagedAdapter.Binder<com.ekoapp.ekosdk.feed.EkoPost> {
    private com.ekoapp.ekosdk.uikit.community.newsfeed.listener.IPostItemActionListener itemActionLister;
    private boolean useHeaderLayout = false;
    private boolean useFooterLayout = false;
    private com.ekoapp.ekosdk.uikit.community.views.newsfeed.EkoPostItemHeader ekoHeader;
    private com.ekoapp.ekosdk.uikit.community.views.newsfeed.EkoPostItemFooter ekoFooter;
    private final com.ekoapp.ekosdk.uikit.community.newsfeed.util.EkoTimelineType timelineType = null;
    
    public final void enableHeaderLayout$community_debug(@org.jetbrains.annotations.NotNull()
    com.ekoapp.ekosdk.uikit.community.views.newsfeed.EkoPostItemHeader header) {
    }
    
    public final void enableFooterLayout$community_debug(@org.jetbrains.annotations.NotNull()
    com.ekoapp.ekosdk.uikit.community.views.newsfeed.EkoPostItemFooter footer) {
    }
    
    public final void setItemActionListener$community_debug(@org.jetbrains.annotations.NotNull()
    com.ekoapp.ekosdk.uikit.community.newsfeed.listener.IPostItemActionListener listener) {
    }
    
    @androidx.annotation.CallSuper()
    @java.lang.Override()
    public void bind(@org.jetbrains.annotations.Nullable()
    com.ekoapp.ekosdk.feed.EkoPost data, int position) {
    }
    
    public final void setPostText$community_debug(@org.jetbrains.annotations.NotNull()
    com.ekoapp.ekosdk.feed.EkoPost data, int position, boolean showCompleteText) {
    }
    
    private final void setHeaderLayout(com.ekoapp.ekosdk.feed.EkoPost data, int position) {
    }
    
    private final void setFooterLayout(com.ekoapp.ekosdk.feed.EkoPost data, int position) {
    }
    
    public EkoBasePostViewHolder(@org.jetbrains.annotations.NotNull()
    android.view.View itemView, @org.jetbrains.annotations.NotNull()
    com.ekoapp.ekosdk.uikit.community.newsfeed.util.EkoTimelineType timelineType) {
        super(null);
    }
}