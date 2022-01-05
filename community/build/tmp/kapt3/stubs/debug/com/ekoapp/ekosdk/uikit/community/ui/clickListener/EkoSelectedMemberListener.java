package com.ekoapp.ekosdk.uikit.community.ui.clickListener;

import java.lang.System;

@kotlin.Metadata(mv = {1, 1, 16}, bv = {1, 0, 3}, k = 1, d1 = {"\u0000\u0016\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\bf\u0018\u00002\u00020\u0001J\u0010\u0010\u0002\u001a\u00020\u00032\u0006\u0010\u0004\u001a\u00020\u0005H&\u00a8\u0006\u0006"}, d2 = {"Lcom/ekoapp/ekosdk/uikit/community/ui/clickListener/EkoSelectedMemberListener;", "", "onMemberRemoved", "", "member", "Lcom/ekoapp/ekosdk/uikit/community/data/SelectMemberItem;", "community_debug"})
public abstract interface EkoSelectedMemberListener {
    
    public abstract void onMemberRemoved(@org.jetbrains.annotations.NotNull()
    com.ekoapp.ekosdk.uikit.community.data.SelectMemberItem member);
}