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