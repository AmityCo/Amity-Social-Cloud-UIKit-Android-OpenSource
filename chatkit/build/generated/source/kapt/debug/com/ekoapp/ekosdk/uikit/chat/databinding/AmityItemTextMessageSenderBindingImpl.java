package com.ekoapp.ekosdk.uikit.chat.databinding;
import com.ekoapp.ekosdk.uikit.chat.R;
import com.ekoapp.ekosdk.uikit.chat.BR;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import android.view.View;
@SuppressWarnings("unchecked")
public class AmityItemTextMessageSenderBindingImpl extends AmityItemTextMessageSenderBinding  {

    @Nullable
    private static final androidx.databinding.ViewDataBinding.IncludedLayouts sIncludes;
    @Nullable
    private static final android.util.SparseIntArray sViewsWithIds;
    static {
        sIncludes = new androidx.databinding.ViewDataBinding.IncludedLayouts(9);
        sIncludes.setIncludes(0, 
            new String[] {"amity_view_date", "amity_view_msg_deleted"},
            new int[] {6, 8},
            new int[] {com.ekoapp.ekosdk.uikit.chat.R.layout.amity_view_date,
                com.ekoapp.ekosdk.uikit.chat.R.layout.amity_view_msg_deleted});
        sIncludes.setIncludes(1, 
            new String[] {"amity_view_msg_error"},
            new int[] {7},
            new int[] {com.ekoapp.ekosdk.uikit.chat.R.layout.amity_view_msg_error});
        sViewsWithIds = null;
    }
    // views
    @NonNull
    private final androidx.constraintlayout.widget.ConstraintLayout mboundView0;
    @Nullable
    private final com.ekoapp.ekosdk.uikit.chat.databinding.AmityViewMsgDeletedBinding mboundView01;
    @NonNull
    private final androidx.constraintlayout.widget.ConstraintLayout mboundView1;
    @Nullable
    private final com.ekoapp.ekosdk.uikit.chat.databinding.AmityViewMsgErrorBinding mboundView11;
    @NonNull
    private final android.widget.TextView mboundView3;
    @NonNull
    private final android.widget.TextView mboundView5;
    // variables
    // values
    // listeners
    // Inverse Binding Event Handlers

    public AmityItemTextMessageSenderBindingImpl(@Nullable androidx.databinding.DataBindingComponent bindingComponent, @NonNull View root) {
        this(bindingComponent, root, mapBindings(bindingComponent, root, 9, sIncludes, sViewsWithIds));
    }
    private AmityItemTextMessageSenderBindingImpl(androidx.databinding.DataBindingComponent bindingComponent, View root, Object[] bindings) {
        super(bindingComponent, root, 12
            , (com.ekoapp.ekosdk.uikit.chat.databinding.AmityViewDateBinding) bindings[6]
            , (com.ekoapp.ekosdk.uikit.components.EkoReadMoreTextView) bindings[4]
            , (android.widget.TextView) bindings[2]
            );
        this.mboundView0 = (androidx.constraintlayout.widget.ConstraintLayout) bindings[0];
        this.mboundView0.setTag(null);
        this.mboundView01 = (com.ekoapp.ekosdk.uikit.chat.databinding.AmityViewMsgDeletedBinding) bindings[8];
        setContainedBinding(this.mboundView01);
        this.mboundView1 = (androidx.constraintlayout.widget.ConstraintLayout) bindings[1];
        this.mboundView1.setTag(null);
        this.mboundView11 = (com.ekoapp.ekosdk.uikit.chat.databinding.AmityViewMsgErrorBinding) bindings[7];
        setContainedBinding(this.mboundView11);
        this.mboundView3 = (android.widget.TextView) bindings[3];
        this.mboundView3.setTag(null);
        this.mboundView5 = (android.widget.TextView) bindings[5];
        this.mboundView5.setTag(null);
        this.tvMessageOutgoing.setTag(null);
        this.tvTimeOutgoing.setTag(null);
        setRootTag(root);
        // listeners
        invalidateAll();
    }

