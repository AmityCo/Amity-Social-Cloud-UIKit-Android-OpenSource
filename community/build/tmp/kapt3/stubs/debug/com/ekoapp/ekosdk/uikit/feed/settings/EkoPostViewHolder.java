package com.ekoapp.ekosdk.uikit.feed.settings;

import java.lang.System;

@kotlin.Metadata(mv = {1, 1, 16}, bv = {1, 0, 3}, k = 1, d1 = {"\u0000\"\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0000\n\u0002\u0010\b\n\u0000\bf\u0018\u00002\u00020\u0001J\u0010\u0010\u0002\u001a\u00020\u00032\u0006\u0010\u0004\u001a\u00020\u0005H&J\b\u0010\u0006\u001a\u00020\u0007H&J\b\u0010\b\u001a\u00020\tH&\u00a8\u0006\n"}, d2 = {"Lcom/ekoapp/ekosdk/uikit/feed/settings/EkoPostViewHolder;", "", "createViewHolder", "Lcom/ekoapp/ekosdk/uikit/community/newsfeed/adapter/EkoBasePostViewHolder;", "view", "Landroid/view/View;", "getDataType", "", "getLayoutId", "", "community_debug"})
public abstract interface EkoPostViewHolder {
    
    @org.jetbrains.annotations.NotNull()
    public abstract java.lang.String getDataType();
    
    public abstract int getLayoutId();
    
    @org.jetbrains.annotations.NotNull()
    public abstract com.ekoapp.ekosdk.uikit.community.newsfeed.adapter.EkoBasePostViewHolder createViewHolder(@org.jetbrains.annotations.NotNull()
    android.view.View view);
}