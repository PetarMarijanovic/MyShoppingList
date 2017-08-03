package com.petarmarijanovic.myshoppinglist.extensions

/** Created by petar on 02/08/2017. */
fun String.encodeAsFirebaseKey() =
    this.replace("%", "%25")
        .replace(".", "%2E")
        .replace("#", "%23")
        .replace("$", "%24")
        .replace("/", "%2F")
        .replace("[", "%5B")
        .replace("]", "%5D")

fun String.decodeFromFirebaseKey() =
    this.replace("%25", "%")
        .replace("%2E", ".")
        .replace("%23", "#")
        .replace("%24", "$")
        .replace("%2F", "/")
        .replace("%5B", "[")
        .replace("%5D", "]")
