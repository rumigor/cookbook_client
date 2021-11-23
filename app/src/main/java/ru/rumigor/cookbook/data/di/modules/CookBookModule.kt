package ru.rumigor.cookbook.data.di.modules

import dagger.Binds
import dagger.Module
import dagger.android.ContributesAndroidInjector
import ru.rumigor.cookbook.ui.Main2Activity
import ru.rumigor.cookbook.data.repository.RecipeRepository
import ru.rumigor.cookbook.data.repository.RecipeRepositoryImpl
import ru.rumigor.cookbook.ui.addRecipe.AddRecipeFragment
import ru.rumigor.cookbook.ui.recipesList.RecipesListFragment
import ru.rumigor.cookbook.ui.recipeDetails.RecipeDetailsFragment
import javax.inject.Singleton

@Module
interface CookBookModule {

    @ContributesAndroidInjector
    fun bindMain2Activity(): Main2Activity

    @ContributesAndroidInjector
    fun bindRecipesListFragment(): RecipesListFragment

    @ContributesAndroidInjector
    fun bindRecipeDetailsFragment(): RecipeDetailsFragment

    @ContributesAndroidInjector
    fun bindAddRecipeFragment(): AddRecipeFragment

    @Singleton
    @Binds
    fun bindRecipeRepository(repository: RecipeRepositoryImpl): RecipeRepository
}