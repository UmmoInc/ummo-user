package xyz.ummo.user.ui.fragments.categories

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import xyz.ummo.user.data.repo.serviceCategories.CategoriesRepo

class CategoriesViewModelProviderFactory(
    private val categoriesRepo: CategoriesRepo
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return ServiceCategoriesViewModel(categoriesRepo) as T
    }
}