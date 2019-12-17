import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

public class PuzzleSolver {
    private final static int DIMENSIONS = Main.DIMENSIONS;
    private static String[] words;
    private static String[] grid;
    private static ArrayList<String> wordList;
    private static ArrayList<String> gridMap;
    private static ArrayList<String> ignoredSpaces = new ArrayList<>();

    public static char[][] solution;


    // Constructor sets word and grid data.
    // Also populates wordList ArrayList and solution (which has only dots and octothorpes) ArrayList
    public PuzzleSolver(String[] tmpWords, String[] tmpGrid) {
        words = tmpWords;
        grid = tmpGrid;
        solution = prepareSolution();
        wordList = new ArrayList<>(Arrays.asList(words));
    }

    // Main solver function.
    // It keeps attempting to solve the puzzle as long as it is unsolved.
    // At each loop, it rebuilds the solution and wordList again.
    public void solvePuzzle() {
        while (checkSolved() == false) {
            solution = prepareSolution();
            wordList = new ArrayList<>(Arrays.asList(words));
            updateGridMap();
            for (int i = 0; i < 50; i++) {
                String chosenSpace = findUniqueInArray(gridMap);
                String chosenWord = "";
                if (chosenSpace != null) {
                    chosenWord = findWord(chosenSpace);
                } else {
                    chosenWord = findRandomWord();
                }

                if (chosenWord != null) {
                    if (findSpace(chosenWord) == false && chosenSpace != null) {
                        ignoredSpaces.add(chosenWord);
                    }
                    updateGridMap();
                } else {
                    updateGridMap();
                }
            }
        }
    }

    // Takes a string of space data (consists of dots and previously placed characters)
    // and finds a word suitable for that space.
    // For instance, if the function takes "".o..n..e" and there is word of "johndoe"
    // in the word list, the two matches correctly and the word johndoe is returned back
    // to be placed in that space.
    private static String findWord(String space) {
        for (String word : wordList) {
            int matchingWordCounter = 0;
            if (word.length() == space.length()) {
                for (int i = 0; i < word.length(); i++) {
                    if (space.charAt(i) == '.' || word.charAt(i) == space.charAt(i)) {
                        matchingWordCounter++;
                    }
                }
                if (matchingWordCounter == word.length()) {
                    return word;
                }
            }
        }
        return null;
    }

    // Finds a random word from the arraylist.
    // This function is only called if findWord cannot find a word
    // and the only option is to choose a random one
    private static String findRandomWord() {
        Random randomGenerator = new Random();
        if (wordList.size() > 0) {
            return wordList.get(randomGenerator.nextInt(wordList.size()));
        }
        return null;
    }

    // Finds a suitable place for the given word.
    // Scans horizontally then vertically.
    private static boolean findSpace(String word) {
        if (checkHorizontally(word) == true) {
            return true;
        } else return checkVertically(word) == true;
    }

    // Tries to find a horizontal space for the given word
    private static boolean checkHorizontally(String word) {
        int spaceCounter = 0;
        int characterCounter = 0;
        int tmpSpaceCounter;
        int tmpCharacterCounter;

        for (int row = 0; row < DIMENSIONS; row++) {
            for (int col = 0; col < DIMENSIONS; col++) {
                if (solution[row][col] == '.' || Character.isAlphabetic(solution[row][col])) {
                    if (Character.isAlphabetic(solution[row][col])) {
                        characterCounter++;
                    }
                    spaceCounter++;
                } else if (solution[row][col] == '#') {
                    tmpSpaceCounter = spaceCounter;
                    tmpCharacterCounter = characterCounter;
                    spaceCounter = 0;
                    characterCounter = 0;
                    if (tmpSpaceCounter == word.length() && tmpSpaceCounter != tmpCharacterCounter) {
                        return placeWord("H", tmpSpaceCounter, row, col, word) == true;
                    }
                }
            }
            spaceCounter = 0;
            characterCounter = 0;
        }
        return false;
    }

    // Tries to find a vertical space for the given word
    private static boolean checkVertically(String word) {
        int spaceCounter = 0;
        int characterCounter = 0;
        int tmpSpaceCounter;
        int tmpCharacterCounter;

        for (int col = 0; col < DIMENSIONS; col++) {
            for (int row = 0; row < DIMENSIONS; row++) {
                if (solution[row][col] == '.' || Character.isAlphabetic(solution[row][col])) {
                    if (Character.isAlphabetic(solution[row][col])) {
                        characterCounter++;
                    }
                    spaceCounter++;
                } else if (solution[row][col] == '#') {
                    tmpSpaceCounter = spaceCounter;
                    tmpCharacterCounter = characterCounter;
                    spaceCounter = 0;
                    characterCounter = 0;
                    if (tmpSpaceCounter == word.length() && tmpSpaceCounter != tmpCharacterCounter) {
                        return placeWord("V", tmpSpaceCounter, row, col, word) == true;
                    }
                }
            }
            spaceCounter = 0;
            characterCounter = 0;
        }
        return false;
    }

