package com.ekoapp.ekosdk.uikit.community.setting;

import java.lang.System;

@kotlin.Metadata(mv = {1, 1, 16}, bv = {1, 0, 3}, k = 1, d1 = {"\u0000*\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\b\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\b6\u0018\u00002\u00020\u0001:\u0007\u0003\u0004\u0005\u0006\u0007\b\tB\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002\u0082\u0001\u0007\n\u000b\f\r\u000e\u000f\u0010\u00a8\u0006\u0011"}, d2 = {"Lcom/ekoapp/ekosdk/uikit/community/setting/SettingsItem;", "", "()V", "Header", "Margin", "NavigationContent", "RadioContent", "Separator", "TextContent", "ToggleContent", "Lcom/ekoapp/ekosdk/uikit/community/setting/SettingsItem$Header;", "Lcom/ekoapp/ekosdk/uikit/community/setting/SettingsItem$TextContent;", "Lcom/ekoapp/ekosdk/uikit/community/setting/SettingsItem$NavigationContent;", "Lcom/ekoapp/ekosdk/uikit/community/setting/SettingsItem$ToggleContent;", "Lcom/ekoapp/ekosdk/uikit/community/setting/SettingsItem$RadioContent;", "Lcom/ekoapp/ekosdk/uikit/community/setting/SettingsItem$Margin;", "Lcom/ekoapp/ekosdk/uikit/community/setting/SettingsItem$Separator;", "community_debug"})
public abstract class SettingsItem {
    
    private SettingsItem() {
        super();
    }
    
    @kotlin.Metadata(mv = {1, 1, 16}, bv = {1, 0, 3}, k = 1, d1 = {"\u0000\u0012\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0002\b\u0004\u0018\u00002\u00020\u0001B\r\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\u0002\u0010\u0004R\u0011\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0005\u0010\u0006\u00a8\u0006\u0007"}, d2 = {"Lcom/ekoapp/ekosdk/uikit/community/setting/SettingsItem$Header;", "Lcom/ekoapp/ekosdk/uikit/community/setting/SettingsItem;", "title", "", "(I)V", "getTitle", "()I", "community_debug"})
    public static final class Header extends com.ekoapp.ekosdk.uikit.community.setting.SettingsItem {
        private final int title = 0;
        
        public final int getTitle() {
            return 0;
        }
        
        public Header(int title) {
            super();
        }
    }
    
    @kotlin.Metadata(mv = {1, 1, 16}, bv = {1, 0, 3}, k = 1, d1 = {"\u0000$\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0002\b\u0004\n\u0002\u0010\u000b\n\u0000\n\u0002\u0018\u0002\n\u0002\u0010\u0002\n\u0002\b\f\u0018\u00002\u00020\u0001BG\u0012\n\b\u0002\u0010\u0002\u001a\u0004\u0018\u00010\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0003\u0012\n\b\u0002\u0010\u0005\u001a\u0004\u0018\u00010\u0003\u0012\b\b\u0002\u0010\u0006\u001a\u00020\u0003\u0012\b\b\u0002\u0010\u0007\u001a\u00020\b\u0012\f\u0010\t\u001a\b\u0012\u0004\u0012\u00020\u000b0\n\u00a2\u0006\u0002\u0010\fR\u0017\u0010\t\u001a\b\u0012\u0004\u0012\u00020\u000b0\n\u00a2\u0006\b\n\u0000\u001a\u0004\b\r\u0010\u000eR\u0015\u0010\u0005\u001a\u0004\u0018\u00010\u0003\u00a2\u0006\n\n\u0002\u0010\u0011\u001a\u0004\b\u000f\u0010\u0010R\u0015\u0010\u0002\u001a\u0004\u0018\u00010\u0003\u00a2\u0006\n\n\u0002\u0010\u0011\u001a\u0004\b\u0012\u0010\u0010R\u0011\u0010\u0007\u001a\u00020\b\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0007\u0010\u0013R\u0011\u0010\u0004\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0014\u0010\u0015R\u0011\u0010\u0006\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0016\u0010\u0015\u00a8\u0006\u0017"}, d2 = {"Lcom/ekoapp/ekosdk/uikit/community/setting/SettingsItem$TextContent;", "Lcom/ekoapp/ekosdk/uikit/community/setting/SettingsItem;", "icon", "", "title", "description", "titleTextColor", "isTitleBold", "", "callback", "Lkotlin/Function0;", "", "(Ljava/lang/Integer;ILjava/lang/Integer;IZLkotlin/jvm/functions/Function0;)V", "getCallback", "()Lkotlin/jvm/functions/Function0;", "getDescription", "()Ljava/lang/Integer;", "Ljava/lang/Integer;", "getIcon", "()Z", "getTitle", "()I", "getTitleTextColor", "community_debug"})
    public static final class TextContent extends com.ekoapp.ekosdk.uikit.community.setting.SettingsItem {
        @org.jetbrains.annotations.Nullable()
        private final java.lang.Integer icon = null;
        private final int title = 0;
        @org.jetbrains.annotations.Nullable()
        private final java.lang.Integer description = null;
        private final int titleTextColor = 0;
        private final boolean isTitleBold = false;
        @org.jetbrains.annotations.NotNull()
        private final kotlin.jvm.functions.Function0<kotlin.Unit> callback = null;
        
