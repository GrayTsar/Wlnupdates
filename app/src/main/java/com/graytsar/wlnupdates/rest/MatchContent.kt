package com.graytsar.wlnupdates.rest

// WARNING
// This is not a REST class. ResultSearch has ambiguous type, that is why I made a wrapper class
class MatchContent(
    var sid:Int,
    var percent: Double,
    var name: String
)