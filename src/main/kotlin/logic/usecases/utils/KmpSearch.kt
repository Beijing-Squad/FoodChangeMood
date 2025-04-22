package org.beijing.logic.usecases.utils

object KmpSearch {

    fun containsPattern(text: String, pattern: String): Boolean {
        if (pattern.isEmpty()) return true

        val longestPrefixSuffix = computeLpsArray(pattern)
        var textIndex = 0
        var patternIndex = 0

        while (textIndex < text.length) {
            if (pattern[patternIndex] == text[textIndex]) {
                textIndex++
                patternIndex++
            }

            if (patternIndex == pattern.length) {
                return true
            } else if (textIndex < text.length && pattern[patternIndex] != text[textIndex]) {
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