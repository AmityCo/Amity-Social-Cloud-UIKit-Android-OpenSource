package com.ekoapp.ekosdk.uikit.chat.messages.popUp;

import java.lang.System;

@kotlin.Metadata(mv = {1, 1, 16}, bv = {1, 0, 3}, k = 1, d1 = {"\u00000\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\u0018\u00002\u00020\u0001:\u0001\u000fB\u0005\u00a2\u0006\u0002\u0010\u0002J\u0006\u0010\u0005\u001a\u00020\u0006J&\u0010\u0007\u001a\u00020\u00062\u0006\u0010\b\u001a\u00020\t2\u0006\u0010\n\u001a\u00020\t2\u0006\u0010\u000b\u001a\u00020\f2\u0006\u0010\r\u001a\u00020\u000eR\u000e\u0010\u0003\u001a\u00020\u0004X\u0082.\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u0010"}, d2 = {"Lcom/ekoapp/ekosdk/uikit/chat/messages/popUp/EkoPopUp;", "", "()V", "popUpWindow", "Landroid/widget/PopupWindow;", "dismiss", "", "showPopUp", "rootView", "Landroid/view/View;", "anchor", "viewModel", "Lcom/ekoapp/ekosdk/uikit/chat/messages/viewModel/EkoSelectableMessageViewModel;", "gravity", "Lcom/ekoapp/ekosdk/uikit/chat/messages/popUp/EkoPopUp$PopUpGravity;", "PopUpGravity", "chatkit_debug"})
public final class EkoPopUp {
    private android.widget.PopupWindow popUpWindow;
    
    public final void showPopUp(@org.jetbrains.annotations.NotNull()
    android.view.View rootView, @org.jetbrains.annotations.NotNull()
    android.view.View anchor, @org.jetbrains.annotations.NotNull()
    com.ekoapp.ekosdk.uikit.chat.messages.viewModel.EkoSelectableMessageViewModel viewModel, @org.jetbrains.annotations.NotNull()
    com.ekoapp.ekosdk.uikit.chat.messages.popUp.EkoPopUp.PopUpGravity gravity) {
    }
    
    public final void dismiss() {
    }
    
    public EkoPopUp() {
        super();
    }
    
    @kotlin.Metadata(mv = {1, 1, 16}, bv = {1, 0, 3}, k = 1, d1 = {"\u0000\f\n\u0002\u0018\u0002\n\u0002\u0010\u0010\n\u0002\b\u0004\b\u0086\u0001\u0018\u00002\b\u0012\u0004\u0012\u00020\u00000\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002j\u0002\b\u0003j\u0002\b\u0004\u00a8\u0006\u0005"}, d2 = {"Lcom/ekoapp/ekosdk/uikit/chat/messages/popUp/EkoPopUp$PopUpGravity;", "", "(Ljava/lang/String;I)V", "START", "END", "chatkit_debug"})
    public static enum PopUpGravity {
        /*public static final*/ START /* = new START() */,
        /*public static final*/ END /* = new END() */;
        
        PopUpGravity() {
        }
    }
}