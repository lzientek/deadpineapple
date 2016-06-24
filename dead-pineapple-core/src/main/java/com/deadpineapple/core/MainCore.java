package com.deadpineapple.core;

/**
 * Created by 15256 on 12/02/2016.
 */
public class MainCore {
    static int contFailed = 0;

    public static void main(String[] args) {
        try {
            ConversionLauncher conversionLauncher;
            if(args.length > 0){
                conversionLauncher = new ConversionLauncher(args[0]);
            }else {
                conversionLauncher = new ConversionLauncher();
            }

            conversionLauncher.start();
        } catch (Exception ex) {
            contFailed++;
            ex.printStackTrace();
            if (contFailed < 4) {
                main(args);
            }else {
                //envoyer un mail a l'admin!
            }

        }
    }
}
