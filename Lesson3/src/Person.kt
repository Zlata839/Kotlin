data class Person(
    val name: String,
    val phones: List<String>,
    val emails: List<String>
) {
    override fun toString() = "Name = $name, Phone = $phones, Email = $emails"
}

var phoneBook = mutableMapOf<String, Person>()