package presentation.service


import fake.createMeal
import fake.seafoodMealOrders
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.beijing.logic.usecases.ManageMealsViewUseCase
import org.beijing.model.Nutrition
import org.beijing.presentation.service.ViewMealsService
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import presentation.view_read.ConsoleIO

class ViewMealsServiceTest {
    private lateinit var useCase: ManageMealsViewUseCase
    private lateinit var service: ViewMealsService
    private lateinit var consoleIO: ConsoleIO

    @BeforeEach
    fun setup() {
        useCase = mockk(relaxed = true)
        consoleIO = mockk(relaxed = true)
        service = ViewMealsService(useCase, consoleIO)
    }

    //region handle user choice
    @Test
    fun `should show option of sub feature when view meal selected`() {
        //When
        service.showOptionService()

        //Then
        verify {
            consoleIO.viewWithLine("\n\n ===Please enter one of the numbers listed below===\n")
            consoleIO.viewWithLine("1. Show Healthy Quick Prepared Meals")
            consoleIO.viewWithLine("2. Show SeaFood Sorted By Protein Content")
            consoleIO.viewWithLine("0. Exit")
        }
    }
    //endregion

    //region get ten healthy quick meals
    @Test
    fun `launchHealthyQuickPreparedMeals should called when selected`() {
        // Given
        val choiceViewFeature = "1"

        every { consoleIO.readInput() } returns choiceViewFeature
        every { useCase.getHealthyQuickPreparedMeals() } returns listOf(
            createMeal(
                name = "Quick Salad",
                minutes = 10,
                nutrition = Nutrition(
                    0.0, 5.0, 0.0, 0.0, 0.0, 2.0, 10.0
                )
            ),
            createMeal(
                name = "Fast Food Burger",
                minutes = 10,
                nutrition = Nutrition(
                    0.0, 20.0, 0.0, 0.0, 0.0, 10.0, 30.0
                )
            )
        )

        // When
        service.handleUserChoice()

        // Then
        verify {
            useCase.getHealthyQuickPreparedMeals()

        }
    }

    @Test
    fun `should show message when empty list of meals passed`() {
        // Given
        val choiceViewFeature = "1"

        every { consoleIO.readInput() } returns choiceViewFeature
        every { useCase.getHealthyQuickPreparedMeals() } returns listOf()

        // When
        service.handleUserChoice()

        // Then
        verify {
            consoleIO.viewWithLine("There is no healthy quick-prepared meals.")
        }
    }
    //endregion

    //region seafood meals test
    @Test
    fun `should call show sorted seaFood by protein when Show SeaFood Sorted By Protein Content selected`() {
        // Given
        val choiceViewFeature = "2"
        every { consoleIO.readInput() } returns choiceViewFeature
        every { useCase.getSortedSeaFoodByProtein() } returns seafoodMealOrders

        // When
        service.handleUserChoice()

        // Then
        verify { useCase.getSortedSeaFoodByProtein() }
    }

    //endregion
}