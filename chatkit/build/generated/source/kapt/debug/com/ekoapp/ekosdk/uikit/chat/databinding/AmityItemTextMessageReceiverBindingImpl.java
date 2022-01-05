package com.ekoapp.ekosdk.uikit.chat.databinding;
import com.ekoapp.ekosdk.uikit.chat.R;
import com.ekoapp.ekosdk.uikit.chat.BR;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import android.view.View;
@SuppressWarnings("unchecked")
public class AmityItemTextMessageReceiverBindingImpl extends AmityItemTextMessageReceiverBinding  {

    @Nullable
    private static final androidx.databinding.ViewDataBinding.IncludedLayouts sIncludes;
    @Nullable
    private static final android.util.SparseIntArray sViewsWithIds;
    static {
        sIncludes = new androidx.databinding.ViewDataBinding.IncludedLayouts(9);
        sIncludes.setIncludes(0, 
            new String[] {"amity_view_date", "amity_view_msg_deleted"},
            new int[] {7, 8},
            new int[] {com.ekoapp.ekosdk.uikit.chat.R.layout.amity_view_date,
                com.ekoapp.ekosdk.uikit.chat.R.layout.amity_view_msg_deleted});
        sViewsWithIds = null;
    }
    // views
    @NonNull
    private final androidx.constraintlayout.widget.ConstraintLayout mboundView0;
    @Nullable
    private final com.ekoapp.ekosdk.uikit.chat.databinding.AmityViewMsgDeletedBinding mboundView01;
    @NonNull
    private final androidx.constraintlayout.widget.ConstraintLayout mboundView1;
    @NonNull
    private final android.widget.TextView mboundView6;
    // variables
    // values
    // listeners
    // Inverse Binding Event Handlers

    public AmityItemTextMessageReceiverBindingImpl(@Nullable androidx.databinding.DataBindingComponent bindingComponent, @NonNull View root) {
        this(bindingComponent, root, mapBindings(bindingComponent, root, 9, sIncludes, sViewsWithIds));
    }
    private AmityItemTextMessageReceiverBindingImpl(androidx.databinding.DataBindingComponent bindingComponent, View root, Object[] bindings) {
        super(bindingComponent, root, 11
            , (com.ekoapp.ekosdk.uikit.chat.databinding.AmityViewDateBinding) bindings[7]
            , (com.google.android.material.imageview.ShapeableImageView) bindings[2]
            , (com.ekoapp.ekosdk.uikit.components.EkoReadMoreTextView) bindings[4]
            , (android.widget.TextView) bindings[3]
            , (android.widget.TextView) bindings[5]
            );
        this.ivAvatar.setTag(null);
        this.mboundView0 = (androidx.constraintlayout.widget.ConstraintLayout) bindings[0];
        this.mboundView0.setTag(null);
        this.mboundView01 = (com.ekoapp.ekosdk.uikit.chat.databinding.AmityViewMsgDeletedBinding) bindings[8];
        setContainedBinding(this.mboundView01);
        this.mboundView1 = (androidx.constraintlayout.widget.ConstraintLayout) bindings[1];
        this.mboundView1.setTag(null);
        this.mboundView6 = (android.widget.TextView) bindings[6];
        this.mboundView6.setTag(null);
        this.tvMessageIncoming.setTag(null);
        this.tvName.setTag(null);
        this.tvTime.setTag(null);
        setRootTag(root);
        // listeners
        invalidateAll();
    }

    @Override
    public void invalidateAll() {
        synchronized(this) {
                mDirtyFlags = 0x2000L;
        }
        dateHeader.invalidateAll();
        mboundView01.invalidateAll();
        requestRebind();
    }

    @Override
    public boolean hasPendingBindings() {
        synchronized(this) {
            if (mDirtyFlags != 0) {
                return true;
            }
        }
        if (dateHeader.hasPendingBindings()) {
            return true;
        }
        if (mboundView01.hasPendingBindings()) {
            return true;
        }
        return false;
    }

    @Override
    public boolean setVariable(int variableId, @Nullable Object variable)  {
        boolean variableSet = true;
        if (BR.lonPressListener == variableId) {
            setLonPressListener((com.ekoapp.ekosdk.uikit.components.ILongPressListener) variable);
        }
        else if (BR.vmTextMessage == variableId) {
            setVmTextMessage((com.ekoapp.ekosdk.uikit.chat.messages.viewModel.EkoTextMessageViewModel) variable);
        }
        else {
            variableSet = false;
        }
            return variableSet;
    }

