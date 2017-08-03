package com.petarmarijanovic.myshoppinglist

import com.petarmarijanovic.myshoppinglist.extensions.decodeFromFirebaseKey
import com.petarmarijanovic.myshoppinglist.extensions.encodeAsFirebaseKey
import org.junit.Assert.assertEquals
import org.junit.Test

class FirebaseKeyEncodingTest {
  @Test
  fun email() {
    
    val email = "petar.marijanovic@gmail.com"
    val encodedEmail = "petar%2Emarijanovic@gmail%2Ecom"
    
    assertEquals(email.encodeAsFirebaseKey(), encodedEmail)
    assertEquals(encodedEmail.decodeFromFirebaseKey(), email)
  }
  
  @Test
  fun allIllegalCharacters() {
    
    val illegalCharacters = ". # $ [ ]"
    val encodedIllegalCharacters = "%2E %23 %24 %5B %5D"
    
    assertEquals(illegalCharacters.encodeAsFirebaseKey(), encodedIllegalCharacters)
    assertEquals(encodedIllegalCharacters.decodeFromFirebaseKey(), illegalCharacters)
  }
}
