package algoritmo.extension

import algoritmo.SensoresPoupador
import algoritmo.data.Estado
import algoritmo.enums.Acao
import algoritmo.enums.PercepcaoVisao
import java.awt.Point

fun SensoresPoupador.toEstado(): Estado {

    return Estado().apply {
        visaoIdentificacao = this@toEstado.visaoIdentificacao
        ambienteOlfatoLadrao = this@toEstado.ambienteOlfatoLadrao
        numeroMoedas = this@toEstado.numeroDeMoedas
        numeroMoedasBanco = this@toEstado.numeroDeMoedasBanco
        numeroJogadasImunes = this@toEstado.numeroJogadasImunes
        posicao = this@toEstado.posicao
    }

}

/**
 * Define, para o estado atual, quais as movimentações válidas.
 *
 * É gerado uma lista com as coordenadas CIMA, DIREIAcaTA, ESQUERDA e BAIXO, em seguida é removida da lista as coordenadas
 * que não podem ser executadas. As coordenadas que não podem ser executadas são aquelas que: são paredes, são fora do
 * ambiente, são pastilha do poder e o poupador não tem moeda suficiente e são banco e o poupado não tem moedas para
 * depositar. Se a lista a ser retornada estiver vazia então é porque o agente terá que ficar parado.
 *
 * @return lista de possíveis ações que o agente pode executar
 * */
fun Estado.funcaoSucessor(): List<Acao> {
    val coordenadas = mutableListOf<Pair<Acao, Int>>(
            Acao.MOVER_CIMA to visaoIdentificacao[7],
            Acao.MOVER_ESQUERDA to visaoIdentificacao[11],
            Acao.MOVER_DIREITA to visaoIdentificacao[12],
            Acao.MOVER_BAIXO to visaoIdentificacao[16]
    )

    coordenadas.removeIf { pair ->
        val coordenada = pair.second

        coordenada == PercepcaoVisao.PARECE.value ||
                coordenada == PercepcaoVisao.FORA_DO_AMBIENTE.value ||
                (coordenada == PercepcaoVisao.PASTILHA_PODER.value && numeroMoedas < 5) ||
                (coordenada == PercepcaoVisao.BANCO.value && numeroMoedas == 0)
    }

    return coordenadas.map { it.first }
}

/**
 * Função que retorna todos os elementos apenas da visão de cima do poupador
 * */
fun Estado.visaoPoupadorCima(): IntArray = visaoIdentificacao.dropLast(14).toIntArray()

/**
 * Função que retorna todos os elementos apenas da visão da direita do poupador
 * */
fun Estado.visaoPoupadorDireita(): IntArray {

    return intArrayOf(
            visaoIdentificacao[3], visaoIdentificacao[4], visaoIdentificacao[8], visaoIdentificacao[9],
            visaoIdentificacao[12], visaoIdentificacao[13], visaoIdentificacao[17], visaoIdentificacao[18],
            visaoIdentificacao[22], visaoIdentificacao[23]
    )

}

/**
 * Função que retorna todos os elementos apenas da visão de baixo do poupador
 * */
fun Estado.visaoPoupadorBaixo(): IntArray = visaoIdentificacao.drop(14).toIntArray()

/**
 * Função que retorna todos os elementos apenas da visão da esquerda do poupador
 * */
fun Estado.visaoPoupadorEsquerda(): IntArray {

    return intArrayOf(
            visaoIdentificacao[0], visaoIdentificacao[1], visaoIdentificacao[5], visaoIdentificacao[6],
            visaoIdentificacao[10], visaoIdentificacao[11], visaoIdentificacao[14], visaoIdentificacao[15],
            visaoIdentificacao[19], visaoIdentificacao[20]
    )

}

/**
 * Função que retorna todos os elementos apenas do olfato de cima do poupador
 * */
fun Estado.olfatoPoupadorCima(): IntArray = ambienteOlfatoLadrao.dropLast(5).toIntArray()

/**
 * Função que retorna todos os elementos apenas do olfato da direita do poupador
 * */
fun Estado.olfatoPoupadorDireita(): IntArray = intArrayOf(ambienteOlfatoLadrao[2], ambienteOlfatoLadrao[4], ambienteOlfatoLadrao[7])

/**
 * Função que retorna todos os elementos apenas do olfato de baixo do poupador
 * */
fun Estado.olfatoPoupadorBaixo(): IntArray = ambienteOlfatoLadrao.drop(5).toIntArray()

/**
 * Função que retorna todos os elementos apenas do olfato da esquerda do poupador
 * */
fun Estado.olfatoPoupadorEsquerda(): IntArray = intArrayOf(ambienteOlfatoLadrao[0], ambienteOlfatoLadrao[3], ambienteOlfatoLadrao[5])

/**
 * Retorna um array que contém os indices de todas as moedas da percepção atual
 * */
fun Estado.getIndicesMoedas(): IntArray {
    val visaoIdentificacaoComPoupador = visaoIdentificacao.copyOf().toMutableList()
            .apply { add(12, Int.MAX_VALUE) }

    val indices = visaoIdentificacaoComPoupador.mapIndexed { i, value ->
        var index: Int = Int.MAX_VALUE

        if (value == PercepcaoVisao.MOEDA.value)
            index = i

        index
    }

    indices.toMutableList().removeIf { it == Int.MAX_VALUE }

    return indices.toIntArray()
}

/**
 * Simula qual vai ser a próxima posição Point(x,y) do agente baseando-se na ação que ele irá tomar
 *
 * @param acao que irá guiar no calculo da próxima posição
 * */
fun Estado.getProximaPosicao(acao: Acao) : Point {
    val proximaPosicao = Point(posicao.x, posicao.y)

    when (acao) {
        Acao.FICAR_PARADO -> {}
        Acao.MOVER_CIMA -> proximaPosicao.y--
        Acao.MOVER_BAIXO -> proximaPosicao.y++
        Acao.MOVER_DIREITA -> proximaPosicao.x++
        Acao.MOVER_ESQUERDA -> proximaPosicao.x--
    }

    return proximaPosicao
}