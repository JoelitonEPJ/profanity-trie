# Estratégias de Filtragem de palavras ofensivas

Implementação, documentação e comparação da Trie e outros métodos para a filtragem de palavras ofensivas.

## Sumário

- [Estrutura de diretórios](#estrutura-de-diretórios)

- [Introdução](#introdução)

- [Objetivo](#objetivo)

- [Estruturas e Algoritmos](#estruturas-e-algoritmos)

- [Metodologia](#metodologia)

- [Experimentos](#experimentos)

- [Conclusão](#conclusão)

- [Ameaças à validade](#ameaças-à-validade)

- [Projetos Futuros](#projetos-futuros)

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

O arquivo `run-benchmark.sh` foi utilizado para rodar o projeto de maneira simples. Ele é responsável por criar os diretórios necessários e fazer o pré-processamento necessário para rodar as comparações. Para mais informações, é possível rodá-lo com as flags `-h`/`--help` para entender melhor como ele funciona.

## Introdução

### Problemática

A existência de um filtro de palavras ofensivas é muito importante para plataformas digitais que almejam ser seguras. Filtros desse tipo são utilizados nos mais diversos ambientes digitais como redes sociais, fóruns, jogos online e plataformas de comunicação. É o uso dessa ferramenta que torna possível a prevenção de discursos de ódio, moderação e cumprimento de políticas, e proteção de usuários vulneráveis como crianças e adolescentes, por exemplo.

Porém, mesmo com a grande relevância dessa ferramenta, ainda não existe um meio definitivo para fazer essa filtragem, já que existem múltiplas estratégias e múltiplas maneiras de aplicar cada uma dessas estratégias. Podemos separar as estratégias em dois tipos diferentes, os quais são:

- **Uso de blacklists**: estratégias desse tipo envolvem a criação de listas de palavras banidas. Geralmente, são usadas em conjunto com algoritmos/estruturas especializados na busca e recuperação rápida de informação em strings, como **Regex**, **Aho-Corasick** e **Trie**, ou estruturas de busca rápida de propósito geral, como **HashTable**;

- **Análise Contextual**: métodos relacionados a essa estratégia estão geralmente relacionados à algoritmos de PLN (Processamento de Linguagem Natural) ou de classificação, como o [classificador de Naive Bayes](https://en.wikipedia.org/wiki/Naive_Bayes_classifier), que conseguem detectar padrões nas frases e classificá-las de acordo.

---

### Escopo

A filtragem de palavras ofensivas é um tema bem complexo com muitos assuntos que podem se ramificar em diversos projetos diferentes. Dessa forma, nós tivemos o cuidado de definir bem o escopo desse projeto de antemão, de forma que fosse possível discutir e analisar de maneira completa o tema proposto por uma das vias.

Dessa forma, tendo essa problemática em mente e considerando que a estrutura base de análise desse projeto inicialmente escolhido era a **Trie**, decidimos focar nos métodos relacionados ao uso de blacklists para realizar essa filtragem, logo, nosso projeto discutirá os seguintes métodos: **Aho-Corasick**, **HashTable**, **Trie** e **Regex**.

Para realizar a análise desses métodos, nós definimos duas restrições para os nossos testes:

- **Palavras diferentes estarão separadas**: Para evitar problemas como o de [Scunthorpe](https://en.wikipedia.org/wiki/Scunthorpe_problem), que dificultariam a realização de análises estatísticas, nós evitamos trabalhar com a detecção de palavras ofensivas que estejam coladas à outras. Porém, iremos fazer testes para comparar se algum método, da maneira que foi implementado, sofreria desse problema;

- **A String recebida não pode ser processada**: Para comparar puramente a capacidade das diferentes estruturas/algoritmos, optamos por adicionar a restrição que não podemos fazer um pré-processamento na palavra/frase que estará sendo analisada, ou seja, não é permitido normalizar a palavra (substituir caracteres unicode por seus equivalentes ascii) ou mudar sua capitalização.

## Objetivo

Nesse contexto, o propósito do seguinte projeto é analisar e comparar diferentes implementações para filtragem de palavras ofensivas que se baseiam no uso de blacklists para categorizar palavras como `bad` ou `good` words. O estudo tem como objetivo analisar qual abordagem se mostra mais eficaz para identificar palavras ofensivas no meio de frases geradas pseudo-aleatoriamente livres de contexto e gírias, de maneira que seja possível analisar friamente os dados do experimento.

O tema discutido vem se tornando cada vez mais relevante nos dias atuais, visto que diversos estados e até mesmo países iniciaram a instauração de leis relacionadas à obrigatoriedade da verificação de idade na internet, a exemplo da [Califórnia](https://leginfo.legislature.ca.gov/faces/billTextClient.xhtml?bill_id=202520260AB1043), [Brasil](https://g1.globo.com/tecnologia/noticia/2025/12/11/eca-digital-protecao-criancas-adolescentes-internet.ghtml) e [Inglaterra](https://en.wikipedia.org/wiki/Online_age_verification_in_the_United_Kingdom), com a justificativa de proteger crianças e adolescentes nos ambientes digitais, proibindo que conteúdos considerados inadequados sejam exibidos para eles, algo que está muito ligado à moderação de conteúdo através da filtragem de palavras ofensivas.

## Estruturas e Algoritmos

### Aho-Corasick

---

### Baseline

---

### HashTable

---

### Trie

---

### Regex

---

## Metodologia

Nesse sentido, os passos seguidos para a execução desse experimento foram os seguintes:

### 1. Definição dos métodos de filtragem



### 2. Busca por Blacklists

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


### 3. Geração das cargas de teste (entrada)

Após separar as palavras que servirão de entrada, nós fizemos um "cleanup", removendo duplicatas e normalizando elas para ascii. Com isso, salvamos as palavras no arquivo [Formatted Bad Words](./data/bad_words/formatted.csv).

Através dele, criaremos as entradas que servirão para os diferentes experimentos de forma programática, utilizando scripts na linguagem python

### 4. Análise de Desempenho das estruturas em diferentes contextos

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

## Conclusão

## Ameaças à validade

## Projetos Futuros

## Contribuintes

- [Anderson Breno Santos Silva](https://github.com/AndersonBreno1)

- [Arthur Ledra de Azevedo](https://github.com/arthurlazevedo)

- [Carlos Arthur Nóbrega Soares](https://github.com/C-Arthurr)

- [Joéliton Elias Pereira Júnior](https://github.com/JoelitonEPJ)

- [Gabriel Victor de Sousa Lima](https://github.com/gvsl60)