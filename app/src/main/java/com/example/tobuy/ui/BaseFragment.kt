package com.example.tobuy.ui

import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.NavDirections
import com.example.tobuy.arch.ToBuyViewModel
import com.example.tobuy.database.AppDatabase

abstract class BaseFragment: Fragment() {
    protected val mainActivity: MainActivity
        get() = activity as MainActivity

    protected val appDatabase: AppDatabase
        get() = AppDatabase.getDatabase(requireActivity())

    protected val sharedViewModel: ToBuyViewModel by activityViewModels()

    protected fun navigateUp() {
        mainActivity.navController.navigateUp()
    }

    protected fun navigateViaNavGraph(actionID: Int) {
        mainActivity.navController.navigate(actionID)
    }

    protected fun navigateViaNavGraph(navDirections: NavDirections) {
        mainActivity.navController.navigate(navDirections)
    }


}