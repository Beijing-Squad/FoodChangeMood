package logic.usecases.utils

import org.beijing.logic.usecases.utils.KmpSearch
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

class KmpSearchTest {

    @Test
    fun `should return true when pattern exists in the text`(){
        //Given
        val text = "Hi, I'm Mohamed Ibrahim"
        val pattern = "mohamed"

        //When
        val result = KmpSearch.containsPattern(text, pattern)

        //Then
        assertTrue(result)
    }
    @Test
    fun `should return true when pattern is empty`(){
        //Given
        val text = "Hello Guys"
        val pattern = ""

        //When
        val result = KmpSearch.containsPattern(text, pattern)

        //Then
        assertTrue(result)
    }

    @Test
    fun `should return false when pattern doesn't exist`(){
        //Given
        val text = "Yesterday, Real Madrid's match was boring."
        val pattern = "Barcelona"

        //When
        val result = KmpSearch.containsPattern(text, pattern)

        //Then
        assertFalse(result)
    }
    @Test
    fun `should return false when pattern is longer than text`() {
        //Given
        val text = "short"
        val pattern = "longerpattern"

        //When
        val result = KmpSearch.containsPattern(text, pattern)

        //Then
        assertFalse(result)
    }


}