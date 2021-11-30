package ru.rumigor.cookbook.data.repository

import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Single
import okhttp3.ResponseBody
import ru.rumigor.cookbook.data.api.CookbookApi
import ru.rumigor.cookbook.data.di.modules.InMemory
import ru.rumigor.cookbook.data.di.modules.Persisted
import ru.rumigor.cookbook.data.model.*
import ru.rumigor.cookbook.data.model.Unit
import ru.rumigor.cookbook.data.storage.CookbookDatabase
import javax.inject.Inject

class RecipeRepositoryImpl @Inject constructor(
    private val cookbookApi: CookbookApi,
    @Persisted private val cookbookDatabase: CookbookDatabase
) : RecipeRepository
{

//    private val recipes = mutableListOf(
//        Recipe("1", "1", "Борщ с говядиной", "Вкусный красный борщ с говядиной приготовить очень легко",
//            "говядина, свекла, картофель, капуста белокочанная, морковь, лук репчатый, томат-паста, масло растительное, уксус, соль, перец чёрный горошком, лавровый лист, вода", "Igor", "https://img1.russianfood.com/dycontent/images_upl/64/sm_63397.jpg"),
//        Recipe("2", "2", "Азу из свинины", "Азу - это традиционное блюдо татарской кухни, которое готовят из говядины или баранины, с картофелем и солеными огурцами. В этом рецепте, как вариации на тему азу, используется свинина.",
//        "свинина, огурцы солёные, томат-паста, картофель, лук репчатый, чеснок, масло сливочное, соль, перец чёрный молотый", "Igor", "https://img1.russianfood.com/dycontent/images_upl/217/sm_216478.jpg"),
//        Recipe("3", "3", "Грибы маринованные", "Наша семья на протяжении многих лет делает маринованные грибы по этому рецепту. Очень вкусно.",
//        "грибы, соль, вода, соль, песок сахарный, лавровый лист, перец чёрный горошком, гвоздика, эссенция уксусная", "Igor", "https://img1.russianfood.com/dycontent/images_upl/274/sm_273143.jpg"),
//        Recipe("4", "4","Бургер классический", "Бургер классический – это разрезанная пополам булочка с жареной котлетой из мясного фарша в середине. Если приготовить бургер дома из натуральных и свежих продуктов, то фаст-фуд превратится не только во вкусную, но еще и полезную еду.",
//        " булочка, фарш мясной, сыр твёрдый, помидоры, огурцы солёные, лук фиолетовый, салат листовой, майонез, кетчуп, перец чёрный, соль, масло подсолнечное",
//        "Igor", "https://img1.russianfood.com/dycontent/images_upl/137/sm_136343.jpg"),
//        Recipe("5", "5", "Панкейки (американские блинчики)", "Мягкие, сладкие, вкусные американские блинчики, которые еще называют панкейки (pancakes), отлично сочетаются с медом. Панкейки понравятся и взрослым, и детям. Блинчики на молоке станут отличным завтраком.",
//        "яйца, молоко, мука пшеничная, разрыхлитель, сахар, сахар ванильный", "Igor", "https://img1.russianfood.com/dycontent/images_upl/124/sm_123944.jpg")
//    )

    override fun getRecipes(): Observable<List<Recipe>> =
        cookbookApi
            .getRecipes()
            .toObservable()

    override fun getRecipe(recipeID: String): Observable<Recipe> =
        cookbookApi
            .getRecipe(recipeID)
            .toObservable()

    override fun getCategories(): Observable<List<Category>> =
        cookbookApi
            .getCategory()
            .toObservable()


    override fun addRecipe(recipe: Recipe): Observable<ServerResponse> =
        cookbookApi
            .addRecipe(recipe)
            .toObservable()

    override fun updateRecipe(recipe: Recipe): Observable<ServerResponse> =
        cookbookApi
            .updateRecipe(recipe)
            .toObservable()

    override fun deleteRecipe(recipeId: String): Observable<ServerResponse> =
        cookbookApi
            .deleteRecipe(recipeId)
            .toObservable()

    override fun getIngredients(): Observable<List<Ingredient>> =
        cookbookApi
            .getIngredients()
            .toObservable()

    override fun getUnits(): Observable<List<Unit>> =
        cookbookApi
            .getUnits()
            .toObservable()

    override fun addIngredient(ingredient: Ingredient): Observable<ServerResponse> =
        cookbookApi
            .addIngredient(ingredient = ingredient)
            .toObservable()

    override fun loadFavorites(): Observable<List<FavoriteRecipe>> =
        cookbookDatabase
            .cookbookDao()
            .loadFavoritesRecipes()

    override fun loadFavoriteRecipe(recipeId: String): Observable<FavoriteRecipe> =
        cookbookDatabase
            .cookbookDao()
            .loadFavoriteRecipe(recipeId)
            .toObservable()

    override fun addToFavorites(recipe: FavoriteRecipe): Single<FavoriteRecipe> =
        cookbookDatabase
            .cookbookDao()
            .insert(recipe)
            .andThen(Single.just(recipe))

    override fun favoriteSearch(name: String): Observable<List<FavoriteRecipe>> =
        cookbookDatabase
            .cookbookDao()
            .favoriteSearch(name)

    override fun deleteFromFavorites(recipeId: String): Completable =
        cookbookDatabase
            .cookbookDao()
            .deleteFromFavorites(recipeId)

}