    public void setLonPressListener(@Nullable com.ekoapp.ekosdk.uikit.components.ILongPressListener LonPressListener) {
        this.mLonPressListener = LonPressListener;
        synchronized(this) {
            mDirtyFlags |= 0x800L;
        }
        notifyPropertyChanged(BR.lonPressListener);
        super.requestRebind();
    }
    public void setVmTextMessage(@Nullable com.ekoapp.ekosdk.uikit.chat.messages.viewModel.EkoTextMessageViewModel VmTextMessage) {
        this.mVmTextMessage = VmTextMessage;
        synchronized(this) {
            mDirtyFlags |= 0x1000L;
        }
        notifyPropertyChanged(BR.vmTextMessage);
        super.requestRebind();
    }

    @Override
    public void setLifecycleOwner(@Nullable androidx.lifecycle.LifecycleOwner lifecycleOwner) {
        super.setLifecycleOwner(lifecycleOwner);
        dateHeader.setLifecycleOwner(lifecycleOwner);
        mboundView01.setLifecycleOwner(lifecycleOwner);
    }

    @Override
    protected boolean onFieldChange(int localFieldId, Object object, int fieldId) {
        switch (localFieldId) {
            case 0 :
                return onChangeVmTextMessageDateFillColor((androidx.databinding.ObservableField<java.lang.Integer>) object, fieldId);
            case 1 :
                return onChangeVmTextMessageIsSenderVisible((androidx.databinding.ObservableBoolean) object, fieldId);
            case 2 :
                return onChangeVmTextMessageSender((androidx.databinding.ObservableField<java.lang.String>) object, fieldId);
            case 3 :
                return onChangeVmTextMessageMsgDate((androidx.databinding.ObservableField<java.lang.String>) object, fieldId);
            case 4 :
                return onChangeVmTextMessageIsDeleted((androidx.databinding.ObservableBoolean) object, fieldId);
            case 5 :
                return onChangeVmTextMessageInSelectionMode((androidx.databinding.ObservableBoolean) object, fieldId);
            case 6 :
                return onChangeVmTextMessageText((androidx.databinding.ObservableField<java.lang.String>) object, fieldId);
            case 7 :
                return onChangeDateHeader((com.ekoapp.ekosdk.uikit.chat.databinding.AmityViewDateBinding) object, fieldId);
            case 8 :
                return onChangeVmTextMessageMsgTime((androidx.databinding.ObservableField<java.lang.String>) object, fieldId);
            case 9 :
                return onChangeVmTextMessageReceiverFillColor((androidx.databinding.ObservableField<java.lang.Integer>) object, fieldId);
            case 10 :
                return onChangeVmTextMessageIsDateVisible((androidx.databinding.ObservableBoolean) object, fieldId);
        }
        return false;
    }
    private boolean onChangeVmTextMessageDateFillColor(androidx.databinding.ObservableField<java.lang.Integer> VmTextMessageDateFillColor, int fieldId) {
        if (fieldId == BR._all) {
            synchronized(this) {
                    mDirtyFlags |= 0x1L;
            }
            return true;
        }
        return false;
    }
    private boolean onChangeVmTextMessageIsSenderVisible(androidx.databinding.ObservableBoolean VmTextMessageIsSenderVisible, int fieldId) {
        if (fieldId == BR._all) {
            synchronized(this) {
                    mDirtyFlags |= 0x2L;
            }
            return true;
        }
        return false;
    }
    private boolean onChangeVmTextMessageSender(androidx.databinding.ObservableField<java.lang.String> VmTextMessageSender, int fieldId) {
        if (fieldId == BR._all) {
            synchronized(this) {
                    mDirtyFlags |= 0x4L;
            }
            return true;
        }
        return false;
    }
    private boolean onChangeVmTextMessageMsgDate(androidx.databinding.ObservableField<java.lang.String> VmTextMessageMsgDate, int fieldId) {
        if (fieldId == BR._all) {
            synchronized(this) {
                    mDirtyFlags |= 0x8L;
            }
            return true;
        }
        return false;
    }
    private boolean onChangeVmTextMessageIsDeleted(androidx.databinding.ObservableBoolean VmTextMessageIsDeleted, int fieldId) {
        if (fieldId == BR._all) {
            synchronized(this) {
                    mDirtyFlags |= 0x10L;
            }
            return true;
        }
        return false;
    }
    private boolean onChangeVmTextMessageInSelectionMode(androidx.databinding.ObservableBoolean VmTextMessageInSelectionMode, int fieldId) {
        if (fieldId == BR._all) {
            synchronized(this) {
                    mDirtyFlags |= 0x20L;
            }
            return true;
        }
        return false;
    }
    private boolean onChangeVmTextMessageText(androidx.databinding.ObservableField<java.lang.String> VmTextMessageText, int fieldId) {
        if (fieldId == BR._all) {
            synchronized(this) {
                    mDirtyFlags |= 0x40L;
            }
            return true;
        }
        return false;
    }
    private boolean onChangeDateHeader(com.ekoapp.ekosdk.uikit.chat.databinding.AmityViewDateBinding DateHeader, int fieldId) {
        if (fieldId == BR._all) {
            synchronized(this) {
                    mDirtyFlags |= 0x80L;
            }
            return true;
        }
        return false;
    }
    private boolean onChangeVmTextMessageMsgTime(androidx.databinding.ObservableField<java.lang.String> VmTextMessageMsgTime, int fieldId) {
        if (fieldId == BR._all) {
            synchronized(this) {
                    mDirtyFlags |= 0x100L;
            }
            return true;
        }
        return false;
    }
    private boolean onChangeVmTextMessageReceiverFillColor(androidx.databinding.ObservableField<java.lang.Integer> VmTextMessageReceiverFillColor, int fieldId) {
        if (fieldId == BR._all) {
            synchronized(this) {
                    mDirtyFlags |= 0x200L;
            }
            return true;
        }
        return false;
    }
    private boolean onChangeVmTextMessageIsDateVisible(androidx.databinding.ObservableBoolean VmTextMessageIsDateVisible, int fieldId) {
        if (fieldId == BR._all) {
            synchronized(this) {
                    mDirtyFlags |= 0x400L;
            }
            return true;
        }
        return false;
    }