    // Places the given word in given orientation and coordinates.
    // Is called by either checkHorizontally() or checkVertically()
    private static boolean placeWord(String orientation, int length, int row, int col, String word) {
        char[] currentWordArray = word.toCharArray();
        int matchingWordCounter = 0;

        if (orientation.equals("H")) {
            col -= word.length();
            for (int i = 0; i < length; i++) {
                if (solution[row][col + i] == currentWordArray[i] || solution[row][col + i] == '.') {
                    matchingWordCounter++;
                }
            }
            if (matchingWordCounter == length) {
                for (int i = 0; i < length; i++) {
                    solution[row][col + i] = currentWordArray[i];
                }
                popElement(word);
                return true;
            }
        }

        if (orientation.equals("V")) {
            row -= word.length();
            for (int i = 0; i < length; i++) {
                if (solution[row + i][col] == currentWordArray[i] || solution[row + i][col] == '.') {
                    matchingWordCounter++;
                }
            }
            if (matchingWordCounter == length) {
                for (int i = 0; i < length; i++) {
                    solution[row + i][col] = currentWordArray[i];
                }
                popElement(word);
                return true;
            }
        }
        return false;
    }

    // Initializes and updates gridMap array, which stores the
    // space info as dots and letters.
    private static void updateGridMap() {
        PuzzleSolver.gridMap = mapGrid(PuzzleSolver.solution);
    }

    // Scans the puzzle area and maps the areas where a word can be placed in.
    // Returned array initially looks like this:
    // {".......", "....", ".........", ".......", "..."}
    // but then is updated by updateGridMap() after a word is placed
    // so dots can be updated with letters.
    // If a word is successfully placed and the space is completely filled in,
    // its info is not included in the array.
    private static ArrayList<String> mapGrid(char[][] solution) {
        ArrayList<String> map = new ArrayList<>();
        String space;

        for (int row = 0; row < DIMENSIONS; row++) {
            space = "";
            for (int column = 0; column < DIMENSIONS; column++) {
                if (solution[row][column] != '#') {
                    space += solution[row][column];
                } else if (solution[row][column] == '#') {
                    if (space.length() > 1 && space.contains(".")) {
                        map.add(space);
                    }
                    space = "";
                }
            }
        }
        for (int column = 0; column < DIMENSIONS; column++) {
            space = "";
            for (int row = 0; row < DIMENSIONS; row++) {
                if (solution[row][column] != '#') {
                    space += solution[row][column];
                } else if (solution[row][column] == '#') {
                    if (space.length() > 1 && space.contains(".")) {
                        map.add(space);
                    }
                    space = "";
                }
            }
        }
        return map;
    }
    
    // Looks at gridMap array and tries to find unique values.
    private static String findUniqueInArray(ArrayList<String> gridMap) {
        for (int i = 0; i < gridMap.size(); i++) {
            boolean isUnique = true;
            for (int j = 0; j < gridMap.size(); j++) {
                if (gridMap.get(i).equals(gridMap.get(j)) && i != j) {
                    isUnique = false;
                }
            }
            if (isUnique && !Arrays.asList(PuzzleSolver.ignoredSpaces).contains(gridMap.get(i)) && compareUnique(gridMap.get(i))) {
                return gridMap.get(i);
            }
        }
        return null;
    }

    public static boolean compareUnique(String gridSpace) {
        ArrayList<String> tmp = new ArrayList<>();
        for (String item : PuzzleSolver.wordList) {
            if (item.length() == gridSpace.length()) {
                tmp.add(item);
            }
        }
        for (int i = 0; i < tmp.size(); i++) {
            for (int j = 0; j < gridSpace.length(); j++) {
                if (tmp.get(i).charAt(j) == gridSpace.charAt(j)) {
                    return false;
                }
            }
        }
        return true;
    }

    // Returns true if there is not any empty space left
    private static boolean checkSolved() {
        for (int i = 0; i < DIMENSIONS; i++) {
            for (int j = 0; j < DIMENSIONS; j++) {
                if (PuzzleSolver.solution[i][j] == '.') {
                    return false;
                }
            }
        }
        return true;
    }

    // Used to print solution array
    public void printSolution() {
        for (int i = 0; i < DIMENSIONS; i++) {
            for (int j = 0; j < DIMENSIONS; j++) {
                System.out.print(solution[i][j] + " ");
            }
            System.out.println();
        }
    }

    // Fills in solution char[][] array by looking at grid info
    public static char[][] prepareSolution() {
        char[][] solution = new char[DIMENSIONS][DIMENSIONS];
        for (int i = 0; i < DIMENSIONS; i++) {
            for (int j = 0; j < DIMENSIONS; j++) {
                solution[i][j] = grid[i].charAt(j);
            }
        }
        return solution;
    }

    // Pops the element from wordList ArrayList after it is successfully placed
    // so that it may not be used again
    private static void popElement(String element) {
        for (int i = 0; i < PuzzleSolver.wordList.size(); i++) {
            if (PuzzleSolver.wordList.get(i).equals(element)) {
                PuzzleSolver.wordList.remove(PuzzleSolver.wordList.get(i));
            }
        }
    }
}
