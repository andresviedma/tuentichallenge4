package com.andresviedma.tuenticontest2014.challenge3;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.LineNumberReader;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import com.google.common.base.Splitter;
import com.google.common.primitives.Doubles;


public class GamblersCalculator {
    
    private Interpolation interpolation = null;
    
    
    public GamblersCalculator() {
    }

    
    public void loadData() throws IOException {
        File fich = new File("src/main/resources/com/andresviedma/tuenticontest2014/challenge3/monkeydata.txt");
        LineNumberReader in = new LineNumberReader (new FileReader(fich));
        String linea = in.readLine();
        List<Double> x = new ArrayList<Double>();
        List<Double> y = new ArrayList<Double>();
        List<Double> z = new ArrayList<Double>();
        while ((linea != null) && (linea.length() > 0)) {
            List<String> vals = Splitter.on(';').trimResults().splitToList(linea);
            x.add (Double.parseDouble(vals.get(0)));
            y.add (Double.parseDouble(vals.get(1)));
            z.add (Double.parseDouble(vals.get(2)));
            linea = in.readLine();
        }
        in.close();
        this.interpolation = new Interpolation(Doubles.toArray(x), Doubles.toArray(z), Doubles.toArray(y), true);
    }
    
    public BigDecimal calculate (int x, int y) {
        double z = this.interpolation.linearInterpolation3d(x, y);
        BigDecimal res = new BigDecimal(z);
        if (res.scale() > 2) {
            res = res.setScale(2, BigDecimal.ROUND_HALF_UP).stripTrailingZeros();
        }
        return res;
    }

    
    
    public static void main(String[] args) throws Exception {
        GamblersCalculator calc = new GamblersCalculator();
        calc.loadData();
        
        File fich = new File("src/main/resources/com/andresviedma/tuenticontest2014/challenge3/monkeydata_check.txt");
        PrintWriter out = new PrintWriter(fich);
        
        for (int x=0; x<=30; x++) {
            for (int y=0; y<=30; y++) {
                System.out.print("*");
                BigDecimal res = calc.calculate(x, y);
                out.println (x + ";" + y + ";" + res);
            }
            System.out.println();
        }
        
        out.close();
        System.out.println("OK!");
    }
}
