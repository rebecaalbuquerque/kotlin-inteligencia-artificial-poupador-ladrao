# Trabalho Ladrão x Poupador
A proposta desta atividade computacional é a de avaliar a habilidade dos alunos da disciplina de Inteligência Artificial da UNIFOR com respeito à 
especificação e implementação de programas de agentes inteligentes (racionais). No caso, os agentes a serem implementados devem ser agentes de software 
simples representando personagens em um jogo de computador denominado poupador. Para tanto, será disponibilizado aos alunos um ambiente de simulação 
completo na forma de um framework computacional em Java 1.5, em que a maior parte do jogo (i.e., parte gráfica do ambiente físico, sensores e atuadores dos 
agentes, etc.) já estará implementada. Caberá aos grupos de alunos somente projetar e implementar a parte comportamental do agente (poupador ou ladrão), 
ou seja, implementar o que seria a ação do agente, de acordo com um dos tipos básicos: **baseado em objetivos**, **baseado em utilidade**, ou **com aprendizagem**.

## Informações importantes
* O ambiente físico (Fig. 1) consistirá de um labirinto, composto por uma série de obstáculos (paredes), por moedas espalhadas aleatoriamente, pastilhas de 
poder, agencias bancarias, um agente poupador e um agente ladrão. O labirinto deve ser visto como uma matriz de células de tamanho idêntico. A configuração 
física do labirinto será diferente no dia da avaliação

<p align="center">
  <br/>
  <img src="https://user-images.githubusercontent.com/41158713/64479352-48e36700-d18c-11e9-9323-9b0ff9d05b36.png">
  <br/>
  <b>Figura 1 - Exemplo de configuração do ambiente do jogo</b>
  <br>
</p>

* Dois tipos de agentes habitam o labirinto: “**agente-poupador**” e “**agente-ladrão**”. Deverão existir mais de um agente-poupador e mais de um agente ladrão por 
partida. Alguns grupos de alunos ficarão responsáveis pela implementação do agente-poupador, enquanto outros grupos ficarão responsáveis pela 
implementação do agente-ladrão. O corpo (arquitetura) de ambos os agentes ocupa somente uma única célula do ambiente
  
* O **agente-poupador** _tem como objetivo guardar a maior quantidade de moedas possíveis_, para isso ele terá a opção de guardá-las no banco. Evitando assim 
que o ladrão as roube. O agente-poupador sabe onde fica o banco ``Constantes.posicaoBanco``. Para isso ele terá um tempo predefinido que pode ser visto no 
canto superior à esquerda do ambiente. A cada movimento seu no ambiente, o tempo é reduzido em uma unidade. No ambiente existem algumas pastilhas 
“pastilhas do poder” que imunizam o poupador não deixando o **agente-ladrão** lhe roubar, porém essas pastilhas têm um custo. Para consegui-las o 
agente-poupador deve dar em troca 5 moedas. Ficando imune por 15 jogadas

* O **agente-ladrão** também tem como objetivo encontrar moedas, porém ele só as consegue roubando do **agente-poupador**

* Ambos os tipos de agentes dispõem de sensores de natureza visual, ou seja, **suas percepções atuais** (repassadas pelo ambiente à sua arquitetura) 
guardam informações visuais da parte do mundo à sua volta, dentro de um raio especificado, o qual, por sua vez, varia de um tipo de agente para outro

* A Fig. 2 representa a visão atual dos agentes (visão 5 X 5). Deve-se notar que o agente (poupador ou ladrão) está representado pela célula de cor branca 
nessas figuras. Já as células em preto e em cinza representam as outras células que o agente pode perceber; os quadros em cinza representam as posições 
para onde o agente pode se locomover. Além das percepções visuais, o agente-poupador recebe também dos seus sensores informações a respeito 
    - (i) da quantidade de moedas em mãos e quantidade já depositadas; 
    - (ii) da coordenada (x,y) da posição atual que ocupa na matriz do labirinto; 
    - (iii) número de jogadas imunes. 
    - (iv) além das percepções visuais, ele recebe também do ambiente informação a respeito de possíveis marcas olfativas (feromônio) deixadas 
    recentemente pelo agente-poupador e pelo ladrão no seu raio de visualização olfativa atual (ver Fig. 3).

