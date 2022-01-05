package com.ekoapp.ekosdk.uikit.community.explore.adapter;

import java.lang.System;

@kotlin.Metadata(mv = {1, 1, 16}, bv = {1, 0, 3}, k = 1, d1 = {"\u0000\u0018\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u000e\n\u0000\n\u0002\u0010\u0002\n\u0002\b\u0002\bf\u0018\u00002\u00020\u0001J\n\u0010\u0002\u001a\u0004\u0018\u00010\u0003H&J\u0012\u0010\u0004\u001a\u00020\u00052\b\u0010\u0006\u001a\u0004\u0018\u00010\u0003H&\u00a8\u0006\u0007"}, d2 = {"Lcom/ekoapp/ekosdk/uikit/community/explore/adapter/ICategorySelectionListener;", "", "getSelection", "", "setSelection", "", "selectedCategory", "community_debug"})
public abstract interface ICategorySelectionListener {
    
    public abstract void setSelection(@org.jetbrains.annotations.Nullable()
    java.lang.String selectedCategory);
    
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.String getSelection();
}