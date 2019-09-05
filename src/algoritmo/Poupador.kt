package algoritmo

import algoritmo.data.Estado
import algoritmo.enums.Acao
import algoritmo.enums.PercepcaoVisao
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

        //return (Math.random() * 5).toInt()
        //acaoBaseadoEmCusto()

        if (acoes.isEmpty())
            return 0

        //return acao.value
        return acaoBaseadoEmCusto().value
    }

    private fun acaoBaseadoEmCusto(): Acao {
        val map = mutableMapOf<Acao, Int>()

        // Calculando escolha das ações baseando-se apenas nas moedas (por enquanto)
        estadoAtual.funcaoSucessor().forEach { mAcao ->

            when (mAcao) {
                Acao.FICAR_PARADO -> { }

                Acao.MOVER_CIMA -> {
                    val custoMoedas = estadoAtual.visaoPoupadorCima().filter { it == PercepcaoVisao.MOEDA.value }.size * (-800)
                    map[Acao.MOVER_CIMA] = custoMoedas
                }

                Acao.MOVER_BAIXO -> {
                    val custoMoedas = estadoAtual.visaoPoupadorBaixo().filter { it == PercepcaoVisao.MOEDA.value }.size * (-800)
                    map[Acao.MOVER_BAIXO] = custoMoedas
                }

                Acao.MOVER_DIREITA -> {
                    val custoMoedas = estadoAtual.visaoPoupadorDireita().filter { it == PercepcaoVisao.MOEDA.value }.size * (-800)
                    map[Acao.MOVER_DIREITA] = custoMoedas
                }

                Acao.MOVER_ESQUERDA -> {
                    val custoMoedas = estadoAtual.visaoPoupadorEsquerda().filter { it == PercepcaoVisao.MOEDA.value }.size * (-800)
                    map[Acao.MOVER_ESQUERDA] = custoMoedas
                }
            }

        }

        println()

        return map.maxBy { it.value }?.key ?: Acao.FICAR_PARADO

    }

}