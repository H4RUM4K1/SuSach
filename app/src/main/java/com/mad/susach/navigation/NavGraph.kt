package com.mad.susach.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.navArgument
import com.google.firebase.auth.FirebaseAuth
import com.mad.susach.auth.login.ui.LoginContent
import com.mad.susach.auth.register.ui.RegisterScreen
import com.mad.susach.main.HomeScreen
import com.mad.susach.timeline.ui.eralselection.EraSelectionScreen
import com.mad.susach.timeline.ui.timelineview.TimelineScreen
import com.mad.susach.article.ui.ArticleScreen

sealed class Screen(val route: String) {
    // Auth & Home
    object Login : Screen("login")
    object Register : Screen("register")
    object Home : Screen("home")

    object EraSelection : Screen("era_selection_screen")
    object TimelineView : Screen("timeline_view_screen/{eraId}") {
        fun createRoute(eraId: String) = "timeline_view_screen/$eraId"
    }
    object ArticleDetail : Screen("article_detail_screen/{articleId}") {
        fun createRoute(articleId: String) = "article_detail_screen/$articleId"
    }
    object Search : Screen("search/{query}") {
        fun createRoute(query: String) = "search/$query"
    }
}

@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    val searchQueryState = remember { mutableStateOf("") }
    // Listen for result from SearchScreen
    val currentEntry = navController.currentBackStackEntryAsState().value
    val savedQuery = currentEntry?.savedStateHandle?.get<String>("searchQuery")
    if (savedQuery != null && savedQuery != searchQueryState.value) {
        searchQueryState.value = savedQuery
        currentEntry.savedStateHandle.remove<String>("searchQuery")
    }
    // Check if user is already logged in
    val isLoggedIn = FirebaseAuth.getInstance().currentUser != null
    NavHost(
        navController = navController,
        startDestination = if (isLoggedIn) Screen.Home.route else Screen.Login.route
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
                username = "Guest",
                notificationCount = 0, // Provide a default value
                onNotificationClick = {},
                onSearchClick = { query ->
                    navController.navigate(Screen.Search.createRoute(query))
                },
                navController = navController
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
            val eraId = backStackEntry.arguments?.getString("eraId")
            if (eraId != null) {
                TimelineScreen(
                    eraId = eraId,
                    navToEventDetail = { /* removed navigation to EventDetailScreen */ }
                )
            }
        }
        composable(
            route = Screen.ArticleDetail.route,
            arguments = listOf(navArgument("articleId") { type = NavType.StringType })
        ) { backStackEntry ->
            val articleId = backStackEntry.arguments?.getString("articleId")
            if (articleId != null) {
                ArticleScreen(articleId = articleId, navController = navController)
            }
        }
        composable(
            route = Screen.Search.route,
            arguments = listOf(navArgument("query") { defaultValue = ""; type = NavType.StringType })
        ) { backStackEntry ->
            val query = backStackEntry.arguments?.getString("query") ?: ""
            com.mad.susach.search.SearchScreen(
                initialQuery = query,
                onBack = { resultQuery ->
                    navController.popBackStack()
                    // Pass resultQuery back to HomeScreen
                    navController.currentBackStackEntry?.savedStateHandle?.set("searchQuery", resultQuery)
                },
                onResultClick = { /* TODO: handle result click */ }
            )
        }
    }
}
