package algoritmo

import algoritmo.data.Estado
import algoritmo.enums.Acao
import algoritmo.enums.PercepcaoVisao.MOEDA
import algoritmo.extension.*
import java.util.*

class Poupador : ProgramaPoupador() {

    private lateinit var estadoAtual: Estado

    override fun acao(): Int {
        estadoAtual = sensor.toEstado()

        val acoes = estadoAtual.funcaoSucessor()
        val acao = acoes.random()

        println("Visao identificacao    : " + Arrays.toString(sensor.visaoIdentificacao))
        println("Ambiente olfato ladrao : " + Arrays.toString(sensor.ambienteOlfatoLadrao))
        println("Quantidade moedas      : ${sensor.numeroDeMoedas}")
        println("Quantidade moedas banco: ${sensor.numeroDeMoedasBanco}")
        println("Numero jogas imnune    : ${sensor.numeroJogadasImunes}")
        println("Posicao                : ${sensor.posicao}")
        println("Acoes: $acoes")
        println("Acao selecionada: $acao")

        println("------------------------------------\n")

        return acaoBaseadaNoCusto().value
    }

    /**
     * Essa função gera um HashMap<Acao, Int> que consta as possíveis ações que o poupador pode realizar na jogada atual
     * e seus respectivos custos. A ação que será tomada é a ação que possuir o menor custo
     * */
    private fun acaoBaseadaNoCusto(): Acao {
        val map = mutableMapOf<Acao, Int>()

        // Calculando escolha das possíveis ações baseando-se apenas nas moedas (por enquanto)
        estadoAtual.funcaoSucessor().forEach { mAcao ->

            when (mAcao) {
                Acao.FICAR_PARADO -> { }

                Acao.MOVER_CIMA -> {
                    // Custo apenas na busca das moedas
                    map[Acao.MOVER_CIMA] = estadoAtual.visaoPoupadorCima().filter { it == MOEDA.value }.size * MOEDA.custo
                }

                Acao.MOVER_BAIXO -> {
                    map[Acao.MOVER_BAIXO] = estadoAtual.visaoPoupadorBaixo().filter { it == MOEDA.value }.size * MOEDA.custo
                }

                Acao.MOVER_DIREITA -> {
                    map[Acao.MOVER_DIREITA] = estadoAtual.visaoPoupadorDireita().filter { it == MOEDA.value }.size * MOEDA.custo
                }

                Acao.MOVER_ESQUERDA -> {
                    map[Acao.MOVER_ESQUERDA] = estadoAtual.visaoPoupadorEsquerda().filter { it == MOEDA.value }.size * MOEDA.custo
                }
            }

        }

        // Descobrindo a ação que possui o menor custo
        val custoMinimo: Map.Entry<Acao, Int>? = map.minBy { it.value }

        // Descobrindo se tem mais de uma ação com o mesmo custo mínimo
        val custoMinimoRepetido = map.filter { it.value == custoMinimo?.value }

        return custoMinimo?.let {

            // Se existir apenas uma ação com custo mínimo, faz essa ação
            if(custoMinimoRepetido.size == 1) {
                it.key
            } else {
                // Se existir mais de uma ação com o mesmo custo mínimo
                custoMinimoRepetido.keys.random()
            }

        } ?: kotlin.run {
            // Se o map de ações sucessores possíveis estiver vazio, então é porque a única coisa que o poupador pode
            // fazer é ficar parado
            Acao.FICAR_PARADO
        }

    }

}