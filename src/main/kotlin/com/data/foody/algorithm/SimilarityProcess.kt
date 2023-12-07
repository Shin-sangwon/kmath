package com.data.foody.algorithm

import smile.nlp.relevance.TFIDF

class SimilarityProcess(private val allIngredients: List<String>) {

    private val tfidf = TFIDF()

    fun tfidfSimilarityUsingClass(refrigerator: String, ingredients: String): Double {

        // 1. Prepare bags (term frequencies) for refrigerator and ingredients
        val bag1 = prepareBags(refrigerator)
        val bag2 = prepareBags(ingredients)

        // 2. Compute maxtf for each bag
        val maxtf1 = bag1.values.maxOrNull() ?: 0
        val maxtf2 = bag2.values.maxOrNull() ?: 0

        // 3. Compute the number of documents N
        val N = allIngredients.size

        // 4. Compute tf-idf vectors using the provided TFIDF class
        val tfidfVector1 = bag1.map { (term, tf) ->
            tfidf.rank(tf, maxtf1, N.toLong(), computeDocumentFrequency(term).toLong())
        }.toDoubleArray()

        val tfidfVector2 = bag2.map { (term, tf) ->
            tfidf.rank(tf, maxtf2, N.toLong(), computeDocumentFrequency(term).toLong())
        }.toDoubleArray()

        // 5. Compute cosine similarity between the two tf-idf vectors
        val similarity = cosineSimilarity(tfidfVector1, tfidfVector2)

        return similarity
    }

    private fun computeDocumentFrequency(term: String): Int {
        // Compute how many times the term appears in allIngredients
        return allIngredients.count { it.contains(term) }
    }

    private fun prepareBags(text: String): Map<String, Int> {
        // Here, implement the logic to convert the text into a bag of words representation
        // For simplicity, you can split the text by spaces and count the occurrences of each word
        return text.split(" ").groupingBy { it }.eachCount()
    }

    private fun cosineSimilarity(vec1: DoubleArray, vec2: DoubleArray): Double {
        val dotProduct = vec1.zip(vec2).sumByDouble { it.first * it.second }
        val normA = kotlin.math.sqrt(vec1.sumByDouble { it * it })
        val normB = kotlin.math.sqrt(vec2.sumByDouble { it * it })

        return if (normA > 0.0 && normB > 0.0) dotProduct / (normA * normB) else 1.0
    }
}