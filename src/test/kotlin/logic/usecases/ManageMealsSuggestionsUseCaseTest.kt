package logic.usecases

import io.mockk.mockk
import org.beijing.logic.MealRepository
import org.beijing.logic.usecases.ManageMealsSuggestionsUseCase
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class ManageMealsSuggestionsUseCaseTest {

 private lateinit var mealRepository: MealRepository
 private lateinit var useCase: ManageMealsSuggestionsUseCase

 @BeforeEach
 fun setup(){
  mealRepository = mockk()
  useCase = ManageMealsSuggestionsUseCase(mealRepository)
 }

@Test
 fun suggestSweetsWithNoEggs() {}

@Test
 fun suggestTenRandomMealsContainsPotato() {}

@Test
 fun suggestItalianLargeGroupsMeals() {}

@Test
 fun suggestKetoMeal() {}

@Test
 fun suggestEasyPreparedMeal() {}

@Test
 fun suggestMealHaveMoreThanSevenHundredCalories() {}

@Test
 fun checkMealCaloriesContent() {}
}