package com.ekoapp.ekosdk.uikit.community.newsfeed.adapter;

import java.lang.System;

@kotlin.Metadata(mv = {1, 1, 16}, bv = {1, 0, 3}, k = 1, d1 = {"\u0000>\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\b\n\u0002\b\u0003\n\u0002\u0010\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0003\u0018\u00002\b\u0012\u0004\u0012\u00020\u00020\u0001B#\u0012\f\u0010\u0003\u001a\b\u0012\u0004\u0012\u00020\u00050\u0004\u0012\u0006\u0010\u0006\u001a\u00020\u0007\u0012\u0006\u0010\b\u001a\u00020\t\u00a2\u0006\u0002\u0010\nJ\b\u0010\u000b\u001a\u00020\fH\u0016J\u0010\u0010\r\u001a\u00020\f2\u0006\u0010\u000e\u001a\u00020\fH\u0016J\u0018\u0010\u000f\u001a\u00020\u00102\u0006\u0010\u0011\u001a\u00020\u00022\u0006\u0010\u000e\u001a\u00020\fH\u0016J\u0018\u0010\u0012\u001a\u00020\u00022\u0006\u0010\u0013\u001a\u00020\u00142\u0006\u0010\u0015\u001a\u00020\fH\u0016J\u0014\u0010\u0016\u001a\u00020\u00102\f\u0010\u0003\u001a\b\u0012\u0004\u0012\u00020\u00050\u0004R\u000e\u0010\b\u001a\u00020\tX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0006\u001a\u00020\u0007X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0014\u0010\u0003\u001a\b\u0012\u0004\u0012\u00020\u00050\u0004X\u0082\u000e\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u0017"}, d2 = {"Lcom/ekoapp/ekosdk/uikit/community/newsfeed/adapter/EkoPostDetailAdapter;", "Landroidx/recyclerview/widget/RecyclerView$Adapter;", "Lcom/ekoapp/ekosdk/uikit/community/newsfeed/adapter/EkoBasePostViewHolder;", "postList", "", "Lcom/ekoapp/ekosdk/feed/EkoPost;", "imageClickListener", "Lcom/ekoapp/ekosdk/uikit/community/newsfeed/listener/IPostImageClickListener;", "fileItemClickListener", "Lcom/ekoapp/ekosdk/uikit/community/newsfeed/listener/IPostFileItemClickListener;", "(Ljava/util/List;Lcom/ekoapp/ekosdk/uikit/community/newsfeed/listener/IPostImageClickListener;Lcom/ekoapp/ekosdk/uikit/community/newsfeed/listener/IPostFileItemClickListener;)V", "getItemCount", "", "getItemViewType", "position", "onBindViewHolder", "", "holder", "onCreateViewHolder", "parent", "Landroid/view/ViewGroup;", "viewType", "submitList", "community_debug"})
public final class EkoPostDetailAdapter extends androidx.recyclerview.widget.RecyclerView.Adapter<com.ekoapp.ekosdk.uikit.community.newsfeed.adapter.EkoBasePostViewHolder> {
    private java.util.List<com.ekoapp.ekosdk.feed.EkoPost> postList;
    private final com.ekoapp.ekosdk.uikit.community.newsfeed.listener.IPostImageClickListener imageClickListener = null;
    private final com.ekoapp.ekosdk.uikit.community.newsfeed.listener.IPostFileItemClickListener fileItemClickListener = null;
    
    @org.jetbrains.annotations.NotNull()
    @java.lang.Override()
    public com.ekoapp.ekosdk.uikit.community.newsfeed.adapter.EkoBasePostViewHolder onCreateViewHolder(@org.jetbrains.annotations.NotNull()
    android.view.ViewGroup parent, int viewType) {
        return null;
    }
    
    @java.lang.Override()
    public void onBindViewHolder(@org.jetbrains.annotations.NotNull()
    com.ekoapp.ekosdk.uikit.community.newsfeed.adapter.EkoBasePostViewHolder holder, int position) {
    }
    
    @java.lang.Override()
    public int getItemCount() {
        return 0;
    }
    
    @java.lang.Override()
    public int getItemViewType(int position) {
        return 0;
    }
    
    public final void submitList(@org.jetbrains.annotations.NotNull()
    java.util.List<com.ekoapp.ekosdk.feed.EkoPost> postList) {
    }
    
    public EkoPostDetailAdapter(@org.jetbrains.annotations.NotNull()
    java.util.List<com.ekoapp.ekosdk.feed.EkoPost> postList, @org.jetbrains.annotations.NotNull()
    com.ekoapp.ekosdk.uikit.community.newsfeed.listener.IPostImageClickListener imageClickListener, @org.jetbrains.annotations.NotNull()
    com.ekoapp.ekosdk.uikit.community.newsfeed.listener.IPostFileItemClickListener fileItemClickListener) {
        super();
    }
}