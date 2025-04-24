package presentation.service

import io.mockk.*
import org.beijing.logic.usecases.ManageMealsSuggestionsUseCase
import org.beijing.model.Meal
import org.beijing.model.Nutrition
import org.beijing.presentation.ViewMealDetails
import org.beijing.presentation.service.SuggestionMealsService
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import presentation.view_read.ConsoleIO
import kotlinx.datetime.LocalDate
import org.junit.jupiter.api.AfterEach

class SuggestionMealsServiceTest {

    private lateinit var suggestionMealsUseCase: ManageMealsSuggestionsUseCase
    private lateinit var viewMealDetails: ViewMealDetails
    private lateinit var consoleIO: ConsoleIO
    private lateinit var suggestionMealsService: SuggestionMealsService

    @BeforeEach
    fun setUp() {
        suggestionMealsUseCase = mockk(relaxed = true)
        viewMealDetails = mockk(relaxed = true)
        consoleIO = mockk(relaxed = true)

        every { consoleIO.viewWithLine(any()) } just Runs
        every { consoleIO.view(any()) } just Runs
        every { viewMealDetails.displayMealDetails(any()) } just Runs

        suggestionMealsService = SuggestionMealsService(
            suggestionMeals = suggestionMealsUseCase,
            viewMealDetails = viewMealDetails,
            consoleIO = consoleIO
        )
    }

    @AfterEach
    fun clear() {
        unmockkAll()
    }

    @Test
    fun `should return suitable message when no available keto meals`() {
        // Given
        every { suggestionMealsUseCase.suggestKetoMeal(any()) } returns null

        // When
        suggestionMealsService.launchKetoMealHelper()

        // Then
        verifyOrder {
            suggestionMealsUseCase.suggestKetoMeal(mutableSetOf())
            consoleIO.viewWithLine("😔 No more keto meals to suggest.")
        }
    }

    @Test
    fun `should handle multiple no responses before yes`() {
        // Given
        val meal1 = createKetoMeal(1, "Keto Burger")
        val meal2 = createKetoMeal(2, "Keto Salad")
        val meal3 = createKetoMeal(3, "Keto Pizza")

        every { suggestionMealsUseCase.suggestKetoMeal(any()) } returnsMany listOf(meal1, meal2, meal3)
        every { consoleIO.readInput() } returnsMany listOf("no", "no", "yes")

        // When
        suggestionMealsService.launchKetoMealHelper()

        // Then
        verify {
            suggestionMealsUseCase.suggestKetoMeal(mutableSetOf())
            consoleIO.viewWithLine("\n🥑 Keto Meal: Keto Burger")
            consoleIO.viewWithLine("Short Description: A keto-friendly meal")
            consoleIO.viewWithLine("Do you like it? ❤")
            consoleIO.view("write 'yes' to get details or 'no' to get another meal (or type 'exit' to quit):")
            consoleIO.readInput()
            consoleIO.viewWithLine("🔄 Okay! Let's try another one.")

            suggestionMealsUseCase.suggestKetoMeal(mutableSetOf(1))
            consoleIO.viewWithLine("\n🥑 Keto Meal: Keto Salad")
            consoleIO.viewWithLine("Short Description: A keto-friendly meal")
            consoleIO.viewWithLine("Do you like it? ❤")
            consoleIO.view("write 'yes' to get details or 'no' to get another meal (or type 'exit' to quit):")
            consoleIO.readInput()
            consoleIO.viewWithLine("🔄 Okay! Let's try another one.")

            suggestionMealsUseCase.suggestKetoMeal(mutableSetOf(1, 2))
            consoleIO.viewWithLine("\n🥑 Keto Meal: Keto Pizza")
            consoleIO.viewWithLine("Short Description: A keto-friendly meal")
            consoleIO.viewWithLine("Do you like it? ❤")
            consoleIO.view("write 'yes' to get details or 'no' to get another meal (or type 'exit' to quit):")
            consoleIO.readInput()
            viewMealDetails.displayMealDetails(meal3)
        }
    }

