# Estratégias de Filtragem de palavras ofensivas

Implementação, documentação e comparação da Trie e outros métodos para a filtragem de palavras ofensivas.

## Sumário

- [Estrutura de diretórios](#estrutura-de-diretórios)

- [Introdução](#introdução)

- [Objetivo](#introdução)

- [Introdução](#introdução)

- [Introdução](#introdução)

### Versionamento de código

O versionamento do projeto foi feito utilizando [Git](https://git-scm.com/) (por meio da plataforma [Github](https://docs.github.com/pt)), permitindo o controle das alterações ao longo do projeto. Foram criadas branches para organizar melhor o fluxo de desenvolvimento e isolar as alterações. Além disso, como regra, permitimos apenas que a branch `main` fosse alterada por meio de pull requests, que deveriam obrigatoriamente ser revisadas por um membro da equipe, garantindo a integração organizada das modificações ao repositório principal.

Dessa forma, foi possível garantir que todos os integrantes do projeto estivessem cientes das alterações que foram introduzidas. Foi criada também uma restrição que pull requests seriam apenas aceitas com a opção de `Squash and Merge`, que possibilita um histórico mais limpo de commits, de forma que seja mais fácil notas a contribuição para o repositório.

### Comparação entre os métodos de filtragem

A comparação entre os métodos de filtragem foi feita utilizando a biblioteca [JMH](https://openjdk.org/projects/code-tools/jmh/) (Java Microbenchmark Harness), uma biblioteca poderosa oficial da [OpenJDK](https://openjdk.org/), utilizada para criar, executar e analisar microbenchmarks em linguagens que utilizam a máquina virtual de java (JVM). Para esse projeto, utilizamos ela para comparar diferentes métricas das estruturas/algoritmos implementados na linguagem Java.

### Geração de inputs (carga de dados)

A geração das cargas de dados foi feita através da linguagem de programação [Python](https://www.python.org/about/), escolhida por sua simplicidade, legibilidade e alto nível de abstração. Foram desenvolvidos scripts específicos para a criação de cenários distintos de testes (especificado mais à diante) e para formatação dos dados de entrada, permitindo a simulação de entradas com diferentes comportamentos.

### Plotagem (geração) de gráficos

A geração dos gráficos foi realizada a partir dos dados experimentais armazenados em arquivos `.csv`, os quais continham os resultados obtidos durante a execução dos diferentes testes. Para a visualização e análise desses dados, utilizou-se a biblioteca [Matplotlib](https://matplotlib.org/) da linguagem Python, que permitiu a construção de gráficos que representassem o comportamento e o desempenho dos métodos avaliados.

## Estrutura de Diretórios

O código do projeto foi organizado da seguinte maneira:

```
/
├───scripts
│    └───python
│       ├────generate
│       └────plot
│   src
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

O arquivo `run-benchmark.sh` foi utilizado para rodar o projeto de maneira simples. Ele é responsável por criar os diretórios necessários e fazer o pré-processamento necessário para rodar as comparações. Para mais informações, é possível rodá-lo com as flags `-h`/`--help` para entender melhor como ele funciona.

## Introdução

### Qual é a necessidade de um filtro de palavras ofensivas?

A existência de um filtro de palavras ofensivas é muito importante para plataformas digitais que almejam ser seguras. Filtros desse tipo são utilizados nos mais diversos ambientes digitais como redes sociais, fóruns, jogos online e plataformas de comunicação. É o uso dessa ferramenta que torna possível a prevenção de discursos de ódio, moderação e cumprimento de políticas, e proteção de usuários vulneráveis como crianças e adolescentes, por exemplo.

Mesmo com a grande relevância dessa ferramenta

A criação de um filtro customizado de palavras ofensivas é um problema muito relevante para as plataformas online (muitos jogos e redes sociais possuem filtros desse tipo, por exemplo). Porém, mesmo sendo uma questão tão importante para o contexto atual, ainda não existe um meio definitivo para fazer essa filtragem.

A principal motivação por trás da escolha desse tema foi pelo seu caráter indefinido até o presente momento. Dessa forma, o projeto tem como propósito comparar os diferentes métodos tradicionais para filtragem de palavras ofensivas: Trie, RegExp e HashTable, com enfoque na performance da Trie quando comparada aos demais.

## Objetivo

O objetivo do seguinte projeto é comparar a performance em diferentes áreas dos métodos tradicionais existentes para detecção e filtragem de palavras ofensivas, nomeadamente: Algoritmo de Aho-Corasick, Trie, HashTable e Regex, com enfoque na performance da Trie quando comparada às demais, visando analisar sua viabilidade de uso em um caso real.

## Metodologia

Nesse sentido, os passos seguidos para a execução desse experimento foram os seguintes:

### 1. Definição dos métodos de filtragem



### 2. Definição de restrições para as comparações

- palavras separadas
- não poderá ser feito nenhum processamento na string

### 3. Busca por "Blacklists"

- pesquisa no github por listas palavras ofensivas, resultados:
  - [Cuss Words](https://github.com/words/cuss/blob/main/pt.js)
  - [Bad Words](https://github.com/Kuyoku-san/Badwords/blob/main/badwords.txt)
  - [Chat Detox](https://github.com/dunossauro/chat-detox/blob/main/palavras.txt)
  - [Lista de palavras ofensivas](https://github.com/Pqoh/Palavreados/blob/main/palavras.txt)
  - [Filtro de palavras ofensivas](https://github.com/leviobrabo/filtro/blob/main/dados/filtro.txt)
  - [Content Moderation](https://github.com/bielgennaro/content-moderation/blob/main/src/dictionaries/pt-br.ts)
  - [Word List for speech recognition subtitling](https://github.com/sayonari/goodBadWordlist/blob/main/pt/BadList.txt)

- pesquisa por possíveis codificações simples para letras:
  - [Tabela L33T](https://pt.wikipedia.org/wiki/Leet#Tabela_do_alfabeto_leet)


### 4. Geração das cargas de teste (entrada)

Após separar as palavras que servirão de entrada, nós fizemos um "cleanup", removendo duplicatas e normalizando elas para ascii. Com isso, salvamos as palavras no arquivo [bad_words_formatted.txt](./data/bad_words_formatted.txt).

Através dele, criaremos as entradas que servirão para os diferentes experimentos de forma programática, utilizando scripts na linguagem python

### 5. Análise de Desempenho das estruturas em diferentes contextos

## Experimentos

Os experimentos de comparação entre os métodos de filtragem foram divididos da seguinte maneira:

**1. Velocidade de Inserção**

Dadas as palavras ofensivas separadas por nós, queremos comparar quanto tempo levará para cada método conseguir processá-las. Em um cenário real, seria necessário sempre carregar as palavras ao iniciar a aplicação, logo, é necessário saber quais possuem uma performance melhor que não atrapalhe a experiência do usuários

**2. Consumo de Memória**

Iremos comparar o consumo de memória de cada método em sua lotação máxima. Para isso, planejamos utilizar a biblioteca JOL (Java Object Layout) que consegue analisar o `footprint` de cada objeto.

**3. Velocidade para entradas grandes**

Dadas "sentenças" geradas aleatoriamente, iremos comparar a velocidade de processamento de cada método para analisar a sua viabilidade em aplicações em tempo real.

**4. Capacidade de detecção**

Finalmente, iremos comparar a capacidade de detecção de profanidades de cada método, analisando seu desempenho com entradas "complicadas", tentando forçar falsos positivos/negativos.

## Análise dos Resultados

## Conclusão