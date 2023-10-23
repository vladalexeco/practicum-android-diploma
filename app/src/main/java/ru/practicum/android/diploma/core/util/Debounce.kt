package ru.practicum.android.diploma.core.util

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

/**
 * Общая функция дебаунса для всего приложения.
 * Функция принимает несколько параметров:
 * - delayMillis — задержка в миллисекундах;
 * - coroutineScope — область видимости сопрограммы, в которой будут выполняться задачи (может быть
 * ViewModel, Fragemnt, Activity);
 * - useLastParam — флаг, указывающий, должна ли функция использовать последний переданный параметр;
 * - action — функция, вызов которой мы хотим "задебаунсить".
 * Она возвращает другую функцию, которая принимает параметр param типа T и выполняет следующие действия:
 * Если useLastParam равен true, то текущая задача (если она существует) отменяется. Это значит, что
 * если функция вызывается с новым параметром до того, как истечёт задержка, то предыдущий вызов
 * отменяется и начинается новая задержка.
 * Если текущая задача уже завершена или useLastParam равен true, то запускается новая задача.
 * Эта новая задача сначала ждёт указанное количество миллисекунд, а затем вызывает функцию action
 * с переданным параметром.
 *
 * Пример использования для поиска:
 * Во вьюмодели устанавливаем переменную:
 * private val trackSearchDebounce = debounce<String>(2000L,
 *         viewModelScope,
 *         true) {
 *         searchRequest(it) - это функция, которая вызывается отложенно. В данном случае - поиск.
 *     }
 *
 * Затем создаем функцию, которая этот дебаунс использует:
 * fun searchDebounce(changedText: String) {
 *      trackSearchDebounce(changedText)
 * }
 */

fun <T> debounce(delayMillis: Long,
                 coroutineScope: CoroutineScope,
                 useLastParam: Boolean,
                 action: (T) -> Unit): (T) -> Unit {
    var debounceJob: Job? = null
    return { param: T ->
        if (useLastParam) {
            debounceJob?.cancel()
        }
        if (debounceJob?.isCompleted != false || useLastParam) {
            debounceJob = coroutineScope.launch {
                delay(delayMillis)
                action(param)
            }
        }
    }
}