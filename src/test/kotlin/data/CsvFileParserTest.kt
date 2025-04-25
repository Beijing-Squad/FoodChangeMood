package data

import com.google.common.truth.Truth.assertThat
import fake.RecordParserTestData
import org.beijing.data.CsvFileParser
import org.junit.jupiter.api.assertThrows
import kotlin.test.Test

class CsvFileParserTest {
    private val parser: CsvFileParser = CsvFileParser()

    @Test
    fun `should return the correct meal from a valid input`() {
        //Given
        val record = RecordParserTestData.recordWithAllDataCorrect()
        val expectedMeal = RecordParserTestData.mealWithAllDataCorrect()

        //When
        val result = parser.parseCsvFileContent(record)

        //Then
        assertThat(result[0]).isEqualTo(expectedMeal)
    }

    @Test
    fun `should return meal with empty string for description`() {
        // Given
        val record = RecordParserTestData.recordWithNoDescription()
        val expectedMeal = RecordParserTestData.mealWithNoDescription()

        //When
        val result = parser.parseCsvFileContent(record)

        //Then
        assertThat(result[0]).isEqualTo(expectedMeal)
    }

    @Test
    fun `should return a meal with empty tags if there is none`() {
        //Given
        val record = RecordParserTestData.recordWithNoDescription()
        val expectedMeal = RecordParserTestData.mealWithNoDescription()

        //When
        val result = parser.parseCsvFileContent(record)

        //Then
        assertThat(result[0]).isEqualTo(expectedMeal)
    }

    @Test
    fun `should return a meal with empty nutrition if there is none`() {
        // Given
        val record = RecordParserTestData.recordWithMissingNutrition()
        val expectedMeal = RecordParserTestData.mealWithMissingNutrition()

        //When
        val result = parser.parseCsvFileContent(record)

        //Then
        assertThat(result[0]).isEqualTo(expectedMeal)

    }

    @Test
    fun `should return a meal with empty steps if there is none`() {
        // Given
        val record = RecordParserTestData.recordWithMissingSteps()
        val expectedMeal = RecordParserTestData.mealWithMissingSteps()

        //When
        val result = parser.parseCsvFileContent(record)

        //Then
        assertThat(result[0]).isEqualTo(expectedMeal)
    }

    @Test
    fun `should return meal with multi-line description`() {
        // Given
        val record = RecordParserTestData.recordWithMultiLineDescription()
        val expectedMeal = RecordParserTestData.mealWithMultiLineDescription()

        //When
        val result = parser.parseCsvFileContent(record)

        //Then
        assertThat(result[0]).isEqualTo(expectedMeal)
    }

    @Test
    fun `should return a meal with empty ingredients if there is none`() {
        //Given
        val record = RecordParserTestData.recordWithMissingIngredients()
        val expectedMeal = RecordParserTestData.mealWithMissingIngredients()

        //When
        val result = parser.parseCsvFileContent(record)

        //Then
        assertThat(result[0]).isEqualTo(expectedMeal)
    }

    @Test
    fun `should throw Exception for CSV line with fewer than 11 fields`() {
        //Given
        val record = RecordParserTestData.recordWithAllDataCorrect()
        val expectedMeal = RecordParserTestData.recordWithFewerThanNumberOfFields()

        // When && Then
        assertThrows<Exception> {
            parser.parseCsvFileContent(expectedMeal)
        }
    }

    @Test
    fun `should parse CSV line with missing ingredient correctly`() {
        //Given
        val record = RecordParserTestData.recordWithMissingStep()
        val expectedMeal = RecordParserTestData.mealWithMissingStep()

        //When
        val result = parser.parseCsvFileContent(record)

        //Then
        assertThat(result[0]).isEqualTo(expectedMeal)
    }

}