package org.beijing.data

import java.io.File

class CsvFileReader(
    private val file: File
) {

    fun readLinesFromFile(): List<String> {
        return file.readLines()
    }

}