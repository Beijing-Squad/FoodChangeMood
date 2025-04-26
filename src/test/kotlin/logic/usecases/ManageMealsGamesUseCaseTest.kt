package logic.usecases

import com.google.common.truth.Truth.assertThat
import fake.createMeal
import io.mockk.every
import io.mockk.mockk
import com.google.common.truth.Truth.assertThat
import fake.createMeal
import io.mockk.*
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertTrue
import model.GameRound
import org.beijing.logic.MealRepository
import org.beijing.logic.usecases.ManageMealsGamesUseCase
import org.beijing.model.IngredientGameRound
import org.beijing.model.IngredientGameState
import org.beijing.model.Meal
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource
import org.junit.jupiter.api.assertThrows

class ManageMealsGamesUseCaseTest {

    private lateinit var mealRepository: MealRepository
    private lateinit var useCase: ManageMealsGamesUseCase
    private lateinit var testMeals: List<Meal>

    @BeforeEach
    fun setup() {
        testMeals = listOf(createMeal())
        mealRepository = mockk()
        every { mealRepository.getAllMeals() } returns testMeals
        useCase = ManageMealsGamesUseCase(mealRepository)
    }

    //region Preparation Time Guess Game
    @Test
    fun `start New Round should throw exception when repository is empty`() {
        //Given
        testMeals = emptyList()
        every { mealRepository.getAllMeals() } returns testMeals
        val emptyUseCase = ManageMealsGamesUseCase(mealRepository)

        // When & Then
        val exception = assertThrows<IllegalArgumentException> {
            emptyUseCase.startNewRound()
        }

        assertThat(exception).hasMessageThat().isEqualTo("No meals found in the repository")
    }

    @Test
    fun `start New Round should create a new game round with correct initial values`() {
        // Given
        testMeals = listOf(
            createMeal(id = 1, name = "Test Meal 1", minutes = 30),
            createMeal(id = 2, name = "Test Meal 2", minutes = 45),
            createMeal(id = 3, name = "Test Meal 3", minutes = 60)
        )
        every { mealRepository.getAllMeals() } returns testMeals
        // When
        val gameRound = useCase.startNewRound()

        // Then
        assertThat(gameRound).isNotNull()
        assertThat(gameRound!!.isCompleted).isFalse()
        assertThat(gameRound.attemptsLeft).isEqualTo(3)
        assertThat(gameRound.lastFeedBack).isNull()

    }

    @Test
    fun `make Guess should return unchanged round with feedback when round is already completed`() {
        // Given
        testMeals = listOf(
            createMeal(id = 1, name = "Test Meal 1", minutes = 30),
            createMeal(id = 2, name = "Test Meal 2", minutes = 45),
            createMeal(id = 3, name = "Test Meal 3", minutes = 60)
        )
        every { mealRepository.getAllMeals() } returns testMeals
        val completedRound = GameRound(
            meal = testMeals[0],
            attemptsLeft = 2,
            isCompleted = true,
            lastFeedBack = "Previous feedback"
        )

        // When
        val result = useCase.makeGuess(completedRound, 25)

        // Then
        assertThat(result.isCompleted).isTrue()
        assertThat(result.lastFeedBack).isEqualTo("This round is already Completed, Start A new Round.")
        assertThat(result.attemptsLeft).isEqualTo(completedRound.attemptsLeft)
    }

    @Test
    fun `make Guess should mark round as completed with no attempts left message when no attempts remain`() {
        // Given
        testMeals = listOf(
            createMeal(id = 1, name = "Test Meal 1", minutes = 30),
            createMeal(id = 2, name = "Test Meal 2", minutes = 45),
            createMeal(id = 3, name = "Test Meal 3", minutes = 60)
        )
        every { mealRepository.getAllMeals() } returns testMeals
        val noAttemptsRound = GameRound(
            meal = testMeals[0],
            attemptsLeft = 0,
            isCompleted = false,
            lastFeedBack = null
        )

        // When
        val result = useCase.makeGuess(noAttemptsRound, 25)

        // Then
        assertThat(result.isCompleted).isTrue()
        assertThat(result.lastFeedBack).isEqualTo("No Attempts Left, The Actual Preparation Time is: 30 minutes.")
    }

    @Test
    fun `make Guess should return correct feedback when guess is too low`() {
        // Given
        testMeals = listOf(
            createMeal(id = 1, name = "Test Meal 1", minutes = 30),
            createMeal(id = 2, name = "Test Meal 2", minutes = 45),
            createMeal(id = 3, name = "Test Meal 3", minutes = 60)
        )
        every { mealRepository.getAllMeals() } returns testMeals
        val round = GameRound(
            meal = testMeals[0],
            attemptsLeft = 3,
            isCompleted = false,
            lastFeedBack = null
        )

        // When
        val result = useCase.makeGuess(round, 25)

        // Then
        assertThat(result.isCompleted).isFalse()
        assertThat(result.attemptsLeft).isEqualTo(2)
        assertThat(result.lastFeedBack).isEqualTo("Too low! Try a higher number.")
    }