<p align="center">
  <br/>
  <img height="267" width="384" src="https://user-images.githubusercontent.com/41158713/64479554-328ada80-d18f-11e9-81b2-8d8462857397.png">
  <br/>
  <b>Figura 2 - Representação da visão do agente-poupador – ver possíveis valores na Tab. 1</b>
  <br>
</p>

<p align="center">
  <br/>
  <img height="241" width="312" src="https://user-images.githubusercontent.com/41158713/64479555-34549e00-d18f-11e9-8a53-5caa1e532ea2.png">
  <br/>
  <b>Figura 3 - Representação do olfato dos agentes – ver possíveis valores na Tab. 2</b>
  <br>
</p>

| Código   | Significado   |
| :------: |:-------------:|
| -2       | Sem visão para o local |
| -1       | Fora do ambiente (mundo exterior) |
|  0       | Célula vazia (sem nenhum agente) |
|  1       | Parede |
|  3       | Banco |
|  4       | Moeda |
|  5       | Pastinha do Poder |
|  100     | Poupador |
|  200     | Ladrão |

<b>Tabela 1 - Código das possíveis percepções visuais aparecendo nas células das Figs. 2</b>
<br/>
<br/>

| Código   | Significado   |
| :------: |:-------------:|
|  0       | Sem nenhuma marca  |
|  1       | Com marca de cheiro gerada a **_uma unidade_** de tempo atrás |
|  2       | Com marca de cheiro gerada a **_duas unidades_** de tempo passadas |
|  3       | Com marca de cheiro gerada a **_três unidades_** de tempo passadas |
|  4       | Com marca de cheiro gerada a **_quatro unidades_** de tempo passadas |
|  5       | Com marca de cheiro gerada a **_cinco unidades_** de tempo passadas |

<b>Tabela 2 - Código das possíveis percepções olfativas aparecendo nas células da Fig. 3</b>
<br/>
<br/>

* Com relação aos atuadores, ambos os tipos de agentes dispõem de membros (pernas) para se locomoverem no ambiente. A cada unidade de tempo, eles só 
podem se mover para uma célula adjacente ou subjacente, ou decidirem ficar parados na célula atual. Ambos os agentes não podem atravessar ou sobrepor às 
paredes; também não podem ver por trás das paredes. Caso o agente decida se mover para uma célula inválida (p. ex., parede), o ambiente o manterá na célula 
atual. À medida que os agentes se movimentam pelo ambiente, eles deixam marcas, as quais permanecem ativas por apenas cinco unidades de tempo de simulação, após 
o que elas evaporam. Deve-se ressaltar que essas marcas são deixadas sempre, ou seja, já fazem parte dos atuadores de cada agente.

* O jogo chega a seu final, quando terminar o tempo. O vencedor será o agente que conseguir uma maior quantidade de moedas.

* Os grupos responsáveis pelo projeto do agente-poupador deverão implementar seu código via classe em Java que herde da classe abstrata ProgramaPoupador. 
Já os que projetarem o agente-ladrão deverão fazer o mesmo só que estendendo da classe abstrata ProgramaLadrao. Todos os métodos necessários para realizar 
as percepções (visuais ou não) e atuações (movimentação) dos agentes já estarão disponíveis no framework computacional, tais como:
    - Para o poupador
        - ``sensor.getVisaoIdentificacao()``
        - ``sensor.getAmbienteOlfatoLadrao()``
        - ``sensor.getAmbienteOlfatoPoupador()``
        - ``sensor.getNumeroDeMoedas()``
        - ``sensor.getNumeroDeMoedasBanco()``
        - ``sensor.getNumeroJogadasImunes()``
        - ``sensor.getPosicao()``
        
    - Para o ladrão
        - ``sensor.getVisaoIdentificacao()``
        - ``sensor.getAmbienteOlfatoLadrao()``
        - ``sensor.getAmbienteOlfatoPoupador()``
        - ``sensor.getNumeroDeMoedas()``
        - ``sensor.getPosicao()``
        
    - Para o ambos
        - 0: ``Ficar Parado()`` – o agente fica parado e não perde energia
        - 1: ``MoverCima()`` – move o agente uma posição acima da atual
        - 2: ``MoverBaixo()`` – move o agente uma posição abaixo da atual
        - 3: ``MoverDireita()`` – move o agente uma posição a direita da atual
        - 4: ``MoverEsquerda()`` – move o agente uma posição a esquerda da atual
