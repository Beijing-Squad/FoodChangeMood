package org.beijing.presentation.service

import io.mockk.*
import org.beijing.logic.usecases.ManageMealsSuggestionsUseCase
import org.beijing.model.Meal
import org.beijing.model.Nutrition
import org.beijing.presentation.ViewMealDetails
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import presentation.view_read.ConsoleIO
import kotlinx.datetime.LocalDate

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
        suggestionMealsService = SuggestionMealsService(
            suggestionMeals = suggestionMealsUseCase,
            viewMealDetails = viewMealDetails,
            consoleIO = consoleIO
        )
    }

//region keto meal test
    @Test
    fun `should handle multiple invalid inputs before valid input`() {
        // Given
        val ketoMeal = createKetoMeal(1, "Keto Burger")
        every { suggestionMealsUseCase.suggestKetoMeal(any()) } returns ketoMeal
        every { consoleIO.readInput() } returnsMany listOf("1", "invalid1", "invalid2", "yes", "0")

        // When
        suggestionMealsService.showService()

        // Then
        verifyOrder {
            suggestionMealsUseCase.suggestKetoMeal(mutableSetOf())
            consoleIO.viewWithLine("\n🥑 Keto Meal: Keto Burger")
            consoleIO.viewWithLine("Short Description: A keto-friendly meal")
            consoleIO.viewWithLine("Do you like it? ❤")
            consoleIO.view("write 'yes' to get details or 'no' to get another meal (or type 'exit' to quit):")
            consoleIO.readInput()
            consoleIO.viewWithLine("⚠️ Please type 'yes' or 'no'")

            suggestionMealsUseCase.suggestKetoMeal(mutableSetOf())
            consoleIO.viewWithLine("\n🥑 Keto Meal: Keto Burger")
            consoleIO.viewWithLine("Short Description: A keto-friendly meal")
            consoleIO.viewWithLine("Do you like it? ❤")
            consoleIO.view("write 'yes' to get details or 'no' to get another meal (or type 'exit' to quit):")
            consoleIO.readInput()
            consoleIO.viewWithLine("⚠️ Please type 'yes' or 'no'")

            suggestionMealsUseCase.suggestKetoMeal(mutableSetOf())
            consoleIO.viewWithLine("\n🥑 Keto Meal: Keto Burger")
            consoleIO.viewWithLine("Short Description: A keto-friendly meal")
            consoleIO.viewWithLine("Do you like it? ❤")
            consoleIO.view("write 'yes' to get details or 'no' to get another meal (or type 'exit' to quit):")
            consoleIO.readInput()
            viewMealDetails.displayMealDetails(ketoMeal)
        }
    }

    @Test
    fun `should handle null or empty input correctly`() {
        // Given
        val ketoMeal = createKetoMeal(1, "Keto Burger")
        every { suggestionMealsUseCase.suggestKetoMeal(any()) } returns ketoMeal
        every { consoleIO.readInput() } returnsMany listOf("1", "", "exit", "0")

        // When
        suggestionMealsService.showService()

        // Then
        verifyOrder {
            suggestionMealsUseCase.suggestKetoMeal(mutableSetOf())
            consoleIO.viewWithLine("\n🥑 Keto Meal: Keto Burger")
            consoleIO.viewWithLine("Short Description: A keto-friendly meal")
            consoleIO.viewWithLine("Do you like it? ❤")
            consoleIO.view("write 'yes' to get details or 'no' to get another meal (or type 'exit' to quit):")
            consoleIO.readInput()
            consoleIO.viewWithLine("⚠️ Please type 'yes' or 'no'")
            suggestionMealsUseCase.suggestKetoMeal(mutableSetOf())
            consoleIO.viewWithLine("\n🥑 Keto Meal: Keto Burger")
            consoleIO.viewWithLine("Short Description: A keto-friendly meal")
            consoleIO.viewWithLine("Do you like it? ❤")
            consoleIO.view("write 'yes' to get details or 'no' to get another meal (or type 'exit' to quit):")
            consoleIO.readInput()
            consoleIO.viewWithLine("GoodBye")
        }
    }

    @Test
    fun `should accept uppercase and space input`() {
        // Given
        val ketoMeal = createKetoMeal(1, "Keto Burger")
        every { suggestionMealsUseCase.suggestKetoMeal(any()) } returns ketoMeal
        every { consoleIO.readInput() } returnsMany listOf("1", "  YES  ", "0")

        // When
        suggestionMealsService.showService()

        // Then
        verifyOrder {
            suggestionMealsUseCase.suggestKetoMeal(mutableSetOf())
            consoleIO.viewWithLine("\n🥑 Keto Meal: Keto Burger")
            consoleIO.viewWithLine("Short Description: A keto-friendly meal")
            consoleIO.viewWithLine("Do you like it? ❤")
            consoleIO.view("write 'yes' to get details or 'no' to get another meal (or type 'exit' to quit):")
            consoleIO.readInput()
            viewMealDetails.displayMealDetails(ketoMeal)
        }
    }

    @Test
    fun `should show another meal when user input no`() {
        // Given
        val ketoMeal1 = createKetoMeal(1, "Keto Salad")
        val ketoMeal2 = createKetoMeal(2, "Keto Pizza")
        every { suggestionMealsUseCase.suggestKetoMeal(any()) } returnsMany listOf(ketoMeal1, ketoMeal2)
        every { consoleIO.readInput() } returnsMany listOf("1", "no", "exit", "0")

        // When
        suggestionMealsService.showService()

        // Then
        verifyOrder {
            consoleIO.viewWithLine("\n🥑 Keto Meal: Keto Salad")
            consoleIO.viewWithLine("Short Description: A keto-friendly meal")
            consoleIO.viewWithLine("Do you like it? ❤")
            consoleIO.view("write 'yes' to get details or 'no' to get another meal (or type 'exit' to quit):")
            consoleIO.readInput()
            consoleIO.viewWithLine("🔄 Okay! Let's try another one.")
            consoleIO.viewWithLine("\n🥑 Keto Meal: Keto Pizza")
            consoleIO.viewWithLine("Short Description: A keto-friendly meal")
            consoleIO.viewWithLine("Do you like it? ❤")
            consoleIO.view("write 'yes' to get details or 'no' to get another meal (or type 'exit' to quit):")
            consoleIO.readInput()
            consoleIO.viewWithLine("GoodBye")
        }
    }

    @Test
    fun `should display keto meal details when user enter yes`() {
        // Given
        val ketoMeal = createKetoMeal(1, "Keto Burger")
        every { suggestionMealsUseCase.suggestKetoMeal(any()) } returns ketoMeal
        every { consoleIO.readInput() } returnsMany listOf("1", "yes", "0")

        // When
        suggestionMealsService.showService()

        // Then
        verify {
            consoleIO.viewWithLine("\n🥑 Keto Meal: Keto Burger")
            consoleIO.viewWithLine("Short Description: A keto-friendly meal")
            consoleIO.viewWithLine("Do you like it? ❤")
            consoleIO.view("write 'yes' to get details or 'no' to get another meal (or type 'exit' to quit):")
            consoleIO.readInput()
            viewMealDetails.displayMealDetails(ketoMeal)
        }
    }

    @Test
    fun `should return suitable message when no more keto meals are available`() {
        // Given
        every { suggestionMealsUseCase.suggestKetoMeal(any()) } returns null
        every { consoleIO.readInput() } returnsMany listOf("1", "0")

        // When
        suggestionMealsService.showService()

        // Then
        verify {
            consoleIO.viewWithLine("😔 No more keto meals to suggest.")
        }
    }

    @Test
    fun `should not accept null empty and whitespace inputs`() {
        // Given
        val ketoMeal = createKetoMeal(1, "Keto Burger")
        every { suggestionMealsUseCase.suggestKetoMeal(any()) } returns ketoMeal
        every { consoleIO.readInput() } returnsMany listOf("1", "", "   ", "exit", "0")

        // When
        suggestionMealsService.showService()

        // Then
        verifyOrder {
            suggestionMealsUseCase.suggestKetoMeal(mutableSetOf())
            consoleIO.viewWithLine("\n🥑 Keto Meal: Keto Burger")
            consoleIO.viewWithLine("Short Description: A keto-friendly meal")
            consoleIO.viewWithLine("Do you like it? ❤")
            consoleIO.view("write 'yes' to get details or 'no' to get another meal (or type 'exit' to quit):")
            consoleIO.readInput()
            consoleIO.viewWithLine("⚠️ Please type 'yes' or 'no'")
            suggestionMealsUseCase.suggestKetoMeal(mutableSetOf())
            consoleIO.viewWithLine("\n🥑 Keto Meal: Keto Burger")
            consoleIO.viewWithLine("Short Description: A keto-friendly meal")
            consoleIO.viewWithLine("Do you like it? ❤")
            consoleIO.view("write 'yes' to get details or 'no' to get another meal (or type 'exit' to quit):")
            consoleIO.readInput()
            consoleIO.viewWithLine("⚠️ Please type 'yes' or 'no'")
            suggestionMealsUseCase.suggestKetoMeal(mutableSetOf())
            consoleIO.viewWithLine("\n🥑 Keto Meal: Keto Burger")
            consoleIO.viewWithLine("Short Description: A keto-friendly meal")
            consoleIO.viewWithLine("Do you like it? ❤")
            consoleIO.view("write 'yes' to get details or 'no' to get another meal (or type 'exit' to quit):")
            consoleIO.readInput()
            consoleIO.viewWithLine("GoodBye")
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
    //endregion
}