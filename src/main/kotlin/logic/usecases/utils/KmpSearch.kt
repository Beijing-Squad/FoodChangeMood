package org.beijing.logic.usecases.utils

object KmpSearch {

    fun containsPattern(searchedFullText: String, searchedPattern: String): Boolean {
        if (searchedPattern.isEmpty()) return true

        val longestPrefixSuffix = computeLpsArray(searchedPattern)
        var textIndex = 0
        var patternIndex = 0

        while (textIndex < searchedFullText.length) {
            if (searchedPattern[patternIndex] == searchedFullText[textIndex]) {
                textIndex++
                patternIndex++
            }

            if (patternIndex == searchedPattern.length) {
                return true
            } else if (textIndex < searchedFullText.length && searchedPattern[patternIndex] != searchedFullText[textIndex]) {
                if (patternIndex != 0) {
                    // Use LPS array to avoid redundant comparisons
                    patternIndex = longestPrefixSuffix[patternIndex - 1]
                } else {
                    textIndex++
                }
            }
        }
        return false
    }

    private fun computeLpsArray(wordToSearchBy: String): IntArray {
        val lps = IntArray(wordToSearchBy.length)
        var prefixLength = 0
        var currentIndex = 1

        while (currentIndex < wordToSearchBy.length) {
            if (wordToSearchBy[currentIndex] == wordToSearchBy[prefixLength]) {
                prefixLength++
                lps[currentIndex] = prefixLength
                currentIndex++
            } else {
                if (prefixLength != 0) {
                    // Fall back to previous LPS value
                    prefixLength = lps[prefixLength - 1]
                } else {
                    lps[currentIndex] = 0
                    currentIndex++
                }
            }
        }
        return lps
    }
}