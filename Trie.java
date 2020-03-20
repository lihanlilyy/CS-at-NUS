import java.util.ArrayList;
public class Trie {
    // Wildcards
    final char WILDCARD = '.';
    final char STAR = '*';
    final char QUESTION = '?';
    final char PLUS = '+';
    
    private class TrieNode {
        TrieNode[] arr;
        boolean isEnd;
        public TrieNode(){
            this.arr = new TrieNode[62];
            isEnd = false;
        }
    }
    private TrieNode root;
    int counterrr = 0;
    
    public Trie() {
        root = new TrieNode();
    }
    //convert to an array of size 62 instead of 256 (ascii). 
    int indexConversion(char c){
        if(c - '0' >= 0 && c - '0' <= 9){
            //34234 numbers;
            return c - '0';
        } else if (c - '0' >= 49 && c - '0' <= 74){
            //small letter
            return c - '0' - 13;
        } else if((c - '0' >= 17 && c - '0' <= 42)){
            //BIGLETTER
            return c - '0' - 7;
        } else {
            return -1;
        }
    }
//convert index from 0 - 62 to ascii
    int numberConversion(int a){
        if(a >= 0 && a <= 9){
            return a + 48;
        } else if (a >= 10 && a <= 35){
            return a + 55;
        } else {
            return a + 61;
        }
    }
    // inserts string s into the Trie
    void insert(final String s) {
        TrieNode p = root;
        for(int i = 0; i < s.length(); i++){
            char c = s.charAt(i);
            int index = indexConversion(c);
            if(p.arr[index] == null){
                TrieNode temp = new TrieNode();
                p.arr[index] = temp;
                p = temp;
            } else {
                p = p.arr[index];
            }
        }
        p.isEnd = true;
    }
    // checks whether string s exists inside the Trie or not
    boolean contains(final String s) {
        TrieNode p = searchNode(s);
        if(p == null){
            return false;
        }
         else{
            if(p.isEnd){
                return true;
            } else {
                return false;
            }
        }
    }
    public TrieNode searchNode(String s){
        TrieNode p = root;
        for(int i = 0; i < s.length(); i++){
            char c = s.charAt(i);
            int index = indexConversion(c);
            if(p.arr[index] != null){
                p = p.arr[index];
            }else{
                return null;
            }
        }
 
        if(p==root && s.length() != 0 ){
            return null;
        } else {
            return p;
        }
    }
    void prefixSearch(String s, ArrayList<String> results, int limit) {
        traversal(this.root, s, "", results, limit, 0);
    }
    void traversal(TrieNode roundBall, String s, String word, ArrayList<String> results, int limit, int pointer) {
        if (s.length() == pointer) {
            if (results.size() < limit && roundBall.isEnd) {
                boolean ok = true;
                for(String str: results){
                    if(str == word){
                        ok = false;
                        break;
                    }
                }
                if(ok){
                    results.add(word);
                    System.out.println(word);
                }
            }
            for (int q = 0; q < 62; q++) {
                if (roundBall.arr[q] != null) {
                    StringBuilder currentWord = new StringBuilder();
                    currentWord = currentWord.append(word);
                    currentWord = currentWord.append(Character.toString((char)(numberConversion(q))));
                    String add = currentWord.toString();
                    traversal(roundBall.arr[q], s, add, results, limit, pointer);
                }
                if (results.size() == limit) {
                    break;
                }
            }
        } else {
            if (s.charAt(pointer) == '.') {
                for (int t = 0; t < 62 ; t++) {
                    if (roundBall.arr[t] != null) {
                        StringBuilder currentWord = new StringBuilder();
                        currentWord = currentWord.append(word);
                        currentWord = currentWord.append(Character.toString((char)(numberConversion(t))));
                        String add = currentWord.toString();
                        traversal(roundBall.arr[t], s, add, results, limit, pointer + 1);
                    }
                }
            } else {
                    int index = indexConversion(s.charAt(pointer));
                    if (roundBall.arr[index] != null) {
                        StringBuilder currentWord = new StringBuilder();
                        currentWord = currentWord.append(word);
                        currentWord = currentWord.append(Character.toString((char)(numberConversion(index))));
                        String add = currentWord.toString();
                        traversal(roundBall.arr[index], s, add, results, limit, pointer + 1);
                    }
                }
            }
        }
    
