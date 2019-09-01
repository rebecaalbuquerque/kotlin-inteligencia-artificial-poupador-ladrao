package algoritmo.extension

import algoritmo.SensoresPoupador
import algoritmo.data.Estado

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