package org.beijing.logic.usecases.utils

class KmpSubstringSearch {

    /**
     * Returns true if the pattern exists within the text using the KMP algorithm.
     */
    fun doesTextContainPattern(text: String, pattern: String): Boolean {
        if (pattern.isEmpty()) return true // Empty pattern is always considered found

        val longestPrefixSuffix = buildLpsArray(pattern)
        var textIndex = 0
        var patternIndex = 0

        while (textIndex < text.length) {
            if (pattern[patternIndex] == text[textIndex]) {
                textIndex++
                patternIndex++
            }

            if (patternIndex == pattern.length) {
                return true // Full pattern matched in text
            } else if (textIndex < text.length && pattern[patternIndex] != text[textIndex]) {
                if (patternIndex != 0) {
                    // Use LPS array to avoid redundant comparisons
                    patternIndex = longestPrefixSuffix[patternIndex - 1]
                } else {
                    textIndex++
                }
            }
        }

        return false // Pattern not found
    }

    /**
     * Builds the LPS (Longest Prefix which is also Suffix) array used by KMP algorithm
     */
    private fun buildLpsArray(pattern: String): IntArray {
        val lps = IntArray(pattern.length) // LPS array
        var length = 0 // Length of the previous longest prefix suffix
        var i = 1

        while (i < pattern.length) {
            if (pattern[i] == pattern[length]) {
                length++
                lps[i] = length
                i++
            } else {
                if (length != 0) {
                    // Fall back to previous LPS value
                    length = lps[length - 1]
                } else {
                    lps[i] = 0
                    i++
                }
            }
        }

        return lps
    }
}