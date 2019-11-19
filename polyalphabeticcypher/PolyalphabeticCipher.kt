package polyalphabeticcypher

import decryptor.word.FileReader
import java.util.*

class PolyalphabeticCipher{

    companion object{
        val START = 'a'.toInt()
        val END = 'z'.toInt()
        val NR = END - START + 1
    }
    private val rand = Random()
    private val textToEncrypt = FileReader("vigenere/textToEncrypt.txt").readFileDirectlyAsText()
    private val alphabetTable = mutableListOf<String>()
    private fun generateTwoRandomLetters(): String{
        val first = rand.nextInt(END - START) + START
        val second = rand.nextInt(END - START) + START
        return "${first.toChar()}${second.toChar()}"
    }

    init {
        generateTable()
    }

    private fun generateTable(){
        var i = 0
        while (i < NR){
            val randomLetters = generateTwoRandomLetters()
            if(!alphabetTable.contains(randomLetters)){
                alphabetTable.add(randomLetters)
                println("$i ${(i+ START).toChar()}: $randomLetters")
                i += 1
            }
        }

    }

    fun encrypt(): String{
        var encrypted = ""
        for(i in 0 until  textToEncrypt.length){
            val letterToEncrypt = textToEncrypt[i]
            val index = letterToEncrypt.toInt() - START
            if(letterToEncrypt != ' '){
                encrypted += alphabetTable[index]
            }else{
                encrypted += ' '
            }
        }

        println("encrypted: $encrypted")
        return encrypted
    }

    fun decrypt(encrypted: String): String{
        var decrypted = ""
        var i = 0
        while (i < encrypted.length - 1){
            val en1 = encrypted[i]
            val en2 = encrypted[i+1]
            val c = "$en1$en2"

            if(en1 != ' '){
                decrypted += (alphabetTable.indexOfFirst { it == c } + START).toChar()
                i += 2
            }else{
                decrypted += ' '
                i += 1
            }

        }

        println("decrypted: $decrypted")
        return encrypted
    }

}