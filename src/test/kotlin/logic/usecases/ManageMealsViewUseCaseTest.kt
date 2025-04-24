package logic.usecases

import io.mockk.mockk
import org.beijing.logic.MealRepository
import org.beijing.logic.usecases.ManageMealsViewUseCase
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class ManageMealsViewUseCaseTest {

    private lateinit var mealRepository: MealRepository
    private lateinit var useCase: ManageMealsViewUseCase

    @BeforeEach
    fun setup() {
        mealRepository = mockk()
        useCase = ManageMealsViewUseCase(mealRepository)
    }

    //region get healthy quick prepared meals
    @Test
    fun getHealthyQuickPreparedMeals() {
    }
//endregion

    //region get sorted seafood by protein
    @Test
    fun getSortedSeaFoodByProtein() {
    }
    //endregion
}