        @org.jetbrains.annotations.Nullable()
        public final java.lang.Integer getIcon() {
            return null;
        }
        
        public final int getTitle() {
            return 0;
        }
        
        @org.jetbrains.annotations.Nullable()
        public final java.lang.Integer getDescription() {
            return null;
        }
        
        public final int getTitleTextColor() {
            return 0;
        }
        
        public final boolean isTitleBold() {
            return false;
        }
        
        @org.jetbrains.annotations.NotNull()
        public final kotlin.jvm.functions.Function0<kotlin.Unit> getCallback() {
            return null;
        }
        
        public TextContent(@org.jetbrains.annotations.Nullable()
        java.lang.Integer icon, int title, @org.jetbrains.annotations.Nullable()
        java.lang.Integer description, int titleTextColor, boolean isTitleBold, @org.jetbrains.annotations.NotNull()
        kotlin.jvm.functions.Function0<kotlin.Unit> callback) {
            super();
        }
    }
    
    @kotlin.Metadata(mv = {1, 1, 16}, bv = {1, 0, 3}, k = 1, d1 = {"\u0000$\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0002\b\u0005\n\u0002\u0010\u000b\n\u0000\n\u0002\u0018\u0002\n\u0002\u0010\u0002\n\u0002\b\r\u0018\u00002\u00020\u0001BU\u0012\n\b\u0002\u0010\u0002\u001a\u0004\u0018\u00010\u0003\u0012\n\b\u0002\u0010\u0004\u001a\u0004\u0018\u00010\u0003\u0012\u0006\u0010\u0005\u001a\u00020\u0003\u0012\n\b\u0002\u0010\u0006\u001a\u0004\u0018\u00010\u0003\u0012\n\b\u0002\u0010\u0007\u001a\u0004\u0018\u00010\u0003\u0012\b\b\u0002\u0010\b\u001a\u00020\t\u0012\f\u0010\n\u001a\b\u0012\u0004\u0012\u00020\f0\u000b\u00a2\u0006\u0002\u0010\rR\u0017\u0010\n\u001a\b\u0012\u0004\u0012\u00020\f0\u000b\u00a2\u0006\b\n\u0000\u001a\u0004\b\u000e\u0010\u000fR\u0015\u0010\u0007\u001a\u0004\u0018\u00010\u0003\u00a2\u0006\n\n\u0002\u0010\u0012\u001a\u0004\b\u0010\u0010\u0011R\u0015\u0010\u0002\u001a\u0004\u0018\u00010\u0003\u00a2\u0006\n\n\u0002\u0010\u0012\u001a\u0004\b\u0013\u0010\u0011R\u0015\u0010\u0004\u001a\u0004\u0018\u00010\u0003\u00a2\u0006\n\n\u0002\u0010\u0012\u001a\u0004\b\u0014\u0010\u0011R\u0011\u0010\b\u001a\u00020\t\u00a2\u0006\b\n\u0000\u001a\u0004\b\b\u0010\u0015R\u0011\u0010\u0005\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0016\u0010\u0017R\u0015\u0010\u0006\u001a\u0004\u0018\u00010\u0003\u00a2\u0006\n\n\u0002\u0010\u0012\u001a\u0004\b\u0018\u0010\u0011\u00a8\u0006\u0019"}, d2 = {"Lcom/ekoapp/ekosdk/uikit/community/setting/SettingsItem$NavigationContent;", "Lcom/ekoapp/ekosdk/uikit/community/setting/SettingsItem;", "icon", "", "iconNavigation", "title", "value", "description", "isTitleBold", "", "callback", "Lkotlin/Function0;", "", "(Ljava/lang/Integer;Ljava/lang/Integer;ILjava/lang/Integer;Ljava/lang/Integer;ZLkotlin/jvm/functions/Function0;)V", "getCallback", "()Lkotlin/jvm/functions/Function0;", "getDescription", "()Ljava/lang/Integer;", "Ljava/lang/Integer;", "getIcon", "getIconNavigation", "()Z", "getTitle", "()I", "getValue", "community_debug"})
    public static final class NavigationContent extends com.ekoapp.ekosdk.uikit.community.setting.SettingsItem {
        @org.jetbrains.annotations.Nullable()
        private final java.lang.Integer icon = null;
        @org.jetbrains.annotations.Nullable()
        private final java.lang.Integer iconNavigation = null;
        private final int title = 0;
        @org.jetbrains.annotations.Nullable()
        private final java.lang.Integer value = null;
        @org.jetbrains.annotations.Nullable()
        private final java.lang.Integer description = null;
        private final boolean isTitleBold = false;
        @org.jetbrains.annotations.NotNull()
        private final kotlin.jvm.functions.Function0<kotlin.Unit> callback = null;
        
