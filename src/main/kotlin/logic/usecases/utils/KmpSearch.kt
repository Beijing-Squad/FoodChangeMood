package org.beijing.logic.usecases.utils

object KmpSearch {

    fun containsPattern(searchedFullText: String, searchedPattern: String): Boolean {
        if (searchedPattern.isEmpty()) return true

        val longestPrefixSuffix = computeLpsArray(searchedPattern)
        var textIndex = 0
        var patternIndex = 0

        while (textIndex < searchedFullText.length) {
            when {
                searchedPattern[patternIndex] == searchedFullText[textIndex] -> {
                    textIndex++
                    patternIndex++
                }

                patternIndex == searchedPattern.length -> {
                    return true
                }

                textIndex < searchedFullText.length && searchedPattern[patternIndex] != searchedFullText[textIndex] -> {
                    when {
                        patternIndex != 0 -> {
                            patternIndex = longestPrefixSuffix[patternIndex - 1]
                        }
                        else -> {
                            textIndex++
                        }
                    }
                }
            }
        }

        return patternIndex == searchedPattern.length
    }

    private fun computeLpsArray(wordToSearchBy: String): IntArray {
        val lps = IntArray(wordToSearchBy.length)
        var prefixLength = 0
        var currentIndex = 1

        while (currentIndex < wordToSearchBy.length) {
            when {
                wordToSearchBy[currentIndex] == wordToSearchBy[prefixLength] -> {
                    prefixLength++
                    lps[currentIndex] = prefixLength
                    currentIndex++
                }

                prefixLength != 0 -> {
                    prefixLength = lps[prefixLength - 1]
                }

                else -> {
                    lps[currentIndex] = 0
                    currentIndex++
                }
            }
        }
        return lps
    }
}