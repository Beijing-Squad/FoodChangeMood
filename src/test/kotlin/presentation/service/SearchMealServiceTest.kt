package presentation.service

import fake.createMeal
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.beijing.logic.usecases.ManageMealsSearchUseCase
import org.beijing.presentation.ViewMealDetails
import org.beijing.presentation.service.SearchMealService
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import presentation.view_read.ConsoleIO


class SearchMealServiceTest {
    private lateinit var manageMealsSearch: ManageMealsSearchUseCase
    private lateinit var searchMealService: SearchMealService
    private lateinit var viewMealDetails: ViewMealDetails
    private lateinit var consoleIO: ConsoleIO

    @BeforeEach
    fun setup() {
        manageMealsSearch = mockk(relaxed = true)
        viewMealDetails = mockk(relaxed = true)
        consoleIO = mockk(relaxed = true)
        searchMealService = SearchMealService(manageMealsSearch, viewMealDetails, consoleIO)
    }

    //region handle user choice
    @Test
    fun `should show option of sub feature when search meal selected`() {
        // When
        searchMealService.showOptionService()

        // Then
        verify {
            consoleIO.viewWithLine("\n\n===Please enter one of the numbers listed below===\n")
            consoleIO.viewWithLine("1. Gym Helper")
            consoleIO.viewWithLine("2. Search by name of meal")
            consoleIO.viewWithLine("3. Search By Date And See Meal Details")
            consoleIO.viewWithLine("4. Explore Country Meals")
            consoleIO.viewWithLine("5. Iraqi Meals")
            consoleIO.viewWithLine("0. Exit")
        }

    }
    //endregion

    //region gym helper
    @Test
    fun `should call gym helper when gym helper selected`() {
        // Given
        val choiceSearchFeature = "1"
        val exitInput = "0"
        every { consoleIO.readInput() } returns choiceSearchFeature andThen exitInput

        // When
        searchMealService.handleUserChoice()

        // Then
        verify {
            consoleIO.view("enter target of Calories: ")
            consoleIO.view("enter target of Protein: ")
        }
    }

    @Test
    fun `should call gym helper when valid input selected`() {
        // Given
        val targetCalories = 500.0
        val targetProtein = 30.0
        val meal = createMeal("egg", 1, targetCalories, targetProtein)
        every {
            consoleIO.readInput()
        } returns "1" andThen "$targetCalories" andThen "$targetProtein"

        every {
            manageMealsSearch.getGymHelperMealsByCaloriesAndProtein(targetCalories, targetProtein)
        } returns listOf(meal)

        // When
        searchMealService.showService()

        // Then
        verify {
            consoleIO.view("enter target of Calories: ")
            consoleIO.view("enter target of Protein: ")
            manageMealsSearch.getGymHelperMealsByCaloriesAndProtein(targetCalories, targetProtein)
            consoleIO.viewWithLine(any())
        }
    }

    @Test
    fun `should show message no meals found  when invalid input`() {
        // Given
        val targetCalories = -100.0
        val targetProtein = 100.0
        val choiceSearchFeature = "1"

        every {
            consoleIO.readInput()
        } returns choiceSearchFeature andThen "$targetCalories" andThen "$targetProtein"

        // When
        searchMealService.showService()

        // Then
        verify {
            consoleIO.view("enter target of Calories: ")
            consoleIO.view("enter target of Protein: ")
            manageMealsSearch.getGymHelperMealsByCaloriesAndProtein(-100.0, 100.0)
            consoleIO.viewWithLine("\n⚠️ No meals found!\n🍽️ Try searching again or check your filters.\n")
        }
    }

