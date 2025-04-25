package logic.usecases

import com.google.common.truth.Truth.assertThat
import fake.createMeal
import io.mockk.*
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertTrue
import model.GameRound
import org.beijing.logic.MealRepository
import org.beijing.logic.usecases.ManageMealsGamesUseCase
import org.beijing.model.Meal
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