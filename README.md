# TPM
Exercitiul 1.

Deoarece avem o ordine de scrieri și citiri care respectă ordinea cronologică a firelor de execuție, putem spune că aceasta este o secvență linearizabilă. Ordinea operațiilor în această secvență este următoarea:
1.	B scrie 1 în r: B -> r.write(1)
2.	A citește valoarea 1: A -> r.read(): 1
3.	A citește din nou valoarea 1: A -> r.read(): 1
4.	C scrie 2 în r: C -> r.write(2)
5.	B citește valoarea 2: B -> r.read(): 2
6.	B citește din nou valoarea 2: B -> r.read(): 2
7.	C scrie 1 în r: C -> r.write(1)
8.	A citește valoarea 1: A -> r.read(): 1
9.	
Această ordine a operațiilor respectă proprietatea de linearizabilitate, deoarece:

•	Fiecare operație de citire reflectă valoarea actuală a lui r la momentul respectiv, așa cum a fost stabilită de scrierile anterioare.

•	Schimbările asupra variabilei r sunt vizibile în mod corespunzător între diferitele operații de citire și scriere.

Astfel, dacă secvența este linearizabilă, ea respectă automat și consistența secvențială, deoarece ambele proprietăți garantează o ordine globală a operațiilor, dar linearizabilitatea impune un criteriu mai strict ,deci putem spune ca este si consistent secventiala.

Exercitiul 2.

De obicei, punem lock() înainte de blocul try ca să evităm problemele care pot apărea dacă blocarea dă greș. Dacă apelăm lock() în interiorul blocului try și se întâmplă ca blocarea să dea o eroare, programul va sări direct la finally și va încerca să deblocheze (unlock()) ceva ce nu a fost blocat. Asta poate duce la erori neprevăzute.

Pe de altă parte, dacă apelăm lock() înainte de try, știm sigur că blocarea a reușit. Așa, când ajungem la finally, putem să deblocăm fără griji. Practic, e o metodă mai sigură și ne asigurăm că blocarea funcționează cum trebuie fără să ne facem griji de erori accidentale.

Exercitiul 3.

În algoritmul Bakery, folosim comparația (label[i], i) tocmai ca să ne asigurăm că firele de execuție intră în secțiunea critică în ordinea corectă, chiar și atunci când două fire ajung aproape simultan.

1.In cazul in care am folosi doar etichetele (label[i]) , presupunem ca avem doua fire de executie T1 si T2 care ajung simultan atunci eticheta ambelor thread-uri iau aceeasi valoarea max(…) + 1 , astfel avand aceeasi eticheta  si fara un alt criteriu suplimentar , nu avem o regula care sa decida care dintre ele va intra primul in sectiunea critica deci ar aparea deadlock. Algoritmul Bakery trebuie să respecte ideea de „primul venit, primul servit”, iar dacă două fire au aceeași etichetă, compararea doar pe baza etichetelor nu poate decide corect.

2.In cazul in care am decide doar pe baza indicilor (adică numărul de identificare al fiecărui fir), algoritmul ar ignora ordinea reală în care au sosit firele. De exemplu, să zicem că T2 ajunge primul și cere accesul, dar T1, cu un indice mai mic, ajunge puțin mai târziu. Dacă folosim doar indicii, T1 ar intra prima în secțiunea critică doar pentru că are un număr mai mic, deși T2 a ajuns primul. Acest lucru ar fi incorect, iar firul care a ajuns mai devreme ar trebui să aștepte fără motiv.

Prin compararea ambelor – eticheta și indicele – în ordinea (label[i], i), algoritmul se asigură că:

1.	Eticheta contează mai întâi, deci firul care așteaptă de mai mult timp intră primul.
   
2.	Indicele e folosit ca indice decisiv dacă două fire au aceeași etichetă. Astfel, firul cu indicele mai mic va avea prioritate, evitând orice conflict sau blocare.
   
În acest fel, algoritmul Bakery păstrează corectitudinea ordinii și evită blocajele între fire, chiar și când două fire ajung să ceară accesul în același timp.

Exercitiul 5.

•	În implementarea nouă, fiecare thread accesează secțiunea critică de mai multe ori consecutiv într-un singur apel de lock() (controlat de variabila BATCH_SIZE).Ajută la reducerea numărului de intrări/ieșiri în secțiunea critică, eliminând necesitatea unui acces constant la algoritmul de locking pentru fiecare incrementare individuală a contorului partajat.

•	Am adaugat un vector de frecvență (accessFrequency) pentru a limita accesul fiecărui thread la secțiunea critică astfel încât niciun thread să nu poată accesa mai des secțiunea critică decât celelalte. În canAccess(i), un thread are voie să intre doar dacă numărul său de accesări este mai mic sau egal cu cele ale altor thread-uri.

•	În implementarea clasică, lock() permite unui thread să intre în secțiunea critică dacă nu există un alt thread la același nivel de prioritizare (level) și dacă victim este setat corect. Am adăugat o verificare suplimentară prin canAccess(i), care ține cont de frecvența de acces, și folosește Thread.yield() pentru a permite altor thread-uri să preia procesorul dacă un thread nu este încă eligibil să intre în secțiunea critică.
