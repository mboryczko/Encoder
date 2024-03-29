import decryptor.model.LetterTranslation
import polyalphabeticcypher.PolyalphabeticCipher
import vigenere.VigenereAlgorithm

fun getProperPopulation() = listOf(
            LetterTranslation('a', 'r'),
            LetterTranslation('b', 'i'),
            LetterTranslation('c', 'a'),
            LetterTranslation('d', 'f'),
            LetterTranslation('e', 'h'),
            LetterTranslation('f', 'y'),
            LetterTranslation('g', 'p'),
            LetterTranslation('h', 'c'),
            LetterTranslation('i', 'w'),
            LetterTranslation('j', 'o'),
            LetterTranslation('k', 'n'),
            LetterTranslation('l', 'm'),
            LetterTranslation('m', 'e'),
            LetterTranslation('n', 't'),
            LetterTranslation('o', 'd'),
            LetterTranslation('p', 's'),
            LetterTranslation('q', 'g'),
            LetterTranslation('r', '•'),
            LetterTranslation('s', 'l'),
            LetterTranslation('t', 'k'),
            LetterTranslation('u', 'j'),
            LetterTranslation('v', 'z'),
            LetterTranslation('w', '•'),
            LetterTranslation('x', '•'),
            LetterTranslation('y', 'u'),
            LetterTranslation('z', 'b')
    )


fun main(args : Array<String>) {
    //val geneticAlgorithm = decryptor.GeneticAlgorithm()
    //geneticAlgorithm.run()

    //val vigenereAlgorithm = VigenereAlgorithm()
    //val encrypted = vigenereAlgorithm.encrypt()
    //val decrypted = vigenereAlgorithm.decrypt(encrypted)


    val polyalphabeticCipher = PolyalphabeticCipher()
    val encrypted = polyalphabeticCipher.encrypt()
    polyalphabeticCipher.decrypt(encrypted)
}

