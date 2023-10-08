package ru.practicum.android.diploma.core.util

/** Передатчик Id вакансии
 * Например, по клику на вакансию в поиске мы вызываем DataTransmitter.postId(vacancy.id) и
 * переходим на другой фрагмент.
 * Там мы либо в самом фрагменте, либо во ViewModel делаем примерно следующее:
 * val id = DataTransmitter.getId()
 * И потом делаем с этим Id что нужно.
 */

object DataTransmitter {

    private var vacancyId: String = ""

    fun postId(id: String) {
        vacancyId = id
    }

    fun getId(): String {
        return vacancyId
    }
}