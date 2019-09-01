package algoritmo.data

import java.awt.Point

class Estado {

    var visaoIdentificacao: IntArray = intArrayOf()
    var ambienteOlfatoLadrao: IntArray = intArrayOf()
    var numeroMoedas: Int = 0
    var numeroMoedasBanco: Int = 0
    var numeroJogadasImunes: Int = 0
    var posicao: Point = Point(0, 0)

}