package decryptor

import decryptor.model.Letter
import decryptor.model.LetterProbabilty
import decryptor.model.LetterTranslation
import decryptor.model.Word
import decryptor.word.FileReader
import java.util.*

class WordAnalyzer(){

    private var decrypted: String = ""
    private lateinit var cryptogram: String
    lateinit var mostPopularWords: List<Word>

    init {
        val fileReader = FileReader("input.txt")
        cryptogram = fileReader.readFileDirectlyAsText("kryptogram.txt")
        mostPopularWords = fileReader.getMostPopularWords()
    }

    /*
        prepared from this statistic
        https://sjp.pwn.pl/poradnia/haslo/frekwencja-liter-w-polskich-tekstach;7072.html
        a	8.91%	  w	4.65%	  p	3.13%	  g	1.42%	  ć	0.40%
        i	8.21%	  s	4.32%	  m	2.80%	  ę	1.11%	  f	0.30%
        o	7.75%	  t	3.98%	  u	2.50%	  h	1.08%	  ń	0.20%
        e	7.66%	  c	3.96%	  j	2.28%	  ą	0.99%	  q	0.14%
        z	5.64%	  y	3.76%	  l	2.10%	  ó	0.85%	  ź	0.06%
        n	5.52%	  k	3.51%	  ł	1.82%	  ż	0.83%	  v	0.04%
        r	4.69%	  d	3.25%	  b	1.47%	  ś	0.66%	  x	0.02%

        for our case where polish letters dont exist
        a	9.90%	  w	4.65%	  p	3.13%	  g	1.42%
        i	8.21%	  s	4.98%	  m	2.80%	        	  f	0.30%
        o	8,60%	  t	3.98%	  u	2.50%	  h	1.08%
        e	8.77%	  c	4.36%	  j	2.28%	        	  q	0.14%
        z	6.53%	  y	3.76%	  l	3.92%
        n	5.72%	  k	3.51%	        	        	  v	0.04%
        r	4.69%	  d	3.25%	  b	1.47%	        	  x	0.02%
     */

    private fun getLetterPopularity(): List<LetterProbabilty>{
        //26 characters
        return listOf(
                LetterProbabilty('a', 9.90),
                LetterProbabilty('i', 8.21),
                LetterProbabilty('o', 8.60),
                LetterProbabilty('e', 8.77),
                LetterProbabilty('z', 6.53),
                LetterProbabilty('n', 5.72),
                LetterProbabilty('r', 4.69),
                LetterProbabilty('w', 4.65),
                LetterProbabilty('s', 4.98),
                LetterProbabilty('t', 3.98),
                LetterProbabilty('c', 4.36),
                LetterProbabilty('y', 3.76),
                LetterProbabilty('k', 3.51),
                LetterProbabilty('d', 3.25),
                LetterProbabilty('p', 3.13),
                LetterProbabilty('m', 2.80),
                LetterProbabilty('u', 2.50),
                LetterProbabilty('i', 2.28),
                LetterProbabilty('l', 3.92),
                LetterProbabilty('b', 1.47),
                LetterProbabilty('g', 1.42),
                LetterProbabilty('h', 1.08),
                LetterProbabilty('f', 0.30),
                LetterProbabilty('q', 0.14),
                LetterProbabilty('v', 0.04),
                LetterProbabilty('x', 0.02)
        )
    }

    /*
        method for crossing in genethic algorithm
        randomly swaps two encrypted characters
     */

    fun swapOrderOfTwoLetterTranslations(population: List<LetterTranslation>): List<LetterTranslation>{
        val randomIndex = Random().nextInt(population.size - 1)
        val randomIndexToSwap = Random().nextInt(population.size - 1)

        val p1 = population[randomIndex]
        val p2 = population[randomIndexToSwap]
        val tmp = StringBuffer(p1.encryptedChar.toString())

        p1.encryptedChar = p2.encryptedChar
        p2.encryptedChar = tmp.single()

        return population
    }

    private fun swap(i1: Int, i2: Int, population: List<LetterTranslation>): List<LetterTranslation>{
        //println("swaping $i1 $i2")
        val p1 = population[i1]
        val p2 = population[i2]
        val tmp = StringBuffer(p1.encryptedChar.toString())

        p1.encryptedChar = p2.encryptedChar
        p2.encryptedChar = tmp.single()

        return population
    }

