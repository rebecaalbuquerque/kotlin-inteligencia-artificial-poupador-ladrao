package algoritmo

import algoritmo.data.Estado
import algoritmo.data.funcaoSucessor
import algoritmo.extension.toEstado
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

        if (acoes.isEmpty())
            return 0

        return acao.value
    }

}