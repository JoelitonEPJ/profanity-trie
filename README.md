# Estratégias de Filtragem de palavras ofensivas

Implementação, documentação e comparação da Trie e outros métodos para a filtragem de palavras ofensivas.

## Sumário

- [Estrutura de diretórios](#estrutura-de-diretórios)

- [Introdução](#introdução)

- [Objetivo](#objetivo)

- [Estruturas e Algoritmos](#estruturas-e-algoritmos)

- [Como rodar o experimento?](#como-rodar-o-experimento)

- [Metodologia](#metodologia)

- [Experimentos](#experimentos)

- [Conclusão](#conclusão)

- [Ameaças à validade](#ameaças-à-validade)

- [Trabalhos Futuros](#trabalhos-futuros)

- [Contribuintes](#contribuintes)

---

### Versionamento de código

O versionamento do projeto foi feito utilizando [Git](https://git-scm.com/) (por meio da plataforma [Github](https://docs.github.com/pt)), permitindo o controle das alterações ao longo do projeto. Foram criadas branches para organizar melhor o fluxo de desenvolvimento e isolar as alterações. Além disso, como regra, permitimos apenas que a branch `main` fosse alterada por meio de pull requests, que deveriam obrigatoriamente ser revisadas por um membro da equipe, garantindo a integração organizada das modificações ao repositório principal.

Dessa forma, foi possível garantir que todos os integrantes do projeto estivessem cientes das alterações que foram introduzidas. Foi criada também uma restrição que pull requests seriam apenas aceitas com a opção de `Squash and Merge`, que possibilita um histórico mais limpo de commits, de forma que seja mais fácil notas a contribuição para o repositório.

### Comparação entre os métodos de filtragem

A comparação entre os métodos de filtragem foi feita utilizando a biblioteca [JMH](https://openjdk.org/projects/code-tools/jmh/) (Java Microbenchmark Harness), uma biblioteca poderosa oficial da [OpenJDK](https://openjdk.org/), utilizada para criar, executar e analisar microbenchmarks em linguagens que utilizam a máquina virtual de java (JVM). Para esse projeto, utilizamos ela para comparar diferentes métricas das estruturas/algoritmos implementados na linguagem Java.

### Geração de inputs

A geração das cargas de dados foi feita através da linguagem de programação [Python](https://www.python.org/about/), escolhida por sua simplicidade, legibilidade e alto nível de abstração. Foram desenvolvidos scripts específicos para a criação de cenários distintos de testes (especificado mais à diante) e para formatação dos dados de entrada, permitindo a simulação de entradas com diferentes comportamentos.

### Geração de gráficos

A geração dos gráficos foi realizada a partir dos dados experimentais armazenados em arquivos `.csv`, os quais continham os resultados obtidos durante a execução dos diferentes testes. Para a visualização e análise desses dados, utilizou-se a biblioteca [Matplotlib](https://matplotlib.org/) da linguagem Python, que permitiu a construção de gráficos que representassem o comportamento e o desempenho dos métodos avaliados.

## Estrutura de Diretórios

O código do projeto foi organizado da seguinte maneira:

```
/
├───scripts
│    └───python
│       ├────generate
│       └────plot
├───src
│   └───main
│       └───java
│           ├───aho_corasick
│           ├───baseline
│           ├───hash_table
│           ├───regex
│           ├───trie
│           └───util
└───run-benchmark.sh
```

O diretório `scripts` contém scripts escritos na linguagem Python utilizados para gerar entradas (contidos no subdiretório `generate`) e plotar os gráficos relevantes aos experimentos (reúnidos no subdiretório `plot`)

Já o diretório `src` contém a implementação em Java dos métodos de filtragem escolhidos, nomeadamente: **Aho-Corasick**, **HashTable**, **Regex**, **Trie**, além de conter o baseline, a implementação "ingênua", que serve como ponto de referência base para os outros métodos.

O arquivo `run-benchmark.sh` foi utilizado para rodar o projeto de maneira simples. Ele é responsável por criar os diretórios necessários e fazer o pré-processamento necessário para rodar as comparações.

## Introdução

### Problemática

A existência de um filtro de palavras ofensivas é muito importante para plataformas digitais que almejam ser seguras. Filtros desse tipo são utilizados nos mais diversos ambientes digitais como redes sociais, fóruns, jogos online e plataformas de comunicação. É o uso dessa ferramenta que torna possível a prevenção de discursos de ódio, moderação e cumprimento de políticas, e proteção de usuários vulneráveis como crianças e adolescentes, por exemplo.

Porém, mesmo com a grande relevância dessa ferramenta, ainda não existe um meio definitivo para fazer essa filtragem, já que existem múltiplas estratégias e múltiplas maneiras de aplicar cada uma delas. Essas estratégias podem ser divididas em dois tipos diferentes, citados abaixo:

- **Uso de blacklists**: estratégias desse tipo envolvem a criação de listas de palavras banidas. Geralmente, são usadas em conjunto com algoritmos/estruturas especializados na busca e recuperação rápida de informação em strings, como **Regex**, **Aho-Corasick** e **Trie**, ou estruturas de busca rápida de propósito geral, como **HashTable**;

- **Análise Contextual**: métodos relacionados a essa estratégia estão geralmente relacionados à algoritmos de PLN (Processamento de Linguagem Natural) ou de classificação, como o [Classificador de Naive Bayes](https://en.wikipedia.org/wiki/Naive_Bayes_classifier), que conseguem detectar padrões nas frases e classificá-las de acordo.

---

### Escopo

A filtragem de palavras ofensivas é um tema bem complexo com muitos assuntos que podem se ramificar em diversos projetos diferentes. Por isso, nós tivemos o cuidado de definir bem o escopo desse projeto de antemão, de forma que fosse possível discutir e analisar de maneira completa o tema proposto por uma das vias.

Dessa forma, tendo essa problemática em mente e considerando que a estrutura base de análise desse projeto inicialmente escolhido era a **Trie**, decidimos focar nos métodos relacionados ao uso de blacklists para realizar essa filtragem, logo, nosso projeto discutirá os seguintes métodos: **Aho-Corasick**, **HashTable**, **Trie** e **Regex**.

Para realizar a análise desses métodos, nós definimos duas restrições para os nossos testes:

- **Palavras diferentes estarão separadas**: Para evitar problemas como o de [Scunthorpe](https://en.wikipedia.org/wiki/Scunthorpe_problem), que dificultariam a realização de análises estatísticas, nós evitamos trabalhar com a detecção de palavras ofensivas que estejam coladas à outras. Porém, iremos fazer testes para comparar se algum método, da maneira que foi implementado, sofreria desse problema;

- **A String recebida não pode ser processada**: Para comparar puramente a capacidade das diferentes estruturas/algoritmos, optamos por adicionar a restrição que não podemos fazer um pré-processamento na palavra/frase que estará sendo analisada, ou seja, não é permitido normalizar a palavra (substituir caracteres unicode por seus equivalentes ascii) ou mudar sua capitalização.

## Objetivo

Nesse contexto, o propósito do seguinte projeto é analisar e comparar diferentes implementações para filtragem de palavras ofensivas que se baseiam no uso de blacklists para categorizar palavras como `bad` ou `good` words. O estudo tem como objetivo analisar qual abordagem se mostra mais eficaz para identificar palavras ofensivas no meio de frases geradas pseudo-aleatoriamente livres de contexto e gírias, de maneira que seja possível analisar friamente os dados do experimento.

O tema discutido vem se tornando cada vez mais relevante nos dias atuais, visto que diversos estados e até mesmo países iniciaram a instauração de leis relacionadas à obrigatoriedade da verificação de idade na internet, a exemplo da [Califórnia](https://leginfo.legislature.ca.gov/faces/billTextClient.xhtml?bill_id=202520260AB1043), [Brasil](https://g1.globo.com/tecnologia/noticia/2025/12/11/eca-digital-protecao-criancas-adolescentes-internet.ghtml) e [Inglaterra](https://en.wikipedia.org/wiki/Online_age_verification_in_the_United_Kingdom), com a justificativa de proteger crianças e adolescentes nos ambientes digitais, proibindo que conteúdos considerados inadequados sejam exibidos para eles, algo que está muito ligado à moderação de conteúdo através da filtragem de palavras ofensivas.

## Estruturas e Algoritmos

### Aho-Corasick

Em tarefas de detecção de padrões em texto, o Aho–Corasick algorithm permite a busca simultânea de múltiplos padrões por meio da construção de um autômato baseado em uma trie com links de falha, tornando o processo eficiente mesmo para grandes conjuntos de palavras. Essa característica é especialmente útil no nosso contexto de detecção de bad words, pois foi possível adaptar o método para reconhecer variações de escrita, incluindo formas de L33T, sem a necessidade de guardar na memória todas as variações possíveis de palavras mascaradas, diferente da Baseline e HashTable. Isso foi possível pois utilizamos um mapa de interpretações. 

#### Funcionamento do Algoritmo

A implementação se estrutura em três fases principais:

1. **Contrução da Trie**: Os padrões fornecidos são organizados em uma árvore de prefixos, onde cada nó representa um caractere e o caminho da raiz até uma folha formam uma palavra completa.

2. **Construção de Links de sufixo e saída**: O coração do algoritmo. Através de uma busca em largura (BFS), cada nó recebe um link de sufixo (suffixLink) que aponta para o maior prefixo que também é sufixo do caminho atual, permitindo transições eficientes quando uma correspondência não é encontrada. Os links de saída (outputLink), por sua vez, criam uma estrutura em cascata que garante a detecção de todos os padrões que possam terminar em uma determinada posição do texto.

3. **Busca com suporte a L33T**: Para cada caractere do texto de entrada, o sistema consulta um mapa de transformações que pode substituí-lo por suas variações (como '4' -> 'a', '3' -> 'e'). O algoritmo então tenta todas as interpretações possíveis, seguindo as transições da máquina de estados e verificando correspondências através dos links de saída. Caracteres especiais como espaços resetam o estado atual, tratando cada palavra independentemente.

#### Análise e Contextualização

O aho-Corasick representa uma evolução significativa em relação às abordagens ingênuas de filtragem. Enquanto a Baseline percorre o texto uma vez para cada padrão, resultando em complexidade O(n · m), este algoritmo constrói uma máquina de estados que processa o texto em uma única passada, alcançando O(n + m), onde n é o tamanho do texto e m é o tamanho dos padrões. Esta diferença torna-se crucial em aplicações de tempo real com milhares de termos proibidos.

Além disso, diferentemente da Baseline e HashTable, a Aho-Corasick detecta facilmente padrões mascarados, isto é, permite identificar "cachorro" mesmo quando escrito como "c@ch0rr0", sem a necessidade de guardar todas as variações. Outra característica importante é a capacidade de detectar padrões sobrepostos; por exemplo, se “cacho” for considerada uma palavra proibida, o algoritmo também a identificará dentro de “cachorro”. No entanto, essa mesma propriedade pode levar à ocorrência de falsos positivos, já que qualquer substring correspondente ao padrão armazenado será sinalizada, independentemente do contexto.

---

### Baseline

O Baseline (ou implementação "ingênua") representa a abordagem mais simples e direta possível para a resolução do problema de filtragem de palavras. Ele serve como uma linha de base, um ponto de referência para avaliar se o custo de desenvolvimento e o consumo de recursos de estruturas mais complexas (como as outras estruturas e implementações) realmente se justificam frente aos resultados obtidos.

#### Funcionamento do Algoritmo

A implementação desenvolvida para o Baseline não utiliza estruturas de dados avançadas para buscas de texto, baseando-se apenas em um array simples de strings (String[]) para armazenar a lista negra (blacklist) de palavras ofensivas. O verificação funciona da seguinte maneira:

1. **Separação da frase (Tokenização)**: Quando a função hasBadWord recebe uma frase, ela a divide em um array de palavras independentes.

2. **Busca Linear (Linear Search)**: O algoritmo itera sobre cada palavra da frase tokenizada e, para cada uma delas, invoca o método isBadWord. Este método percorre sequencialmente todo o array de palavras ofensivas, realizando uma comparação através do método .equals(). E ao final, retorna a quantidade de palavras que são consideradas `bad` (estão presentes na blacklist).

#### Análise e Contextualização

Este método é considerado "ingênuo" principalmente devido à sua ineficiência durante a busca. Para uma frase contendo N palavras e uma blacklist com M palavras ofensivas, a complexidade computacional seria de O(n · m) verificando cada palavra com toda a blacklist.

Além disso, o Baseline se torna altamente suscetível a falsos negativos. Ele é incapaz de detectar variações triviais de uma ofensa (como diferenças de capitalização ou o uso de caracteres da tabela L33T), a menos que cada uma dessas variações seja explicitamente adicionada na blacklist.

Apesar dessas fraquezas evidentes na capacidade de detecção e velocidade em entradas grandes, o Baseline deve apresentar um consumo de memória extremamente baixo, servindo como o limite inferior ideal para os experimentos propostos.

---

### HashTable

A estrutura de HashTable introduz uma melhoria significativa na etapa de busca de palavras em relação ao Baseline. No contexto do projeto, foi utilizada a implementação HashSet da biblioteca padrão do Java. Essa estrutura se destaca por oferecer uma complexidade de tempo média constante para inserções e buscas rápidas.

#### Funcionamento do Algoritmo

A implementação da HashTable para este filtro foca na velocidade de verificação das palavras contidas na blacklist. O fluxo ocorre da seguinte maneira:

1. **Inicialização e Armazenamento**: Durante a instanciação da classe HashTable, o construtor recebe um array contendo as palavras ofensivas. O algoritmo inicializa um `HashSet<String>` e insere todas as palavras do array.

2. **Separação da frase (Tokenização)**: O método countBadWords recebe uma frase completa e a divide em um array de strings individuais.

3. **Busca e contagem de ocorrências**: O algoritmo itera sobre cada palavra da frase e utiliza o método .contains() do HashSet para verificar sua presença na blacklist. O Java calcula o hash da palavra e busca diretamente no endereçamento da memória, evitando a necessidade de percorrer a blacklist inteira. Se a palavra for ofensiva, uma variável de contagem é incrementada e seu valor final é retornado.

#### Análise e Contextualização

A principal vantagem da HashTable deverá ser evidenciada no teste de "Velocidade para entradas grandes". Enquanto o Baseline possui uma desvantagem ao verificar elemento por elemento, a HashTable manterá um desempenho de verificação extremamente rápido, sendo pouco impactada pelo crescimento do tamanho da blacklist.

Apesar da velocidade superior de busca, a estrutura traz trade-offs importantes que serão explorados nos demais testes:

- **Consumo de Memória**: É esperado que a HashTable apresente um custo de memória notavelmente maior que o Baseline. Isso acontece porque a estrutura exige mais memória para organizar suas tabelas internas e para gerenciar as colisões de dados.

- **Capacidade de Detecção Limitada**: Assim como o Baseline, a HashTable é severamente afetada pela restrição de que a String recebida deve ser idêntica à badword contida no HashMap.

---

### Trie

---

### Regex

---

## Como rodar o experimento?

```shell
$ ./run-benchmark.sh
```

Para mais informações, rode `./run-benchmark.sh --help`. 

## Metodologia

Nesse contexto, os passos para a execução desse experimento foram os seguintes:

### 1. Definição da estratégia e métodos de filtragem

A intenção inicial do projeto foi analisar a Trie em um contexto prático do mundo real. Durante a etapa de levantamento de possíveis aplicações que o uso dessa estrutura poderia trazer benefícios, foi observado um interesse crescente na moderação de conteúdo em ambientes digitais, de forma que leis que visam proteger crianças e adolescentes durante o consumo de conteúdo em ambientes digitais vêm sendo cada vez mais discutidas. Partindo desses dois príncipios, foi identificado um ponto "em aberto" relacionado à moderação de conteúdo nas plataformas digitais: _como realizar a filtragem de conteúdo ofensivo de uma maneira eficiente?_

Considerando a proposta da Trie de ser uma estrutura eficiente de busca e recuperação em grandes volumes de conteúdo, foi definido que os filtros deveriam ser baseados na estratégia de blacklists discutidas anteriormente. Com isso, foram escolhidos outros métodos comumente utilizados para esse tipo de problema: **Aho-Corasick**, **HashTable** e **Regex**, notoriamente conhecidos por sua eficiência na busca.

Além disso, como métrica base de comparação (Baseline), foi implementado um simples filtro baseado em array, com tempo de busca linear.

### 2. Busca por Blacklists

O próximo passo foi procurar listas de palavras banidas para formar um catálogo próprio de experimentação. Para isso, a principal fonte foi o GitHub, que contém diversos projetos que mantém catálogos de palavras ofensivas. Os seguintes projetos foram utilizados para montar a coleção de "bad words" para esse projeto: 
  - [Cuss Words](https://github.com/words/cuss/blob/main/pt.js)
  - [Bad Words](https://github.com/Kuyoku-san/Badwords/blob/main/badwords.txt)
  - [Chat Detox](https://github.com/dunossauro/chat-detox/blob/main/palavras.txt)
  - [Lista de palavras ofensivas](https://github.com/Pqoh/Palavreados/blob/main/palavras.txt)
  - [Filtro de palavras ofensivas](https://github.com/leviobrabo/filtro/blob/main/dados/filtro.txt)
  - [Content Moderation](https://github.com/bielgennaro/content-moderation/blob/main/src/dictionaries/pt-br.ts)
  - [Word List for speech recognition subtitling](https://github.com/sayonari/goodBadWordlist/blob/main/pt/BadList.txt)

Durante a análise, foi possível notar que muitas listas possuem variações [Leet](https://pt.wikipedia.org/wiki/Leet) da mesma palavra, o que consumiria mais memória para armazenar todas as palavras nas estruturas estudadas. Para evitar isso, foi criada uma tabela customizada que relaciona caracteres usuais com variações leet/acentuações comuns, permitindo uma maior customização na detecção de palavras.

Por fim, foi criado também um conjunto de good words, retirado do dicionário brasileiro disponibilizado por Linux, geralmente encontrado no diretório `/usr/share/dict`.

### 3. Implementação dos métodos

- a aho foi pensada para contrapor a trie, explorando o algoritmo de bfs, ao contrário do utilizado pela trie normal (dfs), referencia: TheAlgorithms
- a trie teve uma implementação customizada utilizando algoritmos de window sliding
- regex, hash table e baseline?

### 4. Geração das cargas de teste

As cargas de teste correspondem às palavras/frases que serão avaliadas por cada estrutura e foram geradas por meio de scripts em python. As etapas para a geração das entradas foram:

- **Limpeza e filtragem das palavras "ruins" e "boas"**: O primeiro passo envolveu a formatação das palavras, de forma a retirar codificações e acentuações, e remover palavras repetidas. Além disso, para cada conjunto de palavras, foram feitos alguns processamentos adicionais:

  - **Bad Words**: com o propósito de implementar uma Trie que conseguisse identificar mais rapidamente se uma palavra é ofensiva, o arquivo final de "bad words" contém também um índice que indica, para cada palavra, a partir de qual momento ela difere de uma "good word", ou seja, seus prefixos são certamente diferentes. Existem casos em que não é possível fazer essa distinção, e por isso, o índice salvo é -1, o que indica que ela será ofensiva apenas se for exatamente aquela palavra.
  - **Good Words**: para simplificar as entradas, as palavras foram filtradas por meio do seu prefixo, mantendo apenas aquelas com o maior tamanho. Através disso, palavras "redundantes" como `computa` foram retiradas do resultado final e `computação` e `computaremos`, por exemplo, mantidas.

- **Geração de frases e palavras aleatórias**: Utilizando as palavras propriamente formatadas, foram geradas frases e palavras geradas de maneira pseudo-aleatórias, utilizando mecanismos disponíveis em Python. Ao fazer isso, cada linha gerada foi categorizada em um dos seguintes tipos:
  - **None**: Não foi feita nenhuma modificação na entrada.
  - **Upper**: Algumas letras tiveram alterações na capitalização.
  - **Spaced**: Foi adicionado espaçamento entre os caracteres.
  - **Stretched**: Algumas letras foram repetidas.
  - **Hidden**: Uma palavra normal que possui uma palavra ofensiva "escondida" nela, é o caso do Problema de Scunthorpe discutido anteriormente (falsos positivos)

Isso foi feito para que fosse possível analisar quais métodos têm melhor desempenho em cada categoria.

### 5. Análise de Desempenho

O estudo acerca do desempenho dos métodos de filtragem foi feito utilizando a ferramenta de Benchmark JMH (Java Microbenchmark Harness), para garantir assertividade em relação aos resultados. Isso é possível porque o JMH inicia vários ciclos onde realizará sucessivas vezes as operações que estão sendo testadas e, após isso, irá tirar a média entre as operações.

Além disso, há o uso de outras estratégias para evitar interferências externas, como a execução de séries de aquecimento, com o propósito de minimizar o impacto da lentidão das execuções iniciais, causados pelo lento "startup" da JVM, e o uso de forks, que isola a execução de cada Benchmark, evitando interferências de otimizações anteriores.

Para realizar esse experimento, foram definidos 5 forks onde são realizadas 10 ciclos de medição e 5 ciclos de aquecimento por fork, totalizando 75 ciclos de execução no total para cada .

Os experimentos foram divididos em 4 categorias: **Velocidade de Inserção**, **Consumo de Memória**, **Análise de Frases** e **Capacidade de Detecção**.

## Experimentos

### Velocidade de Inserção



### Consumo de Memória



### Análise de Frases

| Método       | None | Upper | Spaced | Encoded | Stretched |
|--------------|------|-------|--------|---------|-----------|
| Aho-Corasick |      |       |        |         |           |
| Baseline     |      |       |        |         |           |
| HashTable    |      |       |        |         |           |
| Regex        |      |       |        |         |           |
| Trie         |      |       |        |         |           |

### Capacidade de Detecção

| Método       | None | Upper | Hidden | Spaced | Encoded | Stretched |
|--------------|------|-------|--------|--------|---------|-----------|
| Aho-Corasick |      |       |        |        |         |           |
| Baseline     |      |       |        |        |         |           |
| HashTable    |      |       |        |        |         |           |
| Regex        |      |       |        |        |         |           |
| Trie         |      |       |        |        |         |           |

## Conclusão

## Ameaças à validade

1. O experimento foi conduzido com tamanho máximo de entrada de 10<sup>5</sup> para inserção, o que, em contextos globais, pode não ser suficiente para acomodar todas as palavras consideradas ofensivas. Já para a busca, as estruturas tiveram apenas que comportar aproximadamente 10<sup>3</sup> elementos. Com uma lotação maior, paginação de memória e estratégias de Garbace Collector podem se tornar mais relevantes e alterar os resultados.
2. Benchmarks dependem fortemente do Hardware, Sistema Operacional, e implementação da máquina virtual de Java (JVM). Mesmo utilizando JMH para tentar minimizar erros pontuais, resultados podem variar se replicados em arquiteturas diferentes.
3. Como discutido anteriormente, uma das restrições desse experimento foi de que a string recebida não poderia ser modificada e deveria ser lida daquela maneira, o que atrapalhou significativamente o desempenho de memória do Regex e de busca da HashTable. Em aplicações reais, uma restrição desse tipo não existiria e ambas as estruturas provavelmente apresentariam um desempenho melhor.
4. Com o intuito de analisar o desempenho em casos extremos, as entradas utilizadas para testar a capacidade de detecção em frases chegaram à casa de 10<sup>7</sup> (1000 frases de 10000 palavras cada), porém, nos contextos onde esses filtros se mostram necessários, como chats de jogos e comentários em plataformas digitais, é comum que sentenças não tenham mais do que 300 palavras cada.

### Experimento de Validação (Ameaça 4)

| Método       | None | Upper | Spaced | Encoded | Stretched |
|--------------|------|-------|--------|---------|-----------|
| Aho-Corasick |      |       |        |         |           |
| Baseline     |      |       |        |         |           |
| HashTable    |      |       |        |         |           |
| Regex        |      |       |        |         |           |
| Trie         |      |       |        |         |           |

## Trabalhos Futuros

- Como distinguido na seção de [Problemática](#problemática), esse projeto se resumiu à comparação entre métodos que utilizam blacklists para detecção e filtragem de palavras ofensivas, deixando de lado a estratégia de análise contextual, que pode trazer benefícios quando comparado ao simples uso de blacklists para categorizar palavras. Por isso, seria interessante expandir o experimento para algoritmos que conseguem realizar análises contextuais.
- Estudar se implementações reduzidas espacialmente da Trie como a [Patricia Trie](https://en.wikipedia.org/wiki/Radix_tree) e a [Height Optimized Trie](https://15721.courses.cs.cmu.edu/spring2019/papers/08-oltpindexes2/p521-binna.pdf) podem ser mais eficazes como filtros, combinadas ao algoritmo de Aho-Corasick.
- Avaliar se combinações entre as estratégias de análise contextual e uso de blacklists podem trazer um resultado ainda melhor.

## Contribuintes

- [Anderson Breno Santos Silva](https://github.com/AndersonBreno1)

- [Arthur Ledra de Azevedo](https://github.com/arthurlazevedo)

- [Carlos Arthur Nóbrega Soares](https://github.com/C-Arthurr)

- [Gabriel Victor de Sousa Lima](https://github.com/gvsl60)

- [Joéliton Elias Pereira Junior](https://github.com/JoelitonEPJ)

Projeto feito como trabalho final da disciplina de Estrutura de Dados e Algoritmos (EDA) e Laboratório de Estrutura de Dados e Algoritmos (LEDA) do curso de Ciência da Computação na Universidade Federal de Campina Grande (UFCG) durante o período 2025.2.