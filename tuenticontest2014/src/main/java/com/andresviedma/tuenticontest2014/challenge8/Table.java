package com.andresviedma.tuenticontest2014.challenge8;


public class Table {
    private String cells;
    private int size = -1;
    
    public Table(char[][] data) {
        StringBuilder buf = new StringBuilder();
        for (int i=0; i<data.length; i++)  buf.append(data[i]);
        this.cells = buf.toString();
        this.size = data.length;
    }
    
    public Table(char[] data, int size) {
        this.cells = new String(data);
        this.size = size;
    }
    
    public Table swap (char a, char b) {
        int posA = cells.indexOf(a);
        int posB = cells.indexOf(b);
        char[] arr = this.cells.toCharArray();
        arr[posA] = b;
        arr[posB] = a;
        Table res = new Table(arr, this.size);
        return res;
    }

    @Override
    public int hashCode() {
        return cells.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null || !(obj instanceof Table))  return false;
        return cells.equals(((Table) obj).cells);
    }

    public int getTableSize() {
        return this.size;
    }
    
    public char getCell(int x, int y) {
        return this.cells.charAt(x * this.size + y);
    }

    @Override
    public String toString() {
        return this.cells;
    }
    
    public boolean equalsRow(Table t2, int i) {
        int base = this.size * i;
        return this.cells.substring(base, base + size)
                .equals(t2.cells.substring(base, base + size));
    }
    
    public boolean equalsColumn(Table t2, int i) {
        return (this.cells.charAt(i) == t2.cells.charAt(i))
                && (this.cells.charAt(size + i) == t2.cells.charAt(size + i))
                && (this.cells.charAt(2*size + i) == t2.cells.charAt(2*size + i));
    }
    
    public boolean isCrossPosition(int i, int j) {
        return (i + j == 1) || (i + j == 3); 
    }
    public boolean isCenterPosition(int i, int j) {
        return (i == 1) && (j == 1); 
    }
    public boolean isCornerPosition(int i, int j) {
        return !isCenterPosition(i, j) && !isCrossPosition(i, j); 
    }
    
    public int[] getPosition(char c) {
        int i = this.cells.indexOf(c);
        return new int[] {i / size, i % size};
    }
}
