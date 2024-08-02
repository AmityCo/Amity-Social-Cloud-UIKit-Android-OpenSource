//package com.amity.socialcloud.uikit.community.compose.community.profile
//import androidx.compose.foundation.Image
//import androidx.compose.foundation.layout.*
//import androidx.compose.foundation.lazy.LazyColumn
//import androidx.compose.foundation.lazy.items
//import androidx.compose.material3.Icon
//import androidx.compose.material3.MaterialTheme
//import androidx.compose.material3.Tab
//import androidx.compose.material3.TabRow
//import androidx.compose.material3.Text
//import androidx.compose.runtime.*
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.graphics.Color
//import androidx.compose.ui.res.painterResource
//import androidx.compose.ui.tooling.preview.Preview
//import androidx.compose.ui.unit.dp
//import com.amity.socialcloud.uikit.community.compose.R
//import kotlinx.coroutines.launch
//
//@Composable
//fun CommunityProfilePage() {
//	val tabs = listOf("Tab1", "Tab2")
//	var selectedTabIndex by remember { mutableStateOf(0) }
//
//	LazyColumn(modifier = Modifier.fillMaxSize()) {
//		item {
//			CoverSections()
//			HeaderSection()
//			TabsSection(tabs, selectedTabIndex) { index ->
//				selectedTabIndex = index
//			}
//		}
//		items(50) { index ->
//			Text("Item $index", modifier = Modifier.padding(16.dp))
//		}
//	}
//}
//
//@Composable
//fun CoverSections() {
//	Image(
//		painter = painterResource(id = R.drawable.amity_ic_empty_newsfeed),
//		contentDescription = "Cover",
//		modifier = Modifier
//			.fillMaxWidth()
//			.height(188.dp)
//	)
//}
//
//@Composable
//fun HeaderSection() {
//	Column(modifier = Modifier.padding(16.dp)) {
//		Text(text = "Title", style = MaterialTheme.typography.titleLarge)
//		Text(text = "Description", style = MaterialTheme.typography.titleSmall)
//	}
//}
//
//@Composable
//fun TabsSection(tabs: List<String>, selectedIndex: Int, onSelect: (Int) -> Unit) {
//	TabRow(selectedTabIndex = selectedIndex) {
//		tabs.forEachIndexed { index, title ->
//			Tab(
//				text = { Icon(painterResource(id = R.drawable.amity_ic_moderator), contentDescription = title) },
//				selected = selectedIndex == index,
//				onClick = { onSelect(index) }
//			)
//		}
//	}
//}
