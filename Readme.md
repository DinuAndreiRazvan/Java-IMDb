# CLI IMDb App

## Mod de Implementare
In vederea realizarii functionalitatiilor aplicatiei am utilizat clase, interfete si enumerarile, folosind principiile programari orientate pe obiecte si diferite sabloane de proiectare. In continuare, va prezint informatii sugestive cu privire la implementare :

- Main - Clasa are o singura functie main() care apeleaza metoda run() din clasa IMDB si astfel porneste aplicatia
- IMDB – Clasa care detine datele aplicatiei si functionalitati ale interactiunii 
utilizatorului cu informatiile din sistem. Aceasta clasa foloseste Singleton Pattern pentru a fi instantiata o singura data. Am schimbat in unele cazuri  afisarea unei liste de elemente pentru a fi prelucrate cu o cautare prin introducerea unui identificator unic (username, name, title etc.), deoarece in cazul dezvoltarii datelor din sistem, implementarea ulterioara este mai eficienta
si mai confortabila pentru utilizator.
- UserFactory – Clasa  proiectata in vederea implementarii sablonului Factory Pattern, pentru o mai usoara instantiere a obiectelor care extind clasa User
ExperienceStrategy – Interfata proiectata in vederea implementarii sablonului Strategy Pattern pentru cresterea in experienta a utilizatorilor in urma unor 
anumite actiuni
- Rating – Implementeaza ExperienceStrategy si ofera punctajul acumulat de utilizator in urma adaugarii unui Rating la o productie
- Production - Implementeaza ExperienceStrategy si ofera punctajul acumulat de utilizator in urma adaugarii unei noi productii in sistem
- Actor - Implementeaza ExperienceStrategy si ofera punctajul acumulat de utilizator in urma adaugarii unui nou actor in sistem
- Observer & Subject - Interfete proiectate in vederea implementarii sablonului Observer Pattern pentru realizarea sistemului de notificari
- User – Clasa descrie proprietatiile si informatiile(Clasa Information) unui utilizator si metodele de prelucrare ale acestora. Aici am implementat Builder 
Pattern pentru clasa Information, utilizand o clasa Builder cu aceleasi atribute ca Information. Aceasta detine metoda build() care apeleaza constructorul privat al clasei Information. De asemenea, clasa User implementeaza si interfata Observer, astfel utilizatorii putand fii notificati de catre subiecte.
- Request – Implementeaza ExperienceStrategy si ofera punctajul acumulat de utilizator daca o cerere propusa de acesta este rezolvata. In plus, aceasta clasa 
implementeaza si Subject, notificand utilizatorii atunci cand au primit o cerere noua sau o cerere le-a fost respinsa sau rezolvata.
- JsonSimpleActors – Aceasta clasa realizeaza citirea tuturor informatiilor despre actori dintr-un fisier .json si transpunerea si adaugarea lor in sistem, sub
forma de obiecte de tipul Actor
- JsonSimpleProduction – Aceasta clasa realizeaza citirea tuturor informatiilor despre filme si seriale dintr-un fisier .json si transpunerea si adaugarea lor in  sistem, sub forma de obiecte de tipul Movie sau Series
- JsonSimpleRequest – Aceasta clasa realizeaza citirea tuturor informatiilor despre cereri dintr-un fisier .json si transpunerea si adaugarea lor in sistem, sub
forma de obiecte de tipul Request
- JsonSimpleUsers – Aceasta clasa realizeaza citirea tuturor informatiilor despre utilizatorii dintr-un fisier .json si transpunerea si adaugarea lor in sistem, sub forma de obiecte de tipul User (Admin, Regular si Contributor) , utilizand metoda Factory din clasa UserFactory
