fun main() {
    var userInput: String = ""
    println("help - помощь по программе.")
    println("show - показать все телефоны и email введённого userName.")
    println("find - показывает всех людей, у которых записан введённый телефон или email.")
    println("add userName phone numberPhone - добавить номер телефона.")
    println("add userName email emailAddress - добавить адрес электронной почты.")
    println("export userName - экспортирует данные по пользователю в json-файл с его именем.")
    println("exit - выход из программы.")

    while (userInput != "exit") {
        println("Введите команду:")
        print(">>: ")

        userInput = readlnOrNull().toString();

        try {
            when (val command = readCommand(userInput)) {
                is Command.Exit -> println("Выход из программы")
                is Command.Help -> println(command.message)
                is Command.Show -> command.execute(phoneBook)
                is Command.Find -> command.execute(phoneBook)
                is Command.Export -> command.execute(phoneBook)
                is Command.AddPhoneNumber -> command.execute(phoneBook)
                is Command.AddEmailAddress -> command.execute(phoneBook)
            }
        } catch (e: IllegalArgumentException) {
            println(e.message)
        }
        println()
    }
}

