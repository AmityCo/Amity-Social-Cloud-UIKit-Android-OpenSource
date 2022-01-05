package com.ekoapp.ekosdk.uikit.community.newsfeed.listener;

import java.lang.System;

@kotlin.Metadata(mv = {1, 1, 16}, bv = {1, 0, 3}, k = 1, d1 = {"\u0000 \n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0000\bf\u0018\u00002\u00020\u0001J\u001e\u0010\u0002\u001a\u00020\u00032\f\u0010\u0004\u001a\b\u0012\u0004\u0012\u00020\u00060\u00052\u0006\u0010\u0007\u001a\u00020\bH&\u00a8\u0006\t"}, d2 = {"Lcom/ekoapp/ekosdk/uikit/community/newsfeed/listener/IPostImageClickListener;", "", "onClickImage", "", "images", "", "Lcom/ekoapp/ekosdk/file/EkoImage;", "position", "", "community_debug"})
public abstract interface IPostImageClickListener {
    
    public abstract void onClickImage(@org.jetbrains.annotations.NotNull()
    java.util.List<com.ekoapp.ekosdk.file.EkoImage> images, int position);
}