        @org.jetbrains.annotations.Nullable()
        public final java.lang.Integer getIcon() {
            return null;
        }
        
        @org.jetbrains.annotations.Nullable()
        public final java.lang.Integer getIconNavigation() {
            return null;
        }
        
        public final int getTitle() {
            return 0;
        }
        
        @org.jetbrains.annotations.Nullable()
        public final java.lang.Integer getValue() {
            return null;
        }
        
        @org.jetbrains.annotations.Nullable()
        public final java.lang.Integer getDescription() {
            return null;
        }
        
        public final boolean isTitleBold() {
            return false;
        }
        
        @org.jetbrains.annotations.NotNull()
        public final kotlin.jvm.functions.Function0<kotlin.Unit> getCallback() {
            return null;
        }
        
        public NavigationContent(@org.jetbrains.annotations.Nullable()
        java.lang.Integer icon, @org.jetbrains.annotations.Nullable()
        java.lang.Integer iconNavigation, int title, @org.jetbrains.annotations.Nullable()
        java.lang.Integer value, @org.jetbrains.annotations.Nullable()
        java.lang.Integer description, boolean isTitleBold, @org.jetbrains.annotations.NotNull()
        kotlin.jvm.functions.Function0<kotlin.Unit> callback) {
            super();
        }
    }
    
