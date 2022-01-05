package com.ekoapp.ekosdk.uikit.community.databinding;
import com.ekoapp.ekosdk.uikit.community.R;
import com.ekoapp.ekosdk.uikit.community.BR;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import android.view.View;
@SuppressWarnings("unchecked")
public class AmityFragmentEditCommentBindingImpl extends AmityFragmentEditCommentBinding  {

    @Nullable
    private static final androidx.databinding.ViewDataBinding.IncludedLayouts sIncludes;
    @Nullable
    private static final android.util.SparseIntArray sViewsWithIds;
    static {
        sIncludes = null;
        sViewsWithIds = null;
    }
    // views
    @NonNull
    private final androidx.constraintlayout.widget.ConstraintLayout mboundView0;
    // variables
    // values
    // listeners
    // Inverse Binding Event Handlers
    private androidx.databinding.InverseBindingListener etPostCommentandroidTextAttrChanged = new androidx.databinding.InverseBindingListener() {
        @Override
        public void onChange() {
            // Inverse of vm.commentText.getValue()
            //         is vm.commentText.setValue((java.lang.String) callbackArg_0)
            java.lang.String callbackArg_0 = androidx.databinding.adapters.TextViewBindingAdapter.getTextString(etPostComment);
            // localize variables for thread safety
            // vm != null
            boolean vmJavaLangObjectNull = false;
            // vm
            com.ekoapp.ekosdk.uikit.community.newsfeed.viewmodel.EkoEditCommentViewModel vm = mVm;
            // vm.commentText.getValue()
            java.lang.String vmCommentTextGetValue = null;
            // vm.commentText
            androidx.lifecycle.MutableLiveData<java.lang.String> vmCommentText = null;
            // vm.commentText != null
            boolean vmCommentTextJavaLangObjectNull = false;



            vmJavaLangObjectNull = (vm) != (null);
            if (vmJavaLangObjectNull) {


                vmCommentText = vm.getCommentText();

                vmCommentTextJavaLangObjectNull = (vmCommentText) != (null);
                if (vmCommentTextJavaLangObjectNull) {




                    vmCommentText.setValue(((java.lang.String) (callbackArg_0)));
                }
            }
        }
    };

    public AmityFragmentEditCommentBindingImpl(@Nullable androidx.databinding.DataBindingComponent bindingComponent, @NonNull View root) {
        this(bindingComponent, root, mapBindings(bindingComponent, root, 3, sIncludes, sViewsWithIds));
    }
    private AmityFragmentEditCommentBindingImpl(androidx.databinding.DataBindingComponent bindingComponent, View root, Object[] bindings) {
        super(bindingComponent, root, 1
            , (com.google.android.material.textfield.TextInputEditText) bindings[2]
            , (android.widget.TextView) bindings[1]
            );
        this.etPostComment.setTag(null);
        this.mboundView0 = (androidx.constraintlayout.widget.ConstraintLayout) bindings[0];
        this.mboundView0.setTag(null);
        this.textviewReplyTo.setTag(null);
        setRootTag(root);
        // listeners
        invalidateAll();
    }

    @Override
    public void invalidateAll() {
        synchronized(this) {
                mDirtyFlags = 0x10L;
        }
        requestRebind();
    }

