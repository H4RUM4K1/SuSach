package com.mad.susach.navigation

import android.content.Intent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.google.firebase.auth.FirebaseAuth
import com.mad.susach.auth.login.ui.LoginContent
import com.mad.susach.auth.register.ui.RegisterScreen
import com.mad.susach.auth.login.ui.LoginActivity
import com.mad.susach.profile.ui.ProfileScreen
import com.mad.susach.profile.ui.ProfileViewModel
import com.mad.susach.timeline.ui.eralselection.EraSelectionScreen
import com.mad.susach.timeline.ui.timelineview.TimelineScreen
import com.mad.susach.article.ui.ArticleView
import com.mad.susach.search.ui.SearchScreen
import com.mad.susach.saved.ui.SavedPostsActivity


sealed class Screen(val route: String) {
    data object Login : Screen("login")
    data object Register : Screen("register")

    data object Home : Screen("home")
    data object Practice : Screen("practice")
    data object Profile : Screen("profile")

    data object Search : Screen("search_screen/{query}") {
        fun createRoute(query: String) = "search_screen/$query"
    }
    data object EraSelection : Screen("era_selection_screen")
    data object TimelineView : Screen("timeline_view_screen/{eraId}") {
        fun createRoute(eraId: String) = "timeline_view_screen/$eraId"
    }
    data object RandomArticle : Screen("random_article_screen")
    data object ArticleDetail : Screen("article_detail_screen/{articleId}") {
        fun createRoute(articleId: String) = "article_detail_screen/$articleId"
    }
    
    data object SavedPosts : Screen("saved_posts")
}

@Composable
fun AppNavigation() {
    val navController: NavHostController = rememberNavController()
    val isLoggedIn = FirebaseAuth.getInstance().currentUser != null

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route
    val currentEntry = navController.currentBackStackEntryAsState().value

    val searchQueryState = remember { mutableStateOf("") }
    val savedQuery = currentEntry?.savedStateHandle?.get<String>("searchQuery")
    if (savedQuery != null && savedQuery != searchQueryState.value) {
        searchQueryState.value = savedQuery
        currentEntry.savedStateHandle.remove<String>("searchQuery")
    }

    val showBottomBarOnRoutes = setOf(
        Screen.Home.route,
        Screen.Practice.route,
        Screen.Profile.route
    )

    Scaffold(
        bottomBar = {
            if (currentRoute in showBottomBarOnRoutes) {
                MainBottomNavBar(navController = navController)
            }
        }
    ) {
        NavHost(
            navController = navController,
            startDestination = if (isLoggedIn) Screen.Home.route else Screen.Login.route,
        ) {
            composable(Screen.Login.route) {
                LoginContent(
                    onLoginSuccess = {
                        navController.navigate(Screen.Home.route) {
                            popUpTo(Screen.Login.route) { inclusive = true }
                        }
                    },
                    onRegisterClick = {
                        navController.navigate(Screen.Register.route)
                    }
                )
            }
        composable(Screen.Register.route) {
            RegisterScreen(
                onRegisterSuccess = {
                    navController.navigate(Screen.Home.route) {
                        popUpTo(Screen.Login.route) { inclusive = true }
                    }
                },
                onLoginClick = {
                    navController.navigateUp()
                }
            )
        }
            composable(Screen.Home.route) {
                HomeScreen(
                    username = "GuestGuest",
                    notificationCount = 0,
                    onNotificationClick = { /* TODO */ },
                    onSearchClick = { query ->
                        navController.navigate(Screen.Search.createRoute(query))
                    },
                    navController = navController
                )
            }
            composable(Screen.Practice.route) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text("Practice Screen Content (Placeholder)")
                }
            }
            composable(Screen.Profile.route) {
                val profileViewModel: ProfileViewModel = viewModel()
                val context = LocalContext.current
                ProfileScreen(
                    viewModel = profileViewModel,
                    onLogout = {
                        profileViewModel.logout()
                        navController.navigate(Screen.Login.route) {
                            popUpTo(navController.graph.id) { inclusive = true }
                        }
                    },
                    onNavigateToSavedPosts = {
                        context.startActivity(Intent(context, SavedPostsActivity::class.java))
                    }
                )
            }
            composable(Screen.EraSelection.route) {
                EraSelectionScreen(
                    onBack = { navController.popBackStack() },
                    navToTimeline = { eraId ->
                        navController.navigate(Screen.TimelineView.createRoute(eraId))
                    }
                )
            }
            composable(
                route = Screen.TimelineView.route,
                arguments = listOf(navArgument("eraId") { type = NavType.StringType })
            ) { backStackEntry ->
                val eraId = backStackEntry.arguments?.getString("eraId") ?: ""
                TimelineScreen(
                    eraId = eraId,
                    navToArticle = { eventId: String ->
                        navController.navigate(Screen.ArticleDetail.createRoute(eventId))
                    },
                    onBack = { navController.popBackStack() }
                )
            }
            composable(
                route = Screen.ArticleDetail.route,
                arguments = listOf(navArgument("articleId") { type = NavType.StringType })
            ) { backStackEntry ->
                val articleId = backStackEntry.arguments?.getString("articleId")
                ArticleView(eventId = articleId, navController = navController)
            }
            composable(Screen.RandomArticle.route) {
                ArticleView(eventId = "random", navController = navController)
            }
            composable(
                Screen.Search.route,
                arguments = listOf(navArgument("query") { type = NavType.StringType; nullable = true })
            ) { backStackEntry ->
                val query = backStackEntry.arguments?.getString("query") ?: ""
                SearchScreen(
                    initialQuery = query,
                    onBack = { currentSearchQuery: String ->
                        navController.previousBackStackEntry?.savedStateHandle?.set("searchQuery", currentSearchQuery)
                        navController.popBackStack()
                    },
                    onResultClick = { eventId: String ->
                        navController.navigate(Screen.ArticleDetail.createRoute(eventId))
                    }
                )
            }
        }
    }
}