    @Test
    fun `should handle multiple invalid inputs before valid input`() {
        // Given
        val ketoMeal = createKetoMeal(1, "Keto Burger")
        every { suggestionMealsUseCase.suggestKetoMeal(any()) } returns ketoMeal
        every { consoleIO.readInput() } returnsMany listOf("invalid1", "invalid2", "yes")

        // When
        suggestionMealsService.launchKetoMealHelper()

        // Then
        verifyOrder {
            suggestionMealsUseCase.suggestKetoMeal(mutableSetOf())
            consoleIO.viewWithLine("\n🥑 Keto Meal: Keto Burger")
            consoleIO.viewWithLine("Short Description: A keto-friendly meal")
            consoleIO.viewWithLine("Do you like it? ❤")
            consoleIO.view("write 'yes' to get details or 'no' to get another meal (or type 'exit' to quit):")
            consoleIO.readInput()
            consoleIO.viewWithLine("⚠️ Please type 'yes' or 'no'")

            consoleIO.viewWithLine("\n🥑 Keto Meal: Keto Burger")
            consoleIO.viewWithLine("Short Description: A keto-friendly meal")
            consoleIO.viewWithLine("Do you like it? ❤")
            consoleIO.view("write 'yes' to get details or 'no' to get another meal (or type 'exit' to quit):")
            consoleIO.readInput()
            consoleIO.viewWithLine("⚠️ Please type 'yes' or 'no'")

            consoleIO.viewWithLine("\n🥑 Keto Meal: Keto Burger")
            consoleIO.viewWithLine("Short Description: A keto-friendly meal")
            consoleIO.viewWithLine("Do you like it? ❤")
            consoleIO.view("write 'yes' to get details or 'no' to get another meal (or type 'exit' to quit):")
            consoleIO.readInput()
            viewMealDetails.displayMealDetails(ketoMeal)
        }
    }

    @Test
    fun `should handle exit input and terminate loop`() {
        // Given
        val ketoMeal = createKetoMeal(1, "Keto Burger")
        every { suggestionMealsUseCase.suggestKetoMeal(any()) } returns ketoMeal
        every { consoleIO.readInput() } returns "exit"

        // When
        suggestionMealsService.launchKetoMealHelper()

        // Then
        verifyOrder {
            suggestionMealsUseCase.suggestKetoMeal(mutableSetOf())
            consoleIO.viewWithLine("\n🥑 Keto Meal: Keto Burger")
            consoleIO.viewWithLine("Short Description: A keto-friendly meal")
            consoleIO.viewWithLine("Do you like it? ❤")
            consoleIO.view("write 'yes' to get details or 'no' to get another meal (or type 'exit' to quit):")
            consoleIO.readInput()
        }
    }


    @Test
    fun `should display keto meal and handle yes response`() {
        // Given
        val ketoMeal = createKetoMeal(1, "Keto Burger")
        every { suggestionMealsUseCase.suggestKetoMeal(any()) } returns ketoMeal
        every { consoleIO.readInput() } returns "yes"

        // When
        suggestionMealsService.launchKetoMealHelper()

        // Then
        verify {
            consoleIO.viewWithLine("\n🥑 Keto Meal: Keto Burger")
            consoleIO.viewWithLine("Short Description: A keto-friendly meal")
            consoleIO.viewWithLine("Do you like it? ❤")
            consoleIO.view("write 'yes' to get details or 'no' to get another meal (or type 'exit' to quit):")
            viewMealDetails.displayMealDetails(ketoMeal)
        }
    }

    @Test
    fun `should break when no more keto meals are available`() {
        every { suggestionMealsUseCase.suggestKetoMeal(any()) } returns null

        suggestionMealsService.launchKetoMealHelper()

        verify {
            consoleIO.viewWithLine("😔 No more keto meals to suggest.")
        }
    }

    @Test
    fun `should handle no response and show next meal`() {
        // Given
        val meal1 = createKetoMeal(1, "Keto Burger")
        val meal2 = createKetoMeal(2, "Keto Salad")
        every { suggestionMealsUseCase.suggestKetoMeal(any()) } returnsMany listOf(meal1, meal2)
        every { consoleIO.readInput() } returnsMany listOf("no", "exit")

        // When
        suggestionMealsService.launchKetoMealHelper()

        // Then
        verify {
            consoleIO.viewWithLine("\n🥑 Keto Meal: Keto Burger")
            consoleIO.viewWithLine("🔄 Okay! Let's try another one.")
            consoleIO.viewWithLine("\n🥑 Keto Meal: Keto Salad")
        }
    }

    @Test
    fun `should handle invalid input`() {
        // Given
        val ketoMeal = createKetoMeal(1, "Keto Burger")
        every { suggestionMealsUseCase.suggestKetoMeal(any()) } returns ketoMeal
        every { consoleIO.readInput() } returnsMany listOf("invalid", "exit")

        // When
        suggestionMealsService.launchKetoMealHelper()

        // Then
        verify {
            consoleIO.viewWithLine("\n🥑 Keto Meal: Keto Burger")
            consoleIO.viewWithLine("⚠️ Please type 'yes' or 'no'")
        }
    }

    private fun createKetoMeal(id: Int, name: String): Meal {
        return Meal(
            name = name,
            id = id,
            minutes = 30,
            contributorId = 1,
            submitted = LocalDate(2005, 9, 16),
            tags = listOf("keto"),
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
            description = "A keto-friendly meal",
            ingredients = listOf("Ingredient 1", "Ingredient 2"),
            nIngredients = 2
        )
    }
}