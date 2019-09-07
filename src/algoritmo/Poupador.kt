package algoritmo

import algoritmo.data.Estado
import algoritmo.enums.Acao
import algoritmo.enums.PercepcaoVisao.*
import algoritmo.extension.*
import java.awt.Point

class Poupador : ProgramaPoupador() {

    private lateinit var estadoAtual: Estado
    private val localizacaoMoedas: MutableList<Point> = mutableListOf()

    override fun acao(): Int {
        estadoAtual = sensor.toEstado()
        val acao = acaoBaseadaNoCusto()
        atualizarMoedasEncontradas(acao)

        return acao.value
    }

    /**
     * Essa função gera um HashMap<Acao, Int> que consta as possíveis ações que o poupador pode realizar na jogada atual
     * e seus respectivos custos. A ação que será tomada é a ação que possuir o menor custo.
     * */
    private fun acaoBaseadaNoCusto(): Acao {
        val map = mutableMapOf<Acao, Int>()

        // Calculando escolha das possíveis ações baseando-se apenas nas moedas (por enquanto)
        estadoAtual.funcaoSucessor().forEach { mAcao ->

            when (mAcao) {
                Acao.FICAR_PARADO -> { }

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

    /**
     * Calcula o custo da percepção (de cima, de baixo, da esquerda ou da direita) do poupador. A coordenada que tiver
     * menor custo dirá qual será a ação ideal a ser realizada.
     * */
    private fun calcularCusto(percepcao: IntArray): Int {
        // TODO: verificar se é assim mesmo
        val custoMoeda = percepcao.filter { it == MOEDA.value }.size * MOEDA.custo
        val custoBanco = if (estadoAtual.numeroMoedas > 10) percepcao.filter { it == BANCO.value }.size * BANCO.custo else 0
        val custoPastilha = if (estadoAtual.numeroMoedas > 10 && estadoAtual.numeroJogadasImunes < 15)
            percepcao.filter { it == PASTILHA_PODER.value }.size * PASTILHA_PODER.custo
        else
            0

        return custoMoeda + custoBanco + custoPastilha
    }

    /**
     * Atualiza na memória do poupador as moedas que ele vai encontrando e/ou coletando. Se ele encontrar novas moedas,
     * elas são adicionadas na lista "localizacaoMoedas". Se ele coletar as moedas, elas são removidas da lista.
     *
     * @param acao variável que irá ajudar a calcular a próxima posição Point(X, Y) do poupador para poder determinar se
     * existe moeda naquela posição. Se existir, a localização dela será removida da memória do poupador
     * */
    private fun atualizarMoedasEncontradas(acao: Acao) {
        val proximaPosicao = Point(estadoAtual.posicao.x, estadoAtual.posicao.y)

        // Descobre qual vai ser a próxima posição do poupador
        when (acao) {
            Acao.FICAR_PARADO -> {}
            Acao.MOVER_CIMA -> proximaPosicao.y++
            Acao.MOVER_BAIXO -> proximaPosicao.y--
            Acao.MOVER_DIREITA -> proximaPosicao.x++
            Acao.MOVER_ESQUERDA -> proximaPosicao.x--
        }

        // Se na próxima posição do poupador existir uma moeda, ele a pegará, portanto essa moeda deverá ser removida
        // da memória do poupador
        localizacaoMoedas.removeIfExists(proximaPosicao)

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

}