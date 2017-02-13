import io.kotlintest.specs.BehaviorSpec

class MyTests : BehaviorSpec() {
    init {
        given("a broomstick") {
            `when`("I sit on it") {
                then("I should be able to fly") {
                    // test code
                }
            }
            `when`("I throw it away") {
                then("it should come back") {
                    // test code
                }
            }
        }
    }
}

fun main(args: Array<String>) {

}