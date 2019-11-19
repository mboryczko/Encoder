package decryptor

import extensions.deepCopy
import getProperPopulation
import decryptor.model.LetterTranslation
import decryptor.word.FilePrinter

class GeneticAlgorithm{
    val wordAnalyzer = WordAnalyzer()
    var population = wordAnalyzer.getInitialPopulation()
    var currentFit = 0
    var iterations = 0
    var iterationsStuck = 0

    fun run(){
        val timeStart = System.currentTimeMillis()
        evaluateNextPopulation(population)
        while (currentFit < 100){
            iterations++
            iterationsStuck++
            val nextPopulation = crossover()
            if(nextPopulation != null){
                evaluateNextPopulation(nextPopulation)
                if(iterations % 10 == 0)
                    println("iterations: $iterations fit: ${currentFit}")
            }
        }

        //FilePrinter.writeWordsToFile("words/$currentFit.csv", words)
        FilePrinter.writeToFile("encrypted/$currentFit.csv", wordAnalyzer.decrypt(population))
        FilePrinter.writeLettersWithTranslationToFile("results/$currentFit.csv", population, getProperPopulation())
        println("evaluation time: ${(System.currentTimeMillis() - timeStart) / 1000L} [s]")
    }

    private fun evaluateNextPopulation(nextPopulations: List<LetterTranslation>){
        val decrypted = wordAnalyzer.decrypt(nextPopulations)
        val words = wordAnalyzer.findMatchingWords()

        if(words.size > currentFit){
            println("found better solution ${words.size} >= $currentFit")
            iterationsStuck = 0
            currentFit = words.size
            population = nextPopulations
        }
    }

    private fun crossover(): List<LetterTranslation>?{
        val populationCopy = deepCopy(population.toTypedArray())
        if(populationCopy != null){
            return wordAnalyzer.swapOrderOfTwoLetterTranslations(populationCopy.toList(), iterationsStuck)
        }

        return null
    }

}