package com.ekoapp.ekosdk.uikit.community.newsfeed.listener;

import java.lang.System;

@kotlin.Metadata(mv = {1, 1, 16}, bv = {1, 0, 3}, k = 1, d1 = {"\u0000\u001c\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0000\bf\u0018\u00002\u00020\u0001J\u0018\u0010\u0002\u001a\u00020\u00032\u0006\u0010\u0004\u001a\u00020\u00052\u0006\u0010\u0006\u001a\u00020\u0007H&\u00a8\u0006\b"}, d2 = {"Lcom/ekoapp/ekosdk/uikit/community/newsfeed/listener/ICreatePostCommunitySelectionListener;", "", "onClickCommunity", "", "community", "Lcom/ekoapp/ekosdk/community/EkoCommunity;", "position", "", "community_debug"})
public abstract interface ICreatePostCommunitySelectionListener {
    
    public abstract void onClickCommunity(@org.jetbrains.annotations.NotNull()
    com.ekoapp.ekosdk.community.EkoCommunity community, int position);
}