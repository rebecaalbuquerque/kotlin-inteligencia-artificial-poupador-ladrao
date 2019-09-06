package algoritmo.enums

enum class PercepcaoVisao(val value: Int, val custo: Int) {

    // TODO: colocar valor correto para o custo

    SEM_VISAO(-2, -1), // Colocar custo correto
    FORA_DO_AMBIENTE(-1, -1), // Colocar custo correto
    CELULA_VAZIA(0, -1), // Colocar custo correto
    PARECE(1, -1), // Colocar custo correto
    BANCO(3, -1000), // Colocar custo correto
    MOEDA(4, -800),
    PASTILHA_PODER(5, -900), // Colocar custo correto
    POUPADOR(100, -1), // Colocar custo correto
    LADRAO(200, -1) // Colocar custo correto

}