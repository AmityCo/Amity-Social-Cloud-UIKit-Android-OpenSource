package com.ekoapp.ekosdk.uikit.community.newsfeed.adapter;

import java.lang.System;

@kotlin.Metadata(mv = {1, 1, 16}, bv = {1, 0, 3}, k = 1, d1 = {"\u0000`\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u000b\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\b\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\b\b\u0018\u00002\u00020\u0001B\u0015\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0005\u00a2\u0006\u0002\u0010\u0006J\u001a\u0010\u0015\u001a\u00020\u00162\b\u0010\u0017\u001a\u0004\u0018\u00010\u00182\u0006\u0010\u0019\u001a\u00020\u0014H\u0016J\u001e\u0010\u001a\u001a\u00020\u00162\u0006\u0010\u0017\u001a\u00020\u00182\f\u0010\u001b\u001a\b\u0012\u0004\u0012\u00020\u001d0\u001cH\u0002J\u0015\u0010\u001e\u001a\u00020\u00162\u0006\u0010\u001f\u001a\u00020\bH\u0000\u00a2\u0006\u0002\b J\u001c\u0010!\u001a\b\u0012\u0004\u0012\u00020\"0\u001c2\f\u0010#\u001a\b\u0012\u0004\u0012\u00020\u001d0\u001cH\u0002J\u0015\u0010$\u001a\u00020\u00162\u0006\u0010%\u001a\u00020\nH\u0000\u00a2\u0006\u0002\b&J\u0015\u0010\'\u001a\u00020\u00162\u0006\u0010%\u001a\u00020\u000eH\u0000\u00a2\u0006\u0002\b(J\u0015\u0010\u0012\u001a\u00020\u00162\u0006\u0010\u001f\u001a\u00020\bH\u0000\u00a2\u0006\u0002\b)R\u000e\u0010\u0007\u001a\u00020\bX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u0010\u0010\t\u001a\u0004\u0018\u00010\nX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u000b\u001a\u00020\fX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0010\u0010\r\u001a\u0004\u0018\u00010\u000eX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u0016\u0010\u000f\u001a\n \u0011*\u0004\u0018\u00010\u00100\u0010X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0012\u001a\u00020\bX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0013\u001a\u00020\u0014X\u0082\u0004\u00a2\u0006\u0002\n\u0000\u00a8\u0006*"}, d2 = {"Lcom/ekoapp/ekosdk/uikit/community/newsfeed/adapter/PostItemAttachmentViewHolder;", "Lcom/ekoapp/ekosdk/uikit/community/newsfeed/adapter/EkoBasePostViewHolder;", "itemView", "Landroid/view/View;", "timelineType", "Lcom/ekoapp/ekosdk/uikit/community/newsfeed/util/EkoTimelineType;", "(Landroid/view/View;Lcom/ekoapp/ekosdk/uikit/community/newsfeed/util/EkoTimelineType;)V", "collapsible", "", "fileItemClickListener", "Lcom/ekoapp/ekosdk/uikit/community/newsfeed/listener/IPostFileItemClickListener;", "itemDecor", "Lcom/ekoapp/ekosdk/uikit/base/SpacesItemDecoration;", "loadMoreFilesClickListener", "Lcom/ekoapp/ekosdk/uikit/community/newsfeed/adapter/EkoPostViewFileAdapter$ILoadMoreFilesClickListener;", "rvAttachment", "Landroidx/recyclerview/widget/RecyclerView;", "kotlin.jvm.PlatformType", "showCompleteText", "space", "", "bind", "", "data", "Lcom/ekoapp/ekosdk/feed/EkoPost;", "position", "initAttachments", "files", "", "Lcom/ekoapp/ekosdk/file/EkoFile;", "isCollapsible", "value", "isCollapsible$community_debug", "mapEkoFilesToFileAttachments", "Lcom/ekoapp/ekosdk/uikit/community/domain/model/FileAttachment;", "ekoFile", "setFileItemClickListener", "listener", "setFileItemClickListener$community_debug", "setLoadMoreFilesListener", "setLoadMoreFilesListener$community_debug", "showCompleteText$community_debug", "community_debug"})
public final class PostItemAttachmentViewHolder extends com.ekoapp.ekosdk.uikit.community.newsfeed.adapter.EkoBasePostViewHolder {
    private final androidx.recyclerview.widget.RecyclerView rvAttachment = null;
    private final int space = 0;
    private final com.ekoapp.ekosdk.uikit.base.SpacesItemDecoration itemDecor = null;
    private boolean collapsible = true;
    private com.ekoapp.ekosdk.uikit.community.newsfeed.adapter.EkoPostViewFileAdapter.ILoadMoreFilesClickListener loadMoreFilesClickListener;
    private com.ekoapp.ekosdk.uikit.community.newsfeed.listener.IPostFileItemClickListener fileItemClickListener;
    private boolean showCompleteText = false;
    
    public final void setLoadMoreFilesListener$community_debug(@org.jetbrains.annotations.NotNull()
    com.ekoapp.ekosdk.uikit.community.newsfeed.adapter.EkoPostViewFileAdapter.ILoadMoreFilesClickListener listener) {
    }
    
    public final void setFileItemClickListener$community_debug(@org.jetbrains.annotations.NotNull()
    com.ekoapp.ekosdk.uikit.community.newsfeed.listener.IPostFileItemClickListener listener) {
    }
    
    public final void showCompleteText$community_debug(boolean value) {
    }
    
    public final void isCollapsible$community_debug(boolean value) {
    }
    
    @java.lang.Override()
    public void bind(@org.jetbrains.annotations.Nullable()
    com.ekoapp.ekosdk.feed.EkoPost data, int position) {
    }
    
    private final void initAttachments(com.ekoapp.ekosdk.feed.EkoPost data, java.util.List<com.ekoapp.ekosdk.file.EkoFile> files) {
    }
    
    private final java.util.List<com.ekoapp.ekosdk.uikit.community.domain.model.FileAttachment> mapEkoFilesToFileAttachments(java.util.List<com.ekoapp.ekosdk.file.EkoFile> ekoFile) {
        return null;
    }
    
    public PostItemAttachmentViewHolder(@org.jetbrains.annotations.NotNull()
    android.view.View itemView, @org.jetbrains.annotations.NotNull()
    com.ekoapp.ekosdk.uikit.community.newsfeed.util.EkoTimelineType timelineType) {
        super(null, null);
    }
}