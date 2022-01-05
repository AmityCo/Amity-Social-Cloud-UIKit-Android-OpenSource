package com.ekoapp.ekosdk.uikit.chat.databinding;
import com.ekoapp.ekosdk.uikit.chat.R;
import com.ekoapp.ekosdk.uikit.chat.BR;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import android.view.View;
@SuppressWarnings("unchecked")
public class AmityActivityEditMessageBindingImpl extends AmityActivityEditMessageBinding  {

    @Nullable
    private static final androidx.databinding.ViewDataBinding.IncludedLayouts sIncludes;
    @Nullable
    private static final android.util.SparseIntArray sViewsWithIds;
    static {
        sIncludes = new androidx.databinding.ViewDataBinding.IncludedLayouts(4);
        sIncludes.setIncludes(0, 
            new String[] {"amity_edit_msg_bar"},
            new int[] {3},
            new int[] {com.ekoapp.ekosdk.uikit.chat.R.layout.amity_edit_msg_bar});
        sViewsWithIds = null;
    }
    // views
    // variables
    // values
    // listeners
    // Inverse Binding Event Handlers
    private androidx.databinding.InverseBindingListener etMessageandroidTextAttrChanged = new androidx.databinding.InverseBindingListener() {
        @Override
        public void onChange() {
            // Inverse of viewModel.message.get()
            //         is viewModel.message.set((java.lang.String) callbackArg_0)
            java.lang.String callbackArg_0 = androidx.databinding.adapters.TextViewBindingAdapter.getTextString(etMessage);
            // localize variables for thread safety
            // viewModel.message
            androidx.databinding.ObservableField<java.lang.String> viewModelMessage = null;
            // viewModel
            com.ekoapp.ekosdk.uikit.chat.editMessage.EkoEditMessageViewModel viewModel = mViewModel;
            // viewModel.message != null
            boolean viewModelMessageJavaLangObjectNull = false;
            // viewModel.message.get()
            java.lang.String viewModelMessageGet = null;
            // viewModel != null
            boolean viewModelJavaLangObjectNull = false;



            viewModelJavaLangObjectNull = (viewModel) != (null);
            if (viewModelJavaLangObjectNull) {


                viewModelMessage = viewModel.getMessage();

                viewModelMessageJavaLangObjectNull = (viewModelMessage) != (null);
                if (viewModelMessageJavaLangObjectNull) {




                    viewModelMessage.set(((java.lang.String) (callbackArg_0)));
                }
            }
        }
    };

    public AmityActivityEditMessageBindingImpl(@Nullable androidx.databinding.DataBindingComponent bindingComponent, @NonNull View root) {
        this(bindingComponent, root, mapBindings(bindingComponent, root, 4, sIncludes, sViewsWithIds));
    }
    private AmityActivityEditMessageBindingImpl(androidx.databinding.DataBindingComponent bindingComponent, View root, Object[] bindings) {
        super(bindingComponent, root, 2
            , (android.view.View) bindings[1]
            , (com.ekoapp.ekosdk.uikit.chat.databinding.AmityEditMsgBarBinding) bindings[3]
            , (com.google.android.material.textfield.TextInputEditText) bindings[2]
            , (androidx.constraintlayout.widget.ConstraintLayout) bindings[0]
            );
        this.divider.setTag(null);
        this.etMessage.setTag(null);
        this.lMessage.setTag(null);
        setRootTag(root);
        // listeners
        invalidateAll();
    }

    @Override
    public void invalidateAll() {
        synchronized(this) {
                mDirtyFlags = 0x8L;
        }
        emToolBar.invalidateAll();
        requestRebind();
    }

    @Override
    public boolean hasPendingBindings() {
        synchronized(this) {
            if (mDirtyFlags != 0) {
                return true;
            }
        }
        if (emToolBar.hasPendingBindings()) {
            return true;
        }
        return false;
    }