    @Test
    fun `should recursively call itself when throw exception`() {
        // Given
        val errorMessage = "Invalid meal data"
        every {
            consoleIO.readInput()
        } returns "1" andThen "500.0" andThen "30.0" andThen "400.0" andThen "25.0" andThen "0"

        every {
            manageMealsSearch.getGymHelperMealsByCaloriesAndProtein(500.0, 30.0)
        } throws Exception(errorMessage)

        // When
        searchMealService.showService()

        // Then
        verify {
            consoleIO.view("enter target of Calories: ")
            consoleIO.readInput()
            consoleIO.view("enter target of Protein: ")
            consoleIO.readInput()
            manageMealsSearch.getGymHelperMealsByCaloriesAndProtein(500.0, 30.0)
            consoleIO.viewWithLine(errorMessage)

        }
    }
    //endregion

    //region Iraqi Meals Tests
    @Test
    fun `should display Iraqi meal details correctly when selected`() {
        // Given
        val iraqiMeal = createMeal(
            name = "Masgouf",
            id = 1,
            tags = listOf("Iraqi", "Grilled"),
            description = "Traditional Iraqi grilled fish"
        )
        every { manageMealsSearch.getIraqiMeals() } returns listOf(iraqiMeal)
        every { consoleIO.readInput() } returns "5" andThen "1" andThen "0" // Select Iraqi option, then meal, then exit

        // When
        searchMealService.handleUserChoice()

        // Then
        verify {
            consoleIO.viewWithLine("Masgouf")
            consoleIO.viewWithLine(match { it.contains("Traditional Iraqi grilled fish") })
        }
    }

    @Test
    fun `should show proper message when no Iraqi meals found`() {
        // Given
        every { manageMealsSearch.getIraqiMeals() } returns emptyList()
        every { consoleIO.readInput() } returns "5" andThen "0"

        // When
        searchMealService.handleUserChoice()

        // Then
        verify {
            consoleIO.viewWithLine("\n⚠️ No Iraqi meals found!\n")
        }
    }

    @Test
    fun `should handle case insensitive Iraqi tags`() {
        // Given
        val meal1 = createMeal(
            name = "Dolma",
            tags = listOf("iraqi", "Vegetarian")
        )
        val meal2 = createMeal(
            name = "Qoozi",
            tags = listOf("IRAQI", "Meat")
        )
        every { manageMealsSearch.getIraqiMeals() } returns listOf(meal1, meal2)
        every { consoleIO.readInput() } returns "5" andThen "0"

        // When
        searchMealService.handleUserChoice()

        // Then
        verify(exactly = 2) {
            consoleIO.viewWithLine(any())
        }
    }

    @Test
    fun `should handle meals with Iraqi in description`() {
        // Given
        val meal = createMeal(
            name = "Tashreeb",
            description = "Traditional iraqi bread soup",
            tags = listOf("Middle Eastern")
        )
        every { manageMealsSearch.getIraqiMeals() } returns listOf(meal)
        every { consoleIO.readInput() } returns "5" andThen "0"

        // When
        searchMealService.handleUserChoice()

        // Then
        verify {
            consoleIO.viewWithLine("Tashreeb")
        }
    }

    @Test
    fun `should show all matching Iraqi meals`() {
        // Given
        val meal1 = createMeal("Masgouf", tags = listOf("Iraqi"))
        val meal2 = createMeal("Tashreeb", description = "Iraqi bread soup")
        val meal3 = createMeal("Dolma", tags = listOf("iraqi"))

        every { manageMealsSearch.getIraqiMeals() } returns listOf(meal1, meal2, meal3)
        every { consoleIO.readInput() } returns "5" andThen "0"

        // When
        searchMealService.handleUserChoice()

        // Then
        verify(exactly = 3) {
            consoleIO.viewWithLine(any())
        }
    }

    @Test
    fun `should handle special characters in Iraqi meals`() {
        // Given
        val meal = createMeal(
            name = "Biryani Baghdadi",
            description = "Iraqi-style biryani with special spices",
            tags = listOf("Iraqi", "Spicy")
        )
        every { manageMealsSearch.getIraqiMeals() } returns listOf(meal)
        every { consoleIO.readInput() } returns "5" andThen "0"

        // When
        searchMealService.handleUserChoice()

        // Then
        verify {
            consoleIO.viewWithLine("Biryani Baghdadi")
        }
    }
//endregion

}