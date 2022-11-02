# Dokumentacja urochomieniowa



# agent 
Branch odpowiada za tworzenie wersji oporgramowania
``` c
git branch // wyswietla liste posiadanych wersji
git branch "ver1.1" // stworzenie wersji programu o nazwie 1.1
git checkout "ver1.1" // wybieranie branch'a który ma być aktywny

git branch -d "ver1.1" // usuwa branch o nazwie ver1.1
```

# Commit
Wprowadzanie zmian do GitHub'a
``` c
git add . //bierze wszystkie ścieżki pod uwagę, używać przy tworzeniu nowych plików
git commit -m "opis aktualizacji" -a //parametra -a tyczy się wszyskich plików
git push //wprowadzanie wszystkich zmian lokalnych do hostingu github'a
```
# pull
pobieranie aktualizacji do plikow lokalnych z hostingu github'a
``` c
git pull 
```
