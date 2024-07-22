
fun main() {
    var userInput: String = ""

    while (userInput != "exit") {
        println("Введите команду:")
        println("help - помощь по программе.")
        println("add userName phone numberPhone - добавить номер телефона.")
        println("add userName email emailAddress - добавить адрес электронной почты.")
        println("exit - выход из программы.")
        print(">>: ")

        userInput = readLine().toString()
        when (userInput) {
            "help" -> println(
                "Телефон должен начинаться с phone ' + '.\n" +
                        "Email должен быть формата userName@example.ru, без пробелов и лишних знаков ' @ ' и ' . '\n" +
                        "Пример: add Alex phone +71234567890\n" +
                        "add Alex email Alex@example.ru"
            )
            "exit" -> println("Работа завершена");
            else -> {
                val parts = userInput.split(" ")
                if (parts.size != 4) {
                    println("Неверная команда!")
                    continue
                }
                when (parts[2]) {
                    "phone" -> {
                        if (parts[3].contains('+') && parts[3].toCharArray().isNotEmpty())
                            println("К пользователю ${parts[1]} добавлен телефон ${parts[3]}")
                        else println("Ошибка в номере телефона.")
                    }
                    "email" -> {
                        if (parts[3].contains('@') && parts[3].contains('.')) {
                            if (parts[3].split('@', '.').size == 3)
                                println("К пользователю ${parts[1]} добавлен email ${parts[3]}")
                            else
                                println("Ошибка в электронной почте.")
                        }
                    }
                }
            }
        }
        println()
    }
}