    @Test
    fun `make Guess should return correct feedback when guess is too high`() {
        // Given
        testMeals = listOf(
            createMeal(id = 1, name = "Test Meal 1", minutes = 30),
            createMeal(id = 2, name = "Test Meal 2", minutes = 45),
            createMeal(id = 3, name = "Test Meal 3", minutes = 60)
        )
        every { mealRepository.getAllMeals() } returns testMeals
        val round = GameRound(
            meal = testMeals[0],
            attemptsLeft = 3,
            isCompleted = false,
            lastFeedBack = null
        )

        // When
        val result = useCase.makeGuess(round, 35)

        // Then
        assertThat(result.isCompleted).isFalse()
        assertThat(result.attemptsLeft).isEqualTo(2)
        assertThat(result.lastFeedBack).isEqualTo("Too high! Try a lower number.")
    }

    @Test
    fun `make Guess should mark round as completed with correct feedback when guess is correct`() {
        // Given
        testMeals = listOf(
            createMeal(id = 1, name = "Test Meal 1", minutes = 30),
            createMeal(id = 2, name = "Test Meal 2", minutes = 45),
            createMeal(id = 3, name = "Test Meal 3", minutes = 60)
        )
        every { mealRepository.getAllMeals() } returns testMeals
        val round = GameRound(
            meal = testMeals[0],
            attemptsLeft = 3,
            isCompleted = false,
            lastFeedBack = null
        )

        // When
        val result = useCase.makeGuess(round, 30)

        // Then
        assertThat(result.isCompleted).isTrue()
        assertThat(result.attemptsLeft).isEqualTo(2)
        assertThat(result.lastFeedBack).isEqualTo("Correct!! The preparation time is indeed 30 minutes.")

    }

    @Test
    fun `make Guess should show game over message when last attempt is incorrect`() {
        // Given
        testMeals = listOf(
            createMeal(id = 1, name = "Test Meal 1", minutes = 30),
            createMeal(id = 2, name = "Test Meal 2", minutes = 45),
            createMeal(id = 3, name = "Test Meal 3", minutes = 60)
        )
        every { mealRepository.getAllMeals() } returns testMeals
        val lastAttemptRound = GameRound(
            meal = testMeals[0],
            attemptsLeft = 1,
            isCompleted = false,
            lastFeedBack = null
        )

        // When
        val result = useCase.makeGuess(lastAttemptRound, 25)

        // Then
        assertThat(result.isCompleted).isTrue()
        assertThat(result.attemptsLeft).isEqualTo(0)
        assertThat(result.lastFeedBack).isEqualTo(
            "Too low! Try a higher number.\nGameOver! The actual preparation time is 30 minutes."
        )

    }

    @Test
    fun `should throw exception when no meals are available`() {
        // Given
        val emptyRepo = mockk<MealRepository>()
        every { emptyRepo.getAllMeals() } returns emptyList()
        val useCase = ManageMealsGamesUseCase(emptyRepo)

        // When / Then
        val exception = assertThrows<IllegalArgumentException> {
            useCase.startNewRound()
        }
        assertThat(exception).hasMessageThat().isEqualTo("No meals found in the repository")
    }

    @Test
    fun `should return no attempts left message when attempts run out immediately`() {
        // Given
        val meal = createMeal(name = "Pizza", minutes = 20)
        val round = GameRound(meal, 0, false, null)

        // When
        val result = useCase.makeGuess(round, 10)

        // Then
        assertTrue(result.isCompleted)
        assertEquals("No Attempts Left, The Actual Preparation Time is: 20 minutes.", result.lastFeedBack)
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
    fun `should return failure when starting game with empty meal list`() {
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
        val emptyMeal = createMeal(
            id = 1,
            name = "Empty Pizza",
            ingredients = emptyList()
        )

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
        val meal = createMeal(
            id = 1,
            name = "Pizza",
            ingredients = listOf("Cheese", "Tomato", "Flour")
        )

        every { mealRepository.getAllMeals() } returns listOf(meal)
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

    @ParameterizedTest
    @CsvSource(
        "1,true,1000,1",
        "2,false,0,0",
        "3,false,0,0"
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

    @ParameterizedTest
    @CsvSource(
        "14,false",
        "15,true",
        "16,true"
    )
    fun `should handle game over conditions correctly`(
        correctAnswers: Int,
        expectedGameOver: Boolean
    ) {
        // Given
        val state = IngredientGameState(correctAnswers = correctAnswers)
        val meal = createMeal(
            id = 1,
            name = "Pizza",
            ingredients = listOf("Cheese", "Tomato", "Flour", "Pepperoni")
        )

        every { mealRepository.getAllMeals() } returns listOf(meal)

        // When
        val gameOverResult = useCase.isGameOver(state)
        val startResult = useCase.startIngredientGame(state)

        // Then
        assertThat(gameOverResult).isEqualTo(expectedGameOver)
        if (expectedGameOver) {
            assertThat(startResult.isFailure).isTrue()
            assertThat(startResult.exceptionOrNull()?.message).isEqualTo("Game Over")
        } else {
            assertThat(startResult.isSuccess).isTrue()
        }
    }

    @Test
    fun `should calculate score correctly when answering multiple questions correctly`() {
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

    @ParameterizedTest
    @CsvSource(
        "14,false",
        "15,true",
        "16,true"
    )
    fun `should return game over when answer all number of corrected answer required`(
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
    fun `should return correct number of options when starting game with many ingredients meal`() {
        // Given
        val numberOfOptions = 3
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
            assertThat(round.options).hasSize(numberOfOptions)
            assertThat(round.options).contains(round.correctAnswer)
            assertThat(round.options.distinct()).hasSize(numberOfOptions)
        }
    }
    //endregion
}