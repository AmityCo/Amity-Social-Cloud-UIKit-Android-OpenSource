package com.amity.socialcloud.uikit.community.compose.search.global

import com.amity.socialcloud.uikit.common.config.AmityUIKitDataGate
import com.amity.socialcloud.uikit.common.config.AmityUIKitFeature
import com.amity.socialcloud.uikit.community.compose.localization.amitySocialString
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.LocalViewModelStoreOwner
import androidx.lifecycle.viewmodel.compose.viewModel
import com.amity.socialcloud.uikit.common.ui.base.AmityBaseComponent
import com.amity.socialcloud.uikit.common.ui.base.AmityBasePage
import com.amity.socialcloud.uikit.common.ui.elements.AmityTabRow
import com.amity.socialcloud.uikit.common.ui.elements.AmityTabRowItem
import com.amity.socialcloud.uikit.community.compose.search.components.AmityCommunitySearchResultComponent
import com.amity.socialcloud.uikit.community.compose.search.components.AmityPostSearchResultComponent
import com.amity.socialcloud.uikit.community.compose.search.components.AmityTopSearchBarComponent
import com.amity.socialcloud.uikit.community.compose.search.components.AmityUserSearchResultComponent

@Composable
fun AmitySocialGlobalSearchPage(
    modifier: Modifier = Modifier,
    prefilledText: String? = null,
) {
    val context = LocalContext.current

    val viewModelStoreOwner = checkNotNull(LocalViewModelStoreOwner.current) {
        "No ViewModelStoreOwner was provided via LocalViewModelStoreOwner"
    }
    val viewModel =
        viewModel<AmityGlobalSearchViewModel>(viewModelStoreOwner = viewModelStoreOwner)

    // A tab searching for something this build cannot display is an offer the app
    // cannot keep: with `post` off the Posts tab opened selected and permanently
    // empty. Users has no module of its own beyond `discovery`, which owns the
    // whole page, so it is always here.
    val searchTypes = remember {
        listOfNotNull(
            AmityGlobalSearchType.POST
                .takeIf { AmityUIKitDataGate.isOn(AmityUIKitFeature.POST) },
            AmityGlobalSearchType.COMMUNITY
                .takeIf { AmityUIKitDataGate.isOn(AmityUIKitFeature.COMMUNITY) },
            AmityGlobalSearchType.USER,
        )
    }
    val tabs = searchTypes.map { type ->
        AmityTabRowItem(
            title = when (type) {
                AmityGlobalSearchType.POST -> amitySocialString("amity_social_tab_tab_posts")
                AmityGlobalSearchType.COMMUNITY -> amitySocialString("amity_social_tab_tab_communities")
                // MY_COMMUNITY belongs to the my-communities search page, and is
                // never in this list.
                else -> amitySocialString("amity_social_tab_tab_users")
            }
        )
    }
    var selectedTabIndex by remember { mutableIntStateOf(0) }

    // Keyed on the type, not the position: dropping a tab shifts every index
    // after it, and an index-based `when` would then open the wrong pane.
    val selectedType = searchTypes.getOrElse(selectedTabIndex) { searchTypes.first() }

    LaunchedEffect(selectedType) {
        viewModel.setSearchType(selectedType)
    }

    AmityBasePage(
        pageId = "social_global_search_page"
    ) {
        Column(
            modifier = modifier.fillMaxSize()
        ) {
            AmityTopSearchBarComponent(
                modifier = modifier,
                pageScope = getPageScope(),
                viewModel = viewModel,
                prefilledText = prefilledText ?: "",
                shouldShowKeyboard = true
            )

            AmityTabRow(
                tabs = tabs,
                selectedIndex = selectedTabIndex
            ) {
                selectedTabIndex = it
            }

            Spacer(modifier.height(8.dp))

            when (selectedType) {
                AmityGlobalSearchType.POST -> {
                    AmityPostSearchResultComponent(
                        modifier = modifier,
                        pageScope = getPageScope(),
                        viewModel = viewModel
                    )
                }

                AmityGlobalSearchType.COMMUNITY -> {
                    AmityBaseComponent(
                        pageScope = getPageScope(),
                        componentId = "community_search_result"
                    ) {
                        AmityCommunitySearchResultComponent(
                            modifier = modifier,
                            pageScope = getPageScope(),
                            componentScope = getComponentScope(),
                            viewModel = viewModel,
                        )
                    }
                }

                else -> AmityUserSearchResultComponent(
                    modifier = modifier,
                    pageScope = getPageScope(),
                    viewModel = viewModel
                )
            }
        }
    }
}