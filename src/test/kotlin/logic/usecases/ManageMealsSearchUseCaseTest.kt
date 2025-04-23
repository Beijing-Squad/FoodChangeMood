package logic.usecases

import io.mockk.mockk
import org.beijing.logic.MealRepository
import org.beijing.logic.usecases.ManageMealsSearchUseCase
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class ManageMealsSearchUseCaseTest {

    private lateinit var mealRepository: MealRepository
    private lateinit var useCase: ManageMealsSearchUseCase

    @BeforeEach
    fun setup() {
        mealRepository = mockk()
        useCase = ManageMealsSearchUseCase(mealRepository)
    }

    //region get meal by date
    @Test
    fun getMealsByDate() {
    }
//endregion

    //region get meal by date and id
    @Test
    fun getMealByDateAndId() {
    }
//endregion

    //region get gym helper meals by calories
    @Test
    fun getGymHelperMealsByCaloriesAndProtein() {
    }
//endregion

    // region get meal by name
    @Test
    fun getMealByName() {
    }
//endregion

    //region get meal by country
    @Test
    fun getMealByCountry() {
    }
//endregion

    //region get iraqi meals
    @Test
    fun getIraqiMeals() {
    }
    //endregion
}