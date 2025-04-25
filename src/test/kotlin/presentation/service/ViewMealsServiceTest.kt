package presentation.service


import fake.seafoodMealOrders
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.beijing.logic.usecases.ManageMealsViewUseCase
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
    fun handleUserChoice() {
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