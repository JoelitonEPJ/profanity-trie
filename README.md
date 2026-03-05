# Estratégias de Filtragem de palavras ofensivas

Implementação, documentação e comparação da Trie e outros métodos na filtragem de palavras ofensivas.

## Introdução

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
  - [Alfabeto Grego](https://pt.wikipedia.org/wiki/Alfabeto_grego)
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