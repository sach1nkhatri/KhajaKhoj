data class Coupon(
    val id: String,
    val code: String,
    val discount: Int,
    val restaurant: String,
    val expirationDate: String,
    var isUsed: Boolean = false
) {
}