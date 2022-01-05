package com.ekoapp.ekosdk.uikit.feed.settings;

import java.lang.System;

@kotlin.Metadata(mv = {1, 1, 16}, bv = {1, 0, 3}, k = 1, d1 = {"\u0000@\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0010%\n\u0002\u0010\u000e\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\b\n\u0002\b\u0003\n\u0002\u0010\u0002\n\u0000\n\u0002\u0010 \n\u0000\b\u00c6\u0002\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002J\u0015\u0010\u0013\u001a\u00020\u00122\u0006\u0010\u0014\u001a\u00020\u0015H\u0000\u00a2\u0006\u0002\b\u0016J\u0015\u0010\u0013\u001a\u00020\u00122\u0006\u0010\u0017\u001a\u00020\u0011H\u0000\u00a2\u0006\u0002\b\u0016J\u0014\u0010\u0018\u001a\u00020\u00192\f\u0010\u001a\u001a\b\u0012\u0004\u0012\u00020\u00120\u001bR\u001a\u0010\u0003\u001a\u00020\u0004X\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\u0005\u0010\u0006\"\u0004\b\u0007\u0010\bR\u001a\u0010\t\u001a\u00020\nX\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\u000b\u0010\f\"\u0004\b\r\u0010\u000eR\u001a\u0010\u000f\u001a\u000e\u0012\u0004\u0012\u00020\u0011\u0012\u0004\u0012\u00020\u00120\u0010X\u0082\u000e\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u001c"}, d2 = {"Lcom/ekoapp/ekosdk/uikit/feed/settings/EkoFeedUISettings;", "", "()V", "postShareClickListener", "Lcom/ekoapp/ekosdk/uikit/feed/settings/IPostShareClickListener;", "getPostShareClickListener", "()Lcom/ekoapp/ekosdk/uikit/feed/settings/IPostShareClickListener;", "setPostShareClickListener", "(Lcom/ekoapp/ekosdk/uikit/feed/settings/IPostShareClickListener;)V", "postSharingSettings", "Lcom/ekoapp/ekosdk/uikit/feed/settings/EkoPostSharingSettings;", "getPostSharingSettings", "()Lcom/ekoapp/ekosdk/uikit/feed/settings/EkoPostSharingSettings;", "setPostSharingSettings", "(Lcom/ekoapp/ekosdk/uikit/feed/settings/EkoPostSharingSettings;)V", "postViewHolders", "", "", "Lcom/ekoapp/ekosdk/uikit/feed/settings/EkoIPostViewHolder;", "getViewHolder", "viewType", "", "getViewHolder$community_debug", "dataType", "registerPostViewHolders", "", "viewHolders", "", "community_debug"})
public final class EkoFeedUISettings {
    @org.jetbrains.annotations.NotNull()
    private static com.ekoapp.ekosdk.uikit.feed.settings.IPostShareClickListener postShareClickListener;
    @org.jetbrains.annotations.NotNull()
    private static com.ekoapp.ekosdk.uikit.feed.settings.EkoPostSharingSettings postSharingSettings;
    private static java.util.Map<java.lang.String, com.ekoapp.ekosdk.uikit.feed.settings.EkoIPostViewHolder> postViewHolders;
    public static final com.ekoapp.ekosdk.uikit.feed.settings.EkoFeedUISettings INSTANCE = null;
    
    @org.jetbrains.annotations.NotNull()
    public final com.ekoapp.ekosdk.uikit.feed.settings.IPostShareClickListener getPostShareClickListener() {
        return null;
    }
    
    public final void setPostShareClickListener(@org.jetbrains.annotations.NotNull()
    com.ekoapp.ekosdk.uikit.feed.settings.IPostShareClickListener p0) {
    }
    
    @org.jetbrains.annotations.NotNull()
    public final com.ekoapp.ekosdk.uikit.feed.settings.EkoPostSharingSettings getPostSharingSettings() {
        return null;
    }
    
    public final void setPostSharingSettings(@org.jetbrains.annotations.NotNull()
    com.ekoapp.ekosdk.uikit.feed.settings.EkoPostSharingSettings p0) {
    }
    
    public final void registerPostViewHolders(@org.jetbrains.annotations.NotNull()
    java.util.List<? extends com.ekoapp.ekosdk.uikit.feed.settings.EkoIPostViewHolder> viewHolders) {
    }
    
    @org.jetbrains.annotations.NotNull()
    public final com.ekoapp.ekosdk.uikit.feed.settings.EkoIPostViewHolder getViewHolder$community_debug(@org.jetbrains.annotations.NotNull()
    java.lang.String dataType) {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final com.ekoapp.ekosdk.uikit.feed.settings.EkoIPostViewHolder getViewHolder$community_debug(int viewType) {
        return null;
    }
    
    private EkoFeedUISettings() {
        super();
    }
}