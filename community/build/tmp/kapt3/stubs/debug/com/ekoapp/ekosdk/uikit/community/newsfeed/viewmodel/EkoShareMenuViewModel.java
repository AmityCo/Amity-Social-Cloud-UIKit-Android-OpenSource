package com.ekoapp.ekosdk.uikit.community.newsfeed.viewmodel;

import java.lang.System;

@kotlin.Metadata(mv = {1, 1, 16}, bv = {1, 0, 3}, k = 1, d1 = {"\u0000$\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000b\n\u0002\b\u0003\b\u0016\u0018\u00002\u00020\u0001B\r\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\u0002\u0010\u0004J\u000e\u0010\u0007\u001a\b\u0012\u0004\u0012\u00020\t0\bH\u0002J\u0006\u0010\n\u001a\u00020\u000bJ\u0006\u0010\f\u001a\u00020\u000bJ\u0006\u0010\r\u001a\u00020\u000bR\u0014\u0010\u0002\u001a\u00020\u0003X\u0096\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0005\u0010\u0006\u00a8\u0006\u000e"}, d2 = {"Lcom/ekoapp/ekosdk/uikit/community/newsfeed/viewmodel/EkoShareMenuViewModel;", "", "post", "Lcom/ekoapp/ekosdk/feed/EkoPost;", "(Lcom/ekoapp/ekosdk/feed/EkoPost;)V", "getPost", "()Lcom/ekoapp/ekosdk/feed/EkoPost;", "getPostSharingTargets", "", "Lcom/ekoapp/ekosdk/uikit/feed/settings/EkoPostSharingTarget;", "isRemoveMoreOption", "", "isRemoveShareToGroup", "isRemoveShareToMyTimeline", "community_debug"})
public class EkoShareMenuViewModel {
    @org.jetbrains.annotations.NotNull()
    private final com.ekoapp.ekosdk.feed.EkoPost post = null;
    
    public final boolean isRemoveShareToMyTimeline() {
        return false;
    }
    
    public final boolean isRemoveShareToGroup() {
        return false;
    }
    
    public final boolean isRemoveMoreOption() {
        return false;
    }
    
    private final java.util.List<com.ekoapp.ekosdk.uikit.feed.settings.EkoPostSharingTarget> getPostSharingTargets() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public com.ekoapp.ekosdk.feed.EkoPost getPost() {
        return null;
    }
    
    public EkoShareMenuViewModel(@org.jetbrains.annotations.NotNull()
    com.ekoapp.ekosdk.feed.EkoPost post) {
        super();
    }
}