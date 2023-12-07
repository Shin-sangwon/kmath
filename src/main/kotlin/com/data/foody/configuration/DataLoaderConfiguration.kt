package com.data.foody.configuration

import com.data.foody.domain.Recipe
import com.github.doyaaaaaken.kotlincsv.dsl.csvReader
import org.springframework.boot.CommandLineRunner
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Profile
import org.springframework.core.io.ClassPathResource
import org.springframework.data.redis.core.RedisTemplate

@Profile("{local, server}")
@Configuration
class DataLoaderConfiguration {

    @Bean
    fun dataLoader(redisTemplate: RedisTemplate<String, Recipe>): CommandLineRunner {
        return CommandLineRunner {
            val resource = ClassPathResource("data/preprocessing/final_recipe_information_reprocessed.csv")

            csvReader().open(resource.inputStream) {
                readNext() // 헤더 읽기
                var row = readNext()
                while (row != null) {
                    val recipe = Recipe(
                            recipe_id = row[0].toLong(),
                            ingredients_concat = row[1],
                            ingredient_count = row[2].toInt(),
                            energy = row[3].toFloat(),
                            carbohydrates = row[4].toFloat(),
                            protein = row[5].toFloat(),
                            fats = row[6].toFloat(),
                            dietaryFiber = row[7].toFloat(),
                            calcium = row[8].toFloat(),
                            sodium = row[9].toFloat(),
                            iron = row[10].toFloat(),
                            vitaminA = row[11].toFloat(),
                            vitaminC = row[12].toFloat(),
                            difficulty = row[13],
                            servers = row[14].toFloat().toInt(),
                            food_preparation_methods = row[15],
                            food_situations = row[16],
                            food_ingredients = row[17],
                            food_types = row[18]
                    )

                    redisTemplate.opsForValue().set(recipe.recipe_id.toString(), recipe)
                    row = readNext()
                }
            }
        }
    }
}