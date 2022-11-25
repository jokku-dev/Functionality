package com.jokku.funapp.presentation.content

interface Set<T> {
    fun set(arg: T)
}

interface Show<T> {
    fun show(visible: T)
}

interface TextSetter : Set<String>
interface ImageSetter : Set<Int>, Show<Boolean>
interface BarShow : Show<Boolean>
interface BtnEnabler {
    fun enable(enabled: Boolean)
}