    fun swapOrderOfTwoLetterTranslations(population: List<LetterTranslation>, iterationsWithoutBetterSolution: Int): List<LetterTranslation>{
        val randomIndex = Random().nextInt(population.size - 1)
        val sign = (iterationsWithoutBetterSolution % 2 == 0)

        val randomIndexWithinSpecificRangePlus =  randomIndex + Random().nextInt(iterationsWithoutBetterSolution)
        val randomIndexWithinSpecificRangeMinus =  randomIndex - Random().nextInt(iterationsWithoutBetterSolution)

        if(!sign && randomIndexWithinSpecificRangeMinus >= 0){
            swap(randomIndex, randomIndexWithinSpecificRangeMinus, population)
            return population
        }else if(sign && randomIndexWithinSpecificRangePlus <= population.size - 1){
            swap(randomIndex, randomIndexWithinSpecificRangePlus, population)
            return population
        }else{
            swap(randomIndex, Random().nextInt(population.size - 1), population)
        }

        return population
    }

    /*
        returns initial population matching statistics from #getLetterPopularity
     */

    fun getInitialPopulation(): List<LetterTranslation>{
        val cryptedLettersSortedByCount = getCryptedLettersCount()
        val letterPopularity = getLetterPopularity()

        val initial = mutableListOf<LetterTranslation>()
        for (i in 0 until cryptedLettersSortedByCount.size){
            val cryptedLetter = cryptedLettersSortedByCount[i]
            val encryptedLetter = letterPopularity[i]
            if(cryptedLetter.letter != '\n' && cryptedLetter.letter != ' ')
            initial.add(LetterTranslation(cryptedLetter.letter, encryptedLetter.letter))
        }

        initial.sortBy { it.cryptedChar }

        return initial
    }

    /*
        finds matching words in current decrypted solution
        from 8000 most popular words in polish language
     */

    fun findMatchingWords(wordsToSearchIn: List<Word> = mostPopularWords): List<Word>{
        val words = decrypted.split(" ")
        val foundMatchingWords = mutableListOf<Word>()

        words.forEach {word->
            val isFound = mostPopularWords.find { it.word == word }
            if(isFound != null){
                val isAlreadyCounted = foundMatchingWords.find { it.word ==  word}
                if(isAlreadyCounted != null){
                    isAlreadyCounted.value += 1
                }else{
                    foundMatchingWords.add(Word(word, 1))
                }
            }else{
                //println("not found $decryptor.word")
            }
        }

        return foundMatchingWords
    }

    /*
        decrypts cryptogram
     */

    fun decrypt(code: List<LetterTranslation>): String{
        var decrypted = ""
        cryptogram.forEach {cryptedLetter->
            val encyptedLetter = code.find { it.cryptedChar == cryptedLetter }
            decrypted += encyptedLetter?.encryptedChar ?: " "
        }

        this.decrypted = decrypted
        return decrypted
    }

    /*
        return list of letters with its count (how many times it occurred in cryptogram)
        sorted descending, so most popular letter is first
     */

    fun getCryptedLettersCount(): List<Letter>{
        val filteredCryptedLetters = mutableListOf<Letter>()
        val filteredCryptogram = cryptogram.replace(" ", "")
        filteredCryptogram.forEach {w->
            val alreadyFoundLetters = filteredCryptedLetters.find { it.letter == w }
            if(alreadyFoundLetters != null){
                alreadyFoundLetters.value += 1
            }else{
                filteredCryptedLetters.add(Letter(w, 1))
            }
        }
        filteredCryptedLetters.sortByDescending { it.value }
        return filteredCryptedLetters
    }

    /*
        returns all matching crypted words found in cryptogram
     */

    fun getCryptedWordsFromText(): List<Word>{
        val allCryptogramWords = cryptogram.split(" ")
        val filteredCryptedWords = mutableListOf<Word>()
        allCryptogramWords.forEach {w->
            val alreadyFoundWord = filteredCryptedWords.find { it.word == w }
            if(alreadyFoundWord != null){
                alreadyFoundWord.value += 1
            }else{
                filteredCryptedWords.add(Word(w, 1))
            }
        }
        filteredCryptedWords.sortByDescending { it.value }
        return filteredCryptedWords
    }

}