    @Override
    public boolean hasPendingBindings() {
        synchronized(this) {
            if (mDirtyFlags != 0) {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean setVariable(int variableId, @Nullable Object variable)  {
        boolean variableSet = true;
        if (BR.replyingToUser == variableId) {
            setReplyingToUser((java.lang.String) variable);
        }
        else if (BR.vm == variableId) {
            setVm((com.ekoapp.ekosdk.uikit.community.newsfeed.viewmodel.EkoEditCommentViewModel) variable);
        }
        else if (BR.showReplyingTo == variableId) {
            setShowReplyingTo((java.lang.Boolean) variable);
        }
        else {
            variableSet = false;
        }
            return variableSet;
    }

    public void setReplyingToUser(@Nullable java.lang.String ReplyingToUser) {
        this.mReplyingToUser = ReplyingToUser;
        synchronized(this) {
            mDirtyFlags |= 0x2L;
        }
        notifyPropertyChanged(BR.replyingToUser);
        super.requestRebind();
    }
    public void setVm(@Nullable com.ekoapp.ekosdk.uikit.community.newsfeed.viewmodel.EkoEditCommentViewModel Vm) {
        this.mVm = Vm;
        synchronized(this) {
            mDirtyFlags |= 0x4L;
        }
        notifyPropertyChanged(BR.vm);
        super.requestRebind();
    }
    public void setShowReplyingTo(@Nullable java.lang.Boolean ShowReplyingTo) {
        this.mShowReplyingTo = ShowReplyingTo;
        synchronized(this) {
            mDirtyFlags |= 0x8L;
        }
        notifyPropertyChanged(BR.showReplyingTo);
        super.requestRebind();
    }

    @Override
    protected boolean onFieldChange(int localFieldId, Object object, int fieldId) {
        switch (localFieldId) {
            case 0 :
                return onChangeVmCommentText((androidx.lifecycle.MutableLiveData<java.lang.String>) object, fieldId);
        }
        return false;
    }
    private boolean onChangeVmCommentText(androidx.lifecycle.MutableLiveData<java.lang.String> VmCommentText, int fieldId) {
        if (fieldId == BR._all) {
            synchronized(this) {
                    mDirtyFlags |= 0x1L;
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
        java.lang.String vmCommentTextGetValue = null;
        java.lang.String replyingToUser = mReplyingToUser;
        int showReplyingToViewVISIBLEViewGONE = 0;
        boolean androidxDatabindingViewDataBindingSafeUnboxShowReplyingTo = false;
        com.ekoapp.ekosdk.uikit.community.newsfeed.viewmodel.EkoEditCommentViewModel vm = mVm;
        java.lang.String stringFormatTextviewReplyToAndroidStringAmityReplyingToReplyingToUser = null;
        androidx.lifecycle.MutableLiveData<java.lang.String> vmCommentText = null;
        java.lang.Boolean showReplyingTo = mShowReplyingTo;

        if ((dirtyFlags & 0x12L) != 0) {



                // read String.format(@android:string/amity_replying_to, replyingToUser)
                stringFormatTextviewReplyToAndroidStringAmityReplyingToReplyingToUser = java.lang.String.format(textviewReplyTo.getResources().getString(R.string.amity_replying_to), replyingToUser);
        }
        if ((dirtyFlags & 0x15L) != 0) {



                if (vm != null) {
                    // read vm.commentText
                    vmCommentText = vm.getCommentText();
                }
                updateLiveDataRegistration(0, vmCommentText);


                if (vmCommentText != null) {
                    // read vm.commentText.getValue()
                    vmCommentTextGetValue = vmCommentText.getValue();
                }
        }
        if ((dirtyFlags & 0x18L) != 0) {



                // read androidx.databinding.ViewDataBinding.safeUnbox(showReplyingTo)
                androidxDatabindingViewDataBindingSafeUnboxShowReplyingTo = androidx.databinding.ViewDataBinding.safeUnbox(showReplyingTo);
            if((dirtyFlags & 0x18L) != 0) {
                if(androidxDatabindingViewDataBindingSafeUnboxShowReplyingTo) {
                        dirtyFlags |= 0x40L;
                }
                else {
                        dirtyFlags |= 0x20L;
                }
            }


                // read androidx.databinding.ViewDataBinding.safeUnbox(showReplyingTo) ? View.VISIBLE : View.GONE
                showReplyingToViewVISIBLEViewGONE = ((androidxDatabindingViewDataBindingSafeUnboxShowReplyingTo) ? (android.view.View.VISIBLE) : (android.view.View.GONE));
        }
        // batch finished
        if ((dirtyFlags & 0x15L) != 0) {
            // api target 1

            androidx.databinding.adapters.TextViewBindingAdapter.setText(this.etPostComment, vmCommentTextGetValue);
        }
        if ((dirtyFlags & 0x10L) != 0) {
            // api target 1

            androidx.databinding.adapters.TextViewBindingAdapter.setTextWatcher(this.etPostComment, (androidx.databinding.adapters.TextViewBindingAdapter.BeforeTextChanged)null, (androidx.databinding.adapters.TextViewBindingAdapter.OnTextChanged)null, (androidx.databinding.adapters.TextViewBindingAdapter.AfterTextChanged)null, etPostCommentandroidTextAttrChanged);
            com.ekoapp.ekosdk.uikit.components.BindingUtilityKt.setEkoTextColor(this.etPostComment, (com.ekoapp.ekosdk.uikit.common.views.ColorShade)null, com.ekoapp.ekosdk.uikit.common.views.ColorShade.SHADE2);
        }
        if ((dirtyFlags & 0x12L) != 0) {
            // api target 1

            androidx.databinding.adapters.TextViewBindingAdapter.setText(this.textviewReplyTo, stringFormatTextviewReplyToAndroidStringAmityReplyingToReplyingToUser);
        }
        if ((dirtyFlags & 0x18L) != 0) {
            // api target 1

            this.textviewReplyTo.setVisibility(showReplyingToViewVISIBLEViewGONE);
        }
    }
    // Listener Stub Implementations
    // callback impls
    // dirty flag
    private  long mDirtyFlags = 0xffffffffffffffffL;
    /* flag mapping
        flag 0 (0x1L): vm.commentText
        flag 1 (0x2L): replyingToUser
        flag 2 (0x3L): vm
        flag 3 (0x4L): showReplyingTo
        flag 4 (0x5L): null
        flag 5 (0x6L): androidx.databinding.ViewDataBinding.safeUnbox(showReplyingTo) ? View.VISIBLE : View.GONE
        flag 6 (0x7L): androidx.databinding.ViewDataBinding.safeUnbox(showReplyingTo) ? View.VISIBLE : View.GONE
    flag mapping end*/
    //end
}