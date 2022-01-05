package com.ekoapp.ekosdk.uikit.community.domain.repository;

import java.lang.System;

@kotlin.Metadata(mv = {1, 1, 16}, bv = {1, 0, 3}, k = 1, d1 = {"\u0000\"\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\bf\u0018\u00002\u00020\u0001J\u000e\u0010\u0002\u001a\b\u0012\u0004\u0012\u00020\u00040\u0003H&J\u0016\u0010\u0002\u001a\b\u0012\u0004\u0012\u00020\u00040\u00032\u0006\u0010\u0005\u001a\u00020\u0006H&J\u000e\u0010\u0007\u001a\b\u0012\u0004\u0012\u00020\b0\u0003H&J\u000e\u0010\t\u001a\b\u0012\u0004\u0012\u00020\b0\u0003H&\u00a8\u0006\n"}, d2 = {"Lcom/ekoapp/ekosdk/uikit/community/domain/repository/IChannelRepository;", "", "getChannelCategory", "", "Lcom/ekoapp/ekosdk/uikit/community/domain/model/ChannelCategory;", "parentCategory", "", "getRecommendedChannels", "Lcom/ekoapp/ekosdk/uikit/community/domain/model/Channel;", "getTrendingChannels", "community_debug"})
public abstract interface IChannelRepository {
    
    @org.jetbrains.annotations.NotNull()
    public abstract java.util.List<com.ekoapp.ekosdk.uikit.community.domain.model.Channel> getRecommendedChannels();
    
    @org.jetbrains.annotations.NotNull()
    public abstract java.util.List<com.ekoapp.ekosdk.uikit.community.domain.model.Channel> getTrendingChannels();
    
    @org.jetbrains.annotations.NotNull()
    public abstract java.util.List<com.ekoapp.ekosdk.uikit.community.domain.model.ChannelCategory> getChannelCategory();
    
    @org.jetbrains.annotations.NotNull()
    public abstract java.util.List<com.ekoapp.ekosdk.uikit.community.domain.model.ChannelCategory> getChannelCategory(@org.jetbrains.annotations.NotNull()
    java.lang.String parentCategory);
}