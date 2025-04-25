package presentation.service

import fake.createMeal
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.datetime.LocalDate
import org.beijing.logic.usecases.ManageMealsSearchUseCase
import org.beijing.presentation.ViewMealDetails
import org.beijing.presentation.service.SearchMealService
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource
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

    // region search by date
    @Test
    fun `should call get meals by date when search by date selected`() {
        // Given
        val searchByDateFeature = "3"
        val date = "2025-03-10"
        every { consoleIO.readInput() } returns searchByDateFeature andThen date
        every { manageMealsSearch.getMealsByDate(date) } returns listOf(
            createMeal(submitted = LocalDate(2025, 2, 10)),
            createMeal(submitted = LocalDate(2025, 3, 10)),
            createMeal(submitted = LocalDate(2025, 3, 10)),
            createMeal(submitted = LocalDate(2025, 4, 10)),
        )

        // When
        searchMealService.handleUserChoice()

        // Then
        verify { manageMealsSearch.getMealsByDate(date) }
    }

    @Test
    fun `should display error message when no meals found on the date`() {
        // Given
        val searchByDateFeature = "3"
        val date = "2025-01-05"
        val errorMessage = "❌ No Meals Found For The Date [$date]."
        every { consoleIO.readInput() } returns searchByDateFeature andThen date
        every { manageMealsSearch.getMealsByDate(date) } returns listOf(
            createMeal(submitted = LocalDate(2025, 2, 10)),
            createMeal(submitted = LocalDate(2025, 3, 10)),
            createMeal(submitted = LocalDate(2025, 3, 10)),
            createMeal(submitted = LocalDate(2025, 4, 10)),
        )
        every { manageMealsSearch.getMealsByDate(date) } throws Exception(errorMessage)

        // When
        searchMealService.handleUserChoice()

        // Then
        verify { consoleIO.viewWithLine(errorMessage) }
    }

    @ParameterizedTest
    @CsvSource(
        "2023 05 10",
        "2023@05@10",
        "20230615",
        "2023-600-01",
        "2023/01/02",
        "2023--32",
    )
    fun `should display error message and call itself while date is invalid`(date: String) {
        // Given
        val searchByDateFeature = "3"
        val errorMessage = "❌ Invalid date format. Please use (YYYY-MM-DD)."
        every { consoleIO.readInput() } returns searchByDateFeature andThen date
        every { manageMealsSearch.getMealsByDate(date) } throws IllegalArgumentException(errorMessage)

        // When
        searchMealService.handleUserChoice()

        // Then
        verify { consoleIO.viewWithLine(errorMessage) }
    }

    @Test
    fun `should view meals when found meals on the date`() {
        // Given
        val searchByDateFeature = "3"
        val date = "2025-03-10"
        every { consoleIO.readInput() } returns searchByDateFeature andThen date
        every { manageMealsSearch.getMealsByDate(date) } returns listOf(
            createMeal(submitted = LocalDate(2025, 2, 10)),
            createMeal(submitted = LocalDate(2025, 3, 10)),
            createMeal(submitted = LocalDate(2025, 3, 10)),
            createMeal(submitted = LocalDate(2025, 4, 10)),
        )

        // When
        searchMealService.handleUserChoice()

        // Then
        verify {
            consoleIO.viewWithLine(any())
        }
    }

    @Test
    fun `should ask for meal details after viewing meal list`() {
        // Given
        val searchByDateFeature = "3"
        val date = "2025-03-10"
        val questionForMealDetails = "Do You Want To See Details Of A Specific Meal? (yes/no)"
        every { consoleIO.readInput() } returns searchByDateFeature andThen date
        every { manageMealsSearch.getMealsByDate(date) } returns listOf(
            createMeal(submitted = LocalDate(2025, 2, 10)),
            createMeal(submitted = LocalDate(2025, 3, 10)),
            createMeal(submitted = LocalDate(2025, 3, 10)),
            createMeal(submitted = LocalDate(2025, 4, 10)),
        )

        // When
        searchMealService.handleUserChoice()

        // Then
        verify {
            consoleIO.viewWithLine(questionForMealDetails)
        }
    }
    // endregion

}