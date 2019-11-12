package word

import extensions.round
import model.Letter
import model.LetterTranslation
import model.Word
import java.io.File

class FilePrinter(){

    companion object{
        fun writeToFile(fileName: String, text: String){
            val file = File(fileName)
            file.writeText(text)
        }

        fun writeLettersWithTranslationToFile(fileName: String, words: List<LetterTranslation>, proper: List<LetterTranslation>){
            val file = File(fileName)
            var output = ""
            words.forEach {w->
                val properEncrypted = proper.find { it.cryptedChar == w.cryptedChar }?.encryptedChar ?: ""
                val isOk = if(properEncrypted == w.encryptedChar) "✓" else ""
                output += "crypted: ${w.cryptedChar} ; encrypted: ${w.encryptedChar}; should be: ${properEncrypted} $isOk \n"
            }

            file.writeText(output)
        }

        fun writeLettersWithTranslationToFile(fileName: String, words: List<LetterTranslation>){
            val file = File(fileName)
            var output = ""
            words.forEach {w->
                output +=   "crypted: ${w.cryptedChar} ; encrypted: ${w.encryptedChar} \n"
            }

            file.writeText(output)
        }

        fun writeWordsToFile(fileName: String, words: List<Word>){
            val file = File(fileName)
            var output = ""
            words.forEach {w->
                output +=   "${w.word} ; ${w.value} \n"
            }

            file.writeText(output)
        }

        fun writeLettersToFile(fileName: String, words: List<Letter>){
            val file = File(fileName)
            var output = ""
            words.forEach {w->
                output +=   "${w.letter} ; ${w.value} \n"
            }

            file.writeText(output)
        }

        fun writeLettersToFileWithPercentage(fileName: String, words: List<Letter>, cryptogram: String){
            val file = File(fileName)
            val filteredCryptogram = cryptogram.replace(" ", "")
            var output = ""
            var sum = 0
            words.forEach {w->
                sum += w.value
                output +=   "${w.letter} ; ${w.value} ; ${((w.value.toDouble() / filteredCryptogram.length.toDouble()) * 100).round(2)} %\n"
            }

            output += "sum of characters: $sum | all characters: ${filteredCryptogram.length}"
            file.writeText(output)
        }

    }

}