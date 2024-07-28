data class Person(
    private val name: String,
    private val phone: String,
    private val email: String
) {
    override fun toString() = "Name = $name, Phone = $phone, Email = $email"
}

var lastPerson: Person? = null

sealed class Command {
    abstract fun isValid(): Boolean

    data class Help(val message: String) : Command() {
        override fun isValid(): Boolean = true
        override fun toString() = message
    }


    data class AddPhoneNumber(val userName: String, val phoneNumber: String) : Command() {
        override fun isValid(): Boolean {
            return phoneNumber.contains('+') && phoneNumber.length > 1
        }
        override fun toString() = "$userName, $phoneNumber"
    }

    data class AddEmailAddress(val userName: String, val emailAddress: String) : Command() {
        override fun isValid(): Boolean {
            return emailAddress.contains('@') && emailAddress.contains('.') &&
                    emailAddress.split('@', '.').size == 3
        }
        override fun toString() = "$userName, $emailAddress"
    }

    data object Exit : Command(){
        override fun isValid(): Boolean = true
    }

    data object Show : Command() {
        override fun isValid(): Boolean = true
    }
}

fun readCommand(userInput: String): Command {
    val parts = userInput.split(" ")
    return when {
        userInput == "help" -> Command.Help(
            "Телефон должен начинаться с '+'.\n" +
                    "Email должен быть формата userName@example.ru\n" +
                    "Пример: add Alex phone +71234567890\n" +
                    "add Alex email Alex@example.ru"
        )
        userInput == "exit" -> Command.Exit
        userInput == "show" -> Command.Show
        parts.size == 4 && (parts[2] == "phone" || parts[2] == "email") -> {
            if (parts[2] == "phone") {
                if (parts[1].isNotBlank() && Command.AddPhoneNumber(parts[1], parts[3]).isValid()) {
                    lastPerson = Person(parts[1], parts[3], "")
                    Command.AddPhoneNumber(parts[1], parts[3])
                } else {
                    Command.Help("Неверная команда. Телефон должен начинаться с '+'.")
                }
            } else {
                if (parts[1].isNotBlank() && Command.AddEmailAddress(parts[1], parts[3]).isValid()) {
                    lastPerson = Person(parts[1], "", parts[3])
                    Command.AddEmailAddress(parts[1], parts[3])
                } else {
                    Command.Help("Неверная команда. Email должен быть формата userName@example.ru")
                }
            }
        }
        else -> throw IllegalArgumentException("Неверная команда!")
    }
}

fun main() {
    var userInput: String = ""

    while (userInput != "exit") {
        println("Введите команду:")
        println("help - помощь по программе.")
        println("show - показывает последнюю исполненную команду add.")
        println("add userName phone numberPhone - добавить номер телефона.")
        println("add userName email emailAddress - добавить адрес электронной почты.")
        println("exit - выход из программы.")
        print(">>: ")
        userInput = readlnOrNull().toString();

        try {
            when (val command = readCommand(userInput)) {
                is Command.Show -> {
                    if (lastPerson != null) {
                        println("Последняя команда: $lastPerson")
                    } else {
                        println("Не инициализировано")
                    }
                }
                else -> println("$command")
            }
        } catch (e: IllegalArgumentException) {
            println(e.message)
        }
        println()
    }
}

