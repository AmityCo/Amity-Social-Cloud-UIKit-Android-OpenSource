package com.amity.socialcloud.uikit.community.compose.community.setup

import android.app.Activity
import android.net.Uri
import androidx.activity.compose.BackHandler
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.LocalViewModelStoreOwner
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.amity.socialcloud.sdk.model.core.file.AmityImage
import com.amity.socialcloud.sdk.model.core.user.AmityUser
import com.amity.socialcloud.uikit.common.common.views.AmityColorShade
import com.amity.socialcloud.uikit.common.config.AmityUIKitConfigController
import com.amity.socialcloud.uikit.common.ui.base.AmityBaseElement
import com.amity.socialcloud.uikit.common.ui.base.AmityBasePage
import com.amity.socialcloud.uikit.common.ui.elements.AmityAlertDialog
import com.amity.socialcloud.uikit.common.ui.elements.AmityTextField
import com.amity.socialcloud.uikit.common.ui.theme.AmityTheme
import com.amity.socialcloud.uikit.common.utils.AmityCameraUtil
import com.amity.socialcloud.uikit.common.utils.clickableWithoutRipple
import com.amity.socialcloud.uikit.common.utils.closePage
import com.amity.socialcloud.uikit.common.utils.closePageWithResult
import com.amity.socialcloud.uikit.common.utils.getIcon
import com.amity.socialcloud.uikit.common.utils.getText
import com.amity.socialcloud.uikit.common.utils.shade
import com.amity.socialcloud.uikit.common.utils.showToast
import com.amity.socialcloud.uikit.community.compose.AmitySocialBehaviorHelper
import com.amity.socialcloud.uikit.community.compose.R
import com.amity.socialcloud.uikit.community.compose.community.category.AmityCommunityAddCategoryPageActivity
import com.amity.socialcloud.uikit.community.compose.community.category.element.AmityCommunityCategoryList
import com.amity.socialcloud.uikit.community.compose.community.membership.add.AmityCommunityAddMemberPageActivity
import com.amity.socialcloud.uikit.community.compose.community.membership.element.AmityCommunityAddMemberList
import com.amity.socialcloud.uikit.community.compose.community.setup.elements.AmityMediaImageSelectionSheet
import com.amity.socialcloud.uikit.community.compose.community.setup.elements.AmityMediaImageSelectionType

