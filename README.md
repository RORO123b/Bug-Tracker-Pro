# Barbulescu Robert Cristian 325CD

# Tema 2 POO  - Bug Tracker Pro

## Structura temei

```
.
-- src/
    `-- main/
        `-- java/
            |-- commands/       --> comenzile necesare pentru executarea temei
            |   |-- search/     --> strategiile necesare pentru comanda de search
            |-- enums/
            |-- fileio/         --> fisierele folosite pentru input
            |-- main/           --> aplicatia principala
            |-- milestones/ 
            |-- tickets/
            |   |-- action/
            `-- users/
```

## Design Patterns

### 1. Singleton
    Am folosit singleton pentru a crea AppCenter-ul, ideea mea a fost sa o folosesc ca pe un fel de data center sau un operation center unde se fac diferite updatari sau se ofera diferite date.

### 2. Commands
    Exista o "telecomanda" `Invoker`, care preia comanda ce trebuie executata si ii apeleaza metoda execute corespunzatoare. Fiecare comanda are un fisier separat pentru rularea lor. De asemenea, am implementat un `CommandHelper` care ajuta la crearea diferitelor noduri pentru output.

### 3. Strategy
    Pentru comanda de search am implementat acest design pattern, fiecare filtru avand strategia sa separata. Developerul si Managerul sunt abordati separat, fiecare are la dispozitie cate un "engine" care este folosit pentru a urma Open-Closed Principle. Trebuie mentionate ca am folosit un unbounded wildcard ("?") in cadrul unei liste pentru a primi ori tichete ori developeri (in functie de ce se cere).

### 4. Factory
    Pentru useri am decis sa folosesc factory intrucat nu are campuri optionale si mi-a usurat munca acest pattern.

### 5. Builder
    Ultima dar nu cea din urma. Am vrut cu aceasta sa-mi imbunatatesc la maxim abilitatile de POO si eu zic ca am facut o treaba destul de buna. Pentru builderul de tichete am creat in primul rand o clasa abstracta generica (`TicketBuilder<T extends Ticket, B extends TicketBuilder<T, B>>`) care foloseste self-bounded generics pentru a permite method chaining corect si type-safe. Aceasta clasa de baza contine metodele comune pentru toate tipurile de tichete (id, title, type, businessPriority, etc.) si o metoda abstracta `self()` care returneaza instanta builder-ului concret. Apoi, fiecare tip specific de tichet (Bug, FeatureRequest, UIFeedback) are propriul builder concret care extinde `TicketBuilder` si adauga metodele specifice tipului respectiv. De exemplu, `BugBuilder` adauga metode pentru `severity`, in timp ce `FeatureRequestBuilder` adauga metode pentru `businessValue` si `customerDemand`. Aceasta abordare permite reutilizarea codului comun si extensibilitate pentru tipuri noi de tichete. De asemenea, am implementat un `ActionBuilder` similar pentru crearea obiectelor de tip `Action` care inregistreaza istoricul modificarilor asupra tichetelor.

    Trebuie mentionat ca si pentru actiuni am folosit un builder (mai simplu) pentru a facilita crearea de actiuni in cadrul `viewTicketHistory`.

## Dificultati intampinate + cum le-am rezolvat
    - Pentru search, am simtit nevoia sa aprofundez expresiile lambda si LLM-urile mi-au dat ideea sa folosesc stream-uri pentru a aplica mai multe operatiuni unei liste (a trebuit sa invat ce fac si acestea)
    - Am considerat ca Comparatorii sunt un tool important ce ar trebui sa-l incorporez in tema, astfel l-am folosit pentru a sorta developeri, milestone-uri si tichete

## Abordarea erorilor
    Aveam optiunea pentru a crea exceptii separate pentru fiecare tip de eroare sau sa le folosesc pe cele built-in. Am optat pentru cea din urma deaorece am putut sa impart toate erorile in 2 categorii: cele pentru argumente gresite si cele pentru stari gresite ale elementelor. Astfel am putut folosi `IllegalArgumentException` sau `IllegalStateException`

## Feedback
    Din punctul meu de vedere a fost cea mai bine conceputa tema de pana acum. A fost foarte laborioasa, insa nu overwhelming, deoarece totul avea sens si un fir clar de abordare. Felicitari autorilor acestei teme, deoare, in cazul meu, m-au facut sa-mi imping limitele. Daca vreti sa mai reciclati tema, din punctul meu de vedere aveti unda verde, este genul de tema satisfacatoare si care atinge acel sweet spot al dopaminei. Also, felicitari ca ati avut cerinta clara pentru cat de mult scris era.