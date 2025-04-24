package logic.usecases

import com.google.common.truth.Truth.assertThat
import io.mockk.every
import io.mockk.mockk
import kotlinx.datetime.Clock
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import org.beijing.logic.MealRepository
import org.beijing.logic.usecases.ManageMealsGamesUseCase
import org.beijing.model.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource

class ManageMealsGamesUseCaseTest {

    private lateinit var mealRepository: MealRepository
    private lateinit var useCase: ManageMealsGamesUseCase
    private val testMeal = createMeal(
        id = 1,
        name = "Pizza",
        ingredients = listOf("Cheese", "Tomato", "Flour")
    )

    @BeforeEach
    fun setup() {
        // Given
        mealRepository = mockk(relaxed = true)
        every { mealRepository.getAllMeals() } returns listOf(testMeal)
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

    //region Start Ingredient Game Tests
    @Test
    fun `should return successful game round when starting with valid meal`() {
        // Given
        val initialState = IngredientGameState()

        // When
        val result = useCase.startIngredientGame(initialState)

        // Then
        assertThat(result.isSuccess).isTrue()
        result.onSuccess { (round, state) ->
            assertThat(round.mealName).isEqualTo("Pizza")
            assertThat(round.options).hasSize(3)
            assertThat(round.options).contains(round.correctAnswer)
            assertThat(state.usedMeals).contains(1)
        }
    }

    @Test
    fun `should return failure when starting game with maximum correct answers`() {
        // Given
        val state = IngredientGameState(correctAnswers = 15)

        // When
        val result = useCase.startIngredientGame(state)

        // Then
        assertThat(result.isFailure).isTrue()
        assertThat(result.exceptionOrNull()?.message).isEqualTo("Game Over")
    }

    @Test
    fun `should return failure when starting game with empty meal repository`() {
        // Given
        every { mealRepository.getAllMeals() } returns emptyList()
        useCase = ManageMealsGamesUseCase(mealRepository)

        // When
        val result = useCase.startIngredientGame(IngredientGameState())

        // Then
        assertThat(result.isFailure).isTrue()
        assertThat(result.exceptionOrNull()?.message).isEqualTo("No meals available 😔")
    }

    @Test
    fun `should return failure when starting game with meals having no ingredients`() {
        // Given
        val emptyMeal = createMeal(id = 1, name = "Empty Pizza", ingredients = emptyList())
        every { mealRepository.getAllMeals() } returns listOf(emptyMeal)
        useCase = ManageMealsGamesUseCase(mealRepository)

        // When
        val result = useCase.startIngredientGame(IngredientGameState())

        // Then
        assertThat(result.isFailure).isTrue()
        assertThat(result.exceptionOrNull()?.message).isEqualTo("No meals available 😔")
    }

    @Test
    fun `should return failure when starting game with all meals already used`() {
        // Given
        val usedState = IngredientGameState(usedMeals = setOf(1))

        // When
        val result = useCase.startIngredientGame(usedState)

        // Then
        assertThat(result.isFailure).isTrue()
        assertThat(result.exceptionOrNull()?.message).isEqualTo("No meals available 😔")
    }

    @Test
    fun `should randomly select meals when starting game with multiple valid meals`() {
        // Given
        val meal1 = createMeal(id = 1, name = "Pizza", ingredients = listOf("Cheese", "Tomato"))
        val meal2 = createMeal(id = 2, name = "Burger", ingredients = listOf("Meat", "Bun"))
        every { mealRepository.getAllMeals() } returns listOf(meal1, meal2)
        useCase = ManageMealsGamesUseCase(mealRepository)
        val selectedMeals = mutableSetOf<String>()

        // When
        repeat(10) {
            val result = useCase.startIngredientGame(IngredientGameState())
            result.onSuccess { (round, _) -> selectedMeals.add(round.mealName) }
        }

        // Then
        assertThat(selectedMeals).containsAtLeast("Pizza", "Burger")
    }

//endregion

    //region Check Answer Tests
    @Test
    fun `should return false when checking answer with invalid option index`() {
        // Given
        val round = IngredientGameRound(
            mealName = "Pizza",
            correctAnswer = "Cheese",
            options = listOf("Cheese", "Tomato", "Flour")
        )
        val initialState = IngredientGameState()

        // When
        val (isCorrect, newState) = useCase.checkAnswer(4, round, initialState)

        // Then
        assertThat(isCorrect).isFalse()
        assertThat(newState).isEqualTo(initialState)
    }

    @Test
    fun `should return false when checking answer with negative index`() {
        // Given
        val round = IngredientGameRound(
            mealName = "Pizza",
            correctAnswer = "Cheese",
            options = listOf("Cheese", "Tomato", "Flour")
        )
        val initialState = IngredientGameState()

        // When
        val (isCorrect, newState) = useCase.checkAnswer(-1, round, initialState)

        // Then
        assertThat(isCorrect).isFalse()
        assertThat(newState).isEqualTo(initialState)
    }

    @ParameterizedTest
    @CsvSource(
        "1,true,1000,1",  // Correct answer
        "2,false,0,0",    // Wrong answer
        "3,false,0,0"     // Wrong answer
    )
    fun `should return correct validation result when checking different answer options`(
        userChoice: Int,
        expectedIsCorrect: Boolean,
        expectedScore: Int,
        expectedCorrectAnswers: Int
    ) {
        // Given
        val round = IngredientGameRound(
            mealName = "Pizza",
            correctAnswer = "Cheese",
            options = listOf("Cheese", "Tomato", "Flour")
        )
        val initialState = IngredientGameState()

        // When
        val (isCorrect, newState) = useCase.checkAnswer(userChoice, round, initialState)

        // Then
        assertThat(isCorrect).isEqualTo(expectedIsCorrect)
        assertThat(newState.score).isEqualTo(expectedScore)
        assertThat(newState.correctAnswers).isEqualTo(expectedCorrectAnswers)
    }

    @Test
    fun `should accumulate score correctly when answering multiple questions correctly`() {
        // Given
        val round = IngredientGameRound(
            mealName = "Pizza",
            correctAnswer = "Cheese",
            options = listOf("Cheese", "Tomato", "Flour")
        )
        var state = IngredientGameState()

        // When
        repeat(3) {
            val (_, newState) = useCase.checkAnswer(1, round, state)
            state = newState
        }

        // Then
        assertThat(state.score).isEqualTo(3000)
        assertThat(state.correctAnswers).isEqualTo(3)
    }

    //region Game Over Tests
    @ParameterizedTest
    @CsvSource(
        "14,false",  // Not game over
        "15,true",   // Game over
        "16,true"    // Game over
    )
    fun `should return correct game over state when checking different correct answer counts`(
        correctAnswers: Int,
        expectedGameOver: Boolean
    ) {
        // Given
        val state = IngredientGameState(correctAnswers = correctAnswers)

        // When
        val result = useCase.isGameOver(state)

        // Then
        assertThat(result).isEqualTo(expectedGameOver)
    }

    @Test
    fun `should transition to game over when reaching maximum correct answers`() {
        // Given
        val round = IngredientGameRound(
            mealName = "Pizza",
            correctAnswer = "Cheese",
            options = listOf("Cheese", "Tomato", "Flour")
        )
        var state = IngredientGameState(correctAnswers = 14)

        // When
        val (_, newState) = useCase.checkAnswer(1, round, state)

        // Then
        assertThat(useCase.isGameOver(state)).isFalse()
        assertThat(useCase.isGameOver(newState)).isTrue()
        assertThat(newState.correctAnswers).isEqualTo(15)
    }

    @Test
    fun `should return correct number of options when starting game with complex meal`() {
        // Given
        val meal = createMeal(
            id = 1,
            name = "Complex Meal",
            ingredients = listOf("Ing1", "Ing2", "Ing3", "Ing4", "Ing5")
        )
        every { mealRepository.getAllMeals() } returns listOf(meal)
        useCase = ManageMealsGamesUseCase(mealRepository)

        // When
        val result = useCase.startIngredientGame(IngredientGameState())

        // Then
        assertThat(result.isSuccess).isTrue()
        result.onSuccess { (round, _) ->
            assertThat(round.options).hasSize(3)
            assertThat(round.options).contains(round.correctAnswer)
            assertThat(round.options.distinct()).hasSize(3)
        }
    }
//endregion

    private fun createMeal(id: Int, name: String, ingredients: List<String>): Meal {
        return Meal(
            name = name,
            id = id,
            minutes = 30,
            contributorId = 1,
            submitted = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()).date,
            tags = emptyList(),
            nutrition = Nutrition(
                caloriesKcal = 500.0,
                totalFatGrams = 40.0,
                sugarGrams = 2.0,
                sodiumGrams = 1.0,
                proteinGrams = 20.0,
                saturatedFatGrams = 15.0,
                carbohydratesGrams = 10.0
            ),
            nSteps = 5,
            steps = listOf("Step 1", "Step 2"),
            description = "Test description",
            ingredients = ingredients,
            nIngredients = ingredients.size
        )
    }
}