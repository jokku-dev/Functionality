package com.jokku.jokeapp.domain

import java.io.IOException

class NoConnectionException : IOException()
class ServiceUnavailableException : IOException()
class NoCachedJokesException : IOException()