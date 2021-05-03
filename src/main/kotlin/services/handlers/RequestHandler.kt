package services.handlers

interface RequestHandler<T, R> {

    suspend fun handleRequest(request: T): R

}
