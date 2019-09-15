package algoritmo.enums

enum class PercepcaoVisao(val value: Int, val custo: Int) {

    // TODO: colocar valor correto para o custo

    SEM_VISAO(-2, -1),
    FORA_DO_AMBIENTE(-1, -1),
    CELULA_VAZIA(0, -1),
    PARECE(1, -1),
    BANCO(3, -1000),
    MOEDA(4, -800),
    PASTILHA_PODER(5, -900),
    POUPADOR(100, -1), // Colocar custo correto
    LADRAO(200, -1) // Colocar custo correto

}