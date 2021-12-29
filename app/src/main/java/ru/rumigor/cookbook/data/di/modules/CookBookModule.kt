package ru.rumigor.cookbook.data.di.modules

import dagger.Binds
import dagger.Module
import dagger.android.ContributesAndroidInjector
import ru.rumigor.cookbook.ui.Main2Activity
import ru.rumigor.cookbook.data.repository.RecipeRepository
import ru.rumigor.cookbook.data.repository.RecipeRepositoryImpl
import ru.rumigor.cookbook.data.repository.UploadImage
import ru.rumigor.cookbook.data.repository.UploadImageImpl
import ru.rumigor.cookbook.ui.login.LoginActivity
import ru.rumigor.cookbook.ui.addRecipe.AddRecipeFragment
import ru.rumigor.cookbook.ui.category.CategoryFragment
import ru.rumigor.cookbook.ui.favorites.FavoritesFragment
import ru.rumigor.cookbook.ui.recipeDetails.RecipeDetailsFragment
import ru.rumigor.cookbook.ui.recipesList.RecipesListFragment
import javax.inject.Singleton
import ru.rumigor.cookbook.ui.StartActivity
import ru.rumigor.cookbook.ui.registration.RegistrationActivity
import ru.rumigor.cookbook.ui.tagFilter.TagsFragment


@Module
interface CookBookModule {


    @ContributesAndroidInjector
    fun bindMain2Activity(): Main2Activity

    @ContributesAndroidInjector
    fun bindRegistrationActivity(): RegistrationActivity

    @ContributesAndroidInjector
    fun bindLoginActivity(): LoginActivity

    @ContributesAndroidInjector
    fun bindStartActivity(): StartActivity

    @ContributesAndroidInjector
    fun bindRecipesListFragment(): RecipesListFragment

    @ContributesAndroidInjector
    fun bindTagsFragment(): TagsFragment

    @ContributesAndroidInjector
    fun bindRecipeDetailsFragment(): RecipeDetailsFragment

    @ContributesAndroidInjector
    fun bindAddRecipeFragment(): AddRecipeFragment

    @ContributesAndroidInjector
    fun bindFavoritesFragment(): FavoritesFragment

    @ContributesAndroidInjector
    fun bindCategoryFragment(): CategoryFragment

    @Singleton
    @Binds
    fun bindRecipeRepository(repository: RecipeRepositoryImpl): RecipeRepository

    @Singleton
    @Binds
    fun bindUploadImage(uploadImage: UploadImageImpl): UploadImage
}