    @kotlin.Metadata(mv = {1, 1, 16}, bv = {1, 0, 3}, k = 1, d1 = {"\u0000*\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\u0010\u000b\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0010\u0002\n\u0002\b\f\u0018\u00002\u00020\u0001BQ\u0012\n\b\u0002\u0010\u0002\u001a\u0004\u0018\u00010\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0003\u0012\n\b\u0002\u0010\u0005\u001a\u0004\u0018\u00010\u0003\u0012\f\u0010\u0006\u001a\b\u0012\u0004\u0012\u00020\b0\u0007\u0012\b\b\u0002\u0010\t\u001a\u00020\b\u0012\u0012\u0010\n\u001a\u000e\u0012\u0004\u0012\u00020\b\u0012\u0004\u0012\u00020\f0\u000b\u00a2\u0006\u0002\u0010\rR\u001d\u0010\n\u001a\u000e\u0012\u0004\u0012\u00020\b\u0012\u0004\u0012\u00020\f0\u000b\u00a2\u0006\b\n\u0000\u001a\u0004\b\u000e\u0010\u000fR\u0015\u0010\u0005\u001a\u0004\u0018\u00010\u0003\u00a2\u0006\n\n\u0002\u0010\u0012\u001a\u0004\b\u0010\u0010\u0011R\u0015\u0010\u0002\u001a\u0004\u0018\u00010\u0003\u00a2\u0006\n\n\u0002\u0010\u0012\u001a\u0004\b\u0013\u0010\u0011R\u0011\u0010\t\u001a\u00020\b\u00a2\u0006\b\n\u0000\u001a\u0004\b\t\u0010\u0014R\u0017\u0010\u0006\u001a\b\u0012\u0004\u0012\u00020\b0\u0007\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0006\u0010\u0015R\u0011\u0010\u0004\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0016\u0010\u0017\u00a8\u0006\u0018"}, d2 = {"Lcom/ekoapp/ekosdk/uikit/community/setting/SettingsItem$ToggleContent;", "Lcom/ekoapp/ekosdk/uikit/community/setting/SettingsItem;", "icon", "", "title", "description", "isToggled", "Lio/reactivex/Flowable;", "", "isTitleBold", "callback", "Lkotlin/Function1;", "", "(Ljava/lang/Integer;ILjava/lang/Integer;Lio/reactivex/Flowable;ZLkotlin/jvm/functions/Function1;)V", "getCallback", "()Lkotlin/jvm/functions/Function1;", "getDescription", "()Ljava/lang/Integer;", "Ljava/lang/Integer;", "getIcon", "()Z", "()Lio/reactivex/Flowable;", "getTitle", "()I", "community_debug"})
    public static final class ToggleContent extends com.ekoapp.ekosdk.uikit.community.setting.SettingsItem {
        @org.jetbrains.annotations.Nullable()
        private final java.lang.Integer icon = null;
        private final int title = 0;
        @org.jetbrains.annotations.Nullable()
        private final java.lang.Integer description = null;
        @org.jetbrains.annotations.NotNull()
        private final io.reactivex.Flowable<java.lang.Boolean> isToggled = null;
        private final boolean isTitleBold = false;
        @org.jetbrains.annotations.NotNull()
        private final kotlin.jvm.functions.Function1<java.lang.Boolean, kotlin.Unit> callback = null;
        
        @org.jetbrains.annotations.Nullable()
        public final java.lang.Integer getIcon() {
            return null;
        }
        
        public final int getTitle() {
            return 0;
        }
        
        @org.jetbrains.annotations.Nullable()
        public final java.lang.Integer getDescription() {
            return null;
        }
        
        @org.jetbrains.annotations.NotNull()
        public final io.reactivex.Flowable<java.lang.Boolean> isToggled() {
            return null;
        }
        
        public final boolean isTitleBold() {
            return false;
        }
        
        @org.jetbrains.annotations.NotNull()
        public final kotlin.jvm.functions.Function1<java.lang.Boolean, kotlin.Unit> getCallback() {
            return null;
        }
        
        public ToggleContent(@org.jetbrains.annotations.Nullable()
        java.lang.Integer icon, int title, @org.jetbrains.annotations.Nullable()
        java.lang.Integer description, @org.jetbrains.annotations.NotNull()
        io.reactivex.Flowable<java.lang.Boolean> isToggled, boolean isTitleBold, @org.jetbrains.annotations.NotNull()
        kotlin.jvm.functions.Function1<? super java.lang.Boolean, kotlin.Unit> callback) {
            super();
        }
    }
    
