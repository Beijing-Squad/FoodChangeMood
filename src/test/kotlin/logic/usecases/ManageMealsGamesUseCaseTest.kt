package logic.usecases

import io.mockk.mockk
import org.beijing.logic.MealRepository
import org.beijing.logic.usecases.ManageMealsGamesUseCase
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class ManageMealsGamesUseCaseTest {

 private lateinit var mealRepository: MealRepository
 private lateinit var useCase: ManageMealsGamesUseCase

 @BeforeEach
 fun setup(){
  mealRepository = mockk()
  useCase = ManageMealsGamesUseCase(mealRepository)
 }

@Test
 fun startNewRound() {}

@Test
 fun makeGuess() {}

@Test
 fun startIngredientGame() {}

@Test
 fun checkAnswer() {}

@Test
 fun isGameOver() {}
}