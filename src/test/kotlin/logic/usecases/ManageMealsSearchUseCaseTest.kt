package logic.usecases

import fake.createMeal
import fake.createMealForSearchByName
import io.mockk.every
import io.mockk.mockk
import org.beijing.logic.MealRepository
import org.beijing.logic.usecases.ManageMealsSearchUseCase
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

class ManageMealsSearchUseCaseTest {

    private lateinit var mealRepository: MealRepository
    private lateinit var useCase: ManageMealsSearchUseCase

    @BeforeEach
    fun setup() {
        mealRepository = mockk()
        useCase = ManageMealsSearchUseCase(mealRepository)
    }

    //region get meal by date
    @Test
    fun getMealsByDate() {
    }
//endregion

    //region get meal by date and id
    @Test
    fun getMealByDateAndId() {
    }
//endregion

    //region get gym helper meals by calories
    @Test
    fun getGymHelperMealsByCaloriesAndProtein() {
    }
//endregion

    // region get meal by name
    @Test
    fun `should return meals that contain the keyword`(){
        //Given
        val query = "Chicken"
        every { mealRepository.getAllMeals() } returns listOf(
            createMealForSearchByName("Spicy Chili Chicken Bowl",87),
            createMealForSearchByName("Triple Fire Chicken Sandwich",98985),
        )

        //When
        val result = useCase.getMealByName(query)

        //Then
        assertEquals(2, result.size)
        assertTrue(result.any { it.name == "Spicy Chili Chicken Bowl" })
        assertTrue(result.any { it.name == "Triple Fire Chicken Sandwich" })

    }

    @Test
    fun `should throw exception if search query is blank`(){
        //Given
        val blankQuery = "   "

        //When
        val exception = assertThrows<IllegalArgumentException> {
            useCase.getMealByName(blankQuery)
        }

        //Then
        assertEquals("Search query must not be blank.", exception.message)

    }

    @Test
    fun `should throw exception if no meals exist in repository`(){
        //Given
        val query = "Chili"
        every { mealRepository.getAllMeals() } returns emptyList()

        //When
        val exception = assertThrows<IllegalStateException> {
            useCase.getMealByName(query)
        }

        //Then
        assertEquals("No food data available to search.", exception.message)

    }

    @Test
    fun `should return meals that contain the word, regardless of the case`(){
        //Given
        val query = "cHiLi"
        every { mealRepository.getAllMeals() } returns listOf(createMealForSearchByName("Spicy Chili Chicken Bowl",87))

        //When
        val result = useCase.getMealByName(query)

        //Then
        assertEquals(1, result.size)
        assertEquals("Spicy Chili Chicken Bowl", result.first().name)
    }

    @Test
    fun `should return empty list if meal name does not match`(){
        //Given
        val query = "Kosharii"
        every { mealRepository.getAllMeals() } returns listOf(
            createMealForSearchByName("Hmam Baldy",323584),
        )

        //When
        val result = useCase.getMealByName(query)

        //Then
        assertTrue(result.isEmpty())
    }
    //endregion

    //region get meal by country
    @Test
    fun getMealByCountry() {
    }
//endregion

    //region get iraqi meals
    @Test
    fun getIraqiMeals() {
    }
    //endregion
}