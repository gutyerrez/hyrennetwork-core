package net.hyren.core.shared.misc.utils

import java.util.*
import java.util.function.Predicate
import java.util.stream.Collectors

/**
 * @author SrGutyerrez
 **/
class RandomList<E> : ArrayList<RandomList<E>.RandomCollectionObject<E>>() {

    private val random = Random()

    fun add(e: E, chance: Double): Boolean {
        return this.add(RandomCollectionObject(e, chance))
    }

    override fun remove(element: RandomCollectionObject<E>): Boolean {
        return if ((this.clone() as RandomList<*>)
                        .stream()
                        .anyMatch { it.t == element }) super.remove(element) else false
    }

    fun getTotalWeight() = this.stream().mapToDouble { it.weight }.sum()

    fun raffle() = this.raffle(this)

    fun raffle(predicate: Predicate<RandomCollectionObject<E>>): E {
        val randomList: RandomList<E> = this.stream()
                .filter(predicate)
                .collect(Collectors.toCollection { RandomList() })

        return this.raffle(randomList)
    }

    private fun raffle(randomList: RandomList<E>): E {
        val navigableMap = Collections.emptyNavigableMap<Double, RandomCollectionObject<E>>()

        randomList.forEach {
            var weight = navigableMap.values
                    .stream()
                    .mapToDouble { randomCollectionObject -> randomCollectionObject.weight }
                    .sum()

            weight += it.weight

            navigableMap[weight] = it
        }

        val totalWeight = randomList.random.nextDouble() * navigableMap.values
                .stream()
                .mapToDouble { it.weight }
                .sum()

        return navigableMap.ceilingEntry(totalWeight).value.t
    }

    inner class RandomCollectionObject<T>(
            val t: T,
            val weight: Double
    ) {

        fun getChance() = this.weight * 100 / getTotalWeight()

    }

}