    @kotlin.Metadata(mv = {1, 1, 16}, bv = {1, 0, 3}, k = 1, d1 = {"\u0000(\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0002\u0010\b\n\u0002\u0010\u000b\n\u0000\n\u0002\u0018\u0002\n\u0002\u0010\u0002\n\u0002\b\u0006\u0018\u00002\u00020\u0001B3\u0012\u0018\u0010\u0002\u001a\u0014\u0012\u0010\u0012\u000e\u0012\u0004\u0012\u00020\u0005\u0012\u0004\u0012\u00020\u00060\u00040\u0003\u0012\u0012\u0010\u0007\u001a\u000e\u0012\u0004\u0012\u00020\u0005\u0012\u0004\u0012\u00020\t0\b\u00a2\u0006\u0002\u0010\nR\u001d\u0010\u0007\u001a\u000e\u0012\u0004\u0012\u00020\u0005\u0012\u0004\u0012\u00020\t0\b\u00a2\u0006\b\n\u0000\u001a\u0004\b\u000b\u0010\fR#\u0010\u0002\u001a\u0014\u0012\u0010\u0012\u000e\u0012\u0004\u0012\u00020\u0005\u0012\u0004\u0012\u00020\u00060\u00040\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\r\u0010\u000e\u00a8\u0006\u000f"}, d2 = {"Lcom/ekoapp/ekosdk/uikit/community/setting/SettingsItem$RadioContent;", "Lcom/ekoapp/ekosdk/uikit/community/setting/SettingsItem;", "choices", "", "Lkotlin/Pair;", "", "", "callback", "Lkotlin/Function1;", "", "(Ljava/util/List;Lkotlin/jvm/functions/Function1;)V", "getCallback", "()Lkotlin/jvm/functions/Function1;", "getChoices", "()Ljava/util/List;", "community_debug"})
    public static final class RadioContent extends com.ekoapp.ekosdk.uikit.community.setting.SettingsItem {
        @org.jetbrains.annotations.NotNull()
        private final java.util.List<kotlin.Pair<java.lang.Integer, java.lang.Boolean>> choices = null;
        @org.jetbrains.annotations.NotNull()
        private final kotlin.jvm.functions.Function1<java.lang.Integer, kotlin.Unit> callback = null;
        
        @org.jetbrains.annotations.NotNull()
        public final java.util.List<kotlin.Pair<java.lang.Integer, java.lang.Boolean>> getChoices() {
            return null;
        }
        
        @org.jetbrains.annotations.NotNull()
        public final kotlin.jvm.functions.Function1<java.lang.Integer, kotlin.Unit> getCallback() {
            return null;
        }
        
        public RadioContent(@org.jetbrains.annotations.NotNull()
        java.util.List<kotlin.Pair<java.lang.Integer, java.lang.Boolean>> choices, @org.jetbrains.annotations.NotNull()
        kotlin.jvm.functions.Function1<? super java.lang.Integer, kotlin.Unit> callback) {
            super();
        }
    }
    
    @kotlin.Metadata(mv = {1, 1, 16}, bv = {1, 0, 3}, k = 1, d1 = {"\u0000\u0012\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0002\b\u0004\u0018\u00002\u00020\u0001B\u000f\u0012\b\b\u0001\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\u0002\u0010\u0004R\u0011\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0005\u0010\u0006\u00a8\u0006\u0007"}, d2 = {"Lcom/ekoapp/ekosdk/uikit/community/setting/SettingsItem$Margin;", "Lcom/ekoapp/ekosdk/uikit/community/setting/SettingsItem;", "margin", "", "(I)V", "getMargin", "()I", "community_debug"})
    public static final class Margin extends com.ekoapp.ekosdk.uikit.community.setting.SettingsItem {
        private final int margin = 0;
        
        public final int getMargin() {
            return 0;
        }
        
        public Margin(@androidx.annotation.DimenRes()
        int margin) {
            super();
        }
    }
    
    @kotlin.Metadata(mv = {1, 1, 16}, bv = {1, 0, 3}, k = 1, d1 = {"\u0000\f\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\b\u00c6\u0002\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002\u00a8\u0006\u0003"}, d2 = {"Lcom/ekoapp/ekosdk/uikit/community/setting/SettingsItem$Separator;", "Lcom/ekoapp/ekosdk/uikit/community/setting/SettingsItem;", "()V", "community_debug"})
    public static final class Separator extends com.ekoapp.ekosdk.uikit.community.setting.SettingsItem {
        public static final com.ekoapp.ekosdk.uikit.community.setting.SettingsItem.Separator INSTANCE = null;
        
        private Separator() {
            super();
        }
    }
}