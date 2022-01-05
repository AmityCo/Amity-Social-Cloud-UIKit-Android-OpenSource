package com.ekoapp.ekosdk.uikit.feed.settings;

import java.lang.System;

@kotlin.Metadata(mv = {1, 1, 16}, bv = {1, 0, 3}, k = 1, d1 = {"\u0000\u001e\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\bf\u0018\u00002\u00020\u0001J\u0018\u0010\u0002\u001a\u00020\u00032\u0006\u0010\u0004\u001a\u00020\u00052\u0006\u0010\u0006\u001a\u00020\u0007H\u0016J\u0018\u0010\b\u001a\u00020\u00032\u0006\u0010\u0004\u001a\u00020\u00052\u0006\u0010\u0006\u001a\u00020\u0007H\u0016J\u0018\u0010\t\u001a\u00020\u00032\u0006\u0010\u0004\u001a\u00020\u00052\u0006\u0010\u0006\u001a\u00020\u0007H\u0016\u00a8\u0006\n"}, d2 = {"Lcom/ekoapp/ekosdk/uikit/feed/settings/IPostShareClickListener;", "", "shareToExternal", "", "context", "Landroid/content/Context;", "post", "Lcom/ekoapp/ekosdk/feed/EkoPost;", "shareToGroup", "shareToMyTimeline", "community_debug"})
public abstract interface IPostShareClickListener {
    
    public abstract void shareToMyTimeline(@org.jetbrains.annotations.NotNull()
    android.content.Context context, @org.jetbrains.annotations.NotNull()
    com.ekoapp.ekosdk.feed.EkoPost post);
    
    public abstract void shareToGroup(@org.jetbrains.annotations.NotNull()
    android.content.Context context, @org.jetbrains.annotations.NotNull()
    com.ekoapp.ekosdk.feed.EkoPost post);
    
    public abstract void shareToExternal(@org.jetbrains.annotations.NotNull()
    android.content.Context context, @org.jetbrains.annotations.NotNull()
    com.ekoapp.ekosdk.feed.EkoPost post);
    
    @kotlin.Metadata(mv = {1, 1, 16}, bv = {1, 0, 3}, k = 3)
    public final class DefaultImpls {
        
        public static void shareToMyTimeline(com.ekoapp.ekosdk.uikit.feed.settings.IPostShareClickListener $this, @org.jetbrains.annotations.NotNull()
        android.content.Context context, @org.jetbrains.annotations.NotNull()
        com.ekoapp.ekosdk.feed.EkoPost post) {
        }
        
        public static void shareToGroup(com.ekoapp.ekosdk.uikit.feed.settings.IPostShareClickListener $this, @org.jetbrains.annotations.NotNull()
        android.content.Context context, @org.jetbrains.annotations.NotNull()
        com.ekoapp.ekosdk.feed.EkoPost post) {
        }
        
        public static void shareToExternal(com.ekoapp.ekosdk.uikit.feed.settings.IPostShareClickListener $this, @org.jetbrains.annotations.NotNull()
        android.content.Context context, @org.jetbrains.annotations.NotNull()
        com.ekoapp.ekosdk.feed.EkoPost post) {
        }
    }
}