    @Override
    public boolean setVariable(int variableId, @Nullable Object variable)  {
        boolean variableSet = true;
        if (BR.viewModel == variableId) {
            setViewModel((com.ekoapp.ekosdk.uikit.chat.editMessage.EkoEditMessageViewModel) variable);
        }
        else {
            variableSet = false;
        }
            return variableSet;
    }

    public void setViewModel(@Nullable com.ekoapp.ekosdk.uikit.chat.editMessage.EkoEditMessageViewModel ViewModel) {
        this.mViewModel = ViewModel;
        synchronized(this) {
            mDirtyFlags |= 0x4L;
        }
        notifyPropertyChanged(BR.viewModel);
        super.requestRebind();
    }

    @Override
    public void setLifecycleOwner(@Nullable androidx.lifecycle.LifecycleOwner lifecycleOwner) {
        super.setLifecycleOwner(lifecycleOwner);
        emToolBar.setLifecycleOwner(lifecycleOwner);
    }

    @Override
    protected boolean onFieldChange(int localFieldId, Object object, int fieldId) {
        switch (localFieldId) {
            case 0 :
                return onChangeViewModelMessage((androidx.databinding.ObservableField<java.lang.String>) object, fieldId);
            case 1 :
                return onChangeEmToolBar((com.ekoapp.ekosdk.uikit.chat.databinding.AmityEditMsgBarBinding) object, fieldId);
        }
        return false;
    }
    private boolean onChangeViewModelMessage(androidx.databinding.ObservableField<java.lang.String> ViewModelMessage, int fieldId) {
        if (fieldId == BR._all) {
            synchronized(this) {
                    mDirtyFlags |= 0x1L;
            }
            return true;
        }
        return false;
    }
    private boolean onChangeEmToolBar(com.ekoapp.ekosdk.uikit.chat.databinding.AmityEditMsgBarBinding EmToolBar, int fieldId) {
        if (fieldId == BR._all) {
            synchronized(this) {
                    mDirtyFlags |= 0x2L;
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
        androidx.databinding.ObservableField<java.lang.String> viewModelMessage = null;
        com.ekoapp.ekosdk.uikit.chat.editMessage.EkoEditMessageViewModel viewModel = mViewModel;
        java.lang.String viewModelMessageGet = null;

        if ((dirtyFlags & 0xdL) != 0) {



                if (viewModel != null) {
                    // read viewModel.message
                    viewModelMessage = viewModel.getMessage();
                }
                updateRegistration(0, viewModelMessage);


                if (viewModelMessage != null) {
                    // read viewModel.message.get()
                    viewModelMessageGet = viewModelMessage.get();
                }
        }
        // batch finished
        if ((dirtyFlags & 0x8L) != 0) {
            // api target 1

            com.ekoapp.ekosdk.uikit.components.BindingUtilityKt.setEkoViewBackgroundColor(this.divider, getColorFromResource(divider, R.color.amityColorBase), com.ekoapp.ekosdk.uikit.common.views.ColorShade.SHADE4);
            androidx.databinding.adapters.TextViewBindingAdapter.setTextWatcher(this.etMessage, (androidx.databinding.adapters.TextViewBindingAdapter.BeforeTextChanged)null, (androidx.databinding.adapters.TextViewBindingAdapter.OnTextChanged)null, (androidx.databinding.adapters.TextViewBindingAdapter.AfterTextChanged)null, etMessageandroidTextAttrChanged);
        }
        if ((dirtyFlags & 0xcL) != 0) {
            // api target 1

            this.emToolBar.setViewModel(viewModel);
        }
        if ((dirtyFlags & 0xdL) != 0) {
            // api target 1

            androidx.databinding.adapters.TextViewBindingAdapter.setText(this.etMessage, viewModelMessageGet);
        }
        executeBindingsOn(emToolBar);
    }
    // Listener Stub Implementations
    // callback impls
    // dirty flag
    private  long mDirtyFlags = 0xffffffffffffffffL;
    /* flag mapping
        flag 0 (0x1L): viewModel.message
        flag 1 (0x2L): emToolBar
        flag 2 (0x3L): viewModel
        flag 3 (0x4L): null
    flag mapping end*/
    //end
}