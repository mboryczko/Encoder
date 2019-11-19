package decryptor.word

import decryptor.model.Word
import java.io.File

class FileReader(val file: String){

    fun readFileDirectlyAsText(fileName: String = file): String
            = File(fileName).readText(Charsets.UTF_8)

    fun getMostPopularWords(): List<Word>{
        val wordsOutput = readFileDirectlyAsText(file)
        val words = mutableListOf<String>()
        val values = mutableListOf<Int>()

        var currentWord = ""
        var currentValue = ""

        for (i in 0 until wordsOutput.length - 1){
            val currentLetter = wordsOutput[i]
            val nextLetter = wordsOutput[i+1]

            if(currentLetter.isDigit()){
                currentValue += currentLetter

                if(nextLetter == ' '){
                    words.add(StringBuffer(currentWord).toString())
                    values.add(StringBuffer(currentValue).toString().toInt())
                    currentWord = ""
                    currentValue = ""
                }
            }else if(currentLetter != '='){
                currentWord += currentLetter
            }
        }

        val output = mutableListOf<Word>()
        for (i in 0 until words.size){
            val word = words[i].trim()
            output.add(Word(word, values[i]))
        }

        return output
    }
}