    @Override
    protected void executeBindings() {
        long dirtyFlags = 0;
        synchronized(this) {
            dirtyFlags = mDirtyFlags;
            mDirtyFlags = 0;
        }
        java.lang.String vmTextMessageMsgDateGet = null;
        java.lang.Integer vmTextMessageDateFillColorGet = null;
        java.lang.String vmTextMessageMsgTimeGet = null;
        java.lang.String vmTextMessageSenderGet = null;
        androidx.databinding.ObservableField<java.lang.Integer> vmTextMessageDateFillColor = null;
        int androidxDatabindingViewDataBindingSafeUnboxVmTextMessageDateFillColorGet = 0;
        boolean vmTextMessageIsDateVisibleGet = false;
        java.lang.String vmTextMessageTextGet = null;
        androidx.databinding.ObservableBoolean vmTextMessageIsSenderVisible = null;
        int vmTextMessageInSelectionModeViewVISIBLEViewGONE = 0;
        int vmTextMessageIsSenderVisibleViewVISIBLEViewGONE = 0;
        boolean vmTextMessageIsSenderVisibleGet = false;
        androidx.databinding.ObservableField<java.lang.String> vmTextMessageSender = null;
        com.ekoapp.ekosdk.uikit.components.ILongPressListener lonPressListener = mLonPressListener;
        int vmTextMessageIsDateVisibleViewVISIBLEViewGONE = 0;
        com.ekoapp.ekosdk.uikit.chat.messages.viewModel.EkoTextMessageViewModel vmTextMessage = mVmTextMessage;
        androidx.databinding.ObservableField<java.lang.String> vmTextMessageMsgDate = null;
        java.lang.Integer vmTextMessageReceiverFillColorGet = null;
        androidx.databinding.ObservableBoolean vmTextMessageIsDeleted = null;
        androidx.databinding.ObservableBoolean vmTextMessageInSelectionMode = null;
        boolean vmTextMessageInSelectionModeGet = false;
        androidx.databinding.ObservableField<java.lang.String> vmTextMessageText = null;
        androidx.databinding.ObservableField<java.lang.String> vmTextMessageMsgTime = null;
        androidx.databinding.ObservableField<java.lang.Integer> vmTextMessageReceiverFillColor = null;
        androidx.databinding.ObservableBoolean vmTextMessageIsDateVisible = null;
        int vmTextMessageIsDeletedViewGONEViewVISIBLE = 0;
        boolean vmTextMessageIsDeletedGet = false;

        if ((dirtyFlags & 0x2800L) != 0) {
        }
        if ((dirtyFlags & 0x377fL) != 0) {


            if ((dirtyFlags & 0x3001L) != 0) {

                    if (vmTextMessage != null) {
                        // read vmTextMessage.dateFillColor
                        vmTextMessageDateFillColor = vmTextMessage.getDateFillColor();
                    }
                    updateRegistration(0, vmTextMessageDateFillColor);


                    if (vmTextMessageDateFillColor != null) {
                        // read vmTextMessage.dateFillColor.get()
                        vmTextMessageDateFillColorGet = vmTextMessageDateFillColor.get();
                    }


                    // read androidx.databinding.ViewDataBinding.safeUnbox(vmTextMessage.dateFillColor.get())
                    androidxDatabindingViewDataBindingSafeUnboxVmTextMessageDateFillColorGet = androidx.databinding.ViewDataBinding.safeUnbox(vmTextMessageDateFillColorGet);
            }
            if ((dirtyFlags & 0x3002L) != 0) {

                    if (vmTextMessage != null) {
                        // read vmTextMessage.isSenderVisible()
                        vmTextMessageIsSenderVisible = vmTextMessage.isSenderVisible();
                    }
                    updateRegistration(1, vmTextMessageIsSenderVisible);


                    if (vmTextMessageIsSenderVisible != null) {
                        // read vmTextMessage.isSenderVisible().get()
                        vmTextMessageIsSenderVisibleGet = vmTextMessageIsSenderVisible.get();
                    }
                if((dirtyFlags & 0x3002L) != 0) {
                    if(vmTextMessageIsSenderVisibleGet) {
                            dirtyFlags |= 0x20000L;
                    }
                    else {
                            dirtyFlags |= 0x10000L;
                    }
                }


                    // read vmTextMessage.isSenderVisible().get() ? View.VISIBLE : View.GONE
                    vmTextMessageIsSenderVisibleViewVISIBLEViewGONE = ((vmTextMessageIsSenderVisibleGet) ? (android.view.View.VISIBLE) : (android.view.View.GONE));
            }
            if ((dirtyFlags & 0x3004L) != 0) {

                    if (vmTextMessage != null) {
                        // read vmTextMessage.sender
                        vmTextMessageSender = vmTextMessage.getSender();
                    }
                    updateRegistration(2, vmTextMessageSender);


                    if (vmTextMessageSender != null) {
                        // read vmTextMessage.sender.get()
                        vmTextMessageSenderGet = vmTextMessageSender.get();
                    }
            }
            if ((dirtyFlags & 0x3008L) != 0) {

                    if (vmTextMessage != null) {
                        // read vmTextMessage.msgDate
                        vmTextMessageMsgDate = vmTextMessage.getMsgDate();
                    }
                    updateRegistration(3, vmTextMessageMsgDate);


                    if (vmTextMessageMsgDate != null) {
                        // read vmTextMessage.msgDate.get()
                        vmTextMessageMsgDateGet = vmTextMessageMsgDate.get();
                    }
            }
            if ((dirtyFlags & 0x3010L) != 0) {

                    if (vmTextMessage != null) {
                        // read vmTextMessage.isDeleted()
                        vmTextMessageIsDeleted = vmTextMessage.isDeleted();
                    }
                    updateRegistration(4, vmTextMessageIsDeleted);


                    if (vmTextMessageIsDeleted != null) {
                        // read vmTextMessage.isDeleted().get()
                        vmTextMessageIsDeletedGet = vmTextMessageIsDeleted.get();
                    }
                if((dirtyFlags & 0x3010L) != 0) {
                    if(vmTextMessageIsDeletedGet) {
                            dirtyFlags |= 0x200000L;
                    }
                    else {
                            dirtyFlags |= 0x100000L;
                    }
                }


                    // read vmTextMessage.isDeleted().get() ? View.GONE : View.VISIBLE
                    vmTextMessageIsDeletedViewGONEViewVISIBLE = ((vmTextMessageIsDeletedGet) ? (android.view.View.GONE) : (android.view.View.VISIBLE));
            }
            if ((dirtyFlags & 0x3020L) != 0) {

                    if (vmTextMessage != null) {
                        // read vmTextMessage.inSelectionMode
                        vmTextMessageInSelectionMode = vmTextMessage.getInSelectionMode();
                    }
                    updateRegistration(5, vmTextMessageInSelectionMode);


                    if (vmTextMessageInSelectionMode != null) {
                        // read vmTextMessage.inSelectionMode.get()
                        vmTextMessageInSelectionModeGet = vmTextMessageInSelectionMode.get();
                    }
                if((dirtyFlags & 0x3020L) != 0) {
                    if(vmTextMessageInSelectionModeGet) {
                            dirtyFlags |= 0x8000L;
                    }
                    else {
                            dirtyFlags |= 0x4000L;
                    }
                }


                    // read vmTextMessage.inSelectionMode.get() ? View.VISIBLE : View.GONE
                    vmTextMessageInSelectionModeViewVISIBLEViewGONE = ((vmTextMessageInSelectionModeGet) ? (android.view.View.VISIBLE) : (android.view.View.GONE));
            }
            if ((dirtyFlags & 0x3040L) != 0) {

                    if (vmTextMessage != null) {
                        // read vmTextMessage.text
                        vmTextMessageText = vmTextMessage.getText();
                    }
                    updateRegistration(6, vmTextMessageText);


                    if (vmTextMessageText != null) {
                        // read vmTextMessage.text.get()
                        vmTextMessageTextGet = vmTextMessageText.get();
                    }
            }
            if ((dirtyFlags & 0x3100L) != 0) {

                    if (vmTextMessage != null) {
                        // read vmTextMessage.msgTime
                        vmTextMessageMsgTime = vmTextMessage.getMsgTime();
                    }
                    updateRegistration(8, vmTextMessageMsgTime);


                    if (vmTextMessageMsgTime != null) {
                        // read vmTextMessage.msgTime.get()
                        vmTextMessageMsgTimeGet = vmTextMessageMsgTime.get();
                    }
            }
            if ((dirtyFlags & 0x3200L) != 0) {

                    if (vmTextMessage != null) {
                        // read vmTextMessage.receiverFillColor
                        vmTextMessageReceiverFillColor = vmTextMessage.getReceiverFillColor();
                    }
                    updateRegistration(9, vmTextMessageReceiverFillColor);


                    if (vmTextMessageReceiverFillColor != null) {
                        // read vmTextMessage.receiverFillColor.get()
                        vmTextMessageReceiverFillColorGet = vmTextMessageReceiverFillColor.get();
                    }
            }
            if ((dirtyFlags & 0x3400L) != 0) {

                    if (vmTextMessage != null) {
                        // read vmTextMessage.isDateVisible()
                        vmTextMessageIsDateVisible = vmTextMessage.isDateVisible();
                    }
                    updateRegistration(10, vmTextMessageIsDateVisible);


                    if (vmTextMessageIsDateVisible != null) {
                        // read vmTextMessage.isDateVisible().get()
                        vmTextMessageIsDateVisibleGet = vmTextMessageIsDateVisible.get();
                    }
                if((dirtyFlags & 0x3400L) != 0) {
                    if(vmTextMessageIsDateVisibleGet) {
                            dirtyFlags |= 0x80000L;
                    }
                    else {
                            dirtyFlags |= 0x40000L;
                    }
                }


                    // read vmTextMessage.isDateVisible().get() ? View.VISIBLE : View.GONE
                    vmTextMessageIsDateVisibleViewVISIBLEViewGONE = ((vmTextMessageIsDateVisibleGet) ? (android.view.View.VISIBLE) : (android.view.View.GONE));
            }
        }
        // batch finished
        if ((dirtyFlags & 0x3400L) != 0) {
            // api target 1

            this.dateHeader.getRoot().setVisibility(vmTextMessageIsDateVisibleViewVISIBLEViewGONE);
        }
        if ((dirtyFlags & 0x3008L) != 0) {
            // api target 1

            this.dateHeader.setDate(vmTextMessageMsgDateGet);
        }
        if ((dirtyFlags & 0x3001L) != 0) {
            // api target 1

            this.dateHeader.setDateFillColor(androidxDatabindingViewDataBindingSafeUnboxVmTextMessageDateFillColorGet);
        }
        if ((dirtyFlags & 0x3002L) != 0) {
            // api target 1

            this.ivAvatar.setVisibility(vmTextMessageIsSenderVisibleViewVISIBLEViewGONE);
            this.tvName.setVisibility(vmTextMessageIsSenderVisibleViewVISIBLEViewGONE);
        }
        if ((dirtyFlags & 0x2000L) != 0) {
            // api target 1

            com.ekoapp.ekosdk.uikit.components.BindingUtilityKt.setEkoViewBackgroundColor(this.ivAvatar, getColorFromResource(ivAvatar, R.color.amityColorPrimary), com.ekoapp.ekosdk.uikit.common.views.ColorShade.SHADE3);
            com.ekoapp.ekosdk.uikit.components.BindingUtilityKt.setEkoTextColor(this.tvName, com.ekoapp.ekosdk.uikit.common.views.ColorShade.SHADE1, (com.ekoapp.ekosdk.uikit.common.views.ColorShade)null);
            com.ekoapp.ekosdk.uikit.components.BindingUtilityKt.setEkoTextColor(this.tvTime, com.ekoapp.ekosdk.uikit.common.views.ColorShade.SHADE2, (com.ekoapp.ekosdk.uikit.common.views.ColorShade)null);
        }
        if ((dirtyFlags & 0x3000L) != 0) {
            // api target 1

            this.mboundView01.setViewModel(vmTextMessage);
        }
        if ((dirtyFlags & 0x3010L) != 0) {
            // api target 1

            this.mboundView1.setVisibility(vmTextMessageIsDeletedViewGONEViewVISIBLE);
        }
        if ((dirtyFlags & 0x3020L) != 0) {
            // api target 1

            this.mboundView6.setVisibility(vmTextMessageInSelectionModeViewVISIBLEViewGONE);
        }
        if ((dirtyFlags & 0x3200L) != 0) {
            // api target 1

            com.ekoapp.ekosdk.uikit.components.BindingUtilityKt.setRoundedCorner(this.tvMessageIncoming, true, tvMessageIncoming.getResources().getDimension(R.dimen.amity_zero), (java.lang.Float)null, (java.lang.Float)null, (java.lang.Float)null, vmTextMessageReceiverFillColorGet, (java.lang.Integer)null, (com.ekoapp.ekosdk.uikit.common.views.ColorShade)null);
        }
        if ((dirtyFlags & 0x3040L) != 0) {
            // api target 1

            com.ekoapp.ekosdk.uikit.components.BindingUtilityKt.setText(this.tvMessageIncoming, vmTextMessageTextGet, false);
        }
        if ((dirtyFlags & 0x2800L) != 0) {
            // api target 1

            com.ekoapp.ekosdk.uikit.components.BindingUtilityKt.setListener(this.tvMessageIncoming, lonPressListener, (java.lang.Integer)null);
        }
        if ((dirtyFlags & 0x3004L) != 0) {
            // api target 1

            androidx.databinding.adapters.TextViewBindingAdapter.setText(this.tvName, vmTextMessageSenderGet);
        }
        if ((dirtyFlags & 0x3100L) != 0) {
            // api target 1

            androidx.databinding.adapters.TextViewBindingAdapter.setText(this.tvTime, vmTextMessageMsgTimeGet);
        }
        executeBindingsOn(dateHeader);
        executeBindingsOn(mboundView01);
    }
    // Listener Stub Implementations
    // callback impls
    // dirty flag
    private  long mDirtyFlags = 0xffffffffffffffffL;
    /* flag mapping
        flag 0 (0x1L): vmTextMessage.dateFillColor
        flag 1 (0x2L): vmTextMessage.isSenderVisible()
        flag 2 (0x3L): vmTextMessage.sender
        flag 3 (0x4L): vmTextMessage.msgDate
        flag 4 (0x5L): vmTextMessage.isDeleted()
        flag 5 (0x6L): vmTextMessage.inSelectionMode
        flag 6 (0x7L): vmTextMessage.text
        flag 7 (0x8L): dateHeader
        flag 8 (0x9L): vmTextMessage.msgTime
        flag 9 (0xaL): vmTextMessage.receiverFillColor
        flag 10 (0xbL): vmTextMessage.isDateVisible()
        flag 11 (0xcL): lonPressListener
        flag 12 (0xdL): vmTextMessage
        flag 13 (0xeL): null
        flag 14 (0xfL): vmTextMessage.inSelectionMode.get() ? View.VISIBLE : View.GONE
        flag 15 (0x10L): vmTextMessage.inSelectionMode.get() ? View.VISIBLE : View.GONE
        flag 16 (0x11L): vmTextMessage.isSenderVisible().get() ? View.VISIBLE : View.GONE
        flag 17 (0x12L): vmTextMessage.isSenderVisible().get() ? View.VISIBLE : View.GONE
        flag 18 (0x13L): vmTextMessage.isDateVisible().get() ? View.VISIBLE : View.GONE
        flag 19 (0x14L): vmTextMessage.isDateVisible().get() ? View.VISIBLE : View.GONE
        flag 20 (0x15L): vmTextMessage.isDeleted().get() ? View.GONE : View.VISIBLE
        flag 21 (0x16L): vmTextMessage.isDeleted().get() ? View.GONE : View.VISIBLE
    flag mapping end*/
    //end
}