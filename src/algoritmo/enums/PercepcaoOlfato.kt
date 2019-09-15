package algoritmo.enums

enum class PercepcaoOlfato(val value: Int, val custo: Int) {

    SEM_CHEIRO(0, 0),
    CHEIRO_1_UNIDADE_ATRAS(1, 0),
    CHEIRO_2_UNIDADES_ATRAS(2, 1),
    CHEIRO_3_UNIDADES_ATRAS(3, 70000),
    CHEIRO_4_UNIDADES_ATRAS(4, 80000),
    CHEIRO_5_UNIDADES_ATRAS(1, 90000);

    companion object {
        fun getByValue(i: Int): PercepcaoOlfato {
            return values().find { it.value == i } ?: SEM_CHEIRO
        }
    }

}