@Composable
fun AmityCommunitySetupPage(
    modifier: Modifier = Modifier,
    mode: AmityCommunitySetupPageMode,
) {
    val context = LocalContext.current
    val behavior by lazy {
        AmitySocialBehaviorHelper.communitySetupPageBehavior
    }

    val isInEditMode by remember(mode) {
        mutableStateOf(mode is AmityCommunitySetupPageMode.Edit)
    }

    val communityToEdit by remember(isInEditMode) {
        derivedStateOf {
            if (isInEditMode) {
                (mode as AmityCommunitySetupPageMode.Edit).community
            } else {
                null
            }
        }
    }

    var name by remember(isInEditMode) {
        mutableStateOf(if (isInEditMode) communityToEdit?.getDisplayName() ?: "" else "")
    }
    var description by remember(isInEditMode) {
        mutableStateOf(if (isInEditMode) communityToEdit?.getDescription() ?: "" else "")
    }
    var isPublic by remember(isInEditMode) {
        mutableStateOf(if (isInEditMode) communityToEdit?.isPublic() != false else true)
    }

    val selectedCategories = remember(isInEditMode) {
        val categories = if (isInEditMode) {
            communityToEdit?.getCategories() ?: emptyList()
        } else {
            emptyList()
        }
        mutableStateListOf(*categories.toTypedArray())
    }

    val selectedUsers = remember {
        mutableStateListOf<AmityUser>()
    }

    val addCategoriesLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            result.data?.let {
                val categories = it.let(AmityCommunityAddCategoryPageActivity::getCategories)

                selectedCategories.clear()
                selectedCategories.addAll(categories)
            }
        }
    }

    val addMembersLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            result.data?.let {
                val users = it.let(AmityCommunityAddMemberPageActivity::getUsers)

                selectedUsers.clear()
                selectedUsers.addAll(users)
            }
        }
    }

    val viewModelStoreOwner = checkNotNull(LocalViewModelStoreOwner.current) {
        "No ViewModelStoreOwner was provided via LocalViewModelStoreOwner"
    }
    val viewModel =
        viewModel<AmityCommunitySetupPageViewModel>(viewModelStoreOwner = viewModelStoreOwner)

    var showLeaveConfirmDialog by remember { mutableStateOf(false) }
    var showMediaCameraSelectionSheet by remember { mutableStateOf(false) }
    var isCameraPermissionGranted by remember { mutableStateOf(false) }

    var isCapturedImageReady by remember { mutableStateOf(false) }
    var avatarUri by remember { mutableStateOf(Uri.EMPTY) }

    val imagePickerLauncher =
        rememberLauncherForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
            avatarUri = uri
            isCapturedImageReady = true
        }

    val imageCaptureLauncher =
        rememberLauncherForActivityResult(ActivityResultContracts.TakePicture()) { isSuccess ->
            isCapturedImageReady = isSuccess
        }

    val cameraPermissionLauncher =
        rememberLauncherForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permissions ->
            isCapturedImageReady = false
            isCameraPermissionGranted = permissions.entries.all { it.value }
            if (!isCameraPermissionGranted) {
                context.showToast("Camera permission not granted")
            } else {
                val imageFile = AmityCameraUtil.createImageFile(context)
                if (imageFile == null) {
                    context.showToast("Failed to create image file")
                } else {
                    avatarUri = AmityCameraUtil.createPhotoUri(context, imageFile)
                    imageCaptureLauncher.launch(avatarUri)
                }
            }
        }

    val shouldActionButtonEnable by remember(communityToEdit, isInEditMode) {
        derivedStateOf {
            if (isInEditMode) {
                val sc = selectedCategories.sortedWith(compareBy {
                    it.getCategoryId()
                }).map { it.getCategoryId() }

                val cc = communityToEdit?.getCategories()?.sortedWith(compareBy {
                    it.getCategoryId()
                })?.map { it.getCategoryId() } ?: emptyList()

                name != communityToEdit?.getDisplayName() ||
                        description != communityToEdit?.getDescription() ||
                        isPublic != communityToEdit?.isPublic() ||
                        sc != cc || avatarUri != Uri.EMPTY
            } else {
                name.isNotBlank()
            }
        }
    }

    BackHandler {
        showLeaveConfirmDialog = true
    }

    AmityBasePage(pageId = "community_setup_page") {
        Box(
            modifier = modifier
                .fillMaxSize()
                .windowInsetsPadding(WindowInsets.safeDrawing)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .background(AmityTheme.colors.background)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = modifier
                        .fillMaxWidth()
                        .padding(horizontal = 12.dp)
                ) {
                    AmityBaseElement(
                        pageScope = getPageScope(),
                        elementId = "close_button"
                    ) {
                        Icon(
                            painter = painterResource(id = getConfig().getIcon()),
                            contentDescription = "Close Button",
                            tint = AmityTheme.colors.base,
                            modifier = modifier
                                .size(16.dp)
                                .clickableWithoutRipple {
                                    showLeaveConfirmDialog = true
                                }
                                .testTag(getAccessibilityId())
                        )
                    }
                    AmityBaseElement(
                        pageScope = getPageScope(),
                        elementId = if (isInEditMode) "community_edit_title" else "title"
                    ) {
                        Text(
                            text = getConfig().getText(),
                            style = AmityTheme.typography.title,
                            modifier = modifier
                                .fillMaxWidth()
                                .padding(vertical = 16.dp)
                                .testTag(getAccessibilityId())
                        )
                    }
                }

                Box(
                    modifier = modifier
                        .aspectRatio(2f)
                        .background(AmityTheme.colors.baseShade3)
                        .background(Color.Black.copy(alpha = 0.5f))
                        .clickableWithoutRipple {
                            showMediaCameraSelectionSheet = true
                        }
                ) {
                    val avatar = when {
                        avatarUri != Uri.EMPTY && isCapturedImageReady -> avatarUri
                        isInEditMode -> communityToEdit?.getAvatar()?.getUrl(AmityImage.Size.LARGE)
                        else -> null
                    }

                    AsyncImage(
                        model = avatar,
                        contentDescription = "Avatar",
                        contentScale = ContentScale.Crop,
                        modifier = modifier.fillMaxWidth(),
                    )
                    Box(
                        modifier = modifier
                            .fillMaxSize()
                            .background(Color.Black.copy(alpha = 0.4f))
                    )
                    Icon(
                        painter = painterResource(R.drawable.amity_ic_camera2),
                        contentDescription = "Upload avatar",
                        tint = Color.White,
                        modifier = modifier
                            .size(28.dp)
                            .align(Alignment.Center)
                    )
                }

                Spacer(modifier = modifier.height(24.dp))
                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp)
                ) {
                    AmityBaseElement(
                        pageScope = getPageScope(),
                        elementId = "community_name_title"
                    ) {
                        Text(
                            text = getConfig().getText(),
                            style = AmityTheme.typography.title,
                            modifier = modifier.testTag(getAccessibilityId())
                        )
                    }

                    Text(
                        text = "${name.length}/30",
                        style = AmityTheme.typography.caption.copy(
                            fontWeight = FontWeight.Normal,
                            color = AmityTheme.colors.baseShade1,
                        )
                    )
                }

                AmityTextField(
                    maxCharacters = 30,
                    text = name,
                    hint = context.getString(R.string.amity_cc_hint),
                    onValueChange = { name = it },
                )

                HorizontalDivider(
                    color = AmityTheme.colors.divider,
                    modifier = modifier.padding(horizontal = 16.dp)
                )

                Spacer(modifier = modifier.height(24.dp))
                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        AmityBaseElement(
                            pageScope = getPageScope(),
                            elementId = "community_about_title"
                        ) {
                            Text(
                                text = getConfig().getText(),
                                style = AmityTheme.typography.title,
                                modifier = modifier.testTag(getAccessibilityId())
                            )
                        }
                        Text(
                            text = " (Optional)",
                            style = AmityTheme.typography.body.copy(
                                color = AmityTheme.colors.baseShade3,
                            )
                        )
                    }

                    Text(
                        text = "${description.length}/180",
                        style = AmityTheme.typography.caption.copy(
                            fontWeight = FontWeight.Normal,
                            color = AmityTheme.colors.baseShade1,
                        )
                    )
                }

                AmityTextField(
                    maxCharacters = 180,
                    text = description,
                    hint = context.getString(R.string.amity_enter_description),
                    onValueChange = { description = it },
                )

                HorizontalDivider(
                    color = AmityTheme.colors.divider,
                    modifier = modifier.padding(horizontal = 16.dp)
                )

                Spacer(modifier = modifier.height(24.dp))
                AmityBaseElement(
                    pageScope = getPageScope(),
                    elementId = "community_category_title"
                ) {
                    Text(
                        text = getConfig().getText(),
                        style = AmityTheme.typography.title,
                        modifier = modifier
                            .padding(horizontal = 16.dp)
                            .testTag(getAccessibilityId())
                    )
                }
                Spacer(modifier = modifier.height(18.dp))
                Box(
                    modifier = modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp)
                ) {
                    if (selectedCategories.isEmpty()) {
                        Text(
                            text = context.getString(R.string.amity_select_category),
                            style = AmityTheme.typography.body.copy(
                                color = AmityTheme.colors.baseShade3,
                            ),
                            modifier = modifier.clickableWithoutRipple {
                                behavior.goToAddCategoryPage(
                                    AmityCommunitySetupPageBehavior.Context(
                                        pageContext = context,
                                        launcher = addCategoriesLauncher,
                                        categories = selectedCategories,
                                    )
                                )
                            }
                        )
                    } else {
                        AmityCommunityCategoryList(
                            modifier = modifier.padding(end = 24.dp),
                            categories = selectedCategories,
                            onRemove = {
                                selectedCategories.remove(it)
                            }
                        )
                    }
                    Icon(
                        painter = painterResource(R.drawable.amity_ic_chevron_right),
                        contentDescription = "Select category",
                        tint = AmityTheme.colors.baseShade2,
                        modifier = modifier
                            .size(16.dp)
                            .align(Alignment.TopEnd)
                            .offset(
                                y = if (selectedCategories.isNotEmpty()) 8.dp else 0.dp
                            )
                            .clickableWithoutRipple {
                                behavior.goToAddCategoryPage(
                                    AmityCommunitySetupPageBehavior.Context(
                                        pageContext = context,
                                        launcher = addCategoriesLauncher,
                                        categories = selectedCategories,
                                    )
                                )
                            }
                    )
                }
                Spacer(modifier = modifier.height(16.dp))
                HorizontalDivider(
                    color = AmityTheme.colors.divider,
                    modifier = modifier.padding(horizontal = 16.dp)
                )

                Spacer(modifier = modifier.height(24.dp))
                AmityBaseElement(
                    pageScope = getPageScope(),
                    elementId = "community_privacy_title"
                ) {
                    Text(
                        text = getConfig().getText(),
                        style = AmityTheme.typography.title,
                        modifier = modifier
                            .padding(horizontal = 16.dp)
                            .testTag(getAccessibilityId())
                    )
                }
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                ) {
                    Box(
                        modifier = modifier
                            .clip(CircleShape)
                            .background(AmityTheme.colors.baseShade4)
                            .size(40.dp)
                    ) {
                        AmityBaseElement(
                            pageScope = getPageScope(),
                            elementId = "community_privacy_public_icon"
                        ) {
                            Icon(
                                painter = painterResource(getConfig().getIcon()),
                                contentDescription = "Public",
                                modifier = modifier
                                    .size(16.dp)
                                    .align(Alignment.Center)
                                    .testTag(getAccessibilityId())
                            )
                        }
                    }

                    Spacer(modifier.width(12.dp))
                    Column(
                        modifier = Modifier
                            .weight(1f)
                            .clickableWithoutRipple { isPublic = true }
                    ) {
                        AmityBaseElement(
                            pageScope = getPageScope(),
                            elementId = "community_privacy_public_title"
                        ) {
                            Text(
                                text = context.getString(R.string.amity_public),
                                style = AmityTheme.typography.body.copy(
                                    fontWeight = FontWeight.SemiBold
                                ),
                                modifier = modifier.testTag(getAccessibilityId())
                            )
                        }
                        Spacer(modifier.height(2.dp))
                        AmityBaseElement(
                            pageScope = getPageScope(),
                            elementId = "community_privacy_public_description"
                        ) {
                            Text(
                                text = getConfig().getText(),
                                style = AmityTheme.typography.caption.copy(
                                    color = AmityTheme.colors.baseShade1,
                                    fontWeight = FontWeight.Normal,
                                ),
                                modifier = modifier.testTag(getAccessibilityId())
                            )
                        }
                    }
                    RadioButton(
                        selected = isPublic,
                        colors = RadioButtonDefaults.colors(
                            selectedColor = AmityTheme.colors.highlight,
                            unselectedColor = AmityTheme.colors.baseShade2,
                        ),
                        onClick = {
                            isPublic = true
                        }
                    )
                }
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                ) {
                    Box(
                        modifier = modifier
                            .clip(CircleShape)
                            .background(AmityTheme.colors.baseShade4)
                            .size(40.dp)
                    ) {
                        AmityBaseElement(
                            pageScope = getPageScope(),
                            elementId = "community_privacy_private_icon"
                        ) {
                            Icon(
                                painter = painterResource(getConfig().getIcon()),
                                contentDescription = "Private",
                                modifier = modifier
                                    .size(16.dp)
                                    .align(Alignment.Center)
                                    .testTag(getAccessibilityId())
                            )
                        }
                    }

                    Spacer(modifier.width(12.dp))
                    Column(
                        modifier = Modifier
                            .weight(1f)
                            .clickableWithoutRipple { isPublic = false }
                    ) {
                        AmityBaseElement(
                            pageScope = getPageScope(),
                            elementId = "community_privacy_private_title"
                        ) {
                            Text(
                                text = context.getString(R.string.amity_private),
                                style = AmityTheme.typography.body.copy(
                                    fontWeight = FontWeight.SemiBold
                                ),
                                modifier = modifier.testTag(getAccessibilityId())
                            )
                        }
                        Spacer(modifier.height(2.dp))
                        AmityBaseElement(
                            pageScope = getPageScope(),
                            elementId = "community_privacy_private_description"
                        ) {
                            Text(
                                text = context.getString(R.string.amity_private_description),
                                style = AmityTheme.typography.caption.copy(
                                    color = AmityTheme.colors.baseShade1,
                                    fontWeight = FontWeight.Normal,
                                ),
                                modifier = modifier.testTag(getAccessibilityId())
                            )
                        }
                    }
                    RadioButton(
                        selected = !isPublic,
                        colors = RadioButtonDefaults.colors(
                            selectedColor = AmityTheme.colors.highlight,
                            unselectedColor = AmityTheme.colors.baseShade2,
                        ),
                        onClick = {
                            isPublic = false
                        }
                    )
                }

                if (!isPublic && !isInEditMode) {
                    Spacer(modifier = modifier.height(16.dp))
                    HorizontalDivider(
                        color = AmityTheme.colors.divider,
                        modifier = modifier.padding(horizontal = 16.dp)
                    )

                    Spacer(modifier = modifier.height(24.dp))
                    AmityBaseElement(
                        pageScope = getPageScope(),
                        elementId = "community_add_member_title"
                    ) {
                        Text(
                            text = getConfig().getText(),
                            style = AmityTheme.typography.title,
                            modifier = modifier
                                .padding(horizontal = 16.dp)
                                .testTag(getAccessibilityId())
                        )
                    }
                    Spacer(modifier = modifier.height(16.dp))
                    AmityCommunityAddMemberList(
                        pageScope = getPageScope(),
                        users = selectedUsers,
                        modifier = modifier.padding(horizontal = 16.dp),
                        onAddAction = {
                            behavior.goToAddMemberPage(
                                AmityCommunitySetupPageBehavior.Context(
                                    pageContext = context,
                                    launcher = addMembersLauncher,
                                    users = selectedUsers,
                                )
                            )
                        },
                        onRemoveAction = {
                            selectedUsers.find {
                                it.getUserId() == it.getUserId()
                            }.let(selectedUsers::remove)
                        }
                    )
                }
                Spacer(modifier.height(96.dp))
            }

            Column(
                modifier = modifier
                    .align(Alignment.BottomCenter)
                    .background(AmityTheme.colors.background)
            ) {
                Spacer(modifier = modifier.height(16.dp))
                HorizontalDivider(
                    color = AmityTheme.colors.divider,
                    modifier = modifier.padding(horizontal = 16.dp)
                )

                Spacer(modifier = modifier.height(16.dp))
                Button(
                    colors = ButtonDefaults.buttonColors(
                        containerColor = AmityTheme.colors.highlight,
                        disabledContainerColor = AmityTheme.colors.highlight.shade(AmityColorShade.SHADE2),
                    ),
                    shape = RoundedCornerShape(4.dp),
                    contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp),
                    enabled = shouldActionButtonEnable,
                    modifier = modifier
                        .height(40.dp)
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    onClick = {
                        if (isInEditMode) {
                            getPageScope().showProgressSnackbar("Updating...")
                            viewModel.editCommunity(
                                communityId = communityToEdit?.getCommunityId() ?: "",
                                avatarUri = avatarUri,
                                displayName = name,
                                description = description,
                                isPublic = isPublic,
                                categoryIds = selectedCategories.map { it.getCategoryId() },
                                onSuccess = {
                                    context.showToast("Successfully updated community profile!")
                                    context.closePageWithResult(Activity.RESULT_OK)
                                },
                                onError = {
                                    context.showToast("Failed to save your community profile. Please try again.")
                                    context.closePageWithResult(Activity.RESULT_OK)
                                }
                            )
                        } else {
                            getPageScope().showProgressSnackbar("Creating...")
                            viewModel.createCommunity(
                                avatarUri = avatarUri,
                                displayName = name,
                                description = description,
                                isPublic = isPublic,
                                categoryIds = selectedCategories.map { it.getCategoryId() },
                                userIds = selectedUsers.map { it.getUserId() },
                                onSuccess = {
                                    context.showToast("Successfully created community!")
                                    context.closePageWithResult(Activity.RESULT_OK)
                                    behavior.goToCommunityProfilePage(
                                        AmityCommunitySetupPageBehavior.Context(
                                            pageContext = context,
                                            communityId = it.getCommunityId(),
                                        )
                                    )
                                },
                                onError = {
                                    context.showToast("Failed to create community. Please try again.")
                                }
                            )
                        }
                    }
                ) {
                    AmityBaseElement(
                        pageScope = getPageScope(),
                        elementId = if (isInEditMode) "community_edit_button" else "community_create_button"
                    ) {
                        Icon(
                            painter = painterResource(getConfig().getIcon()),
                            contentDescription = "Create",
                            tint = Color.White,
                            modifier = modifier.size(16.dp)
                        )
                        Spacer(modifier = modifier.width(8.dp))
                        Text(
                            text = getConfig().getText(),
                            style = AmityTheme.typography.caption.copy(
                                color = Color.White,
                            ),
                            modifier = modifier.testTag(getAccessibilityId())
                        )
                    }
                }
                Spacer(modifier = modifier.height(16.dp))
            }
        }

        if (showMediaCameraSelectionSheet) {
            AmityMediaImageSelectionSheet(
                modifier = modifier,
                pageScope = getPageScope(),
            ) { type ->
                showMediaCameraSelectionSheet = false

                type?.let {
                    when (type) {
                        AmityMediaImageSelectionType.CAMERA -> {
                            val permissions = arrayOf(
                                android.Manifest.permission.CAMERA,
                            )
                            cameraPermissionLauncher.launch(permissions)
                        }

                        AmityMediaImageSelectionType.IMAGE -> {
                            imagePickerLauncher.launch(PickVisualMediaRequest(mediaType = ActivityResultContracts.PickVisualMedia.ImageOnly))
                        }
                    }
                }
            }
        }

        if (showLeaveConfirmDialog) {
            if (shouldActionButtonEnable) {
                if (isInEditMode) {
                    AmityAlertDialog(
                        dialogTitle = context.getString(R.string.amity_ec_leave_title),
                        dialogText = context.getString(R.string.amity_ec_leave_description),
                        confirmText = context.getString(R.string.amity_discard),
                        dismissText = context.getString(R.string.amity_cancel),
                        confirmTextColor = AmityTheme.colors.alert,
                        onConfirmation = {
                            context.closePage()
                        },
                        onDismissRequest = {
                            showLeaveConfirmDialog = false
                        }
                    )
                } else {
                    AmityAlertDialog(
                        dialogTitle = context.getString(R.string.amity_cc_leave_title),
                        dialogText = context.getString(R.string.amity_cc_leave_description),
                        confirmText = context.getString(R.string.amity_leave),
                        dismissText = context.getString(R.string.amity_cancel),
                        confirmTextColor = AmityTheme.colors.alert,
                        onConfirmation = {
                            context.closePage()
                        },
                        onDismissRequest = {
                            showLeaveConfirmDialog = false
                        }
                    )
                }
            } else {
                context.closePage()
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun AmityCommunitySetupPagePreview() {
    AmityUIKitConfigController.setup(LocalContext.current)
    AmityCommunitySetupPage(
        mode = AmityCommunitySetupPageMode.Create
    )
}