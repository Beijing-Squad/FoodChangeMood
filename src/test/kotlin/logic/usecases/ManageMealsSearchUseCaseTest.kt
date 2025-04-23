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
 fun setup(){
  mealRepository = mockk()
  useCase = ManageMealsSearchUseCase(mealRepository)
 }

@Test
 fun getMealsByDate() {}

@Test
 fun getMealByDateAndId() {}

@Test
 fun getGymHelperMealsByCaloriesAndProtein() {}

@Test
 fun getMealByName() {}

@Test
 fun getMealByCountry() {}

@Test
 fun getIraqiMeals() {}
}