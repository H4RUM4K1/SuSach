package com.mad.susach.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.navArgument
import com.google.firebase.auth.FirebaseAuth
import com.mad.susach.auth.login.ui.LoginContent
import com.mad.susach.auth.register.ui.RegisterScreen
import com.mad.susach.timeline.ui.eralselection.EraSelectionScreen
import com.mad.susach.timeline.ui.timelineview.TimelineScreen
import com.mad.susach.article.ui.ArticleView
import com.mad.susach.search.ui.SearchScreen
import com.mad.susach.saved.ui.SavedPostsScreen
import com.mad.susach.profile.ui.ProfileScreen


sealed class Screen(val route: String) {
    data object Login : Screen("login")
    object Register : Screen("register")
    object Home : Screen("home")

    object EraSelection : Screen("era_selection_screen")
    object TimelineView : Screen("timeline_view_screen/{eraId}") {
        fun createRoute(eraId: String) = "timeline_view_screen/$eraId"
    }
    object RandomArticle : Screen("random_article_screen")

    object Search : Screen("search/{query}") {
        fun createRoute(query: String) = "search/$query"
    }
    object ArticleDetail : Screen("article_detail_screen/{articleId}") {
        fun createRoute(articleId: String) = "article_detail_screen/$articleId"
    }
    object SavedPosts : Screen("saved_posts")
}

@Composable
fun AppNavigation() {
    val navController = rememberNavController()

    val currentEntry = navController.currentBackStackEntryAsState().value

    val searchQueryState = remember { mutableStateOf("") }
    val savedQuery = currentEntry?.savedStateHandle?.get<String>("searchQuery")
    if (savedQuery != null && savedQuery != searchQueryState.value) {
        searchQueryState.value = savedQuery
        currentEntry.savedStateHandle.remove<String>("searchQuery")
    }

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
        ) {
            val eraId = it.arguments?.getString("eraId") ?: ""
            TimelineScreen(
                eraId = eraId,
                navToArticle = { eventId: String ->
                    navController.navigate(Screen.ArticleDetail.createRoute(eventId))
                },
                onBack = { navController.popBackStack() }
            )
        }
        

        composable(
            route = Screen.ArticleDetail.route, // Use standardized route from Screen sealed class
            arguments = listOf(navArgument("articleId") { type = NavType.StringType }) // Use "articleId"
        ) { backStackEntry ->
            val articleId = backStackEntry.arguments?.getString("articleId") // Use "articleId"
            ArticleView(eventId = articleId, navController = navController)
        }

        composable(Screen.RandomArticle.route) { // New composable for random article
            ArticleView(eventId = "random", navController = navController)
        }


        composable(
                Screen.Search.route,
                arguments =
                        listOf(
                                navArgument("query") {
                                    type = NavType.StringType
                                    nullable = true
                                }
                        )
        ) { backStackEntry ->
            val query = backStackEntry.arguments?.getString("query") ?: ""
            SearchScreen(
                    initialQuery = query,
                    onBack = { currentSearchQuery: String ->
                        navController.previousBackStackEntry?.savedStateHandle?.set(
                                "searchQuery",
                                currentSearchQuery
                        )
                        navController.popBackStack()
                    },
                    onResultClick = { eventId: String ->
                        navController.navigate(Screen.ArticleDetail.createRoute(eventId))
                    }
            )
        }
        composable(Screen.SavedPosts.route) {
            SavedPostsScreen(navController = navController)
        }
    }
}
