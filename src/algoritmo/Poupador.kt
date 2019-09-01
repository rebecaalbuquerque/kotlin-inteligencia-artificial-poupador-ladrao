package algoritmo

import algoritmo.extension.toEstado
import java.util.*

class Poupador: ProgramaPoupador() {

    var count = 0

    override fun acao(): Int {
        count++

        if(count == 2) {
            count = 0
        } else {
            println("Visao identificacao    : " + Arrays.toString(sensor.visaoIdentificacao))
            println("A,biente olfato ladrao : " + Arrays.toString(sensor.ambienteOlfatoLadrao))
            println("Quantidade moedas      : ${sensor.numeroDeMoedas}")
            println("Quantidade moedas banco: ${sensor.numeroDeMoedasBanco}")
            println("Numero jogas imnune    : ${sensor.numeroJogadasImunes}")
            println("Posicao                : ${sensor.posicao}")

            println("------------------------------------")

        }

        val estadoAtual = sensor.toEstado()

        return (Math.random() * 5).toInt()
    }

}