    @Override
    public void invalidateAll() {
        synchronized(this) {
                mDirtyFlags = 0x4000L;
        }
        dateHeader.invalidateAll();
        mboundView11.invalidateAll();
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
        if (mboundView11.hasPendingBindings()) {
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
            mDirtyFlags |= 0x1000L;
        }
        notifyPropertyChanged(BR.lonPressListener);
        super.requestRebind();
    }
    public void setVmTextMessage(@Nullable com.ekoapp.ekosdk.uikit.chat.messages.viewModel.EkoTextMessageViewModel VmTextMessage) {
        this.mVmTextMessage = VmTextMessage;
        synchronized(this) {
            mDirtyFlags |= 0x2000L;
        }
        notifyPropertyChanged(BR.vmTextMessage);
        super.requestRebind();
    }

    @Override
    public void setLifecycleOwner(@Nullable androidx.lifecycle.LifecycleOwner lifecycleOwner) {
        super.setLifecycleOwner(lifecycleOwner);
        dateHeader.setLifecycleOwner(lifecycleOwner);
        mboundView11.setLifecycleOwner(lifecycleOwner);
        mboundView01.setLifecycleOwner(lifecycleOwner);
    }

    @Override
    protected boolean onFieldChange(int localFieldId, Object object, int fieldId) {
        switch (localFieldId) {
            case 0 :
                return onChangeVmTextMessageDateFillColor((androidx.databinding.ObservableField<java.lang.Integer>) object, fieldId);
            case 1 :
                return onChangeVmTextMessageIsFailed((androidx.databinding.ObservableBoolean) object, fieldId);
            case 2 :
                return onChangeVmTextMessageEditedAt((androidx.databinding.ObservableField<java.lang.String>) object, fieldId);
            case 3 :
                return onChangeVmTextMessageMsgDate((androidx.databinding.ObservableField<java.lang.String>) object, fieldId);
            case 4 :
                return onChangeVmTextMessageSenderFillColor((androidx.databinding.ObservableField<java.lang.Integer>) object, fieldId);
            case 5 :
                return onChangeVmTextMessageIsDeleted((androidx.databinding.ObservableBoolean) object, fieldId);
            case 6 :
                return onChangeVmTextMessageInSelectionMode((androidx.databinding.ObservableBoolean) object, fieldId);
            case 7 :
                return onChangeVmTextMessageText((androidx.databinding.ObservableField<java.lang.String>) object, fieldId);
            case 8 :
                return onChangeDateHeader((com.ekoapp.ekosdk.uikit.chat.databinding.AmityViewDateBinding) object, fieldId);
            case 9 :
                return onChangeVmTextMessageMsgTime((androidx.databinding.ObservableField<java.lang.String>) object, fieldId);
            case 10 :
                return onChangeVmTextMessageIsEdited((androidx.databinding.ObservableBoolean) object, fieldId);
            case 11 :
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
    private boolean onChangeVmTextMessageIsFailed(androidx.databinding.ObservableBoolean VmTextMessageIsFailed, int fieldId) {
        if (fieldId == BR._all) {
            synchronized(this) {
                    mDirtyFlags |= 0x2L;
            }
            return true;
        }
        return false;
    }
    private boolean onChangeVmTextMessageEditedAt(androidx.databinding.ObservableField<java.lang.String> VmTextMessageEditedAt, int fieldId) {
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
    private boolean onChangeVmTextMessageSenderFillColor(androidx.databinding.ObservableField<java.lang.Integer> VmTextMessageSenderFillColor, int fieldId) {
        if (fieldId == BR._all) {
            synchronized(this) {
                    mDirtyFlags |= 0x10L;
            }
            return true;
        }
        return false;
    }
    private boolean onChangeVmTextMessageIsDeleted(androidx.databinding.ObservableBoolean VmTextMessageIsDeleted, int fieldId) {
        if (fieldId == BR._all) {
            synchronized(this) {
                    mDirtyFlags |= 0x20L;
            }
            return true;
        }
        return false;
    }
    private boolean onChangeVmTextMessageInSelectionMode(androidx.databinding.ObservableBoolean VmTextMessageInSelectionMode, int fieldId) {
        if (fieldId == BR._all) {
            synchronized(this) {
                    mDirtyFlags |= 0x40L;
            }
            return true;
        }
        return false;
    }
    private boolean onChangeVmTextMessageText(androidx.databinding.ObservableField<java.lang.String> VmTextMessageText, int fieldId) {
        if (fieldId == BR._all) {
            synchronized(this) {
                    mDirtyFlags |= 0x80L;
            }
            return true;
        }
        return false;
    }
    private boolean onChangeDateHeader(com.ekoapp.ekosdk.uikit.chat.databinding.AmityViewDateBinding DateHeader, int fieldId) {
        if (fieldId == BR._all) {
            synchronized(this) {
                    mDirtyFlags |= 0x100L;
            }
            return true;
        }
        return false;
    }
    private boolean onChangeVmTextMessageMsgTime(androidx.databinding.ObservableField<java.lang.String> VmTextMessageMsgTime, int fieldId) {
        if (fieldId == BR._all) {
            synchronized(this) {
                    mDirtyFlags |= 0x200L;
            }
            return true;
        }
        return false;
    }
    private boolean onChangeVmTextMessageIsEdited(androidx.databinding.ObservableBoolean VmTextMessageIsEdited, int fieldId) {
        if (fieldId == BR._all) {
            synchronized(this) {
                    mDirtyFlags |= 0x400L;
            }
            return true;
        }
        return false;
    }
    private boolean onChangeVmTextMessageIsDateVisible(androidx.databinding.ObservableBoolean VmTextMessageIsDateVisible, int fieldId) {
        if (fieldId == BR._all) {
            synchronized(this) {
                    mDirtyFlags |= 0x800L;
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
        boolean vmTextMessageIsFailedGet = false;
        java.lang.String vmTextMessageMsgTimeGet = null;
        androidx.databinding.ObservableField<java.lang.Integer> vmTextMessageDateFillColor = null;
        int androidxDatabindingViewDataBindingSafeUnboxVmTextMessageDateFillColorGet = 0;
        boolean vmTextMessageIsDateVisibleGet = false;
        androidx.databinding.ObservableBoolean vmTextMessageIsFailed = null;
        java.lang.String vmTextMessageTextGet = null;
        int vmTextMessageIsEditedViewVISIBLEViewGONE = 0;
        int vmTextMessageInSelectionModeViewVISIBLEViewGONE = 0;
        com.ekoapp.ekosdk.uikit.components.ILongPressListener lonPressListener = mLonPressListener;
        int vmTextMessageIsDateVisibleViewVISIBLEViewGONE = 0;
        androidx.databinding.ObservableField<java.lang.String> vmTextMessageEditedAt = null;
        com.ekoapp.ekosdk.uikit.chat.messages.viewModel.EkoTextMessageViewModel vmTextMessage = mVmTextMessage;
        androidx.databinding.ObservableField<java.lang.String> vmTextMessageMsgDate = null;
        boolean vmTextMessageIsEditedGet = false;
        java.lang.String vmTextMessageIsEditedVmTextMessageEditedAtVmTextMessageMsgTime = null;
        androidx.databinding.ObservableField<java.lang.Integer> vmTextMessageSenderFillColor = null;
        java.lang.String vmTextMessageEditedAtGet = null;
        androidx.databinding.ObservableBoolean vmTextMessageIsDeleted = null;
        androidx.databinding.ObservableBoolean vmTextMessageInSelectionMode = null;
        java.lang.Integer vmTextMessageSenderFillColorGet = null;
        boolean vmTextMessageInSelectionModeGet = false;
        androidx.databinding.ObservableField<java.lang.String> vmTextMessageText = null;
        androidx.databinding.ObservableField<java.lang.String> vmTextMessageMsgTime = null;
        androidx.databinding.ObservableBoolean vmTextMessageIsEdited = null;
        int vmTextMessageIsFailedViewVISIBLEViewGONE = 0;
        androidx.databinding.ObservableBoolean vmTextMessageIsDateVisible = null;
        int vmTextMessageIsDeletedViewGONEViewVISIBLE = 0;
        boolean vmTextMessageIsDeletedGet = false;

        if ((dirtyFlags & 0x5000L) != 0) {
        }
        if ((dirtyFlags & 0x6effL) != 0) {


            if ((dirtyFlags & 0x6001L) != 0) {

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
            if ((dirtyFlags & 0x6002L) != 0) {

                    if (vmTextMessage != null) {
                        // read vmTextMessage.isFailed()
                        vmTextMessageIsFailed = vmTextMessage.isFailed();
                    }
                    updateRegistration(1, vmTextMessageIsFailed);


                    if (vmTextMessageIsFailed != null) {
                        // read vmTextMessage.isFailed().get()
                        vmTextMessageIsFailedGet = vmTextMessageIsFailed.get();
                    }
                if((dirtyFlags & 0x6002L) != 0) {
                    if(vmTextMessageIsFailedGet) {
                            dirtyFlags |= 0x1000000L;
                    }
                    else {
                            dirtyFlags |= 0x800000L;
                    }
                }


                    // read vmTextMessage.isFailed().get() ? View.VISIBLE : View.GONE
                    vmTextMessageIsFailedViewVISIBLEViewGONE = ((vmTextMessageIsFailedGet) ? (android.view.View.VISIBLE) : (android.view.View.GONE));
            }
            if ((dirtyFlags & 0x6008L) != 0) {

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
            if ((dirtyFlags & 0x6010L) != 0) {

                    if (vmTextMessage != null) {
                        // read vmTextMessage.senderFillColor
                        vmTextMessageSenderFillColor = vmTextMessage.getSenderFillColor();
                    }
                    updateRegistration(4, vmTextMessageSenderFillColor);


                    if (vmTextMessageSenderFillColor != null) {
                        // read vmTextMessage.senderFillColor.get()
                        vmTextMessageSenderFillColorGet = vmTextMessageSenderFillColor.get();
                    }
            }
            if ((dirtyFlags & 0x6020L) != 0) {

                    if (vmTextMessage != null) {
                        // read vmTextMessage.isDeleted()
                        vmTextMessageIsDeleted = vmTextMessage.isDeleted();
                    }
                    updateRegistration(5, vmTextMessageIsDeleted);


                    if (vmTextMessageIsDeleted != null) {
                        // read vmTextMessage.isDeleted().get()
                        vmTextMessageIsDeletedGet = vmTextMessageIsDeleted.get();
                    }
                if((dirtyFlags & 0x6020L) != 0) {
                    if(vmTextMessageIsDeletedGet) {
                            dirtyFlags |= 0x4000000L;
                    }
                    else {
                            dirtyFlags |= 0x2000000L;
                    }
                }


                    // read vmTextMessage.isDeleted().get() ? View.GONE : View.VISIBLE
                    vmTextMessageIsDeletedViewGONEViewVISIBLE = ((vmTextMessageIsDeletedGet) ? (android.view.View.GONE) : (android.view.View.VISIBLE));
            }
            if ((dirtyFlags & 0x6040L) != 0) {

                    if (vmTextMessage != null) {
                        // read vmTextMessage.inSelectionMode
                        vmTextMessageInSelectionMode = vmTextMessage.getInSelectionMode();
                    }
                    updateRegistration(6, vmTextMessageInSelectionMode);


                    if (vmTextMessageInSelectionMode != null) {
                        // read vmTextMessage.inSelectionMode.get()
                        vmTextMessageInSelectionModeGet = vmTextMessageInSelectionMode.get();
                    }
                if((dirtyFlags & 0x6040L) != 0) {
                    if(vmTextMessageInSelectionModeGet) {
                            dirtyFlags |= 0x40000L;
                    }
                    else {
                            dirtyFlags |= 0x20000L;
                    }
                }


                    // read vmTextMessage.inSelectionMode.get() ? View.VISIBLE : View.GONE
                    vmTextMessageInSelectionModeViewVISIBLEViewGONE = ((vmTextMessageInSelectionModeGet) ? (android.view.View.VISIBLE) : (android.view.View.GONE));
            }
            if ((dirtyFlags & 0x6080L) != 0) {

                    if (vmTextMessage != null) {
                        // read vmTextMessage.text
                        vmTextMessageText = vmTextMessage.getText();
                    }
                    updateRegistration(7, vmTextMessageText);


                    if (vmTextMessageText != null) {
                        // read vmTextMessage.text.get()
                        vmTextMessageTextGet = vmTextMessageText.get();
                    }
            }
            if ((dirtyFlags & 0x6604L) != 0) {

                    if (vmTextMessage != null) {
                        // read vmTextMessage.isEdited()
                        vmTextMessageIsEdited = vmTextMessage.isEdited();
                    }
                    updateRegistration(10, vmTextMessageIsEdited);


                    if (vmTextMessageIsEdited != null) {
                        // read vmTextMessage.isEdited().get()
                        vmTextMessageIsEditedGet = vmTextMessageIsEdited.get();
                    }
                if((dirtyFlags & 0x6400L) != 0) {
                    if(vmTextMessageIsEditedGet) {
                            dirtyFlags |= 0x10000L;
                    }
                    else {
                            dirtyFlags |= 0x8000L;
                    }
                }
                if((dirtyFlags & 0x6604L) != 0) {
                    if(vmTextMessageIsEditedGet) {
                            dirtyFlags |= 0x400000L;
                    }
                    else {
                            dirtyFlags |= 0x200000L;
                    }
                }

                if ((dirtyFlags & 0x6400L) != 0) {

                        // read vmTextMessage.isEdited().get() ? View.VISIBLE : View.GONE
                        vmTextMessageIsEditedViewVISIBLEViewGONE = ((vmTextMessageIsEditedGet) ? (android.view.View.VISIBLE) : (android.view.View.GONE));
                }
            }
            if ((dirtyFlags & 0x6800L) != 0) {

                    if (vmTextMessage != null) {
                        // read vmTextMessage.isDateVisible()
                        vmTextMessageIsDateVisible = vmTextMessage.isDateVisible();
                    }
                    updateRegistration(11, vmTextMessageIsDateVisible);


                    if (vmTextMessageIsDateVisible != null) {
                        // read vmTextMessage.isDateVisible().get()
                        vmTextMessageIsDateVisibleGet = vmTextMessageIsDateVisible.get();
                    }
                if((dirtyFlags & 0x6800L) != 0) {
                    if(vmTextMessageIsDateVisibleGet) {
                            dirtyFlags |= 0x100000L;
                    }
                    else {
                            dirtyFlags |= 0x80000L;
                    }
                }


                    // read vmTextMessage.isDateVisible().get() ? View.VISIBLE : View.GONE
                    vmTextMessageIsDateVisibleViewVISIBLEViewGONE = ((vmTextMessageIsDateVisibleGet) ? (android.view.View.VISIBLE) : (android.view.View.GONE));
            }
        }
        // batch finished

        if ((dirtyFlags & 0x400000L) != 0) {

                if (vmTextMessage != null) {
                    // read vmTextMessage.editedAt
                    vmTextMessageEditedAt = vmTextMessage.getEditedAt();
                }
                updateRegistration(2, vmTextMessageEditedAt);


                if (vmTextMessageEditedAt != null) {
                    // read vmTextMessage.editedAt.get()
                    vmTextMessageEditedAtGet = vmTextMessageEditedAt.get();
                }
        }
        if ((dirtyFlags & 0x200000L) != 0) {

                if (vmTextMessage != null) {
                    // read vmTextMessage.msgTime
                    vmTextMessageMsgTime = vmTextMessage.getMsgTime();
                }
                updateRegistration(9, vmTextMessageMsgTime);


                if (vmTextMessageMsgTime != null) {
                    // read vmTextMessage.msgTime.get()
                    vmTextMessageMsgTimeGet = vmTextMessageMsgTime.get();
                }
        }

        if ((dirtyFlags & 0x6604L) != 0) {

                // read vmTextMessage.isEdited().get() ? vmTextMessage.editedAt.get() : vmTextMessage.msgTime.get()
                vmTextMessageIsEditedVmTextMessageEditedAtVmTextMessageMsgTime = ((vmTextMessageIsEditedGet) ? (vmTextMessageEditedAtGet) : (vmTextMessageMsgTimeGet));
        }
        // batch finished
        if ((dirtyFlags & 0x6800L) != 0) {
            // api target 1

            this.dateHeader.getRoot().setVisibility(vmTextMessageIsDateVisibleViewVISIBLEViewGONE);
        }
        if ((dirtyFlags & 0x6008L) != 0) {
            // api target 1

            this.dateHeader.setDate(vmTextMessageMsgDateGet);
        }
        if ((dirtyFlags & 0x6001L) != 0) {
            // api target 1

            this.dateHeader.setDateFillColor(androidxDatabindingViewDataBindingSafeUnboxVmTextMessageDateFillColorGet);
        }
        if ((dirtyFlags & 0x6000L) != 0) {
            // api target 1

            this.mboundView01.setViewModel(vmTextMessage);
            this.mboundView11.setViewModel(vmTextMessage);
        }
        if ((dirtyFlags & 0x6020L) != 0) {
            // api target 1

            this.mboundView1.setVisibility(vmTextMessageIsDeletedViewGONEViewVISIBLE);
        }
        if ((dirtyFlags & 0x6002L) != 0) {
            // api target 1

            this.mboundView11.getRoot().setVisibility(vmTextMessageIsFailedViewVISIBLEViewGONE);
        }
        if ((dirtyFlags & 0x6400L) != 0) {
            // api target 1

            this.mboundView3.setVisibility(vmTextMessageIsEditedViewVISIBLEViewGONE);
        }
        if ((dirtyFlags & 0x4000L) != 0) {
            // api target 1

            com.ekoapp.ekosdk.uikit.components.BindingUtilityKt.setEkoTextColor(this.mboundView3, com.ekoapp.ekosdk.uikit.common.views.ColorShade.SHADE2, (com.ekoapp.ekosdk.uikit.common.views.ColorShade)null);
            com.ekoapp.ekosdk.uikit.components.BindingUtilityKt.setRoundedCorner(this.mboundView5, true, (java.lang.Float)null, (java.lang.Float)null, mboundView5.getResources().getDimension(R.dimen.amity_zero), (java.lang.Float)null, (java.lang.Integer)null, (java.lang.Integer)null, (com.ekoapp.ekosdk.uikit.common.views.ColorShade)null);
            com.ekoapp.ekosdk.uikit.components.BindingUtilityKt.setEkoTextColor(this.tvTimeOutgoing, com.ekoapp.ekosdk.uikit.common.views.ColorShade.SHADE2, (com.ekoapp.ekosdk.uikit.common.views.ColorShade)null);
        }
        if ((dirtyFlags & 0x6040L) != 0) {
            // api target 1

            this.mboundView5.setVisibility(vmTextMessageInSelectionModeViewVISIBLEViewGONE);
        }
        if ((dirtyFlags & 0x6010L) != 0) {
            // api target 1

            com.ekoapp.ekosdk.uikit.components.BindingUtilityKt.setRoundedCorner(this.tvMessageOutgoing, true, (java.lang.Float)null, (java.lang.Float)null, tvMessageOutgoing.getResources().getDimension(R.dimen.amity_zero), (java.lang.Float)null, vmTextMessageSenderFillColorGet, (java.lang.Integer)null, (com.ekoapp.ekosdk.uikit.common.views.ColorShade)null);
        }
        if ((dirtyFlags & 0x6080L) != 0) {
            // api target 1

            com.ekoapp.ekosdk.uikit.components.BindingUtilityKt.setText(this.tvMessageOutgoing, vmTextMessageTextGet, true);
        }
        if ((dirtyFlags & 0x5000L) != 0) {
            // api target 1

            com.ekoapp.ekosdk.uikit.components.BindingUtilityKt.setListener(this.tvMessageOutgoing, lonPressListener, (java.lang.Integer)null);
        }
        if ((dirtyFlags & 0x6604L) != 0) {
            // api target 1

            androidx.databinding.adapters.TextViewBindingAdapter.setText(this.tvTimeOutgoing, vmTextMessageIsEditedVmTextMessageEditedAtVmTextMessageMsgTime);
        }
        executeBindingsOn(dateHeader);
        executeBindingsOn(mboundView11);
        executeBindingsOn(mboundView01);
    }
    // Listener Stub Implementations
    // callback impls
    // dirty flag
    private  long mDirtyFlags = 0xffffffffffffffffL;
    /* flag mapping
        flag 0 (0x1L): vmTextMessage.dateFillColor
        flag 1 (0x2L): vmTextMessage.isFailed()
        flag 2 (0x3L): vmTextMessage.editedAt
        flag 3 (0x4L): vmTextMessage.msgDate
        flag 4 (0x5L): vmTextMessage.senderFillColor
        flag 5 (0x6L): vmTextMessage.isDeleted()
        flag 6 (0x7L): vmTextMessage.inSelectionMode
        flag 7 (0x8L): vmTextMessage.text
        flag 8 (0x9L): dateHeader
        flag 9 (0xaL): vmTextMessage.msgTime
        flag 10 (0xbL): vmTextMessage.isEdited()
        flag 11 (0xcL): vmTextMessage.isDateVisible()
        flag 12 (0xdL): lonPressListener
        flag 13 (0xeL): vmTextMessage
        flag 14 (0xfL): null
        flag 15 (0x10L): vmTextMessage.isEdited().get() ? View.VISIBLE : View.GONE
        flag 16 (0x11L): vmTextMessage.isEdited().get() ? View.VISIBLE : View.GONE
        flag 17 (0x12L): vmTextMessage.inSelectionMode.get() ? View.VISIBLE : View.GONE
        flag 18 (0x13L): vmTextMessage.inSelectionMode.get() ? View.VISIBLE : View.GONE
        flag 19 (0x14L): vmTextMessage.isDateVisible().get() ? View.VISIBLE : View.GONE
        flag 20 (0x15L): vmTextMessage.isDateVisible().get() ? View.VISIBLE : View.GONE
        flag 21 (0x16L): vmTextMessage.isEdited().get() ? vmTextMessage.editedAt.get() : vmTextMessage.msgTime.get()
        flag 22 (0x17L): vmTextMessage.isEdited().get() ? vmTextMessage.editedAt.get() : vmTextMessage.msgTime.get()
        flag 23 (0x18L): vmTextMessage.isFailed().get() ? View.VISIBLE : View.GONE
        flag 24 (0x19L): vmTextMessage.isFailed().get() ? View.VISIBLE : View.GONE
        flag 25 (0x1aL): vmTextMessage.isDeleted().get() ? View.GONE : View.VISIBLE
        flag 26 (0x1bL): vmTextMessage.isDeleted().get() ? View.GONE : View.VISIBLE
    flag mapping end*/
    //end
}