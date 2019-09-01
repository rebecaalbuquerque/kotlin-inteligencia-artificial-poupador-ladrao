package algoritmo.enums

enum class Acao(val value: Int) {

    FICAR_PARADO(0),
    MOVER_CIMA(1),
    MOVER_BAIXO(2),
    MOVER_DIREITA(3),
    MOVER_ESQUERDA(4);

    companion object {
        fun getByValue(i: Int): Acao {
            return values().find { it.value == i } ?: FICAR_PARADO
        }
    }

}