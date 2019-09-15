package algoritmo

import algoritmo.data.Estado
import algoritmo.enums.Acao
import algoritmo.enums.Acao.*
import algoritmo.enums.PercepcaoVisao.*
import algoritmo.extension.*
import java.awt.Point

class Poupador : ProgramaPoupador() {

    private lateinit var estadoAtual: Estado

    // MEMORIA DO POUPADOR
    // Moedas que o poupador vai encontrando ao longo do jogo
    private val localizacaoMoedas: MutableList<Point> = mutableListOf()
    // Lugares e quantidade de vezes por onde o poupador passou
    private val lugaresVisitados: MutableMap<Point, Int> = mutableMapOf()

    override fun acao(): Int {
        estadoAtual = sensor.toEstado()
        val acao = acaoBaseadaNoCusto()
        atualizarMoedasEncontradas(acao)
        atualizarLugaresVisitados(estadoAtual.posicao)

        return acao.value
    }

    /**
     * Essa função gera um HashMap<Acao, Int> que consta as possíveis ações que o poupador pode realizar na jogada atual
     * e seus respectivos custos. A ação que será tomada é a ação que possuir o menor custo.
     * */
    private fun acaoBaseadaNoCusto(): Acao {
        val map = mutableMapOf<Acao, Int>()

        // Calculando escolha das possíveis ações
        estadoAtual.funcaoSucessor().forEach { mAcao ->

            when (mAcao) {
                FICAR_PARADO -> { }

                MOVER_CIMA -> map[MOVER_CIMA] = calcularCusto(estadoAtual.visaoPoupadorCima(), MOVER_CIMA)

                MOVER_BAIXO -> map[MOVER_BAIXO] = calcularCusto(estadoAtual.visaoPoupadorBaixo(), MOVER_BAIXO)

                MOVER_DIREITA -> map[MOVER_DIREITA] = calcularCusto(estadoAtual.visaoPoupadorDireita(), MOVER_DIREITA)

                MOVER_ESQUERDA -> map[MOVER_ESQUERDA] = calcularCusto(estadoAtual.visaoPoupadorEsquerda(), MOVER_ESQUERDA)
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
            FICAR_PARADO
        }

    }

    /**
     * Calcula o custo de uma ação e uma determinada percepção. O calculo para o custo total leva em conta o custo das
     * percepções visuais, o custo dos lugares visitados e o custo de fugir dos ladrões
     * */
    private fun calcularCusto(percepcao: IntArray, acao: Acao): Int {
        return calcularCustoPercepcao(percepcao) +
                calcularCustoLugaresVisitados(acao) /*+
                calcularCustoMoedasEncontradas(acao)*/
    }

    /**
     * Calcula o custo da percepção (de cima, de baixo, da esquerda ou da direita) do poupador. A coordenada que tiver
     * menor custo dirá qual será a ação ideal a ser realizada.
     * */
    private fun calcularCustoPercepcao(percepcao: IntArray): Int {
        val custoMoeda = percepcao.filter { it == MOEDA.value }.size * MOEDA.custo
        val custoBanco = if (estadoAtual.numeroMoedas > 10) percepcao.filter { it == BANCO.value }.size * BANCO.custo else 0
        val custoPastilha = if (estadoAtual.numeroMoedas > 10 && estadoAtual.numeroJogadasImunes < 15)
            percepcao.filter { it == PASTILHA_PODER.value }.size * PASTILHA_PODER.custo
        else
            0

        return custoMoeda + custoBanco + custoPastilha
    }

    /**
     * Calcula o custo que uma determinada ação irá gerar se for executada verificando no HashMap de lugares visitados a
     * quantidade de vezes que o poupador passou por um determinado lugar
     * */
    private fun calcularCustoLugaresVisitados(acao: Acao): Int {

        return when (acao) {
            FICAR_PARADO -> 0
            MOVER_CIMA -> lugaresVisitados[estadoAtual.getProximaPosicao(MOVER_CIMA)] ?: 1
            MOVER_BAIXO -> lugaresVisitados[estadoAtual.getProximaPosicao(MOVER_BAIXO)] ?: 1
            MOVER_DIREITA -> lugaresVisitados[estadoAtual.getProximaPosicao(MOVER_DIREITA)] ?: 1
            MOVER_ESQUERDA -> lugaresVisitados[estadoAtual.getProximaPosicao(MOVER_ESQUERDA)] ?: 1
        }

    }

    private fun calcularCustoMoedasEncontradas(acao: Acao): Int {

        return when (acao) {
            FICAR_PARADO -> 0
            MOVER_CIMA -> localizacaoMoedas.filter { it.y < estadoAtual.posicao.y }.size
            MOVER_BAIXO -> localizacaoMoedas.filter { it.y > estadoAtual.posicao.y }.size
            MOVER_DIREITA -> localizacaoMoedas.filter { it.x > estadoAtual.posicao.x }.size
            MOVER_ESQUERDA -> localizacaoMoedas.filter { it.x < estadoAtual.posicao.x }.size
        }.times(-1)

    }

    /**
     * Atualiza na memória do poupador as moedas que ele vai encontrando e/ou coletando. Se ele encontrar novas moedas,
     * elas são adicionadas na lista "localizacaoMoedas". Se ele coletar as moedas, elas são removidas da lista.
     *
     * @param acao variável que irá ajudar a calcular a próxima posição Point(X, Y) do poupador para poder determinar se
     * existe moeda naquela posição. Se existir, a localização dela será removida da memória do poupador
     * */
    private fun atualizarMoedasEncontradas(acao: Acao) {
        // Descobre qual vai ser a próxima posição do poupador
        val proximaPosicao = estadoAtual.getProximaPosicao(acao)

        // Adicionando localização das moedas encontradas pelo poupador através das percepções visuais
        estadoAtual.getIndicesMoedas().forEach { indice ->
            when (indice) {
                in 0..4 -> localizacaoMoedas.addIfNonexistent(calcularPosicaoMoeda(indice, 0, 4, -2))

                in 5..9 -> localizacaoMoedas.addIfNonexistent(calcularPosicaoMoeda(indice, 5, 9, -1))

                in 10..14 -> localizacaoMoedas.addIfNonexistent(calcularPosicaoMoeda(indice, 10, 14, 0))

                in 15..19 -> localizacaoMoedas.addIfNonexistent(calcularPosicaoMoeda(indice, 15, 19, 1))

                in 20..24 -> localizacaoMoedas.addIfNonexistent(calcularPosicaoMoeda(indice, 20, 24, 2))
            }
        }

        // Se na próxima posição do poupador existir uma moeda, ele a pegará, portanto essa moeda deverá ser removida
        // da memória do poupador
        localizacaoMoedas.removeIfExists(proximaPosicao)

    }

    /**
     * Função auxiliar que calcula a posição da moeda que o poupador visualizou na sua percepção visual.
     * O count é um valor auxiliar no cálculo da posição X da moeda e poderá ser -2, -1, 1 ou 2. Se for -2 é porque a
     * moeda está a 2 posições a esquerda do poupador, se for -1 está a 1 posição a esquerda do poupador, se for 1 está
     * a 1 posição a direita e se for 2 está a 2 posições a direita do poupador.
     *
     * @param indice é a posição/index em que a moeda está no IntArray da percepção visual do poupador
     * @param from a partir de que indice/index está sendo feito o cálculo atual
     * @param to até que indice/index está sendo feito o cálculo atual
     * @param helper valor auxiliar no cálculo da posição Y da moeda, se for -2 significa que a moedas está 2 posições
     * a cima do poupador, se for -1 a 1 posição acima, se for 0 a moeda está na mesma coluna, se for 1 está 1 posição a
     * baixo e se for 2 a moeda está 2 posições abaixo do poupador.
     *
     * @return Point(x,Y) da moeda localizada no IntArray da percepção visual
     * */
    private fun calcularPosicaoMoeda(indice: Int, from: Int, to: Int, helper: Int): Point {
        var count = -3
        var moedaPosicaoX = 0

        for(i in from..to) {
            count++
            if(indice == i) moedaPosicaoX = estadoAtual.posicao.x + count
        }

        return Point(moedaPosicaoX, estadoAtual.posicao.y + helper)

    }

    /**
     * Função que atualiza o HashMap de lugares visitados do poupador. Se o poupador ja passou por aquele Point(x,y),
     * então incrementa +1. Caso contrário, inicializa o contador de um determinado Point(x,y) com 1
     *
     * @param key lugar que o poupador vai passar
     * */
    private fun atualizarLugaresVisitados(key: Point) {

        if(lugaresVisitados.containsKey(key)) {
            lugaresVisitados[key] = lugaresVisitados.getValue(key).plus(1)
        } else {
            lugaresVisitados[key] = 1
        }

    }

}