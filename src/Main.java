public class Main {
    public static final int DIMENSIONS = 16; // For a 15x15 puzzle grid 16 is given since an extra column and row is used as a wall of end-of-line
    
    // Set of words
    public static String[] words = {
            "MÜ", "ŞA",
            "ERK", "AYN", "Şİİ", "İLA", "MAİ", "TİZ", "KİK", "KÜR",
            "IŞIN", "ZEKİ",
            "ALKIM", "İHLAS", "OLEİK", "HERTZ", "BUNMA", "ETKEN", "LAMBA",
            "EDİMSEL", "TAKMAAD",
            "REASÜRANS",
            "ARDIARDINA", "TAKIRTUKUR",
            "SUNİSOLUNUM", "AÇIKORDUGAH",
            "GÜNLÜKDEFTER",
            "ROMENRAKAMLARI",
            "SİGARATİRYAKİSİ", "NEFESLİÇALGILAR", "SENDİKALAŞTIRMA"
    };

    // Puzzle grid. Cells are marked with a '.' for empty spaces
    // and a '#' for black blocks.
    public static String[] grid = {
            "...............#",
            ".#.#.#.#.#.##.##",
            ".#.#.....#....##",
            ".....#.#...##.##",
            ".#.#.#...#.....#",
            ".#...#.#.#.##.##",
            ".#.#..........##",
            ".#.#.###.#.##.##",
            "...............#",
            ".#.##.##.#.##.##",
            ".#.......#.....#",
            "##.##.##.##.#.##",
            "..##..........##",
            ".#...#.#.##.#.##",
            "...#....#.....##",
            "################"};

    public static void main(String[] args) {
        // Start GUI
        GUI.invokeGUI();
    }
}