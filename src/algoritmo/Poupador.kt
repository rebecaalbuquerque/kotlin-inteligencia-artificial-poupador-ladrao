package algoritmo

import algoritmo.data.Estado
import algoritmo.enums.Acao
import algoritmo.enums.PercepcaoVisao.*
import algoritmo.extension.*

class Poupador : ProgramaPoupador() {

    private lateinit var estadoAtual: Estado

    override fun acao(): Int {
        estadoAtual = sensor.toEstado()
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
                Acao.FICAR_PARADO -> {
                }

                Acao.MOVER_CIMA -> map[Acao.MOVER_CIMA] = calcularCusto(estadoAtual.visaoPoupadorCima())

                Acao.MOVER_BAIXO -> map[Acao.MOVER_BAIXO] = calcularCusto(estadoAtual.visaoPoupadorBaixo())

                Acao.MOVER_DIREITA -> map[Acao.MOVER_DIREITA] = calcularCusto(estadoAtual.visaoPoupadorDireita())

                Acao.MOVER_ESQUERDA -> map[Acao.MOVER_ESQUERDA] = calcularCusto(estadoAtual.visaoPoupadorEsquerda())
            }

        }

        // Descobrindo a ação que possui o menor custo
        val custoMinimo: Map.Entry<Acao, Int>? = map.minBy { it.value }

        // Descobrindo se tem mais de uma ação com o mesmo custo mínimo
        val custoMinimoRepetido = map.filter { it.value == custoMinimo?.value }

        return custoMinimo?.let {

            // Se existir apenas uma ação com custo mínimo, faz essa ação
            if (custoMinimoRepetido.size == 1) {
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

    private fun calcularCusto(percepcao: IntArray): Int {
        val custoMoeda = percepcao.filter { it == MOEDA.value }.size * MOEDA.custo
        val custoBanco = if (estadoAtual.numeroMoedas > 10) percepcao.filter { it == BANCO.value }.size * BANCO.custo else 0
        val custoPastilha = if (estadoAtual.numeroMoedas > 10 && estadoAtual.numeroJogadasImunes < 15)
            percepcao.filter { it == PASTILHA_PODER.value }.size * PASTILHA_PODER.custo
        else
            0

        return custoMoeda + custoBanco + custoPastilha
    }

}