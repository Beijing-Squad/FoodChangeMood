package logic.usecases

import fake.createMeal
import io.mockk.*
import model.GameRound
import org.beijing.logic.MealRepository
import org.beijing.logic.usecases.ManageMealsGamesUseCase
import org.beijing.model.Meal
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
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

        Assertions.assertEquals("No meals found in the repository", exception.message)
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
        Assertions.assertNotNull(gameRound)
        Assertions.assertFalse(gameRound.isCompleted)
        Assertions.assertEquals(3, gameRound.attemptsLeft)
        Assertions.assertNull(gameRound.lastFeedBack)

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
        Assertions.assertTrue(result.isCompleted)
        Assertions.assertEquals("This round is already Completed, Start A new Round.", result.lastFeedBack)
        Assertions.assertEquals(completedRound.attemptsLeft, result.attemptsLeft)
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
        Assertions.assertTrue(result.isCompleted)
        Assertions.assertEquals("No Attempts Left, The Actual Preparation Time is: 30 minutes.", result.lastFeedBack)
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
        Assertions.assertFalse(result.isCompleted)
        Assertions.assertEquals(2, result.attemptsLeft)
        Assertions.assertEquals("Too low! Try a higher number.", result.lastFeedBack)
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
        Assertions.assertFalse(result.isCompleted)
        Assertions.assertEquals(2, result.attemptsLeft)
        Assertions.assertEquals("Too high! Try a lower number.", result.lastFeedBack)
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
        Assertions.assertTrue(result.isCompleted)
        Assertions.assertEquals(2, result.attemptsLeft)
        Assertions.assertEquals("Correct!! The preparation time is indeed 30 minutes.", result.lastFeedBack)
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
        Assertions.assertTrue(result.isCompleted)
        Assertions.assertEquals(0, result.attemptsLeft)
        Assertions.assertEquals(
            "Too low! Try a higher number.\nGameOver! The actual preparation time is 30 minutes.",
            result.lastFeedBack
        )
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