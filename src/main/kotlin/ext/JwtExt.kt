package ext

import io.jsonwebtoken.JwtBuilder
import io.jsonwebtoken.SignatureAlgorithm

// JWT headers have common fields like "alg" and "typ", but they are not standardized
// We define a standard here
private const val HEADER_ALG = "alg"
private const val HEADER_TYP = "typ"

fun JwtBuilder.setAlgorithm(alg: String): JwtBuilder {
    return setHeaderParam(HEADER_ALG, alg)
}

fun JwtBuilder.setType(typ : String = "JWT"): JwtBuilder {
    return setHeaderParam(HEADER_TYP, typ)
}

