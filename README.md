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

- **Capacidade de Detecção Limitada**: Assim como o Baseline, a HashTable é severamente afetada pela restrição de que a String recebida deve ser idêntica a badword contida no HashMap.

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

- [Gabriel Victor de Sousa Lima](https://github.com/gvsl60)

- [Joéliton Elias Pereira Junior](https://github.com/JoelitonEPJ)

Projeto feito como trabalho final da disciplina de Estrutura de Dados e Algoritmos (EDA) e Laboratório de Estrutura de Dados e Algoritmos (LEDA) da graduação em Ciência da Computação na Universidade Federal de Campina Grande (UFCG) no período 2025.2.