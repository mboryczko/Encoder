package vigenere

import decryptor.word.FileReader


class VigenereAlgorithm{

    companion object{
        val START = 'a'.toInt()
        val END = 'z'.toInt()
        val NR = END - START + 1
    }

    private val alphabetTable = Array(NR) { CharArray(NR) }
    private val originalKey = FileReader("vigenere/key.txt").readFileDirectlyAsText()
    private val textToEncrypt = FileReader("vigenere/textToEncrypt.txt").readFileDirectlyAsText()

    init {
        prepareTable()
        print("\n\n")
        println("original key: $originalKey")
        println("text     : $textToEncrypt")
    }

    private fun printTableLetter(currentLetter: Char, i: Int, j: Int){
        if(i != 0 && j == 0){
            print("\n $currentLetter")
        }else{
            //print(" $j:$currentLetter")
            print(" $currentLetter")
        }
    }

    private fun prepareTable(){
        for(i in 0 until NR){
            for(j in 0 until NR){
                val currentLetter: Char  = ((i + j ) % NR + START).toChar()
                alphabetTable[i][j] = currentLetter
                printTableLetter(currentLetter, i, j)
            }
        }
    }

    private fun getIndex(char: Char): Int = char.toInt() - START
    private fun getIndex(char: Char, row: CharArray): Int{
        for(i in 0 until row.size){
            if(row[i] == char)
                return i
        }

        return -1
    }

    fun adjustKey(): String{
        var output = ""
        var j = 0
        for(i in 0 until textToEncrypt.length){
            if(textToEncrypt[i] == ' '){
                output +=  ' '
            }else{
                output +=  originalKey[j % originalKey.length]
                j += 1
            }
        }
        return output
    }

    fun encrypt(): String{
        var encypted = ""
        val key = adjustKey()
        println("key      : $key")
        for(i in 0 until textToEncrypt.length){
            val letterToEncrypt = textToEncrypt[i]
            val xIndex = getIndex(letterToEncrypt)
            val yIndex = getIndex(key[i])
            encypted += if(letterToEncrypt == ' ') ' ' else alphabetTable[xIndex][yIndex]
        }

        println("encrypted: $encypted")
        return encypted
    }

    fun decrypt(encrypted: String): String{
        var decrypted = ""
        val key = adjustKey()

        for(i in 0 until encrypted.length) {
            val encryptedLetter = encrypted[i]
            if(encryptedLetter != ' '){
                val yIndex = getIndex(key[i])
                val xIndex = getIndex(encryptedLetter, alphabetTable[yIndex])
                decrypted += if (encryptedLetter == ' ') ' ' else alphabetTable[0][xIndex]
            }else{
                decrypted += ' '
            }
        }

        println("decrypted: $decrypted")
        return decrypted
    }

}