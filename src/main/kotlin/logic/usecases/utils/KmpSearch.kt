package org.beijing.logic.usecases.utils

object KmpSearch {

    fun containsPattern(searchedFullText: String, searchedPattern: String): Boolean {
        val textLower = searchedFullText.lowercase()
        val patternLower = searchedPattern.lowercase()

        if (patternLower.isEmpty()) return true

        val longestPrefixSuffix = computeLpsArray(patternLower)
        var textIndex = 0
        var patternIndex = 0

        while (textIndex < textLower.length) {

            if (patternIndex == patternLower.length) return true

            when {
                patternLower[patternIndex] == textLower[textIndex] -> {
                    textIndex++
                    patternIndex++
                }

                patternIndex == patternLower.length -> {
                    return true
                }

                textIndex < textLower.length && patternLower[patternIndex] != textLower[textIndex] -> {
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