package algoritmo.extension

import algoritmo.SensoresPoupador
import algoritmo.data.Estado
import algoritmo.enums.Acao
import algoritmo.enums.PercepcaoVisao

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
fun Estado.visaoPoupadorCima(): IntArray {
    return visaoIdentificacao.dropLast(14).toIntArray()
}

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
fun Estado.visaoPoupadorBaixo(): IntArray {
    return visaoIdentificacao.drop(13).toIntArray()
}

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