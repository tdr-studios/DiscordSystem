package de.tdrstudios.discordsystem.api.dsl

import de.tdrstudios.discordsystem.api.Discord
import kotlin.reflect.KClass

fun <T : Any> getInstance(clazz: KClass<T>): T {
    return Discord.getInstance(clazz.java)
}

fun scanServices(vararg packageString: String?) {
    Discord.scanCommands(*packageString)
}

fun scanEventHandler(vararg packageString: String?) {
    Discord.scanEventHandler(*packageString)
}

fun scanCommands(vararg packageString: String?) {
    Discord.scanCommands(*packageString)
}

