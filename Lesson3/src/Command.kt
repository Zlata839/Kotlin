sealed class Command {
    abstract fun isValid(): Boolean

    data class Help(val message: String) : Command() {
        override fun isValid(): Boolean = true
    }

    data class AddPhoneNumber(val userName: String, val phoneNumber: String) : Command() {
        override fun isValid(): Boolean {
            return (phoneNumber.startsWith('+') && phoneNumber.length > 1)
        }

        fun execute(phoneBook: MutableMap<String, Person>) {
            val person = phoneBook[userName]
            if (person != null) {
                val updatedPhones = person.phones.toMutableList()
                updatedPhones.add(phoneNumber)
                phoneBook[userName] = person.copy(phones = updatedPhones)
                println("$userName добавлен $phoneNumber")
            } else {
                val newPerson = Person(userName, listOf(phoneNumber), emptyList())
                phoneBook.put(userName, newPerson)
                println("Создан $newPerson")
            }
        }
    }

    data class AddEmailAddress(val userName: String, val emailAddress: String) : Command() {
        override fun isValid(): Boolean {
            return emailAddress.contains('@') && emailAddress.contains('.') &&
                    emailAddress.split('@', '.').size == 3

        }

        fun execute(phoneBook: MutableMap<String, Person>) {
            val person = phoneBook[userName]
            if (person != null) {
                val updatedEmails = person.emails.toMutableList()
                updatedEmails.add(emailAddress)
                phoneBook[userName] = person.copy(emails = updatedEmails)
                println("$userName добавлен $emailAddress")
            } else {
                val newPerson = Person(userName, emptyList(), listOf(emailAddress))
                phoneBook.put(userName, newPerson)
                println("Создан $newPerson")
            }
        }
    }

    data object Exit : Command() {
        override fun isValid(): Boolean = true
    }

    data class Show(val userName: String) : Command() {
        override fun isValid(): Boolean = true

        fun execute(phoneBook: MutableMap<String, Person>) {
            val person = phoneBook[userName]
            if (person != null) {
                println("Телефоны $userName: ${person.phones}.")
                println("Электронные почты $userName: ${person.emails}.")
            } else {
                println("$userName не найден.")
            }
        }
    }

    data class Find(val value: String) : Command() {
        override fun isValid(): Boolean {
            return value.isNotEmpty()
        }

        fun execute(phoneBook: MutableMap<String, Person>) {
            val people = phoneBook.filter {
                it.value.phones.contains(value) ||
                        it.value.emails.contains(value)
            }
            if (people.isNotEmpty()) {
                println("$value есть у:")
                people.forEach { println(it.key) }
            } else {
                println("Не удалось найти $value")
            }
        }
    }
}

fun readCommand(userInput: String): Command {
    val parts = userInput.split(" ")
    return when {
        parts[0] == "help" -> Command.Help(
            "Телефон должен начинаться с '+'.\n" +
                    "Email должен быть формата userName@example.ru\n" +
                    "Пример: add Alex phone +71234567890\n" +
                    "add Alex email Alex@example.ru"
        )

        parts[0] == "exit" -> Command.Exit
        parts[0] == "find" -> Command.Find(parts[1])
        parts[0] == "show" -> Command.Show(parts[1])
        parts.size == 4 && (parts[2] == "phone" || parts[2] == "email") -> {
            if (parts[2] == "phone") {
                if (Command.AddPhoneNumber(parts[1], parts[3]).isValid()) {
                    val person = Person(parts[1], listOf(parts[3]), emptyList())
                    phoneBook.put(parts[0], person)
                    Command.AddPhoneNumber(parts[1], parts[3])
                } else {
                    Command.Help(
                        "Неверная команда." +
                                "Телефон должен начинаться с '+'."
                    )
                }
            } else {
                if (Command.AddEmailAddress(parts[1], parts[3]).isValid()) {
                    val person = Person(parts[1], emptyList(), listOf(parts[3]))
                    phoneBook.put(parts[0], person)
                    Command.AddEmailAddress(parts[1], parts[3])
                } else {
                    Command.Help(
                        "Неверная команда." +
                                "Email должен быть формата userName@example.ru, без пробелов и лишних знаков ' @ ' и ' . '"
                    )
                }
            }
        }

        else -> throw IllegalArgumentException("Неверная команда!")
    }
}