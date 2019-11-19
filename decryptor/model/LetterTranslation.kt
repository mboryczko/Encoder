package decryptor.model

import java.io.Serializable

data class LetterTranslation(
        val cryptedChar: Char,
        var encryptedChar: Char? = null
): Serializable