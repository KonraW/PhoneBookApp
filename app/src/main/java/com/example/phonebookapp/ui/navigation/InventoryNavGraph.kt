package com.example.phonebookapp.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.phonebookapp.ui.home.HomeDestination
import com.example.phonebookapp.ui.home.HomeScreen
import com.example.phonebookapp.ui.item.DetailsDestination
import com.example.phonebookapp.ui.item.DetailsScreen
import com.example.phonebookapp.ui.item.EditDestination
import com.example.phonebookapp.ui.item.EditScreen
import com.example.phonebookapp.ui.item.EntryDestination
import com.example.phonebookapp.ui.item.EntryScreen

@Composable
fun PhoneBookNavHost(
    navController: NavHostController,
    modifier: Modifier = Modifier,
) {
    NavHost(
        navController = navController,
        startDestination = HomeDestination.route,
        modifier = modifier
    ) {
        composable(route = HomeDestination.route) {
            HomeScreen(
                navigateToItemEntry = { navController.navigate(EntryDestination.route) },
                navigateToItemUpdate = {
                    navController.navigate("${DetailsDestination.route}/${it}")
                }
            )
        }
        composable(route = EntryDestination.route) {
            EntryScreen(
                navigateBack = { navController.popBackStack() },
                onNavigateUp = { navController.navigateUp() }
            )
        }
        composable(
            route = DetailsDestination.routeWithArgs,
            arguments = listOf(navArgument(DetailsDestination.itemIdArg) {
                type = NavType.IntType
            })
        ) {
            DetailsScreen(
                navigateToEditItem = { navController.navigate("${EditDestination.route}/$it") },
                navigateBack = { navController.navigateUp() }
            )
        }
        composable(
            route = EditDestination.routeWithArgs,
            arguments = listOf(navArgument(EditDestination.itemIdArg) {
                type = NavType.IntType
            })
        ) {
            EditScreen(
                navigateBack = { navController.popBackStack() },
                onNavigateUp = { navController.navigateUp() }
            )
        }
    }
}