    // Search for string matching the specified pattern.
    // Return results in the specified ArrayList.
    // Only return at most limit results.
    void patternSearch(final String s, final ArrayList<String> results, final int limit) {
        traversal2(this.root, s, "", results, limit, 0);
    }
    void traversal2(TrieNode roundBall, String s, String word, ArrayList<String> results, int limit, int pointer) {
        if (s.length() == pointer) {
            if (results.size() < limit && roundBall.isEnd) {
                boolean ok = true;
                for(String a: results){
                    if (a.equals(word)){
                        ok = false;
                        break;
                    }
                }
                if(ok){
                    System.out.println("added  " + word);
                    results.add(word);
                }
            }
        } else {
             if(pointer < s.length() - 1 && s.charAt(pointer + 1) == '*'){
                StringBuilder strWithoutStar = new StringBuilder(s);
                strWithoutStar.replace(pointer, pointer + 2, "");
                String newS = strWithoutStar.toString();
                //System.out.println("newS  " + newS);
                traversal2(roundBall, newS, word, results, limit, pointer);
                StringBuilder strWithStar = new StringBuilder(s);
                strWithStar.replace(pointer + 1, pointer + 2, "+");
                String newS2 = strWithStar.toString();
                //System.out.println("newS2  " + newS2);
                traversal2(roundBall, newS2, word, results, limit, pointer);
            } else if(pointer < s.length() - 1 && s.charAt(pointer + 1) == '+'){
                StringBuilder strWithoutPlus = new StringBuilder(s);
                strWithoutPlus.replace(pointer + 1, pointer + 2, "");
                String newS = strWithoutPlus.toString();
                //System.out.println("newS  " + newS);
                traversal2(roundBall, newS, word, results, limit, pointer);
                StringBuilder strWithPlus = new StringBuilder(s);
                String letterAtPointer = Character.toString(s.charAt(pointer));
                strWithPlus.replace(pointer + 1, pointer + 2, letterAtPointer + "+");
                String newS2 = strWithPlus.toString();
                //System.out.println("newS2  " + newS2);
                traversal2(roundBall, newS2, word, results, limit, pointer);
            } else if(pointer < s.length() - 1 && s.charAt(pointer + 1) == '?'){
                StringBuilder strWithoutQnandPrev = new StringBuilder(s);
                //System.out.println("strWithoutQnandPrev  " + strWithoutQnandPrev);
                strWithoutQnandPrev.replace(pointer, pointer + 2, "");
                String newS = strWithoutQnandPrev.toString();
                //System.out.println("newS  " + newS);
                traversal2(roundBall, newS, word, results, limit, pointer);
                StringBuilder strWithoutQn = new StringBuilder(s);
                strWithoutQn.replace(pointer + 1, pointer + 2, "");
                String newS2 = strWithoutQn.toString();
                System.out.println("newS2  " + newS2);
                traversal2(roundBall, newS2, word, results, limit, pointer);
            } else if (s.charAt(pointer) == '.') {
                for (int t = 0; t < 62 ; t++) {
                    if (roundBall.arr[t] != null) {
                        StringBuilder currentWord = new StringBuilder();
                        currentWord = currentWord.append(word);
                        currentWord = currentWord.append(Character.toString((char)(numberConversion(t))));
                        String add = currentWord.toString();
                        traversal2(roundBall.arr[t], s, add, results, limit, pointer + 1);
                    }
                }
            } else {
                    int index = indexConversion(s.charAt(pointer));
                    if (roundBall.arr[index] != null) {
                        StringBuilder currentWord = new StringBuilder();
                        currentWord = currentWord.append(word);
                        currentWord = currentWord.append(Character.toString((char)(numberConversion(index))));
                        String add = currentWord.toString();
                        traversal2(roundBall.arr[index], s, add, results, limit, pointer + 1);
                    }
                }
            }
        }
    // Simplifies function call by initializing an empty array to store the results.
    // PLEASE DO NOT CHANGE the implementation for this function as it will be used
    // to run the test cases.
    String[] prefixSearch(final String s, final int limit) {
        final ArrayList<String> results = new ArrayList<String>();
        prefixSearch(s, results, limit);
        return results.toArray(new String[0]);
    }
    // Simplifies function call by initializing an empty array to store the results.
    // PLEASE DO NOT CHANGE the implementation for this function as it will be used
    // to run the test cases.
    String[] patternSearch(final String s, final int limit) {
        final ArrayList<String> results = new ArrayList<String>();
        patternSearch(s, results, limit);
        return results.toArray(new String[0]);
    }
    public static void main(final String[] args) {
        final Trie t = new Trie();
        t.insert("peter");
        t.insert("piper");
        t.insert("picked");
        t.insert("a");
        t.insert("peck");
        t.insert("of");
        t.insert("pickled");
        t.insert("peppers");
        t.insert("pepppito");
        t.insert("pepi");
        t.insert("piiik");
        t.insert("piik");
        t.insert("pik");
        t.insert("pk");
        t.insert("pd");
        t.insert("p");
        t.insert("nAcc");
        t.insert("naaaa");
        t.insert("");
        t.insert("p");
        t.insert("pp");
        t.insert("ppp");
        t.insert("pppp");
        t.insert("ppppp");
        t.insert("pppppp");
        t.insert("ppppppp");
        t.patternSearch("p*ppp*p", 10);
        //final String[] result2 = t.patternSearch("pe.*", 10);
        //System.out.println(t.contains("A12a"));
        // result1 should be:
        // ["peck", "pepi", "peppers", "pepppito", "peter"]
        // result2 should contain the same elements with result1 but may be ordered arbitrarily
    }
}