package com.petarmarijanovic.myshoppinglist

import com.petarmarijanovic.myshoppinglist.data.decodeFromFirebaseKey
import com.petarmarijanovic.myshoppinglist.data.encodeAsFirebaseKey
import org.junit.Assert.assertEquals
import org.junit.Test

class FirebaseKeyEncodingTest {
  @Test
  fun email() {
    
    val email = "petar.marijanovic@gmail.com"
    val encodedEmail = "petar%2Emarijanovic@gmail%2Ecom"
    
    assertEquals(encodeAsFirebaseKey(email), encodedEmail)
    assertEquals(decodeFromFirebaseKey(encodedEmail), email)
  }
  
  @Test
  fun allIllegalCharacters() {
    
    val illegalCharacters = ". # $ [ ]"
    val encodedIllegalCharacters = "%2E %23 %24 %5B %5D"
    
    assertEquals(encodeAsFirebaseKey(illegalCharacters), encodedIllegalCharacters)
    assertEquals(decodeFromFirebaseKey(encodedIllegalCharacters), illegalCharacters)
  }
}
