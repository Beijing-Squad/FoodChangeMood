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
    fun setup() {
        mealRepository = mockk(relaxed = true)
        useCase = ManageMealsGamesUseCase(mealRepository)
    }

    //region start new round
    @Test
    fun startNewRound() {
    }
    //endregion

    // region make guess
    @Test
    fun makeGuess() {
    }
    //endregion

    //region start ingredient game
    @Test
    fun startIngredientGame() {
    }
//endregion

    //region check answer
    @Test
    fun checkAnswer() {
    }
//endregion

    //region is game over
    @Test
    fun isGameOver() {
    }
    //endregion
}