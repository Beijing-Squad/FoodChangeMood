package org.beijing.data

import java.io.File

class CsvFileReader(
    private val file: File
) {
    fun readLine(): List<String> = file.readLines()
}