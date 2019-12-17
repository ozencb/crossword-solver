# Crossword Puzzle Solver

This horrible spaghetti simply attempts to fill a crossword puzzle grid with given set of words. I strongly discourage anyone looking at it, let alone using it. You would be better off finding something algorithmic.

The algorithm is completely heuristic. I did not use a back-tracking algorithm or something like that, so it probably is not very optimal. The sample grid and word set I put in works fine but I did not try another sample. I was too afraid that it everything would break and I would not have the false sense of accomplishment.

The first priority of the algorithm is to find unique paths, paths that are 100% correct, and if there is none, it tries pushing in a random word selected from the word list, then tries finding unique paths again and this cycle continues until the puzzle is solved.