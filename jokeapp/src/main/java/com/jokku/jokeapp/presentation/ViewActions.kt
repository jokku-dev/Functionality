package com.jokku.jokeapp.presentation

interface Set<T> {
    fun set(arg: T)
}
interface Show<T> {
    fun show(visible: T)
}
interface SetText : Set<String>
interface SetImage : Set<Int>, Show<Boolean>
interface ShowBar : Show<Boolean>
interface EnableBtn {
    fun enable(enabled: Boolean)
}