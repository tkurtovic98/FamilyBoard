package com.hr.kurtovic.tomislav.familyboard.util

/**
 * It's a one item box. If it has something you can take it.
 * After you take something it's empty again until someone puts something into (through constructor or through put).
 * If someone puts something it will replace current item there.
 */
class Box<T>(var content: T? = null) {

    fun take(f: (T) -> Unit) {
        content?.apply(f)
        content = null
    }

    fun put(content: T) {
        this.content = content
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Box<*>

        if (content != other.content) return false

        return true
    }

    override fun hashCode(): Int {
        return content?.hashCode() ?: 0
    }
}
